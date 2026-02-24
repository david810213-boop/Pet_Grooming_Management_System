package Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Service.GrommingItemCode;

public class AppointmentReceipt {
    private static int counter = 1; 
    private String appointmentId;
    private String userEmail;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private List<GrommingItemCode.Item> items;
    public int totalAmount;
    private boolean paid = false;



    public AppointmentReceipt(String userEmail,
                              LocalDate date, LocalTime start, LocalTime end,
                              List<GrommingItemCode.Item> items) {
        this.appointmentId = generateId();
        this.userEmail = userEmail;
        this.date = date;
        this.start = start;
        this.end = end;
        this.items = items;
        this.totalAmount = calculateTotal(items);
    }

    private static String generateId() {
        return String.format("AP%03d", counter++); // AP001, AP002, ...
    }

    private int calculateTotal(List<GrommingItemCode.Item> items) {
        int sum = 0;
        for (GrommingItemCode.Item item : items) {
            sum += item.getPrice();
        }
        return sum;
    }
    

    // Getter
    public String getAppointmentId() { return appointmentId; }
    public String getUserEmail() { return userEmail; }
    public boolean isPaid() { return paid; }
    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public List<GrommingItemCode.Item> getItems() { return items; }
    public int getTotalAmount() { return totalAmount;}

    // Setter
    public void markPaid() { this.paid = true; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== 本次預約 ===\n");
        sb.append("預約編號: ").append(appointmentId).append("\n");
        sb.append("使用者: ").append(userEmail).append("\n");
        sb.append("日期: ").append(date).append("\n");
        sb.append("時間: ").append(start).append(" - ").append(end).append("\n");
        sb.append("美容項目:\n");
        for (GrommingItemCode.Item item : items) {
            sb.append("  - ").append(item.getDescription())
              .append(" ($").append(item.getPrice()).append(")\n");
        }
        sb.append("總金額: $").append(totalAmount).append("\n");
        sb.append("付款狀態: ").append(paid ? "已付款" : "未付款").append("\n");
        sb.append("================\n");
        return sb.toString();
    }
}