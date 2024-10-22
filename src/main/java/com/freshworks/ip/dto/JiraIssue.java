package com.freshworks.ip.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
  @JsonProperty
  private String key;
  @JsonProperty
  private Fields fields;

  @Override
  public String toString() {
    return "JiraIssue{" +
        "key='" + key + '\'' +
        ", fields=" + fields +
        '}';
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Fields getFields() {
    return fields;
  }

  public void setFields(Fields fields) {
    this.fields = fields;
  }

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

    @Override
    public String toString() {
      return "Fields{" +
          "summary='" + summary + '\'' +
          ", description='" + description + '\'' +
          ", priority=" + priority +
          ", issuetype=" + issuetype +
          '}';
    }

    public String getSummary() {
      return summary;
    }

    public void setSummary(String summary) {
      this.summary = summary;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Priority getPriority() {
      return priority;
    }

    public void setPriority(Priority priority) {
      this.priority = priority;
    }

    public IssueType getIssuetype() {
      return issuetype;
    }

    public void setIssuetype(IssueType issuetype) {
      this.issuetype = issuetype;
    }

    public Creator getCreator() {
      return creator;
    }

    public void setCreator(Creator creator) {
      this.creator = creator;
    }

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

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }
    }
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

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }
    }
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

      public String getEmailAddress() {
        return emailAddress;
      }

      public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
      }
    }
  }
}

