package edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels;

public class Amount {
    private String total;
    private String currency;

    public Amount(String total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }
}
