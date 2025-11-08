package com.ProductQuery.controller;

import com.ProductQuery.entity.Product;
import com.ProductQuery.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productsQuery")
public class ProductQueryController {

    @Autowired
    private ProductQueryService productQueryService;

    @GetMapping
    public ResponseEntity<List<Product>> fetchAllProducts() {
        List<Product> allProducts = productQueryService.getAllProduct();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }



}
