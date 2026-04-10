package com.withHarsh.MediCore.Entity;

import com.withHarsh.MediCore.Entity.type.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role=RoleType.PATIENT;

    @UpdateTimestamp
    private LocalDateTime updated_At;

    @CreationTimestamp
    private LocalDateTime created_at;

    //bydivercify mapping
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Patient patient;

    @OneToOne(mappedBy = "user")
    private Docter docter;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }
}
