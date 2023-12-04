package org.gozantes.strava.google;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.security.Security;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@ComponentScan (basePackages = "org.gozantes.strava.server")
@PropertySource ("classpath:google.properties")
public class Main {
    public static void main(String[] args) throws URISyntaxException, NoSuchAlgorithmException {
        Security.init ();

        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runConsole() {
        return (args) -> {
            Logger.getLogger ().info("Google Server started.");
        };
    }
}
