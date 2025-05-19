package project.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;

public class Schedule {
    private long id;
    private TimeInterval[][] schedule;

    public Schedule() {
        schedule = new TimeInterval[7][];
        for (int i = 0; i < 7; i++) {
            schedule[i] = new TimeInterval[0];
        }
    }

    public TimeInterval[][] getSchedule() {
        return schedule;
    }

    public void addToSchedule(String day, LocalTime start, LocalTime end) {
        DayOfWeek dayWeek = DayOfWeek.valueOf(day.toUpperCase());
        int dayOfWeek = dayWeek.ordinal();

        TimeInterval newInterval = new TimeInterval(start, end);
        TimeInterval[] currentSchedule = schedule[dayOfWeek];

        for (TimeInterval existing : currentSchedule) {
            if (existing.overlaps(newInterval)) {
                System.out.println("The working intervals are overlapping");
                return;
            }
        }

        TimeInterval[] newSchedule = new TimeInterval[currentSchedule.length + 1];
        System.arraycopy(currentSchedule, 0, newSchedule, 0, currentSchedule.length);
        newSchedule[currentSchedule.length] = newInterval;
        schedule[dayOfWeek] = newSchedule;
    }

    public void printSchedule() {
        System.out.println("Schedule of the week");
        for (int day = 0; day < 7; day++) {
            if (schedule[day].length > 0) {
                System.out.println(DayOfWeek.values()[day] + ":");
                for (TimeInterval interval : schedule[day]) {
                    System.out.println(interval.printInterval() + " ");
                }
            }
        }
    }

    public Boolean scheduleValidator(Schedule clinicSchedule, String day, TimeInterval interval) {
        DayOfWeek dayWeek = DayOfWeek.valueOf(day.toUpperCase());
        int dayOfWeek = dayWeek.ordinal();

        TimeInterval[] clinicDaySchedule = clinicSchedule.getSchedule()[dayOfWeek];

        for(TimeInterval existing : clinicDaySchedule) {
            if(existing.contains(interval)) {
                return true;
            }
        }

        return false;
    }

    public DayOfWeek getDay(int day) {
        return DayOfWeek.values()[day];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSchedule(TimeInterval[][] schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", schedule=" + Arrays.toString(schedule) +
                '}';
    }
}