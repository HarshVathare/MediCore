package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocterRequestDTO {

    //id created // 1
    private String name;
    private String email;
    private String password;

//    private RoleType role = RoleType.DOCTOR;

    private String specialization;
    private String experianceInYears;
    private boolean availibility_stutus;

}
