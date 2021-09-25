package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LexV2LambdaRequest {
    private String messageVersion;
    private InvocationSource invocationSource;
    private InputMode inputMode;
    private String responseContentType;
    private String sessionId;
    private String inputTranscript;
    private Bot bot;
    private List<Interpretation> interpretations;
    private Map<String, String> requestAttributes;
    private SessionState sessionState;


}
/*

{
    "messageVersion": "1.0",
    "invocationSource": "DialogCodeHook | FulfillmentCodeHook",
    "inputMode": "DTMF | Speech | Text",
    "responseContentType": "CustomPayload | ImageResponseCard | PlainText | SSML",

    "sessionId": "string",
    "inputTranscript": "string",
    "bot": {
        "id": "string",

        "name": "string",
        "aliasId": "string",
        "localeId": "string",
        "version": "string"
    },
    "interpretations": [
        {
            "intent": {

                "confirmationState": "Confirmed | Denied | None",
                "name": "string",
                "slots": {
                    "string": {
                        "value": {
                            "interpretedValue": "string",
                            "originalValue": "string",
                            "resolvedValues": [
                                "string"
                            ]
                        }
                    },

                    "string": {
                        "shape": "List",
                        "value": {
                            "interpretedValue": "string",
                            "originalValue": "string",
                            "resolvedValues": [
                                "string"
                            ]
                        },
                        "values": [
                            {

                                "shape": "Scalar",
                                "value": {
                                    "originalValue": "string",
                                    "interpretedValue": "string",
                                    "resolvedValues": [
                                        "string"
                                    ]
                                }
                            },
                            {

                                "shape": "Scalar",
                                "value": {

                                    "originalValue": "string",
                                    "interpretedValue": "string",
                                    "resolvedValues": [
                                        "string"
                                    ]
                                }
                            }
                        ]
                    }
                },
                "state": "Failed | Fulfilled | InProgress | ReadyForFulfillment",
                "kendraResponse": {
                    // Only present when intent is KendraSearchIntent. For details, see
                    // https://docs.aws.amazon.com/kendra/latest/dg/API_Query.html#API_Query_ResponseSyntax
                }
            },
            "nluConfidence": {
                "score": number
            },
            "sentimentResponse": {

                "sentiment": "string",
                "sentimentScore": {
                    "mixed": number,
                    "negative": number,
                    "neutral": number,
                    "positive": number
                }
            }
        }
    ],
    "requestAttributes": {
        "string": "string"
    },
    "sessionState": {
        "activeContexts": [
            {
                "name": "string",
                "contextAttributes": {
                    "string": "string"
                },
                "timeToLive": {
                    "timeToLiveInSeconds": number,

                    "turnsToLive": number

                }
            }
        ],
        "sessionAttributes": {
            "string": "string"
        },
        "dialogAction": {
            "slotToElicit": "string",

            "type": "Close | ConfirmIntent | Delegate | ElicitIntent | ElicitSlot"
        },
        "intent": {
            "confirmationState": "Confirmed | Denied | None",
            "name": "string",
            "slots": {
                "string": {

                    "value": {
                        "interpretedValue": "string",
                        "originalValue": "string",
                        "resolvedValues": [
                            "string"
                        ]
                    }
                },
                "string": {
                    "shape": "List",
                    "value": {
                        "interpretedValue": "string",
                        "originalValue": "string",
                        "resolvedValues": [
                            "string"
                        ]
                    },
                    "values": [
                        {
                            "shape": "Scalar",
                            "value": {
                                "originalValue": "string",
                                "interpretedValue": "string",
                                "resolvedValues": [
                                    "string"
                                ]
                            }
                        },
                        {
                            "shape": "Scalar",
                            "value": {
                                "originalValue": "string",
                                "interpretedValue": "string",
                                "resolvedValues": [
                                    "string"
                                ]
                            }
                        }
                    ]
                }
            },
            "state": "Failed | Fulfilled | InProgress | ReadyForFulfillment",
            "kendraResponse": {
                // Only present when intent is KendraSearchIntent. For details, see

                // https://docs.aws.amazon.com/kendra/latest/dg/API_Query.html#API_Query_ResponseSyntax                     }
            },
            "originatingRequestId": "string"
        }
    }
}

 */
