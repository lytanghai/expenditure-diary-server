package com.expenditure_diary.expenditure_diary.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Reusable method to call external APIs
     *
     * @param url        Target API endpoint
     * @param method     GET, POST, PUT, DELETE
     * @param headersMap Map of headers
     * @param body       Request body (can be null for GET/DELETE)
     * @param responseType Response class type
     * @return ResponseEntity<T>
     */
    public <T> ResponseEntity<T> callExternalApi(
            String url,
            HttpMethod method,
            Map<String, String> headersMap,
            Object body,
            Class<T> responseType
    ) {
        // Build headers
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null) {
            headersMap.forEach(headers::set);
        }

        // Build entity
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        // Call API
        return restTemplate.exchange(url, method, entity, responseType);
    }
}
