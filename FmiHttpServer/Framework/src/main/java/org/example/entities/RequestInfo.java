package org.example.entities;

import java.util.HashMap;
import java.util.Objects;

public class RequestInfo {
    private String httpMethod;

    private String httpEndpoint;
    private String body;

    private HashMap<String, String> headers = new HashMap<>();

    public RequestInfo() {
        this.httpMethod = "";
        this.httpEndpoint = "";
    }

    public RequestInfo(String httpMethod, String httpEndpoint) {
        this.httpMethod = httpMethod;
        this.httpEndpoint = httpEndpoint;
    }

    public int getContentLength(){
        String contentLengthValue  = this.getHeaderValue("Content-Length");

        if(Objects.isNull(contentLengthValue)){
            return 0;
        }

        return Integer.parseInt(contentLengthValue);
    }

    public boolean hasBodyContent() {
        return this.getContentLength() != 0;
    }

    public boolean hasMethodAndEndpoint() {
        return !this.getHttpMethod().isEmpty() && !this.getHttpEndpoint().isEmpty();
    }

    public void setHeader(String key, String value){
        this.headers.put(key, value);
    }

    public boolean hasHeader(String header){
        return this.headers.containsKey(header);
    }

    public String getHeaderValue(String header){
        return this.headers.get(header);
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

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