package edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarAddRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String name;
    @JsonIgnore
    private Integer birthYear;

    @JsonCreator
    public StarAddRequestModel(
            @JsonProperty(value="name", required=true) String name,
            @JsonProperty(value="birthYear") Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
