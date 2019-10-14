package edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels;

public class ItemModel {
    private String email;
    private String movieId;
    private int quantity;


    public ItemModel(String email, String movieId, int quantity) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public static ItemModel buildModelFromObject(Item item) {
        return new ItemModel(
                item.getEmail(),
                item.getMovieId(),
                item.getQuantity()
        );
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getQuantity() {
        return quantity;
    }
}
