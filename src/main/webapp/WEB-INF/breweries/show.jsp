<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:layout>
    <jsp:body>
        <h3>Show Details for "${brewery.name}"</h3>
            <table class="table table-striped">
                <tbody>
                    <c:forEach items="${brewery}" var="item">
                        <tr>
                            <td><strong>${item.key}</strong></td>
                            <td>${item.value}</td>
                        </tr>
                      </c:forEach>
                </tbody>
            </table>
    </jsp:body>
</t:layout>