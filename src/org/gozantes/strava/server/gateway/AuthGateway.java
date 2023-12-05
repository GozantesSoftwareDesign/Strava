package org.gozantes.strava.server.gateway;

import org.gozantes.strava.server.data.domain.auth.UserCredentials;

public interface AuthGateway {
    boolean exists (String id);

    boolean validate (UserCredentials user);

    public boolean signup (UserCredentials user);
}
