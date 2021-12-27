package com.fbytes.contest.Contest.Model.TestParams;


import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonTypeName("https")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TestParamsHttps extends TestParamsHttp {
    protected Boolean bypassCertCheck;

    public TestParamsHttps(String id, String address, String httpMethod, int expectedResultCode, Boolean measureLatency, Boolean measureBandwidth) {
        super(id, address, httpMethod, expectedResultCode, measureLatency, measureBandwidth);
    }
}
