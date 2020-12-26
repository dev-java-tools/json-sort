package dev.javatools.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SortJsonTest {

    ClassLoader classLoader = getClass().getClassLoader();
    private String sampleInput;
    private String expectedOutput;
    private String expectedOutputWithoutListObjectsSorted;
    private Map<String, String> listFilters;
    private File inputJsonFile;
    private SortJson sortJson;

    @Before
    public void initTests() throws IOException {
        Path inputFilePath = Path.of(classLoader.getResource("sample-input.json").getPath());
        sampleInput = Files.readString(inputFilePath);
        inputJsonFile = new File("src/test/resources/sample-input.json");
        Path outputFilePath = Path.of(classLoader.getResource("expected-output.json").getPath());
        expectedOutput = Files.readString(outputFilePath);
        Path expectedOutputWithoutListObjectsSortedFilePath = Path.of(classLoader.getResource("expected-output-without-list-objects-sorted.json").getPath());
        expectedOutputWithoutListObjectsSorted = Files.readString(expectedOutputWithoutListObjectsSortedFilePath);
        listFilters = new HashMap<>();
        listFilters.put("list[]", "listKey");
        listFilters.put("innerList[]", "name");
        sortJson = new SortJson();
    }

    @Test
    public void getSortedJsonStringTest() throws JsonProcessingException {
        String sortedJson = sortJson.getSortedJson(sampleInput);
        Assert.assertEquals(expectedOutputWithoutListObjectsSorted, sortedJson);
        saveOutput(sortedJson, "src/test/resources/actual-output1.json");
    }

    @Test
    public void getSortedJsonStringWithListKeyTest() throws JsonProcessingException {
        String sortedJson = sortJson.getSortedJson(sampleInput, listFilters);
        Assert.assertEquals(expectedOutput, sortedJson);
        saveOutput(sortedJson, "src/test/resources/actual-output2.json");
    }

    @Test
    public void getSortedJsonFileTest() throws IOException {
        String sortedJson = sortJson.getSortedJson(inputJsonFile);
        Assert.assertEquals(expectedOutputWithoutListObjectsSorted, sortedJson);
        saveOutput(sortedJson, "src/test/resources/actual-output3.json");
    }

    @Test
    public void getSortedJsonFileWithListKeyTest() throws IOException {
        String sortedJson = sortJson.getSortedJson(inputJsonFile, listFilters);
        Assert.assertEquals(expectedOutput, sortedJson);
        saveOutput(sortedJson, "src/test/resources/actual-output4.json");
    }

    @Test
    public void getSortedJsonObjectTest() throws JsonProcessingException {
        //TODO: Work on models after preparing the clean test data.
    }

    @Test
    public void getSortedJsonObjectWithListKeyTest() throws JsonProcessingException {
        //TODO: Work on models after preparing the clean test data.
    }

    private void saveOutput(String content, String outputPath) {
        try {
            File actualOutput = new File(outputPath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(actualOutput.getAbsolutePath()));
            writer.write(content);
            writer.close();
        } catch (Exception exce) {
            exce.printStackTrace();
        }
    }
}