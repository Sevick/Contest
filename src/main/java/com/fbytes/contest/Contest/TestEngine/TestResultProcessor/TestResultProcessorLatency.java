package com.fbytes.contest.Contest.TestEngine.TestResultProcessor;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestResultProcessorLatency implements ITestResultProcessor {
    @Autowired
    private ILogger logger;

    @Value("${contest.testresultprocessor.latency.threshold:1000}")
    private Integer threshold;

    Map<String, Integer> latencyResults = new ConcurrentHashMap<>();

    @Override
    public void process(TestParams testParams, TestResult testResult) {
        Integer timeElapsed = (Integer) testResult.getAdditionalProperties().get("latency");
        if (timeElapsed != null) {
            String testIdentifier = testParams.genIdentifier();
            Integer lastRunLatency = latencyResults.get(testIdentifier);
            if (lastRunLatency != null) {
                int delta = timeElapsed - lastRunLatency;
                if (delta > threshold)
                    logger.log(ILogger.Severity.warn, String.format("LATENCY GROWS ALERT: %s %s", testParams.getType(), testParams.getAddress()));
            }
            latencyResults.put(testIdentifier, timeElapsed);
        }
    }
}
