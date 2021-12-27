package com.fbytes.contest.Contest.TestResultsLoader;

import com.fbytes.contest.Contest.Model.TestResults.TestResult;

import java.io.IOException;
import java.util.List;

public interface ITestResultsLoader {
    List<TestResult> loadTestResults() throws IOException;

    List<TestResult> loadTestResults(String fileName) throws IOException;
}
