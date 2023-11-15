package org.gozantes.strava.server.data.domain.challenge.activity;

import org.gozantes.strava.server.data.domain.Sport;

import java.math.BigDecimal;
import java.util.Objects;

public final class DistanceActivity extends Activity {
    private final BigDecimal goal;

    public DistanceActivity (Sport sport, BigDecimal goal) throws Exception {
        this (sport, goal, ActivityState.IN_PROGRESS);
    }

    public DistanceActivity (Sport sport, BigDecimal goal, ActivityState state) throws Exception {
        super (sport, state);

        Objects.requireNonNull (goal);

        if (goal.compareTo (BigDecimal.ZERO) <= 0)
            throw new Exception ("Distance goals should be positive numbers representing kilometers.");

        this.goal = goal;
    }

    public DistanceActivity (DistanceActivity activity) throws Exception {
        this (activity.sport, activity.goal, activity.state);
    }

    public BigDecimal getGoal () {
        return this.goal;
    }
}
