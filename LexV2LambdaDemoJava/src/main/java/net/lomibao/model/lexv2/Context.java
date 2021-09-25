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
public class Context {
    private String name;
    private Map<String,String> contextAttributes;
    private TimeToLive timeToLive;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class TimeToLive {
        private Integer timeToLiveInSeconds;
        private Integer turnsToLive;
    }
}
