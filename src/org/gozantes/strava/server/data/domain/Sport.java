package org.gozantes.strava.server.data.domain;

public enum Sport {
    Cyclism ("Cyclism"),
    Running ("Running");

    private String value;

    private Sport(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
