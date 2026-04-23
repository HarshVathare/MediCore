package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Services.DocterServices;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docters")
@RequiredArgsConstructor
public class DocterController {

    private final DocterServices docterServices;

    @GetMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> getProfile(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(docterServices.getProfile(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> UpdateProfile(@Valid @RequestBody DocterProfileRequestDTO docterProfileRequestDTO, @Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(docterServices.updateProfile(docterProfileRequestDTO, authentication));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<DocterAppointmentResponceDTO>> getAppointments(
            @RequestParam(name = "Status", required = false) String Status,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        if (Status != null) {
            return ResponseEntity.ok(
                    docterServices.getAppointmentByStatus(Status, authentication, page, size));
        }

        return ResponseEntity.ok(docterServices.getAppointments(authentication, page, size));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long id, @RequestBody UpdateAppointmentRequestDTO requestDTO) {
        return ResponseEntity.ok(docterServices.updateAppointmentStatus(id, requestDTO));
    }

    @PostMapping("/{appointmentId}/medical-record")
    public ResponseEntity<MedicalRecordResponceDTO> createMedicalRecord(@PathVariable Long appointmentId, @RequestBody MedicalRecordRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(docterServices.createMedicalRecord(appointmentId ,requestDTO));
    }

    @GetMapping("/medical-record/{Patient_Id}")
    public ResponseEntity<List<MedicalRecordResponceDTO>>getMedicalRecordByPatientId(@PathVariable Long Patient_Id) {
        return ResponseEntity.ok(docterServices.getMedicalRecordByPatientId(Patient_Id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponceDTO>> getDocterBySpecialization(
            @RequestParam(name = "specialization") String specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(docterServices.getDocterBySpecialization(specialization, page, size));
    }

}
