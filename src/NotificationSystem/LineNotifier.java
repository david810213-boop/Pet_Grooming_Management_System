package NotificationSystem;

public class LineNotifier implements NotificationSystem{
    @Override
    public void send(String recipient, String message) {
        // 模擬 LINE API 傳送邏輯
        System.out.println("[LINE 通知] 傳送到用戶 " + recipient + ": " + message);
    }

}
