package org.gozantes.strava.server.data.domain.auth;

import java.io.Serializable;

public enum CredType implements Serializable {
    Meta ("Meta"),
    Google ("Google");

    private String value;

    private CredType (String value) {
        this.value = value;
    }

    static public CredType ParseCredType (String value) {
        if (value == CredType.Meta.value) {
            return CredType.Meta;
        }
        else if (value == CredType.Google.value) {
            return CredType.Google;
        }
        else {
            return null;
        }

    }

    @Override
    public String toString () {
        return this.value;
    }
}
