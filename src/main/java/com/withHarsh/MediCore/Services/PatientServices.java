package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PatientServices {

    ProfileResponceDTO getProfile(Authentication authentication);

    ProfileResponceDTO updateProfile(@Valid ProfileRequestDTO profileRequestDTO, Authentication authentication);

    List<PatientResponceDTO> fetchAllDocters(int page, int size);

    AppointmentResponceDTO createAppointment(AppointmentRequestDTO requestDTO, Authentication authentication);

    PatientResponceDTO getDocterById(Long id);

    List<AppointmentResponceDTO> getAppointments(Authentication authentication, int page, int size);

    String deleteAppointment(Long id);

    List<PatientResponceDTO> getDocterByExperience(String experienceInYears,int page, int size);

    List<PatientResponceDTO> getDocterBySpecialization(String specialization,int page, int size);

    String uploadReport(Long id, MultipartFile file) throws IOException;

    String changePassword(ChangePasswordRequestDTO requestDTO, Authentication authentication);

}
