package net.rebworks.lunchy.domain.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DateCalculator {

    // The choice of LinkedHashMap as the implementation is to make the ordering of days intuitive
    private static final Map<String, DayOfWeek> dayMapping = new LinkedHashMap<>();

    static {
        dayMapping.put("monday", DayOfWeek.MONDAY);
        dayMapping.put("måndag", DayOfWeek.MONDAY);
        dayMapping.put("tuesday", DayOfWeek.TUESDAY);
        dayMapping.put("tisdag", DayOfWeek.TUESDAY);
        dayMapping.put("wednesday", DayOfWeek.WEDNESDAY);
        dayMapping.put("onsdag", DayOfWeek.WEDNESDAY);
        dayMapping.put("thursday", DayOfWeek.THURSDAY);
        dayMapping.put("torsdag", DayOfWeek.THURSDAY);
        dayMapping.put("friday", DayOfWeek.FRIDAY);
        dayMapping.put("fredag", DayOfWeek.FRIDAY);
    }

    private final LocalDate now;

    public DateCalculator(final LocalDate now) {
        this.now = now;
    }

    public LocalDate getDayOfWeek(final DayOfWeek dayOfWeek) {
        final DayOfWeek currentDay = now.getDayOfWeek();
        return now.plusDays(dayOfWeek.getValue() - currentDay.getValue());
    }

    public LocalDate getBeginningOfWeek() {
        return getDayOfWeek(DayOfWeek.MONDAY);
    }

    public LocalDate getEndOfWorkWeek() {
        return getDayOfWeek(DayOfWeek.FRIDAY);
    }

    public LocalDate getEndOfWeek() {
        return getDayOfWeek(DayOfWeek.SUNDAY);
    }

    public DayOfWeek today() {
        return now.getDayOfWeek();
    }

    public DayOfWeek parseDayOfWeekOrToday(final String dayName) {
        final String day = dayName.trim().toLowerCase();
        for (Entry<String, DayOfWeek> entry : dayMapping.entrySet()) {
            if (day.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return today();
    }
}
