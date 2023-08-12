package com.lazeez.controller;



import com.lazeez.dto.LoginRequest;
import com.lazeez.entity.User;

import com.lazeez.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



@RequestMapping("/user")
@RestController
public class UserController {




    @Autowired
    private UserService userService;





    @PostMapping("/addUser")
    public ResponseEntity<?>  addUser(@RequestBody User user){


        return  userService.addUser(user);

    }


    @PutMapping("/updateUser/")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User user){
        return userService.updateUser( authorizationHeader,user);

    }


    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authorizationHeader){

        return userService.deleteUser(authorizationHeader);

    }



    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorizationHeader)
    {

        return userService.getUser(authorizationHeader);

    }



    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){

        return userService.getAllUser();

    }


    @PostMapping("/addToCart/{productid}")
    public ResponseEntity<?>  addToCart(@RequestHeader("Authorization") String authorizationHeader,@PathVariable  String productid)
    {
        return userService.addToCart(productid,authorizationHeader);
    }


    @GetMapping("/getCart")
    public ResponseEntity<?>  getCart(@RequestHeader("Authorization") String authorizationHeader)
    {

        return userService.getCart(authorizationHeader);


    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest)
    {

        return userService.login(loginRequest);




    }








}
