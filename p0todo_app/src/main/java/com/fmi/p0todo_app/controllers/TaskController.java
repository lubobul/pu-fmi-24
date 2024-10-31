package com.fmi.p0todo_app.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    public String getTask(){
        return "Todo Task";
    }
}
