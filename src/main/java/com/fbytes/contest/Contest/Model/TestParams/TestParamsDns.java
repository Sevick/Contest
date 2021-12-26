package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("dns")
public class TestParamsDns extends TestParams {



    public TestParamsDns() {
    }

    public TestParamsDns(String id, String address) {
        super("dns", id, address);
    }

}
