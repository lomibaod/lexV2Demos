package net.lomibao.handler;

import io.micronaut.context.annotation.Bean;
import net.lomibao.model.lexv2.IntentState;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.util.LexUtil;

@Bean //eqive to spring @Component
public class HelloIntentHandler extends IntentHandler {


    public HelloIntentHandler() {
        super("Hello");
    }

    @Override
    public LexV2LambdaResponse handle(LexV2LambdaRequest request) {
        String name = LexUtil.getSlot(request, "name");
        if (name != null) {
            return LexUtil.generateResponse(request, LexUtil.closeAction(), IntentState.Fulfilled, Message.plainText(String.format("Hello, %s! Welcome to Lex V2. I'm a Java Lambda", name)));
        }
        return LexUtil.generateResponse(request, LexUtil.closeAction(), IntentState.Fulfilled, Message.plainText("Howdy! Welcome to Lex V2. I'm a Java Lambda"));
    }
}
