package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;


import java.util.List;

public interface AdminServices {

    CreateDocterResponceDTO createDocter(CreateDocterRequestDTO requestDTO);

    List<PatientResponceDTO> fetchAllDocters(int page, int size);

    String deleteDocterById(Long id);

    List<RegisterResponceDTO> fetchAllUsers(int page, int size);

    String deleteUserById(Long id);

    List<PatientResponceDTO> getDocterBySpecialization(String specialization, int page, int size);

    List<PatientResponceDTO> getDocterByExperience(String experienceInYears, int page, int size);

    DashboardResponseDTO getDashboard();

}
