package org.example;

public class Item {
    @Documentable
    private String name = "Book";

    @Documentable(title = "sub_title")
    private String subtitle = "Science";
    @Documentable
    private double price = 14.0;
}
