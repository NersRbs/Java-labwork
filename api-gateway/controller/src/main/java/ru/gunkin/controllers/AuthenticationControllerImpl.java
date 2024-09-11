package ru.gunkin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gunkin.contracts.AuthenticationController;
import ru.gunkin.contracts.AuthenticationService;
import ru.gunkin.models.security.AuthenticationRequest;
import ru.gunkin.models.security.RegisterRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {
    private final AuthenticationService authenticationService;
    @Override
    @PostMapping("/signup")
    public String register(@RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @Override
    @PostMapping("/login")
    public String authenticate(@RequestBody AuthenticationRequest authenticationResponse) {
        return authenticationService.authenticate(authenticationResponse);
    }
}
