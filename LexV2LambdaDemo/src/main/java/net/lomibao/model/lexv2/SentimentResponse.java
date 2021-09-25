package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentimentResponse {
    private String sentiment;
    private SentimentScore sentimentScore;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SentimentScore {
        private String mixed;
        private String negative;
        private String neutral;
        private String positive;
    }
}
