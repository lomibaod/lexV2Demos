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
public class SessionState {
    private List<Context> activeContexts;
    private Map<String,String> sessionAttributes;
    private DialogAction dialogAction;
    private Intent intent;
    private String originatingRequestId;
}
