package net.lomibao.model.lexv2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bot {
    private String id;
    private String name;
    private String aliasId;
    private String aliasName;
    private String localeId;
    private String version;
}
//{
//        "id": "string",
//
//        "name": "string",
//        "aliasId": "string",
//        "localeId": "string",
//        "version": "string"
//    }
