package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.Movies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.GenreSimpleModel;

public class GetGenreResponseModel extends ResponseModel {
    @JsonIgnore
    private GenreSimpleModel[] genres;

    public GetGenreResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public GetGenreResponseModel(
            @JsonProperty(value = "resultCode", required = true)int resultCode,
            @JsonProperty(value = "resultCode", required = true)String message,
            @JsonProperty(value = "genres")GenreSimpleModel[] genres) {
        super(resultCode, message);
        this.genres = genres;
    }

    @JsonProperty(value = "genres")
    public GenreSimpleModel[] getGenres() {
        return genres;
    }
}
