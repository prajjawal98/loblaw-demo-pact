package pact;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConsumerContract {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a Pact for the consumer based on JSON data read from a file.
     *
     * @param fileName The name of the JSON file containing the data.
     * @param builder  The MessagePactBuilder to build the Pact.
     * @return A MessagePact representing the contract between consumer and provider.
     * @throws IOException If there is an error reading the JSON file.
     */
    public static MessagePact contract(String fileName, MessagePactBuilder builder) throws IOException {
        // Read JSON data from file into a Map
        Map<String, Object> jsonMap = objectMapper.readValue(new File(fileName), new TypeReference<Map<String, Object>>() {});

        // Create a PactDslJsonBody to build the Pact content
        PactDslJsonBody body = new PactDslJsonBody();

        // Process JSON data recursively
        processJson(jsonMap, body);

        // Build and return the Pact using the provided builder
        return builder.expectsToReceive("a user created message")
                .withContent(body)
                .toPact();
    }

    /**
     * Recursively processes a JSON Map and builds a PactDslJsonBody.
     *
     * @param jsonMap The JSON data as a Map of String keys to Object values.
     * @param body    The PactDslJsonBody to which the JSON data is added.
     */
    private static void processJson(Map<String, Object> jsonMap, PactDslJsonBody body) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                // If value is a Map, recursively process it
                PactDslJsonBody nestedBody = new PactDslJsonBody();
                processJson((Map<String, Object>) value, nestedBody);
                body.object(key, nestedBody);
            } else if (value instanceof List) {
                // If value is a List, process it as an array
                processJsonArray((List<Object>) value, body, key);
            } else if (value instanceof Number) {
                // If value is a Number, add it as a number type
                body.numberType(key, (Number) value);
            } else if (value instanceof Boolean) {
                // If value is a Boolean, add it as a boolean type
                body.booleanType(key, (Boolean) value);
            } else if (value instanceof String) {
                // If value is a String, add it as a string value
                body.stringValue(key, (String) value);
            } else if (value == null) {
                // If value is null, add it as a null value
                body.nullValue(key);
            } else {
                // Handle other types if needed (not implemented in current example)
                // This block could be used to handle other types of values if necessary
            }
        }
    }

    /**
     * Processes a JSON array (List) and adds it to the PactDslJsonBody.
     *
     * @param list The JSON array as a List of Objects.
     * @param body The PactDslJsonBody to which the JSON array is added.
     * @param key  The key under which the array is added in the PactDslJsonBody.
     */
    private static void processJsonArray(List<Object> list, PactDslJsonBody body, String key) {
        PactDslJsonBody arrayObject = new PactDslJsonBody();

        int index = 0;
        for (Object item : list) {
            String arrayKey = key + "_" + index;
            if (item instanceof Map) {
                // If item in the array is a Map, recursively process it
                PactDslJsonBody nestedBody = new PactDslJsonBody();
                processJson((Map<String, Object>) item, nestedBody);
                arrayObject.object(arrayKey, nestedBody);
            } else if (item instanceof Number) {
                // If item in the array is a Number, add it as a number type
                arrayObject.numberType(arrayKey, (Number) item);
            } else if (item instanceof Boolean) {
                // If item in the array is a Boolean, add it as a boolean type
                arrayObject.booleanType(arrayKey, (Boolean) item);
            } else if (item instanceof String) {
                // If item in the array is a String, add it as a string value
                arrayObject.stringValue(arrayKey, (String) item);
            } else if (item == null) {
                // If item in the array is null, add it as a null value
                arrayObject.nullValue(arrayKey);
            }
            index++;
        }
        // Add the arrayObject to the main body under the given key
        body.object(key, arrayObject);
    }

}
