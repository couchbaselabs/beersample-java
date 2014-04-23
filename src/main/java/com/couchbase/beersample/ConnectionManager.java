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
