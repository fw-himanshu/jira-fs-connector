package com.freshworks.ip.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueResponse {

  @JsonProperty("issues")
  private List<JiraIssue> issues;

  @Override
  public String toString() {
    return "JiraIssueResponse{" +
        "issues=" + issues +
        '}';
  }

  public List<JiraIssue> getIssues() {
    return issues;
  }

  public void setIssues(List<JiraIssue> issues) {
    this.issues = issues;
  }
}
