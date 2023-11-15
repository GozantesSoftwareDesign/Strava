package org.gozantes.strava.internals.hash;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import java.lang.reflect.Field;
import java.util.Objects;

public final class SHA1Hasher {
    public static String hash (Object data) {
        return SHA1Hasher.hash (data, null);
    }

    public static String hash (Object data, Long timestamp) {
        Objects.requireNonNull (data);

        final HMac hmac = new HMac (new SHA1Digest ());
        hmac.init (new KeyParameter ("Strava".getBytes ()));

        byte[] in;
        if (data instanceof String)
            in = (data.toString () + (timestamp == null ? "" : timestamp.toString ())).getBytes ();
        else {
            StringBuilder sb = new StringBuilder ();

            for (Field f : Object.class.getDeclaredFields ())
                try {
                    if (f.canAccess (data))
                        sb.append (f.get (data).toString ());
                }
                catch (IllegalAccessException e) {
                }

            in = (sb.toString () + (timestamp == null ? "" : timestamp.toString ())).getBytes ();
        }

        hmac.update (in, 0, in.length);

        final byte[] out = new byte[hmac.getMacSize ()];
        hmac.doFinal (out, 0);

        final StringBuilder sb = new StringBuilder ();
        for (byte b : out)
            sb.append (String.format ("%x", b));

        return sb.toString ();
    }
}
