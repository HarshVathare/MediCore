package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.PatientResponceDTO;
import com.withHarsh.MediCore.DTO.ProfileRequestDTO;
import com.withHarsh.MediCore.DTO.ProfileResponceDTO;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.PatientServices;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientServices {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DocterRepository docterRepository;

    @Override
    public ProfileResponceDTO getProfile(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        String email = principal.toString();

        System.out.println("Email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Patient patient = patientRepository.findByUser(user);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        return new ProfileResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                patient.getAge(),
                patient.getGender(),
                patient.getMedicalHistory(),
                user.getRole(),
                user.getCreated_at()
        );
    }

    @Override
    public ProfileResponceDTO updateProfile(ProfileRequestDTO profileRequestDTO, Authentication authentication) {

        // Best way to get email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update only if not null (safe update)
        if (profileRequestDTO.getName() != null) {
            user.setName(profileRequestDTO.getName());
        }

        // ⚠Avoid updating email directly (JWT issue)
        // user.setEmail(profileRequestDTO.getEmail());

        // Fetch existing patient
        Patient patient = patientRepository.findByUser(user);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        // Update patient fields
        if (profileRequestDTO.getAge() != null) {
            patient.setAge(profileRequestDTO.getAge());
        }

        if (profileRequestDTO.getGender() != null) {
            patient.setGender(profileRequestDTO.getGender());
        }

        if (profileRequestDTO.getMedicalRecord() != null) {
            patient.setMedicalHistory(profileRequestDTO.getMedicalRecord());
        }

        // Save
        userRepository.save(user);

        return new ProfileResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                patient.getAge(),
                patient.getGender(),
                patient.getMedicalHistory(),
                user.getRole(),
                user.getCreated_at()
        );
    }

    @Override
    public List<PatientResponceDTO> fetchAllDocters() {

        List<Docter> docters = docterRepository.findAll();

        List<PatientResponceDTO> responceDTOList = docters
                .stream()
                .map(docter -> new PatientResponceDTO(
                        docter.getId(),
                        docter.getUser().getName(),
                        docter.getSpecialization(),
                                docter.getExperianceInYears(),
                        docter.isAvailibility_stutus()
                                )
                        )
                .toList();

        return responceDTOList;
    }


}
