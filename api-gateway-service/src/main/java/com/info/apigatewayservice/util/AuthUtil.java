package com.info.apigatewayservice.util;

import com.info.apigatewayservice.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthUtil {

    @Autowired
    private RestTemplate restTemplate;

    public String getToken(String userName, String role) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userName",userName);
        headers.set("role",role);
        HttpEntity<Credential> request = new HttpEntity<>(new Credential(userName, role), headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8087/login", HttpMethod.POST,request,String.class);
        System.out.println("token: "+response.getBody());
        return response.getBody();
    }
}
