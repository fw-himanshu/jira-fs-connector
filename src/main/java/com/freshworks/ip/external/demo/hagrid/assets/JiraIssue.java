package com.freshworks.ip.external.demo.hagrid.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshworks.core.processor.AbstractAsset;
import com.freshworks.core.processor.Annotations.FreshAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue extends AbstractAsset {
  @JsonProperty
  private String key;
  @JsonProperty
  private Fields fields;

  public void setFromBean(com.freshworks.ip.external.demo.hagrid.beans.JiraIssue bean) {
    BeanUtils.copyProperties(bean, this);
  }

  @Override
  public String toString() {
    return "JiraIssue{" +
        "key='" + key + '\'' +
        ", fields=" + fields +
        '}';
  }

  @Override
  public void transform() {

  }

  @Override
  public Object getUniqueIdentifier() {
    return null;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Fields {
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
}

