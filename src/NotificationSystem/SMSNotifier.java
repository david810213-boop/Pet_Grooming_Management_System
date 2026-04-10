package NotificationSystem;

public class SMSNotifier implements NotificationSystem {
    @Override
    public void send(String recipient, String message) {
        // Simulate sending an SMS
        System.out.println("[SMS 通知] 傳送到用戶 " + recipient + ": " + message);
    }

}
