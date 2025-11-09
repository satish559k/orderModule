package com.orderModule.orderModule.Service;

import com.orderModule.orderModule.Entity.Product;
import com.orderModule.orderModule.Repository.ProductRepo;
import com.orderModule.orderModule.dto.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

    private final ProductRepo productRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public boolean CheckOrderedProductExist(UUID productId,int quantityOrder){
        try{
            Product data = productRepo.findByproductId(productId);
            int quantity = data.getQuantity();
            log.info("quantity: "+quantity);
            if( quantity>0 && quantityOrder<=quantity){
//                data.setQuantity(data.getQuantity()-quantityOrder);
//                productRepo.save(data);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void CreateOrder(OrderRequest orderRequest){
        HashMap<String,Object> datakafka = new HashMap<>();
        datakafka.put("productId",orderRequest.getProductId());
        datakafka.put("userId",orderRequest.getUserId());
        datakafka.put("quantityOrder",orderRequest.getQuantity());
        log.info("data produced in kafka "+datakafka.toString());
        kafkaTemplate.send("Inventory",datakafka);
    }
}
