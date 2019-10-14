package edu.uci.ics.matthes3.service.api_gateway.models.ResponseModels.Movies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.MovieSearchModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieResponseModel extends ResponseModel {
    @JsonIgnore
    private MovieSearchModel[] movies;

    public SearchMovieResponseModel() {
    }

    public SearchMovieResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message) {
        super(resultCode, message);
    }

    public SearchMovieResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="movies") MovieSearchModel[] movies) {
        super(resultCode, message);
        this.movies = movies;
    }

    public MovieSearchModel[] getMovies() {
        return movies;
    }
}
