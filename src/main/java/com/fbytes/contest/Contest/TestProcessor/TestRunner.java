package com.fbytes.contest.Contest.TestProcessor;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.TestResultWriter.ITestResultWriter;
import com.fbytes.contest.Contest.TestEngine.ITestEngine;
import com.fbytes.contest.Contest.TestResultProcessor.ITestResultProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public class TestRunner implements Runnable{

    private TestParams testParams;
    private ILogger logger;
    private Map<String, ITestEngine> testersMap;
    private Boolean ignoreInvalidConfig;
    private Map<String, ITestResultProcessor> testResultProcessorMap;
    private Map<String, ITestResultWriter> writersMap;

    public TestRunner(TestParams testParams, ILogger logger, Map<String, ITestEngine> testersMap, Boolean ignoreInvalidConfig, Map<String, ITestResultProcessor> testResultProcessorMap, Map<String, ITestResultWriter> writersMap) {
        this.testParams = testParams;
        this.logger = logger;
        this.testersMap = testersMap;
        this.ignoreInvalidConfig = ignoreInvalidConfig;
        this.testResultProcessorMap = testResultProcessorMap;
        this.writersMap = writersMap;
    }

    @Override
    public void run() {
        try {
            execTest(testParams);
        } catch (Exception e) {
            logger.logException(e);
        }
    }

    private void execTest(TestParams testParameters) throws Exception{
        ITestEngine testEngine = testersMap.get("testEngine" + StringUtils.capitalize(testParameters.getType()));
        if (testEngine == null) {
            logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType() + " check that engine is annotated with @Service");
            throw new Exception("TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType());
        }
        TestResult testResult = null;
        try {
            testResult = testEngine.testConnection(testParameters);
            testResult.setTestId(testParameters.getId());
        } catch (Exception e) {
            logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType() + "  : " + e.getMessage());
            if (!ignoreInvalidConfig)
                throw new RuntimeException("TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType());
        }

        // apply result processors
        Pair<TestParams, TestResult> resultPairParamResult = Pair.of(testParameters, testResult);
        if (testResultProcessorMap != null && !testResultProcessorMap.isEmpty()) {
            testResultProcessorMap.entrySet().stream()
                    .forEach(testResultProcessor ->
                            testResultProcessor.getValue().process(resultPairParamResult.getLeft(), resultPairParamResult.getRight()));
        }

        // execute writers
        if (writersMap != null && !writersMap.isEmpty()) {
            writersMap.entrySet().stream()
                    .forEach(writersMapsEntry -> {
                        try {
                            writersMapsEntry.getValue().write(resultPairParamResult.getRight());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
