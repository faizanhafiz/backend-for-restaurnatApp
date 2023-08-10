package com.lazeez.controller;


import com.lazeez.dto.OrderRequest;
import com.lazeez.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/getOrderByUserId/{userId}")
    public ResponseEntity<?>  getOrderBYUserId(@PathVariable String userId)
    {
        return  orderService.getOrderBYUserId(userId);

    }



    @PostMapping("/placeOrder")
    public  ResponseEntity<?>   placeOrder(@RequestBody OrderRequest orderRequest)
    {
        return orderService.placeOrder(orderRequest);

    }


    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId)
    {

        return orderService.deleteOrder(orderId);

    }


    @GetMapping("/getAllOrder")
    public ResponseEntity<?> getAllOrders()
    {
        return orderService.getAllOrders();
    }



}
