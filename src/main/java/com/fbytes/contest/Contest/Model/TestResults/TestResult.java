package com.fbytes.contest.Contest.Model.TestResults;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class TestResult {

    private String testId;
    @JsonProperty(required = true)
    private String testIdentifier;
    @JsonProperty(required = true)
    private Boolean testPassed;

    public TestResult(String testIdentifier, Boolean testPassed) {
        this.testIdentifier = testIdentifier;
        this.testPassed = testPassed;
    }

    private final static ObjectMapper mapper = new ObjectMapper();

    public TestResult(String testIdentifier) {
        testPassed = false;
        this.testIdentifier = testIdentifier;
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }

    @JsonIgnore
    Map<String, Object> additionalProperties = new ConcurrentHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        additionalProperties.put(key, value);
    }
}
