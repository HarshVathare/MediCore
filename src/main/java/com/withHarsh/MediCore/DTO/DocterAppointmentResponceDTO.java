package com.withHarsh.MediCore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocterAppointmentResponceDTO {

    private Long Appointment_Id;
    private Long Patient_Id;
    private LocalDateTime Appointment_Time;
    private String Name;
    private String Email_Id;
    private String Age;
    private String Gender;
    private String Medical_History;
    private LocalDateTime CreatedAt;
}
