package dev.hiwa.iblog.controllers;

import dev.hiwa.iblog.domain.dto.request.LoginRequest;
import dev.hiwa.iblog.domain.dto.response.JwtResponse;
import dev.hiwa.iblog.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        JwtResponse jwtResponse = authService.login(request);

        return ResponseEntity.ok(jwtResponse);
    }
}
