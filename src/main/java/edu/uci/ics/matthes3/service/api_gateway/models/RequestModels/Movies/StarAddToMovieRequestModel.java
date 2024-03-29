package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

public class StarAddToMovieRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String starid;
    @JsonProperty(required = true)
    private String movieid;

    @JsonCreator
    public StarAddToMovieRequestModel(
            @JsonProperty(value = "starid", required = true)String starid,
            @JsonProperty(value = "movieid", required = true)String movieid) {
        this.starid = starid;
        this.movieid = movieid;
    }

    @JsonProperty("starid")
    public String getStarid() {
        return starid;
    }

    @JsonProperty("movieid")
    public String getMovieid() {
        return movieid;
    }
}
