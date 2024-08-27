package com.example.encodeVideo.service;

import com.example.encodeVideo.model.Users;
import com.example.encodeVideo.model.UserPrincipal;
import com.example.encodeVideo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);

        if(users == null){
            System.out.println("User Not found");
            throw new UsernameNotFoundException("not found");
        }
        return new UserPrincipal(users);
    }
}
