package com.fbytes.contest.Contest.TestEngine;

import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;


public abstract class TestEngine<T extends TestParams> implements ITestEngine<T> {

    final public TestResult testConnection(T testParams) throws Exception {
        long start = System.currentTimeMillis();
        TestResult testResult = testConnectionImpl(testParams);
        long finish = System.currentTimeMillis();
        int timeElapsed = (int) (finish - start);
        testResult.getAdditionalProperties().put("latency", timeElapsed);
        return testResult;
    }

    abstract public TestResult testConnectionImpl(T testParams) throws Exception;
}