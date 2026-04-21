package com.withHarsh.MediCore.Entity;

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
public class Medical_Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_Id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docter_Id")
    private Docter docter;

    @Column(nullable = false)
    private String diagnoses;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_Id")
    private Prescription prescription;

    @OneToOne
    @JoinColumn(name = "appointment_Id")
    private Appointment appointment;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "medical_report", columnDefinition = "LONGBLOB")
    private byte[] medicalReport;

    private String fileName;
    private String fileType;
}
