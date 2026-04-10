package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.LoginRequestDTO;
import com.withHarsh.MediCore.DTO.LoginResponceDTO;
import com.withHarsh.MediCore.DTO.RegisterRequestDTO;
import com.withHarsh.MediCore.DTO.RegisterResponceDTO;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public @Nullable RegisterResponceDTO register(RegisterRequestDTO registerRequestDTO) {

        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        Patient patient = new Patient();
        patient.setAge(registerRequestDTO.getAge());
        patient.setGender(registerRequestDTO.getGender());
        patient.setMedicalHistory(registerRequestDTO.getMedicalHistory());

        //set relationship
        patient.setUser(user);
        user.setPatient(patient);

        userRepository.save(user);

        return new RegisterResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUpdated_At(),
                user.getCreated_at()
        );
    }


    public @Nullable LoginResponceDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       loginRequestDTO.getEmail(),
                       loginRequestDTO.getPassword()
               )
        );

        // Get authenticated user (usually UserDetails)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtUtils.generateTokenFromUsername(userDetails);

        System.out.println("Generated JWT: " + token);

        // ✅ Fetch full user from DB
        User user = (User) userRepository.findByEmail(userDetails.getUsername());

        return new LoginResponceDTO(
                token,
                user.getId(),
                user.getEmail()
        );
    }
}
