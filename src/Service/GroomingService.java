package Service;


import Service.GrommingItemCode.Item;

public class GroomingService {
    public class GroomingServiceItem {
    private Item code; 
    private String notes;

    public GroomingServiceItem(Item code, String notes) {
        this.code = code;
        this.notes = notes;
    }

    public Item getCode() { return code; }
    public String getNotes() { return notes; }
    }

}
