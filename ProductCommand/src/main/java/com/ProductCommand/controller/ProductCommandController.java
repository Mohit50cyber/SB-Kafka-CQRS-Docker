package com.ProductCommand.controller;

import com.ProductCommand.dto.ProductEvent;
import com.ProductCommand.entity.Product;
import com.ProductCommand.service.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productsCommand")
public class ProductCommandController {

    @Autowired
    private ProductCommandService productCommandService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductEvent productEvent){
        Product savedProduct = productCommandService.createProduct(productEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductEvent productEvent){
        Product updateProduct = productCommandService.updateProduct(id, productEvent);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        String message = productCommandService.deleteProduct(id);
        return ResponseEntity.ok(message);
    }
}
