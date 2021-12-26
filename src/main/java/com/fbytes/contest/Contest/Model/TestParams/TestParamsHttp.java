package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;


@Data
@JsonTypeName("http")
public class TestParamsHttp extends TestParams {
    @JsonProperty(required = true)
    private String httpMethod;      // an example of mandatory field

    private int expectedResultCode;
    private boolean measureLatency;
    private boolean measureBandwidth;

    public TestParamsHttp() {
        this.httpMethod = "HEAD";
        this.measureLatency = false;
        this.measureBandwidth = false;
        this.expectedResultCode = 200;
    }

    public TestParamsHttp(String id, String address, String httpMethod, int expectedResultCode, boolean measureLatency, boolean measureBandwidth) {
        super("http", id, address);
        this.httpMethod = httpMethod;
        this.measureLatency = measureLatency;
        this.measureBandwidth = measureBandwidth;
        this.expectedResultCode = expectedResultCode;
    }

    public TestParamsHttp(String name, String id, String address) {
        super(name, id, address);
    }
}
