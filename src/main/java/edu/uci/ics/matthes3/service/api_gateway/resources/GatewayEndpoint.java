package edu.uci.ics.matthes3.service.api_gateway.resources;

import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.core.ResponseDatabase;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.ObjectModels.Transaction;
import edu.uci.ics.matthes3.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.matthes3.service.api_gateway.threadpool.ThreadPool;
import edu.uci.ics.matthes3.service.api_gateway.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("report")
public class GatewayEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportGateway(@Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Getting request to report...");
        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];


//        Response response;
//        response = ModelValidator.verifySession(email, sessionID);
//        if (response != null)
//            return response;

//        ThreadPool threadPool = GatewayService.getThreadPool();

        if (transactionID != null) {
            Map<String, Integer> response_in_db = ResponseDatabase.checkResponse(transactionID);
            if (response_in_db != null) {
                ResponseDatabase.deleteRow(transactionID);
                if (response_in_db.size() != 0) {
                    for (String j : response_in_db.keySet()) {
                        String json = j;
                        int status = response_in_db.get(j);
                        ServiceLogger.LOGGER.info("************************************************");
                        ServiceLogger.LOGGER.info("FOUND RESPONSE IN DATABASE");
                        ServiceLogger.LOGGER.info(json);
                        ServiceLogger.LOGGER.info("************************************************");
                        return Response.status(status).entity(json)
                                .header("email", email)
                                .header("sessionID", sessionID)
                                .header("transactionID", transactionID)
                                .build();
                    }
                }
            }
        }

        return Response.status(Response.Status.NO_CONTENT)
                .header("message", "Response not ready; Please try again later.")
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionID", transactionID)
                .build();

    }

}
