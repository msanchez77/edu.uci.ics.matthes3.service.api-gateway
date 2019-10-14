package edu.uci.ics.matthes3.service.api_gateway.resources;

import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM.GetRegisterRequestModel;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM.GetSessionRequestModel;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.IDM.GetVerifyRequestModel;
import edu.uci.ics.matthes3.service.api_gateway.models.VerifySessionResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.matthes3.service.api_gateway.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to register user.");
        GetRegisterRequestModel requestModel;

        String transactionID = TransactionIDGenerator.generateTransactionID();

        try {
            requestModel = (GetRegisterRequestModel) ModelValidator.verifyModel(jsonText, GetRegisterRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GetRegisterRequestModel.class, null, null, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserRegister());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT)
                .header("transactionID", transactionID)
                .header("responseModel", responseModel.toString())
                .build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to login user.");
        GetRegisterRequestModel requestModel;

        String transactionID = TransactionIDGenerator.generateTransactionID();

        try {
            requestModel = (GetRegisterRequestModel) ModelValidator.verifyModel(jsonText, GetRegisterRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GetRegisterRequestModel.class, null, null, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserLogin());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getIdmConfigs().getIdmUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getIdmConfigs().getEPUserLogin());

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT)
                .header("transactionID", transactionID)
                .header("responseModel", responseModel.toString())
                .build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to verify user session.");
        GetSessionRequestModel requestModel;

        String transactionID = TransactionIDGenerator.generateTransactionID();

        try {
            requestModel = (GetSessionRequestModel) ModelValidator.verifyModel(jsonText, GetSessionRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GetSessionRequestModel.class, null, null, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());
        cr.setRequest(requestModel);
        cr.setEmail(requestModel.getEmail());
        cr.setSessionID(requestModel.getSessionID());
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT)
                .header("email", requestModel.getEmail())
                .header("transactionID", transactionID)
                .header("sessionID", requestModel.getSessionID())
                .header("responseModel", responseModel.toString())
                .build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(@Context HttpHeaders headers, String jsonText) {

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);

        VerifySessionResponseModel response;
        response = ModelValidator.verifySession(email, sessionID);
        if (response.getResultCode() != 130) {
            if (response.getResultCode() > 0) {
                return Response.status(Status.OK).entity(response)
                        .header("email", email)
                        .header("sessionID", sessionID)
                        .header("transactionID", transactionID)
                        .build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response)
                        .header("email", email)
                        .header("sessionID", sessionID)
                        .header("transactionID", transactionID)
                        .build();
            }
        }

        sessionID = response.getSessionID();

        GetVerifyRequestModel requestModel;

        try {
            requestModel = (GetVerifyRequestModel) ModelValidator.verifyModel(jsonText, GetVerifyRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GetVerifyRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getIdmConfigs().getIdmUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getIdmConfigs().getEPUserLogin());

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("responseModel", responseModel.toString())
                .build();
    }
}
