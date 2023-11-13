package org.gozantes.strava.server.data.domain.auth;

public enum CredType {
    Meta("Meta"),
    Google("Google");

    private String value;

    private CredType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
