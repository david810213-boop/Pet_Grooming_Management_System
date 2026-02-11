package NotificationSystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReminderService {
    private NotificationHandler handler;
        public ReminderService(NotificationHandler handler) {
            this.handler = handler;
        }

        public void scheduleReminder(String recipient, LocalDate date, LocalTime start) {
        LocalDate reminderDate = date.minusDays(1);
        String message = String.format(
            "提醒您：您的寵物美容預約在 %s %s，請準時到店。",
            date, start
        );
        System.out.println("\n=== 系統提醒 ===");
        System.out.println("將於 " + reminderDate + " 發送通知");
        handler.notifyAll(recipient, message);

        }
        
    }


