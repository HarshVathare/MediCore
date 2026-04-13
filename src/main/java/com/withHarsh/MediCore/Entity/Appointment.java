package com.withHarsh.MediCore.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withHarsh.MediCore.Entity.type.AppointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_Id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docter_Id")
    private Docter docter;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointType appointmentStatus;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToOne(mappedBy = "appointment")
    private Medical_Records medicalRecords;

}
