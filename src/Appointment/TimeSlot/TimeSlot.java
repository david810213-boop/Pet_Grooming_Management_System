package Appointment.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
    private LocalDate date;       
    private LocalTime startTime;  
    private LocalTime endTime;    
    private boolean available;    

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, boolean available) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    // Getter
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public boolean isAvailable() { return available; }

    // Setter
    public void setAvailable(boolean available) { this.available = available; }

    // 檢查某個時間是否在營業時段內
    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }
    //預約日期是否符合條件
    public boolean matches(LocalDate day, LocalTime start, LocalTime end) {
        return date.equals(day) && startTime.equals(start) && endTime.equals(end);
    }

    @Override
    public String toString() {
        return String.format("日期: %s, 時段: %s - %s, 可預約: %s",
            date, startTime, endTime, available ? "是" : "否");
    }
}
