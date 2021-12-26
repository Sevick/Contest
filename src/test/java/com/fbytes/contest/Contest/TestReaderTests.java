package com.fbytes.contest.Contest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbytes.contest.Contest.TestProcessor.ITestExecutor;
import com.fbytes.contest.Contest.TestReader.ITestReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.InputStream;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class TestReaderTests {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ITestReader testReader;

    @Test
    public void testReader() throws Exception {
        String fileName = "input.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        ITestExecutor mockTestExecutor = mock(ITestExecutor.class);
        testReader.retrieveTests(inputStream, mockTestExecutor);
        verify(mockTestExecutor, times(3)).execTest(any());
    }
}
