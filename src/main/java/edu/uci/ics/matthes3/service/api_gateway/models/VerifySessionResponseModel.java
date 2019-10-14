package edu.uci.ics.matthes3.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.utilities.ResultCodes;

@JsonInclude(JsonInclude.Include.NON_NULL) // Tells Jackson to ignore all fields with value of NULL
public class VerifySessionResponseModel {
    @JsonProperty(required = true) // Forces this field to be required in the JSON
    private int resultCode;
    @JsonProperty(required = true) // Forces this field to be required in the JSON
    private String message;
    private String sessionID;

    public VerifySessionResponseModel() {
    }

    public VerifySessionResponseModel(int resultCode, String sessionID) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode); // ResultCodes.setMessage is NOT required for your implementation
        this.sessionID = sessionID;
    }

    public VerifySessionResponseModel(int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode); // ResultCodes.setMessage is NOT required for your implementation
        this.sessionID = null;
    }


    @JsonProperty("resultCode") // Forces Jackson to name the field "resultCode" when serializing this object
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty("message") // Forces Jackson to name the field "resultCode" when serializing this object
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "sessionID") // Forces Jackson to name the field "resultCode" when serializing this object
    public String getSessionID() {
        return sessionID;
    }
}
