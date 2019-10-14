package edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels;

import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;

import java.util.ArrayList;

public class Item  {
    private String email;
    private String movieId;
    private int quantity;

    public Item(String email, String movieId, int quantity) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public static ItemModel[] buildItemArray(ArrayList<Item> items) {
        ServiceLogger.LOGGER.info("Creating ItemModel from list...");

        if (items == null) {
            ServiceLogger.LOGGER.info("No items passed to model constructor.");
            return new ItemModel[0];
        }

        ServiceLogger.LOGGER.info("Items list is not empty...");
        int len = items.size();
        ItemModel[] array = new ItemModel[len];

        for (int i = 0; i < len; ++i) {
//            ServiceLogger.LOGGER.info("Adding " + items.get(i).getEmail() + ", " + items.get(i).getMovieId() + " to array.");
            // Convert each item in the arraylist to a ItemModel
            ItemModel itemModel = ItemModel.buildModelFromObject(items.get(i));
            array[i] = itemModel;
        }

        return array;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
