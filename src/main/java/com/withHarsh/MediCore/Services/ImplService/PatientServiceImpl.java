package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.Appointment;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.User;
import com.withHarsh.MediCore.Entity.type.AppointType;
import com.withHarsh.MediCore.Repository.AppointmentRepository;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.PatientServices;
import jakarta.transaction.Transactional;
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
    private final AppointmentRepository appointmentRepository;

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

    @Override
    public List<PatientResponceDTO> getDocterBySpecialization(String specialization) {

        List<Docter> docters = docterRepository.findBySpecialization(specialization);

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
    public List<PatientResponceDTO> getDocterByExperience(String experience_in_years) {

        List<Docter> docters = docterRepository.findByExperianceInYears(experience_in_years);

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
    public List<AppointmentResponceDTO> getAppointments(Authentication authentication) {

        Object principal = authentication.getPrincipal();
        String email = principal.toString();
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("User not found by email : "+email));

        Long user_Id = user.getId();

        Patient patient = patientRepository.findByUser_Id(user_Id);
        Long patient_Id = patient.getId(); //2

        List<Appointment> appointments = appointmentRepository.findAllByPatient_Id(patient_Id);

        List<AppointmentResponceDTO> responceDTOList = appointments.stream()
                .map(appointment -> new AppointmentResponceDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDocter().getUser().getName(),
                        appointment.getPatient().getUser().getName(),
                        appointment.getAppointmentTime(),
                        appointment.getAppointmentStatus(),
                        appointment.getCreatedAt()
                )).toList();


        return responceDTOList;
    }

    @Override
    public String deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("appointment not found by Id : "+id));

        appointmentRepository.delete(appointment);

        return "Appointment deleted Successfully ..!";
    }


}
