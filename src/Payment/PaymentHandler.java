package Payment;

public class PaymentHandler {
        private static final String appointmentId = null;

        public void processPayment(PaymentSystem paymentMethod, String memberName, int baseAmount) {
        if (paymentMethod instanceof PaymentVerification) {
            PaymentVerification verifier = (PaymentVerification) paymentMethod;
            if (!verifier.verifyPaymentDetails(memberName, baseAmount)) {
                System.out.println("支付驗證失敗");
                return;
            }
            System.out.println("支付驗證成功");
        } else {
            System.out.println("支付方式不支援驗證");
            return;
        }

        // 使用 instanceof 識別支付方式並執行特定邏輯
        if (paymentMethod instanceof CashPayment) {
            CashPayment cashPayment = (CashPayment) paymentMethod;
            handleCashPayment(cashPayment, memberName, baseAmount);
        } else if (paymentMethod instanceof CreditCardPayment) {
            CreditCardPayment creditCardPayment = (CreditCardPayment) paymentMethod;
            handleCreditCardPayment(creditCardPayment, memberName, baseAmount);
        } else {
            System.out.println("未知的支付方式");
        }
    }

    // 處理現金支付方法
    private void handleCashPayment(CashPayment cashPayment, String memberName, int baseAmount) {
        // 計算總金額（帶折扣）
        int totalAmount = cashPayment.calculateTotal(baseAmount);

        // 處理支付
        if (cashPayment.processPayment(totalAmount)) {
            // 生成收據
            System.out.println(cashPayment.generateReceipt(appointmentId,memberName, totalAmount));
        }
    }

    // 處理信用卡支付方法
    private void handleCreditCardPayment(CreditCardPayment creditCardPayment, String memberName, int baseAmount) {
        // 計算總金額（加手續費）
        int totalAmount = creditCardPayment.calculateTotal(baseAmount);

        // 處理支付
        if (creditCardPayment.processPayment(totalAmount)) {
            // 生成收據
            System.out.println(creditCardPayment.generateReceipt(appointmentId,memberName, totalAmount));
        }
    }

}
