package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocterResponceDTO {

    private Long id;
    private String name;
    private String email;
    private RoleType role;
    private LocalDateTime updated_At;
    private LocalDateTime created_at;
}
