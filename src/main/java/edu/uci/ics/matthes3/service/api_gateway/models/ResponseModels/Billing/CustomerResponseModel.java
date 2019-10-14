package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.Billing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.CustomerModel;

public class CustomerResponseModel extends ResponseModel {
    @JsonIgnore
    private CustomerModel customer;

    public CustomerResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public CustomerResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty("customer") CustomerModel customer) {
        super(resultCode, message);
        this.customer = customer;
    }

    @JsonProperty(value="customer")
    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }
}
