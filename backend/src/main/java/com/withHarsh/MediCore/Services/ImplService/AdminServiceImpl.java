package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Entity.type.RoleType;
import com.withHarsh.MediCore.RabbitMQ.SmtpEmailService;
import com.withHarsh.MediCore.Repository.AppointmentRepository;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.AdminServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocterRepository docterRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final SmtpEmailService emailService;

    @Transactional
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

        user.setVerificationToken(UUID.randomUUID().toString()); //set Verification Token in DB

        userRepository.save(user);

        String link = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();

        emailService.sendEmailForVerifyAccount(
                user.getEmail(),
                "Verify Your MediCore Account",
                link
        );

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
    public List<PatientResponceDTO> fetchAllDocters(int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> doctorPage = docterRepository.findAll(request);

        return doctorPage.getContent()
                .stream()
                .map(docter -> new PatientResponceDTO(
                        docter.getId(),
                        docter.getUser().getName(),
                        docter.getSpecialization(),
                        docter.getExperianceInYears(),
                        docter.isAvailibility_stutus()
                ))
                .toList();
    }

    @Transactional
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
    public List<RegisterResponceDTO> fetchAllUsers(int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<User> users = userRepository.findAll(request);

        return users.getContent()
               .stream()
                .map(user -> new RegisterResponceDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getUpdated_At(),
                        user.getCreated_at()
                )).toList();

    }

    @Transactional
    @Override
    public String deleteUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("User not found by Id : "+ id));

        userRepository.delete(user);

        return "User deleted Successfully ..!";
    }


    @Override
    public List<PatientResponceDTO> getDocterBySpecialization(String specialization,int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findBySpecialization(specialization,request);

        return docters.getContent()
                .stream().map(
                        docter -> new PatientResponceDTO(
                                docter.getId(),
                                docter.getUser().getName(),
                                docter.getSpecialization(),
                                docter.getExperianceInYears(),
                                docter.isAvailibility_stutus()
                        )
                ).toList();

    }

    @Override
    public List<PatientResponceDTO> getDocterByExperience(String experience_in_years,int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findByExperianceInYears(experience_in_years, request);

        List<PatientResponceDTO> docterlist = docters
                .stream()
                .map(docter -> new PatientResponceDTO(
                        docter.getId(),
                        docter.getUser().getName(),
                        docter.getSpecialization(),
                        docter.getExperianceInYears(),
                        docter.isAvailibility_stutus()
                )).toList();

        return docterlist;
    }

    @Override
    public DashboardResponseDTO getDashboard() {

        long totalPatients = patientRepository.count();
        long totalDocters = docterRepository.count();
        long totalAppointments = appointmentRepository.count();

        // For today's appointments
        LocalDate todayDate = LocalDate.now();
        LocalDateTime start = todayDate.atStartOfDay();
        LocalDateTime end = todayDate.atTime(23, 59, 59);

        long todayAppointments = appointmentRepository
                .countByAppointmentTimeBetween(start, end);

        return new DashboardResponseDTO(
                totalPatients,
                totalDocters,
                totalAppointments,
                todayAppointments
        );
    }


}
