package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

      Patient findByUser(User user);

      Patient findByUser_Id(Long userId);
}