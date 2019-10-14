package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM;


import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class InsertUserRequestModel extends RequestModel {
    private String email;
    private int status;
    private int plevel;
    private String salt;
    private String pword;

    public InsertUserRequestModel() {
    }

    public InsertUserRequestModel(String email, int status, int plevel, String salt, String pword) {
        this.email = email;
        this.status = status;
        this.plevel = plevel;
        this.salt = salt;
        this.pword = pword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlevel() {
        return plevel;
    }

    public void setPlevel(int plevel) {
        this.plevel = plevel;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }
}
