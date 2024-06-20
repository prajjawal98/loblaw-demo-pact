package pact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Utility {

    /**
     * Reads JSON content from a file and returns it as a JsonNode.
     *
     * @param fileName The path to the JSON file.
     * @return JsonNode representing the parsed JSON structure.
     * @throws IOException If there's an error reading the file.
     */
    public static JsonNode getJsonNode(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Read JSON file into JsonNode
        File jsonFile = new File(fileName);
        JsonNode rootNode = mapper.readTree(jsonFile);
        return rootNode;
    }

    /**
     * Converts a JSON-like string into a Map of String to Object.
     * Assumes the input string is in a format similar to JSON key-value pairs.
     *
     * @param body The JSON-like string to parse.
     * @return A Map<String, Object> containing the extracted key-value pairs.
     * @throws IOException If there's an error processing the JSON-like string.
     */
    public static Map<String, Object> getStringObjectMap(String body) {
        if (body.startsWith("{") && body.endsWith("}")) {
            body = body.substring(1, body.length() - 1);
        }

        // Split by commas outside of curly braces
        String[] keyValuePairs = body.split(",(?![^\\{]*\\})");

        // Initialize dataMap to store extracted values
        Map<String, Object> dataMap = new HashMap<>();

        // Extract key-value pairs and populate dataMap
        for (String pair : keyValuePairs) {
            String[] entry = pair.trim().split("=", 2);
            String key = entry[0].trim();
            String value = entry[1].trim();
            dataMap.put(key, value);
        }
        return dataMap;
    }

    /**
     * Extracts the value associated with a specific key from a nested string.
     *
     * @param nestedString The string containing nested key-value pairs.
     * @param key          The key whose value is to be extracted.
     * @return The value associated with the key, or null if the key is not found.
     */
    public static String extractValueForKey(String nestedString, String key) {
        int start = nestedString.indexOf(key + "=");
        if (start == -1) {
            return null; // Key not found
        }
        start += key.length() + 1; // Skip key and '='

        int end = nestedString.indexOf(",", start);
        if (end == -1) {
            end = nestedString.length(); // Reach end of string
        }

        // Check if the end character is '}'
        if (nestedString.charAt(end - 1) == '}') {
            end--; // Exclude the '}'
        }

        return nestedString.substring(start, end).trim();
    }
}
