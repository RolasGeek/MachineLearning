package com.studies.SpellingChecker;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"suggestion",
"score"
})
public class Suggestion {

@JsonProperty("suggestion")
private String suggestion;
@JsonProperty("score")
private Integer score;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("suggestion")
public String getSuggestion() {
return suggestion;
}

@JsonProperty("suggestion")
public void setSuggestion(String suggestion) {
this.suggestion = suggestion;
}

@JsonProperty("score")
public Integer getScore() {
return score;
}

@JsonProperty("score")
public void setScore(Integer score) {
this.score = score;
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