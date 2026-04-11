package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.CreateDocterRequestDTO;
import com.withHarsh.MediCore.DTO.CreateDocterResponceDTO;
import com.withHarsh.MediCore.DTO.PatientResponceDTO;
import com.withHarsh.MediCore.DTO.RegisterResponceDTO;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Entity.type.RoleType;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.AdminServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocterRepository docterRepository;

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

    @Override
    public String deleteDocterById(Long id) {

        //get docter id
        Docter docter = docterRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Docter not found by Id : "+id));

        Long user_Id = docter.getUser().getId(); //getting User_Id : 12

        User user = userRepository.findById(user_Id).orElseThrow(()->
                new IllegalArgumentException("User not found by Id : "+user_Id));

        userRepository.delete(user);

        return "Docter deleted Successfully ..!";
    }

    @Override
    public List<RegisterResponceDTO> fetchAllUsers() {

        List<User> users = userRepository.findAll();

        List<RegisterResponceDTO> responceDTOList = users.stream()
                .map(user -> new RegisterResponceDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getUpdated_At(),
                        user.getCreated_at()
                )).toList();

        return responceDTOList;
    }

    @Override
    public String deleteUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("User not found by Id : "+ id));

        userRepository.delete(user);

        return "User deleted Successfully ..!";
    }


}
