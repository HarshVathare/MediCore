package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.DocterAppointmentResponceDTO;
import com.withHarsh.MediCore.DTO.DocterProfileRequestDTO;
import com.withHarsh.MediCore.DTO.DocterProfileResponceDTO;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocterServices {


    DocterProfileResponceDTO getProfile(Authentication authentication);

    DocterProfileResponceDTO updateProfile(@Valid DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication);

    List<DocterAppointmentResponceDTO> getAppointments(Authentication authentication);
}
