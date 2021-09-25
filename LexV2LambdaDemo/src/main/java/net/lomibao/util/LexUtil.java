package net.lomibao.util;

import lombok.NonNull;
import net.lomibao.model.lexv2.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LexUtil {


    public static DialogAction closeAction(){
        return DialogAction.builder()
                .type(DialogActionType.Close)
                .build();
    }
    public static DialogAction elicitSlotAction(String slotToElicit){
        return DialogAction.builder()
                .type(DialogActionType.ElicitSlot)
                .slotToElicit(slotToElicit)
                .build();
    }
    public static DialogAction delegateAction(){
        return new DialogAction(null,DialogActionType.Delegate);
    }

    public static LexV2LambdaResponse generateResponse(LexV2LambdaRequest request, DialogAction dialogAction,IntentState fulfillmentState, Message... messages){
        Optional.ofNullable(request.getSessionState()).map(SessionState::getIntent).ifPresent(intent->intent.setState(fulfillmentState));
        return LexV2LambdaResponse.builder()
                .sessionState(SessionState.builder()
                        .sessionAttributes(Optional.ofNullable(request.getSessionState()).map(SessionState::getSessionAttributes).orElse(Collections.emptyMap()))
                        .dialogAction(dialogAction)
                        .intent(Optional.ofNullable(request.getSessionState()).map(SessionState::getIntent).orElse(null))
                        .build())
                .messages(List.of(messages))
                .sessionId(request.getSessionId())
                .build();
    }

    /**
     * lets you search all intents for a possible match based on confidence score
     * @param request
     * @param intentName
     * @param minConfidence
     * @return
     */
    public static boolean matchesIntent(LexV2LambdaRequest request,String intentName,double minConfidence){
        return Optional.ofNullable(request.getInterpretations()).orElse(Collections.emptyList()).stream()
                .filter(i->Double.compare(
                        Optional.ofNullable(i)
                                .map(Interpretation::getNluConfidence)
                                .map(Interpretation.NLUConfidence::getScore).orElse(1.0),minConfidence)>=0)
                .map(Interpretation::getIntent).map(Intent::getName).anyMatch(x->x.equals(intentName));
    }

    /**
     * get the top intent match and compares it to intentName
     * @param request
     * @param intentName
     * @return
     */
    public static boolean matchesIntent(LexV2LambdaRequest request,String intentName){
        return Optional.ofNullable(request.getInterpretations()).orElse(Collections.emptyList())
                .stream().map(Interpretation::getIntent).findFirst().map(Intent::getName).map(x->x.equals(intentName)).orElse(false);
    }
    public static String getIntentName(LexV2LambdaRequest request){
        return Optional.ofNullable(request.getInterpretations())
                .orElse(Collections.emptyList()).stream().map(Interpretation::getIntent).map(Intent::getName).findFirst().orElse(null);
    }

    /**
     *  util method to get the slots from the request
     * @param request
     * @return the map of slots where key is the slot name
     */
    public static Map<String,Slot> getSlots(@NonNull LexV2LambdaRequest request){
        return Optional.ofNullable(request.getSessionState()).map(SessionState::getIntent).map(Intent::getSlots).orElse(Collections.emptyMap());
    }

    /**
     * util method to get a slot's value
     * @param request
     * @param slotName
     * @return the value of the slot, null if not found or resolved
     */
    public static String getSlot(@NonNull LexV2LambdaRequest request,String slotName){
        Map<String,Slot> slots=getSlots(request);
        if(!slots.containsKey(slotName)){return null;}
        return Optional.ofNullable(slots.get(slotName)).map(Slot::getValue).map(Slot.SlotValue::getInterpretedValue).orElse(null);
    }

    /**
     * gets the session attributes
     * @param request
     * @return
     */
    public static Map<String,String> getSessionAttributes(@NonNull LexV2LambdaRequest request){
        Map<String,String> session= Optional.ofNullable(request.getSessionState()).map(SessionState::getSessionAttributes).orElse(null);
        if(session==null){//if no session attributes create, set on request and return
            return setSessionAttributes(request,new HashMap<>());
        }
        return session;
    }
    public static Map<String,String> setSessionAttributes(@NonNull LexV2LambdaRequest request,Map<String,String> attributes){
        Optional.ofNullable(request.getSessionState()).ifPresent(s->s.setSessionAttributes(attributes));
        return attributes;
    }


}
