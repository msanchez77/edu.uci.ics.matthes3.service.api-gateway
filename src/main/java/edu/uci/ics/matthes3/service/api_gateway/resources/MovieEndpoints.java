package edu.uci.ics.matthes3.service.api_gateway.resources;

import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.models.RequestModels.Movies.*;
import edu.uci.ics.matthes3.service.api_gateway.models.VerifySessionResponseModel;
import edu.uci.ics.matthes3.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.matthes3.service.api_gateway.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;

import static edu.uci.ics.matthes3.service.api_gateway.threadpool.Worker.GET_REQUEST;
import static edu.uci.ics.matthes3.service.api_gateway.threadpool.Worker.POST_REQUEST;
import static edu.uci.ics.matthes3.service.api_gateway.threadpool.Worker.DELETE_REQUEST;


@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo
            /*@QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("year") Integer year,
            @QueryParam("director") String director,
            @QueryParam("hidden") Boolean hidden,
            @QueryParam("limit") Integer limit,
            @QueryParam("offset") Integer offset,
            @DefaultValue("rating") @QueryParam("orderby") String orderby,
            @DefaultValue("desc") @QueryParam("direction") String direction*/
            ) {
        ServiceLogger.LOGGER.info("Received request to search movie. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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
        ServiceLogger.LOGGER.info("New sessionID: " + sessionID);

        MultivaluedMap<String, String> query_params = uriInfo.getQueryParameters();
        ServiceLogger.LOGGER.info("Query params: " + query_params);

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch());
        cr.setQuery_param(query_params);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers,
                                    @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Received request to get movie by id. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String endpoint_with_path_param = (GatewayService.getMovieConfigs().getEPMovieGet()).substring(0,5) + movieid;
        cr.setEndpoint(endpoint_with_path_param);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers,
                                    String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add movie. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        MovieAddRequestModel requestModel;

        try {
            requestModel = (MovieAddRequestModel) ModelValidator.verifyModel(jsonText, MovieAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, MovieAddRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieAdd());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(POST_REQUEST);

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

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers,
                                       @PathParam("movieid") String movieid) {

        ServiceLogger.LOGGER.info("Received request to delete movie. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String endpoint_with_path_param = (GatewayService.getMovieConfigs().getEPMovieDelete()).substring(0,8) + movieid;
        cr.setEndpoint(endpoint_with_path_param);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(DELETE_REQUEST);

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

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received request to get all the genres from the database. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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



    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers,
                                    String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add genre. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        GenreAddRequestModel requestModel;

        try {
            requestModel = (GenreAddRequestModel) ModelValidator.verifyModel(jsonText, GenreAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GenreAddRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(POST_REQUEST);

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

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers,
                                             @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Received request to get genres for a movie. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String endpoint_with_path_param = (GatewayService.getMovieConfigs().getEPGenreMovie()).substring(0,7) + movieid;
        cr.setEndpoint(endpoint_with_path_param);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers,
                                      @Context UriInfo uriInfo
                                      /*@QueryParam("title") String name,
                                      @QueryParam("genre") Integer birthYear,
                                      @QueryParam("year") String movieTitle,
                                      @QueryParam("limit") Integer limit,
                                      @QueryParam("offset") Integer offset,
                                      @DefaultValue("name") @QueryParam("orderBy") String orderBy,
                                      @DefaultValue("asc") @QueryParam("direction") String direction*/ ) {
        ServiceLogger.LOGGER.info("Received request to search for a star. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        MultivaluedMap<String, String> query_params = uriInfo.getQueryParameters();
        ServiceLogger.LOGGER.info("Query params: " + query_params);

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarSearch());
        cr.setQuery_param(query_params);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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

    @Path("star/{starid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers,
                                   @PathParam("starid") String starid) {
        ServiceLogger.LOGGER.info("Received request to get star by id. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String endpoint_with_path_param = (GatewayService.getMovieConfigs().getEPMovieDelete()).substring(0,6) + starid;
        cr.setEndpoint(endpoint_with_path_param);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(GET_REQUEST);

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

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers,
                                   String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add a star to the db. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        StarAddRequestModel requestModel;

        try {
            requestModel = (StarAddRequestModel) ModelValidator.verifyModel(jsonText, StarAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarAddRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(POST_REQUEST);

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

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers,
                                          String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add star to a movie. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        StarAddToMovieRequestModel requestModel;

        try {
            requestModel = (StarAddToMovieRequestModel) ModelValidator.verifyModel(jsonText, StarAddToMovieRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarAddToMovieRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarIn());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(POST_REQUEST);

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

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers,
                                        String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add movie rating. G");

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = TransactionIDGenerator.generateTransactionID();

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

        RatingAddRequestModel requestModel;

        try {
            requestModel = (RatingAddRequestModel) ModelValidator.verifyModel(jsonText, RatingAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RatingAddRequestModel.class, email, sessionID, transactionID);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPRating());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setHttp_method(POST_REQUEST);

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