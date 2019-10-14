package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.Movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.StarModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    @JsonIgnore
    private StarModel[] stars;

    public SearchStarResponseModel() {
    }

    @JsonCreator
    public SearchStarResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="stars") StarModel[] stars) {
//        super(resultCode, message);
        this.resultCode = resultCode;
        this.message = message;
        this.stars = stars;
    }

    public StarModel[] getStars() {
        return stars;
    }
}
