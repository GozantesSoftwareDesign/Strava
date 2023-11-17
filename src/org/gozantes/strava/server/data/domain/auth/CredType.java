package org.gozantes.strava.server.data.domain.auth;

import java.io.Serializable;

public enum CredType implements Serializable {
    Meta ("Meta"),
    Google ("Google");

    private String value;

    private CredType (String value) {
        this.value = value;
    }

    @Override
    public String toString () {
        return this.value;
    }
}
