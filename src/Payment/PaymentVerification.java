package Payment;

public interface PaymentVerification {
    boolean verifyPaymentDetails(String membername, int amount);
}
