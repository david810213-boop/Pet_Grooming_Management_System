package Payment;

import java.time.LocalDateTime;

public class CreditCardPayment implements PaymentSystem, PaymentVerification {
    private static final double PROCESSING_FEE_RATE = 0.02;

    @Override
    public boolean processPayment(int amount) {
        System.out.println("信用卡授權中... 金額: " + amount + "元");
        return true; // 模擬授權成功
    }

    @Override
    public int calculateTotal(int baseAmount) {
        return (int) (baseAmount * (1 + PROCESSING_FEE_RATE));
    }

    @Override
    public String generateReceipt(String appointmentId, String memberName, int amount) {
        int fee = (int) (amount / (1 + PROCESSING_FEE_RATE) * PROCESSING_FEE_RATE);
        return String.format("""
            ===== 本次消費收據 (信用卡) =====
            預約編號: %s
            姓名: %s
            手續費(2%%): %d元
            支付總額: %d元
            日期時間: %s
            ==========================
            """, appointmentId, memberName, fee, amount, LocalDateTime.now());
    }

    @Override
    public boolean verifyPaymentDetails(String memberName, int amount) {
        return amount > 0;
    }
}

