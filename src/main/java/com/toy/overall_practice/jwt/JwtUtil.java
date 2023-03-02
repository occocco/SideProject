package com.toy.overall_practice.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.HashMap;

@Slf4j
public class JwtUtil {
    public static HashMap<String, String> getPayloadInToken(String token) {
        try {
            String[] splitJwt = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(splitJwt[1].getBytes()));
            return new ObjectMapper().readValue(payload, HashMap.class);
        } catch (JsonProcessingException e) {
            log.error("errors ={}", e.getMessage(), e);
            return null;
        }
    }
}
