package Payment;

public interface PaymentVerification {
    boolean verifyPaymentDetails(String memberName, int amount);
}