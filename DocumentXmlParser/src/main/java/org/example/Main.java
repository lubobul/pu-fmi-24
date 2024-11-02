package org.example;

import org.example.parse.json.JSONField;
import org.example.parse.json.JsonFieldType;

import java.lang.reflect.Field;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
//        var item = new Item();
//        System.out.println(parseObject(item));

        var human = new Human();
        System.out.println(parseObjectToJSON(human));
    }

    public static String parseObjectToJSON(Object parsableObject) throws IllegalAccessException {

        Class<?> parsableObjectClass = parsableObject.getClass();

        Field[] fields = parsableObjectClass.getDeclaredFields();

        StringBuilder xmlBuilder = new StringBuilder();

        if (!parsableObjectClass.isAnnotationPresent(org.example.parse.json.JSONEntity.class)) {
            throw new RuntimeException("Class " + parsableObjectClass.getName() + " is not parsable. Please annotate with JSONEntity.");
        }
        xmlBuilder.append("{");

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(org.example.parse.json.JSONField.class)) {

                JSONField jsonAnnotation = field.getAnnotation(org.example.parse.json.JSONField.class);
                String fieldName = Objects.equals(jsonAnnotation.title(), "_") ?
                        field.getName() : jsonAnnotation.title();

                var isJsonValuePlain = jsonAnnotation.expectedType() == JsonFieldType.PLAIN;

                var jsonValue = isJsonValuePlain ? field.get(parsableObject) : "\"" + field.get(parsableObject) + "\"";

                xmlBuilder.append(fieldName)
                        .append(":")
                        .append(jsonValue)
                        .append(",");
            }

        }
        xmlBuilder.deleteCharAt(xmlBuilder.length() - 1);
        xmlBuilder.append("}");

        return xmlBuilder.toString();
    }

    public static String parseObject(Object parsableObject) throws IllegalAccessException {
        Class<?> parsableObjectClass = parsableObject.getClass();

        Field[] fields = parsableObjectClass.getDeclaredFields();

        StringBuilder xmlBuilder = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);


            if (field.isAnnotationPresent(org.example.Documentable.class)) {

                Documentable documentableAnnotation = field.getAnnotation(org.example.Documentable.class);
                String fieldName = Objects.equals(documentableAnnotation.title(), "_") ?
                        field.getName() : documentableAnnotation.title();

                xmlBuilder.append("<")
                        .append(fieldName)
                        .append(">")
                        .append(field.get(parsableObject))
                        .append("</")
                        .append(fieldName)
                        .append(">");
            }

//            System.out.println(field.getName());
//            System.out.println(field.get(parsableObject));
        }

        return xmlBuilder.toString();
    }
}