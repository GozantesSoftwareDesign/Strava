package org.gozantes.strava.server.services.session;

import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;

import java.rmi.RemoteException;
import java.util.Map;

public final class SessionAppService {
    private static SessionAppService instance;
    private static long counter = 0;

    private SessionAppService() {
        super();
    }

    public static SessionAppService getInstance() {
        if (SessionAppService.instance == null)
            SessionAppService.instance = new SessionAppService();

        return SessionAppService.instance;
    }

    public Session create(SessionData data) throws RemoteException {
        if (data == null)
            throw new RemoteException("The session data cannot be null");

        return new Session(SessionAppService.counter++, data);
    }

    public Map<Long, Session> getSessions (SessionFilters filters) {
        return null;
    }
}
