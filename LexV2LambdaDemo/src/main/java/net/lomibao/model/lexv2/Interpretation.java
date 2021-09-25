package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interpretation {
    private Intent intent;
    private NLUConfidence nluConfidence;
    private SentimentResponse sentimentResponse;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NLUConfidence {
        private Double score;
    }
}
