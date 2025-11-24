package com.example.reception.service;

import com.example.reception.dto.ReceptionUserDto;
import com.example.reception.entity.ReceptionUser;
import com.example.reception.exception.UserAlreadyExistsException;
import com.example.reception.repository.ReceptionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReceptionUserServiceImpl implements ReceptionUserService {

    private final ReceptionUserRepository receptionUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(ReceptionUserDto userDto) {
        var username = userDto.getUsername();
        if (receptionUserRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException();
        }
        var newUser = ReceptionUser.builder()
                .username(username)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        receptionUserRepository.save(newUser);
    }
}
