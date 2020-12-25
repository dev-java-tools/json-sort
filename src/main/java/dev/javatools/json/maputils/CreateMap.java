package dev.javatools.json.maputils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class CreateMap {

    ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> create(File input) throws IOException {
        Path tempFilePath = Path.of(input.getAbsolutePath());
        String jsonString = Files.readString(tempFilePath);
        return create(jsonString);
    }

    public Map<String, Object> create(String input) throws JsonProcessingException {
        Map<String, Object> mapInput = objectMapper.readValue(input, Map.class);
        return mapInput;
    }

    public Map<String, Object> create(Object input) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(input);
        Map<String, Object> mapInput = objectMapper.readValue(jsonString, Map.class);
        return mapInput;
    }

}
