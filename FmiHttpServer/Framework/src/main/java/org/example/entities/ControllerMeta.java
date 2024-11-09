package org.example.entities;

public class ControllerMeta {
    private Class classRef;

    private String methodName;
    public ControllerMeta(Class classRef, String methodName) {
        this.classRef = classRef;
        this.methodName = methodName;
    }
    public Class getClassRef() {
        return classRef;
    }

    public void setClassRef(Class classRef) {
        this.classRef = classRef;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
