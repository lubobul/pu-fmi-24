package com.fmi.p0todo_app.services;

import org.springframework.stereotype.Service;

@Service
public class SequenceGenerator {
    private int sequenceId = 1;

    public int getNextId(){
        return this.sequenceId++;
    }
}
