package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageResponseCard {
    private String title;
    private String subtitle;
    private String imageUrl;
    private List<Button> buttons;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Button {
        private String text;
        private String value;
    }
}
