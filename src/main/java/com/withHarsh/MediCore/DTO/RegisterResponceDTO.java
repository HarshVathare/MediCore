package com.withHarsh.MediCore.DTO;

import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.type.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponceDTO {

    private Long id;
    private String name;
    private String email;
    private RoleType role;
    private LocalDateTime updated_At;
    private LocalDateTime created_at;
}
