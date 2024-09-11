package ru.gunkin.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gunkin.abstractions.UserRepository;
import ru.gunkin.contracts.AuthenticationService;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;
import ru.gunkin.resterror.exaptions.AlreadyExistsException;
import ru.gunkin.resterror.exaptions.NotFoundException;
import ru.gunkin.models.security.AuthenticationRequest;
import ru.gunkin.models.security.RegisterRequest;
import ru.gunkin.models.user.Role;
import ru.gunkin.models.user.User;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OwnerServices ownerServices;

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }

        var user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var ownerDto = OwnerDto.builder()
                .name(registerRequest.getName())
                .birthDate(registerRequest.getBirthDate())
                .build();

        ownerServices.addOwner(registerRequest.getUsername(), ownerDto);

        return jwtService.generateToken(user);
    }

    @Override
    @Transactional
    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new NotFoundException("Invalid username or password");
        }

        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("Invalid username or password"));

        return jwtService.generateToken(user);
    }

    @Transactional
    @KafkaListener(topics = "setOwnerToUser")
    protected void addOwner(AddOwnerDto addDto) {
        var user = userRepository.findByUsername(addDto.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setOwnerId(addDto.getOwnerDto().getId());
        userRepository.save(user);
    }
}
