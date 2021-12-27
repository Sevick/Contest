package com.fbytes.contest.Contest;

import com.fbytes.contest.Contest.Model.TestParams.TestParamsHttp;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.TestEngine.TestEngineHttp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles(profiles = "test")
public class TestEngineHttpTests {

    @Autowired
    private TestEngineHttp testEngineHttp;

    @Test
    public void testHttpOk() throws IOException {
        HttpURLConnection huc = Mockito.mock(HttpURLConnection.class);
        Mockito.when(huc.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        TestParamsHttp testParamsHttp = new TestParamsHttp("777", "http://yahoo.com", "HEAD", 200, false, false);
        TestResult testResultPassed = testEngineHttp.testConnection(testParamsHttp, huc);
        assertTrue(testResultPassed.getTestPassed());
        TestResult testResultFailed = testEngineHttp.testConnection(new TestParamsHttp("777", "http://yahoo.com", "HEAD", 403, false, false), huc);
        assertFalse(testResultFailed.getTestPassed());
    }


    @Test
    public void testHttpFail() throws IOException {
        HttpURLConnection huc = Mockito.mock(HttpURLConnection.class);
        Mockito.when(huc.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);
        TestParamsHttp testParamsHttp = new TestParamsHttp("777", "http://yahoo.com", "HEAD", 403, false, false);
        TestResult testResultPassed = testEngineHttp.testConnection(testParamsHttp, huc);
        assertTrue(testResultPassed.getTestPassed());
    }
}
