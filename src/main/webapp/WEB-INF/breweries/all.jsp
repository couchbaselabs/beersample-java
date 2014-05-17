<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
        <h3>Browse Breweries and Beers</h3>


        <table id="brewery-table" class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${items}" var="item">
                <c:if test="${ item.type == 'brewery' }">
                    <tr>
                        <td colspan="2"><strong><a href="/breweries/show/${item.id}">${item.name}</a></strong></td>
                        <td><a class="btn btn-small btn-danger" href="/breweries/delete/${item.id}">Delete</a>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${ item.type == 'beer' }">
                    <tr>
                        <td></td>
                        <td><a href="/beers/show/${item.id}">${item.name}</a></td>
                        <td>
                            <a class="btn btn-small btn-danger" href="/beers/delete/${item.id}">Delete</a>
                            <a class="btn btn-small btn-warning" href="/beers/edit/${item.id}">Edit</a>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:layout>