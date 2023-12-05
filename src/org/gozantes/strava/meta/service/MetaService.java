package org.gozantes.strava.meta.service;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class MetaService extends Thread {
    private static String DELIMITER = "#";

    private ServerSocket tcpSocket;
    private Map <String, User> users = new HashMap <> ();

    public MetaService (ServerSocket socket) {
        super ();

        try {
            this.tcpSocket = socket;
            this.start ();
        }

        catch (Exception e) {
            Logger.getLogger ().severe ("Meta Service, TCPConnection error: " + e.getMessage ());
        }
    }

    public void run () {
        DataInputStream in = null;
        DataOutputStream out = null;

        Socket s = null;

        try {
            s = this.tcpSocket.accept ();
            in = new DataInputStream (s.getInputStream ());
            out = new DataOutputStream (s.getOutputStream ());
        }

        catch (Exception e) {
            e.printStackTrace ();
        }

        try {
            String data = in.readUTF ();
            Logger.getLogger ()
                    .info (" Meta Service Received data from '" + tcpSocket.getInetAddress ().getHostAddress () + ":"
                            + s.getPort () + "' -> '" + data + "'");

            this.messageType (out, data);
        }

        catch (Exception e) {
            Logger.getLogger ().severe ("Meta Service error: " + e.getMessage ());
        }

        finally {
            try {
                if (s != null)
                    s.close ();

                s = null;

                users.forEach ((id, user) -> {
                    Logger.getLogger ().info ("id : " + id + ", user : " + user.toString ());
                });
            }
            catch (Exception e) {
                Logger.getLogger ().severe ("Meta Service cant close: " + e.getMessage ());
            }
        }
    }

    private void messageType (DataOutputStream out, String msg) {
        if (msg != null && !msg.trim ().isEmpty ()) {
            StringTokenizer tokenizer = new StringTokenizer (msg, DELIMITER);
            String type = tokenizer.nextToken ();

            if (type.equals ("0")) {
                SaveUser (tokenizer);
            }

            else if (type.equals ("1")) {
                String message = getUser (tokenizer.nextToken ());
                try {
                    out.writeUTF (message);
                }
                catch (IOException e) {
                    Logger.getLogger ().info ("   # Meta Service error, sending user message" + e.getMessage ());
                }
            }
        }
    }

    private void SaveUser (StringTokenizer tokenizer) {
        try {
            User user;

            CredType type = CredType.ParseCredType (tokenizer.nextToken ());
            String id = tokenizer.nextToken ();
            String passwd = tokenizer.nextToken ();
            String name = tokenizer.nextToken ();
            String dia = tokenizer.nextToken ();
            String mes = tokenizer.nextToken ();
            String año = tokenizer.nextToken ();
            Date birth = new SimpleDateFormat ("dd/MM/yyyy").parse (dia + "/" + mes + "/" + año);
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
                users.put (id, user);
                Logger.getLogger ().info ("User Saved: " + user.toString ());
                Logger.getLogger ().info ("Data saved");
            }
            else {
                Logger.getLogger ().info ("CredType not valid: " + type.name ());
            }
        }
        catch (Exception e) {
            Logger.getLogger ().info ("  MetaService UserSave error: " + e.getMessage ());
        }
    }

    private String getUser (String id) {
        User user = users.get (id);
        System.out.println (user.toString ());

        String type = "Meta";
        String passwd = user.getCredentials ().passwd ();
        String name = user.getData ().name ();
        Calendar cal = Calendar.getInstance (TimeZone.getTimeZone ("Europe/Madrid"));
        cal.setTime (user.getBirthDate ());
        String dia = String.valueOf (cal.get (Calendar.DAY_OF_MONTH));
        String mes = String.valueOf (cal.get (Calendar.MONTH));
        String year = String.valueOf (cal.get (Calendar.YEAR));
        String weight = user.getData ().weight ().toString ();
        String height = user.getData ().height ().toString ();
        String MaximunHeartRate = user.getData ().heartRate ().x ().toString ();
        String RestingHeartRate = user.getData ().heartRate ().y ().toString ();

        String result =
                type + DELIMITER + id + DELIMITER + passwd + DELIMITER + name + DELIMITER + dia + DELIMITER + mes
                        + DELIMITER + year + DELIMITER + weight + DELIMITER + height + DELIMITER + MaximunHeartRate
                        + DELIMITER + RestingHeartRate;
        Logger.getLogger ().info ("  -  user returned as Message:  " + result);
        return result;
    }

}
