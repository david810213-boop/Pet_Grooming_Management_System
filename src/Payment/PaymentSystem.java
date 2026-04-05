package Payment;

public interface PaymentSystem {
    boolean processPayment(int amount);
    int calculateTotal(int baseAmount);
    String generateReceipt(String appointmentId, String memberName, int amount);
}
