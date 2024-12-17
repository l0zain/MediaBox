package org.mediabox.mediabox.service;

import lombok.AllArgsConstructor;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.repository.UserRepository;
import org.mediabox.mediabox.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User %s not found", email)));
        return UserDetailsImpl.build(user);
    }
}
