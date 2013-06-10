<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
        <h3>Search Beer and Brewery Database</h3>


        <form class="pull-left" action="/search" method="get">
            <input id="q" name="q" type="text" class="search-query"
                   placeholder="Search" value="${query}">
        </form>

        <table id="beer-table" class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Type</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${results}" var="item">
                <tr>
                    <c:if test = "${item.type == 'beer'}">
                        <td><a href="/beers/show/${item.id}">${item.name}</a></td>
                        </td>
                        <td style="text-transform: capitalize;">${item.type}</td>
                        <td><a class="btn btn-small btn-warning"
                               href="/beers/edit/${item.id}">Edit</a>
                            <a class="btn btn-small btn-danger"
                               href="/beers/delete/${item.id}">Delete</a>
                        </td>
                    </c:if>
                    <c:if test = "${item.type == 'brewery'}">
                        <td><a href="/breweries/show/${item.id}">${item.name}</a></td>
                        </td>
                        <td style="text-transform: capitalize;">${item.type}</td>
                        <td><a class="btn btn-small btn-danger"
                               href="/breweries/delete/${items.id}">Delete</a>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:layout>