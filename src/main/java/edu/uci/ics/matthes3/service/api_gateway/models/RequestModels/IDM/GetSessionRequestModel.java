package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM;

import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class GetSessionRequestModel extends RequestModel {
    private String email;
    private String sessionID;

    public GetSessionRequestModel() {
    }

    public GetSessionRequestModel(String email, String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
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
}
