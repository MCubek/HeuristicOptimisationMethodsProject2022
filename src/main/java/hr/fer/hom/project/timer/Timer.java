package hr.fer.hom.project.timer;

import java.time.Duration;
import java.time.Instant;

/**
 * @author matejc
 * Created on 06.01.2023.
 */

public class Timer {
    private final Instant end;

    public Timer(Duration duration) {
        Instant start = Instant.now();
        this.end = start.plus(duration);
    }

    public boolean isActive() {
        return Instant.now().isBefore(this.end);
    }
}
