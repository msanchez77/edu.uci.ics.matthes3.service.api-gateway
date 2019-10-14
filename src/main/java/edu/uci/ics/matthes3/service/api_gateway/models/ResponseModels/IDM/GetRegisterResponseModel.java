package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.IDM;

import edu.uci.ics.matthes3.service.api_gateway.models.ResponseModel;

public class GetRegisterResponseModel extends ResponseModel {
    private int resultCode;
    private String message;

    public GetRegisterResponseModel() {
    }

    public GetRegisterResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
