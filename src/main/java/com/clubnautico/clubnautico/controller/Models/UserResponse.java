package com.clubnautico.clubnautico.controller.Models;

import com.clubnautico.clubnautico.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String lastname;
    private String username;
    private Role role;
    private Boolean isPatron;
}
