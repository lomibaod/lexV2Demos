package net.lomibao.model.lexv2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Slot {
    private SlotValue value;
    private String shape;
    private List<SlotValue> values;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotValue {
        private String interpretedValue;
        private String originalValue;
        private List<String> resolvedValues;
    }
}
