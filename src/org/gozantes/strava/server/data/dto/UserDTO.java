package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.util.Objects;

public record UserDTO(UserCredentials creds, UserData data) {
    public UserDTO (UserCredentials creds, UserData data) {
        this.creds = Objects.requireNonNull (creds);
        this.data = Objects.requireNonNull (data);
    }

    public UserCredentials creds () {
        return creds;
    }

    public UserData data () {
        return data;
    }
}
