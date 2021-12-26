package com.fbytes.contest.Contest.ResultWriter;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
public class TestResultWriterFile extends TestResultWriter{

    @Autowired
    private ILogger logger;
    @Value("${testresultwriterfile.logfile:contest.log}")
    private String logFileName;
    @Value("${testresultwriterfile.ignoreWriteErrors:false}")
    private boolean ignoreWriteErrors;

    private FileOutputStream fileOutputStream=null;

    @PreDestroy
    void onDestroy(){
        if (fileOutputStream!=null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
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
