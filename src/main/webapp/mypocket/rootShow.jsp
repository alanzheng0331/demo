<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<html>
<head>
    <title>Root数据展示页（测试）</title>
    <!-- 简单样式，仅辅助表格展示（符合“不许渲染”要求，仅基础样式） -->
    <style>
        table {border-collapse: collapse; width: 80%; margin: 20px auto;}
        th, td {border: 1px solid #333; padding: 8px; text-align: center;}
        .block {margin: 20px; padding: 10px; border: 1px solid #eee;}
        h3 {color: #333; margin-left: 20px;}
        .error {color: red; margin-left: 20px;}
        /* 删除按钮基础样式 */
        .del-btn {
            padding: 4px 8px;
            background-color: #ff4444;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 12px;
            border: none;
            cursor: pointer;
        }
        .del-btn:hover {
            background-color: #cc0000;
        }
    </style>
</head>
<body>
<%-- 分块1：Root用户身份判断 --%>
<div class="block">
    <h3>用户身份验证</h3>
    <%
        // 从request域获取是否为root的标识（也可直接从Session获取username判断）
        Boolean isRoot = (Boolean) request.getAttribute("isRoot");
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

<%-- 分块2：用户表数据展示 --%>
<div class="block">
    <h3>用户表（t_user）数据展示</h3>
    <%
        // 从request域获取查询到的用户数据
        List<Map<String, Object>> userDataList = (List<Map<String, Object>>) request.getAttribute("userDataList");
        if (userDataList == null || userDataList.isEmpty()) {
    %>
    <p>暂无用户数据（或数据查询失败）</p>
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
    <p class="error">错误信息：<%= errorMsg %></p>
    <%
        }
    %>
    <%
    } else {
    %>
    <table>
        <thead>
        <tr>
            <%-- 动态获取表头（根据Map的key） --%>
            <%
                Map<String, Object> firstRow = userDataList.get(0);
                for (String key : firstRow.keySet()) {
            %>
            <th><%= key %></th>
            <%
                }
            %>
            <th>操作</th> <%-- 新增：操作列表头 --%>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> userMap : userDataList) {
                // 获取用户主键ID（确保Map中包含数据库表的id字段）
                Object userId = userMap.get("id");
        %>
        <tr>
            <%
                for (Object value : userMap.values()) {
            %>
            <td><%= value == null ? "" : value %></td>
            <%
                }
            %>
            <%-- 新增：每行末尾添加删除按钮 --%>
            <td>
                <%-- 方式1：GET请求（简单直接，路径可自定义） --%>
                <a href="<%= request.getContextPath() %>/mypocket/root/deleteUserServlet?userId=<%= userId %>" class="del-btn">删除</a>

                <%-- 方式2：POST请求（更安全，可替换上面的a标签，需配合Servlet处理POST）
                <form action="<%= request.getContextPath() %>/mypocket/root/deleteUserServlet" method="post" style="display: inline;">
                    <input type="hidden" name="userId" value="<%= userId %>">
                    <button type="submit" class="del-btn">删除</button>
                </form>
                --%>
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

<%-- 分块3：企业表数据展示（替换原预留扩展区域） --%>
<div class="block">
    <h3>企业表（t_company）数据展示</h3>
    <%
        // 从request域获取查询到的企业数据
        List<Map<String, Object>> companyDataList = (List<Map<String, Object>>) request.getAttribute("companyDataList");
        if (companyDataList == null || companyDataList.isEmpty()) {
    %>
    <p>暂无企业数据（或数据查询失败）</p>
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
    <p class="error">错误信息：<%= errorMsg %></p>
    <%
        }
    %>
    <%
    } else {
    %>
    <table>
        <thead>
        <tr>
            <%-- 动态获取企业表表头（根据Map的key） --%>
            <%
                Map<String, Object> firstRow = companyDataList.get(0);
                for (String key : firstRow.keySet()) {
            %>
            <th><%= key %></th>
            <%
                }
            %>
            <th>操作</th> <%-- 新增：操作列表头 --%>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> companyMap : companyDataList) {
                // 获取企业主键ID（确保Map中包含数据库表的id字段）
                Object companyId = companyMap.get("id");
        %>
        <tr>
            <%
                for (Object value : companyMap.values()) {
            %>
            <td><%= value == null ? "" : value %></td>
            <%
                }
            %>
            <%-- 新增：每行末尾添加删除按钮 --%>
            <td>
                <%-- 方式1：GET请求（简单直接，路径可自定义） --%>
                <a href="<%= request.getContextPath() %>/mypocket/root/deleteCompanyServlet?companyId=<%= companyId %>" class="del-btn">删除</a>

                <%-- 方式2：POST请求（更安全，可替换上面的a标签）
                <form action="<%= request.getContextPath() %>/mypocket/root/deleteCompanyServlet" method="post" style="display: inline;">
                    <input type="hidden" name="companyId" value="<%= companyId %>">
                    <button type="submit" class="del-btn">删除</button>
                </form>
                --%>
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