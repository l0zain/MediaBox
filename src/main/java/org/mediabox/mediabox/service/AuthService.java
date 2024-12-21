package org.mediabox.mediabox.service;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.dto.auth.JwtRequest;
import org.mediabox.mediabox.dto.auth.JwtResponse;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userService.getUserByEmail(loginRequest.getEmail());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getEmail());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().name()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail()));
        return jwtResponse;
    }

    public JwtResponse refreshToken(String refreshRequest) throws AccessDeniedException {
        return jwtTokenProvider.refreshUserTokens(refreshRequest);
    }
}
