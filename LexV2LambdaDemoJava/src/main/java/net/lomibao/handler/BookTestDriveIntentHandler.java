package net.lomibao.handler;

import io.micronaut.context.annotation.Bean;
import net.lomibao.model.Vehicle;
import net.lomibao.model.lexv2.IntentState;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.service.InventoryService;
import net.lomibao.util.LexUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Bean// equiv to spring @Component
public class BookTestDriveIntentHandler extends IntentHandler {

    private final InventoryService inventoryService;

    //micronaut automatically will @Autowired
    public BookTestDriveIntentHandler(InventoryService inventoryService) {
        super("BookTestDrive");
        this.inventoryService = inventoryService;
    }

    @Override
    public LexV2LambdaResponse handle(LexV2LambdaRequest request) {
        String make = LexUtil.getSlot(request, "make");
        String vehicleType = LexUtil.getSlot(request, "vehicleType");
        String date = LexUtil.getSlot(request, "date");
        String time = LexUtil.getSlot(request, "time");

        Map<String, String> session = LexUtil.getSessionAttributes(request);

        if (!session.containsKey("selectedVehicle")) {
            if (make != null && vehicleType != null) {//make and vehicle type slots already set
                Optional<Vehicle> vehicle = inventoryService.queryByMakeAndType(make, vehicleType).stream().findFirst();
                if (vehicle.isPresent()) {
                    Vehicle v = vehicle.get();
                    //saves a value to the session which can be retrieved in later interactions, lives longer than just the request itself
                    session.put("selectedVehicle", String.format("%s %s %s", v.getYear(), v.getMake(), v.getModel()));
                } else {//query was made and no matches in inventory
                    return LexUtil.generateResponse(request, LexUtil.closeAction(), IntentState.Fulfilled, Message.plainText(String.format("Sorry, we don't have any %s %ss on the dealership lot right now", make, vehicleType)));
                }
            }
        }


        if (session.containsKey("selectedVehicle")) {
            String vehicle = session.get("selectedVehicle");
            if (date != null && time != null) {
                //all slots filled book it
                Message message = Message.plainText("Your test drive for the %s is scheduled for %s at %s", vehicle, date, time);
                LexUtil.setSessionAttributes(request, Collections.emptyMap());//clear session state
                return LexUtil.generateResponse(request, LexUtil.closeAction(), IntentState.Fulfilled, message);
            } else if (date == null) {
                return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("date"), IntentState.InProgress,
                        Message.plainText("What day do you want to come in and drive a %s?", vehicle));
            } else if (time == null) {
                return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("time"), IntentState.InProgress,
                        Message.plainText("What time do you want to come in and try driving a %s?", vehicle));
            }
        }

        LexV2LambdaResponse response = elicitMissingSlots(request, make, vehicleType, date, time);
        if (response != null) {
            return response;
        }

        return LexUtil.generateResponse(request, LexUtil.closeAction(), IntentState.Failed,
                Message.plainText("Hmm... It shouldn't be possible to get here... I think i need to go through the debugger"));


    }

    private LexV2LambdaResponse elicitMissingSlots(LexV2LambdaRequest request, String make, String vehicleType, String date, String time) {
        if (date == null) {
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("date"), IntentState.InProgress,
                    Message.plainText("What day do you want to come for your test drive?"));
        } else if (time == null) {
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("time"), IntentState.InProgress,
                    Message.plainText("What time do you want to come to the dealership?"));
        } else if (make == null) {
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("make"), IntentState.InProgress,
                    Message.plainText("What automaker are you interested in?"));
        } else if (vehicleType == null) {
            return LexUtil.generateResponse(request, LexUtil.elicitSlotAction("vehicleType"), IntentState.InProgress,
                    Message.plainText("Do you want to drive a car,truck or suv?"));
        }
        return null;
    }
}
