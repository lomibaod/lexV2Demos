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
public class LexV2LambdaResponse {
    private SessionState sessionState;
    private List<Message> messages;
    private Map<String,String> requestAttributes;
    private String sessionId;
}
