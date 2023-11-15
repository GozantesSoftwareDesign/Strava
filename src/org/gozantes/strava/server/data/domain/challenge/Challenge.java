package org.gozantes.strava.server.data.domain.challenge;

import org.gozantes.strava.internals.types.Pair;
import org.gozantes.strava.internals.types.Triplet;
import org.gozantes.strava.server.data.domain.Sport;
import org.gozantes.strava.server.data.domain.challenge.activity.Activity;
import org.gozantes.strava.server.data.domain.challenge.activity.ActivityState;
import org.gozantes.strava.server.data.domain.challenge.activity.DistanceActivity;
import org.gozantes.strava.server.data.domain.challenge.activity.TimedActivity;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public final class Challenge {
    private Long id;
    private String name;
    private Pair <Date, Date> date;
    private List <Activity> activities;

    public Challenge (String name, Date start, Date end, List <Activity> activities) throws Exception {
        this (null, name, new Pair <Date, Date> (start, end), activities);
    }

    public Challenge (String name, Pair <Date, Date> date, List <Activity> activities) throws Exception {
        this (null, name, date, activities);
    }

    public Challenge (Long id, String name, Pair <Date, Date> date, List <Activity> activities) throws Exception {
        super ();

        this.setId (id);
        this.setName (name);
        this.setDate (date);
        this.setActivities ();
    }

    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) throws Exception {
        Objects.nonNull (name);

        if (name.isBlank ())
            throw new Exception ("Challenge names cannot be blank.");

        this.name = name;
    }

    public Pair <Date, Date> getDate () {
        return this.date;
    }

    public void setDate (Pair <Date, Date> date) throws Exception {
        Objects.nonNull (date);
        Objects.requireNonNull (date.x ());
        Objects.requireNonNull (date.y ());

        if (date.x ().getTime () >= date.y ().getTime ())
            throw new Exception ("Challenges must start before their deadline has already been met.");

        this.date = date;
    }

    public Date getStart () {
        return this.date.x ();
    }

    public void setStart (Date start) throws Exception {
        this.setDate (new Pair <Date, Date> (start, this.date.y ()));
    }

    public Date getEnd () {
        return this.date.y ();
    }

    public void setEnd (Date end) throws Exception {
        this.setDate (new Pair <Date, Date> (this.date.x (), end));
    }

    public List <Activity> getActivities () {
        return this.activities;
    }

    public void setActivities (List <Activity> activities) {
        this.activities = activities == null ? new ArrayList <Activity> () : activities;
    }

    public ChallengeState isCompleted () {
        return this.activities.stream ().anyMatch ((x) -> x.getState ().equals (ChallengeState.IN_PROGRESS))
                ? ChallengeState.IN_PROGRESS
                : ChallengeState.COMPLETED;
    }

    /*
     * Return value layout (might turn this into a class, although I prefer tuples)
     * ----------------------------------------------------------------------------
     * A pair of:
     *   - A triplet that contains:
     *     - A pair of:
     *       - Kilometers run (d)
     *       - Total kilometers (dT)
     *     - A pair of:
     *       - Completed time (t)
     *       - Total time (tT)
     *     - The completion rate -> ((d / dT) + (t / tT)) / 2
     *   - Activities mapped by sport
     */
    public Pair <Triplet <Pair <BigDecimal, BigDecimal>, Pair <Duration, Duration>, BigDecimal>, Map <Sport,
            List <Activity>>> completionRate () {
        BigDecimal d[] = new BigDecimal[] { BigDecimal.valueOf (0), BigDecimal.valueOf (0) };
        Duration t[] = new Duration[] { Duration.ofMinutes (0), Duration.ofMinutes (0) };

        Map <Sport, List <Activity>> activities = Map.ofEntries (Map.entry (Sport.Cyclism, new ArrayList <Activity> ()),
                Map.entry (Sport.Running, new ArrayList <Activity> ()));

        this.activities.forEach ((x) -> {
            activities.get (x.getSport ()).add (x);

            if (x.isTimed ()) {
                if (x.getState ().equals (ActivityState.COMPLETED))
                    t[0] = t[0].plus (((TimedActivity) x).getGoal ());

                t[1] = t[1].plus (((TimedActivity) x).getGoal ());
            }

            else {
                if (x.getState ().equals (ActivityState.COMPLETED))
                    d[0] = d[0].add (((DistanceActivity) x).getGoal ());

                d[1] = d[1].add (((DistanceActivity) x).getGoal ());
            }
        });

        return new Pair <> (new Triplet <> (new Pair <> (d[0], d[1]), new Pair <> (t[0], t[1]), d[0].divide (d[1])
                .add (BigDecimal.valueOf (t[0].toMinutes ()).divide (BigDecimal.valueOf (t[1].toMinutes ())))
                .divide (BigDecimal.valueOf (2))), activities);
    }
}