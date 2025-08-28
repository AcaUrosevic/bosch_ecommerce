package com.bosch.ecommerce.auth;

import com.bosch.ecommerce.auth.dto.*;
import com.bosch.ecommerce.security.JwtService;
import com.bosch.ecommerce.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public void register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = User.builder()
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(Role.USER)
                .build();
        users.save(u);
    }

    public LoginResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        var token = jwt.generate(req.email(), Map.of("role", "USER"));
        return new LoginResponse(token);
    }
}
