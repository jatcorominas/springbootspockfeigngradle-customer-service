package com.feign.customerservice.controller;

import com.feign.customerservice.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    private static List<Customer> customerList = new ArrayList<>();
    static {
        customerList.add(new Customer(1, "customer-1", 24));
        customerList.add(new Customer( 2, "customer-2", 30));
        customerList.add(new Customer( 3, "customer-3", 40));
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() { return ResponseEntity.ok(customerList);}

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable int id){
        Customer customer = findCustomer(id);
        if(customer == null){
            return ResponseEntity.badRequest().body("Invalid customer id");
        }
        return ResponseEntity.ok(customer);
    }

    private Customer findCustomer(int id){
        Integer customerInt = new Integer(id);
        return customerList.stream()
                .filter(cust -> cust.getId()
                        .equals(customerInt)).findFirst().orElse(null);
    }
}
