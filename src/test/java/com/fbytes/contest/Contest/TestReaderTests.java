package com.fbytes.contest.Contest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsDns;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsHttp;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;
import com.fbytes.contest.Contest.TestReader.ITestReader;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class TestReaderTests implements ITestExecutor {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ITestReader testReader;

    @Test
    public void testSerialization() throws JsonProcessingException {
        TestParamsHttp test1 = new TestParamsHttp("id", "address", "HEAD", 200, true, true);
        String json1 = mapper.writeValueAsString(test1);
        System.out.println(json1);
        TestParamsDns test2 = new TestParamsDns("id", "address");
        String json2 = mapper.writeValueAsString(test2);
        System.out.println(json2);
    }


    @Test
    public void testDeserialization() throws IOException {
        String fileName = "input.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        testReader.retrieveTests(inputStream, this);
    }

    @Override
    public Pair<TestParams, TestResult> execTest(TestParams testParameters) {
        return null;
    }
}
