package edu.uci.ics.matthes3.service.api_gateway.resources;

import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Billing.*;
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

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert into cart.");
        
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

        SCInsertUpdateRequestModel requestModel;

        try {
            requestModel = (SCInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, SCInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SCInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCartInsert());

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

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText) {

        ServiceLogger.LOGGER.info("Received request to update cart.");
        
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

        SCInsertUpdateRequestModel requestModel;

        try {
            requestModel = (SCInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, SCInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SCInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCartUpdate());
        
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

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText) {
        
        ServiceLogger.LOGGER.info("Received request to delete cart.");
        
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
                        .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response)
                        .header("email", email)
                        .header("sessionID", sessionID)
                        .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        SCDeleteRequestModel requestModel;

        try {
            requestModel = (SCDeleteRequestModel) ModelValidator.verifyModel(jsonText, SCDeleteRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SCDeleteRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartDelete());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCartDelete());

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

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve cart.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        SCRetrieveClearRequestModel requestModel;

        try {
            requestModel = (SCRetrieveClearRequestModel) ModelValidator.verifyModel(jsonText, SCRetrieveClearRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SCRetrieveClearRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCartRetrieve());

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

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to clear cart.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        SCRetrieveClearRequestModel requestModel;

        try {
            requestModel = (SCRetrieveClearRequestModel) ModelValidator.verifyModel(jsonText, SCRetrieveClearRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SCRetrieveClearRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartClear());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCartClear());

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

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert credit card.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CCInsertUpdateRequestModel requestModel;

        try {
            requestModel = (CCInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, CCInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CCInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCcInsert());

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

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update credit card.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CCInsertUpdateRequestModel requestModel;

        try {
            requestModel = (CCInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, CCInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CCInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCcUpdate());

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

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to delete credit card.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();
        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CCDeleteRetrieveRequestModel requestModel;

        try {
            requestModel = (CCDeleteRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CCDeleteRetrieveRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CCDeleteRetrieveRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcDelete());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCcDelete());

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

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve credit card.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CCDeleteRetrieveRequestModel requestModel;

        try {
            requestModel = (CCDeleteRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CCDeleteRetrieveRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CCDeleteRetrieveRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCcRetrieve());

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

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert customer.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CustomerInsertUpdateRequestModel requestModel;

        try {
            requestModel = (CustomerInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, CustomerInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCustomerInsert());

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

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update customer.");
        
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
                return Response.status(Status.OK).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(response).header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID).build();
            }
        }

        sessionID = response.getSessionID();

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CustomerInsertUpdateRequestModel requestModel;

        try {
            requestModel = (CustomerInsertUpdateRequestModel) ModelValidator.verifyModel(jsonText, CustomerInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerInsertUpdateRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCustomerUpdate());

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

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve customer.");
        
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

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        CustomerRetrieveRequestModel requestModel;

        try {
            requestModel = (CustomerRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CustomerRetrieveRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerRetrieveRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPCustomerRetrieve());

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

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to place order.");
        
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

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        OrderRequestModel requestModel;

        try {
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, OrderRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderPlace());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPOrderPlace());

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

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve order.");
        
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

        ServiceLogger.LOGGER.info("Received request to verify user privilege.");
        OrderRequestModel requestModel;

        try {
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
            ServiceLogger.LOGGER.info("Received jsonText: " + jsonText);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, OrderRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(1);

        ServiceLogger.LOGGER.info("URI: " + GatewayService.getBillingConfigs().getBillingUri());
        ServiceLogger.LOGGER.info("Endpoint: " + GatewayService.getBillingConfigs().getEPOrderRetrieve());

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
