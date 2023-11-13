package org.gozantes.strava.server.services.auth;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.dao.UserDAO;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

public final class LoginAppService {
    private static LoginAppService instance;

    private LoginAppService() {
    }

    public static LoginAppService getInstance() {
        if (LoginAppService.instance == null) LoginAppService.instance = new LoginAppService();

        return LoginAppService.instance;
    }

    public Pair<String, User> login(UserCredentials creds) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = CredsValidationAppService.getInstance().validate(creds);

        if (token == null)
            return null;

        Calendar birth = Calendar.getInstance();
        birth.set(2003, 21, 02);

        User u = false ? UserDAO.getInstance().find(creds.id()) : new User(creds, new UserData(null, birth.getTime()));

        return u == null ? null : new Pair<String, User>(token, u);
    }
}
