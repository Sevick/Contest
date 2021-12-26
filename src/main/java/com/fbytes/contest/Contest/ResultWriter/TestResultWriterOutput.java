package com.fbytes.contest.Contest.ResultWriter;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class TestResultWriterOutput extends TestResultWriter {
    @Autowired
    ILogger logger;
    @Value("${contest.testresultwriteroutput.ignoreWriteErrors:false}")
    private boolean ignoreWriteErrors;

    @Override
    public void write(TestResult testResult){
        try {
            System.out.println(testResult);
        }
        catch (Exception e){
            logger.logException("Failed to write: "+e.getMessage(),e);
            if (!ignoreWriteErrors)
                throw e;
        }    }
}
