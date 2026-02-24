package Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import Service.GrommingItemCode.Item;

public class GroomingAppointment {
    private String appointmentId;
    private String userEmail;
    private String ownerName;
    private String petName;
    private List<Item> selectedServices; 
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    
    public GroomingAppointment(String appointmentId, String userEmail, String ownerName, String petName,
            LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.appointmentId = appointmentId;
        this.userEmail = userEmail;
        this.ownerName = ownerName;
        this.petName = petName;
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
        
    
    public String getAppointmentId() {return appointmentId;}    
    public String getUserEmail() { return userEmail; }
    public String getOwnerName() { return ownerName; }
    public String getPetName() { return petName; }
    public List<Item> getSelectedServices() { return selectedServices; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
    

}


