package ru.gunkin.contracts;

import ru.gunkin.models.security.AuthenticationRequest;
import ru.gunkin.models.security.RegisterRequest;

public interface AuthenticationController {
    String register(RegisterRequest registerRequest);
    String authenticate(AuthenticationRequest authenticationResponse);
}
