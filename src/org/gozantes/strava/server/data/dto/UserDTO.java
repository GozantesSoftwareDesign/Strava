package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.util.Objects;

public record UserDTO(UserCredentials creds, UserData data) {
    public UserDTO (UserCredentials creds, UserData data) {
        Objects.nonNull (creds);
        Objects.nonNull (data);

        this.creds = creds;
        this.data = data;
    }

    public UserCredentials creds () {
        return creds;
    }

    public UserData data () {
        return data;
    }
}
