package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.*;

import com.withHarsh.MediCore.Entity.*;

import com.withHarsh.MediCore.Entity.type.AppointType;
import com.withHarsh.MediCore.RabbitMQ.AppointmentEmailEventDTO;
import com.withHarsh.MediCore.RabbitMQ.MessageProducer;
import com.withHarsh.MediCore.Repository.*;
import com.withHarsh.MediCore.Services.DocterServices;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocterServiceImpl implements DocterServices {

    private final UserRepository userRepository;
    private final DocterRepository docterRepository;
    private final AppointmentRepository appointmentRepository;
    private final Medical_RecordsRepository medical_RecordsRepository;
    private final PatientRepository patientRepository;
    private final MessageProducer producer;

    public DocterServiceImpl(UserRepository userRepository, DocterRepository docterRepository,
                             AppointmentRepository appointmentRepository,
                             Medical_RecordsRepository medical_RecordsRepository,
                             PatientRepository patientRepository, MessageProducer producer) {
        this.userRepository = userRepository;
        this.docterRepository = docterRepository;
        this.appointmentRepository = appointmentRepository;
        this.medical_RecordsRepository = medical_RecordsRepository;
        this.patientRepository = patientRepository;
        this.producer = producer;
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

    @Transactional
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

    @Override
    public List<DocterAppointmentResponceDTO> getAppointments(Authentication authentication, int page, int size) {

        Object principle = authentication.getPrincipal();
        String email = principle.toString();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long user_Id = user.getId();

        PageRequest request = PageRequest.of(page,size);

        Docter docter = docterRepository.findByUser_Id(user_Id);
        Long docter_Id = docter.getId(); //docter id 10

        Page<Appointment> appointments = appointmentRepository.findAllByDocter_Id(docter_Id, request);

        //Convert to DTO
        return appointments.getContent()
                .stream()
                .map(appointment -> new DocterAppointmentResponceDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getAppointmentTime(),
                        appointment.getPatient().getUser().getName(),
                        appointment.getPatient().getUser().getEmail(),
                        appointment.getPatient().getAge(),
                        appointment.getPatient().getGender(),
                        appointment.getPatient().getMedicalHistory(),
                        appointment.getCreatedAt(),
                        appointment.getAppointmentStatus()
                )).toList();

    }

    @Transactional
    @Override
    public String updateAppointmentStatus(Long id, UpdateAppointmentRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        Patient patient = patientRepository.findByUser(appointment.getPatient().getUser());

        // ✅ update appointment
        if (requestDTO.getAppointment_status() != null) {
            appointment.setAppointmentStatus(requestDTO.getAppointment_status());
        }

        // ✅ update doctor availability
        Docter docter = appointment.getDocter();
        if (docter != null) {
            docter.setAvailibility_stutus(requestDTO.isAvailability());
        }

        appointmentRepository.save(appointment); // cascade should handle doctor

        // 🔥 STEP 1: Build Event DTO (IMPORTANT)
        AppointmentEmailEventDTO event = new AppointmentEmailEventDTO(
                appointment.getId(),
                patient.getUser().getName(),                     // patient name
                patient.getUser().getEmail(),                    // patient email
                docter.getUser().getName(),                      // doctor name
                appointment.getAppointmentTime().toString(),     // time
                appointment.getAppointmentStatus().name()        // status
        );

        producer.sendMessage(event);

        return "Appointment Confirmed ..!";
    }

    @Transactional
    @Override
    public MedicalRecordResponceDTO createMedicalRecord(Long appointmentId, MedicalRecordRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getAppointmentStatus() == AppointType.COMPLETED) {
            throw new IllegalStateException("Medical record already created");
        }

        Docter doctor = appointment.getDocter();
        Patient patient = appointment.getPatient();

        Prescription prescription = new Prescription();
        prescription.setMedicine(requestDTO.getPrescription());
        prescription.setNotes(requestDTO.getNotes());

        Medical_Records record = new Medical_Records();
        record.setDiagnoses(requestDTO.getDiagnosis());
        record.setDocter(doctor);
        record.setPatient(patient);
        record.setAppointment(appointment);
        record.setPrescription(prescription);

        prescription.setMedicalRecords(record);

        medical_RecordsRepository.save(record);

        // ✅ Update appointment
        appointment.setAppointmentStatus(AppointType.COMPLETED);

        // ✅ Update doctor
        doctor.setAvailibility_stutus(true);

        // ✅ Save both explicitly
        appointmentRepository.save(appointment);
        docterRepository.save(doctor);

        return new MedicalRecordResponceDTO(
                record.getId(),
                patient.getId(),
                doctor.getUser().getName(),
                patient.getUser().getName(),
                record.getDiagnoses(),
                prescription.getMedicine(),
                prescription.getNotes(),
                record.getCreatedAt()
        );

    }

    @Override
    public List<MedicalRecordResponceDTO> getMedicalRecordByPatientId(Long patientId) {

        Patient patient = patientRepository.findById(patientId).orElseThrow(()->
                new IllegalArgumentException("Patient Not found"));

        List<Medical_Records> medicalRecords = medical_RecordsRepository.findAllByPatient_Id(patientId);

        List<MedicalRecordResponceDTO> medicalRecordResponceDTOList = medicalRecords.stream()
                .map(records-> new MedicalRecordResponceDTO(
                        records.getId(),
                        records.getPatient().getId(),
                        records.getDocter().getUser().getName(),
                        records.getPatient().getUser().getName(),
                        records.getDiagnoses(),
                        records.getPrescription().getMedicine(),
                        records.getPrescription().getNotes(),
                        records.getCreatedAt()
                )).toList();

        return medicalRecordResponceDTOList;
    }

    @Override
    public List<PatientResponceDTO> getDocterBySpecialization(String specialization,int page, int size) {

        PageRequest request = PageRequest.of(page, size);

        Page<Docter> docters = docterRepository.findBySpecialization(specialization, request);

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
    public List<DocterAppointmentResponceDTO> getAppointmentByStatus(String status, Authentication authentication, int page, int size) {

        Object principle = authentication.getPrincipal();
        String email = principle.toString();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Docter docter = docterRepository.findByUser(user);

        PageRequest request = PageRequest.of(page, size);

        AppointType appointType;

        try {
            appointType = AppointType.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Page<Appointment> appointments =
                appointmentRepository.findByDocterAndAppointmentStatus(docter, appointType, request);


        //Convert to DTO
        return appointments.getContent()
                .stream()
                .map(appointment -> new DocterAppointmentResponceDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getAppointmentTime(),
                        appointment.getPatient().getUser().getName(),
                        appointment.getPatient().getUser().getEmail(),
                        appointment.getPatient().getAge(),
                        appointment.getPatient().getGender(),
                        appointment.getPatient().getMedicalHistory(),
                        appointment.getCreatedAt(),
                        appointment.getAppointmentStatus()
                )).toList();

    }


}
