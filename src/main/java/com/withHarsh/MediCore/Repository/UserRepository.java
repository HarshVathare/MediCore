package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    UserDetails findByUserName(String username);

    UserDetails findByEmail(String email);

//    <T> ScopedValue<T> findByEmail(String username);
//    Optional<UserDetails> findByUserName(String username);
//
//    Optional<User> findByEmail(String username);
}