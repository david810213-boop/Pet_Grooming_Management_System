import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Appointment.AppointmentManager;
import Appointment.AppointmentReceipt;
import Appointment.GroomingAppointment;
import Appointment.TimeSlot.TimeSlot;
import Appointment.TimeSlot.TimeSlotManager;
import Payment.CashPayment;
import Payment.CreditCardPayment;
import Payment.PaymentSystem;
import Payment.Transaction;
import PetManagement.Pet;
import Service.GrommingItemCode;
import User.User;
import User.UserRole;
import User.UserService;



public class Systemtest {
    private static UserService userService = new UserService();
    private static User currentUser = null; 
    private static TimeSlotManager timeSlotManager = new TimeSlotManager();
    private static AppointmentManager manager = new AppointmentManager();
    private static Payment.TransactionManager transactionManager = new Payment.TransactionManager();
    private static GroomingAppointment appointment = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== 歡迎使用寵物美容服務系統 ===");
            
            // 1. 根據角色顯示對應選單
            if (currentUser == null) {
                showGuestMenu();
            } else {
                switch (currentUser.getRole()) {
                    case ADMIN -> showAdminMenu();
                    case STAFF -> showStaffMenu();
                    case CUSTOMER -> showCustomerMenu();
                }
            }

            // 2. 讀取輸入
            System.out.print("請選擇功能: ");
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("請輸入數字！");
                continue;
            }

            // 3. 處理功能邏輯
            if (currentUser == null) {
                // 未登入時的邏輯
                if (choice == 1) registerUser(scanner);
                else if (choice == 2) loginUser(scanner);
                else if (choice == 3) running = false;
                else System.out.println("無效選項");
            } else {
                // 已登入後的邏輯切換
                processAuthenticatedChoice(choice, scanner);
                if (choice == 0) currentUser = null; // 假設 0 是登出
            }
        }
        System.out.println("系統已退出，再見！");
        scanner.close();
    }

    // === 選單顯示區域 ===
    private static void showGuestMenu() {
        System.out.println("1. 註冊 / 2. 登入 / 3. 離開系統");
    }

    private static void showCustomerMenu() {
        System.out.println("--- 顧客模式 (" + currentUser.getMemberName() + ") ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 6. 查詢預約紀錄 / 0. 登出");
    }

    private static void showAdminMenu() {
        System.out.println("--- 管理者模式 ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 5. 查看所有用戶 / 6. 查詢預約紀錄 / 7. 結帳服務 / 8. 查詢所有交易 / 0. 登出");
    }

    private static void showStaffMenu() {
        System.out.println("--- 員工模式 ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 6. 查詢預約紀錄 / 7. 結帳服務 / 0. 登出");
    }

    // === 邏輯分發區域 ===

    private static void processAuthenticatedChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 3 -> handlepet(scanner);
            case 4 -> bookAppointment(scanner);
            case 5 -> viewUserAndPets(scanner); // 管理者功能
            case 6 -> viewAppointments(scanner);
            case 7 -> processPayment(scanner); // 員工/管理者功能
            case 8 -> queryTransactions(scanner);
            case 0 -> logout();
            default -> System.out.println("無效選項");
        }
    }
                //=== 各功能實作區域 ===
                private static void registerUser(Scanner scanner) {
                    System.out.println("=== 註冊使用者 ===");
                    System.out.print("姓名: ");
                    String name = scanner.nextLine();
                    System.out.print("密碼: ");
                    String password = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    // 直接給 CUSTOMER 權限
                    User newUser = new User(name, password, email, UserRole.CUSTOMER); 
                    
                    String result = userService.register(newUser); 
                    System.out.println(result);
                }
                
                // 登入
                private static void loginUser(Scanner scanner) {
                    System.out.println("=== 使用者登入 ===");
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("密碼: ");
                    String loginPassword = scanner.nextLine();

                    currentUser = userService.login(loginEmail, loginPassword);
                    if (currentUser != null) {
                        System.out.println("登入成功 ! " + currentUser.getMemberName());
                    } else {
                        System.out.println("登入失敗：帳號或密碼錯誤");
                    }
                }
                // 新增寵物
                private static void handlepet(Scanner scanner) {
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                    System.out.print("寵物名字: ");
                    String petName = scanner.nextLine();
                    System.out.print("種類 (DOG/CAT): ");
                    String petType = scanner.nextLine();
                    System.out.print("品種: ");
                    String breed = scanner.nextLine();
                    System.out.print("體重: ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    System.out.print("年齡: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    Pet newPet = new Pet(petName, petType, breed, weight, age);
                    currentUser.addPet(newPet);
                    System.out.println(userService.addPetToUser(currentUser.getEmail(), newPet));
                }
                // 預約服務
                private static void bookAppointment(Scanner scanner) {    
                if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                        System.out.println("=== 預約服務 ===");
                        System.out.println("營業時間: 11:00 - 19:30");

                        try {
                        System.out.print("日期 (yyyy-MM-dd): ");
                        LocalDate date = LocalDate.parse(scanner.nextLine());

                        // 自動生成當日可預約時段
                        timeSlotManager.generateDailySlots(date);

                        System.out.println("\n=== 今日可預約時段 ===");
                        List<TimeSlot> availableSlots = timeSlotManager.getAvailableSlots(date);
                        if (availableSlots.isEmpty()) {
                            System.out.println("今日沒有可預約時段。");
                        } else {
                            for (TimeSlot slot : availableSlots) {
                                System.out.println(slot);
                            }
                        }

                        System.out.print("開始時間 (HH:mm): ");
                        LocalTime start = LocalTime.parse(scanner.nextLine());
                        System.out.print("結束時間 (HH:mm): ");
                        LocalTime end = LocalTime.parse(scanner.nextLine());
                        System.out.print("請輸入寵物名字: ");
                        String petName1 = scanner.nextLine().trim();
                        System.out.print("請輸入寵物種類 (狗/貓/其他): ");
                        String petType1 = scanner.nextLine().trim();
                        
                        LocalTime opening = LocalTime.of(11, 0);
                        LocalTime closing = LocalTime.of(19, 30);

                        if (start.isBefore(opening) || end.isAfter(closing)) {
                            throw new IllegalArgumentException(
                                "輸入時間超出營業時間範圍！請輸入 11:00–19:30 之間的時間。"
                            );
                        }
                        TimeSlot slot = new TimeSlot(date, start, end, true);
                        if (slot.isAvailable()) {
                            System.out.println("時段可預約，建立預約...");
                            
                        // 顯示美容項目選單
                        System.out.println("\n=== 美容項目選單 ===");
                        for (GrommingItemCode.Item item : GrommingItemCode.Item.values()) {
                             System.out.println(item);
                            }

                        // 使用者選擇美容項目
                        List<GrommingItemCode.Item> selectedItems = new ArrayList<>();
                        while (true) {
                        System.out.print("輸入美容項目代碼 (或輸入 'done' 結束): ");
                        String code = scanner.nextLine().trim().toUpperCase();
                        if (code.equals("DONE")) break;
                            try {
                                 GrommingItemCode.Item selected = GrommingItemCode.Item.valueOf(code);
                                 selectedItems.add(selected);
                                 System.out.println("已加入: " + selected.getDescription() + " ($" + selected.getPrice() + ")");
                            } catch (IllegalArgumentException e) {
                                        System.out.println("無效代碼，請重新輸入！");
                                }
                        }
                        appointment = new GroomingAppointment(
                            "AP001", currentUser.getMemberName(), currentUser.getEmail(), petName1, petType1, selectedItems,
                            date, start, end
                        );
                        timeSlotManager.markUnavailable(date, start, end); // 標記時段不可預約
                        System.out.println("\n=== 顯示可再預約時段 ===");
                        List<TimeSlot> updatedSlots = timeSlotManager.getAvailableSlots(date);
                        for (TimeSlot s : updatedSlots) {
                            System.out.println(s);
                        }
                                        
                        AppointmentReceipt receipt = new AppointmentReceipt(
                        currentUser.getEmail(),date, start, end,selectedItems,petName1, petType1
                        );
                        manager.addReceipt(receipt);
                        System.out.println(receipt);

                        } else {
                            System.out.println("該時段不可預約");
                        }

                    } catch (Exception e) {
                        System.out.println("預約失敗：" + e.getMessage());
                    }
                }
                // 查看所有使用者及其寵物
                private static void viewUserAndPets(Scanner scanner) {
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                    System.out.println("===使用者資訊===");
                    System.out.println(userService);
                }
                // 查詢預約紀錄
                private static void viewAppointments(Scanner scanner) {
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                    System.out.print("請輸入要查詢的 Email: ");
                    String queryEmail = scanner.nextLine();

                    List<AppointmentReceipt> receipts = manager.getReceiptsByUser(queryEmail);
                    if (receipts.isEmpty()) {
                        System.out.println("沒有找到該使用者的預約紀錄。");
                        } else {
                            System.out.println("=== 預約紀錄 ===");
                            for (AppointmentReceipt r : receipts) {
                                System.out.println(r);
                            }
                        }
                    }
                // 結帳服務
                private static void processPayment(Scanner scanner) {
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                    System.out.println("===結帳服務===");

                    List<AppointmentReceipt> userReceipts = manager.getReceiptsByUser(currentUser.getEmail());
                    if (userReceipts.isEmpty()) {
                        System.out.println("沒有可結帳的預約紀錄。");
                        return;
                    }

                    int fee = 0;
                    for (AppointmentReceipt r : userReceipts) {
                        fee += r.totalAmount;
                        System.out.println(r);
                    }

                    System.out.println("\n請選擇支付方式:");
                    System.out.println("  1. 現金");
                    System.out.println("  2. 信用卡");
                    System.out.print("選擇 (1-2): ");
                    String paymentChoice = scanner.nextLine().trim();

                    if (!paymentChoice.equals("1") && !paymentChoice.equals("2")) {
                        System.out.println("無效的支付方式");
                        return;
                    }

                    PaymentSystem payment = null;
                    switch (paymentChoice) {
                        case "1":
                            payment = new CashPayment();
                            break;
                        case "2":
                            payment = new CreditCardPayment();
                            fee = payment.calculateTotal(fee);
                            System.out.println("  加上手續費後: $" + fee);
                            break;
                    }

                    System.out.print("\n確認支付 $" + fee + "? (y/n): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();

                    if (!confirm.equals("y") && !confirm.equals("yes")) {
                        System.out.println("取消支付");
                        return;
                    }

                    boolean paymentSuccess = payment.processPayment(fee);
                    System.out.println("支付結果: " + (paymentSuccess ? "支付成功" : "支付失敗"));

                    // === 新增交易紀錄 ===
                    if (paymentSuccess) {
                        for (AppointmentReceipt r : userReceipts) {
                            r.markPaid(); // 更新預約紀錄付款狀態
                            Transaction transaction = new Transaction(
                                r.getAppointmentId(),
                                r.getUserEmail(),
                                r.totalAmount
                            );
                            transaction.markPaid();
                            transactionManager.addTransaction(transaction);
                        }
                        System.out.println("交易紀錄已更新！");
                    }
                    return;
                }
                // 查詢交易紀錄
                private static void queryTransactions(Scanner scanner) {
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        return;
                    }
                    
                    List<Transaction> userTransactions = transactionManager.getTransactionsByUser(currentUser.getEmail());
                    if (userTransactions.isEmpty()) {
                        System.out.println("您沒有任何交易紀錄。");
                    } else {
                        System.out.println("=== 您的交易紀錄 ===");
                        for (Transaction t : userTransactions) {
                            System.out.println(t);
                        }
                    }
                    return;
                }
                // 登出
                private static void logout() {
                    currentUser = null;
                    System.out.println("已成功登出！");
                    return;
                }
}


               
        

        





   
    

    



    

    
         

       
        
    

