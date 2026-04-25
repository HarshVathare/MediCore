package com.withHarsh.MediCore.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private boolean isVerified = false;

    private String verificationToken;
    private String resetToken;

    //bydivercify mapping
    @JsonIgnore
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Patient patient;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Docter docter;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()-> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
