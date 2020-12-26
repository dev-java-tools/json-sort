package dev.javatools.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.javatools.map.CreateMap;
import dev.javatools.map.SortMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class has all the apis needs to sort a Json String.
 *
 * How to use listKeys?
 *  If the json has a list of objects and if you need to sort the list based on a
 *  field of an object, this feature will help.
 *
 *  Here is how it works.
 *      Take the below Json as an example
 *          {
 *              "members": [
 *                  {
 *                      "name": "John"
 *                      "age": 22
 *                  }
 *                  {
 *                      "name": "Bob"
 *                      "age": 18
 *                  }
 *              ]
 *          }
 *      If you need the sort the above Json based on the member name. You need to pass
 *          listKey.put("members[]", "name");
 *     If you need the sort the above Json based on the member age. You need to pass
 *          listKey.put("members[]", "age");
 */
public class SortJson {

    private ObjectMapper objectMapper = new ObjectMapper();
    private CreateMap createMap = new CreateMap();

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @return returns the formatted and sorted json in String representation
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Json is not valid
     */
    public String getSortedJson(final String input) throws JsonProcessingException {
        return getSortedJson(createMap.create(input));
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @return returns the formatted and sorted json in String representation
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Json is not valid
     */
    public String getSortedJson(final String input, Map<String, String> listKeys) throws JsonProcessingException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @return returns the formatted and sorted json in String representation
     * @throws IOException Throws IOException, if the provided Json is not valid or if the
     * file does not exists.
     */
    public String getSortedJson(final File input) throws IOException {
        return getSortedJson(createMap.create(input));
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @return returns the formatted and sorted json in String representation
     * @throws IOException Throws IOException, if the provided Json is not valid or if the
     * file does not exists.
     */
    public String getSortedJson(final File input, Map<String, String> listKeys) throws IOException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @return returns the formatted and sorted json in String representation
     * @throws IOException Throws IOException, if the provided Json is not valid or if the
     * file does not exists.
     */
    public String getSortedJson(final Object input) throws IOException {
        return getSortedJson(createMap.create(input));
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @return returns the formatted and sorted json in String representation
     * @throws IOException Throws IOException, if the provided Json is not valid or if the
     * file does not exists.
     */
    public String getSortedJson(final Object input, Map<String, String> listKeys) throws IOException {
        return getSortedJson(createMap.create(input), listKeys);
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @return returns the formatted and sorted json in String representation
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Json is not valid
     */
    public String getSortedJson(final Map<String, Object> input) throws JsonProcessingException {
        return getSortedJson(input, null);
    }

    /**
     *
     * Sorts the given json String and respond the sorted and formatted String.
     *
     * @param input Json String
     * @param listKeys List of field names that needs to be sorted, see the class documentation for more details
     * @return returns the formatted and sorted json in String representation
     * @throws JsonProcessingException Throws JsonProcessingException, if the provided Json is not valid
     */
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
