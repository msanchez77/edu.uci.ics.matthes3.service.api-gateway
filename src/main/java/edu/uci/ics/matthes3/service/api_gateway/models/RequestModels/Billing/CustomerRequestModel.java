package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class CustomerRequestModel extends RequestModel {
    @JsonProperty(required=true)
    private String email;

    private Integer invalidResultCode = 0;

    public CustomerRequestModel() {
    }

    public CustomerRequestModel(
            @JsonProperty(value="email", required=true) String email) {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public Integer getInvalidResultCode() {
        return invalidResultCode;
    }

    public void setInvalidResultCode(Integer invalidResultCode) {
        this.invalidResultCode = invalidResultCode;
    }
}
