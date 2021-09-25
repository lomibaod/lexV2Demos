package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Intent {
    private ConfirmationState confirmationState;
    private String name;
    private Map<String,Slot> slots;
    private IntentState state;

}
