package com.arutech.mftracker.UserService.service;

import com.arutech.mftracker.UserService.dto.GoogleUserInfoDto;
import com.arutech.mftracker.UserService.exceptions.GoogleAuthenticationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    @Value("${google.client-id}")
    private String googleClientId;

    public GoogleUserInfoDto verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken token = verifier.verify(idToken);

            if (token == null) {
                throw new GoogleAuthenticationException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = token.getPayload();

            return GoogleUserInfoDto.builder()
                    .sub(payload.getSubject())
                    .email(payload.getEmail())
                    .name(payload.get("name").toString())
                    .pictureUrl(payload.get("picture").toString())
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            throw new GoogleAuthenticationException("Google authentication failed", e);
        }
    }
}
