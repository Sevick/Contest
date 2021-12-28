package com.fbytes.contest.Contest.TestResultWriter;

import com.fbytes.contest.Contest.Model.TestResults.TestResult;

public abstract class TestResultWriter implements ITestResultWriter{
    public void init() throws Exception {}
    abstract public void write(TestResult testResult) throws Exception;
}
