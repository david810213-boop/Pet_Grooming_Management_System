package NotificationSystem;

import java.util.ArrayList;
import java.util.List;

public class NotificationHandler {
    private List<NotificationSystem> notifiers = new ArrayList<>();

    public void addNotifier(NotificationSystem notifier) {
        notifiers.add(notifier);
    }

    public void notifyAll(String recipient, String message) {
        for (NotificationSystem notifier : notifiers) {
            notifier.send(recipient, message);
        }
    }



}
