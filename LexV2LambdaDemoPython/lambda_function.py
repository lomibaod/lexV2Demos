import json
import random
import decimal
import os
import logging
import traceback

debug=True


# on error, return nice message to bot
def fail(intent_request,error):
    #don't share the full eerror in production code, it's not good to give full traceback data to users
    error = error if debug else ''
    intent_name = intent_request['sessionState']['intent']['name']
    message = {
                'contentType': 'PlainText',
                'content': f"Oops... I guess I ran into an error I wasn't expecting... Sorry about that. My dev should probably look in the logs.\n {error}"
                }
    fulfillment_state = "Fulfilled"
    return close(intent_request, get_session_attributes(intent_request), fulfillment_state, message) 
       
#mock data query against inventory.json instead of a database or using an api call
def query_data(make,vehicle_type):
    inventory_path = os.environ['LAMBDA_TASK_ROOT'] + "/inventory.json"
    content=open(inventory_path).read()
    inventory_json=json.loads(content)
    filtered= [v for v in inventory_json if make==v['make'] and vehicle_type==v['type']]
    return filtered

'''''
=== UTIL METHODS ===========================
'''''

#util method to get the slots fromt he request
def get_slots(intent_request):
    return intent_request['sessionState']['intent']['slots']

#util method to get a slot's value
def get_slot(intent_request, slotName):
    slots = get_slots(intent_request)
    if slots is not None and slotName in slots and slots[slotName] is not None and 'interpretedValue' in slots[slotName]['value']:
        return slots[slotName]['value']['interpretedValue']
    else:
        return None
#gets a map of the session attributes
def get_session_attributes(intent_request):
    sessionState = intent_request['sessionState']
    if 'sessionAttributes' in sessionState:
        return sessionState['sessionAttributes']

    return {}
# builds response to tell the bot you want to trigger another intent (use to switch the context)
def elicit_intent(intent_request, session_attributes, message):
    return {
        'sessionState': {
            'dialogAction': {
                'type': 'ElicitIntent'
            },
            'sessionAttributes': session_attributes
        },
        'messages': [ message ] if message != None else None,
        'requestAttributes': intent_request['requestAttributes'] if 'requestAttributes' in intent_request else None
    }
# builds response to tell the bot you need to get the value of a particular slot
def elicit_slot(intent_request, session_attributes,slot_to_elicit, message):
    intent_request['sessionState']['intent']['state'] = 'InProgress'
    return {
        'sessionState': {
            'sessionAttributes': session_attributes,
            'dialogAction': {
                'type': 'ElicitSlot',
                'slotToElicit': slot_to_elicit
            },
            'intent': intent_request['sessionState']['intent']
        },
        'messages': [message],
        'sessionId': intent_request['sessionId'],
        'requestAttributes': intent_request['requestAttributes'] if 'requestAttributes' in intent_request else None
    }
# builds response to end the dialog
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



'''
==== intent handlers =====
'''

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

#handle the BookTestDrive intent.
#this shows an example of using session_attributes to save information over multiple interactions
def process_book_test_drive(intent_request):
    session_attributes = get_session_attributes(intent_request)
    slots = get_slots(intent_request)
    
    # check if we already set the session data
    if 'selectedVehicle' not in session_attributes:
        make=get_slot(intent_request,'make')
        vehicle_type=get_slot(intent_request,'vehicleType')
        if make and vehicle_type: #if the make and vehicle type is set in the slots query the data
            results = query_data(make,vehicle_type)
            if len(results)>0:
                v=results[0] #pick the first vehicle match
                # set the display value of the selected vehicle
                session_attributes['selectedVehicle']=f"{v['year']} {v['make']} {v['model']}"
            else: #no match, no car to test drive
                message= {'contentType': 'PlainText','content': f"Sorry, we don't have any {make} {vehicle_type}s on the dealership lot right now"}
                fulfillment_state = "Fulfilled"
                return close(intent_request, session_attributes, fulfillment_state, message)
    
    date=get_slot(intent_request,'date')
    time=get_slot(intent_request,'time')
    if date and time and 'selectedVehicle' in session_attributes:
        vehicle_str = session_attributes['selectedVehicle']
        # all data available fulfill
        message= {'contentType': 'PlainText','content': f"Your test drive for the {vehicle_str} is scheduled for {date} at {time}"}
        fulfillment_state = "Fulfilled"
        return close(intent_request, session_attributes, fulfillment_state, message)
    elif 'selectedVehicle' in session_attributes:
        vehicle=session_attributes['selectedVehicle']
        # still need data delegate to the bot
        if date is None:
            message= {'contentType': 'PlainText','content': f'What day do you want to come in and drive a {vehicle}?'}
            return elicit_slot(intent_request,session_attributes,'date',message)
        elif time is None:
            message= {'contentType': 'PlainText','content': f'What time do you want to come and test drive a {vehicle}?'}
            return elicit_slot(intent_request,session_attributes,'time',message)
        else:
            message= {'contentType': 'PlainText','content': "It shouldn't be possible to reach here... I guess I have a bug"}
            fulfillment_state = "Fulfilled"
            return close(intent_request, session_attributes, fulfillment_state, message)
    else:
        if make is None:
            message= {'contentType': 'PlainText','content': 'What automaker are you interested in?'}
            return elicit_slot(intent_request,session_attributes,'make',message)
        elif vehicle_type is None:
            message= {'contentType': 'PlainText','content': 'Do you want to drive a car, a truck, or an suv?'}
            return elicit_slot(intent_request,session_attributes,'vehicleType',message)
        else:
            message= {'contentType': 'PlainText','content': "It shouldn't be possible to reach here... I guess I have a bug"}
            fulfillment_state = "Fulfilled"
            return close(intent_request, session_attributes, fulfillment_state, message)
            
            
    
# handles the hello intent
def process_hello(intent_request):
    session_attributes = get_session_attributes(intent_request)
    slots = get_slots(intent_request)
    message = {
        'contentType': 'PlainText',
        'content': 'hello from the lambda'
    }
    fulfillment_state = "Fulfilled"
    return close(intent_request, session_attributes, fulfillment_state, message)

#handler for when there is no matching intent handler
def default_response(intent_request):
    session_attributes = get_session_attributes(intent_request)
    intent_name = intent_request['sessionState']['intent']['name']
    message = {
        'contentType': 'PlainText',
        'content': f"This lambda doesn't know how to process intent_name={intent_name}"
    }
    fulfillment_state = "Fulfilled"
    return close(intent_request, session_attributes, fulfillment_state, message)
    

#looks at the intent_name and routes to the handler method    
def dispatch(intent_request):
    try:
        intent_name = intent_request['sessionState']['intent']['name']
        response = None
        # Dispatch to your bot's intent handlers
        if intent_name == 'Hello':
            return process_hello(intent_request)
        elif intent_name == 'VehicleInventorySearch':
            return process_find_car(intent_request)
        elif intent_name == 'BookTestDrive':
            return process_book_test_drive(intent_request)
        else:
            return default_response(intent_request)
    except Exception as ex:
        error = traceback.format_exc()
        print(error)
        return fail(intent_request,error)


    
#entry point of lambda
def lambda_handler(event, context):
    print(json.dumps(event))
    response = dispatch(event)
    return response

