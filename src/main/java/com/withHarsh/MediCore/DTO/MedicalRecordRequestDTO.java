package com.withHarsh.MediCore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequestDTO {

    private String diagnosis;
    private String prescription; //Add In medicine column
    private String notes;
}
