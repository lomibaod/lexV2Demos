package net.lomibao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    private String make;
    private String color;
    private String type;
    private String model;
    private Integer year;
    private String price;
    private Integer inventory;
}
