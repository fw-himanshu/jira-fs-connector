package com.freshworks.ip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.ip.dto.FreshserviceTicket;
import java.io.IOException;
import java.util.Base64;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class FreshserviceService {

    private final String freshserviceTicketUrl;
    private final String freshserviceApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FreshserviceService(String fsAccount, String freshserviceApiKey) {
        this.freshserviceTicketUrl = "https://" + fsAccount +".freshservice.com/api/v2/tickets";
        this.freshserviceApiKey = freshserviceApiKey;
    }

    public void createTicket(FreshserviceTicket ticket) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(freshserviceTicketUrl);
        String auth = freshserviceApiKey + ":X";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        post.setHeader("Authorization", "Basic " + encodedAuth);
        post.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(ticket));
        post.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            if (response.getCode() != 201) {
                throw new IOException("Failed to create Freshservice ticket, response code: " + response.getCode());
            }
        }
    }
}
