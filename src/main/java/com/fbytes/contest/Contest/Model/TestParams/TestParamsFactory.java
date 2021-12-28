package com.fbytes.contest.Contest.Model.TestParams;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fbytes.contest.Contest.Logger.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;

@Service
public class TestParamsFactory {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ILogger logger;

    @PostConstruct
    private void init() {
        // search for TestParams ancestors in the same package and register jackson subtypes
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(TestParams.class));
        Set<BeanDefinition> components = provider.findCandidateComponents(TestParams.class.getPackageName().replaceAll("[.]", "/"));
        components.forEach(component -> {
            logger.log(ILogger.Severity.debug, "Register TestParams subtype: " + component.getBeanClassName());
            try {
                Class<?> paramsImplClass = Class.forName(component.getBeanClassName());
                String subType = getJsonClassAnnotationValue(paramsImplClass);
                mapper.registerSubtypes(new NamedType(paramsImplClass, subType));
            } catch (Exception e) {
                logger.logException(e);
                throw new RuntimeException(e);
            }
        });
    }

    public TestParams getTestParams(String json) throws JsonProcessingException {
        return mapper.readValue(json, TestParams.class);
    }

    private String getJsonClassAnnotationValue(Class<?> cl) {
        return ((JsonTypeName) Arrays.stream(cl.getAnnotations())
                .filter(a -> "com.fasterxml.jackson.annotation.JsonTypeName".equals(a.annotationType().getName()))
                .findAny().orElseThrow()).value();
    }
}
