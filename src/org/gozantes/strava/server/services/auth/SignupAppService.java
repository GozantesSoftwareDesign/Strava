package org.gozantes.strava.server.services.auth;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.dao.UserDAO;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

public final class SignupAppService {
    private static SignupAppService instance;

    private SignupAppService() {
    }

    public static SignupAppService getInstance() {
        if (SignupAppService.instance == null) SignupAppService.instance = new SignupAppService();

        return SignupAppService.instance;
    }

    public Pair<String, User> signup(UserCredentials creds, UserData data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = CredsValidationAppService.getInstance().validate(creds);

        if (token == null)
            return null;

        Calendar birth = Calendar.getInstance();
        birth.set(2003, 21, 02);

        User u = false ? UserDAO.getInstance().find(creds.id()) : new User(creds, data);

        return u == null ? null : new Pair<String, User>(token, u);
    }
}
