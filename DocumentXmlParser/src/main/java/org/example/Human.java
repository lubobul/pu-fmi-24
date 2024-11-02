package org.example;

import org.example.parse.json.JSONEntity;
import org.example.parse.json.JSONField;
import org.example.parse.json.JsonFieldType;

@JSONEntity
public class Human {
    @JSONField(title = "firstName")
    private String fname = "Lyubomir";
    @JSONField(title = "lastName")
    private String lname = "Stoychev";
    @JSONField(expectedType = JsonFieldType.PLAIN)
    private int age = 33;
}
