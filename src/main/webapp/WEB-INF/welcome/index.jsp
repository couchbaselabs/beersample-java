<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Couchbase Java Beer-Sample</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="The Couchbase Java Beer-Sample App">
    <meta name="author" content="Couchbase, Inc. 2012">

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/beersample.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.min.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container-narrow">
      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li class="active"><a href="#">Home</a></li>
          <li><a href="/beers">Beers</a></li>
          <li><a href="/breweries">Breweries</a></li>
          <li><a href="/map">Map</a></li>
        </ul>
        <h2 class="muted">Couchbase Beer-Sample</h2>
      </div>
      <hr>
      <div class="row-fluid">
        <div class="span6">
          <div class="span12">
            <h4>Browse all Beers</h4>
            <a href="/beers" class="btn btn-warning">Show me all beers</a>
            <hr />
          </div>
          <div class="span12">
            <h4>Browse all Breweries</h4>
            <a href="/breweries" class="btn btn-info">Take me to the breweries</a>
            <hr />
          </div>
          <div class="span12">
            <h4>Find Breweries near you</h4>
            <a href="/map" class="btn btn-success">Jump to the map</a>
          </div>
        </div>
        <div class="span6">
          <div class="span12">
            <h4>About this App</h4>
            <p>Welcome to Couchbase!</p>
            <p>This application helps you to get started on application
                development with Couchbase. It shows how to create, update and
                delete documents and how to work with JSON documents.</p>
            <p>The official tutorial can be found
                <a href="http://www.couchbase.com/docs/couchbase-sdk-java-1.1/tutorial.html">here</a>!</p>
          </div>
        </div>
      </div>
      <hr>
      <div class="footer">
        <p>&copy; Couchbase, Inc. 2012</p>
      </div>
    </div>
    <script src="http://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/beersample.js"></script>
  </body>
</html>