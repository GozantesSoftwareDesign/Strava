package org.gozantes.strava.server.services;

import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.session.Session;
import org.gozantes.strava.server.data.domain.session.SessionData;
import org.gozantes.strava.server.data.domain.session.SessionFilters;
import org.gozantes.strava.server.data.domain.session.SessionState;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public final class SessionAppService {
    private final static List <Session> sessions;
    private static SessionAppService instance;
    private static long counter;

    static {
        UserCredentials[] users = new UserCredentials[] {
                new UserCredentials (CredType.Google, "mikel.p@opendeusto" + ".es"),
                new UserCredentials (CredType.Meta, "dnocito@opendeusto.es"),
                new UserCredentials (CredType.Google, "iker.arrien@opendeusto.es"),
                new UserCredentials (CredType.Meta, "gonzalo.pena@opendeusto.es") };

        Calendar c = Calendar.getInstance ();
        c.set (c.get (Calendar.YEAR), c.get (Calendar.MONTH), c.get (Calendar.DATE), 0, 0);

        counter = 0;
        sessions = Arrays.asList (
                new SessionData ("Bici por el monte", Sport.Cyclism, BigDecimal.valueOf (30), c.getTime (),
                        Duration.ofHours (4)),
                new SessionData ("Correr por la ciudad", Sport.Running, BigDecimal.valueOf (5),
                        new Date (Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (1).toMillis ()),
                        Duration.ofHours (1)),
                new SessionData ("Repartirle la comida al Xokas", Sport.Cyclism, BigDecimal.valueOf (100),
                        new Date (Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (30).toMillis ()),
                        Duration.ofHours (4)),
                new SessionData ("Ironman mañanero", Sport.Running, BigDecimal.valueOf (500),
                        new Date (Calendar.getInstance ().getTime ().getTime () - Duration.ofDays (1).toMillis ()),
                        Duration.ofHours (4).plus (Duration.ofMinutes (45)))).stream ().map ((x) -> {
            Session s = new Session (counter, users[(int) counter], x);
            counter++;

            return s;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private SessionAppService () {
        super ();
    }

    public static SessionAppService getInstance () {
        if (SessionAppService.instance == null)
            SessionAppService.instance = new SessionAppService ();

        return SessionAppService.instance;
    }

    public Session create (UserCredentials creds, SessionData data) throws RemoteException {
        if (data == null)
            throw new RemoteException ("The session data cannot be null");

        Session s = new Session (SessionAppService.counter++, creds, data);
        sessions.add (s);

        return s;
    }

    public List <Session> getSessions (SessionFilters filters) {
        return filters != null
                ? SessionAppService.sessions.stream ().filter (
                (x) -> (filters.user () == null || (filters.user ().id ().equals (x.getParent ().id ())
                        && filters.user ().type ().equals (x.getParent ().type ()))) && x.getTitle ().toLowerCase ()
                        .contains (((filters.title () == null ? "" : filters.title ()).strip ()).toLowerCase ()) && (
                        filters.sport () == null || x.getSport ().equals (filters.sport ())) && (
                        filters.distance () == null || filters.distance ().x () == null
                                || filters.distance ().x ().compareTo (x.getDistance ()) <= 0) && (
                        filters.distance () == null || filters.distance ().y () == null
                                || filters.distance ().y ().compareTo (x.getDistance ()) >= 0) && (
                        filters.duration () == null || filters.duration ().x () == null
                                || filters.duration ().x ().compareTo (x.getDuration ()) <= 0) && (
                        filters.duration () == null || filters.duration ().y () == null
                                || filters.duration ().y ().compareTo (x.getDuration ()) >= 0)).toList ()
                : new ArrayList <Session> (SessionAppService.sessions);
    }

    public void setState (UserCredentials creds, long session, SessionState state) throws RemoteException {
        if (creds == null)
            throw new RemoteException ("User credentials cannot be null.");

        if (state == null)
            throw new RemoteException ("The state cannot be null.");

        Optional <Session> s = SessionAppService.sessions.stream ().filter ((x) -> x.getId () == session).findFirst ();

        if (!s.isPresent ())
            throw new RemoteException (String.format ("No such session (%s)", session));

        if (!(s.get ().getParent ().type ().equals (creds.type ()) && s.get ().getParent ().id ().equals (creds.id ())))
            throw new RemoteException ("The requestor is not the same as the session's parent.");

        sessions.get (sessions.indexOf (s.get ())).setState (state);
    }
}
