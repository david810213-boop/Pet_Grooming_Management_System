package NotificationSystem;

public class EmailNotifier implements NotificationSystem {
    @Override
    public void send(String recipient, String message) {
        // Simulate sending an email
        System.out.println("[EMAIL 通知] 傳送到用戶 " + recipient + ": " + message);
    }

}
