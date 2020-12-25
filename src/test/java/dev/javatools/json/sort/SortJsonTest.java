package dev.javatools.json.sort;

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

    @Test
    public void sortJsonString() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path filePath = Path.of(classLoader.getResource("input.json").getPath());
        String inputJson = Files.readString(filePath);
        SortJson sortJson = new SortJson();
        Map<String, String> listFilters = new HashMap<>();
        listFilters.put("list[]", "listKey");
        listFilters.put("innerList[]", "name");
        String sortedJson = sortJson.sort(inputJson, listFilters);
        File file = new File("src/test/resources/output.json");
        saveOutput(sortedJson, file.getAbsolutePath());
    }

    private void saveOutput(String content, String outputPath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write(content);
            writer.close();
        }
        catch (Exception exce) {
            exce.printStackTrace();
        }
    }
}