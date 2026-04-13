package com.withHarsh.MediCore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponceDTO {

    private Long MedicalRecord_Id;
    private Long Patient_Id;
    private String Docter_Name;
    private String Patient_Name;
    private String Diagnosis;
    private String Medicine;
    private String Notes;
    private LocalDateTime CreatedAt;
}
