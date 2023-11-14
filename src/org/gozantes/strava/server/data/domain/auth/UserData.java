package org.gozantes.strava.server.data.domain.auth;

import org.gozantes.strava.internals.types.Pair;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public record UserData(Date birth, BigDecimal weight, Integer height,
                       Pair<Integer, Integer[]> heartRate) {
    public UserData(Date birth, BigDecimal weight, Integer height,
                    Integer maxHeartRate, Integer[] heartRateMeasures) {
        this(birth, weight, height, new Pair<Integer, Integer[]>(maxHeartRate, heartRateMeasures));
    }

    public UserData(Date birth, BigDecimal weight, Integer height,
                    Pair<Integer, Integer[]> heartRate) {
        Objects.requireNonNull(birth);

        Calendar cal = Calendar.getInstance();
        cal.set(1900, 01, 01);

        if (cal.getTime().compareTo(birth) > 0)
            throw new DateTimeException(String.format("Users born before %s are not allowed.", new SimpleDateFormat("dd/MM/YYYY").format(cal.getTime())));

        else if (Calendar.getInstance().getTime().compareTo(birth) < 0)
            throw new DateTimeException("Users born after today's date are not allowed.");

        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Weight must be a positive number.");

        if (height != null && height <= 0)
            throw new RuntimeException("Height must be a positive number.");

        if (heartRate == null)
            heartRate = new Pair<Integer, Integer[]>(null, null);

        for (Integer i : List.of(heartRate.y() == null ? new Integer[0] : heartRate.y()))
            if (i == null || i <= 0)
                throw new RuntimeException("Heart rates must be positive.");

        this.birth = birth;
        this.weight = weight;
        this.height = height;
        this.heartRate = heartRate;
    }

    public UserData(UserData data) {
        this(data.birth, data.weight, data.height, data.heartRate);
    }

    public UserData(UserData data, Date birth) {
        this(birth, data == null ? null : data.weight, data == null ? null : data.height, data == null ? null : data.heartRate);
    }

    public UserData(UserData data, BigDecimal weight) {
        this(data.birth, weight, data.height, data.heartRate);
    }

    public UserData(UserData data, Integer height) {
        this(data.birth, data.weight, height, data.heartRate);
    }

    public UserData(UserData data, Pair<Integer, Integer[]> heartRate) {
        this(data.birth, data.weight, data.height, heartRate);
    }
}
