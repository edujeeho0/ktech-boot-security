package com.example.auth;

import com.example.auth.entity.CustomUserDetails;
import com.example.auth.entity.UserEntity;
import com.example.auth.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public UserService(
            PasswordEncoder passwordEncoder,
            UserRepository repository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword(passwordEncoder
                .encode("password"));
        user1.setEmail("user1@a.a");
        user1.setPhone("010-asdf-zxcv");
        user1.setAuthorities("ROLE_USER,READ");
        this.repository.save(user1);
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder
                .encode("password"));
        admin.setAuthorities("ROLE_ADMIN,READ,WRITE");
        this.repository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("loadUserByUsername in UserService by me!");
        Optional<UserEntity> optionalUser =
                repository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
//        return User
//                .withUsername(username)
//                .password(optionalUser.get().getPassword())
//                .build();
        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    public void createUser(String username, String password, String passCheck) {
        if (userExists(username) || !password.equals(passCheck))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAuthorities("ROLE_USER,READ");
        // => { "ROLE_USER", "READ" }
        repository.save(newUser);
    }

    public boolean userExists(String username) {
        return repository.existsByUsername(username);
    }
}
