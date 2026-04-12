package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.DocterProfileRequestDTO;
import com.withHarsh.MediCore.DTO.DocterProfileResponceDTO;

import com.withHarsh.MediCore.DTO.ProfileResponceDTO;
import com.withHarsh.MediCore.Entity.Docter;

import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.DocterServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class DocterServiceImpl implements DocterServices {

    private final UserRepository userRepository;
    private final DocterRepository docterRepository;

    public DocterServiceImpl(UserRepository userRepository, DocterRepository docterRepository) {
        this.userRepository = userRepository;
        this.docterRepository = docterRepository;
    }


    @Override
    public DocterProfileResponceDTO getProfile(Authentication authentication) {

        String email = authentication.getName(); // ✅ BEST WAY

        System.out.println("Email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Docter docter = docterRepository.findByUser_Id(user.getId()); // ✅ FIXED

        if (docter == null) {
            throw new RuntimeException("Doctor not found");
        }

        return new DocterProfileResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                docter.getSpecialization(),
                docter.getExperianceInYears(),
                docter.isAvailibility_stutus(),
                user.getRole(),
                user.getCreated_at()
        );
    }

    @Override
    public DocterProfileResponceDTO updateProfile(DocterProfileRequestDTO docterProfileRequestDTO, Authentication authentication) {

        // Best way to get email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update only if not null (safe update)
        if (docterProfileRequestDTO.getName() != null) {
            user.setName(docterProfileRequestDTO.getName());
        }

        // ⚠Avoid updating email directly (JWT issue)
        // user.setEmail(profileRequestDTO.getEmail());

        // Fetch existing patient
        Docter docter = docterRepository.findByUser(user);

        if (docter == null) {
            throw new RuntimeException("Patient not found");
        }

        // Update patient fields
        if (docterProfileRequestDTO.getSpecialization() != null) {
            docter.setSpecialization(docterProfileRequestDTO.getSpecialization());
        }

        if (docterProfileRequestDTO.getExperianceInYears() != null) {
            docter.setExperianceInYears(docterProfileRequestDTO.getExperianceInYears());
        }

        if (docterProfileRequestDTO.isAvailibility_stutus() != false) {
            docter.setAvailibility_stutus(docterProfileRequestDTO.isAvailibility_stutus());
        }

        // Save
        userRepository.save(user);

        return new DocterProfileResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                docter.getSpecialization(),
                docter.getExperianceInYears(),
                docter.isAvailibility_stutus(),
                user.getRole(),
                user.getCreated_at()
        );




    }

}
