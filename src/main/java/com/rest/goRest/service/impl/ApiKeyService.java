package com.rest.goRest.service.impl;

import com.rest.goRest.dao.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ApiKeyService {

    private final TokenRepository tokenRepository;

    public boolean isValidApiKey(String apiKey) {
        var token = tokenRepository.findByToken(apiKey).orElse(null);
        if (!Objects.isNull(token)) {
            return true;
        }
        return false;
    }
}
