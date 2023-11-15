package org.gozantes.strava.server.data.domain.challenge.activity;

import org.gozantes.strava.server.data.domain.Sport;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

public final class TimedActivity extends Activity {
    private final Duration goal;

    public TimedActivity (Sport sport, Duration goal) throws Exception {
        this (sport, goal, ActivityState.IN_PROGRESS);
    }

    public TimedActivity (Sport sport, Duration goal, ActivityState state) throws Exception {
        super (sport, state);

        Objects.requireNonNull (goal);

        if (goal.compareTo (Duration.ofMinutes (1)) < 0)
            throw new Exception ("Time goals must last longer than a minute.");

        this.goal = goal;
    }

    public TimedActivity (TimedActivity activity) throws Exception {
        this (activity.sport, activity.goal, activity.state);
    }

    public Duration getGoal () {
        return this.goal;
    }

}
