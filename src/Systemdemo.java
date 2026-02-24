import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Appointment.AppointService;
import Appointment.AppointmentManager;
import Appointment.AppointmentReceipt;
import Appointment.GroomingAppointment;
import Appointment.TimeSlot.TimeSlot;
import Appointment.TimeSlot.TimeSlotManager;
import Member.User;
import Member.UserService;
import PetManagement.Pet;
import Service.GrommingItemCode.Item;
import Payment.TransactionManager;
import Payment.Transaction;
import Payment.CashPayment;
import Payment.CreditCardPayment;
import Payment.PaymentSystem;


public class Systemdemo {
    private static TransactionManager transactionManager = new TransactionManager();


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
            "AP001","alice@example.com","Alice","Lucky",
            LocalDate.of(2026, 2, 25),
            LocalTime.of(11, 0),
            LocalTime.of(12, 0)
        );

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

        AppointmentManager appointmentManager = new AppointmentManager();
        AppointmentReceipt receipt = new AppointmentReceipt(
            appointment.getUserEmail(),
            appointment.getDate(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            selectedServices
        );
            

        appointmentManager.addReceipt(receipt);
        
        NotificationSystem.NotificationHandler handler = new NotificationSystem.NotificationHandler();
        NotificationSystem.ReminderService reminderService = new NotificationSystem.ReminderService(handler);
            handler.addNotifier(new NotificationSystem.EmailNotifier());
            handler.addNotifier(new NotificationSystem.SMSNotifier());

            String message = "親愛的用戶: " + appointment.getOwnerName() + "，您已預約成功！ 寵物: " + appointment.getPetName() +
                            " 日期: " + appointment.getDate() +
                            " 時間: " + appointment.getStartTime() + " - " + appointment.getEndTime();
            handler.notifyAll(appointment.getOwnerName(), message);
            reminderService.scheduleReminder(appointment.getOwnerName(), appointment.getDate(), appointment.getStartTime());
            
        // 查詢預約紀錄
        
        System.out.println("\n=== 查詢 Alice 的預約紀錄 ===");
        for (AppointmentReceipt r : appointmentManager.getReceiptsByUser("alice@example.com")) {
                System.out.println(r);
        } 
        // 結帳服務
        System.out.println("\n=== Alice 結帳 ===");
        List<AppointmentReceipt> aliceReceipts = appointmentManager.getReceiptsByUser("alice@example.com");
        int fee = 0;
        for (AppointmentReceipt r : aliceReceipts) {
            fee += r.totalAmount;
        }

        System.out.println("總金額: $" + fee);

        // 模擬選擇支付方式 (這裡直接用現金)
        PaymentSystem payment = new CashPayment();
        boolean paymentSuccess = payment.processPayment(fee);

        System.out.println("支付結果: " + (paymentSuccess ? "支付成功" : "支付失敗"));

        if (paymentSuccess) {
            for (AppointmentReceipt r : aliceReceipts) {
                r.markPaid(); // 更新預約紀錄付款狀態
                Transaction transaction = new Transaction(
                    r.getAppointmentId(),
                    r.getUserEmail(),
                    r.totalAmount
                );
                transaction.markPaid(); // 更新交易紀錄付款狀態
                transactionManager.addTransaction(transaction);
            }
            System.out.println("交易紀錄已更新！");
        }
       
        // 查詢交易紀錄
        System.out.println("\n=== 查詢 Alice 的交易紀錄 ===");
        for (Transaction t : transactionManager.getTransactionsByUser("alice@example.com")) {
            System.out.println(t);
        }

        System.out.println("\n=== 商家查看所有交易紀錄 ===");
        transactionManager.printAllTransactions();
    }
    
}

