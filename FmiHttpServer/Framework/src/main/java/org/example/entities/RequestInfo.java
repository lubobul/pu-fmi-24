package org.example.entities;

import java.util.Objects;

public class RequestInfo {
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