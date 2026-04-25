package com.withHarsh.MediCore.Repository;


import com.withHarsh.MediCore.Entity.Medical_Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Medical_RecordsRepository extends JpaRepository<Medical_Records, Long> {

    List<Medical_Records> findAllByPatient_Id(Long patientId);

//    <Medical_Records> findByPatient_Id(Long patientId);
}
