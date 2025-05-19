package project.models;

import java.time.LocalTime;

public record TimeInterval(LocalTime start, LocalTime end) {
    public String printInterval(){
        String interval = start.toString() + " - " + end.toString();
        return interval;
    }

    public boolean overlaps(TimeInterval other) {
        return !(this.end.compareTo(other.start) <= 0 || this.start.compareTo(other.end) >= 0);
    }

    public boolean contains(TimeInterval other) {
        return this.start.compareTo(other.start) <= 0 && this.end.compareTo(other.end) >= 0;
    }

}
