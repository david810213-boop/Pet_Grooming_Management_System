package Payment;

public class PaymentHandler {
    // 透過介面多型處理支付，不需關心是現金還是刷卡
    public boolean handleProcess(PaymentSystem method, String appointmentId, String memberName, int baseAmount) {
        
        // 1. 檢查是否需要驗證 (利用 Pattern Matching for instanceof, Java 16+)
        if (method instanceof PaymentVerification verifier) {
            if (!verifier.verifyPaymentDetails(memberName, baseAmount)) {
                System.out.println("❌ 支付驗證失敗：資料異常");
                return false;
            }
        }

        // 2. 計算最終應付金額
        int finalAmount = method.calculateTotal(baseAmount);

        // 3. 執行支付邏輯
        if (method.processPayment(finalAmount)) {
            // 4. 印出收據
            System.out.println(method.generateReceipt(appointmentId, memberName, finalAmount));
            return true;
        }

        return false;
    }
}
