package Payment;

import java.time.LocalDateTime;

public class CreditCardPayment implements PaymentSystem,PaymentVerification{
    private static final double PROCESSING_FEE = 0.02; // 2%手續費

    @Override
    public boolean processPayment(int amount) {
        System.out.println("處理信用卡支付: " + amount + "元");
        // 實際信用卡處理邏輯
        return validateAndProcessCreditCard(amount);
    }

    @Override
    public int calculateTotal(int baseAmount) {
        // 加上信用卡手續費
        return (int) (baseAmount * (1 + PROCESSING_FEE));
    }

    @Override
    public String generateReceipt(String appointmentId,String memberName, int amount) {
        return String.format("""
            ===== 本次消費收據 =====
            預約編號: %s
            姓名: %s
            支付方式: 信用卡
            原始金額: %d元
            手續費(2%%): %d元
            總金額: %d元
            日期時間: %s
            ===================
            """,appointmentId,
            memberName,
            amount,
            (int)(amount * PROCESSING_FEE),
            (int)(amount * (1 + PROCESSING_FEE))
            ,LocalDateTime.now());
    }

    private boolean validateAndProcessCreditCard(int amount) {
        // 模擬信用卡驗證和處理
        return true;
    }

    @Override
    public boolean verifyPaymentDetails(String memberName, int amount) {
        return amount > 0;
    }
}

