package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.PatientResponceDTO;
import com.withHarsh.MediCore.DTO.ProfileRequestDTO;
import com.withHarsh.MediCore.DTO.ProfileResponceDTO;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PatientServices {

    ProfileResponceDTO getProfile(Authentication authentication);

    ProfileResponceDTO updateProfile(@Valid ProfileRequestDTO profileRequestDTO, Authentication authentication);

    List<PatientResponceDTO> fetchAllDocters();
}
