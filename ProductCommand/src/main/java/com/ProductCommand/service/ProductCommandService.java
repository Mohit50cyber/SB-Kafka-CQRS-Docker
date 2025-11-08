package com.ProductCommand.service;

import com.ProductCommand.dto.ProductEvent;
import com.ProductCommand.entity.Product;
import com.ProductCommand.repo.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProductCommandService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String TOPIC = "product-event-topic";

    public Product createProduct(ProductEvent productEvent) {

        Product savedProduct = productRepo.save(productEvent.getProduct());
        ProductEvent event = new ProductEvent("CREATE_PRODUCT", savedProduct);

        sendEvent(event);

        log.info("✅ Product created in DB: {}", savedProduct.getId());
        return savedProduct;
    }

    public Product updateProduct(long id, ProductEvent productEvent) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Product req = productEvent.getProduct();
        existing.setName(req.getName());
        existing.setDescription(req.getDescription());
        existing.setPrice(req.getPrice());

        Product updated = productRepo.save(existing);
        ProductEvent event = new ProductEvent("UPDATE_PRODUCT", updated);

        sendEvent(event);

        log.info("♻️ Product updated: {}", updated.getId());
        return updated;
    }

    public String deleteProduct(long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        productRepo.deleteById(id);

        ProductEvent event = new ProductEvent("DELETE_PRODUCT", product);
        sendEvent(event);

        log.info("❌ Product deleted: {}", id);
        return "Product deleted with id: " + id;
    }

    private void sendEvent(ProductEvent event) {
        log.info("📤 Sending Event → {}: {}", TOPIC, event.getEventType());

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Failed to send event to Kafka: {}", ex.getMessage(), ex);
                kafkaTemplate.send(TOPIC + ".DLT", event); // Send to Dead Letter Topic
            } else {
                log.info("✅ Event sent to Kafka topic {} | Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset()
                );
            }
        });
    }
}
