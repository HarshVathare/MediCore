package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocterServices {

    DocterProfileResponceDTO getProfile(Authentication authentication);

    DocterProfileResponceDTO updateProfile(@Valid DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication);

    String updateAppointmentStatus(Long id, UpdateAppointmentRequestDTO requestDTO);

    MedicalRecordResponceDTO createMedicalRecord(Long appointmentId ,MedicalRecordRequestDTO requestDTO);

    List<MedicalRecordResponceDTO> getMedicalRecordByPatientId(Long patientId);

    List<PatientResponceDTO> getDocterBySpecialization(String specialization,int page, int size);

    List<DocterAppointmentResponceDTO> getAppointmentByStatus(String status, Authentication authentication, int page, int size);

    List<DocterAppointmentResponceDTO> getAppointments(Authentication authentication, int page, int size);

}
