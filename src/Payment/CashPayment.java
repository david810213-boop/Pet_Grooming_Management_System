package Payment;

import java.time.LocalDateTime;

public class CashPayment implements PaymentSystem, PaymentVerification {
    @Override
    public boolean processPayment(int amount) {
        System.out.println("實收現金金額: " + amount + "元");
        return true;
    }

    @Override
    public int calculateTotal(int baseAmount) {
        return baseAmount; // 現金無手續費
    }

    @Override
    public String generateReceipt(String appointmentId, String memberName, int amount) {
        return String.format("""
            ===== 本次消費收據 (現金) =====
            預約編號: %s
            姓名: %s
            支付金額: %d元
            日期時間: %s
            ==========================
            """, appointmentId, memberName, amount, LocalDateTime.now());
    }

    @Override
    public boolean verifyPaymentDetails(String memberName, int amount) {
        return amount > 0 && memberName != null;
    }
}
