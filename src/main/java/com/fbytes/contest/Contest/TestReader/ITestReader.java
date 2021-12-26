package com.fbytes.contest.Contest.TestReader;

import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;

import java.io.InputStream;

public interface ITestReader {
    void retrieveTests(InputStream inputStream, ITestExecutor testExecutor) throws Exception;
}
