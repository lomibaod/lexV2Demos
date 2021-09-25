package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private ContentType contentType;
    private String content;
    private ImageResponseCard imageResponseCard;

    public static Message plainText(String messageString){
        return Message.builder()
                .content(messageString)
                .contentType(ContentType.PlainText).build();
    }
    public static Message plainText(String messageString,Object... params){
        return Message.builder()
                .content(String.format(messageString,params))
                .contentType(ContentType.PlainText).build();
    }
}
