package com.lazeez.controller;



import com.lazeez.entity.Product;
import com.lazeez.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/product")
@RestController
@CrossOrigin(origins = "*")
public class ProductController {





     @Autowired
      private ProductService productService;

    @GetMapping("/products")

    public ResponseEntity<?> getAllProduct()
    {
            return productService.getAllproducts();
    }


    @PostMapping("/addProduct")
    public ResponseEntity<?> saveProduct(@RequestBody Product request )
    {
        return productService.saveProduct(request);

    }




    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId , @RequestBody Product product){

        return  productService.updateProduct(productId,product);


    }


    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId)
    {
        return productService.deleteProduct(productId);
    }



    @GetMapping("/dto/{pId}")
    public ResponseEntity<?>  dtoTest(@PathVariable String pId)
    {
        return productService.dtoTest(pId);
    }



}
