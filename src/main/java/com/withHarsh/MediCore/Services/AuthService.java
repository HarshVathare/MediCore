package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.RefreshToken;

import com.withHarsh.MediCore.Entity.User;

import com.withHarsh.MediCore.RabbitMQ.SmtpEmailService;
import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.RefreshTokenRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SmtpEmailService emailService;


    @Transactional
    public RegisterResponceDTO register(RegisterRequestDTO registerRequestDTO) {

        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        Patient patient = new Patient();
        patient.setAge(registerRequestDTO.getAge());
        patient.setGender(registerRequestDTO.getGender());
        patient.setMedicalHistory(registerRequestDTO.getMedicalHistory());

        //set relationship
        patient.setUser(user);
        user.setPatient(patient);

        user.setVerificationToken(UUID.randomUUID().toString()); //set verification-token in DB

        userRepository.save(user);

        String link = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();

        emailService.sendEmailForVerifyAccount(
                user.getEmail(),
                "Verify Your MediCore Account",
                link
        );
        return new RegisterResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUpdated_At(),
                user.getCreated_at()
        );
    }


    public LoginResponceDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                         loginRequestDTO.getEmail(),
                         loginRequestDTO.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // IMPORTANT CHECK
        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email before login");
        }

        String accessToken = jwtUtils.generateTokenFromUsername(userDetails);
        System.out.println("Token :- "+accessToken);

//        create Refresh Token

        RefreshToken refreshToken = createRefreshToken(user.getEmail());


        return new LoginResponceDTO(
                accessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail());
    }


    private final long refreshTokenDuration = 7 * 24 * 60 * 60 * 1000;

    @Transactional// 7 days
    public RefreshToken createRefreshToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user); // ✅ FIXED

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenDuration));

        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    @Transactional
    public String verify(String token) {
         User user = userRepository.findByVerificationToken(token)
               .orElseThrow();
         user.setVerified(true);
         user.setVerificationToken(null);
         userRepository.save(user);

         return "Account verified";
}

    public RefreshTokenResponceDTO getRefreshToken(RefreshTokenRequestDTO request) {

        RefreshToken refreshToken = verifyToken(request.getRefreshToken());

        User user = refreshToken.getUser();

        String newAccessToken = jwtUtils.generateTokenFromUsername(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getAuthorities()
//                        List.of() // roles optional
                )
        );

        return new RefreshTokenResponceDTO(
                newAccessToken,
                refreshToken.getToken()
        );
    }

    @Transactional
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found..!"));

        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);

        String link = "http://localhost:8080/api/auth/reset?token=" + user.getResetToken();

        emailService.sendEmailForForgotPassword(
                user.getEmail(),
                "Reset Your MediCore Password",
                link
        );

        return "Password reset link sent to your email";
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token).orElseThrow();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        userRepository.save(user);

        return "Password updated";

    }

    @Transactional
    public String logout(RefreshTokenRequestDTO request) {

        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.delete(token);

        return "Logged out successfully";
    }


}
