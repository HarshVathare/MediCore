package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.CreateDocterRequestDTO;
import com.withHarsh.MediCore.DTO.CreateDocterResponceDTO;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Entity.type.RoleType;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.AdminServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateDocterResponceDTO createDocter(CreateDocterRequestDTO requestDTO) {

        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole(RoleType.DOCTOR);

        Docter docter = new Docter();
        docter.setSpecialization(requestDTO.getSpecialization());
        docter.setExperianceInYears(requestDTO.getExperianceInYears());
        docter.setAvailibility_stutus(requestDTO.isAvailibility_stutus());

        //Maintain the relationship
        user.setDocter(docter);
        docter.setUser(user);

        userRepository.save(user);

        return new CreateDocterResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUpdated_At(),
                user.getCreated_at()
        );
    }








}
