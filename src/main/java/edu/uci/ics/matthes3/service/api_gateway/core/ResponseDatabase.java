package edu.uci.ics.matthes3.service.api_gateway.core;

import edu.uci.ics.matthes3.service.api_gateway.GatewayService;
import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.api_gateway.threadpool.ClientRequest;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResponseDatabase {

    public static Map<String, Integer> checkResponse(String transactionId) {
        String query =
                "SELECT response, httpstatus " +
                "FROM responses " +
                "WHERE transactionid = ? ";

        try {
            Connection con = GatewayService.getConPool().requestCon();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, transactionId);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                Map<String, Integer> response = new HashMap<String, Integer>();

                String json = rs.getString(1);
                int status = rs.getInt(2);
                response.put(json, status);

                return response;
            }

            GatewayService.getConPool().releaseCon(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ServiceLogger.LOGGER.warning("Unable to find transactionId: " + transactionId);
        return null;
    }

    public static void insert(ClientRequest clientRequest, Response response) {
        String insert_query =
                "INSERT INTO responses (transactionid, email, sessionid, response, httpstatus) VALUES (?,?,?,?,?);";

        try {
            Connection con = GatewayService.getConPool().requestCon();

            PreparedStatement insert_ps = con.prepareStatement(insert_query);

            insert_ps.setString(1, clientRequest.getTransactionID());
            insert_ps.setString(2, clientRequest.getEmail());
            insert_ps.setString(3, clientRequest.getSessionID());
            insert_ps.setString(4, response.readEntity(String.class));
            insert_ps.setInt(5, response.getStatus());

            ServiceLogger.LOGGER.info("Trying query: " + insert_ps.toString());
            insert_ps.execute();

            GatewayService.getConPool().releaseCon(con);

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to insert response: " + response.readEntity(String.class));
            e.printStackTrace();
        }
    }


//    public static void updateRow(String email, String sessionID, String transactionID) {
//        String update_query =
//                "UPDATE responses " +
//                "SET email = ?, sessionid = ? " +
//                "WHERE transactionid = ? ";
//
//        try {
//            PreparedStatement insert_ps = GatewayService.getConPool().requestCon().prepareStatement(update_query);
//
//            insert_ps.setString(1, email);
//            insert_ps.setString(2, sessionID);
//            insert_ps.setString(3, transactionID);
//
//            ServiceLogger.LOGGER.info("Trying query: " + insert_ps.toString());
//            insert_ps.executeUpdate();
//        } catch (SQLException e) {
//            ServiceLogger.LOGGER.warning("Unable to update response: " + transactionID);
//            e.printStackTrace();
//        }
//
//    }

    public static void deleteRow(String transactionID) {
        String delete_query =
                "DELETE FROM responses " +
                "WHERE transactionid = ? ";

        try {

            Connection con = GatewayService.getConPool().requestCon();
            PreparedStatement delete_ps = con.prepareStatement(delete_query);

            delete_ps.setString(1, transactionID);

            ServiceLogger.LOGGER.info("Trying query: " + delete_ps.toString());
            delete_ps.executeUpdate();

            GatewayService.getConPool().releaseCon(con);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to delete response: " + transactionID);
            e.printStackTrace();
        }

    }
}
