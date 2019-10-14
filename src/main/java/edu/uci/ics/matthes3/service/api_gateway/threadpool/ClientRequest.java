package edu.uci.ics.matthes3.service.api_gateway.threadpool;

import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

import javax.ws.rs.core.MultivaluedMap;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;

    // 0 --> GET
    // 1 --> POST
    // 2 --> DELETE
    private Integer http_method;
    private String path_parameter;

    private MultivaluedMap<String, String> query_param;

    public ClientRequest() {

    }

    public ClientRequest(String email, String sessionID, String transactionID, RequestModel request, String URI, String endpoint, Integer http_method, String path_parameter, MultivaluedMap<String, String> query_param) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
        this.http_method = http_method;
        this.path_parameter = path_parameter;
        this.query_param = query_param;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getHttp_method() {
        return http_method;
    }

    public void setHttp_method(Integer http_method) {
        this.http_method = http_method;
    }

    public String getPath_parameter() {
        return path_parameter;
    }

    public void setPath_parameter(String path_parameter) {
        this.path_parameter = path_parameter;
    }

    public MultivaluedMap<String, String> getQuery_param() {
        return query_param;
    }

    public void setQuery_param(MultivaluedMap<String, String> query_param) {
        this.query_param = query_param;
    }
}
