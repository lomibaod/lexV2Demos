package net.lomibao.model.lexv2;

/**
 * The fulfillment state of the intent. The possible values are:
 * Failed - The Lambda function associated with the intent failed to fulfill the intent.
 * Fulfilled - The intent has fulfilled by the Lambda function associated with the intent.
 * ReadyForFulfillment - All of the information necessary for the intent is present and the intent ready to be fulfilled by the client application.
 */
public enum IntentState {
    Failed,Fulfilled,InProgress,ReadyForFulfillment;
}
