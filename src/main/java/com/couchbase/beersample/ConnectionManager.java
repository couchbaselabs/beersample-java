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
import com.couchbase.client.protocol.views.ViewDesign;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The ConnectionManager handles connecting, disconnecting and managing
 * of the Couchbase connection.
 *
 * To get the connection from a Servlet context, use the getInstance()
 * method.
 */
public class ConnectionManager implements ServletContextListener {

  /**
   * Holds the connected Couchbase instance.
   */
  private static CouchbaseClient client;

  /**
   * The Logger to use.
   */
  private static final Logger logger = Logger.getLogger(
    ConnectionManager.class.getName());

  private DesignDocument getDesignDocument(String name) {
    try {
        return client.getDesignDoc(name);
    } catch (com.couchbase.client.protocol.views.InvalidViewException e) {
        return new DesignDocument(name);
    }
  }

  /**
   * Connect to Couchbase when the Server starts.
   *
   * @param sce the ServletContextEvent (not used here).
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    String cluster = System.getProperty("com.couchbase.beersample.cluster", "http://127.0.0.1:8091/pools");
    logger.log(Level.INFO, String.format("Connecting to Couchbase Cluster [%s]", cluster));
    ArrayList<URI> nodes = new ArrayList<URI>();
    nodes.add(URI.create(cluster));
    try {
      client = new CouchbaseClient(nodes, "beer-sample", "");
    } catch (IOException ex) {
      logger.log(Level.SEVERE, ex.getMessage());
    }

    logger.log(Level.INFO, "Trying to verify the views");
    try {
        DesignDocument designDoc = getDesignDocument("beer");
        boolean found = false;
        for (ViewDesign view : designDoc.getViews()) {
            if (view.getMap() == "by_name") {
                found = true;
                break;
            }
        }

        if (!found) {
            ViewDesign design = new ViewDesign("by_name", "function (doc, meta) {\n" +
                    "  if(doc.type && doc.type == \"beer\") {\n" +
                    "    emit(doc.name, null);\n" +
                    "  }\n" +
                    "}");
            designDoc.getViews().add(design);
            client.createDesignDoc(designDoc);
        }

        designDoc = getDesignDocument("brewery");
        found = false;
        for (ViewDesign view : designDoc.getViews()) {
            if (view.getMap() == "by_name") {
                found = true;
                break;
            }
        }

        if (!found) {
            ViewDesign design = new ViewDesign("by_name", "function (doc, meta) {\n" +
                    "  if(doc.type && doc.type == \"brewery\") {\n" +
                    "    emit(doc.name, null);\n" +
                    "  }\n" +
                    "}");
            designDoc.getViews().add(design);
            client.createDesignDoc(designDoc);
        }
    } catch (Exception e) {
        logger.log(Level.WARNING, "An error occurred", e);

    }
  }

  /**
   * Disconnect from Couchbase when the Server shuts down.
   *
   * @param sce the ServletContextEvent (not used here).
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.log(Level.INFO, "Disconnecting from Couchbase Cluster");
    client.shutdown();
  }

  /**
   * Returns the current CouchbaseClient object.
   *
   * @return the current Couchbase connection.
   */
  public static CouchbaseClient getInstance() {
    return client;
  }

}
