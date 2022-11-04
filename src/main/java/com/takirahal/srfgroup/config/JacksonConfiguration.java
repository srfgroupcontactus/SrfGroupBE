package com.takirahal.srfgroup.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;


@Configuration
public class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    /*
     * Module for serialization/deserialization of RFC7807 Problem.
     */
    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }

    /*
     * Module for serialization/deserialization of ConstraintViolationProblem.
     */
    @Bean
    public ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }


    /*
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();


        // fields set to be ignore in serialization
        String[] ignorableFieldNames = {"d_createdDate", "d_lastModifiedDate", "d_dateCreated", "date_created", "dateCreated", "date_created", "d_date_created"};
        SimpleBeanPropertyFilter ignoreFieldFilter = SimpleBeanPropertyFilter.serializeAllExcept(
                ignorableFieldNames);
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("ignoredFields", ignoreFieldFilter);
        objectMapper.setFilterProvider(filters);


        // objectMapper.addMixIn(Object.class, UserDetailsResource.class);


        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        jsonConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonConverter.getObjectMapper().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy")));


        javaTimeModule.addDeserializer(Instant.class, new InstantCustomDeserializer());
        jsonConverter.getObjectMapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        jsonConverter.getObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        jsonConverter.getObjectMapper().registerModule(javaTimeModule);


        return jsonConverter;
    }



    static class InstantCustomDeserializer extends JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String dateString = p.getText().trim();
            if (StringUtils.isNotBlank(dateString)) {
                Date pareDate;
                try {
                    pareDate = new Date(dateString); // DateFormatUtil.pareDate(dateString);
                    if (null != pareDate) {
                        return pareDate.toInstant();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
    */
}
