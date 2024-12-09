package com.freshworks.ip.external.demo.hagrid.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Fields {

  @JsonProperty
  private String summary;
  @JsonProperty
  private String description;
  @JsonProperty
  private Priority priority;
  @JsonProperty
  private IssueType issuetype;
  @JsonProperty
  private Creator creator;

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Priority {

    @JsonProperty
    private String name;

    @Override
    public String toString() {
      return "Priority{" +
          "name='" + name + '\'' +
          '}';
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class IssueType {

    @JsonProperty
    private String name;

    @Override
    public String toString() {
      return "IssueType{" +
          "name='" + name + '\'' +
          '}';
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Creator {

    @JsonProperty
    private String emailAddress;

    @Override
    public String toString() {
      return "Creator{" +
          "emailAddress='" + emailAddress + '\'' +
          '}';
    }
  }
}