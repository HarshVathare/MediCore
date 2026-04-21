package com.withHarsh.MediCore.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column(length = 3000)
    private String medicalHistory;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE })
    private List<Appointment> appointment = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE} )
    private List<Medical_Records> medicalRecords = new ArrayList<>();


    @Lob
    @Column(name = "medical_report", columnDefinition = "LONGBLOB")
    private byte[] medicalReport;

    private String fileName;
    private String fileType;
}
