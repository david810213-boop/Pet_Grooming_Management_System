package Payment;

import java.time.LocalDateTime;

public class LinePayPayment implements PaymentSystem, PaymentVerification {
    private static final double LINE_POINTS_DISCOUNT = 0.95; // 假設 LinePay 偶爾有 5% 優惠

    @Override
    public boolean processPayment(int amount) {
        System.out.println("📱 LinePay 呼叫中...");
        System.out.println("請掃描螢幕上的 QR Code... (模擬中)");
        System.out.println("支付點數確認中... 支付金額: " + amount + "元");
        return true; // 模擬支付成功
    }

    @Override
    public int calculateTotal(int baseAmount) {
        // 假設 LinePay 結帳享 95 折優惠
        return (int) (baseAmount * LINE_POINTS_DISCOUNT);
    }

    @Override
    public String generateReceipt(String appointmentId, String memberName, int amount) {
        return String.format("""
            ===== 本次消費收據 (LinePay) =====
            預約編號: %s
            姓名: %s
            支付方式: LinePay (已享 5%% 優惠)
            實付金額: %d元
            日期時間: %s
            ==============================
            """, appointmentId, memberName, amount, LocalDateTime.now());
    }

    @Override
    public boolean verifyPaymentDetails(String memberName, int amount) {
        // 簡單驗證：LinePay 必須有綁定姓名且金額大於 0
        return memberName != null && !memberName.isEmpty() && amount > 0;
    }
}