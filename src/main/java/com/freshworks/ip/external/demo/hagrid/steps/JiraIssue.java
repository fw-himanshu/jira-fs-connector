package com.freshworks.ip.external.demo.hagrid.steps;

import com.fasterxml.jackson.databind.JsonNode;
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

@FreshHierarchy(parentClass = ParentStep.class, rateLimit = 5, duration = 5)
public class JiraIssue extends AbstractStep {

  ImmutableMap<String, String> parameters;

  public JiraIssue(InfraDbList list,
      InfraDbKeyValue abstractKeyValue) {
    super(list, abstractKeyValue);
  }

  @Override
  public void setup(ImmutableMap<String, String> baggageMap) throws StepFailedException {
    this.parameters = baggageMap;
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
      uriBuilder = new URIBuilder("https://" + parameters.get("jiraAcount") +".atlassian.net/rest/api/2/search");
      uriBuilder.addParameter("jql", "project = DEMO");
      uriBuilder.addParameter("maxResults", "100");
      HttpRequest httpRequest = new HttpRequest(String.valueOf(uriBuilder.build()));
      String jiraEmail = parameters.get("jiraEmail");
      String jiraApiToken = parameters.get("jiraAPIToken");
      String auth = jiraEmail + ":" + jiraApiToken;
      String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
      httpRequest.setHeader("Authorization", "Basic " + encodedAuth);
      httpRequest.setHeader("Content-Type", "application/json");
      httpRequestResponse.setRequest(httpRequest);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(httpRequestResponse);
  }

  @Override
  public void filterResponse(JsonNode jsonNode) throws StepFailedException {
    System.out.println(jsonNode.toString());
  }

  @Override
  public Optional getNextSyncRequest(HttpRequestResponse currentRequest,
      JsonNode... parentJsonObject) throws StepFailedException {
    return null;
  }

  @Override
  public TraverseAction handleNon200ResponseCode(HttpRequestResponse currentRequest)
      throws URISyntaxException, StepFailedException {
      TraverseAction traverseAction = new TraverseAction();
      traverseAction.abortTransaction();
      return traverseAction;
  }

  @Override
  public Optional<Boolean> isSyncComplete(HttpRequestResponse currentRequest,
      JsonNode... parentJsonObject) throws StepFailedException {
    return Optional.of(true);
  }

  @Override
  public Optional<JsonNode> parseSyncResponse(JsonNode jsonNode) {
    return Optional.of(jsonNode.get("issues"));
  }

  @Override
  public void closeSync() {

  }

}
