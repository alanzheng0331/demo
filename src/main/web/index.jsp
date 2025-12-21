<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>数据库连接测试</title>
</head>
<body>
<h1>数据库连接测试结果</h1>
<%
    // 1. 配置数据库连接信息（根据你的SQL Server实际情况修改）
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // SQL Server驱动类
    String url = "jdbc:sqlserver://localhost:1433;DatabaseName=part_job"; // 数据库URL
    String username = "sa"; // 数据库账号
    String password = "123456"; // 数据库密码

    Connection conn = null;
    try {
        // 2. 加载驱动
        Class.forName(driver);
        // 3. 建立连接
        conn = DriverManager.getConnection(url, username, password);

        out.println("<p style='color:green;'>✅ 数据库连接成功！</p>");
    } catch (ClassNotFoundException e) {
        out.println("<p style='color:red;'>❌ 驱动加载失败：" + e.getMessage() + "</p>");
    } catch (SQLException e) {
        out.println("<p style='color:red;'>❌ 数据库连接失败：" + e.getMessage() + "</p>");
    } finally {
        // 4. 关闭连接
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
%>
</body>
</html>