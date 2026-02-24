import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Appointment.AppointmentManager;
import Appointment.AppointmentReceipt;
import Appointment.TimeSlot.TimeSlot;
import Member.User;
import Member.UserService;
import Payment.CashPayment;
import Payment.CreditCardPayment;
import Payment.PaymentSystem;
import Payment.Transaction;
import PetManagement.Pet;
import Service.GrommingItemCode;



public class Systemtest {
    private static UserService userService = new UserService();
    private static User currentUser = null; 
    private static AppointmentManager manager = new AppointmentManager();
    private static Payment.TransactionManager transactionManager = new Payment.TransactionManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== 歡迎使用寵物美容服務系統 ===");
            System.out.println("1. 註冊使用者");
            System.out.println("2. 登入");
            System.out.println("3. 新增寵物");
            System.out.println("4. 預約服務");
            System.out.println("5. 查看使用者與寵物");
            System.out.println("6. 查詢預約紀錄");
            System.out.println("7. 結帳服務");
            System.out.println("8. 查詢交易紀錄");
            System.out.println("0. 離開");
            System.out.print("請選擇功能: ");
            
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1: // 註冊
                    System.out.print("姓名: ");
                    String name = scanner.nextLine();
                    System.out.print("密碼: ");
                    String password = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    User user = new User(name, password, email);
                    System.out.println(userService.register(user));
                    break;

                case 2: // 登入
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
                    break;

                case 3: // 新增寵物
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
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

                    Pet pet = new Pet(petName, petType, breed, weight, age);
                    currentUser.addPet(pet);
                    System.out.println(userService.addPetToUser(currentUser.getEmail(), pet));
                    break;

                case 4: // 預約服務
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
                    }
                    System.out.println("=== 預約服務 ===");
                    System.out.println("營業時間: 11:00 - 19:30");
                    System.out.print("日期 (yyyy-MM-dd): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    System.out.print("開始時間 (HH:mm): ");
                    LocalTime start = LocalTime.parse(scanner.nextLine());
                    System.out.print("結束時間 (HH:mm): ");
                    LocalTime end = LocalTime.parse(scanner.nextLine());

                    TimeSlot slot = new TimeSlot(date, start, end, true);
                    if (slot.isAvailable()) {
                        
                    
                    
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
                            slot.setAvailable(false); // 標記時段為不可用
                            
                            AppointmentReceipt receipt = new AppointmentReceipt(
                                currentUser.getEmail(),date, start, end,selectedItems);
                            manager.addReceipt(receipt);
                            System.out.println(receipt);

                        } else {
                                System.out.println("預約失敗：該時段已有預約");
                        }

                         NotificationSystem.NotificationHandler handler = new NotificationSystem.NotificationHandler();
                            handler.addNotifier(new NotificationSystem.EmailNotifier());
                            handler.addNotifier(new NotificationSystem.SMSNotifier());

                            String message = "親愛的用戶: " + currentUser.getMemberName() + "，您的預約已成功！日期: " + date +
                                            " 時間: " + start + " - " + end;
                            handler.notifyAll(currentUser.getEmail(), message);

                            // === 新增提醒功能 ===
                            NotificationSystem.ReminderService reminderService = new NotificationSystem.ReminderService(handler);
                            reminderService.scheduleReminder(currentUser.getEmail(), date, start);
                            
                            break;

                case 5: // 查看使用者與寵物
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
                    }
                    System.out.println("===使用者資訊===");
                    System.out.println(userService);
            
                    break;

                case 6://查看預約紀錄
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
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
                        break;
                    


                case 7: // 結帳
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
                    }
                    System.out.println("===結帳服務===");
                    
                    List<AppointmentReceipt> userReceipts = manager.getReceiptsByUser(currentUser.getEmail());
                    if (userReceipts.isEmpty()) {
                        System.out.println("沒有可結帳的預約紀錄。");
                        break;
                    }
                    int fee = 0;
                    for (AppointmentReceipt r : userReceipts) {
                        fee += r.totalAmount;
                        System.out.println(r);
                    }
                
                    // 選擇支付方式
                        System.out.println("\n請選擇支付方式:");
                        System.out.println("  1. 現金");
                        System.out.println("  2. 信用卡");
                        System.out.print("選擇 (1-2): ");

                        String paymentChoice = scanner.nextLine().trim();

                        // 驗證支付方式選擇
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
                                fee = payment.calculateTotal(fee); // 信用卡加手續費
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
                                Transaction transaction = new Transaction(
                                    r.getUserEmail(),
                                    r.getAppointmentId(),
                                    r.totalAmount
                                );
                                transaction.markPaid();
                                transactionManager.addTransaction(transaction);
                            }
                            System.out.println("交易紀錄已更新！");
                        }
                        break;
                
                case 8: // 查詢交易紀錄
                    if (currentUser == null) {
                        System.out.println("請先登入！");
                        break;
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
                    break;

                case 0: // 離開
                    running = false;
                    System.out.println("系統已退出，再見！");
                    break;

                default:
                    System.out.println("無效選項，請重新輸入。");
            }
        }

        scanner.close();
    }
}



   
    

    



    

    
         

       
        
    

