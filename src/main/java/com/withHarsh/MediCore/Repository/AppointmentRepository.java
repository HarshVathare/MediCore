package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.Appointment;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.type.AppointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

//    List<Appointment> findAllByPatient_Id(Long patientId);

    List<Appointment> findAllByDocter_Id(Long docterId);

    Appointment findByDocter(Docter docter);

    List<Appointment> findByDocterAndAppointmentStatus(
            Docter docter,
            AppointType appointmentStatus);


    Page<Appointment> findAllByPatient_Id(Long patientId, PageRequest request);
}