package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderFormData {
    private String name;
    private String phoneNumber;
    private String city;
}