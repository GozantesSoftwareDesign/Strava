package org.gozantes.strava.server.gateway;

import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.gateway.meta.MetaServiceGateway;

import java.util.stream.IntStream;

public final class AuthGatewayFactory {
    public static AuthGateway createAuthGateway (CredType type) throws Exception {
        switch (type) {
            case Google:
                return GoogleServiceGateway.getInstance ();

            case Meta:
                return MetaServiceGateway.getInstance ();

            default:
                throw new Exception (
                        String.format ("Invalid type. AuthGateway types can only be one of the following: " + "[%s]",
                                IntStream.range (0, CredType.class.getEnumConstants ().length).mapToObj (
                                                (i) -> String.format ("%s%s",
                                                        i < CredType.class.getEnumConstants ().length - 1 ? ", " : "",
                                                        CredType.class.getEnumConstants ()[i].toString ()))
                                        .reduce ("", (str, x) -> str + x)));
        }
    }
}
