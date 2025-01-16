package com.clubnautico.clubnautico.controller.Models;

import com.clubnautico.clubnautico.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String lastname;
    private String username;
    private String password;
    private Role role;
    private Boolean isPatron;
}
