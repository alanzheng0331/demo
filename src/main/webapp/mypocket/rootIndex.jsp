<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>根用户首页</title>
</head>
<body>
<%
    // 仅修正：从Session读取rootUser（原有逻辑不变，仅格式化）
    String rootUser = (String) session.getAttribute("rootUser");
    if (rootUser == null) {
        // 未登录，重定向到登录页（原有逻辑完全不动）
        response.sendRedirect(request.getContextPath() + "/mypocket/rootlogin.html");
        return;
    }
%>
<!-- 其他功能（isUser读取、展示）完全不动 -->
<h1>欢迎您，<%= rootUser %>！<% String isUser = (String) session.getAttribute("isUser"); %><%= isUser %></h1>
</body>
</html>