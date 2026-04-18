package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.CreateDocterRequestDTO;
import com.withHarsh.MediCore.DTO.CreateDocterResponceDTO;
import com.withHarsh.MediCore.DTO.PatientResponceDTO;
import com.withHarsh.MediCore.DTO.RegisterResponceDTO;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AdminServices {

    CreateDocterResponceDTO createDocter(CreateDocterRequestDTO requestDTO);

    List<PatientResponceDTO> fetchAllDocters();

    String deleteDocterById(Long id);

    List<RegisterResponceDTO> fetchAllUsers();

    String deleteUserById(Long id);

    List<PatientResponceDTO> getDocterBySpecialization(String specialization);

    List<PatientResponceDTO> getDocterByExperience(String experienceInYears);

//    List<PatientResponceDTO> getDocterByExperience(String experience_in_years);
//
//    List<PatientResponceDTO> getDoctorBySpecializationAndExperience(String specialization, String experienceInYears);
}
