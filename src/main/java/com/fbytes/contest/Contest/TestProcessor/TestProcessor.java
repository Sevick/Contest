package com.fbytes.contest.Contest.TestProcessor;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.ResultWriter.ITestResultWriter;
import com.fbytes.contest.Contest.TestEngine.TestResultProcessor.ITestResultProcessor;
import com.fbytes.contest.Contest.TestEngine.ITestEngine;
import com.fbytes.contest.Contest.TestReader.ITestReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;


@Service
public class TestProcessor implements ITestExecutor{
    @Autowired
    private ILogger logger;
    @Autowired
    private ITestReader testReader;
    @Autowired
    private Map<String, ITestResultWriter> writersMap;
    @Autowired
    private Map<String, ITestEngine> testersMap;
    @Autowired
    private Map<String, ITestResultProcessor> testResultProcessorMap;

    @Value("${contest.ignoreinvalidconfig:true}")
    private boolean ignoreInvalidConfig;

    // !!! read previous test data
    //Map<String, TestResult> lastResults;

    public void runTests(InputStream inputStream) throws Exception {
        // initialize writer to ensure that they configured properly
        if (writersMap != null && !writersMap.isEmpty()) {
            writersMap.entrySet()
                    .forEach(writersMapsEntry -> {
                        try {
                            writersMapsEntry.getValue().init();
                        } catch (Exception e) {
                            logger.logException("Unable to initialize TestResultWrite " + writersMapsEntry.getKey() + " " + e.getMessage(), e);
                            throw new RuntimeException("Unable to initialize TestResultWrite " + writersMapsEntry.getKey());
                        }
                    });
        }

        testReader.retrieveTests(inputStream,this);

/*
        testReader.retrieveTests(inputStream)
                .stream()
                .map(testParameters -> {
                    ITestEngine testEngine = testersMap.get("testEngine" + StringUtils.capitalize(testParameters.getType()));
                    if (testEngine == null) {
                        logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType() + " check that engine is annotated with @Service");
                        if (!ignoreInvalidConfig)
                            throw new RuntimeException("TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType());
                    }
                    try {
                        TestResult testResult = testEngine.testConnection(testParameters);
                        return Pair.of(testParameters, testResult);
                    } catch (Exception e) {
                        logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType() + "  : " + e.getMessage());
                        if (!ignoreInvalidConfig)
                            throw new RuntimeException("TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType());
                    }

                    return null;
                })
                .peek(testParamResult -> {
                    testResultProcessorMap.entrySet().stream()
                            .forEach(testResultProcessor -> {
                                testResultProcessor.getValue().process(testParamResult.getLeft(), testParamResult.getRight());
                            });
                })
                .peek(testParamResult -> {
                    if (writersMap != null && !writersMap.isEmpty()) {
                        writersMap.entrySet().stream()
                                .forEach(writersMapsEntry -> {
                                    try {
                                        writersMapsEntry.getValue().write(testParamResult.getRight());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                })
                .forEach(result -> {
                });
 */
    }

    public Pair<TestParams, TestResult> execTest(TestParams testParameters) {
        ITestEngine testEngine = testersMap.get("testEngine" + StringUtils.capitalize(testParameters.getType()));
        if (testEngine == null) {
            logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType() + " check that engine is annotated with @Service");
            if (!ignoreInvalidConfig)
                throw new RuntimeException("TEST#" + testParameters.getId() + "  No such TestEngine: " + testParameters.getType());
        }
        TestResult testResult=null;
        try {
            testResult = testEngine.testConnection(testParameters);
        } catch (Exception e) {
            logger.log(ILogger.Severity.err, "TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType() + "  : " + e.getMessage());
            if (!ignoreInvalidConfig)
                throw new RuntimeException("TEST#" + testParameters.getId() + "  incorrect configuration of " + testParameters.getType());
        }

        // apply result processors
        Pair<TestParams, TestResult> resultPairParamResult = Pair.of(testParameters, testResult);
        if (testResultProcessorMap!=null && !testResultProcessorMap.isEmpty()) {
            testResultProcessorMap.entrySet().stream()
                    .forEach(testResultProcessor -> {
                        testResultProcessor.getValue().process(resultPairParamResult.getLeft(), resultPairParamResult.getRight());
                    });
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
        return resultPairParamResult;
    }
}
