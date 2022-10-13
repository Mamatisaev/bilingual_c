package com.example.bilingual_c.dto.response;

import com.example.bilingual_c.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthInfoResponse {

    private String email;

    private String token;

    private Role role;
}
