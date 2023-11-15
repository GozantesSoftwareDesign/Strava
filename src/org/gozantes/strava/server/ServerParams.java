package org.gozantes.strava.server;

import org.w3c.dom.ranges.RangeException;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public record ServerParams(String name, Inet4Address ip, int port) {
    public static ServerParams defaults;

    static {
        try {
            defaults = new ServerParams ("StravaServer", (Inet4Address) Inet4Address.getByName ("127.0.0.1"), 1099);
        }
        catch (UnknownHostException e) {
            throw new RuntimeException (e);
        }
    }

    public ServerParams () {
        this (ServerParams.defaults);
    }

    public ServerParams (String name) {
        this (name, ServerParams.defaults.ip, ServerParams.defaults.port);
    }

    public ServerParams (String name, String ip) throws UnknownHostException {
        this (name, ip == null ? ServerParams.defaults.ip : (Inet4Address) Inet4Address.getByName (ip),
                ServerParams.defaults.port);
    }

    public ServerParams (String name, Inet4Address ip) {
        this (name, ip, ServerParams.defaults.port);
    }

    public ServerParams (String name, String ip, String port) throws UnknownHostException, NumberFormatException {
        this (name, ip == null ? ServerParams.defaults.ip : (Inet4Address) Inet4Address.getByName (ip),
                port == null ? ServerParams.defaults.port : Integer.parseInt (port));
    }

    public ServerParams (String name, String ip, int port) throws UnknownHostException {
        this (name, (Inet4Address) Inet4Address.getByName (ip), port);
    }

    public ServerParams (String name, Inet4Address ip, int port) {
        this.name = name == null ? ServerParams.defaults.name : name;
        this.ip = ip;
        this.port = port;

        if (this.port < 0 || this.port > (2 << 15) - 1)
            throw new RangeException (RangeException.BAD_BOUNDARYPOINTS_ERR,
                    "Invalid port (" + Integer.toString (this.port) + ")");
    }

    public ServerParams (ServerParams params) {
        this (params.name, params.ip, params.port);
    }

    public String fullName () {
        return "//" + this.ip.getHostAddress () + ":" + this.port + "/" + this.name;
    }
}
