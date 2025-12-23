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

<%-- 分块2：数据展示区域 --%>
<div class="block">
    <h3>用户表数据展示</h3>
    <%
        // 从request域获取查询到的用户数据
        List<Map<String, Object>> userDataList = (List<Map<String, Object>>) request.getAttribute("userDataList");
        if (userDataList == null || userDataList.isEmpty()) {
    %>
    <p>暂无数据（或数据查询失败）</p>
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
    <p style="color: red;">错误信息：<%= errorMsg %></p>
    <%
        }
    %>
    <%
    } else {
    %>
    <table>
        <thead>
        <tr>
            <%-- 动态获取表头（根据Map的key，也可固定表头） --%>
            <%
                Map<String, Object> firstRow = userDataList.get(0);
                for (String key : firstRow.keySet()) {
            %>
            <th><%= key %></th>
            <%
                }
            %>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> userMap : userDataList) {
        %>
        <tr>
            <%
                for (Object value : userMap.values()) {
            %>
            <td><%= value == null ? "" : value %></td>
            <%
                }
            %>
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

<%-- 分块3：预留扩展区域（方便后续修改，无需重写整体） --%>
<div class="block">
    <h3>扩展区域</h3>
    <p>后续可添加企业表、XXX表数据展示等功能</p>
</div>
</body>
</html>