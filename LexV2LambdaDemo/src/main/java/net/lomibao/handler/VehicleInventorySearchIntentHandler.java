package net.lomibao.handler;

import io.micronaut.context.annotation.Bean;
import net.lomibao.model.Vehicle;
import net.lomibao.model.lexv2.IntentState;
import net.lomibao.model.lexv2.LexV2LambdaRequest;
import net.lomibao.model.lexv2.LexV2LambdaResponse;
import net.lomibao.model.lexv2.Message;
import net.lomibao.service.InventoryService;
import net.lomibao.util.LexUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Bean //equiv to spring @Component
public class VehicleInventorySearchIntentHandler extends IntentHandler {

    private final InventoryService inventoryService;

    // micronaut automatically @Autowired
    public VehicleInventorySearchIntentHandler(InventoryService inventoryService) {
        super("VehicleInventorySearch");
        this.inventoryService = inventoryService;
    }

    @Override
    public LexV2LambdaResponse handle(LexV2LambdaRequest request) {
        String make=LexUtil.getSlot(request,"make");
        String vehicleType=LexUtil.getSlot(request,"vehicleType");
        String color=LexUtil.getSlot(request,"color");

        List<Vehicle> vehiclesFound=inventoryService.queryByMakeAndType(make,vehicleType);

        String text="";
        if(vehiclesFound==null || vehiclesFound.isEmpty()){
            text=String.format("no %s %ss are available in our inventory",make,vehicleType);
        }else if(vehiclesFound.size()==1){
            Vehicle vehicle=vehiclesFound.get(0);
            text=String.format("we have %d %s %s %s %s in stock. The current price is %s",
                    vehicle.getInventory(),vehicle.getColor(),vehicle.getYear(),vehicle.getMake(),vehicle.getModel(),vehicle.getPrice());
        }else{//multiple found, filter by color as well to see if we can get a better match
            Vehicle vehicle=vehiclesFound.stream().filter(v->v.getColor().equalsIgnoreCase(color)).findFirst().orElse(vehiclesFound.get(0));//if no color match, just use first
            text=String.format("we have %d %s %s %s %s in stock. The current price is  %s",
                    vehicle.getInventory(),vehicle.getColor(),vehicle.getYear(),vehicle.getMake(),vehicle.getModel(),vehicle.getPrice());
        }

        return LexUtil.generateResponse(request,LexUtil.closeAction(), IntentState.Fulfilled,  Message.plainText(text));
    }
}
/*

# process the VehicleInventorySearch intent
def process_find_car(intent_request):
    session_attributes = get_session_attributes(intent_request)
    slots = get_slots(intent_request)

    #get slot values
    make=get_slot(intent_request,'make')
    vehicle_type=get_slot(intent_request,'vehicleType')
    color=slots['color']['value']['interpretedValue'] if slots['color'] else ''

    #look up data
    results=query_data(make,vehicle_type)

    # process resulsts
    if(len(results)==1):
        print(results)
        found=results[0]
        text = f" we have {found['inventory']} {found['color']} {found['year']} {found['make']} {found['model']} in stock. It would cost {found['price']}"
    elif(len(results)>1): #multiple results, check if we can match color as well
        color_match=[v for v in results if v['color'].lower()==color.lower()]
        found=color_match[0] if len(color_match)>0 else results[0]
        text = f" we have {found['inventory']} {found['color']} {found['year']} {found['make']} {found['model']} in stock. It would cost {found['price']}"
    else: #nothing found
        text= f"no {make} {vehicle_type}s are currently available in our inventory"

    message =  {
                'contentType': 'PlainText',
                'content': text
                }

    fulfillment_state = "Fulfilled"
    return close(intent_request, session_attributes, fulfillment_state, message)

 */