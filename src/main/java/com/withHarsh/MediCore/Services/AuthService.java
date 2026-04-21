package com.withHarsh.MediCore.Services;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Entity.Patient;
import com.withHarsh.MediCore.Entity.RefreshToken;

import com.withHarsh.MediCore.Entity.User;

import com.withHarsh.MediCore.Repository.PatientRepository;
import com.withHarsh.MediCore.Repository.RefreshTokenRepository;
import com.withHarsh.MediCore.Repository.UserRepository;
import com.withHarsh.MediCore.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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


    public @Nullable RegisterResponceDTO register(RegisterRequestDTO registerRequestDTO) {

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

        userRepository.save(user);

        return new RegisterResponceDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUpdated_At(),
                user.getCreated_at()
        );
    }


    public @Nullable LoginResponceDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                         loginRequestDTO.getEmail(),
                         loginRequestDTO.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateTokenFromUsername(userDetails);
        System.out.println("Token :- "+accessToken);

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

//        create Refresh Token

        RefreshToken refreshToken = createRefreshToken(user.getEmail());


        return new LoginResponceDTO(
                accessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail());
    }


    private final long refreshTokenDuration = 7 * 24 * 60 * 60 * 1000; // 7 days
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


    public String logout(RefreshTokenRequestDTO request) {

        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.delete(token);

        return "Logged out successfully";
    }
}








//public String register(User user) {
//    user.setPassword(encoder.encode(user.getPassword()));
//    user.setVerificationToken(UUID.randomUUID().toString());
//
//    userRepository.save(user);
//
//    emailService.sendEmail(user.getEmail(),
//            "Verify Account",
//            "Click: http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken());
//
//    return "User registered. Check email.";
//}
//
//public String verify(String token) {
//    User user = userRepository.findByVerificationToken(token)
//            .orElseThrow();
//
//    user.setVerified(true);
//    user.setVerificationToken(null);
//    userRepository.save(user);
//
//    return "Account verified";
//}
//
//public String login(String email, String password) {
//    User user = userRepository.findByEmail(email).orElseThrow();
//
//    if (!encoder.matches(password, user.getPassword())) {
//        throw new RuntimeException("Invalid password");
//    }
//
//    if (!user.isVerified()) {
//        throw new RuntimeException("Email not verified");
//    }
//
//    return jwtUtil.generateToken(email);
//}
//
//public String forgotPassword(String email) {
//    User user = userRepository.findByEmail(email).orElseThrow();
//
//    user.setResetToken(UUID.randomUUID().toString());
//    userRepository.save(user);
//
//    emailService.sendEmail(email,
//            "Reset Password",
//            "Click: http://localhost:8080/api/auth/reset?token=" + user.getResetToken());
//
//    return "Reset link sent";
//}
//
//public String resetPassword(String token, String newPassword) {
//    User user = userRepository.findByResetToken(token).orElseThrow();
//
//    user.setPassword(encoder.encode(newPassword));
//    user.setResetToken(null);
//
//    userRepository.save(user);
//
//    return "Password updated";