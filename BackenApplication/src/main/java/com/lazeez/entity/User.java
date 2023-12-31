package com.lazeez.entity;

import com.lazeez.dto.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "users")
public class User {


    @Id
    private String id;


    private String userName;


    private String profileUrl ;

    private Long mobile;


    private Address address;


    private String email;

    private String password;

    private List<Cart> cart;

    private String role ="USER";





}
