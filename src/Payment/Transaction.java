package Payment;

import java.time.LocalDateTime;


public class Transaction {
    private String appointmentId;
    private String memberEmail;
    private int amount;
    private boolean paid;
    private LocalDateTime paymentTime;
    private String staffName; // 新增：儲存結帳人員姓名

    public Transaction(String appointmentId, String memberEmail, int amount) {
        this.appointmentId = appointmentId;
        this.memberEmail = memberEmail;
        this.amount = amount;
        this.paid = false;
        this.staffName = "待處理"; 
    }

    public void markPaid(String staffName) {
        this.paid = true;
        this.paymentTime = LocalDateTime.now();
        this.staffName = staffName;
    }

    public boolean isPaid() {
        return paid;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s] 訂單:%s | 會員:%s | 金額:%d | 經手人:%s",
            paid ? "已支付" : "未支付", appointmentId, memberEmail, amount, staffName
        );
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public String getStaffName() {
        return staffName;
    }
    

}

