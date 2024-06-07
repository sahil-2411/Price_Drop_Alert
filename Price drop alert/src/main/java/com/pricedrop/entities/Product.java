package com.pricedrop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int p_id;
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 1000, message = "Product name must be between 2 and 100 characters")
    private String p_name;

    @NotBlank(message = "Product URL is required")
    @Size(min = 10, max = 200, message = "Product URL must be between 10 and 200 characters")
    private String p_url;

    @DecimalMin(value = "100", message = "Price must be greater than 100")
    @DecimalMax(value = "999999.99", message = "Total price must not exceed 999999.99")
    private double t_price;

    @ManyToOne
    @JsonIgnore
    private User user;
}
