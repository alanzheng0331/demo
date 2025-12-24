<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<html>
<head>
    <title>Root数据展示页</title>
    <%-- 样式相关完全不动 --%>
</head>
<body>
<%-- 分块1：Root用户身份判断（仅改这几行，其他完全不动） --%>
<div>
    <h3>用户身份验证</h3>
    <%
        // 核心改动：从Session读取rootUser（替代request），判断是否为root管理员
        String rootUser = (String) session.getAttribute("rootUser");
        Boolean isRoot = "root".equals(rootUser); // 转布尔值适配原有判断逻辑
        // 原有判断逻辑完全不动
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

<hr>

<%-- 分块2：用户表数据展示（完全不动，包括删除按钮） --%>
<div>
    <h3>用户表（t_user）数据展示</h3>
    <%
        List<Map<String, Object>> userDataList = (List<Map<String, Object>>) request.getAttribute("userDataList");
        if (userDataList == null || userDataList.isEmpty()) {
    %>
    <p>暂无用户数据（或数据查询失败）</p>
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
    <p style="color:red;">错误信息：<%= errorMsg %></p>
    <%
        }
    %>
    <%
    } else {
    %>
    <table border="1">
        <thead>
        <tr>
            <th>id</th>
            <%
                Map<String, Object> firstRow = userDataList.get(0);
                for (String key : firstRow.keySet()) {
                    if (!"id".equals(key)) {
            %>
            <th><%= key %></th>
            <%
                    }
                }
            %>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> userMap : userDataList) {
                Object userId = userMap.get("id");
        %>
        <tr>
            <td><%= userId == null ? "" : userId %></td>
            <%
                for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (!"id".equals(key)) {
            %>
            <td><%= value == null ? "" : value %></td>
            <%
                    }
                }
            %>
            <td>
                <!-- 删除按钮完全不动 -->
                <form action="http://localhost:8080/part_time_system_war_exploded/RootDeleteUserSocketServlet" method="post">
                    <input type="hidden" name="id" value="<%= userId %>">
                    <button type="submit">删除</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
        }
    %>
</div>

<hr>

<%-- 分块3：企业表数据展示（完全不动，包括删除按钮） --%>
<div>
    <h3>企业表（t_company）数据展示</h3>
    <%
        List<Map<String, Object>> companyDataList = (List<Map<String, Object>>) request.getAttribute("companyDataList");
        if (companyDataList == null || companyDataList.isEmpty()) {
    %>
    <p>暂无企业数据（或数据查询失败）</p>
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
    <p style="color:red;">错误信息：<%= errorMsg %></p>
    <%
        }
    %>
    <%
    } else {
    %>
    <table border="1">
        <thead>
        <tr>
            <th>id</th>
            <%
                Map<String, Object> firstRow = companyDataList.get(0);
                for (String key : firstRow.keySet()) {
                    if (!"id".equals(key)) {
            %>
            <th><%= key %></th>
            <%
                    }
                }
            %>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> companyMap : companyDataList) {
                Object companyId = companyMap.get("id");
        %>
        <tr>
            <td><%= companyId == null ? "" : companyId %></td>
            <%
                for (Map.Entry<String, Object> entry : companyMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (!"id".equals(key)) {
            %>
            <td><%= value == null ? "" : value %></td>
            <%
                    }
                }
            %>
            <td>
                <!-- 删除按钮完全不动 -->
                <form action="http://localhost:8080/part_time_system_war_exploded/RootDeleteCompanySocketServlet" method="post">
                    <input type="hidden" name="id" value="<%= companyId %>">
                    <button type="submit">删除</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
        }
    %>
</div>

</body>
</html>