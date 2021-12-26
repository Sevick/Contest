package com.fbytes.contest.Contest.ResultWriter;

import com.fbytes.contest.Contest.Model.TestResults.TestResult;

public interface ITestResultWriter {

    void init() throws Exception;
    void write(TestResult testResult) throws Exception;
}
