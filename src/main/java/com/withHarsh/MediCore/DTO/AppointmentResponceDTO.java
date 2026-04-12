package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.type.AppointType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponceDTO {

    private Long Appointment_Id;
    private Long Patient_Id;
    private String Docter;
    private String Patient;
    private LocalDateTime Appointment_Date;
    private AppointType Status;
    private LocalDateTime Created_At;

}
