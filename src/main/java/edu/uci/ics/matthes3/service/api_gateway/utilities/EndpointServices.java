package edu.uci.ics.matthes3.service.api_gateway.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.*;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies.SearchMovieRequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;

public class EndpointServices {

    public static String[] getHeaders(HttpHeaders headers) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        ServiceLogger.LOGGER.info("EMAIL: " + email);
        ServiceLogger.LOGGER.info("SESSIONID: " + sessionID);
        ServiceLogger.LOGGER.info("TRANSACTIONID: " + transactionID);

        return new String[]{email, sessionID, transactionID};
    }

    public static VerifyPrivilegeResponseModel verifyPrivilege(String email) {
        VerifyPrivilegeResponseModel privilegeResponseModel;
        boolean privilegeSufficient = userPrivilegeSufficient(email, 3);
        if (!privilegeSufficient) {
            ResultCodes resultCodes = new ResultCodes();

            privilegeResponseModel = new VerifyPrivilegeResponseModel(
                    141,
                    resultCodes.setMessage(141)
            );

            return privilegeResponseModel;
        }

        return null;
    }

    public static boolean userPrivilegeSufficient(String email, int plevel) {
        ServiceLogger.LOGGER.info("Getting privilege level with IDM...");

        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();

        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = GatewayService.getIdmConfigs().getEPUserPrivilegeVerify();

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");

        // Check that status code of the request
        if (response.getStatus() == 200) {
            ServiceLogger.LOGGER.info("Received Status 200");
            // Success! Map the response to a ResponseModel
//            VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);
            String jsonText = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            try {
                VerifyPrivilegeResponseModel responseModel = mapper.readValue(jsonText, VerifyPrivilegeResponseModel.class);

                ServiceLogger.LOGGER.info("Verify Privilege ResultCode: " + responseModel.getResultCode());

                return responseModel.getResultCode() == 140;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            ServiceLogger.LOGGER.info("Received Status " + response.getStatus() + " -- you lose.");
        }
        return false;
    }

    public static VerifySessionResponseModel sendSessionVerifyRequest(String email, String sessionID) {
        ServiceLogger.LOGGER.info("Verifying sessionID with IDM...");

        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();

        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = GatewayService.getIdmConfigs().getEPSessionVerify();

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifySessionRequestModel requestModel = new VerifySessionRequestModel(email, sessionID);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");

        // Check that status code of the request
//        if (response.getStatus() == 200) {
//        ServiceLogger.LOGGER.info("Received Status 200");
        // Success! Map the response to a ResponseModel
//            VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);

        ServiceLogger.LOGGER.info("Received status: " + response.getStatus());
        String jsonText = response.readEntity(String.class);

        ServiceLogger.LOGGER.info("Json: " + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        try {
            VerifySessionResponseModel responseModel = mapper.readValue(jsonText, VerifySessionResponseModel.class);

            ServiceLogger.LOGGER.info("Verify Session ResultCode: " + responseModel.getResultCode());
            return responseModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        } else {
//            ServiceLogger.LOGGER.info("Received Status " + response.getStatus() + " -- you lose.");
//        }
//
//        return -13;
    }

    public static String buildJsonText(String email, String sessionID) {
        return String.format("{\"email\":\"%s\",\"sessionID\":\"%s\"}", email, sessionID);
    }

    public static String buildJsonText_Movie(String title, String genre, Integer year, String director, Boolean hidden, Integer limit, Integer offset, String sortBy, String direction) {
        String jsonText = "{";

        if (title != null)
            jsonText += String.format("\"title\":\"%s\"", title);
        if (genre != null)
            jsonText += String.format(",\"genre\":\"%s\"", genre);
        if (year != null)
            jsonText += String.format(",\"year\":%d", year);
        if (director != null)
            jsonText += String.format(",\"director\":\"%s\"", director);
        if (hidden != null)
            jsonText += String.format(",\"hidden\":%b", hidden);

        jsonText += String.format(",\"limit\":%d", limit);
        jsonText += String.format(",\"offset\":%d", offset);
        jsonText += String.format(",\"sortBy\":\"%s\"", sortBy);
        jsonText += String.format(",\"direction\":\"%s\"", direction);
        jsonText += "}";

        ServiceLogger.LOGGER.info("buildJsonText successfully ran.");
        return jsonText;
    }

    public static String buildJsonText_Star(String name, Integer birthYear, String movieTitle, Integer limit, Integer offset, String orderby, String direction) {
        String jsonText = "{";
        if (name != null)
            jsonText += String.format("\"name\":\"%s\"", name);
        if (birthYear != null)
            jsonText += String.format(",\"birthYear\":%d", birthYear);
        if (movieTitle != null)
            jsonText += String.format(",\"movieTitle\":\"%s\"", movieTitle);

        jsonText += String.format(",\"offset\":%d", offset);
        jsonText += String.format(",\"limit\":%d", limit);
        jsonText += String.format(",\"orderby\":\"%s\"", orderby);
        jsonText += String.format(",\"direction\":\"%s\"", direction);
        jsonText += "}";

        ServiceLogger.LOGGER.info("buildJsonText successfully ran.");
        return jsonText;
    }

    public static String buildGetMovieEndpoint(String endpoint, SearchMovieRequestModel requestModel) {
        UriBuilder uriBuilder = UriBuilder.fromPath(endpoint);

        if (requestModel.getTitle() != null)
            uriBuilder.queryParam("title", requestModel.getTitle());
        if (requestModel.getGenre() != null)
            uriBuilder.queryParam("genre", requestModel.getGenre());
        if (requestModel.getYear() != null)
            uriBuilder.queryParam("year", requestModel.getYear());
        if (requestModel.getDirector() != null)
            uriBuilder.queryParam("director", requestModel.getDirector());
        if (requestModel.getHidden() != null)
            uriBuilder.queryParam("hidden", requestModel.getHidden());
        if (requestModel.getGenre() != null)
            uriBuilder.queryParam("limit", requestModel.getLimit());
        if (requestModel.getYear() != null)
            uriBuilder.queryParam("offset", requestModel.getOffset());
        if (requestModel.getDirector() != null)
            uriBuilder.queryParam("orderBy", requestModel.getOrderby());
        if (requestModel.getDirector() != null)
            uriBuilder.queryParam("direction", requestModel.getDirection());

        return uriBuilder.toString();
    }
}
