package net.lomibao.handler;

import net.lomibao.model.lexv2.IntentState;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.util.LexUtil;

import java.util.HashMap;
import java.util.Map;

public class MakePizzaHandler extends IntentHandler {
    public MakePizzaHandler() {
        super("MakePizza");
    }

    @Override
    public LexV2LambdaResponse handle(LexV2LambdaRequest request) {
        Map<String, String> session = LexUtil.getSessionAttributes(request);
        String topping = LexUtil.getSlot(request, "topping");
        String crust = LexUtil.getSlot(request, "crust");
        String waitStop = LexUtil.getSlot(request, "waitStop");

        if (topping != null && crust != null) {
            if (session.containsKey("cookingStatus")) {
                //start cooking pizza
                session.put("cookingStatus", "started");
                //enhancement idea, make different crusts take a different number of turns
                session.put("roundsLeft", String.valueOf(2));//session attributes are always strings
                return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("waitStop"),
                        IntentState.InProgress,
                        Message.plainText("your %s %s pizza is getting started and should take 2 turns. " +
                                "continue or stop?", topping, crust));
            }
            //cooking started
            int roundsLeft = Integer.valueOf(session.getOrDefault("roundsLeft", "0"));
            if (waitStop.equalsIgnoreCase("continue")) {
                roundsLeft--;
                session.put("roundsLeft", String.valueOf(roundsLeft));
                return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("waitStop"),
                        IntentState.InProgress,
                        Message.plainText("you have %d turns till your pizza is done." +
                                "continue or stop?", topping, crust));
            } else {//stop
                String messageText = String.format("your %s %s pizza is ", topping, crust);
                switch (roundsLeft) {
                    case 2:
                        messageText += "uncooked and you get food poisoning :(";
                        break;
                    case 1:
                        messageText += "under cooked but luckily you only get a mild stomach ache";
                        break;
                    case 0:
                        messageText += "cooked to perfection. It tastes delicious!";
                        break;
                    default:
                        messageText += "burnt and the pizza shop is on fire too!";
                }
                LexUtil.setSessionAttributes(request, new HashMap<>());//clear session since game is over
                return LexUtil.generateResponse(request, LexUtil.closeAction(),
                        IntentState.Fulfilled, Message.plainText(messageText));
            }
        }

        if (topping == null) {
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("topping"),
                    IntentState.InProgress,
                    Message.plainText("what topping do you want on your pizza"));
        }else{//crust ==null
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("crust"),
                    IntentState.InProgress,
                    Message.plainText("what kind of crust do you want? thin, pan, or deep dish?"));
        }


    }


}

