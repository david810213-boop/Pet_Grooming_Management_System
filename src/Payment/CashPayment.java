package Payment;

import java.time.LocalDateTime;


public class CashPayment implements PaymentSystem,PaymentVerification{
     @Override
    public boolean processPayment(int amount) {
        System.out.println("處理現金支付: " + amount + "元");
        // 實際現金處理邏輯
        return true;
    }

    @Override
    public int calculateTotal(int baseAmount) {
        // 現金支付沒有額外費用
        return baseAmount;
    }

    @Override
    public String generateReceipt(String appointmentId,String memberName, int amount) {
        
        return String.format("""
            ===== 本次消費收據 =====
            預約編號: %s
            姓名: %s
            支付方式: 現金
            支付金額: %d元
            日期時間: %s
            ===================
            """,appointmentId,
            memberName,
            amount, LocalDateTime.now());
    }

    @Override
    public boolean verifyPaymentDetails(String memberName, int amount) {
        return amount > 0;
    }
}
