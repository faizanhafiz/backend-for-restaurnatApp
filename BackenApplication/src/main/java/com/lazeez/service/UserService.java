package com.lazeez.service;



import com.lazeez.dto.Cart;
import com.lazeez.entity.Product;
import com.lazeez.entity.User;
import com.lazeez.repository.ProductRepository;
import com.lazeez.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    Logger logger = LoggerFactory.getLogger(UserService.class);
    public   ResponseEntity<?> updateUser(String userId , User user) {
        try{


            user.setId(userId);

            userRepository.save(user);

            return ResponseEntity.ok("User Update");

        }catch (IllegalArgumentException ex){

                logger.info("error occured inside updateUser",ex);

                 return new ResponseEntity<>("user Id must not be null", HttpStatus.BAD_REQUEST);
        }catch (Exception ex){

            logger.info("error occured inside updateUser",ex);

            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public  ResponseEntity<?> deleteUser(String userId) {

        try
        {

            User user  = userRepository.findById(userId).get();

            if(user!=null)
            {

                userRepository.deleteById(userId);
                return  ResponseEntity.ok("User deleted");

            }else{

                return new ResponseEntity<>("User not Found",HttpStatus.BAD_REQUEST);

            }




        }catch (IllegalArgumentException ex){

            logger.info("error occured inside updateUser",ex);

            return new ResponseEntity<>("user Id must not be null", HttpStatus.BAD_REQUEST);
        }

        catch (Exception ex)
        {
            logger.info("error occured inside deleteUser",ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public   ResponseEntity<?> getUser(String userId) {

        try
        {

            User user  = userRepository.findById(userId).get();
            if(user!=null)
            {
                return ResponseEntity.ok(user);

            }else {
                return  new ResponseEntity<>("User not Found",HttpStatus.BAD_REQUEST);
            }

        }catch (IllegalArgumentException ex){

            logger.info("error occured inside getUser",ex);

            return new ResponseEntity<>("user Id must not be null", HttpStatus.BAD_REQUEST);
        }

        catch (Exception ex){
            logger.info("error occured inside getUser",ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public  ResponseEntity<?> getAllUser() {

        try
        {
            List<User> users  = userRepository.findAll();
            return ResponseEntity.ok(users);

        }catch (Exception ex)
        {
            logger.info("error occured inside getAllUser",ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> addUser(User user) {

        try{

            User userExist = userRepository.findByEmail(user.getEmail());
            User userExist2 = userRepository.findByMobile(user.getMobile());
            if((userExist !=null) || (userExist2 !=null))
            {

                return new ResponseEntity<>("User Already present",HttpStatus.BAD_REQUEST);


            }else{

               User addedUser = userRepository.save(user);
               if(addedUser!=null)
               {
                   return  ResponseEntity.ok("User Added");
               }else{
                   return  new ResponseEntity<>("something went wrong",HttpStatus.BAD_REQUEST);
                }
            }

        }catch (IllegalArgumentException ex){

            logger.info("error occured inside addUser",ex);

            return new ResponseEntity<>("user  must not be null", HttpStatus.BAD_REQUEST);
        }

        catch (Exception ex)
        {
            logger.info("error occured inside addUser",ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> addToCart(String productId ,String userId) {
        try
        {

            User user = userRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            if(product==null)
            {
                return new ResponseEntity<>("Product not Found",HttpStatus.BAD_REQUEST);

            }

            if(user!=null)
            {
                List<Cart>  cartList = user.getCart();
                if(cartList!=null)
                {

                    for (Cart cart : cartList)
                    {
                        System.out.println("Inside==============");
                        System.out.println(cart.getId());
                        System.out.print(product.getId());
                        if (cart.getId().equals(productId)) {

                            System.out.println("Inside===== 2=========");


                            cart.setQuantity(cart.getQuantity() + 1);



                            logger.info(("Cart quantity is increased"));


                            userRepository.save(user);

                            return ResponseEntity.ok(cart.getProductName() + "is Added to Cart");

                        }


                    }
                    cartList.add( Cart.builder().id(product.getId())
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .imageUrl(product.getImageUrl())
                            .quantity(1).build());
                    userRepository.save(user);
                    return ResponseEntity.ok(product.getProductName() + "is Added to Cart");

                }
                Cart cart = Cart.builder().id(product.getId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .quantity(1).build();
                List<Cart> finalCart = new ArrayList<>();
                finalCart.add(cart);
                user.setCart(finalCart);

                userRepository.save(user);
                return ResponseEntity.ok(product.getProductName()+"is Added to Cart");



            }else{
                logger.info("User not Found");
                return new ResponseEntity<>("User nNot Found", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            logger.info("error inside addtoCart",ex);
            return new ResponseEntity<>("Something went wrong ",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<?> getCart(String userId) {
        try{


           User user = userRepository.findById(userId).get();
           List<Cart> cart = user.getCart();

            return ResponseEntity.ok(cart);



        }catch (Exception ex)
        {
            logger.info("Exception occured in getcart method",ex);
            return new ResponseEntity<>("Something went wrong" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
