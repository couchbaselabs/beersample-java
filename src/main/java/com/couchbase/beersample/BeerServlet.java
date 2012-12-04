/**
 * Copyright (C) 2009-2012 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.couchbase.beersample;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.spy.memcached.internal.OperationFuture;

/**
 * The BeerServlet handles all Beer-related HTTP Queries.
 *
 * The BeerServlet is used to handle all HTTP queries under the /beers
 * namespace. The "web.xml" defines a wildcard route for every /beers/*
 * route, so the doGet() method needs to determine what should be dispatched.
 */
public class BeerServlet extends HttpServlet {

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
   * Since the /beers/* routes are wildcarded and will all end up here, the
   * method needs to check agains the PATH (through getPathInfo()) and
   * determine which helper method should be called. The helper method then
   * does the actual request and response handling.
   *
   * @param request the HTTP request object.
   * @param response the HTTP response object.
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    try {
      if(request.getPathInfo() == null) {
        handleIndex(request, response);
      } else if(request.getPathInfo().startsWith("/show")) {
        handleShow(request, response);
      } else if(request.getPathInfo().startsWith("/delete")) {
        handleDelete(request, response);
      }
    } catch (InterruptedException ex) {
      Logger.getLogger(BeerServlet.class.getName()).log(
        Level.SEVERE, null, ex);
    } catch (ExecutionException ex) {
      Logger.getLogger(BeerServlet.class.getName()).log(
        Level.SEVERE, null, ex);
    }
  }

  /**
   * Handle the /beers action.
   *
   * Based on a defined Couchbase View (beer/brewery_beers), the beers are
   * loaded, arranged and passed to the JSP layer. Google GSON is used to
   * handle the JSON encoding/decoding.
   *
   * @param request the HTTP request object.
   * @param response the HTTP response object.
   * @throws IOException
   * @throws ServletException
   */
  private void handleIndex(HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {

    View view = client.getView("beer", "brewery_beers");
    Query query = new Query();
    query.setStale(Stale.FALSE).setIncludeDocs(true).setLimit(20);
    ViewResponse result = client.query(view, query);

    ArrayList<HashMap<String, String>> beers =
      new ArrayList<HashMap<String, String>>();
    for(ViewRow row : result) {
      ArrayList<String> parsedKey = gson.fromJson(
        row.getKey(), ArrayList.class);
      HashMap<String, String> parsedDoc = gson.fromJson(
        (String)row.getDocument(), HashMap.class);

      if(parsedKey.size() == 2) {
        HashMap<String, String> beer = new HashMap<String, String>();
        beer.put("id", row.getId());
        beer.put("name", parsedDoc.get("name"));
        beer.put("brewery", parsedDoc.get("brewery_id"));
        beers.add(beer);
      }
    }
    request.setAttribute("beers", beers);

    request.getRequestDispatcher("/WEB-INF/beers/index.jsp")
      .forward(request, response);
  }

  /**
   * Handle the /beers/show/<BEER-ID> action
   *
   * This method loads up a document based on the given beer id.
   *
   * @param request the HTTP request object.
   * @param response the HTTP response object.
   * @throws IOException
   * @throws ServletException
   */
  private void handleShow(HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {

    String beerId = request.getPathInfo().split("/")[2];
    String document = (String) client.get(beerId);
    if(document != null) {
      HashMap<String, String> beer = gson.fromJson(document, HashMap.class);
      request.setAttribute("beer", beer);
    }

    request.getRequestDispatcher("/WEB-INF/beers/show.jsp")
      .forward(request, response);
  }

  /**
   * Handle the /beers/delete/<BEER-ID> action
   *
   * This method deletes a beer based on the given beer id.
   *
   * @param request the HTTP request object.
   * @param response the HTTP response object.
   * @throws IOException
   * @throws ServletException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  private void handleDelete(HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException,
    InterruptedException,
    ExecutionException {

    String beerId = request.getPathInfo().split("/")[2];
    OperationFuture<Boolean> delete = client.delete(beerId);

    if(delete.get()) {
      response.sendRedirect("/beers");
    }
  }

}
