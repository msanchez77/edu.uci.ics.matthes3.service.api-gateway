package edu.uci.ics.matthes3.service.api_gateway.threadpool;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.api_gateway.core.ResponseDatabase;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies.SearchMovieRequestModel;
import edu.uci.ics.matthes3.service.api_gateway.models.VerifySessionResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.utilities.EndpointServices;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Worker extends Thread {
    public static final int GET_REQUEST = 0;
    public static final int POST_REQUEST = 1;
    public static final int DELETE_REQUEST = 2;

    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process(ClientRequest clientRequest) {

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        WebTarget webTarget = client.target(clientRequest.getURI()).path(clientRequest.getEndpoint());

        if (clientRequest.getEndpoint().equals("/search") || clientRequest.getEndpoint().equals("/star/search")) {
            for (Map.Entry<String, List<String>> entry : clientRequest.getQuery_param().entrySet()) {
                ServiceLogger.LOGGER.info("Adding query parameter: " + entry.getKey() + ", value: " + entry.getValue());
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue().get(0));
            }
        }

        ServiceLogger.LOGGER.info("WebTarget URI: " + webTarget.getUri().toString());

        String email = clientRequest.getEmail();
        String sessionID = clientRequest.getSessionID();
        String transactionID = clientRequest.getTransactionID();

        Response response_header;
        if (clientRequest.getHttp_method() == GET_REQUEST) {
            ServiceLogger.LOGGER.info("Processing GET request...");
            Invocation.Builder invocationBuilder =
                    webTarget.request(MediaType.APPLICATION_JSON)
                            .header("email", email)
                            .header("sessionID", sessionID)
                            .header("transactionID", transactionID);
            response_header = invocationBuilder.get();
        } else if (clientRequest.getHttp_method() == POST_REQUEST) {
            ServiceLogger.LOGGER.info("Processing POST request...");
            Invocation.Builder invocationBuilder =
                    webTarget.request(MediaType.APPLICATION_JSON)
                            .header("email", email)
                            .header("sessionID", sessionID)
                            .header("transactionID", transactionID);
            response_header = invocationBuilder.post(Entity.entity(clientRequest.getRequest(), MediaType.APPLICATION_JSON));

        } else if (clientRequest.getHttp_method() == DELETE_REQUEST) {
            ServiceLogger.LOGGER.info("Processing DELETE request...");
            Invocation.Builder invocationBuilder =
                    webTarget.request()
                            .header("email", email)
                            .header("sessionID", sessionID)
                            .header("transactionID", transactionID);

            response_header = invocationBuilder.delete();
        } else {
            ServiceLogger.LOGGER.info("Received unknown request type.");
            return;
        }

        ServiceLogger.LOGGER.info("Response status code: " + response_header.getStatus());

        if (response_header.getStatus() != -1)
            ResponseDatabase.insert(clientRequest, response_header);
    }

    @Override
    public void run() {
        while (true) {
            ClientRequest request = threadPool.remove();
            if (request != null)
                process(request);
        }
    }
}
