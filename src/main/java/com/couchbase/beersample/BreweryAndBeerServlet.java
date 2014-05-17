package com.couchbase.beersample;


import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The BreweryServlet handles all Brewery-related HTTP Queries.
 *
 * The BreweryServlet is used to handle all HTTP queries under the /breweries
 * namespace. The "web.xml" defines a wildcard route for every /breweries/*
 * route, so the doGet() method needs to determine what should be dispatched.
 */
public class BreweryAndBeerServlet extends HttpServlet {

    /**
     * Obtains the current CouchbaseClient connection.
     */
    final CouchbaseClient client = ConnectionManager.getInstance();

    /**
     * Google GSON is used for JSON encoding/decoding.
     */
    final Gson gson = new Gson();

    /**
     * Dispatch all incoming GET HTTP requests.
     *
     * @param request the HTTP request object.
     * @param response the HTTP response object.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            if(request.getPathInfo() == null) {
                handleIndex(request, response);
            }
    }

    private void handleIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            View view = client.getView("brewery", "all_with_beers");
            Query query = new Query();
            query.setIncludeDocs(true).setLimit(100);
            ViewResponse result = client.query(view, query);

            ArrayList<HashMap<String, String>> items =
                    new ArrayList<HashMap<String, String>>();
            for (ViewRow row : result) {
                HashMap<String, String> parsedDoc = gson.fromJson(
                        (String) row.getDocument(), HashMap.class);

                HashMap<String, String> item = new HashMap<String, String>();
                item.put("id", row.getId());
                item.put("name", parsedDoc.get("name"));
                item.put("type", parsedDoc.get("type"));


                items.add(item);
            }
            request.setAttribute("items", items);

            request.getRequestDispatcher("/WEB-INF/breweries/all.jsp")
                    .forward(request, response);
        } catch (InvalidViewException e) {
            response.getWriter().print( InstallViewServlet.printNoViewMessage() );
        }
    }


}
