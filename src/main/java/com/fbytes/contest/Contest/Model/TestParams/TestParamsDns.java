package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonTypeName("dns")
public class TestParamsDns extends TestParams {
    public TestParamsDns(String id, String address) {
        super("dns", id, address);
    }
}
