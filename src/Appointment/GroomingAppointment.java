package Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import Service.GrommingItemCode.Item;

public class GroomingAppointment {
    private String appointmentId;
    private String userEmail;
    private String userName;
    private String petName;
    private String petType;
    private List<Item> selectedServices; 
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    
    public GroomingAppointment(String appointmentId, String userName, String userEmail, String petName, String petType, List<Item> selectedServices,
            LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.appointmentId = appointmentId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.petName = petName;
        this.petType = petType;
        this.selectedServices = selectedServices;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int calculateTotalAmount() {
        int total = 0;
       for (Item item : selectedServices) {
            if (item != null) {
                total += item.getPrice();  
            }
       }
       return total;
    }

    // Getter
    public String getAppointmentId() { return appointmentId; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getPetName() { return petName; }
    public String getPetType() { return petType; }
    public List<Item> getSelectedServices() { return selectedServices; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
    

}


