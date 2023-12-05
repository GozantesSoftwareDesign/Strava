package org.gozantes.strava.server.gateway;

import org.gozantes.strava.internals.logging.Logger;
import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.server.data.domain.auth.CredType;
import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;
import org.gozantes.strava.server.data.domain.auth.UserData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public final class MetaServiceGateway implements AuthGateway {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8001;
    private static String DELIMITER = "#";
    private static MetaServiceGateway instance;

    private MetaServiceGateway () {
        super ();
    }

    public static MetaServiceGateway getInstance () {
        return MetaServiceGateway.instance == null
                ? (MetaServiceGateway.instance = new MetaServiceGateway ())
                : MetaServiceGateway.instance;
    }

    public void saveUser (User user) {
        try (Socket tcpSocket = new Socket (SERVER_IP, SERVER_PORT);
                DataOutputStream out = new DataOutputStream (tcpSocket.getOutputStream ())) {

            out.writeUTF (this.saveMessageParse (user));
        }
        catch (Exception e) {
            Logger.getLogger ().severe ("# MetaServiceGateway : Error: " + e.getMessage ());
        }
    }

    public User getUser (String id) {
        User result = null;
        try (Socket tcpSocket = new Socket (SERVER_IP, SERVER_PORT);
                DataInputStream in = new DataInputStream (tcpSocket.getInputStream ());
                DataOutputStream out = new DataOutputStream (tcpSocket.getOutputStream ())) {

            out.writeUTF (this.getMessageParse (id));
            String inString = in.readUTF ();
            result = MessageToUser (inString);

        }
        catch (Exception e) {
            Logger.getLogger ().severe ("# MetaServiceGateway : Error: " + e.getMessage ());
        }
        return result;
    }

    private String saveMessageParse (User user) {
        String type = user.getType ().toString ();
        String id = user.getId ();
        String passwd = user.getCredentials ().passwd ();
        String name = user.getData ().name ();
        Calendar cal = Calendar.getInstance (TimeZone.getTimeZone ("Europe/Madrid"));
        cal.setTime (user.getBirthDate ());
        String dia = String.valueOf (cal.get (Calendar.DAY_OF_MONTH));
        String mes = String.valueOf (cal.get (Calendar.MONTH));
        String año = String.valueOf (cal.get (Calendar.YEAR));
        String weight = user.getData ().weight ().toString ();
        String height = user.getData ().height ().toString ();
        String MaximunHeartRate = user.getData ().heartRate ().x ().toString ();
        String RestingHeartRate = user.getData ().heartRate ().y ().toString ();

        String result = "0" + DELIMITER + type + DELIMITER + id + DELIMITER + passwd + DELIMITER +
                name + DELIMITER + dia + DELIMITER + mes + DELIMITER + año + DELIMITER + weight + DELIMITER +
                height + DELIMITER + MaximunHeartRate + DELIMITER + RestingHeartRate;
        Logger.getLogger ().info ("  -  user Message:  " + result);
        return result;
    }

    private String getMessageParse (String id) {
        return "1" + DELIMITER + id;
    }

    private User MessageToUser (String msg) {
        if (msg != null && !msg.trim ().isEmpty ()) {
            try {
                StringTokenizer tokenizer = new StringTokenizer (msg, DELIMITER);
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

                User user = new User (new UserCredentials (type, id, passwd), new UserData (name, birth, weight, height,
                        new Pair <Integer, Integer> (MaximunHeartRate, RestingHeartRate)));
                Logger.getLogger ().info ("User obtained successfully: " + user.toString ());
            }
            catch (Exception e) {
                Logger.getLogger ().info ("  MetaServiceGateWay User obtaining error: " + e.getMessage ());
            }
        }

        return null;
    }

    @Override
    public boolean exists (String id) {
        return false;
    }

    @Override
    public boolean validate (UserCredentials user) {
        return false;
    }

    @Override
    public boolean signup (UserCredentials user) {
        return false;
    }
}
