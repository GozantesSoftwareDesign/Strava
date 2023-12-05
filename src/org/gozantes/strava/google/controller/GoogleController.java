package org.gozantes.strava.google.controller;

import org.gozantes.strava.google.service.GoogleService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleController {
    private GoogleService service;

    public GoogleController(GoogleService service) {
        this.service = service;
    }
}