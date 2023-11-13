package org.gozantes.strava.server.services.auth;

import org.gozantes.strava.internals.hash.SHA1Hasher;
import org.gozantes.strava.server.data.dao.UserDAO;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

public final class LoginAppService {
    private static LoginAppService instance;

    private LoginAppService() {
    }

    public static LoginAppService getInstance() {
        if (LoginAppService.instance == null) LoginAppService.instance = new LoginAppService();

        return LoginAppService.instance;
    }

    public User login(UserCredentials creds) {
        User u = UserDAO.getInstance().find(creds.id());

        if (u == null || !u.getPassword().equals(SHA1Hasher.hash(creds.passwd())))
            return null;

        return u;
    }
}
