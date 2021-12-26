package com.fbytes.contest.Contest.TestProcessor;

import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.apache.commons.lang3.tuple.Pair;

public interface ITestExecutor {
    Pair<TestParams, TestResult> execTest(TestParams testParameters);
}
