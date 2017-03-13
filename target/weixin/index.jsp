<%--
  Created by IntelliJ IDEA.
  User: jjtx
  Date: 2016/9/18
  Time: 下午 6:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>

    <form action="${pageContext.request.contextPath}/arduinoServlet.action" method="post" enctype="multipart/form-data">
        <input type="file"/><input type="submit">
    </form>

  </body>
</html>
