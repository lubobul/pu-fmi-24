package org.example.system;

import org.example.entities.ControllerMeta;
import org.example.entities.RequestInfo;
import org.example.stereotypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ApplicationLoader {

    private ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private HashMap<RequestInfo, ControllerMeta> controllerLookUpTable = new HashMap<>();

    public String executeController(RequestInfo requestInfo) throws
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        ControllerMeta controllerMethodRef = this.controllerLookUpTable.get(new RequestInfo(requestInfo.getHttpMethod(), requestInfo.getHttpEndpoint()));

        if (controllerMethodRef == null) {
            return "";
        }

        Class controllerClass = controllerMethodRef.getClassRef();
        String methodName = controllerMethodRef.getMethodName();

        var controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        return (String) controllerClass.getMethod(methodName).invoke(controllerInstance);
    }

    public void findAllClasses(String packageName) throws IOException, ClassNotFoundException {
        InputStream classLoaderStream = this.classLoader.getResourceAsStream(packageName.replace(".", "/"));
        BufferedReader classReader = new BufferedReader(new InputStreamReader((classLoaderStream)));

        String packageRef = "";

        while ((packageRef = classReader.readLine()) != null) {

            if (!packageRef.contains(".class")) {
                findAllClasses(packageName + "." + packageRef);
                continue;
            }

            if (packageRef.contains(".class")) {

                this.classParser(packageRef, packageName);
            }
        }
    }

    private void classParser(String packageRef, String packageName) throws ClassNotFoundException {
        String className = packageRef.replace(".class", "");
        String classPath = packageName + "." + className;
        Class clazz = Class.forName(classPath);

        if (clazz.isAnnotationPresent(org.example.stereotypes.Controller.class)) {
            this.parseController(clazz);
        }
    }

    private void parseController(Class clazz) {

        Method[] controllerClassMethodCollection = clazz.getMethods();

        for (Method method : controllerClassMethodCollection) {
            if (method.isAnnotationPresent(org.example.stereotypes.GetMapping.class)) {
                GetMapping getAnnotation = method.getAnnotation(org.example.stereotypes.GetMapping.class);
                String methodEndpoint = getAnnotation.value();
                this.controllerLookUpTable.put(new RequestInfo(
                        "GET",
                        methodEndpoint
                ), new ControllerMeta(
                        clazz,
                        method.getName()
                ));
            }
        }

        for (Method method : controllerClassMethodCollection) {
            if (method.isAnnotationPresent(org.example.stereotypes.PostMapping.class)) {
                PostMapping getAnnotation = method.getAnnotation(org.example.stereotypes.PostMapping.class);
                String methodEndpoint = getAnnotation.value();
                this.controllerLookUpTable.put(new RequestInfo(
                        "POST",
                        methodEndpoint
                ), new ControllerMeta(
                        clazz,
                        method.getName()
                ));
            }
        }

        for (Method method : controllerClassMethodCollection) {
            if (method.isAnnotationPresent(org.example.stereotypes.PutMapping.class)) {
                PutMapping getAnnotation = method.getAnnotation(org.example.stereotypes.PutMapping.class);
                String methodEndpoint = getAnnotation.value();
                this.controllerLookUpTable.put(new RequestInfo(
                        "PUT",
                        methodEndpoint
                ), new ControllerMeta(
                        clazz,
                        method.getName()
                ));
            }
        }

        for (Method method : controllerClassMethodCollection) {
            if (method.isAnnotationPresent(org.example.stereotypes.DeleteMapping.class)) {
                DeleteMapping getAnnotation = method.getAnnotation(org.example.stereotypes.DeleteMapping.class);
                String methodEndpoint = getAnnotation.value();
                this.controllerLookUpTable.put(new RequestInfo(
                        "DELETE",
                        methodEndpoint
                ), new ControllerMeta(
                        clazz,
                        method.getName()
                ));
            }
        }
    }
}
