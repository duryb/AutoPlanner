import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ScheduleBuilder {
    private TreeMap<LocalDate, Day> days;
    private TreeMap<LocalDate, SolvedDay> solution;

	public ScheduleBuilder() {
        this.days = new TreeMap<LocalDate, Day>();
        this.solution = new TreeMap<LocalDate, SolvedDay>();
    }
    
    public void solve() {
        LocalDate prev = LocalDate.now();

        for (LocalDate date : days.keySet()) {
            if (!solution.containsKey(date)) {
                solution.put(date, new SolvedDay());
            }
            int wholeDaysBetween = (int) prev.until(date, ChronoUnit.DAYS);
            for (StrictTask strictTask : days.get(date).getStrictTasks()) {
                solution.get(date).addStrictTask(strictTask);
            }
            for (LooseTask looseTask : days.get(date).getlooseTasksEndingToday())
            {
                double percentOfLastDay;
                
            }
            if (!days.get(date).getlooseTasksEndingToday().isEmpty()) {
                prev = date;
            }
        }
        
    }

	public void addLooseTask(String name, int durationMinutes, int deadlineYear, int deadlineMonth, int deadlineDate, int deadlineHour, int deadlineMinute) {
        LocalDate date = LocalDate.of(deadlineYear, deadlineMonth, deadlineDate);
        LocalTime time = LocalTime.of(deadlineHour, deadlineMinute);
        if (!days.containsKey(date)) {
            days.put(date, new Day());
        }
        days.get(date).addLooseTask(new LooseTask(name, date.atTime(time), durationMinutes));
	}

	public void addStrictTask(String name, int startYear, int startMonth, int startDate, int startHour, int startMinute, int endYear, int endMonth, int endDate, int endHour, int endMinute) {
        LocalDate startKey = LocalDate.of(startYear, startMonth, startDate);
        LocalDate endKey = LocalDate.of(endYear, endMonth, endDate);
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        while (!startKey.equals(endKey)) {
            if (!days.containsKey(startKey)) {
                days.put(startKey, new Day());
            }
            days.get(startKey).addStrictTask(new StrictTask(name, LocalTime.of(0, 0), LocalTime.of(23,59)));
            int minutes = (int) LocalTime.of(0, 0).until(LocalTime.of(23,59), ChronoUnit.MINUTES);
            days.get(startKey).addStrictTaskMinutes(minutes);
            startHour = 0;
            startMinute = 0;
            startTime = LocalTime.of(startHour, startMinute);
        }
        if (!days.containsKey(startKey)) {
            days.put(startKey, new Day());
        }
        days.get(startKey).addStrictTask(new StrictTask(name, LocalTime.of(startHour, startMinute), LocalTime.of(endHour, endMinute)));
        int minutes = (int) startTime.until(endTime, ChronoUnit.MINUTES);
        days.get(startKey).addStrictTaskMinutes(minutes);
    }
}