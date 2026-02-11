package Appointment.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotManager {
    private List<TimeSlot> slots = new ArrayList<>();

    // 新增時段
    public void addTimeSlot(TimeSlot slot) {
        slots.add(slot);
    }

    // 標記某個時段不可預約
    public void markUnavailable(LocalDate date, LocalTime start, LocalTime end) {
        for (TimeSlot slot : slots) {
            if (slot.getDate().equals(date) &&
                slot.getStartTime().equals(start) &&
                slot.getEndTime().equals(end)) {
                slot.setAvailable(false);
            }
        }
    }
    // 檢查某個時段是否可用
    public boolean isSlotAvailable(LocalDate date, LocalTime start, LocalTime end) {
        for (TimeSlot slot : slots) {
            if (slot.matches(date, start, end)) {
                return slot.isAvailable();
            }
        }
        return false;
    }


    // 查詢某天所有可預約時段
    public List<TimeSlot> getAvailableSlots(LocalDate date) {
        List<TimeSlot> result = new ArrayList<>();
        for (TimeSlot slot : slots) {
            if (slot.getDate().equals(date) && slot.isAvailable()) {
                result.add(slot);
            }
        }
        return result;
    }

}
