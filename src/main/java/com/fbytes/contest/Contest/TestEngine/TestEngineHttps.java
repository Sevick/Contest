package com.fbytes.contest.Contest.TestEngine;

import com.fbytes.contest.Contest.Logger.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class TestEngineHttps extends TestEngineHttp {
    @Autowired
    private ILogger logger;

    @Override
    protected HttpURLConnection getConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
