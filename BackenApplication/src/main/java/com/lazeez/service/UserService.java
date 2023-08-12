package com.lazeez.service;



import com.lazeez.CustomeException;
import com.lazeez.dto.Cart;
import com.lazeez.dto.LoginRequest;
import com.lazeez.entity.Product;
import com.lazeez.entity.User;
import com.lazeez.repository.ProductRepository;
import com.lazeez.repository.UserRepository;
import com.lazeez.security.JwtUtil;
import com.lazeez.security.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    Logger logger = LoggerFactory.getLogger(UserService.class);
    public   ResponseEntity<?> updateUser(String authorizationHeader , User user) {
        try{

            User  user1  = getUserByToken(authorizationHeader);

            user.setId(user1.getId());

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

    public ResponseEntity<?> deleteUser(String authorizationHeader) {
        try {
            // Extract the token from the authorization header
            String token = authorizationHeader.replace("Bearer ", "");

            System.out.println("token"+"    "+token);

            String email = jwtUtil.extractUsername(token);
            UserDetails userDetails;
            if(!email.isEmpty())
            {
                 userDetails =userDetailService.loadUserByUsername(email);
            }else{
                return new ResponseEntity<>("User not there",HttpStatus.BAD_REQUEST);

            }

            System.out.println("inside delete========="+email);


            // Validate the token
            if (jwtUtil.validateToken(token,userDetails))
            {
                // Extract user information from the token
                String username = jwtUtil.extractUsername(token);

                // Find the user by email (assuming email is the identifier)
                User user = userRepository.findByEmail(username);

                if (user != null) {
                    userRepository.deleteById(user.getId()); // Assuming user.getId() is the user's identifier
                    return ResponseEntity.ok("User deleted");
                } else {
                    return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException ex) {
            logger.info("error occurred inside deleteUser", ex);
            return new ResponseEntity<>("Authorization header must not be null", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.info("error occurred inside deleteUser", ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public   ResponseEntity<?> getUser(String authorizationHeader) {

        try
        {
            User user = getUserByToken(authorizationHeader);


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

    public ResponseEntity<?> addToCart(String productId ,String authorizationHeader) {
        try
        {

            User user = getUserByToken(authorizationHeader);
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

    public ResponseEntity<?> getCart(String authorizationHeader) {
        try{


           User user = getUserByToken(authorizationHeader);
           List<Cart> cart = user.getCart();

            return ResponseEntity.ok(cart);



        }catch (Exception ex)
        {
            logger.info("Exception occured in getcart method",ex);
            return new ResponseEntity<>("Something went wrong" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String login(LoginRequest loginRequest) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

        }catch (Exception ex)
        {
            throw new UsernameNotFoundException("User not found "+loginRequest.getEmail());
        }

        UserDetails userDetails= this.userDetailService.loadUserByUsername(loginRequest.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return token;
    }



    private User getUserByToken(String authorizationHeader)
    {
        // Extract the token from the authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        if(token.isEmpty())
        {
            throw new CustomeException("token is empty");
        }



        String email = jwtUtil.extractUsername(token);
        User  user = userRepository.findByEmail(email);
        if(user!=null)
        {
            return  user;
        }

        return null;


    }
}
