package tutoringproject.breaks;

import java.util.concurrent.TimeUnit;

/**
 * Created by kjiang on 5/2/16.
 * Usage:
 * TimeWatch watch = TimeWatch.start();
 * // do something
 * long passedTimeInMs = watch.time();
 * long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
 */
public class TimeWatch {
    long starts;

    public static TimeWatch start() {
        return new TimeWatch();
    }

    private TimeWatch() {
        reset();
    }

    public TimeWatch reset() {
        starts = System.nanoTime();
        return this;
    }

    public long time() {
        long ends = System.nanoTime() - starts;
        return ends;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.NANOSECONDS);
    }
}
