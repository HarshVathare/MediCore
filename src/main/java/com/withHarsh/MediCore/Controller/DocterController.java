package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Services.DocterServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docters")
@RequiredArgsConstructor
public class DocterController {

    private final DocterServices docterServices;

    @GetMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(docterServices.getProfile(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> UpdateProfile(@Valid @RequestBody DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(docterServices.updateProfile(docterProfileRequestDTO, authentication));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<DocterAppointmentResponceDTO>> getAppointments(Authentication authentication) {
        return ResponseEntity.ok(docterServices.getAppointments(authentication));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long id, @RequestBody UpdateAppointmentRequestDTO requestDTO) {
        return ResponseEntity.ok(docterServices.updateAppointmentStatus(id, requestDTO));
    }

    @PostMapping("/{appointmentId}/medical-record")
    public ResponseEntity<MedicalRecordResponceDTO> createMedicalRecord(@PathVariable Long appointmentId, @RequestBody MedicalRecordRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(docterServices.createMedicalRecord(appointmentId ,requestDTO));
    }

}
