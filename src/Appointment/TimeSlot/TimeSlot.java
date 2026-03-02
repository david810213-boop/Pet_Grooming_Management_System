package Appointment.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {
    private LocalDate date;       
    private LocalTime start;  
    private LocalTime end;    
    private boolean available;    

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, boolean available) {
        this.date = date;
        this.start = startTime;
        this.end = endTime;
        this.available = available;
    }

    // Getter
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return start; }
    public LocalTime getEndTime() { return end; }
    public boolean isAvailable() { return available; }

    // Setter
    public void setAvailable(boolean available) { this.available = available; }

    // 檢查某個時間是否在營業時段內
    public boolean contains(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }
    //預約日期是否符合條件
    public boolean matches(LocalDate day, LocalTime start, LocalTime end) {
        return date.equals(day) && start.equals(start) && this.end.equals(end);
    }

    @Override
    public String toString() {
        return String.format("日期: %s, 時段: %s - %s, 可預約: %s",
            date, start, end, available ? "是" : "否");
    }
    // ✅ 覆寫 equals 與 hashCode，讓同日期+時段的 TimeSlot 視為同一個
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot slot = (TimeSlot) o;
        return Objects.equals(date, slot.date) &&
               Objects.equals(start, slot.start) &&
               Objects.equals(end, slot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, start, end);
    }

}
