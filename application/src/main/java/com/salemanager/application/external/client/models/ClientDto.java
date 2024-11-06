package com.salemanager.application.external.client.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Long clientId;
    private String name;
    private String lastName;
    private String email;
    private String phone;
}
