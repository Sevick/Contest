package com.fbytes.contest.Contest.TestReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.Model.TestParams.TestParams;
import com.fbytes.contest.Contest.Model.TestParams.TestParamsFactory;
import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

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

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        // register subclasses of TestParams as jackson subtypes
        Class baseClass = TestParams.class;
        Reflections reflections = new Reflections(baseClass.getPackage().getName());
        Set<Class<? extends TestParams>> children = reflections.getSubTypesOf(TestParams.class);
        children.stream()
                .forEach(cl -> {
                    mapper.registerSubtypes(new NamedType(cl,
                            getClassShortName(cl, 1 + getClassShortName(baseClass, 1).length())
                                    .toLowerCase(Locale.ROOT)
                    ));
                });
    }

    static String getClassShortName(Class cl, int offset) {
        return cl.getName().substring(cl.getName().lastIndexOf('.') + offset);
    }


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
