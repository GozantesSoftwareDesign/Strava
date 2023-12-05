package org.gozantes.strava.meta;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.io.DataInputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MetaService extends Thread {
    private static String DELIMITER = "#";
    private DataInputStream in;
    //private DataOutputStream out;
    private Socket tcpSocket;
    private HashMap <String, User> usersMeta = new HashMap <> ();

    public MetaService (Socket socket) {
        super ();
        try {
            this.in = new DataInputStream (socket.getInputStream ());
            //this.out = new DataOutputStream(socket.getOutputStream());
            this.tcpSocket = socket;
            this.start ();
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("Meta Service, TCPConnection error: " + e.getMessage ());
        }
    }

    public void run () {
        //Echo server
        try {
            String data = this.in.readUTF ();
            Logger.getLogger ()
                    .info (" Meta Service Received data from '" + tcpSocket.getInetAddress ().getHostAddress () + ":"
                            + tcpSocket.getPort () + "' -> '" + data + "'");
            this.SaveUser (data);
            Logger.getLogger ().info ("Data saved");
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("Meta Service error: " + e.getMessage ());
        }
        finally {
            try {
                tcpSocket.close ();
                usersMeta.forEach ((id, user) -> {
                    Logger.getLogger ().info ("id : " + id + "user : " + user.toString ());
                });
            }
            catch (Exception e) {
                Logger.getLogger ().severe ("Meta Service cant close: " + e.getMessage ());
            }
        }
    }

    private void SaveUser (String msg) {

        if (msg != null && !msg.trim ().isEmpty ()) {
            try {
                StringTokenizer tokenizer = new StringTokenizer (msg, DELIMITER);
                User user;

                CredType type = CredType.ParseCredType (tokenizer.nextToken ());
                String id = tokenizer.nextToken ();
                String passwd = tokenizer.nextToken ();
                String name = tokenizer.nextToken ();
                Date birth = new SimpleDateFormat ("dd/MM/yyyy").parse (tokenizer.nextToken ());
                BigDecimal weight = new BigDecimal (tokenizer.nextToken ());
                Integer height = Integer.parseInt (tokenizer.nextToken ());
                Integer MaximunHeartRate = Integer.parseInt (tokenizer.nextToken ());
                Integer RestingHeartRate = Integer.parseInt (tokenizer.nextToken ());

                if (type.equals (CredType.Meta)) {
                    if (weight.intValue () == 0) {
                        weight = null;
                    }
                    if (height == 0) {
                        height = null;
                    }
                    if (height == 0) {
                        MaximunHeartRate = null;
                    }
                    if (height == 0) {
                        RestingHeartRate = null;
                    }

                    user = new User (new UserCredentials (type, id, passwd), new UserData (name, birth, weight, height,
                            new Pair <Integer, Integer> (MaximunHeartRate, RestingHeartRate)));
                    usersMeta.put (id, user);
                    Logger.getLogger ().info ("User Saved: " + user.toString ());
                }
                else {
                    Logger.getLogger ().info ("CredType not valid: " + type.name ());
                }
            }
            catch (Exception e) {
                Logger.getLogger ().info ("  MetaService UserSave error: " + e.getMessage ());
            }
        }
    }

}
