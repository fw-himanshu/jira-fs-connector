package com.freshworks.ip.external.demo.hagrid.assets;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshworks.core.processor.AbstractAsset;
import com.freshworks.ip.external.demo.hagrid.commons.Author;
import com.freshworks.ip.external.demo.hagrid.commons.Fields.Creator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Comment extends AbstractAsset {

  @JsonProperty
  private String body;
  @JsonProperty
  private Author author;
  @JsonProperty
  private String self;

  public void setFromBean(com.freshworks.ip.external.demo.hagrid.beans.Comment bean) {
    BeanUtils.copyProperties(bean, this);
  }

  @Override
  public void transform() {

  }

  @Override
  public Object getUniqueIdentifier() {
    return null;
  }
}
