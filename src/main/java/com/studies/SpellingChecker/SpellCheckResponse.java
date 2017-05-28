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
"_type",
"flaggedTokens"
})
public class SpellCheckResponse {

@JsonProperty("_type")
private String type;
@JsonProperty("flaggedTokens")
private List<FlaggedToken> flaggedTokens = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("_type")
public String getType() {
return type;
}

@JsonProperty("_type")
public void setType(String type) {
this.type = type;
}

@JsonProperty("flaggedTokens")
public List<FlaggedToken> getFlaggedTokens() {
return flaggedTokens;
}

@JsonProperty("flaggedTokens")
public void setFlaggedTokens(List<FlaggedToken> flaggedTokens) {
this.flaggedTokens = flaggedTokens;
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