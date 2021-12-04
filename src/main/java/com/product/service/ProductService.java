package com.product.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    public String getProductName(){
        log.info("Getting Product info");
        return "Welcome Gift";
    }
}
