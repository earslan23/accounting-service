package com.accounting.ilab.service.impl;

import com.accounting.ilab.entity.Role;
import com.accounting.ilab.entity.User;
import com.accounting.ilab.exception.BadRequestException;
import com.accounting.ilab.mapper.UserMapper;
import com.accounting.ilab.model.enums.RoleTypes;
import com.accounting.ilab.model.request.LoginRequestDto;
import com.accounting.ilab.model.request.RegisterRequestDto;
import com.accounting.ilab.model.response.JwtAuthenticationResponse;
import com.accounting.ilab.repos.UserRepository;
import com.accounting.ilab.service.AuthenticationService;
import com.accounting.ilab.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Override
    public JwtAuthenticationResponse register(RegisterRequestDto request) {

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        final User user = userMapper.fromDtoToEntity(request);
        final RoleTypes role = RoleTypes.getById(request.getRoleId());
        user.setRole(new Role(role.getId(), role));

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder()
                .expireIn(new Date(System.currentTimeMillis() + expirationTime))
                .token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByUserName(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder()
                .expireIn(new Date(System.currentTimeMillis() + expirationTime))
                .token(jwt).build();

    }
}
