package com.freshworks.ip.external.demo.hagrid.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.core.shared.infra.InfraDbKeyValue;
import com.freshworks.core.shared.infra.InfraDbList;
import com.freshworks.core.traverser.AbstractStep;
import com.freshworks.core.traverser.Annotations.FreshHierarchy;
import com.freshworks.core.traverser.ParentStep;
import com.freshworks.core.traverser.TraverserService.TraverseAction;
import com.freshworks.core.traverser.exception.StepFailedException;
import com.freshworks.core.traverser.net.http.HttpRequest;
import com.freshworks.core.traverser.net.http.HttpRequestResponse;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;
import org.apache.hc.core5.net.URIBuilder;
import org.codehaus.plexus.util.StringUtils;

@FreshHierarchy(parentClass = JiraIssue.class, rateLimit = 20, duration = 20)
public class Comment extends AbstractStep {

  ImmutableMap<String, String> parameters;

  private String commentId;

  public Comment(InfraDbList list,
      InfraDbKeyValue abstractKeyValue) {
    super(list, abstractKeyValue);
  }

  @Override
  public void setup(ImmutableMap<String, String> baggageMap) throws StepFailedException {
    this.parameters = baggageMap;
    String event = parameters.get("event");
    if (StringUtils.isNotBlank(event)) {
      try {
        Map eventMap = new ObjectMapper().readValue(event, Map.class);
        if (eventMap != null && eventMap.containsKey("comment")) {
          commentId = (String) eventMap.get("comment");
        }
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Optional<Boolean> shouldProceedWithParentObject(JsonNode... parentJsonObject)
      throws StepFailedException {
    return Optional.of(true);
  }

  @Override
  public Optional startSync(JsonNode... parentJsonObject) throws StepFailedException {
    HttpRequestResponse httpRequestResponse = new HttpRequestResponse();
    URIBuilder uriBuilder = null;
    try {
      com.freshworks.ip.external.demo.hagrid.beans.JiraIssue issue = new ObjectMapper().treeToValue(parentJsonObject[0], com.freshworks.ip.external.demo.hagrid.beans.JiraIssue.class);
      if (commentId != null) {
        uriBuilder = new URIBuilder("https://" + parameters.get("jiraAcount") +".atlassian.net/rest/api/2/issue/" + issue.getKey()  + "/comment/" + commentId);
      } else {
        uriBuilder = new URIBuilder("https://" + parameters.get("jiraAcount") +".atlassian.net/rest/api/2/issue/" + issue.getKey()  + "/comment");
      }
      HttpRequest httpRequest = new HttpRequest(String.valueOf(uriBuilder.build()));
      String jiraEmail = parameters.get("jiraEmail");
      String jiraApiToken = parameters.get("jiraAPIToken");
      String auth = jiraEmail + ":" + jiraApiToken;
      String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
      httpRequest.setHeader("Authorization", "Basic " + encodedAuth);
      httpRequest.setHeader("Content-Type", "application/json");
      httpRequestResponse.setRequest(httpRequest);
    } catch (URISyntaxException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(httpRequestResponse);
  }

  @Override
  public void filterResponse(JsonNode jsonNode) throws StepFailedException {

  }

  @Override
  public Optional getNextSyncRequest(HttpRequestResponse currentRequest,
      JsonNode... parentJsonObject) throws StepFailedException {
    return Optional.absent();
  }

  @Override
  public TraverseAction handleNon200ResponseCode(HttpRequestResponse currentRequest)
      throws URISyntaxException, StepFailedException {
    return null;
  }

  @Override
  public Optional<Boolean> isSyncComplete(HttpRequestResponse currentRequest,
      JsonNode... parentJsonObject) throws StepFailedException {
    return Optional.of(true);
  }

  @Override
  public Optional<JsonNode> parseSyncResponse(JsonNode jsonNode) {
    return commentId != null ? Optional.of(jsonNode) : Optional.of(jsonNode.get("comments"));
  }

  @Override
  public void closeSync() {

  }
}
