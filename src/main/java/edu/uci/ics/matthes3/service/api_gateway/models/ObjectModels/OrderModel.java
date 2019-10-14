package edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;

public class OrderModel {
    private String email;
    private String movieId;
    private Integer quantity;

    private Float unit_price;
    private Float discount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date saleDate;

    public OrderModel() {
    }

    public OrderModel(String email, String movieId, Integer quantity, Float unit_price, Float discount, Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.saleDate = saleDate;
    }

    public static OrderModel buildModelFromObject(OrderItem order) {
        return new OrderModel(
                order.getEmail(),
                order.getMovieId(),
                order.getQuantity(),
                order.getUnit_price(),
                order.getDiscount(),
                order.getSaleDate()
        );
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getUnit_price() {
        return roundFloat(unit_price);
    }

    public Float getDiscount() {
        return roundFloat(discount);
    }

    public Date getSaleDate() {
        return saleDate;
    }

    private Float roundFloat(Float num) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
