package org.example.controllers;

import org.example.stereotypes.Controller;
import org.example.stereotypes.GetMapping;

@Controller(method = "GET", endpoint = "/customer")
public class CustomerController {

    @GetMapping("/customer")
    public String index(){
        return "Customer info";
    }
}
