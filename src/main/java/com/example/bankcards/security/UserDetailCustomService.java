package com.example.bankcards.security;

import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailCustomService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailCustomService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<com.example.bankcards.entity.User> user = this.userRepository.findByUsername(username);
        if (!user.isEmpty()) {
            final List<SimpleGrantedAuthority> grandAuthorities = new ArrayList<>();
            grandAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().toString()));
            return new User(user.get().getUsername(), user.get().getPassword(), grandAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
