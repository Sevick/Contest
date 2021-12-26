package com.fbytes.contest.Contest.TestEngine;

import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;

import java.net.MalformedURLException;

public interface ITestEngine<T extends TestParams> {

    TestResult testConnection(T testParams) throws Exception;
}
