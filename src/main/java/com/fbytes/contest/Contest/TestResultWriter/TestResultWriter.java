package com.fbytes.contest.Contest.TestResultWriter;

import com.fbytes.contest.Contest.Model.TestResults.TestResult;

public abstract class TestResultWriter implements ITestResultWriter{

    abstract public void write(TestResult testResult) throws Exception;
}
