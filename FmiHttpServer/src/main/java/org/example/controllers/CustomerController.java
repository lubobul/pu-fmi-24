package org.example.controllers;

import org.example.stereotypes.*;

@Controller(method = "GET", endpoint = "/customer")
public class CustomerController {

    @GetMapping("/customer")
    public String fetchAllCustomers(){

        return "Customer info - GET Request";
    }

    @PostMapping("/customer")
    public String createNewCustomer(){

        return "Customer info - POST Request";
    }

    @PutMapping("/customer")
    public String updateCustomer(){

        return "Customer info - PUT Request";
    }

    @DeleteMapping("/customer")
    public String deleteCustomer(){

        return "Customer info - DELETE Request";
    }
}
