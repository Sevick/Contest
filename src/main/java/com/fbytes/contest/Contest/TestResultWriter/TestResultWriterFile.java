package com.fbytes.contest.Contest.TestResultWriter;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@Profile("!skiplogger")
public class TestResultWriterFile extends TestResultWriter{

    @Autowired
    private ILogger logger;
    @Value("${contest.testresultwriterfile.logfile:contest.log}")
    private String logFileName;
    @Value("${contest.testresultwriterfile.ignoreWriteErrors:false}")
    private boolean ignoreWriteErrors;

    private FileOutputStream fileOutputStream=null;

    @PreDestroy
    void onDestroy(){
        if (fileOutputStream!=null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public void init() throws FileNotFoundException {
        try{
            fileOutputStream = new FileOutputStream(logFileName,true);
        } catch (Exception e) {
            logger.logException("Unable to open output stream for logfile: "+logFileName+"   "+e.getMessage(),e);
            throw e;
        }
    }

    @Override
    public void write(TestResult testResult) throws Exception {
        if (fileOutputStream==null){
            init();
        }
        try {
            fileOutputStream.write(String.format("%s%s",testResult.toString(), "\n" ).getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            logger.logException("Failed to write: "+e.getMessage(),e);
            if (!ignoreWriteErrors)
                throw e;
        }
    }
}
