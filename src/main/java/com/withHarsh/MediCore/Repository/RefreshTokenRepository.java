package com.withHarsh.MediCore.Repository;

import com.withHarsh.MediCore.Entity.RefreshToken;
import com.withHarsh.MediCore.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

//    void deleteByUser(com.withHarsh.MediCore.Entity.User user);
}