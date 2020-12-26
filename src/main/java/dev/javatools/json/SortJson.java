package dev.javatools.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.javatools.map.CreateMap;
import dev.javatools.map.SortMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SortJson {

    private ObjectMapper objectMapper = new ObjectMapper();
    private CreateMap createMap = new CreateMap();

    public String getSortedJson(final String input) throws JsonProcessingException {
        return getSortedJson(createMap.create(input));
    }

    public String getSortedJson(final String input, Map<String, String> listKeys) throws JsonProcessingException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    public String getSortedJson(final File input) throws IOException {
        return getSortedJson(createMap.create(input));
    }

    public String getSortedJson(final File input, Map<String, String> listKeys) throws IOException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    public String getSortedJson(final Object input) throws IOException {
        return getSortedJson(createMap.create(input));
    }

    public String getSortedJson(final Object input, Map<String, String> listKeys) throws IOException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    public String getSortedJson(final Map<String, Object> input) throws JsonProcessingException {
        return getSortedJson(input, null);
    }

    private String getSortedJson(final Map<String, Object> input, final Map<String, String> listKeys) throws JsonProcessingException {
        SortMap sortMap = new SortMap();
        Map<String, Object> sorted;
        if (null == listKeys) {
            sorted = sortMap.getSortedMap(input);
        } else {
            sorted = sortMap.getSortedMap(input, listKeys);
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sorted);
    }

}
