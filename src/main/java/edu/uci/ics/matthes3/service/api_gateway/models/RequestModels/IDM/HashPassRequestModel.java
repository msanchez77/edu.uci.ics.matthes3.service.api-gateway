package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class HashPassRequestModel extends RequestModel {
    private String password;

    @JsonCreator
    public HashPassRequestModel(@JsonProperty(value = "password", required = true) String password) {
        this.password = password;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
}
