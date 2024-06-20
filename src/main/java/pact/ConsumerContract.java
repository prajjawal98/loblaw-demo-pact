package pact;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ConsumerContract {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static MessagePact contract(String fileName, MessagePactBuilder builder) throws IOException {
        Map<String, Object> jsonMap = objectMapper.readValue(new File(fileName), new TypeReference<Map<String, Object>>() {});
        PactDslJsonBody body = new PactDslJsonBody();

        processJson(jsonMap, body);

        return builder.expectsToReceive("a user created message")
                .withContent(body)
                .toPact();
    }

    private static void processJson(Map<String, Object> jsonMap, PactDslJsonBody body) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                PactDslJsonBody nestedBody = new PactDslJsonBody();
                processJson((Map<String, Object>) value, nestedBody);
                body.object(key, nestedBody);
            } else if (value instanceof Number) {
                body.numberType(key, (Number) value);
            } else if (value instanceof Boolean) {
                body.booleanType(key, (Boolean) value);
            } else if (value instanceof String) {
                body.stringValue(key, (String) value);
            } else if (value == null) {
                body.nullValue(key);
            } else {
                // Handle other types if needed
            }
        }
    }

    public static Map<String, Object> getStringObjectMap(MessagePact contract) throws JsonProcessingException {
        String pact = contract.getMessages().get(0).getContents().valueAsString();
        return objectMapper.readValue(pact, new TypeReference<Map<String, Object>>() {});
    }

}
