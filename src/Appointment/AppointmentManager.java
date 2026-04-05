package Appointment;

import java.util.ArrayList;
import java.util.List;


public class AppointmentManager {
    private List<AppointmentReceipt> receipts = new ArrayList<>();

    // 新增收據
    public void addReceipt(AppointmentReceipt receipt) {
        receipts.add(receipt);
    }

    // 取得所有收據
    public List<AppointmentReceipt> getAllReceipts() {
        return receipts;
    }

    // 顯示所有收據
    public void printAllReceipts() {
        if (receipts.isEmpty()) {
            System.out.println("目前沒有任何預約紀錄。");
        } else {
            for (AppointmentReceipt receipt : receipts) {
                System.out.println(receipt);
            }
        }
    }
    
    // 根據使用者電子郵件取得收據
    public List<AppointmentReceipt> getReceiptsByUser(String email) {
        List<AppointmentReceipt> result = new ArrayList<>();
        for (AppointmentReceipt r : receipts) {
            if (r.getUserEmail().equals(email)) {
                result.add(r);
            }
      
        }
        return result;
    }

}
