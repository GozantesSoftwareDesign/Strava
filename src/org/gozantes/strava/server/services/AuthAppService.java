package org.gozantes.strava.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.gozantes.strava.internals.hash.SHA1Hasher;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.dao.UserDAO;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;

public final class AuthAppService {
    private static final String GooglePublicKeyStr = "uB-3s136B_Vcme1zGQEg-Avs31_voau8BPKtvbYhB0QOHTtrXCF_wxIH5vWjl"
            + "-5ts8up8Iy2kVnaItsecGohBAy_0kRgq8oi"
            + "-n_cZ0i5bspAX5VW0peh_QU3KTlKSBaz3ZD9xMCDWuJFFniHuxLtJ4QtL4v2oDD3pBPNRPyIcZ_LKhH3-Jm-EAvubI5"
            +
            "-6lB01zkP5x8f2mp2upqAmyex0jKFka2e0DOBavmGsGvKHKtTnE9oSOTDlhINgQPohoSmir89NRbEqqzeZVb55LWRl_hkiDDOZmcM_oJ8iUbm6vQu3YwCy-ef9wGYEij5GOWLmpYsws5vLVtTE2U-0C_ItQ";
    private static AuthAppService instance;
    private final RSAPublicKey GooglePublicKey;
    private final RSAPrivateKey PrivateKey;

    private AuthAppService () throws NoSuchAlgorithmException, InvalidKeySpecException {
        super ();

        this.GooglePublicKey = (RSAPublicKey) KeyFactory.getInstance ("RSA")
                .generatePublic (new X509EncodedKeySpec (Base64.getDecoder ().decode (GooglePublicKeyStr)));

        RSAKeyPairGenerator kpg = new RSAKeyPairGenerator ();
        kpg.init (new KeyGenerationParameters (SecureRandom.getInstance ("SHA1PRNG"), 256));

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance ("RSA");
        keyPairGenerator.initialize (256);

        this.PrivateKey = (RSAPrivateKey) keyPairGenerator.generateKeyPair ().getPrivate ();
    }

    public static AuthAppService getInstance () throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (AuthAppService.instance == null)
            AuthAppService.instance = new AuthAppService ();

        return AuthAppService.instance;
    }

    private String googleValidate (UserCredentials creds) {
        String token = null;

        try {
            token = JWT.create ().withPayload (
                            Map.ofEntries (Map.entry ("type", creds.type ().toString ()), Map.entry ("id", creds.id ()),
                                    Map.entry ("passwd", creds.passwd ())))
                    .withExpiresAt (Instant.now ().plus (1, ChronoUnit.YEARS))
                    .withAudience ("1234567890-abc123def456.apps.googleusercontent.com")
                    .withIssuer ("accounts.google.com").sign (Algorithm.RSA256 (AuthAppService.instance.GooglePublicKey,
                            AuthAppService.instance.PrivateKey));
        }
        catch (JWTCreationException e) {
            Logger.getLogger ().severe ("JWT creation error: " + e.getMessage (), e);
        }

        return token;
    }

    private String metaValidate (UserCredentials creds) {
        return SHA1Hasher.hash (creds, System.currentTimeMillis ());
    }

    public String validate (UserCredentials creds) {
        if (creds == null)
            Logger.getLogger ().severe (new NullPointerException ("The credentials cannot be null."));

        if (creds.passwd () == null)
            Logger.getLogger ().severe (new NullPointerException ("Credentials must have a password."));

        return creds.type () == CredType.Google ? googleValidate (creds) : metaValidate (creds);
    }

    public Pair <String, User> login (UserCredentials creds) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = AuthAppService.getInstance ().validate (creds);

        if (token == null)
            return null;

        Calendar birth = Calendar.getInstance ();
        birth.set (2003, Calendar.FEBRUARY, 21);

        User u = false
                ? UserDAO.getInstance ().find (creds.id ())
                : new User (creds, new UserData (null, birth.getTime ()));

        return u == null ? null : new Pair <String, User> (token, u);
    }

    public Pair <String, User> signup (UserCredentials creds, UserData data)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = AuthAppService.getInstance ().validate (creds);

        if (token == null)
            return null;

        Calendar birth = Calendar.getInstance ();
        birth.set (2003, Calendar.FEBRUARY, 21);

        User u = false ? UserDAO.getInstance ().find (creds.id ()) : new User (creds, data);

        return u == null ? null : new Pair <String, User> (token, u);
    }
}
