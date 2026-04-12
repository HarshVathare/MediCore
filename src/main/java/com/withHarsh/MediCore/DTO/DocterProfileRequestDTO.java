package com.withHarsh.MediCore.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocterProfileRequestDTO {

    private String name;
    @Email(message = "Enter Valid email ..!")
    private String email;
    private String specialization;
    private String experianceInYears;
    private boolean availibility_stutus;
}
