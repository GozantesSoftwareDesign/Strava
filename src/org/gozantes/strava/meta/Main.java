package org.gozantes.strava.meta;

import org.gozantes.strava.internals.logging.Logger;

import java.net.ServerSocket;

public class Main {

    public static void main (String[] args) {
        if (args.length < 1) {
            System.err.println (" # Usage: TCPSocketEchoServer [PORT]");
            System.exit (1);
        }

        //args[1] = Server socket port
        int serverPort = Integer.parseInt (args[0]);

        //Declaration of the ServerSocket (only a port number is needed)
        try (ServerSocket tcpMetaServerSocket = new ServerSocket (serverPort);) {
            Logger.getLogger ().info ("Meta Server, waiting for connecntions, " + tcpMetaServerSocket.getInetAddress ()
                    .getHostAddress () + ":" + tcpMetaServerSocket.getLocalPort () + "' ...");

            //The main thread is always waiting for connections
            while (true) {
                //When a connection from a client is received, a new EchoService is created. The "accept()" method
                // returns the socket to
                //communicate with the client.
                new MetaService (tcpMetaServerSocket.accept ());
            }
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("# Meta Server: IO error:" + e.getMessage ());
        }
    }
}
