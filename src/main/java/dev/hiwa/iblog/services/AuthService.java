package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.LoginRequest;
import dev.hiwa.iblog.domain.dto.request.RegisterRequest;
import dev.hiwa.iblog.domain.dto.response.JwtResponse;
import dev.hiwa.iblog.domain.dto.response.UserDto;
import dev.hiwa.iblog.domain.entities.User;
import dev.hiwa.iblog.exceptions.ResourceAlreadyExistsException;
import dev.hiwa.iblog.mappers.UserMapper;
import dev.hiwa.iblog.repositories.UserRepository;
import dev.hiwa.iblog.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                                                   request.getPassword()
        ));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtUtil.generateAccessToken(user);
//        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new JwtResponse(accessToken);

    }

    public UserDto register(RegisterRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent())
            throw new ResourceAlreadyExistsException("User", "Email", request.getEmail());

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
