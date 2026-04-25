package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.*;
import com.withHarsh.MediCore.Entity.type.AppointType;
import com.withHarsh.MediCore.RabbitMQ.AppointmentEmailEventDTO;
import com.withHarsh.MediCore.RabbitMQ.MessageProducer;
import com.withHarsh.MediCore.Repository.*;
import com.withHarsh.MediCore.Services.PatientServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientServices {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DocterRepository docterRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageProducer producer;
    private final Medical_RecordsRepository medicalRecordsRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
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
    public List<PatientResponceDTO> fetchAllDocters(int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findAll(request);

        return docters.getContent()
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
    }

    @Override
    public List<PatientResponceDTO> getDocterBySpecialization(String specialization,int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findBySpecialization(specialization, request);

        return docters.getContent()
                .stream()
                .map(docter -> new PatientResponceDTO(
                        docter.getId(),
                        docter.getUser().getName(),
                        docter.getSpecialization(),
                        docter.getExperianceInYears(),
                        docter.isAvailibility_stutus()
                )).toList();

    }

    @Override
    public List<PatientResponceDTO> getDocterByExperience(String experience_in_years, int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findByExperianceInYears(experience_in_years, request);

        return docters.getContent()
                .stream()
                .map(docter -> new PatientResponceDTO(
                        docter.getId(),
                        docter.getUser().getName(),
                        docter.getSpecialization(),
                        docter.getExperianceInYears(),
                        docter.isAvailibility_stutus()
                )).toList();

    }

    @Override
    public PatientResponceDTO getDocterById(Long id) {

        Docter docter = docterRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Docter not found by Id : "+id));

        return new PatientResponceDTO(
                docter.getId(),
                docter.getUser().getName(),
                docter.getSpecialization(),
                docter.getExperianceInYears(),
                docter.isAvailibility_stutus()
        );
    }

    @Transactional
    @Override
    public AppointmentResponceDTO createAppointment(AppointmentRequestDTO requestDTO, Authentication authentication) {

        Object principal = authentication.getPrincipal();
        String email = principal.toString();
        System.out.println(email);

        User user = userRepository.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("Email not found by email : "+email));

        Long user_Id = user.getId(); //get user id = 4

        Patient patient = patientRepository.findByUser_Id(user_Id);
        Long patient_Id = patient.getId();

        Docter doctor = docterRepository.findById(requestDTO.getDocter_Id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Doctor not found by id: " + requestDTO.getDocter_Id()));


        if (requestDTO.getAppointment_Date() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }

        if (!doctor.isAvailibility_stutus()) {
            throw new RuntimeException("Doctor is not available");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(requestDTO.getAppointment_Date());
        appointment.setAppointmentStatus(AppointType.PENDING);

        appointment.setPatient(patient);
        appointment.setDocter(doctor);
        //  Save
        appointmentRepository.save(appointment);

        // 🔥 STEP 1: Build Event DTO (IMPORTANT)
        AppointmentEmailEventDTO event = new AppointmentEmailEventDTO(
                appointment.getId(),
                patient.getUser().getName(),                     // patient name
                patient.getUser().getEmail(),                    // patient email
                doctor.getUser().getName(),                      // doctor name
                appointment.getAppointmentTime().toString(),     // time
                appointment.getAppointmentStatus().name()        // status
        );

        producer.sendMessage(event);

        return new AppointmentResponceDTO(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDocter().getUser().getName(),
                appointment.getPatient().getUser().getName(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentStatus(),
                appointment.getCreatedAt()
        );

    }

    @Override
    public List<AppointmentResponceDTO> getAppointments(Authentication authentication, int page, int size) {

        Object principal = authentication.getPrincipal();
        String email = principal.toString();
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("User not found by email : "+email));

        Long user_Id = user.getId();

        PageRequest request = PageRequest.of(page, size);

        Patient patient = patientRepository.findByUser_Id(user_Id);
        Long patient_Id = patient.getId(); //2

        Page<Appointment> appointments = appointmentRepository.findAllByPatient_Id(patient_Id, request);

        return appointments.getContent()
                .stream()
                .map(appointment -> new AppointmentResponceDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDocter().getUser().getName(),
                        appointment.getPatient().getUser().getName(),
                        appointment.getAppointmentTime(),
                        appointment.getAppointmentStatus(),
                        appointment.getCreatedAt()
                )).toList();

    }

    @Transactional
    @Override
    public String deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("appointment not found by Id : "+id));

        appointmentRepository.delete(appointment);

        return "Appointment deleted Successfully ..!";
    }

    @Transactional
    public String uploadReport(Long patientId, MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setFileName(file.getOriginalFilename());
        patient.setFileType(file.getContentType());
        patient.setMedicalReport(file.getBytes());

        patientRepository.saveAndFlush(patient);

        return "File uploaded successfully";
    }

    @Transactional
    @Override
    public String changePassword(ChangePasswordRequestDTO requestDTO, Authentication authentication) {

        // 1. Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        String email = authentication.getName();

        // 2. Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. Validate input
        if (requestDTO.getOldPassword() == null || requestDTO.getOldPassword().isBlank()) {
            throw new IllegalArgumentException("Old password is required");
        }

        if (requestDTO.getNewPassword() == null || requestDTO.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        // 4. Handle null password case
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("No existing password found. Use forgot password.");
        }

        // 5. Verify old password
        if (!passwordEncoder.matches(requestDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // 6. Prevent same password reuse
        if (passwordEncoder.matches(requestDTO.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be same as old password");
        }

        // 7. Extra security (recommended)
        if (requestDTO.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // 8. Update password
        String encodedPassword = passwordEncoder.encode(requestDTO.getNewPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        // 9. Debug (remove later)
        System.out.println("Password updated for user: " + email);

        return "Password changed successfully";
    }

}
