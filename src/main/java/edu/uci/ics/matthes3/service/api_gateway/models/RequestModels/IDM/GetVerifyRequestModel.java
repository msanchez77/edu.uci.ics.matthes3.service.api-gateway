package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM;

import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class GetVerifyRequestModel extends RequestModel {
    private String email;
    private int plevel;

    public GetVerifyRequestModel() {
    }

    public GetVerifyRequestModel(String email, int plevel) {
        this.email = email;
        this.plevel = plevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }
}
