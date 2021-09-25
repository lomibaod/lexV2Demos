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

def create_message(messageText):
    return {
                'contentType': 'PlainText',
                'content': messageText
            }

'''
==== intent handlers =====
'''
            
    
# handles the hello intent
def process_pizza(intent_request):
    session_attributes = get_session_attributes(intent_request)
    #slots = get_slots(intent_request)
    topping = get_slot(intent_request,'topping') # cheese, pepperoni, mushroom
    crust = get_slot(intent_request,'crust') # thin crust, pan, deep dish
    wait_stop = get_slot(intent_request,'waitStop') # continue or stop

    if topping and crust:
        if 'cooking_status' not in session_attributes:
            #start cooking
            session_attributes['cooking_status']='started'
            session_attributes['rounds_left']=str(2)
            message = {
                'contentType': 'PlainText',
                'content': f'your {topping} {crust} pizza is getting started, and should take 2 turns. continue or stop?'
            }
            fulfillment_state = "InProgress"
            return elicit_slot(intent_request,session_attributes,'waitStop',message)
        #cooking started
        rounds_left=int(session_attributes['rounds_left'])
        if wait_stop == 'continue':
            #go on
            rounds_left=rounds_left-1
            session_attributes['rounds_left']=rounds_left
            fulfillment_state = "InProgress"
            return elicit_slot(intent_request,session_attributes,'waitStop',create_message(f"you have {rounds_left} turns till your pizza is done. continue or stop?"))
            
        else:
            message_text=f"your {topping} {crust} pizza is "
            if rounds_left==2:
                message_text= message_text+"uncooked and you get food poisoning :("
            elif rounds_left==1:
                message_text= message_text+"under cooked but luckily you only get a mild stomach ache"
            elif rounds_left==0:
                message_text= message_text+"cooked to perfection. It tastes delicious!"
            else:
                message_text = message_text+"burnt and the pizza shop is on fire too! "
            session_attributes={} # clear session
            return close(intent_request,session_attributes,'Fulfilled',create_message(message_text))
    elif topping is None:
        return elicit_slot(intent_request,session_attributes,'topping',create_message("what topping do you want on your pizza?"))
    elif crust is None:
        return elicit_slot(intent_request,session_attributes,'crust',create_message("what kind of crust do you want on your pizza?"))



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
        if intent_name == 'MakePizza':
            return process_pizza(intent_request)
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

