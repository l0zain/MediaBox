package org.mediabox.mediabox.controller.auth;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.dto.user.UserRegisterDto;
import org.mediabox.mediabox.dto.auth.JwtRequest;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.mapper.UserMapper;
import org.mediabox.mediabox.dto.auth.JwtResponse;
import org.mediabox.mediabox.service.AuthService;
import org.mediabox.mediabox.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
     public JwtResponse login(@RequestBody JwtRequest loginRequest) {
         return authService.login(loginRequest);
     }

    @PostMapping("/register")
     public UserRegisterDto register(@RequestBody UserRegisterDto userDto) {
         User user = userMapper.toEntity(userDto);
         User createdUser = userService.create(user);
         return userMapper.toDto(createdUser);
     }

    @PostMapping("/refresh")
     public JwtResponse refresh(@RequestBody String refreshToken) throws AccessDeniedException {
         return authService.refreshToken(refreshToken);
     }
}
