package net.lomibao.handler;

import net.lomibao.model.lexv2.IntentState;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.util.LexUtil;

public class FallbackIntentHandler extends IntentHandler {
    public FallbackIntentHandler() {
        super("");
    }

    @Override
    public LexV2LambdaResponse handle(LexV2LambdaRequest request) {
        String intentName= LexUtil.getIntentName(request);
        return LexUtil.generateResponse(request,LexUtil.closeAction(), IntentState.Fulfilled, Message.plainText(String.format("no handler found for %s",intentName)));
    }
}
