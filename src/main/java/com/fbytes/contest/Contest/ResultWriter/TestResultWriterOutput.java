package com.fbytes.contest.Contest.ResultWriter;

import com.fbytes.contest.Contest.Model.TestResults.TestResult;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class TestResultWriterOutput extends TestResultWriter {

    @Override
    public void write(TestResult testResult){
        System.out.println(testResult);
    }
}
