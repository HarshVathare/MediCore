package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.Appointment;
import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.type.AppointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findByDocter(Docter docter);

//    List<Appointment> findByDocterAndAppointmentStatus(
//            Docter docter,
//            AppointType appointmentStatus);

    Page<Appointment> findAllByPatient_Id(Long patientId, PageRequest request);

    Page<Appointment> findAllByDocter_Id(Long docterId, PageRequest request);

    Page<Appointment> findByDocterAndAppointmentStatus(Docter docter, AppointType appointType, PageRequest request);

    long countByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
}