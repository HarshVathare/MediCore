package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.Docter;
import com.withHarsh.MediCore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocterRepository extends JpaRepository<Docter, Long> {

    Docter findByUser_Id(Long userId);

    Docter findByUser(User user);

//    List<Docter> findByAllSpecialization(String specialization);

    List<Docter> findBySpecialization(String specialization);

    List<Docter> findByExperianceInYears(String experienceInYears);


//    List<Docter> findByExperienceInYears(String experienceInYears);

//    List<Docter> findByExperienceInYears(String experienceInYears);

//    List<Docter> findBySpecializationAndExperienceInYears(
//            String specialization,
//            String experienceInYears
//    );

}