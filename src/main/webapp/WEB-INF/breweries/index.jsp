<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
 <h3>Browse Breweries</h3>

     <form class="navbar-search pull-left">
        <input id="brewery-search" type="text" class="search-query"
               placeholder="Search for Breweries">
    </form>

            <table id="brewery-table" class="table table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${breweries}" var="brewery">
                        <tr>
                            <td><a href="/breweries/show/${brewery.id}">${brewery.name}</a></td>
                            <td><a class="btn btn-small btn-danger"
                                   href="/breweries/delete/${brewery.id}">Delete</a>
                            </td>
                        </tr>
                      </c:forEach>
                </tbody>
            </table>
    </jsp:body>
</t:layout>