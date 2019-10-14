package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.Billing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.ItemModel;

public class ShoppingCartResponseModel extends ResponseModel {
    @JsonIgnore
    private ItemModel[] items;

    public ShoppingCartResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public ShoppingCartResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty("items") ItemModel[] items) {
        super(resultCode, message);
        this.items = items;
    }

    @JsonProperty(value="items")
    public ItemModel[] getItems() {
        return items;
    }

    public void setItems(ItemModel[] items) {
        this.items = items;
    }
}
