package com.studies.SpellingChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"offset",
"token",
"type",
"suggestions"
})
public class FlaggedToken {

@JsonProperty("offset")
private Integer offset;
@JsonProperty("token")
private String token;
@JsonProperty("type")
private String type;
@JsonProperty("suggestions")
private List<Suggestion> suggestions = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("offset")
public Integer getOffset() {
return offset;
}

@JsonProperty("offset")
public void setOffset(Integer offset) {
this.offset = offset;
}

@JsonProperty("token")
public String getToken() {
return token;
}

@JsonProperty("token")
public void setToken(String token) {
this.token = token;
}

@JsonProperty("type")
public String getType() {
return type;
}

@JsonProperty("type")
public void setType(String type) {
this.type = type;
}

@JsonProperty("suggestions")
public List<Suggestion> getSuggestions() {
return suggestions;
}

@JsonProperty("suggestions")
public void setSuggestions(List<Suggestion> suggestions) {
this.suggestions = suggestions;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}