package com.fbytes.contest.Contest.Model.TestParams;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;

import java.util.Locale;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TestParamsHttp.class, name = "http"),
        @JsonSubTypes.Type(value = TestParamsHttps.class, name = "https"),
        @JsonSubTypes.Type(value = TestParamsDns.class, name = "dns")
})
public abstract class TestParams{

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
                            getClassShortName(cl, 1+getClassShortName(baseClass,1).length())
                                    .toLowerCase(Locale.ROOT)
                    ));
                });
    }


    private String type;
    private String id;  // !!! Ignore/NotIgnore
    @JsonProperty(required = true)
    private String address;

    public String genIdentifier(){
        return String.format("%s-%s", type, address);
    }

    public String toString(){
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
