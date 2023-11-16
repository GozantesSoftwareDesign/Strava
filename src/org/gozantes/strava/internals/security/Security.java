package org.gozantes.strava.internals.security;

import org.gozantes.strava.server.Main;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public final class Security {
    @SuppressWarnings ({ "removal", "deprecation" })
    public static void init () throws URISyntaxException, NoSuchAlgorithmException {
        java.security.Policy customPolicy = java.security.Policy.getInstance ("JavaPolicy",
                new java.security.URIParameter (
                        Objects.requireNonNull (Main.class.getClassLoader ().getResource ("java.policy")).toURI ()));
        java.security.Policy.setPolicy (customPolicy);
        java.security.Security.setProperty ("policy.provider", "com.sun.security.provider.PolicyFile");
        java.security.Policy.getPolicy ().refresh ();

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
    }
}
