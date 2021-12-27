package com.fbytes.contest.Contest.TestEngine;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsDns;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class TestEngineDns extends TestEngine<TestParamsDns> {
    @Autowired
    private ILogger logger;

    @Override
    public TestResult testConnectionImpl(TestParamsDns testParams) throws Exception {
        URL url;
        try {
            url = new URL(testParams.getAddress());
        }
        catch (MalformedURLException e){
            logger.logException("Test#"+testParams.getId(),e);
            throw e;
        }
        try {
            InetAddress addr1 = InetAddress.getByName(url.getHost());
            addr1.getHostAddress();
        } catch (Exception e) {
            logger.log(ILogger.Severity.info, String.format("Test#%s FAILED =>DNS %s", testParams.getId(), testParams.getAddress()));
            return new TestResult(testParams.genIdentifier(), false);
        }
        logger.log(ILogger.Severity.info, "Test#" + testParams.getId() + " =>DNS " + testParams.getAddress());
        return new TestResult(testParams.genIdentifier(), true);
    }
}
