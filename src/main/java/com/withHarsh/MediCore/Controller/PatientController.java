package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Services.PatientServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientServices patientServices;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponceDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(patientServices.getProfile(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponceDTO> UpdateProfile(@Valid @RequestBody ProfileRequestDTO profileRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(patientServices.updateProfile(profileRequestDTO, authentication));
    }

    @GetMapping("/docters")
    public ResponseEntity<List<PatientResponceDTO>> fetchAllDocters() {
        return ResponseEntity.ok(patientServices.fetchAllDocters());
    }

    @GetMapping("/docters/{id}")
    public ResponseEntity<PatientResponceDTO> getDocterById(@PathVariable Long id) {
        return ResponseEntity.ok(patientServices.getDocterById(id));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponceDTO> createAppointment(@RequestBody AppointmentRequestDTO requestDTO , Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientServices.createAppointment(requestDTO, authentication));
    }
}
