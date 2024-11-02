package org.example.parse.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JSONField {
    String title() default "_";
    JsonFieldType expectedType() default JsonFieldType.STRING;
}
