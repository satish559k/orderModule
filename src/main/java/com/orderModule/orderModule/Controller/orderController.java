package com.orderModule.orderModule.Controller;

import com.orderModule.orderModule.Service.OrderService;
import com.orderModule.orderModule.dto.OrderRequest;
import com.orderModule.orderModule.dto.RestResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("order")

@AllArgsConstructor
public class orderController {


    private final OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<RestResponse> order(@RequestBody OrderRequest orderRequest) {
        try{
            boolean isExist =  orderService.CheckOrderedProductExist(orderRequest.getProductId(),orderRequest.getQuantity());
            if (isExist) {
                orderService.CreateOrder(orderRequest);
                return ResponseEntity.ok(new RestResponse(HttpStatus.CREATED.value(),"Order placed"));
            }else{
                return ResponseEntity.ok(new RestResponse(HttpStatus.NOT_FOUND.value(),"Out of stock product"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error"));
        }
    }
}
