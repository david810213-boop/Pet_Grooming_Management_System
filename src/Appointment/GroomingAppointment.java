package Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import Service.GrommingItemCode.Item;

public class GroomingAppointment {
    private String appointmentId;
    private String petName;
    private String ownerName;
    private List<Item> selectedServices; 
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    
    public GroomingAppointment(String appointmentId, String petName, String ownerName,
            List<Item> selectedServices,LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.appointmentId = appointmentId;
        this.petName = petName;
        this.ownerName = ownerName;
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
    public String getPetName() { return petName; }
    public String getOwnerName() { return ownerName; }
    public List<Item> getSelectedServices() { return selectedServices; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
    

}


