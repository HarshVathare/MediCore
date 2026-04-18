package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PatientServices {

    ProfileResponceDTO getProfile(Authentication authentication);

    ProfileResponceDTO updateProfile(@Valid ProfileRequestDTO profileRequestDTO, Authentication authentication);

    List<PatientResponceDTO> fetchAllDocters();

    AppointmentResponceDTO createAppointment(AppointmentRequestDTO requestDTO, Authentication authentication);

    PatientResponceDTO getDocterById(Long id);

    List<AppointmentResponceDTO> getAppointments(Authentication authentication);

    String deleteAppointment(Long id);

    List<PatientResponceDTO> getDocterByExperience(String experienceInYears);

    List<PatientResponceDTO> getDocterBySpecialization(String specialization);
}
