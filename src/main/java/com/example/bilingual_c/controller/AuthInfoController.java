package com.example.bilingual_c.controller;

import com.example.bilingual_c.dto.request.AuthInfoRequest;
import com.example.bilingual_c.dto.request.ClientRegisterRequest;
import com.example.bilingual_c.dto.response.AuthInfoResponse;
import com.example.bilingual_c.dto.response.ClientRegisterResponse;
import com.example.bilingual_c.dto.response.SimpleResponse;
import com.example.bilingual_c.service.AuthInfoService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authInfo")
public class AuthInfoController {

    private final AuthInfoService authInfoService;

    @PostMapping("/login")
    public AuthInfoResponse login(@RequestBody AuthInfoRequest authInfoRequest) {
        return authInfoService.login(authInfoRequest);
    }

    @PostMapping("/register")
    public ClientRegisterResponse register(@RequestBody ClientRegisterRequest clientRegisterRequest) {
        return authInfoService.register(clientRegisterRequest);
    }

    @GetMapping("/forgotPassword")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link)
            throws MessagingException {
        return authInfoService.forgotPassword(email, link);
    }

    @PostMapping("resetPassword/{id}")
    public SimpleResponse resetPassword(@PathVariable Long id, String newPassword) {
        return authInfoService.resetPassword(id, newPassword);
    }

    @PostMapping("/authenticate/google")
    public AuthInfoResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        return authInfoService.authWithGoogleAccount(tokenId);
    }
}
