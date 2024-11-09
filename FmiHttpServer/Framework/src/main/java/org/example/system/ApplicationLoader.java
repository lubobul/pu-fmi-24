package org.example.system;
import org.example.stereotypes.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class ApplicationLoader {

    public static class RequestInfo {
        public RequestInfo(String httpMethod, String httpEndpoint) {
            this.httpMethod = httpMethod;
            this.httpEndpoint = httpEndpoint;
        }

        private String httpMethod;
        private String httpEndpoint;

        @Override
        public int hashCode() {
            return Objects.hash(httpMethod, httpEndpoint);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RequestInfo that)) return false;
            return Objects.equals(httpMethod, that.httpMethod) && Objects.equals(httpEndpoint, that.httpEndpoint);
        }
    }

    private ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private HashMap<RequestInfo, Class> controllerLookUpTable = new HashMap<>();

    public String executeController(String httpMethod, String httpEndpoint) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = this.controllerLookUpTable.get(new RequestInfo(httpMethod, httpEndpoint));

        if(clazz == null){
            return "";
        }

        var controllerInstance = clazz.getDeclaredConstructor().newInstance();

        return (String) clazz.getMethod("index").invoke(controllerInstance);
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

                String className = packageRef.replace(".class", "");
                String classPath = packageName + "." + className;
                Class clazz = Class.forName(classPath);

                if (clazz.isAnnotationPresent(org.example.stereotypes.Controller.class)) {
                    Controller controllerAnnotation = (Controller) clazz.getAnnotation(Controller.class);

                    String method = controllerAnnotation.method();
                    String endpoint = controllerAnnotation.endpoint();

                    this.controllerLookUpTable.put(
                            new RequestInfo(method, endpoint),
                            clazz
                    );
                }
            }
        }
    }
}
