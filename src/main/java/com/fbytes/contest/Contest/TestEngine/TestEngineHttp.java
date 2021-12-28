package com.fbytes.contest.Contest.TestEngine;


import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsHttp;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class TestEngineHttp extends TestEngine<TestParamsHttp> {
    @Autowired
    private ILogger logger;

    @Value("${contest.testenginehttp.readtimeout:1000}")
    private Integer readTimeout;    // millisec
    @Value("${contest.testenginehttp.connecttimeout:1000}")
    private Integer connectTimeout; // millisec
    @Value("${contest.testenginehttp.bandwidthSampleSize:10240}")
    private Integer bandwidthSampleSize;

    @Override
    public TestResult testConnectionImpl(TestParamsHttp testParams) throws Exception {
        URL url;
        try {
            url = new URL(testParams.getAddress());
        } catch (MalformedURLException e) {
            logger.logException("Test#" + testParams.getId(), e);
            throw e;
        }

        try {
            HttpURLConnection connection = getConnection(url);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setRequestMethod(testParams.getHttpMethod());
            return testConnection(testParams, connection);
        } catch (IOException e) {
            return new TestResult(testParams.genIdentifier(), false);
        }
    }

    protected HttpURLConnection getConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }


    public TestResult testConnection(TestParamsHttp testParams, HttpURLConnection connection) throws IOException {
        logger.log(ILogger.Severity.debug, String.format("Test#%s => HTTP %s", testParams.getId(), testParams.getAddress()));
        TestResult result = new TestResult(testParams.genIdentifier());
        result.getAdditionalProperties().put("responseCode", connection.getResponseCode());
        result.getAdditionalProperties().put("responseMessage", connection.getResponseMessage());
        if (connection.getResponseCode() == testParams.getExpectedResultCode())
            result.setTestPassed(true);

        if (testParams.getMeasureBandwidth()) {
            try (InputStream inputStream = connection.getInputStream()) {
                long start = System.nanoTime();
                byte[] buffer = new byte[bandwidthSampleSize];
                int bytesRead = inputStream.read(buffer, 0, bandwidthSampleSize);
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                if (bytesRead != -1) {
                    float bandwidth = ((float) bytesRead / 1024) / ((float) timeElapsed / 1000000000);
                    result.getAdditionalProperties().put("bandwidth", bandwidth);
                }
            }
        }

        connection.disconnect();
        return result;
    }
}
