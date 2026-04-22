package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.RabbitMQ.MessageProducer;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Services.PatientServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientServices patientServices;
    private final MessageProducer producer;
    private final PatientRepository patientRepository;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponceDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(patientServices.getProfile(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponceDTO> UpdateProfile(@Valid @RequestBody ProfileRequestDTO profileRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(patientServices.updateProfile(profileRequestDTO, authentication));
    }

    @GetMapping("/docters")
    public ResponseEntity<List<PatientResponceDTO>> fetchAllDocters(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String experienceInYears,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {

        if (specialization != null) {
            return ResponseEntity.ok(patientServices.getDocterBySpecialization(specialization, page, size));
        }

        if (experienceInYears != null) {
            return ResponseEntity.ok(patientServices.getDocterByExperience(experienceInYears, page, size));
        }

        return ResponseEntity.ok(patientServices.fetchAllDocters( page, size));
    }

    @GetMapping("/docters/{id}")
    public ResponseEntity<PatientResponceDTO> getDocterById(@PathVariable Long id) {
        return ResponseEntity.ok(patientServices.getDocterById(id));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponceDTO> createAppointment(@RequestBody AppointmentRequestDTO requestDTO , Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientServices.createAppointment(requestDTO, authentication));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponceDTO>> getAppointments(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(patientServices.getAppointments(authentication, page, size));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(patientServices.deleteAppointment(id));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDTO requestDTO,
            Authentication authentication
    ) {
        try {
            String response = patientServices.changePassword(requestDTO, authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Upload File API
    @PostMapping("/{id}/report")
    public ResponseEntity<String> uploadReport(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return ResponseEntity.ok(patientServices.uploadReport(id, file));
    }

    // Download File API
    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> getReport(@PathVariable Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + patient.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, patient.getFileType())
                .body(patient.getMedicalReport());
    }
}
