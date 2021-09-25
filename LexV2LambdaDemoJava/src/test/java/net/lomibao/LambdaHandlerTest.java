package net.lomibao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.lomibao.model.lexv2.Intent;
import net.lomibao.model.lexv2.Interpretation;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.model.lexv2.SessionState;
import net.lomibao.model.lexv2.Slot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LambdaHandlerTest {
    private static LambdaHandler lambdaHandler;

    @BeforeAll
    public static void setup() {
        lambdaHandler = new LambdaHandler(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .registerModule(new JavaTimeModule()));
    }

    @AfterAll
    public static void stopServer() {
        if (lambdaHandler != null) {
            lambdaHandler.getApplicationContext().close();
        }
    }

    @Test
    public void helloHandler() {
        Intent intent = Intent.builder().name("Hello").build();
        LexV2LambdaRequest request = LexV2LambdaRequest.builder()
                .interpretations(List.of(Interpretation.builder()
                        .intent(intent)
                        .build()))
                .sessionState(SessionState.builder()
                        .intent(intent)
                        .build())
                .build();
        LexV2LambdaResponse response = lambdaHandler.run(request);
        String responseMessage = response.getMessages().stream().map(Message::getContent).collect(Collectors.joining("\n"));
        assertNotNull(response);
        assertEquals("Howdy! Welcome to Lex V2. I'm a Java Lambda", responseMessage);
        request.getSessionState().getIntent()
                .setSlots(Map.of("name", Slot.builder()
                        .value(Slot.SlotValue.builder()
                                .interpretedValue("Derek").build()).build()));
        response = lambdaHandler.run(request);
        responseMessage = response.getMessages().stream().map(Message::getContent).collect(Collectors.joining("\n"));
        assertEquals("Hello, Derek! Welcome to Lex V2. I'm a Java Lambda", responseMessage);
    }

    @Test
    public void fallbackHandler() {
        String intentName="FakeIntentWithNoHandler";
        Intent intent = Intent.builder().name(intentName).build();
        LexV2LambdaRequest request = LexV2LambdaRequest.builder()
                .interpretations(List.of(Interpretation.builder()
                        .intent(intent)
                        .build()))
                .sessionState(SessionState.builder()
                        .intent(intent)
                        .build())
                .build();
        LexV2LambdaResponse response = lambdaHandler.run(request);
        String responseMessage = response.getMessages().stream().map(Message::getContent).collect(Collectors.joining("\n"));
        assertNotNull(response);
        assertEquals("no handler found for "+intentName, responseMessage);

    }
    @Test
    public void vehicleInventorySearchHandler() {
        String intentName="VehicleInventorySearch";
        Intent intent = Intent.builder().name(intentName).build();
        LexV2LambdaRequest request = LexV2LambdaRequest.builder()
                .interpretations(List.of(Interpretation.builder()
                        .intent(intent)
                        .build()))
                .sessionState(SessionState.builder()
                        .intent(intent)
                        .build())
                .build();
        LexV2LambdaResponse response = lambdaHandler.run(request);
        String responseMessage = response.getMessages().stream().map(Message::getContent).collect(Collectors.joining("\n"));
        assertNotNull(response);
        assertNotEquals("no handler found for "+intentName, responseMessage);

    }
}
