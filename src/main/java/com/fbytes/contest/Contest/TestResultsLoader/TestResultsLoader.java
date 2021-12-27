package com.fbytes.contest.Contest.TestResultsLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbytes.contest.Contest.HashGen.HashGen;
import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Service
public class TestResultsLoader implements ITestResultsLoader {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ILogger logger;
    @Autowired
    private HashGen hashGen;

    @Value("${contest.testresultsloader.ignoreErrors:true}")
    private boolean ignoreErrors;
    @Value("${contest.testresultsloader.datafile:contest.log}")
    private String datafile;
    @Value("${contest.testresultsloader.readlimit:100}")
    private Integer readLimit;

    @Override
    public List<TestResult> loadTestResults() throws IOException {
        return loadTestResults(datafile);
    }

    @Override
    public List<TestResult> loadTestResults(String fileName) throws IOException {
        List<TestResult> resultList = new LinkedList<>();
        long lineNum = 0;
        long loadedCount = 0;
        try (ReversedLinesFileReader reversedLinesFileReader = new ReversedLinesFileReader(new File(fileName), StandardCharsets.UTF_8)) {
            while (lineNum <= readLimit) {
                lineNum++;
                String nextLine = reversedLinesFileReader.readLine();
                if (nextLine == null)   // we reach the beginning of the file
                    break;

                try {
                    TestResult testResult = mapper.readValue(nextLine, TestResult.class);
                    resultList.add(testResult);
                    loadedCount++;
                } catch (Exception e) {
                    logger.logException(String.format("Exception reading TestResult json on line #%d", lineNum), e);
                    if (!ignoreErrors)
                        throw e;
                }

            }
        } catch (Exception e) {
            logger.logException("Exception loading reasult from file " + fileName, e);
            if (!ignoreErrors)
                throw e;
        }
        logger.log(ILogger.Severity.debug, String.format("TestResultsLoader processed %d lined and loaded %d test results", lineNum, loadedCount));
        return resultList;
    }
}
