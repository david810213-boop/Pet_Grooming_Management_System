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
import NotificationSystem.EmailNotifier;
import NotificationSystem.LineNotifier;
import NotificationSystem.NotificationHandler;
import NotificationSystem.ReminderService;
import NotificationSystem.SMSNotifier;
import Payment.CashPayment;
import Payment.CreditCardPayment;
import Payment.LinePayPayment;
import Payment.PaymentHandler;
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
    private static NotificationHandler notificationHandler = new NotificationHandler();
    private static ReminderService reminderService = new ReminderService(notificationHandler);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        notificationHandler.addNotifier(new EmailNotifier());
        notificationHandler.addNotifier(new SMSNotifier());
        notificationHandler.addNotifier(new LineNotifier());

        while (running) {
            System.out.println("\n=== 寵物美容服務系統 ===");
            
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
        System.out.println("--- 歡迎回來！ (" + currentUser.getName() + ") ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 6. 查詢預約紀錄 / 7. 結帳服務 / 8. 查詢交易紀錄 / 10. 查看我的寵物 / 0. 登出");
    }

    private static void showAdminMenu() {
        System.out.println("--- 管理者模式 ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 5. 查看所有用戶 / 6. 查詢預約紀錄 / 7. 結帳服務 / 8. 查詢所有交易 / 9. 查看所有預約 / 11. 產生財務報告 / 12. 管理員專區 0. 登出");
    }

    private static void showStaffMenu() {
        System.out.println("--- 員工模式 ---");
        System.out.println("3. 新增寵物 / 4. 預約服務 / 5. 查看所有用戶 / 6. 查詢預約紀錄 / 7. 結帳服務 / 8. 查詢所有交易 / 9. 查看所有預約 / 0. 登出");
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
            case 9 -> viewAllAppointments(scanner); // 管理者功能
            case 10 -> viewMyPets(scanner); // 顧客功能
            case 11 -> generateFinancialReport(scanner); // 管理者功能
            case 12 -> adminList(scanner); // 管理者功能
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
                        System.out.println("登入成功 ! " + currentUser.getName());
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
                            "AP001", currentUser.getName(), currentUser.getEmail(), petName1, petType1, selectedItems,
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
                        // --- 新增：發送預約成功通知與排定提醒 ---
                        System.out.println("\n系統正在處理預約通知...");
                        
                        // 1. 立即發送「預約成功」確認訊息
                        String successMessage = String.format(
                            "【預約成功確認】您已成功預約 %s %s 的寵物美容服務。寵物：%s",
                            date, start, petName1
                        );

                        notificationHandler.notifyAll(currentUser.getEmail(), successMessage);

                        // 2. 呼叫 ReminderService 排定（並模擬發送）前一日提醒
                        reminderService.scheduleReminder(currentUser.getEmail(), date, start);

                        System.out.println("=== 預約程序完成 ===");

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

                    String targetEmail;
                    // 1. 根據角色決定先「要結誰的帳」
                    if (currentUser.getRole() == UserRole.CUSTOMER) {
                        targetEmail = currentUser.getEmail();
                        System.out.println("--- 您的待結帳清單 ---");
                    } else {
                        System.out.println("--- 店鋪結帳系統 ---");
                        System.out.print("請輸入欲結帳的會員 Email: ");
                        targetEmail = scanner.nextLine();
                    }

                    // 2. 搜尋該 Email 尚未付款的預約單
                    List<AppointmentReceipt> receipts = manager.getReceiptsByUser(targetEmail);
                    List<AppointmentReceipt> unpaidReceipts = new ArrayList<>();
                    for (AppointmentReceipt r : receipts) {
                        if (!r.isPaid()) {
                            unpaidReceipts.add(r);
                        }
                    }

                    if (unpaidReceipts.isEmpty()) {
                        System.out.println("目前沒有待付款的預約紀錄。");
                        return;
                    }

                    // 3. 列出待付款清單供選擇
                    for (int i = 0; i < unpaidReceipts.size(); i++) {
                        System.out.println((i + 1) + ". " + unpaidReceipts.get(i));
                    }

                    System.out.print("請選擇要結帳的單號 (輸入數字，按 0 取消): ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); 

                    if (choice > 0 && choice <= unpaidReceipts.size()) {
                        AppointmentReceipt selected = unpaidReceipts.get(choice - 1);

                        System.out.println("\n請選擇支付方式: (1) 現金 (2) 信用卡 (3) LinePay");
                        System.out.print("您的選擇: ");
                        int payType = scanner.nextInt();
                        scanner.nextLine();
                        // 建立處理器與支付物件
                        PaymentHandler handler = new PaymentHandler();
                        PaymentSystem selectedMethod;

                        // 根據輸入決定使用的策略
                        if (payType == 1) {
                            selectedMethod = new CashPayment();
                        } else if (payType == 2) {
                            selectedMethod = new CreditCardPayment();
                        } else if (payType == 3) {
                            selectedMethod = new LinePayPayment(); //新增linepay
                        } else {
                            System.out.println("無效的支付選擇");
                            return;
                        }

                        // 4. 驗證、計算總額、印出收據
                        boolean success = handler.handleProcess(
                            selectedMethod, 
                            selected.getAppointmentId(), 
                            currentUser.getName(), 
                            selected.getTotalAmount()
                        );

                        // 5. 若支付成功，更新系統狀態並存檔
                        if (success) {
                            // 獲取包含手續費後的最終金額
                            int finalPrice = selectedMethod.calculateTotal(selected.getTotalAmount());

                            // 建立交易紀錄物件
                            Transaction transaction = new Transaction(
                                selected.getAppointmentId(),
                                selected.getUserEmail(),
                                finalPrice
                            );

                            // 標記經手人
                            String staffInfo = (currentUser.getRole() == UserRole.CUSTOMER) 
                                            ? "會員自助(" + currentUser.getName() + ")" 
                                            : "員工:" + currentUser.getName();
                            
                            transaction.markPaid(staffInfo);

                            // 更新預約單與交易管理器狀態
                            selected.setPaid(true); 
                            transactionManager.addTransaction(transaction);

                            System.out.println("結帳成功！狀態已同步至系統。");
                        } else {
                            System.out.println("支付失敗，交易已取消。");
                        }

                    } else {
                        System.out.println("取消結帳。");
                    }
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

                // 管理者查看所有預約紀錄
                private static void viewAllAppointments(Scanner scanner) {
                    // 1. 權限檢查（確保只有管理員或員工能看）
                    if (currentUser == null || currentUser.getRole() == UserRole.CUSTOMER) {
                        System.out.println("【權限不足】只有管理員或員工可以查看所有預約。");
                        return;
                    }

                    System.out.println("\n=== 系統所有預約紀錄 ===");
                    List<AppointmentReceipt> all = manager.getAllReceipts();

                    if (all.isEmpty()) {
                        System.out.println("目前系統中沒有任何預約紀錄。");
                    } else {
                        // 按照順序印出每一筆預約
                        for (int i = 0; i < all.size(); i++) {
                            System.out.println((i + 1) + ". " + all.get(i));
                        }
                        System.out.println("總計共 " + all.size() + " 筆預約。");
                    }
                }
                //查看我的寵物清單
                private static void viewMyPets(Scanner scanner) {
                    // 1. 安全檢查：確保使用者已登入
                    if (currentUser == null) {
                        System.out.println("請先登入系統才能查看寵物清單！");
                        return;
                    }

                    System.out.println("\n========================================");
                    System.out.println("        親愛的 " + currentUser.getName() +  "  您的寵物清單 ");
                    System.out.println("========================================");

                    // 2. 透過 UserService 取得該 Email 對應的寵物清單
                    // 在UserService 中回傳 usersMap.get(email).getPets()
                    List<Pet> pets = userService.getPetsByEmail(currentUser.getEmail());

                    // 3. 檢查清單是否為空
                    if (pets == null || pets.isEmpty()) {
                        System.out.println(" 您目前尚未登記任何寵物。");
                        System.out.println(" 提示：請選擇選單中的「新增寵物」功能來建立資料。");
                    } else {
                        // 4. 迴圈遍歷並格式化印出每一隻寵物
                        for (int i = 0; i < pets.size(); i++) {
                            Pet p = pets.get(i);
                            // 假設 Pet 類別有提供基本資訊的 getter 或 toString
                            System.out.println(pets.get(i).toString());
                        }
                        System.out.println("----------------------------------------");
                        System.out.println(" 總計：共 " + pets.size() + " 隻寵物");
                    }
                    System.out.println("========================================\n");
                }

                // 管理者生成財務報表
                private static void generateFinancialReport(Scanner scanner) {
                    // 1. 權限檢查：只有 ADMIN 角色可以查看
                    if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
                        System.out.println("【系統訊息】權限不足，僅限管理員查看財務報表。");
                        return;
                    }

                    System.out.println("\n==========================================================================");
                    System.out.println("                寵物美容服務系統 - 年度/月度 財務報表");
                    System.out.println("==========================================================================");
                    System.out.println("報表產出時間: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    System.out.println("--------------------------------------------------------------------------");

                    // 2. 從 TransactionManager 取得所有資料
                    List<Transaction> allTransactions = transactionManager.getAllTransactions();

                    if (allTransactions.isEmpty()) {
                        System.out.println(" [訊息] 目前尚無任何交易紀錄。");
                    } else {
                        // 印出明細表頭
                        System.out.printf("%-10s | %-20s | %-8s | %-6s | %-15s%n", 
                                        "預約編號", "會員 Email", "金額", "狀態", "支付時間");
                        System.out.println("--------------------------------------------------------------------------");

                        int paidCount = 0;
                        for (Transaction t : allTransactions) {
                            String timeStr = (t.getPaymentTime() != null) 
                                            ? t.getPaymentTime().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm")) 
                                            : "    -    ";
                            
                            System.out.printf("%-12s | %-20s | $%7d | %-6s | %-15s%n", 
                                            t.getAppointmentId(), 
                                            t.getMemberEmail(), 
                                            t.getAmount(), 
                                            t.isPaid() ? "已付" : "未付", 
                                            timeStr);
                            
                            if (t.isPaid()) paidCount++;
                        }

                        // 3. 數據統計
                        double totalRevenue = transactionManager.calculateTotalRevenue();
                        double average = (paidCount > 0) ? totalRevenue / paidCount : 0;

                        System.out.println("--------------------------------------------------------------------------");
                        System.out.printf("  統計總結： %n");
                        System.out.printf("    ▶ 總成交筆數： %d 筆 %n", paidCount);
                        System.out.printf("    ▶ 總累積營收： NT$ %,.0f %n", totalRevenue);
                        System.out.printf("    ▶ 平均客單價： NT$ %,.0f %n", average);
                    }
                    System.out.println("==========================================================================\n");
                }

                // 管理者专区
                private static void adminList(Scanner scanner) {
                    if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
                        System.out.println("【系統訊息】權限不足，僅限管理员使用");
                        return;
                    }
                    while(true){
                        System.out.println("=== [管理員專區] ===");
                        System.out.println("1. 列出所有員工(STAFF)帳號");
                        System.out.println("2. 新增員工帳號");
                        System.out.println("0. 返回選單");
                        System.out.println("請選擇: ");
                        String subChoice = scanner.nextLine();
                        if(subChoice.equals("1")){
                            System.out.print("\n=== 員工清單 === \n");
                            List<User> allUsers = userService.getAllUsers();
                            long count = allUsers.stream().filter(u -> u.getRole() == UserRole.STAFF)
                                .peek(u -> System.out.println("員工姓名: " + u.getName() 
                                + " | Email : " + u.getEmail())).count();
                                System.out.println("\n================ \n");
                            if(count == 0){
                                System.out.println("目前暫無員工。");
                            }
                        }else if (subChoice .equals("2")){
                            System.out.print("\n--- 建立新員工帳號 ---\n");
                        // ... 輸入姓名、密碼、Email ...
                        System.out.print("姓名: ");
                        String name = scanner.nextLine();
                        System.out.print("密碼: ");
                        String password = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        User newStaff = new User(name, password, email, UserRole.STAFF); 
                        String result = userService.register(newStaff); 
                        System.out.println(result);
                        }else if (subChoice .equals("0")){
                            break;
                        }else{
                            System.out.print("無效選擇，請重新输入!");
                        }
                    }
                }
                // 登出
                private static void logout() {
                    currentUser = null;
                    System.out.println("已成功登出！");
                    return;
                }
}


               
        

        





   
    

    



    

    
         

       
        
    

