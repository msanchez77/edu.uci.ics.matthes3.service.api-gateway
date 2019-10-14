package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarRequestModel extends RequestModel {
    @JsonIgnore
    private String name;
    @JsonIgnore
    private int birthYear;
    @JsonIgnore
    private String movieTitle;

    @JsonProperty(required = true)
    private int offset;
    @JsonProperty(required = true)
    private int limit;
    @JsonProperty(required = true)
    private String orderby;
    @JsonProperty(required = true)
    private String direction;

    public SearchStarRequestModel() {
    }

    @JsonCreator
    public SearchStarRequestModel(
            @JsonProperty(value="name")String name,
            @JsonProperty(value="birthYear")Integer birthYear,
            @JsonProperty(value="movieTitle")String movieTitle,
            @JsonProperty(value="offset", required=true) int offset,
            @JsonProperty(value="limit", required=true) int limit,
            @JsonProperty(value="orderby", required=true) String orderby,
            @JsonProperty(value="direction", required=true) String direction) {

        ServiceLogger.LOGGER.info("In non-empty constructor");
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.offset = offset;
        this.limit = limit;
        this.orderby = orderby;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public String getOrderby() {
        return orderby;
    }

    public String getDirection() {
        return direction;
    }
}
