package Appointment;
import java.time.LocalTime;

public class BusinessHours {
    
    private final LocalTime openingTime = LocalTime.of(11, 0);
    private final LocalTime closingTime = LocalTime.of(19, 30);

    public boolean isWithinHours(LocalTime start, LocalTime end) {
        return !start.isBefore(openingTime) && !end.isAfter(closingTime);
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}


