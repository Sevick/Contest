package com.fbytes.contest.Contest.TestReader;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsFactory;
import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

@Service
public class TestReader implements ITestReader {
    @Autowired
    private ILogger logger;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TestParamsFactory testParamsFactory;

    @Value("${contest.ignoreinvalidconfig:true}")
    private boolean ignoreInvalidConfig;

    @Override
    public void retrieveTests(InputStream inputStream, ITestExecutor testExecutor) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            long lineNum = 1;
            Scanner inScan = new Scanner(inputStream);
            while (inScan.hasNext()) {
                try {
                    TestParams testParams = testParamsFactory.getTestParams(inScan.next());
                    testParams.setId(String.format("%d", lineNum));
                    testExecutor.execTest(testParams);
                } catch (Exception e) {
                    logger.logException(String.format("Test#%d Exception reading json", lineNum), e);
                    if (!ignoreInvalidConfig)
                        throw e;
                }
                lineNum++;
            }
        }
    }
}

