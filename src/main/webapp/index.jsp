<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Document</title>
</head>
<body>
http://localhost:8080/part_time_system_war_exploded/RootOnlyShowSocketServlet<br/>
<a href="mypocket/index.html">mypocket/Login.html</a><br/>
<a href="mypocket/rootLogin.html">mypocket/rootLogin.html</a><br/>
<a href="RootShowSocketServlet">/RootShowSocketServlet</a><br/>
<a href="RootOnlyShowSocketServlet">/RootOnlyShowSocketServlet</a><br/>

<div>
    <h3>用户身份验证</h3>
    <%
    Boolean isRoot = (Boolean) request.getAttribute("rootUser");
    if (isRoot != null && isRoot) {
    %>
    <p>当前用户是：Root管理员</p>
    <%
    } else {
    %>
    <p>当前用户不是：Root管理员</p>
    <%
    }
    %>
</div>
</body>
</html>