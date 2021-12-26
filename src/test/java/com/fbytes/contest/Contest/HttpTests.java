package com.fbytes.contest.Contest;

import com.fbytes.contest.Contest.Model.TestParams.TestParamsHttp;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.TestEngine.TestEngineHttp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HttpTests {

    @Autowired
    private TestEngineHttp testEngineHttp;

    @Test
    public void testHttpOk() throws IOException {
        HttpURLConnection huc = Mockito.mock(HttpURLConnection.class);
        Mockito.when(huc.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        TestParamsHttp testParamsHttp = new TestParamsHttp("777","http://yahoo.com","HEAD",200,false,false);
        TestResult testResultPassed = testEngineHttp.testConnection(testParamsHttp,huc);
        assertEquals(true, testResultPassed.isTestPassed());
        TestResult testResultFailed = testEngineHttp.testConnection(new TestParamsHttp("777","http://yahoo.com","HEAD",403,false,false),huc);
        assertEquals(false, testResultFailed.isTestPassed());
    }


    @Test
    public void testHttpFail() throws IOException {
        HttpURLConnection huc = Mockito.mock(HttpURLConnection.class);
        Mockito.when(huc.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);
        TestParamsHttp testParamsHttp = new TestParamsHttp("777","http://yahoo.com","HEAD",403,false,false);
        TestResult testResultPassed = testEngineHttp.testConnection(testParamsHttp,huc);
        assertEquals(true, testResultPassed.isTestPassed());
    }
}
