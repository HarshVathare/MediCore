package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.CreateDocterRequestDTO;
import com.withHarsh.MediCore.DTO.CreateDocterResponceDTO;
import org.jspecify.annotations.Nullable;

public interface AdminServices {
    CreateDocterResponceDTO createDocter(CreateDocterRequestDTO requestDTO);
}
