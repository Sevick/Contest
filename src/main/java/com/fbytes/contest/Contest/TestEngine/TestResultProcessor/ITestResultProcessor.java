package com.fbytes.contest.Contest.TestEngine.TestResultProcessor;

import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;

public interface ITestResultProcessor {

    void process(TestParams testParams, TestResult testResult);
}