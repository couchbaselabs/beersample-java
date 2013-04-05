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
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The SearchServlet handles full text search query using Elasticsearch.
 *
 */
public class SearchServlet extends HttpServlet {

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
     * Since the /breweries/* routes are wildcarded and will all end up here, the
     * method needs to check agains the PATH (through getPathInfo()) and
     * determine which helper method should be called. The helper method then
     * does the actual request and response handling.
     *
     * @param request the HTTP request object.
     * @param response the HTTP response object.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleSearch(request, response);
    }

    /**
     * Handle the full text search action.
     *
     * Based Elasticsearch query
     *
     * @param request the HTTP request object.
     * @param response the HTTP response object.
     * @throws IOException
     * @throws ServletException
     */
    private void handleSearch(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        String query =  request.getParameter("q");
        if (query == null) {

        }  else {
            Client elasticSearchClient = new TransportClient()
                    .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
            SearchResponse searchResponse = elasticSearchClient.prepareSearch("beer-sample")
                    .setQuery(QueryBuilders.queryString(query))
                    .execute()
                    .actionGet();

            ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
            for (SearchHit hit : searchResponse.getHits()) {
                HashMap<String, String> parsedDoc = gson.fromJson(
                        (String) client.get(hit.getId()), HashMap.class);
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("id", hit.getId());
                item.put("name", parsedDoc.get("name"));
                item.put("type", parsedDoc.get("type"));
                results.add(item);
            }
            request.setAttribute("results", results);
            request.setAttribute("query", query);
        }
        request.getRequestDispatcher("/WEB-INF/search/index.jsp").forward(request, response);
    }

}
