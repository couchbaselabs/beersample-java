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
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.InvalidViewException;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This servlet is just a servet that is used to install the view
 */
public class InstallViewServlet extends HttpServlet{

    public final static String BEER_DESIGN_DOC_NAME = "beer";
    public final static String BREWERY_DESIGN_DOC_NAME = "brewery";
    public final static String BY_NAME_VIEW_NAME = "by_name";
    public final static String ALL_WITH_BEERS_VIEW_NAME = "all_with_beers";



    /**
     * Obtains the current CouchbaseClient connection.
     */
    final CouchbaseClient client = ConnectionManager.getInstance();


    /**
     * Install views if they are not present
     *
     * @param request the servlet request instance.
     * @param response the servlet response instance.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        installBeerViews();
        installBreweryViews();

        response.sendRedirect("/");
    }


    private void installBeerViews() {

        boolean installView = false;
        try {
        View view = client.getView(BEER_DESIGN_DOC_NAME, BY_NAME_VIEW_NAME);
        } catch (InvalidViewException e) {
            installView = true;
        }

        if (installView) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Installing Beer views....");
            DesignDocument designDoc = new DesignDocument(BEER_DESIGN_DOC_NAME);
            String viewName = BY_NAME_VIEW_NAME;
            String mapFunction =
                    "function (doc, meta) {\n" +
                            "  if(doc.type && doc.type == \"beer\") {\n" +
                            "    emit(doc.name);\n" +
                            "  }\n" +
                            "}";
            ViewDesign viewDesign = new ViewDesign(viewName, mapFunction);
            designDoc.getViews().add(viewDesign);
            client.createDesignDoc(designDoc);
        }
    }

    private void installBreweryViews() {

        boolean installView = false;
        try {
            View view = client.getView(BREWERY_DESIGN_DOC_NAME, BY_NAME_VIEW_NAME);
            view = client.getView(BREWERY_DESIGN_DOC_NAME, ALL_WITH_BEERS_VIEW_NAME);
        } catch (InvalidViewException e) {
            installView = true;
        }

        if (installView) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Installing Brewery views....");
            DesignDocument designDoc = new DesignDocument(BREWERY_DESIGN_DOC_NAME);

            // add brewery by name
            String viewName = BY_NAME_VIEW_NAME;
            String mapFunctionName =
                    "function (doc, meta) {\n" +
                            "  if(doc.type && doc.type == \"brewery\") {\n" +
                            "    emit(doc.name);\n" +
                            "  }\n" +
                            "}";
            ViewDesign viewNameDesign = new ViewDesign(viewName, mapFunctionName);
            designDoc.getViews().add(viewNameDesign);

            // add brewery and beer (collated views)
            String viewBreweryAndBeer = "all_with_beers";
            String mapFunctionBreweryAndBeer =
                    "function(doc, meta) {\n" +
                            "  switch(doc.type) {\n" +
                            "    case \"brewery\":\n" +
                            "      emit([meta.id, 0, doc.name]);\n" +
                            "      break;\n" +
                            "    case \"beer\":\n" +
                            "      if (doc.name && doc.brewery_id) {\n" +
                            "        emit([doc.brewery_id, 1, doc.name], null);\n" +
                            "      }\n" +
                            "  }\n" +
                            "}";
            ViewDesign viewCollatedDesign = new ViewDesign(viewBreweryAndBeer, mapFunctionBreweryAndBeer);
            designDoc.getViews().add(viewCollatedDesign);

            // save the new views in the design doc
            client.createDesignDoc(designDoc);
        }
    }


    public static String printNoViewMessage() {
        return "<p>View are not installed; click <a href='/admin/install-views'>here</a> to install them.</p>";
    }

}
