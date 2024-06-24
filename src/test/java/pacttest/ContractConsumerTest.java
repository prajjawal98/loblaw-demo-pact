package pacttest;

import au.com.dius.pact.core.model.messaging.Message;
import com.fasterxml.jackson.databind.JsonNode;
import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit.MessagePactProviderRule;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import pact.ConsumerContract;
import java.io.File;
import java.util.List;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


@Tag("annotations")
@Tag("junit5")
@PactFolder("GeneratedPact")
public class ContractConsumerTest {

    @Rule
    public MessagePactProviderRule mockProvider = new MessagePactProviderRule(this);

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

        // Parse the generated Pact content
        JsonNode actualJson = objectMapper.readTree(content);

        // Load expected JSON from file
        File expectedFile = new File("28195_pact.json");
        JsonNode expectedJson = objectMapper.readTree(expectedFile);
        // Assert specific values
        verifyValues(expectedJson, actualJson);

    }

    private void verifyValues(JsonNode expectedJson, JsonNode actualJson) {
        // Example: Verify specific fields for null values
        // System.out.println(actualJson.path("contentId"));
        assertEquals("null", actualJson.path("contentId").asText());
        assertEquals("null", actualJson.path("layout").path("sections").path("productListingSection").path("components")
                .path("components_0").path("data").path("categoryNavigation").path("headline").asText());
        JsonNode childNodes = actualJson
                .path("layout").path("sections").path("productListingSection")
                .path("components").path("components_0").path("data")
                .path("categoryNavigation").path("navigation")
                .path("childNodes");
        for (int i = 0; i <= childNodes.size()-1; i++) {
            String childNodes0 = actualJson
                    .path("layout").path("sections").path("productListingSection")
                    .path("components").path("components_0").path("data")
                    .path("categoryNavigation").path("navigation")
                    .path("childNodes").path("childNodes_" + i).path("childNodes").asText();
            assertEquals("null", childNodes0);
        }

        for (int i = 0; i <= childNodes.size()-1; i++) {
            String active = actualJson
                    .path("layout").path("sections").path("productListingSection")
                    .path("components").path("components_0").path("data")
                    .path("categoryNavigation").path("navigation")
                    .path("childNodes").path("childNodes_" + i).path("active").asText();
            assertEquals("null", active);
        }

        JsonNode productTiles = actualJson
                .path("layout")
                .path("sections")
                .path("productListingSection")
                .path("components")
                .path("components_0")
                .path("data")
                .path("productGrid")
                .path("productTiles");
        System.out.println(productTiles.size());

        for (int i = 0; i <= productTiles.size()-1; i++) {
            String deal = actualJson
                    .path("layout")
                    .path("sections")
                    .path("productListingSection")
                    .path("components")
                    .path("components_0")
                    .path("data")
                    .path("productGrid")
                    .path("productTiles")
                    .path("productTiles_" + i)
                    .path("deal").asText();
            assertTrue("null".equals(deal) || deal.isEmpty());
        }

        for (int i = 0; i <= productTiles.size()-1; i++) {
            String pcoDeal = actualJson
                    .path("layout")
                    .path("sections")
                    .path("productListingSection")
                    .path("components")
                    .path("components_0")
                    .path("data")
                    .path("productGrid")
                    .path("productTiles")
                    .path("productTiles_" + i)
                    .path("pcoDeal").asText();
            assertTrue("null".equals(pcoDeal) || pcoDeal.isEmpty());
        }

        for (int i = 0; i <= productTiles.size()-1; i++) {
            String title = actualJson
                    .path("layout")
                    .path("sections")
                    .path("productListingSection")
                    .path("components")
                    .path("components_0")
                    .path("data")
                    .path("productGrid")
                    .path("productTiles")
                    .path("productTiles_"+i)
                    .path("title").asText();
            assertFalse(title.equalsIgnoreCase("null"));
        }

        // Example: Verify specific fields for string values
        assertEquals(expectedJson.path("categoryId").asText(), actualJson.path("categoryId").asText());
        assertEquals(expectedJson.path("categoryDisplayName").asText(), actualJson.path("categoryDisplayName").asText());

        // Example: Verify specific fields for numeric values
        assertEquals(expectedJson.path("productGrid").path("pagination").path("pageNumber").asInt(),
                actualJson.path("productGrid").path("pagination").path("pageNumber").asInt());
        assertEquals(expectedJson.path("productGrid").path("pagination").path("pageSize").asInt(),
                actualJson.path("productGrid").path("pagination").path("pageSize").asInt());
        assertEquals(expectedJson.path("productGrid").path("pagination").path("totalResults").asInt(),
                actualJson.path("productGrid").path("pagination").path("totalResults").asInt());
    }

}

