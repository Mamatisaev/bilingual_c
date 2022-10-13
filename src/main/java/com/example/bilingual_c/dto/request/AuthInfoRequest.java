package com.example.bilingual_c.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthInfoRequest {

    private String email;

    private String password;
}
