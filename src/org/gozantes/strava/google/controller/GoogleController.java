package org.gozantes.strava.google.controller;

import org.gozantes.strava.google.service.GoogleService;
import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public final class GoogleController {
    private GoogleService service;

    public GoogleController (GoogleService service) {
        this.service = service;
    }

    @GetMapping ("/google/{id}")
    public ResponseEntity <?> exists (@PathVariable ("id") String id) {
        return this.service.exists (id)
                ? new ResponseEntity <> (null, HttpStatus.OK)
                : new ResponseEntity <> (null, HttpStatus.NOT_FOUND);
    }

    @PostMapping ("/google")
    public ResponseEntity <?> validate (@RequestBody UserCredentials user) {
        return this.service.validate (user)
                ? new ResponseEntity <> (null, HttpStatus.OK)
                : new ResponseEntity <> (null, HttpStatus.NOT_FOUND);
    }

    @PutMapping ("/google")
    public ResponseEntity <?> signup (@RequestBody UserCredentials user) {
        if (user == null || !user.type ().equals (CredType.Google))
            return new ResponseEntity (null, HttpStatus.BAD_REQUEST);

        try {
            this.service.signup (user);

            Logger.getLogger ().info (String.format ("User %s has successfully signed up.", user));

            return new ResponseEntity (null, HttpStatus.OK);
        }

        catch (Exception e) {
            Logger.getLogger ().warning (String.format ("User %s could not sign up.", user));

            return new ResponseEntity (null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}