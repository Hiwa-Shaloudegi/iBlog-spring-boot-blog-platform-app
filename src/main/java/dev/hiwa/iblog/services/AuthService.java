package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.LoginRequest;
import dev.hiwa.iblog.domain.dto.response.JwtResponse;
import dev.hiwa.iblog.domain.entities.User;
import dev.hiwa.iblog.repositories.UserRepository;
import dev.hiwa.iblog.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                                                   request.getPassword()
        ));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);

    }
}
