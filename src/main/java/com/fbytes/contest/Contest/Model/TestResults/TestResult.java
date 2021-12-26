package com.fbytes.contest.Contest.Model.TestResults;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TestResult {

    @JsonProperty(required = true)
    private boolean testPassed;

    private final static ObjectMapper mapper = new ObjectMapper();

    public TestResult(boolean testPassed) {
        this.testPassed = testPassed;
    }

    public TestResult() {
        testPassed = false;
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }

    Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        additionalProperties.put(key, value);
    }
}
