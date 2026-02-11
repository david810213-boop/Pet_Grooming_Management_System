package Appointment;

import java.util.List;

import Service.GrommingItemCode.Item;


public class AppointmentCalculator {
    
    public static int calculateTotalAmount(List<Item> selectedItems) {
        int total = 0;
    for (Item item : selectedItems) {
        total +=  item.getPrice();
    }
        return total;
    }
}


