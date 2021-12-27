package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("http")
public class TestParamsHttp extends TestParams {

    @JsonProperty(required = true)
    private String httpMethod;      // an example of mandatory field
    private Integer expectedResultCode;
    private Boolean measureLatency;
    private Boolean measureBandwidth;

    public TestParamsHttp() {
        super();
        this.httpMethod = "HEAD";
        this.measureLatency = false;
        this.measureBandwidth = false;
        this.expectedResultCode = 200;
    }

    public TestParamsHttp(String id, String address, String httpMethod, int expectedResultCode, Boolean measureLatency, Boolean measureBandwidth) {
        super("http", id, address);
        this.httpMethod = httpMethod;
        this.measureLatency = measureLatency;
        this.measureBandwidth = measureBandwidth;
        this.expectedResultCode = expectedResultCode;
    }
}
