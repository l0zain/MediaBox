package org.mediabox.mediabox.service;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.entity.Role;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.mapper.UserMapper;
import org.mediabox.mediabox.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public User getUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFreeSpace(10*1024*1024L);
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);

    }
}
