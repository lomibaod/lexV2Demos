package net.lomibao.model.lexv2;

/**
 * ConfirmIntent - The next action is asking the user if the intent is complete and ready to be fulfilled. This is a yes/no question such as "Place the order?"
 * Close - Indicates that the there will not be a response from the user. For example, the statement "Your order has been placed" does not require a response.
 * Delegate - The next action is determined by Amazon Lex.
 * ElicitIntent - The next action is to determine the intent that the user wants to fulfill.
 * ElicitSlot - The next action is to elicit a slot value from the user.
 */
public enum DialogActionType {
    Close,ConfirmIntent,Delegate,ElicitIntent,ElicitSlot;
}
