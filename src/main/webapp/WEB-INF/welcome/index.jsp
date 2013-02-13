<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
        <div class="span6">
            <div class="span12">
                <h4>Browse all Beers</h4>
                <a href="/beers" class="btn btn-warning">Show me all beers</a>
                <hr/>
            </div>
            <div class="span12">
                <h4>Browse all Breweries</h4>
                <a href="/breweries" class="btn btn-info">Take me to the breweries</a>
                <hr/>
            </div>
            <div class="span12">
                <h4>Browse all Breweries and Beers</h4>
                <a href="/all" class="btn btn-primary">Take me to the all records</a>
                <hr/>
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
    </jsp:body>
</t:layout>