package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fbytes.contest.Contest.Logger.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
public class TestParamsFactory {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ILogger logger;

    @PostConstruct
    private void init() {
        mapper.registerSubtypes(new NamedType(TestParamsHttp.class, getJsonClassAnnotationValue(TestParamsHttp.class)));
        mapper.registerSubtypes(new NamedType(TestParamsHttps.class, getJsonClassAnnotationValue(TestParamsHttps.class)));
        mapper.registerSubtypes(new NamedType(TestParamsDns.class, getJsonClassAnnotationValue(TestParamsDns.class)));
    }

    private String getJsonClassAnnotationValue(Class<?> cl) {
        try {
            return ((JsonTypeName) Arrays.stream(cl.getAnnotations())
                    .filter(a -> "com.fasterxml.jackson.annotation.JsonTypeName".equals(a.annotationType().getName()))
                    .findAny().orElseThrow()).value();
        }
        catch (Exception e){
            logger.logException("Class "+cl.getName()+ " must have @JsonTypeName annotation", e);
            throw e;
        }
    }


    public TestParams getTestParams(String json) throws JsonProcessingException {
        return mapper.readValue(json, TestParams.class);
    }
}
