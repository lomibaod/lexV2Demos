package net.lomibao.handler;

import net.lomibao.model.lexv2.Intent;
import net.lomibao.model.lexv2.Interpretation;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.util.LexUtil;

import java.util.Locale;

public abstract class IntentHandler {
    public final String INTENT_NAME;
    public IntentHandler(String intentName){
        INTENT_NAME=intentName;
    }

    public abstract LexV2LambdaResponse handle(LexV2LambdaRequest request);

    public boolean matchesIntent(LexV2LambdaRequest request){
        return LexUtil.matchesIntent(request,INTENT_NAME);
    }


}
