package com.fbytes.contest.Contest.TestResultProcessor;

import com.fbytes.contest.Contest.HashGen.HashGen;
import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import com.fbytes.contest.Contest.TestResultsLoader.ITestResultsLoader;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestResultProcessorLatency implements ITestResultProcessor {
    @Autowired
    private ILogger logger;
    @Autowired
    private HashGen hashGen;
    @Autowired
    private ITestResultsLoader testResultsLoader;

    @Value("${contest.testresultprocessor.latency.threshold:10}")
    private Integer threshold;

    @Setter
    private Map<String, Integer> latencyResults = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() throws IOException {
        if (testResultsLoader != null)
            testResultsLoader.loadTestResults()
                    .stream()
                    .parallel()
                    .filter(ent -> ent.getAdditionalProperties()!=null && ent.getAdditionalProperties().get("latency")!=null)
                    .forEach(ent -> latencyResults.putIfAbsent(hashGen.genHash(ent.getTestIdentifier()), (Integer) ent.getAdditionalProperties().get("latency")));
        logger.log(ILogger.Severity.debug, String.format("Loaded %d entries",latencyResults.size()));
    }

    @Override
    public void process(TestParams testParams, TestResult testResult) {
        Integer timeElapsed = (Integer) testResult.getAdditionalProperties().get("latency");
        if (testResult.getTestPassed() && timeElapsed != null) {
            String testIdentifier = testParams.genIdentifier();
            Integer lastRunLatency = latencyResults.get(hashGen.genHash(testIdentifier));
            if (lastRunLatency != null) {
                int delta = timeElapsed - lastRunLatency;
                if (delta > threshold)
                    logger.log(ILogger.Severity.warn, String.format("LATENCY GROWS ALERT: %s %s", testParams.getType(), testParams.getAddress()));
            }
            latencyResults.put(hashGen.genHash(testIdentifier), timeElapsed);
        }
    }
}
