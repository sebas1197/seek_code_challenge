package com.seek.infrastructure.security;

import com.seek.domain.model.User;
import com.seek.infrastructure.persistence.JpaUserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Lógica de login y registro que genera JWT al usuario.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JpaUserRepository userRepo;
    private final PasswordEncoder   encoder;
    private final AuthenticationManager authManager;
    private final JwtService        jwtService;

    public String login(String username, String password) {

        /* 1 · Autenticar contra Spring Security */
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

        /* 2 · Emitir JWT */
        return jwtService.createToken(username);
    }

    public void register(String username, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));
        userRepo.save(user);
    }
}
