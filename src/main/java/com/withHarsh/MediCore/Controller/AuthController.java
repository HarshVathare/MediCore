package com.withHarsh.MediCore.Controller;

import com.withHarsh.MediCore.DTO.*;
import com.withHarsh.MediCore.Services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponceDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponceDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponceDTO> RefreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return ResponseEntity.ok(authService.getRefreshToken(refreshTokenRequestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.logout(request));
    }
}










//@PostMapping("/register")
//public String register(@RequestBody User user) {
//    return service.register(user);
//}
//
//@GetMapping("/verify")
//public String verify(@RequestParam String token) {
//    return service.verify(token);
//}
//
//@PostMapping("/login")
//public String login(@RequestBody User user) {
//    return service.login(user.getEmail(), user.getPassword());
//}
//
//@PostMapping("/forgot-password")
//public String forgot(@RequestParam String email) {
//    return service.forgotPassword(email);
//}
//
//@PostMapping("/reset-password")
//public String reset(@RequestParam String token,
//                    @RequestParam String password) {
//    return service.resetPassword(token, password);
//}