package pacttest;

import au.com.dius.pact.core.model.messaging.Message;
import com.fasterxml.jackson.databind.JsonNode;
import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit.MessagePactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import pact.ConsumerContract;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import pact.Utility;
import java.io.IOException;
import java.util.HashMap;

import static com.ibm.icu.impl.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pact.Utility.getStringObjectMap;

@Tag("annotations")
@Tag("junit5")
@PactFolder("GeneratedPact")
public class ContractConsumerTest {

//    @Rule
//    public MessagePactProviderRule mockProvider = new MessagePactProviderRule(this);
//
//    @Pact(provider = "user", consumer = "loblaw")
//    public MessagePact userCreatedMessagePact(MessagePactBuilder builder) throws IOException {
//        MessagePact contract = ConsumerContract.contract("28195_pact.json", builder);
//        pact1 = ConsumerContract.getStringObjectMap(contract);
//
//        return contract;
//    }
//    @Test
//    public void testPact() throws IOException {
//        MessagePactBuilder builder = MessagePactBuilder
//                .consumer("Consumer")
//                .hasPactWith("Provider");
//
//        MessagePact pact = ConsumerContract.contract("28195_pact.json", builder);
//
//        List<Message> messages = pact.getMessages();
//        assertEquals(1, messages.size());
//
//        Message message = messages.get(0);
//        String content = new String(message.contentsAsBytes());
//        System.out.println(content); // For debugging purposes
//
//        // Add your assertions to verify the message content
//    }


    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPact() throws IOException {
        MessagePactBuilder builder = MessagePactBuilder
                .consumer("Consumer")
                .hasPactWith("Provider");

        MessagePact pact = ConsumerContract.contract("28195_pact.json", builder);

        List<Message> messages = pact.getMessages();
        assertEquals(1, messages.size());

        Message message = messages.get(0);
        String content = new String(message.contentsAsBytes());
        System.out.println(content); // For debugging purposes

        // Parse the JSON content
        JsonNode actualJson = objectMapper.readTree(content);

        // Load expected JSON from file
        JsonNode expectedJson = objectMapper.readTree(new File("28195_pact.json"));

        // Assert the JSON content dynamically
        assertJsonNodes(expectedJson, actualJson, "/");
    }

    private void assertJsonNodes(JsonNode expected, JsonNode actual, String path) {
        assertEquals(expected.getNodeType(), actual.getNodeType(),
                String.format("Node types do not match at path %s ==> expected: <%s> but was: <%s>", path, expected.getNodeType(), actual.getNodeType()));

        switch (expected.getNodeType()) {
            case OBJECT:
                Iterator<String> fieldNames = expected.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    assertTrue(actual.has(fieldName), "Missing field at path " + path + fieldName);
                    assertJsonNodes(expected.get(fieldName), actual.get(fieldName), path + fieldName + "/");
                }
                break;
            case ARRAY:
                assertTrue(actual.isArray(), "Expected array but got " + actual.getNodeType() + " at path " + path);
                assertEquals(expected.size(), actual.size(), "Array sizes do not match at path " + path);
                for (int i = 0; i < expected.size(); i++) {
                    assertJsonNodes(expected.get(i), actual.get(i), path + "[" + i + "]/");
                }
                break;
            case STRING:
                assertEquals(expected.asText(), actual.asText(), "String values do not match at path " + path);
                break;
            case NUMBER:
                assertEquals(expected.numberValue(), actual.numberValue(), "Number values do not match at path " + path);
                break;
            case BOOLEAN:
                assertEquals(expected.asBoolean(), actual.asBoolean(), "Boolean values do not match at path " + path);
                break;
            case NULL:
                assertTrue(actual.isNull(), "Actual value is not null at path " + path);
                break;
            default:
                fail("Unexpected node type at path " + path + ": " + expected.getNodeType());
        }
    }



//    public Map<String, Object> pact1 = new HashMap<>();
//    @Test
//    @PactVerification("userCreatedMessagePact")
//    public void PersonIdFoundInBq() throws IOException {
//        String body = pact1.toString();
//        System.out.println(body);
////        Map<String, Object> dataMap = getStringObjectMap(body);
////        String viewMetaData = (String) dataMap.get("viewMetaData");
////        String theme = (String) dataMap.get("theme");
////        String layoutString = (String) dataMap.get("layout");
////
////        JsonNode rootNode = Utility.getJsonNode("28195_pact.json");
////        // Extract values from JsonNode
////        String categoryId = rootNode.path("categoryId").asText();
////        String jsonLd = rootNode.path("viewMetaData").path("jsonLd").asText();
////        String contentId = rootNode.path("contentId").asText();
////        String name = rootNode.path("theme").path("name").asText();
////        String typeId = rootNode.path("layout").path("typeId").asText();
////        String addMetaNoRobots = rootNode.path("viewMetaData").path("addMetaNoRobots").toString();
////        String headline = rootNode.path("viewMetaData").path("headline").asText();
////
////        Assert.assertEquals(categoryId, dataMap.get("categoryId"));
////        Assert.assertEquals(jsonLd, Utility.extractValueForKey(viewMetaData, "jsonLd"));
////        Assert.assertEquals(contentId, dataMap.get("contentId"));
////        Assert.assertEquals(name, Utility.extractValueForKey(theme, "name"));
////        Assert.assertEquals(typeId,  Utility.extractValueForKey(layoutString, "typeId"));
////        Assert.assertEquals(addMetaNoRobots, Utility.extractValueForKey(viewMetaData, "addMetaNoRobots"));
////        Assert.assertEquals(headline, Utility.extractValueForKey(viewMetaData, "headline"));
//
//
//    }

}

