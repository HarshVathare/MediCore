package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocterServices {

    DocterProfileResponceDTO getProfile(Authentication authentication);

    DocterProfileResponceDTO updateProfile(@Valid DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication);

    List<DocterAppointmentResponceDTO> getAppointments(Authentication authentication);

    String updateAppointmentStatus(Long id, UpdateAppointmentRequestDTO requestDTO);

    MedicalRecordResponceDTO createMedicalRecord(Long appointmentId ,MedicalRecordRequestDTO requestDTO);

    List<MedicalRecordResponceDTO> getMedicalRecordByPatientId(Long patientId);
}
