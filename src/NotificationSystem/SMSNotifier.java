package NotificationSystem;

public class SMSNotifier implements NotificationSystem {
    @Override
    public void send(String recipient, String message) {
        // Simulate sending an SMS
        System.out.println("發送 SMS 到 " + recipient + ": " + message);
    }

}
