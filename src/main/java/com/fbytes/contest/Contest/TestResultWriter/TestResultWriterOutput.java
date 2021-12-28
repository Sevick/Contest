package com.fbytes.contest.Contest.TestResultWriter;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestResultWriterOutput extends TestResultWriter {
    @Autowired
    private ILogger logger;
    @Value("${contest.testresultwriteroutput.ignoreWriteErrors:false}")
    private Boolean ignoreWriteErrors;

    @Override
    public void write(TestResult testResult) {
        try {
            System.out.println(testResult);
        } catch (Exception e) {
            logger.logException("Failed to write: " + e.getMessage(), e);
            if (!ignoreWriteErrors)
                throw e;
        }
    }
}
