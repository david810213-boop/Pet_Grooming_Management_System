package NotificationSystem;

public class EmailNotifier implements NotificationSystem {
    @Override
    public void send(String recipient, String message) {
        // Simulate sending an email
        System.out.println("發送 Email 到 " + recipient + ": " + message);
    }

}
