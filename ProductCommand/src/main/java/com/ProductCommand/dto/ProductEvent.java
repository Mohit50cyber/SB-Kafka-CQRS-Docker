package com.ProductCommand.dto;

import com.ProductCommand.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {

    @NotNull
    private String eventType;

    @NotNull
    private Product product;
}
