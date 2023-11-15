package org.gozantes.strava.server.data.dao;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.User;

public final class UserDAO extends DAO <User> {
    private static UserDAO instance;

    private UserDAO () throws NoSuchMethodException {
        super (User.class, User.class.getMethod ("getId"));
    }

    public static UserDAO getInstance () {
        if (instance == null) {
            try {
                instance = new UserDAO ();
            }
            catch (NoSuchMethodException e) {
                Logger.getLogger ().severe (e);
            }
        }

        return instance;
    }

    @Override
    protected UserDAO instance () {
        return UserDAO.instance;
    }
}
