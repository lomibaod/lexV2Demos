package net.lomibao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import net.lomibao.handler.FallbackIntentHandler;
import net.lomibao.handler.IntentHandler;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;

import java.util.Collection;

/**
 * Uses Micronaut for lighterweight dependency injection than spring.
 * see https://micronaut-projects.github.io/micronaut-spring/latest/guide/index.html
 * to see how spring annotations map to micronaut
 *
 */
@Introspected
public class LambdaHandler extends MicronautRequestHandler<JsonNode, LexV2LambdaResponse> {

    private ObjectMapper objectMapper;

    public LambdaHandler(){
        super();
        this.objectMapper= applicationContext.getBean(ObjectMapper.class);
    }

    public LambdaHandler(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;

    }



    /**
     * the primary handler, loads all beans matching IntentHandler abstract class and checks if it matches the request
     *
     * @param request the LexV2 request from Lex
     * @return LexV2LambdaResponse which tells the Lex bot what to do next
     */
    public LexV2LambdaResponse run(LexV2LambdaRequest request) {
        Collection<IntentHandler> handlers = applicationContext.getBeansOfType(IntentHandler.class);
        IntentHandler matchedHandler = handlers.stream().filter(h -> h.matchesIntent(request)).findFirst().orElse(new FallbackIntentHandler());
        return matchedHandler.handle(request);
    }


    /**
     * entry point method to allow us to easily log input and output,
     * delegates the actual running to the "run" method
     *
     * @param input Json that is expected to be in the format of LexV2LambdaRequest,
     *              but if it isn't, at least will get logged
     * @return LexV2LambdaResponse
     */
    @Override
    public LexV2LambdaResponse execute(JsonNode input) {
        System.out.printf("input=%s\n", input.toString());
        try {
            LexV2LambdaRequest request = objectMapper.treeToValue(input, LexV2LambdaRequest.class);
            LexV2LambdaResponse response = run(request);
            java.lang.String output = objectMapper.writeValueAsString(response);
            System.out.printf("output=%s\n", output);
            return response;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     def close(intent_request, session_attributes, fulfillment_state, message):
     intent_request['sessionState']['intent']['state'] = fulfillment_state
     return {
     'sessionState': {
     'sessionAttributes': session_attributes,
     'dialogAction': {
     'type': 'Close'
     },
     'intent': intent_request['sessionState']['intent']
     },
     'messages': [message],
     'sessionId': intent_request['sessionId'],
     'requestAttributes': intent_request['requestAttributes'] if 'requestAttributes' in intent_request else None
     }
     */
}
