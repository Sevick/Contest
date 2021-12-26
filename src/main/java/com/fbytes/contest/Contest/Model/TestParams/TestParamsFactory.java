package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class TestParamsFactory {
    private final static ObjectMapper mapper = new ObjectMapper();

    static String getClassShortName(Class cl, int offset) {
        return cl.getName().substring(cl.getName().lastIndexOf('.') + offset);
    }

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

    public TestParams getTestParams(String json) throws JsonProcessingException, InvalidTypeIdException {
        return mapper.readValue(json, TestParams.class);
    }
}
