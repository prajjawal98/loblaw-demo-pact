package pacttest;

import com.fasterxml.jackson.databind.JsonNode;
import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit.MessagePactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import pact.ConsumerContract;
import java.util.Map;
import pact.Utility;
import java.io.IOException;
import java.util.HashMap;
import static pact.Utility.getStringObjectMap;

@Tag("annotations")
@Tag("junit5")
@PactFolder("GeneratedPact")
public class ContractConsumerTest {

    @Rule
    public MessagePactProviderRule mockProvider = new MessagePactProviderRule(this);

    @Pact(provider = "user", consumer = "loblaw")
    public MessagePact userCreatedMessagePact(MessagePactBuilder builder) throws IOException {
        MessagePact contract = ConsumerContract.contract("28195_pact.json", builder);
        pact1 = ConsumerContract.getStringObjectMap(contract);

        return contract;
    }


    public Map<String, Object> pact1 = new HashMap<>();
    @Test
    @PactVerification("userCreatedMessagePact")
    public void PersonIdFoundInBq() throws IOException {
        String body = pact1.toString();
        Map<String, Object> dataMap = getStringObjectMap(body);
        String viewMetaData = (String) dataMap.get("viewMetaData");
        String theme = (String) dataMap.get("theme");
        String layoutString = (String) dataMap.get("layout");

        JsonNode rootNode = Utility.getJsonNode("28195_pact.json");
        // Extract values from JsonNode
        String categoryId = rootNode.path("categoryId").asText();
        String jsonLd = rootNode.path("viewMetaData").path("jsonLd").asText();
        String contentId = rootNode.path("contentId").asText();
        String name = rootNode.path("theme").path("name").asText();
        String typeId = rootNode.path("layout").path("typeId").asText();
        String addMetaNoRobots = rootNode.path("viewMetaData").path("addMetaNoRobots").toString();
        String headline = rootNode.path("viewMetaData").path("headline").asText();

        Assert.assertEquals(categoryId, dataMap.get("categoryId"));
        Assert.assertEquals(jsonLd, Utility.extractValueForKey(viewMetaData, "jsonLd"));
        Assert.assertEquals(contentId, dataMap.get("contentId"));
        Assert.assertEquals(name, Utility.extractValueForKey(theme, "name"));
        Assert.assertEquals(typeId,  Utility.extractValueForKey(layoutString, "typeId"));
        Assert.assertEquals(addMetaNoRobots, Utility.extractValueForKey(viewMetaData, "addMetaNoRobots"));
        Assert.assertEquals(headline, Utility.extractValueForKey(viewMetaData, "headline"));


    }

}

