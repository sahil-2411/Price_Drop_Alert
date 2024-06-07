package com.pricedrop.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiProduct {
    private int product_id;
    private String product_url;
    private String product_name;
    private String product_image;
    private String product_price;
}
