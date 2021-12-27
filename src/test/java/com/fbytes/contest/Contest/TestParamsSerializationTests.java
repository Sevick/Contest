package com.fbytes.contest.Contest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbytes.contest.Contest.Model.TestParams.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles(profiles = "test")
public class TestParamsSerializationTests {

    private final static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    TestParamsFactory testParamsFactory;

    @Test
    public void testSerializationDns() throws JsonProcessingException {
        TestParamsDns testDns = new TestParamsDns("id", "address");
        String jsonResult = mapper.writeValueAsString(testDns);
        Assertions.assertEquals("{\"type\":\"dns\",\"id\":\"id\",\"address\":\"address\"}", jsonResult);
    }

    @Test
    public void testSerializationHttp() throws JsonProcessingException {
        TestParamsHttp testHttp = new TestParamsHttp("id", "address", "HEAD", 200, true, true);
        String jsonResult = mapper.writeValueAsString(testHttp);
        Assertions.assertEquals("{\"type\":\"http\",\"id\":\"id\",\"address\":\"address\",\"httpMethod\":\"HEAD\",\"expectedResultCode\":200,\"measureLatency\":true,\"measureBandwidth\":true}", jsonResult);
    }

    @Test
    public void testSerializationHttps() throws JsonProcessingException {
        TestParamsHttp testHttp = new TestParamsHttps("id", "address", "HEAD", 200, true, true, false);
        String jsonResult = mapper.writeValueAsString(testHttp);
        Assertions.assertEquals("{\"type\":\"http\",\"id\":\"id\",\"address\":\"address\",\"httpMethod\":\"HEAD\",\"expectedResultCode\":200,\"measureLatency\":true,\"measureBandwidth\":true,\"bypassCertCheck\":false}", jsonResult);
    }

    @Test
    public void testDeserializationHttps() throws JsonProcessingException {
        TestParams testParams = testParamsFactory.getTestParams("{\"type\":\"https\",\"id\":\"id\",\"address\":\"address\",\"httpMethod\":\"HEAD\",\"expectedResultCode\":200,\"measureLatency\":true,\"measureBandwidth\":false,\"bypassCertCheck\":false}");
        Assertions.assertEquals("com.fbytes.contest.Contest.Model.TestParams.TestParamsHttps", testParams.getClass().getName());
        Assertions.assertEquals("https", testParams.getType());
        Assertions.assertEquals("id", testParams.getId());
        Assertions.assertEquals("address", testParams.getAddress());
        Assertions.assertEquals("HEAD", ((TestParamsHttps) testParams).getHttpMethod());
        Assertions.assertEquals(200, ((TestParamsHttps) testParams).getExpectedResultCode());
        assertTrue(((TestParamsHttps) testParams).getMeasureLatency());
        assertFalse(((TestParamsHttps) testParams).getMeasureBandwidth());
    }
}
