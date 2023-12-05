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
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;
import org.gozantes.strava.server.gateway.AuthGatewayFactory;
import org.gozantes.strava.server.gateway.meta.MetaServiceGateway;

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
import java.util.Date;
import java.util.Map;

public final class AuthAppService {
    private static final String GooglePublicKeyStr = "MIIDJzCCAg+gAwIBAgIJAO5q5hCX9S"
            + "+zMA0GCSqGSIb3DQEBBQUAMDYxNDAyBgNV\\nBAMMK2ZlZGVyYXRlZC1zaWdub24uc3lzdGVtLmdzZXJ2aWNlYWNjb3VudC5jb20w"
            + "\\nHhcNMjMxMTA0MDQzODA0WhcNMjMxMTIwMTY1MzA0WjA2MTQwMgYDVQQDDCtmZWRl"
            + "\\ncmF0ZWQtc2lnbm9uLnN5c3RlbS5nc2VydmljZWFjY291bnQuY29tMIIBIjANBgkq"
            + "\\nhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuB+3s136B/Vcme1zGQEg+Avs31/voau8\\nBPKtvbYhB0QOHTtrXCF/wxIH5vWjl"
            + "+5ts8up8Iy2kVnaItsecGohBAy/0kRgq8oi\\n+n/cZ0i5bspAX5VW0peh/QU3KTlKSBaz3ZD9xMCDWuJFFniHuxLtJ4QtL4v2oDD3"
            + "\\npBPNRPyIcZ/LKhH3+Jm+EAvubI5+6lB01zkP5x8f2mp2upqAmyex0jKFka2e0DOB"
            + "\\navmGsGvKHKtTnE9oSOTDlhINgQPohoSmir89NRbEqqzeZVb55LWRl/hkiDDOZmcM\\n/oJ8iUbm6vQu3YwCy"
            + "+ef9wGYEij5GOWLmpYsws5vLVtTE2U+0C/ItQIDAQABozgw\\nNjAMBgNVHRMBAf8EAjAAMA4GA1UdDwEB"
            + "/wQEAwIHgDAWBgNVHSUBAf8EDDAKBggr\\nBgEFBQcDAjANBgkqhkiG9w0BAQUFAAOCAQEAifQankV6Ca4UQ9MvTX4KlsaVV6WR"
            + "\\n1FL2ZwRPHwFQnw3hFJrHKdQBvCS1339G1uguCOi0CQQJmQauSvRureJ/80Fc/j3c"
            + "\\nwEWQgBhuKCHiQbIMFpVoljsVsF91E0FvZ8eJJ5/y7QB3Ww68FavXNjZ62GaYp8aw"
            + "\\nEdqJdqNFaIv7yWzOO27filjzF3H6VJG7ucx0P6JCCCC6HSii3o1lkRISvSTcevqZ"
            + "\\nsFdbJEqtVU70siOHxWxMqRopetiTEAsbvwiicdZ6flZqtnxKqB6YEb6TocWpzGvd"
            + "\\nVKxzByXdPJbyYvAnvusborJZPHKbleZjyonK+cmsOU6N1Yn/FUxKKhFXEg==";
    private static AuthAppService instance;
    private final RSAPublicKey GooglePublicKey;
    private final RSAPrivateKey PrivateKey;

    private AuthAppService () throws NoSuchAlgorithmException, InvalidKeySpecException {
        super ();

        RSAPublicKey gk = null;
        RSAPrivateKey pk = null;

        try {
            gk = (RSAPublicKey) KeyFactory.getInstance ("RSA")
                    .generatePublic (new X509EncodedKeySpec (Base64.getMimeDecoder ().decode (GooglePublicKeyStr)));

            final RSAKeyPairGenerator kpg = new RSAKeyPairGenerator ();
            kpg.init (new KeyGenerationParameters (SecureRandom.getInstance ("SHA1PRNG"), 256));

            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance ("RSA");
            keyPairGenerator.initialize (256);

            pk = (RSAPrivateKey) keyPairGenerator.generateKeyPair ().getPrivate ();
        }
        catch (Exception e) {
            gk = null;
            pk = null;
        }

        this.GooglePublicKey = gk;
        this.PrivateKey = pk;
    }

    public static AuthAppService getInstance () throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (AuthAppService.instance == null)
            AuthAppService.instance = new AuthAppService ();

        return AuthAppService.instance;
    }

    private String googleValidate (UserCredentials creds) {
        if (this.GooglePublicKey == null || this.PrivateKey == null) // Keys are not working right now :(
            return metaValidate (creds);

        String token = null;

        try {
            token = JWT.create ().withPayload (
                            Map.ofEntries (Map.entry ("type", creds.type ().toString ()), Map.entry ("id", creds.id ()),
                                    Map.entry ("passwd", creds.passwd ())))
                    .withExpiresAt (Date.from (Instant.now ().plus (1, ChronoUnit.YEARS)))
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

        assert creds != null;
        if (creds.passwd () == null)
            Logger.getLogger ().severe (new NullPointerException ("Credentials must have a password."));

        try {
            if (AuthGatewayFactory.createAuthGateway (creds.type ()).validate (creds))
                return SHA1Hasher.hash (creds, System.currentTimeMillis ());
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("Could not validate user.");

            return null;
        }

        return null;
    }

    public Pair <String, User> login (UserCredentials creds) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = AuthAppService.getInstance ().validate (creds);

        if (token == null)
            return null;

        String name = "Alfonso María Isabel Francisco Eugenio Gabriel Pedro Sebastián Pelayo Fernando Francisco de "
                + "Paula Pío Miguel Rafael Juan José Joaquín Ana Zacarías Elisabeth Simeón Tereso Pedro Pablo Tadeo "
                + "Santiago Simón Lucas Juan Mateo Andrés Bartolomé Ambrosio Gerónimo Agustín Bernardo Cándido "
                + "Gerardo Luis-Gonzaga Filomeno Camilo Cayetano Andrés-Avelino Bruno Joaquín-Picolimini Felipe "
                + "Luis-Rey-de-Francia Ricardo Esteban-Protomártir Genaro Nicolás Estanislao-de-Koska Lorenzo Vicente"
                + " Crisóstomo Cristano Darío Ignacio Francisco-Javier Francisco-de-Borja Higona Clemente "
                + "Esteban-de-Hungría Ladislado Enrique Ildefonso Hermenegildo Carlos-Borromeo Eduardo "
                + "Francisco-Régis Vicente-Ferrer Pascual Miguel-de-los-Santos Adriano Venancio Valentín Benito "
                + "José-Oriol Domingo Florencio Alfacio Benére Domingo-de-Silos Ramón Isidro Manuel Antonio "
                + "Todos-los-Santos de Borbón y Borbón"; // https://es.wikipedia.org/w/index.php?title=Alfonso_Mar%C3%ADa_de_Borb%C3%B3n_y_Borb%C3%B3n&oldid=154761971

        Calendar birth = Calendar.getInstance ();
        birth.set (2003, Calendar.FEBRUARY, 21);

        User u = false
                ? UserDAO.getInstance ().find (creds.id ())
                : new User (creds, new UserData (name, birth.getTime (), null, null, null));

        return new Pair <> (token, u);
    }

    public Pair <String, User> signup (UserCredentials creds, UserData data)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = AuthAppService.getInstance ().validate (creds);

        if (token == null)
            return null;

        User u = false ? UserDAO.getInstance ().find (creds.id ()) : new User (creds, data);
        MetaServiceGateway.getInstance ().saveUser (u);
        return u == null ? null : new Pair <String, User> (token, u);
    }
}
