<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
    <h3>Browse Beers</h3>

    <form class="navbar-search pull-left">
        <input id="beer-search" type="text" class="search-query"
               placeholder="Search for Beers">
    </form>

            <table id="beer-table" class="table table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Brewery</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${beers}" var="beer">
                        <tr>
                            <td><a href="/beers/show/${beer.id}">${beer.name}</a></td>
                            <td><a href="/breweries/show/${beer.brewery}">To Brewery</a></td>
                            <td><a class="btn btn-small btn-warning"
                                   href="/beers/edit/${beer.id}">Edit</a>
                                <a class="btn btn-small btn-danger"
                                   href="/beers/delete/${beer.id}">Delete</a>
                            </td>
                        </tr>
                      </c:forEach>
                </tbody>
            </table>
    </jsp:body>
</t:layout>