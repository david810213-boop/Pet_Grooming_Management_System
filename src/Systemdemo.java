import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;

import Appointment.AppointService;
import Appointment.GroomingAppointment;
import Appointment.TimeSlot.TimeSlot;
import Appointment.TimeSlot.TimeSlotManager;
import Member.User;
import Member.UserService;
import PetManagement.Pet;
import Service.GrommingItemCode.Item;

public class Systemdemo {

    public static void main(String[] args) {
        UserService userService = new UserService();

        User user1 = new User("Alice", "1234", "alice@example.com");
        User user2 = new User("Bob", "5678", "bob@example.com");
        User user3 = new User("Alice", "9999", "alice@example.com"); // 重複 email

        System.out.println(userService.register(user1)); // 註冊成功
        System.out.println(userService.register(user2)); // 註冊成功
        System.out.println(userService.register(user3)); // 註冊失敗：帳號已存在

        // 查看所有使用者
        for (User u : userService.getAllUsers()) {
            System.out.println(u.getMemberName() + " - " + u.getEmail());
        }
        // 新增寵物到使用者
        Pet pet1 = new Pet("Lucky", "Dog", "Labrador", 15, 4);
        Pet pet2 = new Pet("Mimi", "Cat", "American Shorthair", 5, 6);

        System.out.println(userService.addPetToUser("alice@example.com", pet1)); 
        System.out.println(userService.addPetToUser("alice@example.com", pet2)); 

        // 查看 Alice 的寵物
        for (Pet p : user1.getPets()) {
            System.out.println(p.getName() + " - " + p.getPetType() + " - " + p.getAge());
        }

        TimeSlotManager manager = new TimeSlotManager();

        // 建立 1 月 25 日的時段
        manager.addTimeSlot(new TimeSlot(LocalDate.of(2026, 1, 25),
                LocalTime.of(10, 0), LocalTime.of(12, 0), true));
        manager.addTimeSlot(new TimeSlot(LocalDate.of(2026, 1, 25),
                LocalTime.of(12, 0), LocalTime.of(14, 0), true));
        manager.addTimeSlot(new TimeSlot(LocalDate.of(2026, 1, 25),
                LocalTime.of(14, 0), LocalTime.of(16, 0), true));

        // 標記某個時段不可預約
        manager.markUnavailable(LocalDate.of(2026, 1, 25),
                LocalTime.of(12, 0), LocalTime.of(14, 0));

         // 查詢可預約時段
        System.out.println("2026-01-25 可預約時段：");
        for (TimeSlot slot : manager.getAvailableSlots(LocalDate.of(2026, 1, 25))) {
            System.out.println(slot);
        }

        //建立預約服務
        GroomingAppointment appointment = new GroomingAppointment(
            "AP001",
            "Lucky",
            "Alice",
            null,
            LocalDate.of(2026, 1, 25),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0)
        );
        // 使用預約服務
        AppointService appointService = new AppointService();
        appointService.createAppointment(appointment);

        // 選擇的美容項目
        List<Item> selectedServices = new ArrayList<>();
        selectedServices.add(Item.GS001);
        selectedServices.add(Item.GS004);
    
        // 計算總金額
        int totalAmount = 0;
        for (Item item : selectedServices) {
            totalAmount += item.getPrice();
            System.out.println(item);
        }
        System.out.println("\n---預約成功---\n編號: " + appointment.getAppointmentId() + "\n預約美容項目: " + selectedServices.size() 
        + " \n總金額: " + totalAmount + " 元");
        
        NotificationSystem.NotificationHandler handler = new NotificationSystem.NotificationHandler();
        NotificationSystem.ReminderService reminderService = new NotificationSystem.ReminderService(handler);
            handler.addNotifier(new NotificationSystem.EmailNotifier());
            handler.addNotifier(new NotificationSystem.SMSNotifier());

            String message = "親愛的用戶: " + appointment.getOwnerName() + "，您已預約成功！ 寵物: " + appointment.getPetName() +
                            " 日期: " + appointment.getDate() +
                            " 時間: " + appointment.getStartTime() + " - " + appointment.getEndTime();
            handler.notifyAll(appointment.getOwnerName(), message);
            reminderService.scheduleReminder(appointment.getOwnerName(), appointment.getDate(), appointment.getStartTime());

            return;
            

    }
    
}

