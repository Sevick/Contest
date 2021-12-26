package com.fbytes.contest.Contest;

import com.fbytes.contest.Contest.Model.TestParams.TestParamsHttp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDataProducer {

    @Test
    public void generateTestParameters() {
        System.out.println(new TestParamsHttp("777", "http://yahoo.com", "HEAD", 301, false, false));
        System.out.println(new TestParamsHttp("777", "http://yahoo.com", "HEAD", 200, false, false));
    }

}
