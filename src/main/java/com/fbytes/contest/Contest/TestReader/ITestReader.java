package com.fbytes.contest.Contest.TestReader;

import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ITestReader {
    void retrieveTests(InputStream inputStream, ITestExecutor testExecutor) throws IOException;
}
