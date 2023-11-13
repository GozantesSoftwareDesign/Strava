package org.gozantes.strava.server.services.auth;

import org.gozantes.strava.server.data.dao.UserDAO;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

public final class SignupAppService {
    private static SignupAppService instance;

    private SignupAppService() {
    }

    public static SignupAppService getInstance() {
        if (SignupAppService.instance == null) SignupAppService.instance = new SignupAppService();

        return SignupAppService.instance;
    }

    public User signup(UserCredentials creds) {
        User u = UserDAO.getInstance().find(creds.id());

        if (u == null)
            return null;

        return u;
    }
}
