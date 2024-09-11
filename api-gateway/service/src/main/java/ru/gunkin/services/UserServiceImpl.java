package ru.gunkin.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.gunkin.contracts.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
