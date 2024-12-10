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
import com.freshworks.platform.utils.auth.AuthUtil;
import com.freshworks.platform.utils.auth.AuthUtilFactory;
import com.freshworks.platform.utils.auth.AuthUtilFactory.AuthType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import org.apache.hc.core5.net.URIBuilder;
import org.codehaus.plexus.util.StringUtils;

@FreshHierarchy(parentClass = ParentStep.class, rateLimit = 20, duration = 20)
public class JiraIssue extends AbstractStep {

  ImmutableMap<String, String> parameters;

  AuthUtil authUtil;

  private String issueKey;

  public JiraIssue(InfraDbList list,
      InfraDbKeyValue abstractKeyValue) {
    super(list, abstractKeyValue);
  }

  @Override
  public void setup(ImmutableMap<String, String> baggageMap) throws StepFailedException {
    this.parameters = baggageMap;
    this.authUtil = AuthUtilFactory.createAuthUtil(AuthType.BASIC, Map.of("username",
        Objects.requireNonNull(parameters.get("jiraEmail")), "password",
        Objects.requireNonNull(parameters.get("jiraAPIToken"))));
    String event = parameters.get("event");
    if (StringUtils.isNotBlank(event)) {
      try {
        Map eventMap = new ObjectMapper().readValue(event, Map.class);
        if (eventMap != null && eventMap.containsKey("issue")) {
          issueKey = (String) eventMap.get("issue");
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
      uriBuilder = new URIBuilder("https://" + parameters.get("jiraAcount") +".atlassian.net/rest/api/2/search");
      if (issueKey != null) {
        uriBuilder.addParameter("jql", "project = DEMO AND key = " + issueKey);
      } else {
        uriBuilder.addParameter("jql", "project = DEMO");
      }
      uriBuilder.addParameter("maxResults", "100");
      HttpRequest httpRequest = new HttpRequest(String.valueOf(uriBuilder.build()));
      authUtil.getHeaderMap().forEach(httpRequest::setHeader);
      httpRequestResponse.setRequest(httpRequest);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(httpRequestResponse);
  }

  @Override
  public void filterResponse(JsonNode jsonNode) throws StepFailedException {
    //System.out.println(jsonNode.toString());
  }

  @Override
  public Optional getNextSyncRequest(HttpRequestResponse currentRequest,
      JsonNode... parentJsonObject) throws StepFailedException {
    return Optional.absent();
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
