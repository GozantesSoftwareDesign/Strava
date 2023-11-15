package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionAssembler {
    private static SessionAssembler instance;

    private SessionAssembler () {
    }

    public static SessionAssembler getInstance () {
        if (instance == null) {
            instance = new SessionAssembler ();
        }
        return instance;
    }

    public SessionDTO SessionToDTO (Session session) {
        SessionDTO SessionDTO = new SessionDTO (session.getId (), session.getState (), session.getData ());
        return SessionDTO;
    }

    public List <SessionDTO> SessionsToDTO (List <Session> sessions) {
        List <SessionDTO> dtos = new ArrayList <> ();

        for (Session Session : sessions) {
            dtos.add (this.SessionToDTO (Session));
        }

        return dtos;
    }
}
