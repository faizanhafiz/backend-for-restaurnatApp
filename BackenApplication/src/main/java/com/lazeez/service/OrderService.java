package com.lazeez.service;


import com.lazeez.dto.OrderRequest;
import com.lazeez.entity.Order;
import com.lazeez.entity.Product;
import com.lazeez.repository.OrderRepository;
import com.lazeez.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    Logger logger = LoggerFactory.getLogger(OrderService.class);
    public ResponseEntity<?> getOrderBYUserId(String userId) {

        try{

            List<Order> order = orderRepository.findByUserId(userId);
            if(order!=null)
            {
                return  ResponseEntity.ok(order);
            }else{


                return new ResponseEntity<>("Order not Found",HttpStatus.BAD_REQUEST);
            }


        }catch (IllegalArgumentException ex)
        {

            logger.info("Order Id is null passed ===> ",ex);
            return new ResponseEntity<>("OrderId must not be null", HttpStatus.BAD_REQUEST);


        }

        catch (Exception ex)
        {

            logger.info("error occured inside  getOrderBYUserId ===> ",ex);
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);


        }

    }

    public ResponseEntity<?> placeOrder(OrderRequest orderRequest) {

        try{
            List<Product> products = new ArrayList<>();
            Order  order = new Order();
            order.setStatus(orderRequest.getStatus());
            order.setUserId(orderRequest.getUserId());

            List<String> productIds = orderRequest.getProductIds();
            for(String prodId : productIds){

                Product product = productRepository.findById(prodId).get();
                if(product!=null)
                {
                    products.add(product);
                }
            }
            order.setProduct(products);
           Order placedOrder = orderRepository.save(order);
           if(placedOrder!=null)
           {
               return  ResponseEntity.ok("order placed");
           }else{
               return  new ResponseEntity<>("Order not Placed",HttpStatus.BAD_REQUEST);
           }

        }catch (Exception ex)
        {
            logger.info("error occured inside  placeOrder ===> ",ex);
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> deleteOrder(String orderId) {

        try
        {
            orderRepository.deleteById(orderId);
            return ResponseEntity.ok("Order Deleted");

        }catch (Exception ex)
        {
            logger.info("error occured inside  deleteOrder ===> ",ex);
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }

    public ResponseEntity<?> getAllOrders() {

        try{


            return ResponseEntity.ok(orderRepository.findAll());

        }catch (Exception ex)
        {
            logger.info("error occured inside  getAllOrders  ===> ",ex);
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }
}
