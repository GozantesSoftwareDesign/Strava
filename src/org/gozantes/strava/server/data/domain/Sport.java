package org.gozantes.strava.server.data.domain;

import java.io.Serializable;

public enum Sport implements Serializable {
    Cyclism ("Cyclism"),
    Running ("Running");

    private String value;

    private Sport (String value) {
        this.value = value;
    }

    @Override
    public String toString () {
        return this.value;
    }
}
