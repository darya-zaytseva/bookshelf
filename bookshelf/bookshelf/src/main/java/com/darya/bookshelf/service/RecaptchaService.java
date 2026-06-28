package com.darya.bookshelf.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;
    @Value("${recaptcha.verify-url}")
    private String recaptchaVerifyUrl;
    private final WebClient webClient;
    public RecaptchaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public boolean verifyCaptcha(String captchaResponse) {
        if (captchaResponse == null || captchaResponse.isBlank()) {
            return false;
        }
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", recaptchaSecret);
        formData.add("response", captchaResponse);
        try {
            Map<String, Object> response = webClient.post()
                    .uri(recaptchaVerifyUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response == null) {
                return false;
            }
            Boolean success = (Boolean) response.get("success");
            return success != null && success;
        } catch (WebClientResponseException e) {
            System.err.println("reCAPTCHA verification failed: " + e.getMessage());
            return false;
        }
    }
}