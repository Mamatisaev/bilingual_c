package com.example.bilingual_c.service;

import com.example.bilingual_c.dto.request.AuthInfoRequest;
import com.example.bilingual_c.dto.request.ClientRegisterRequest;
import com.example.bilingual_c.dto.response.AuthInfoResponse;
import com.example.bilingual_c.dto.response.ClientRegisterResponse;
import com.example.bilingual_c.dto.response.SimpleResponse;
import com.example.bilingual_c.entity.AuthInfo;
import com.example.bilingual_c.entity.Client;
import com.example.bilingual_c.entity.enums.Role;
import com.example.bilingual_c.exceptions.BadCredentialsException;
import com.example.bilingual_c.exceptions.BadRequestException;
import com.example.bilingual_c.repository.AuthInfoRepository;
import com.example.bilingual_c.repository.ClientRepository;
import com.example.bilingual_c.security.jwt.JwtUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthInfoService {


    private final AuthInfoRepository authInfoRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @PostConstruct
    public void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("bilingual_c.json")
                        .getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();

        FirebaseApp.initializeApp(firebaseOptions);
    }

    public AuthInfoResponse login(AuthInfoRequest authInfoRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authInfoRequest.getEmail(),
                        authInfoRequest.getPassword()));

        AuthInfo authInfo = authInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("bad credentials"));

        if (authInfoRequest.getPassword().isBlank()) {
            throw new BadRequestException("password cannot be empty");
        }

        if (!passwordEncoder.matches(authInfoRequest.getPassword(), authInfo.getPassword())) {
            throw new BadCredentialsException("incorrect password");
        }

        String token = jwtUtils.generateToken(authInfo.getEmail());
        return new AuthInfoResponse(authInfo.getUsername(), token, authInfo.getRole());
    }

    public ClientRegisterResponse register(ClientRegisterRequest clientRegisterRequest) {

    if (clientRegisterRequest.getPassword().isBlank()) {
        throw new BadRequestException("Password cannot be empty!");
    }

    if (authInfoRepository.existsAuthInfoByEmail(clientRegisterRequest.getEmail())) {
        throw new BadRequestException("This email: " +
                clientRegisterRequest.getEmail() + " is already in use!");
    }

        clientRegisterRequest.setPassword(passwordEncoder.encode(clientRegisterRequest.getPassword()));

        Client client = new Client(clientRegisterRequest);

        Client saveClient = clientRepository.save(client);

        String token = jwtUtils.generateToken(saveClient.getAuthInfo().getEmail());

        return new ClientRegisterResponse(
                saveClient.getFirstName(),
                saveClient.getLastName(),
                saveClient.getAuthInfo().getEmail(),
                token,
                saveClient.getAuthInfo().getRole());
    }

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {

        AuthInfo authInfo = authInfoRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("client not found"));

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                true, "UTF-8");

        mimeMessageHelper.setSubject("[bilingual_c] reset password link");

        mimeMessageHelper.setFrom("bilingual-b6@gmail.com");

        mimeMessageHelper.setTo(email);

        mimeMessageHelper.setText(link + "/" + authInfo.getId(), true);

        javaMailSender.send(mimeMessage);

        return new SimpleResponse("email send");
    }

    public SimpleResponse resetPassword(Long id, String newPassword) {

        AuthInfo authInfo = authInfoRepository.getById(id);

        authInfo.setPassword(passwordEncoder.encode(newPassword));

        return new SimpleResponse("password updated");
    }

    public AuthInfoResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);

        Client client;

        if (!authInfoRepository.existsAuthInfoByEmail(firebaseToken.getEmail())) {

            Client newClient = new Client();

            newClient.setFirstName(firebaseToken.getName());

            newClient.setAuthInfo(new AuthInfo(firebaseToken.getEmail(), firebaseToken.getEmail(), Role.CLIENT));

            client = clientRepository.save(newClient);
        }
        client = clientRepository.findClientByAuthInfoEmail(firebaseToken.getEmail());

        String token = jwtUtils.generateToken(client.getAuthInfo().getEmail());

        return new AuthInfoResponse(client.getAuthInfo().getEmail(),
                token,
                client.getAuthInfo().getRole());
    }
}