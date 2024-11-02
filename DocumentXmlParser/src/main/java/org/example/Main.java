package org.example;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        var item = new Item();
        System.out.println(parseObject(item));
    }

    public static String parseObject(Object parsableObject) throws IllegalAccessException {
        Class<?> parsableObjectClass = parsableObject.getClass();

        Field[] fields = parsableObjectClass.getDeclaredFields();

        StringBuilder xmlBuilder = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);

            xmlBuilder.append("<")
                    .append(field.getName())
                    .append(">")
                    .append(field.get(parsableObject))
                    .append("</")
                    .append(field.getName())
                    .append(">");
//            System.out.println(field.getName());
//            System.out.println(field.get(parsableObject));
        }

        return xmlBuilder.toString();
    }
}