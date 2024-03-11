package com.taskflow.service.impl;

import com.taskflow.config.context.TenantContext;
import com.taskflow.domain.dto.response.auth.JwtAuthenticationResponseDto;
import com.taskflow.domain.entity.User;
import com.taskflow.domain.mapper.TenantMapper;
import com.taskflow.domain.mapper.UserMapper;
import com.taskflow.exception.customexceptions.BadRequestException;
import com.taskflow.exception.customexceptions.ValidationException;
import com.taskflow.repository.RoleRepository;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.AuthenticationService;
import com.taskflow.service.JwtService;
import com.taskflow.service.TenantService;
import com.taskflow.utils.ErrorMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            @Qualifier("bcryptPasswordEncoder")
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserMapper userMapper,
            TenantMapper tenantMapper,
            TenantService tenantService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tenantService = tenantService;
        this.tenantMapper = tenantMapper;
    }
    @Override
    @Transactional
    public JwtAuthenticationResponseDto signup(User user,String organizationId) throws ValidationException {
        List<ErrorMessage> errors = new ArrayList<>();
        if( tenantService.getTenantByOrganizationName(user.getOrganizationName()).isPresent())
            errors.add(ErrorMessage.builder()
                    .field("organization name")
                    .message("organization name already exists")
                    .build()
            );
        if( tenantService.getTenantByPersonalEmail(user.getPersonalEmail()).isPresent())
            errors.add(ErrorMessage.builder()
                    .field("email")
                    .message("Email already exists")
                    .build()
            );

        if( Boolean.FALSE.equals(tenantService.checkAvailableTenant(organizationId)))
            errors.add(
                    ErrorMessage.builder()
                            .field("organization id")
                            .message("Organization id already exists")
                            .build()
            );
        if(!errors.isEmpty()) throw new ValidationException(errors);
        String email = user.getLastName() + "-" +
                UUID.randomUUID().toString().substring(0,8) + "@" +
                organizationId + ".com";
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        tenantService.createTenant(tenantMapper.userToTenant(user),organizationId);
        userRepository.save(user);
        return JwtAuthenticationResponseDto.builder()
                .accessToken(jwtService.generateToken(user))
                .user(userMapper.toDto(user))
                .build();
    }

    @Override
    public JwtAuthenticationResponseDto signin(User user) throws BadRequestException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        var optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isEmpty())
            throw new BadCredentialsException("Invalid email or password");
        return JwtAuthenticationResponseDto.builder()
                .accessToken(jwtService.generateToken(user))
                .user(userMapper.toDto(optionalUser.get()))
                .build();
    }
}
