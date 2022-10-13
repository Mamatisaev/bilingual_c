package com.example.bilingual_c.security;

import com.example.bilingual_c.repository.AuthInfoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthInfoRepository authInfoRepository;

    public UserDetailsServiceImpl(AuthInfoRepository authInfoRepository) {
        this.authInfoRepository = authInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authInfoRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("not found"));
    }
}