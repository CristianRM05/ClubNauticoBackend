package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.controller.Models.AuthResponse;
import com.clubnautico.clubnautico.controller.Models.AuthenticateRequest;
import com.clubnautico.clubnautico.controller.Models.RegisterRequest;
import com.clubnautico.clubnautico.entity.User;
import org.springframework.stereotype.Service;


public interface AauthService {

     AuthResponse register (RegisterRequest registerRequest);

    AuthResponse authenticate (AuthenticateRequest authenticateRequest);

    User getAuthenticatedUser();
}
