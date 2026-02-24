package Payment;

import java.time.LocalDateTime;


public class Transaction {
    private String appointmentId;
    private String memberEmail;
    private int amount;
    private boolean paid;
    private LocalDateTime paymentTime;

    public Transaction(String appointmentId, String memberEmail, int amount) {
        this.appointmentId = appointmentId;
        this.memberEmail = memberEmail;
        this.amount = amount;
        this.paid = false;
    }

    public void markPaid() {
        this.paid = true;
        this.paymentTime = LocalDateTime.now();
    }

    public boolean isPaid() {
        return paid;
    }

    @Override
    public String toString() {
        return String.format(
            "訂單編號: %s | 會員: %s | 金額: %d | 狀態: %s | 支付時間: %s",
            appointmentId, memberEmail, amount,
            paid ? "已付款" : "未付款",
            paid ? paymentTime : "-"
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

    
}
