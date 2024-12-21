package org.mediabox.mediabox.security;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.repository.UserRepository;
import org.mediabox.mediabox.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        return JwtEntityFactory.create(user);
    }
}
