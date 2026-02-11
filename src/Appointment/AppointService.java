package Appointment;

import java.util.ArrayList;
import java.util.List;
import Appointment.TimeSlot.TimeSlotManager;

public class AppointService {
    private List<GroomingAppointment> appointments = new ArrayList<>();
    private TimeSlotManager timeSlotManager; 

    public AppointService() {
        this.appointments = new ArrayList<>();
        this.timeSlotManager = new TimeSlotManager();
    }

    // 建立預約
    public String createAppointment(GroomingAppointment appointment) {
         // 檢查時段是否可用
        if (!timeSlotManager.isSlotAvailable(appointment.getDate(),
                                             appointment.getStartTime(),
                                             appointment.getEndTime())) {
            return "預約失敗：該時段不可預約";
        }
        // 新增預約
        appointments.add(appointment);

        // 標記該時段不可再預約
        timeSlotManager.markUnavailable(appointment.getDate(),
                                        appointment.getStartTime(),
                                        appointment.getEndTime());

        return "預約成功，編號：" + appointment.getAppointmentId() ;

    }

    // 根據主人名稱取得預約清單
    public List<GroomingAppointment> getAppointmentsByOwner(String ownerName) {
        List<GroomingAppointment> result = new ArrayList<>();
        for (GroomingAppointment a : appointments) {
            if (a.getOwnerName().equals(ownerName)) {
                result.add(a);
            }
        }
        return result;
    }

}
