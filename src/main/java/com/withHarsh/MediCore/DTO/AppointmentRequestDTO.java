package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.type.AppointType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    private Long Docter_Id;
//    private Long Patient_Id;
    private LocalDateTime Appointment_Date;

}
