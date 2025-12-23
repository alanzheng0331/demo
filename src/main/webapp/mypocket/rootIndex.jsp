<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>根用户首页</title>
</head>
<body>
<%
    String rootUser = (String) session.getAttribute("rootUser");
    if (rootUser == null) {
    // 未登录，重定向到登录页
    response.sendRedirect(request.getContextPath() + "/mypocket/rootlogin.html");
    return;
}
%>
<h1>欢迎您，<%= rootUser %>！</h1>
</body>
</html>