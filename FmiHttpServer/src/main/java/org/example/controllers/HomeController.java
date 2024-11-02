package org.example.controllers;

import org.example.stereotypes.Controller;
import org.example.stereotypes.GetMapping;

@Controller(method = "GET", endpoint = "/home")
public class HomeController {

    @GetMapping("/home")

    public String index(){
        return "Home content";
    }
}
