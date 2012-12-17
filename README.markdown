## Couchbase Beer Tutorial

This tutorial uses Servlets and JSPs in combination with Couchbase
Server 2.0 to display and manage beers and breweries found in the
beer-sample dataset. The code used here is designed to be as portable
as possible, but it may be the case that you need to tweak one or two
things if you have a slightly different version or a customized setup.

### Quick Start

Install Couchbase Server 2.0: http://www.couchbase.com/download

During initial configuration load `beer-sample` dataset. It could be
also loaded later if your node/cluster has free space. More info
http://www.couchbase.com/docs/couchbase-manual-2.0/couchbase-sampledata-beer.html

Add two more views to your bucket. To save prevous design documents
you should first copy your design document from "Production Views" tab
to "Development Views", update it and publish back to production. Here
are two new views you need:

* Design document: `_design/beer`. View: `by_name`

```javascript
function (doc, meta) {
  if(doc.type && doc.type == "beer") {
    emit(doc.name, null);
  }
}
```
* Design document: `_design/brewery`. View: `by_name`

```javascript
function (doc, meta) {
  if(doc.type && doc.type == "brewery") {
    emit(doc.name, null);
  }
}
```

Clone this repo:

    $ git clone git://github.com/couchbaselabs/beersample-java.git
    Cloning into 'beersample-java'...
    remote: Counting objects: 153, done.
    remote: Compressing objects: 100% (92/92), done.
    remote: Total 153 (delta 51), reused 124 (delta 22)
    Receiving objects: 100% (153/153), 81.97 KiB | 120 KiB/s, done.
    Resolving deltas: 100% (51/51), done.
    $ cd beersample-java

Run the application using jetty container:

    $ mvn jetty:run
    .... snip ....
    Dec 17, 2012 1:50:16 PM com.couchbase.beersample.ConnectionManager contextInitialized
    INFO: Connecting to Couchbase Cluster
    2012-12-17 13:50:16.621 INFO com.couchbase.client.CouchbaseConnection:  Added {QA sa=/127.0.0.1:11210, #Rops=0, #Wops=0, #iq=0, topRop=null, topWop=null, toWrite=0, interested=0} to connect queue
    2012-12-17 13:50:16.624 INFO com.couchbase.client.CouchbaseConnection:  Connection state changed for sun.nio.ch.SelectionKeyImpl@2e2a730e
    2012-12-17 13:50:16.635 WARN net.spy.memcached.auth.AuthThreadMonitor:  Incomplete authentication interrupted for node {QA sa=localhost/127.0.0.1:11210, #Rops=0, #Wops=0, #iq=0, topRop=null, topWop=null, toWrite=0, interested=8}
    2012-12-17 13:50:16.662 WARN net.spy.memcached.auth.AuthThread:  Authentication failed to localhost/127.0.0.1:11210
    2012-12-17 13:50:16.662 INFO net.spy.memcached.protocol.binary.BinaryMemcachedNodeImpl:  Removing cancelled operation: SASL auth operation
    2012-12-17 13:50:16.664 INFO net.spy.memcached.auth.AuthThread:  Authenticated to localhost/127.0.0.1:11210
    2012-12-17 13:50:16.666 INFO com.couchbase.client.ViewConnection:  Added localhost to connect queue
    2012-12-17 13:50:16.667 INFO com.couchbase.client.CouchbaseClient:  viewmode property isn't defined. Setting viewmode to production mode
    2012-12-17 13:50:16.866:INFO::Started SelectChannelConnector@0.0.0.0:8080
    [INFO] Started Jetty Server

Navigate to http://localhost:8080/welcome
