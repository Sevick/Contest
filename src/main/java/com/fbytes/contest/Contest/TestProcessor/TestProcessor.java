package com.fbytes.contest.Contest.TestProcessor;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.TestEngine.ITestEngine;
import com.fbytes.contest.Contest.TestReader.ITestReader;
import com.fbytes.contest.Contest.TestResultProcessor.ITestResultProcessor;
import com.fbytes.contest.Contest.TestResultWriter.ITestResultWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class TestProcessor implements ITestExecutor {
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
    private Boolean ignoreInvalidConfig;
    @Value("${contest.testprocessor.threads:10}")
    private Integer threadsCount;
    @Value("${contest.testprocessor.shutdowntimeout:60}")
    private Integer shutdowntimeout;        // sec

    private ExecutorService executor;

    @PostConstruct
    private void init() {
        executor = Executors.newFixedThreadPool(threadsCount);
    }


    public void runTests(InputStream inputStream) throws Exception {
        testReader.retrieveTests(inputStream, this);
        awaitTerminationAfterShutdown(executor);
    }

    private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(shutdowntimeout, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    public void execTest(TestParams testParameters) {
        Runnable testRun = new TestRunner(testParameters, logger, testersMap, ignoreInvalidConfig, testResultProcessorMap, writersMap);
        executor.submit(testRun);
    }
}