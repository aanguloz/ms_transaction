package com.project.ms_transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/secure")
@RequiredArgsConstructor
public class SecureController {

    @GetMapping("/test")
    public Map<String, Object> SecureEndPoint(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Token validado correctamente");
        response.put("subject", jwt.getSubject());
        response.put("roles", jwt.getClaim("roles"));
        response.put("issuer", jwt.getIssuer() != null ? jwt.getIssuer().toString() : "N/A");
        response.put("expiresAt", jwt.getExpiresAt());
        return response;
    }

}
