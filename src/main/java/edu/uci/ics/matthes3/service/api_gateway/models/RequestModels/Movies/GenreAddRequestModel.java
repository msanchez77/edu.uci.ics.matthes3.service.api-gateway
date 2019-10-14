package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies;

import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class GenreAddRequestModel extends RequestModel {
    private String name;

    public GenreAddRequestModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
