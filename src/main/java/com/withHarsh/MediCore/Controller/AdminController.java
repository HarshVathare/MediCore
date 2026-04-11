package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.CreateDocterRequestDTO;
import com.withHarsh.MediCore.DTO.CreateDocterResponceDTO;
import com.withHarsh.MediCore.DTO.PatientResponceDTO;
import com.withHarsh.MediCore.DTO.RegisterResponceDTO;
import com.withHarsh.MediCore.Services.AdminServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServices adminServices;

    @PostMapping("/docter")
    public ResponseEntity<CreateDocterResponceDTO> CreateDocter(@RequestBody CreateDocterRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminServices.createDocter(requestDTO));
    }

    @GetMapping("/docters")
    public ResponseEntity<List<PatientResponceDTO>> fetchAllDocters() {
        return ResponseEntity.ok(adminServices.fetchAllDocters());
    }

    @GetMapping("/users")
    public ResponseEntity<List<RegisterResponceDTO>> fetchAllUsers() {
        return ResponseEntity.ok(adminServices.fetchAllUsers());
    }



}
