package com.salemanager.application.external.client;

import com.salemanager.application.external.client.models.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-clients")
public interface ICustomerFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/clients/{clientId}", consumes = "application/json")
    ClientDto getClientById(@PathVariable Long clientId);
}
