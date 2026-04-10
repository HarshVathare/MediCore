package com.withHarsh.MediCore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponceDTO {

    private Long id; //fetch docter-id
    private String name;
    private String specialization;
    private String experianceInYears;
    private boolean availibility_stutus;
}
