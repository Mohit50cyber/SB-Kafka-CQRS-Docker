package com.ProductQuery.service;

import com.ProductQuery.entity.Product;
import com.ProductQuery.exceptions.ResourceNotFoundException;
import com.ProductQuery.repo.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductQueryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductQueryService.class);


    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @RetryableTopic(attempts = "4")
    @KafkaListener(
            topics = "product-event-topic",
            groupId = "product-event-group"
    )
    public void processProductEvent(@Payload(required = false) Map<String, Object> productEvent) {

        if (productEvent == null) {
            log.warn("Received NULL tombstone event — ignoring...");
            return;
        }

        log.info(" Received event: {}", productEvent);

        try {
            String eventType = (String) productEvent.get("eventType");
            Map<String, Object> productMap = (Map<String, Object>) productEvent.get("product");

            Product product = new Product();
            product.setId(Long.valueOf(productMap.get("id").toString()));
            product.setName(productMap.get("name").toString());
            product.setDescription(productMap.get("description").toString());
            product.setPrice(Double.valueOf(productMap.get("price").toString()));

            if ("CREATE_PRODUCT".equalsIgnoreCase(eventType)) {
                if (productRepo.existsById(product.getId())) {
                    log.warn("Product already exists, skipping CREATE: {}", product.getId());
                    return;
                }
                productRepo.save(product);
                log.info("Product Created: {}", product.getId());
            }

            else if ("UPDATE_PRODUCT".equalsIgnoreCase(eventType)) {

                productRepo.findById(product.getId()).ifPresentOrElse(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());

                    try {
                        productRepo.save(existingProduct);
                        System.out.println(" Product Updated: " + existingProduct.getId());
                    } catch (Exception e) {
                        System.out.println(" Optimistic Lock — skipping stale update for product " + product.getId());
                    }

                }, () -> {
                    System.out.println(" Product not found on UPDATE, ignoring event: " + product.getId());
                });
            }

            else if ("DELETE_PRODUCT".equalsIgnoreCase(eventType)) {
                if (!productRepo.existsById(product.getId())) {
                    log.warn("Product not present for DELETE: {}", product.getId());
                    return;
                }
                productRepo.deleteById(product.getId());
                log.info(" Product Deleted: {}", product.getId());
            }

            else {
                log.error(" Unknown eventType: {}", eventType);
            }

        } catch (Exception ex) {
            log.error(" Error processing event, sending to DLT — Error: {}", ex.getMessage(), ex);
            kafkaTemplate.send("product-event-topic.DLT", productEvent);
        }
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @DltHandler
    public void listenDLT(Product product , @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        logger.info("DLT Received : {} , from {} ,offset {} ",product.getName(),topic );
    }

}
