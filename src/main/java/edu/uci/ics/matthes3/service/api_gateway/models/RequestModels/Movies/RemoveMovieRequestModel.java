package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveMovieRequestModel extends RequestModel {

    @JsonProperty(required = true)
    private String id;

    public RemoveMovieRequestModel(
            @JsonProperty(value="id", required=true)String id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }
}
