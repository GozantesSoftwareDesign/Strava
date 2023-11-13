package org.gozantes.strava.server.data.domain.auth;

import java.util.Objects;

public record UserCredentials(CredType type, String id, String passwd) {
    public UserCredentials(CredType type, String id, String passwd) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(id);
        Objects.requireNonNull(passwd);

        this.type = type;
        this.id = id;
        this.passwd = passwd;
    }

    public UserCredentials(UserCredentials creds) {
        this(creds.type, creds.id, creds.passwd);
    }

    public CredType type() {
        return this.type;
    }

    public String id() {
        return this.id;
    }

    public String passwd() {
        return this.passwd;
    }
}
