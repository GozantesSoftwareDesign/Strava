package org.gozantes.strava.server.data.domain.auth;

import org.gozantes.strava.internals.types.Pair;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public record UserData(String name, Date birth, BigDecimal weight, Integer height,
        Pair <Integer, Integer> heartRate) {
    public UserData (String name, Date birth, BigDecimal weight, Integer height, Pair <Integer, Integer> heartRate) {
        Objects.requireNonNull (name);
        Objects.requireNonNull (birth);

        if (name.isEmpty ())
            throw new RuntimeException ("Names must not be empty strings");

        Calendar cal = Calendar.getInstance ();
        cal.set (1900, Calendar.JANUARY, 1);

        if (cal.getTime ().compareTo (birth) > 0)
            throw new DateTimeException (String.format ("Users born before %s are not allowed.",
                    new SimpleDateFormat ("dd/MM/yyyy").format (cal.getTime ())));

        else if (Calendar.getInstance ().getTime ().compareTo (birth) < 0)
            throw new DateTimeException ("Users born after today's date are not allowed.");

        if (weight != null && weight.compareTo (BigDecimal.ZERO) <= 0)
            throw new RuntimeException ("Weight must be a positive number.");

        if (height != null && height <= 0)
            throw new RuntimeException ("Height must be a positive number.");

        if (heartRate == null)
            heartRate = new Pair <Integer, Integer> (null, null);

        this.name = name;
        this.birth = birth;
        this.weight = weight;
        this.height = height;
        this.heartRate = heartRate;
    }

    public UserData (UserData data) {
        this (data.name, data.birth, data.weight, data.height, data.heartRate);
    }

    public UserData (UserData data, String name) {
        this (name, data == null ? null : data.birth, data == null ? null : data.weight,
                data == null ? null : data.height, data == null ? null : data.heartRate);
    }

    public UserData (UserData data, Date birth) {
        this (data == null ? null : data.name, birth, data == null ? null : data.weight,
                data == null ? null : data.height, data == null ? null : data.heartRate);
    }

    public UserData (UserData data, BigDecimal weight) {
        this (data == null ? null : data.name, data == null ? null : data.birth, weight,
                data == null ? null : data.height, data == null ? null : data.heartRate);
    }

    public UserData (UserData data, Integer height) {
        this (data == null ? null : data.name, data == null ? null : data.birth, data == null ? null : data.weight,
                height, data == null ? null : data.heartRate);

    }

    public UserData (UserData data, Pair <Integer, Integer> heartRate) {
        this (data == null ? null : data.name, data == null ? null : data.birth, data == null ? null : data.weight,
                data == null ? null : data.height, heartRate);
    }
}
