package com.salemanager.application.external.client;

import com.salemanager.application.external.client.models.ClientDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8082/api";

    public ClientApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ClientDto getClientById(Long clientId) {
        String uri = baseUrl + "/clients/" + clientId;
        return restTemplate.getForObject(uri, ClientDto.class);
    }
}
