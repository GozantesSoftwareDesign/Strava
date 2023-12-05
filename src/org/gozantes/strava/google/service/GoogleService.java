package org.gozantes.strava.google.service;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@Service
public final class GoogleService {
    public static GoogleService instance;

    private static String DATABASE_FILE = "db/google.sql";

    private EntityManagerFactory emf;
    private final Map <String, String> users = new HashMap <String, String> ();

    private GoogleService () {
        super ();

        emf = Persistence.createEntityManagerFactory ("Google");
    }

    public GoogleService getInstance () {
        return GoogleService.instance == null
                ? (GoogleService.instance = new GoogleService ())
                : GoogleService.instance;
    }

    private void initUsers () {
        Logger.getLogger ().info ("Getting Google users...");

    }

    public boolean validate (UserCredentials user) {
        return user != null && this.users.containsKey (user.id ()) && this.users.get (user.id ())
                .equals (user.passwd ());
    }

    public void storeUsers (UserCredentials user) throws Exception {
        this.storeUsers (new UserCredentials[] { user });
    }

    public void storeUsers (UserCredentials[] users) throws Exception {
        EntityManager em = emf.createEntityManager ();
        EntityTransaction tx = em.getTransaction ();

        Map <String, String> temp = new HashMap <> ();
        UserCredentials u = null;

        try {
            tx.begin ();

            for (UserCredentials x : users) {
                u = x;

                if (x == null)
                    throw new Exception ("Null users are not allowed.");

                if (!x.type ().equals (CredType.Google))
                    throw new Exception ("Non-Google users are not allowed.");

                em.persist (x);
                temp.put (x.id (), x.passwd ());
            }

            tx.commit ();
            this.users.putAll (temp);

            Logger.getLogger ().info (String.format ("%d users stored successfully.", users.length));
        }
        catch (Exception e) {
            Logger.getLogger ().warning (
                    String.format ("Could not store user %s: %s", u == null ? "(null)" : u.toString (),
                            e.getMessage ()));
        }

        finally {
            if (tx != null && tx.isActive ())
                tx.rollback ();

            em.close ();
        }
    }

    public boolean exists (String id) {
        return this.users.containsKey (id);
    }

    public void signup (UserCredentials user) throws Exception {
        if (user == null)
            throw new Exception ("Null users cannot sign up.");

        if (this.users.containsKey (user.id ()))
            throw new Exception ("User already exists.");

        this.storeUsers (user);
    }
}
