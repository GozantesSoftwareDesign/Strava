package org.gozantes.strava.server.data.domain.auth;

import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public record UserCredentials(@Id CredType type, @Id String id, String passwd) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


	public UserCredentials (CredType type, String id) {
        this (type, id, null);
    }

    public UserCredentials (CredType type, String id, String passwd) {
        this.type = Objects.requireNonNull (type);
        this.id = Objects.requireNonNull (id);
        this.passwd = passwd;
    }

    public UserCredentials (UserCredentials creds) {
        this (creds.type, creds.id, creds.passwd);
    }

    public CredType type () {
        return this.type;
    }

    public String id () {
        return this.id;
    }

    public String passwd () {
        return this.passwd;
    }
    @Override
	public String toString() {
		return "UserCredentials [type=" + type + ", id=" + id + ", passwd=" + passwd + "]";
	}
}
