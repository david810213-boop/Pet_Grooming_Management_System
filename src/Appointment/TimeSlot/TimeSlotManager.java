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
    TimeSlot target = new TimeSlot(date, start, end, false);
    for (TimeSlot slot : slots) {
        if (slot.equals(target)) {
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
    // 生成某天的預設時段（11:00-13:00, 13:00-15:00, ..., 17:30-19:30）
    public void generateDailySlots(LocalDate date) {
    LocalTime opening = LocalTime.of(11, 0);
    LocalTime closing = LocalTime.of(19, 30);

    // 如果已經有該日期的時段，就不再生成
    for (TimeSlot slot : slots) {
        if (slot.getDate().equals(date)) {
            return; // 已生成過，直接跳出
        }
    }

    LocalTime current = opening;
    while (current.isBefore(closing)) {
        LocalTime next = current.plusHours(2);
        if (next.isAfter(closing)) {
            next = closing;
        }
        addTimeSlot(new TimeSlot(date, current, next, true));
        current = next;
    }
}

}
