package org.gozantes.strava.server.gateway;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public final class GoogleServiceGateway {
    private static GoogleServiceGateway instance;

    private static final String BASE_URL = "http://127.0.0.1:10000/";
    private final RestTemplate restTemplate;

    private GoogleServiceGateway () {
        super ();

        this.restTemplate = new RestTemplate ();
    }

    public static GoogleServiceGateway getInstance () {
        return GoogleServiceGateway.instance == null
                ? (GoogleServiceGateway.instance = new GoogleServiceGateway ())
                : GoogleServiceGateway.instance;
    }

    public boolean exists (String id) {
        if ((id == null ? "" : id).isBlank ())
            return false;

        try {
            return restTemplate.exchange (BASE_URL + "google/" + id, HttpMethod.GET, null, Boolean.class)
                    .getStatusCode ().is2xxSuccessful ();
        }

        catch (Exception e) {
            Logger.getLogger ().warning (
                    "Could not receive a confirmation of the user's existence from the server: " + e.getMessage ());

            return false;
        }
    }

    public boolean validate (UserCredentials user) {
        try {
            return restTemplate.exchange (BASE_URL + "google", HttpMethod.POST, new HttpEntity <> (user), Boolean.class)
                    .getStatusCode ().is2xxSuccessful ();
        }

        catch (Exception e) {
            Logger.getLogger ().warning ("The server could not validate the user's credentials: " + e.getMessage ());

            return false;
        }
    }

    public boolean signup (UserCredentials user) {
        try {
            return restTemplate.exchange (BASE_URL + "google", HttpMethod.PUT, new HttpEntity <> (user), Boolean.class)
                    .getStatusCode ().is2xxSuccessful ();
        }

        catch (Exception e) {
            Logger.getLogger ().warning (
                    String.format ("Could not perform the signup process for %s %s: ", e.getMessage (),
                            e.getMessage ()));

            return false;
        }
    }
}
