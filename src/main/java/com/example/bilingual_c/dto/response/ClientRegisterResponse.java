package com.example.bilingual_c.dto.response;

import com.example.bilingual_c.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientRegisterResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String token;

    private Role role;
}
