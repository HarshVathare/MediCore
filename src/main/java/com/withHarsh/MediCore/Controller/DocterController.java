package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.DocterProfileRequestDTO;
import com.withHarsh.MediCore.DTO.DocterProfileResponceDTO;
import com.withHarsh.MediCore.DTO.ProfileRequestDTO;
import com.withHarsh.MediCore.DTO.ProfileResponceDTO;
import com.withHarsh.MediCore.Services.DocterServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/docters")
@RequiredArgsConstructor
public class DocterController {

    private final DocterServices docterServices;

    @GetMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(docterServices.getProfile(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<DocterProfileResponceDTO> UpdateProfile(@Valid @RequestBody DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(docterServices.updateProfile(docterProfileRequestDTO, authentication));
    }


}
