package com.lazeez.controller;



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


    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User user){
        return userService.updateUser(userId, user);

    }


    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId){

        return userService.deleteUser(userId);

    }



    @GetMapping("/getUser/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId)
    {

        return userService.getUser(userId);

    }



    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){

        return userService.getAllUser();

    }


    @PostMapping("/addToCart/{productid}/{userId}")
    public ResponseEntity<?>  addToCart(@PathVariable  String productid,@PathVariable String userId)
    {
        return userService.addToCart(productid ,userId);
    }


    @GetMapping("/getCart/{userId}")
    public ResponseEntity<?>  getCart(@PathVariable String userId)
    {

        return userService.getCart(userId);


    }








}
