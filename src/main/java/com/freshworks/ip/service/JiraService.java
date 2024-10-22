package com.freshworks.ip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.ip.dto.JiraIssueResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

public class JiraService {

  private final String jiraAPIUrl;
  private final String jiraEmail;
  private final String jiraApiToken;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JiraService(String jiraAccount, String jiraEmail, String jiraApiToken) {
    this.jiraAPIUrl = "https://" + jiraAccount +".atlassian.net/rest/api/2/search";
    this.jiraEmail = jiraEmail;
    this.jiraApiToken = jiraApiToken;
  }

  // Fetch issues from JIRA
  public JiraIssueResponse fetchIssues() throws IOException, URISyntaxException, ParseException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    URIBuilder uriBuilder = new URIBuilder(jiraAPIUrl);
    uriBuilder.addParameter("jql", "project = DEMO");
    uriBuilder.addParameter("maxResults", "100");

    HttpGet request = new HttpGet(uriBuilder.build());
    String auth = jiraEmail + ":" + jiraApiToken;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
    request.setHeader("Authorization", "Basic " + encodedAuth);
    request.setHeader("Content-Type", "application/json");

    try (CloseableHttpResponse response = httpClient.execute(request)) {
      if (response.getCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        return objectMapper.readValue(json, JiraIssueResponse.class);
      } else {
        throw new IOException("Failed to fetch JIRA issues, response code: " + response.getCode());
      }
    }
  }
}
