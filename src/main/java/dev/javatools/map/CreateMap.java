package dev.javatools.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Use this for creating a Map from Json String or from models or from json String stored in a file.
 */
public class CreateMap {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert json string that is in the file into a Map.
     *
     * @param input, file containing the json String.
     * @return returns Map representation of the File content
     * @throws IOException Throws IOException, if the provided Json is not valid or if the
     * file does not exists.
     */
    public Map<String, Object> create(File input) throws IOException {
        Path tempFilePath = Path.of(input.getAbsolutePath());
        String jsonString = Files.readString(tempFilePath);
        return create(jsonString);
    }

    /**
     * Create a Map from json String
     *
     * @param input Json String
     * @return returns Map representation of the File content
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Json is not valid
     */
    public Map<String, Object> create(String input) throws JsonProcessingException {
        Map<String, Object> mapInput = objectMapper.readValue(input, Map.class);
        return mapInput;
    }

    /**
     * Convert any custom object into a Map.
     *
     * @param input And custome model object that you want to convert to Map
     * @return returns Map representation of the File content
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Object is not valid model
     */
    public Map<String, Object> create(Object input) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(input);
        Map<String, Object> mapInput = objectMapper.readValue(jsonString, Map.class);
        return mapInput;
    }

}
