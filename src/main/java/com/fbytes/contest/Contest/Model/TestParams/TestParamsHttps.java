package com.fbytes.contest.Contest.Model.TestParams;


import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("https")
@Data
@NoArgsConstructor
public class TestParamsHttps extends TestParamsHttp {

    boolean bypassCertCheck;

    public TestParamsHttps(String id, String address) {
        super("https", id, address);
    }

}
