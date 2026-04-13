package com.withHarsh.MediCore.Services.ImplService;

import com.withHarsh.MediCore.DTO.*;

import com.withHarsh.MediCore.Entity.*;

import com.withHarsh.MediCore.Entity.type.AppointType;
import com.withHarsh.MediCore.Repository.AppointmentRepository;
import com.withHarsh.MediCore.Repository.DocterRepository;
import com.withHarsh.MediCore.Repository.Medical_RecordsRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Services.DocterServices;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocterServiceImpl implements DocterServices {

    private final UserRepository userRepository;
    private final DocterRepository docterRepository;
    private final AppointmentRepository appointmentRepository;
    private final Medical_RecordsRepository medical_RecordsRepository;

    public DocterServiceImpl(UserRepository userRepository, DocterRepository docterRepository,
                             AppointmentRepository appointmentRepository,
                             Medical_RecordsRepository medical_RecordsRepository) {
        this.userRepository = userRepository;
        this.docterRepository = docterRepository;
        this.appointmentRepository = appointmentRepository;
        this.medical_RecordsRepository = medical_RecordsRepository;
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

    @Override
    public List<DocterAppointmentResponceDTO> getAppointments(Authentication authentication) {

        Object principle = authentication.getPrincipal();
        String email = principle.toString();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long user_Id = user.getId();

        Docter docter = docterRepository.findByUser_Id(user_Id);
        Long docter_Id = docter.getId(); //docter id 10

        List<Appointment> appointments = appointmentRepository.findAllByDocter_Id(docter_Id);

        //Convert to DTO
        List<DocterAppointmentResponceDTO> responceDTOList = appointments.stream()
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

        return responceDTOList;
    }

    @Override
    @Transactional
    public String updateAppointmentStatus(Long id, UpdateAppointmentRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

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

        return "Appointment Confirmed ..!";
    }

    @Override
    public MedicalRecordResponceDTO createMedicalRecord(Long appointmentId, MedicalRecordRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        // ✅ Check if already completed
        if (appointment.getAppointmentStatus() == AppointType.COMPLETED) {
            throw new IllegalStateException("Medical record already created for this appointment");
        }

        Docter doctor = appointment.getDocter();
        Patient patient = appointment.getPatient();

        // ✅ Create Prescription
        Prescription prescription = new Prescription();
        prescription.setMedicine(requestDTO.getPrescription());
        prescription.setNotes(requestDTO.getNotes());

        // ✅ Create Medical Record
        Medical_Records record = new Medical_Records();
        record.setDiagnoses(requestDTO.getDiagnosis());
        record.setDocter(doctor);
        record.setPatient(patient);
        record.setAppointment(appointment);
        record.setPrescription(prescription);

        // ✅ If bidirectional mapping
        prescription.setMedicalRecords(record);

        // ✅ Save (ensure cascade = ALL)
        medical_RecordsRepository.save(record);

        // ✅ Update appointment
        appointment.setAppointmentStatus(AppointType.valueOf("COMPLETED"));
        doctor.setAvailibility_stutus(true);

        appointmentRepository.save(appointment);

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



//    @Override
//    public MedicalRecordResponceDTO createMedicalRecord(Long appointmentId ,MedicalRecordRequestDTO requestDTO) {
//
//        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()->
//                new IllegalArgumentException("Appointment not found"));
//
//        Docter docter = appointment.getDocter();
//        Patient patient = appointment.getPatient();
//
//        Medical_Records records = new Medical_Records();
//        records.setDiagnoses(requestDTO.getDiagnosis());
//        records.setDocter(docter);
//        records.setPatient(patient);
//        records.setAppointment(appointment);
//
//        Prescription prescription = new Prescription();
//        prescription.setMedicine(requestDTO.getPrescription());
//        prescription.setNotes(requestDTO.getNotes());
//
//        records.setPrescription(prescription);
//
//        medical_RecordsRepository.save(records);
//
//        //update appointment status & docter is available
//        appointment.setAppointmentStatus(AppointType.COMPLETED);
//        docter.setAvailibility_stutus(true);
//
//        appointmentRepository.save(appointment);
//
//
//        return new MedicalRecordResponceDTO(
//                records.getId(),
//                patient.getId(),
//                docter.getUser().getName(),
//                patient.getUser().getName(),
//                records.getDiagnoses(),
//                prescription.getMedicine(),
//                prescription.getNotes(),
//                records.getCreatedAt()
//        );
//    }


}
