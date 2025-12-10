package DAO_FOR_MYSQL_20252160A0925;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }



    private void SQL(){
        //如果你们用的是 sqlserver
        //首先要 下载  sqljdbc_13.2.1.0_chs.zip 的文件 如果是Maven 要下载对应的依赖
            //<dependency>
            //    <groupId>com.microsoft.sqlserver</groupId>
            //    <artifactId>mssql-jdbc</artifactId>
            //    <version>12.4.0.jre11</version>   <!-- 或 12.4.0.jre8 如果你坚持 JDK 8 -->
            //</dependency>
        //然后 把你的用户名 改好 权限打开
        //推荐网站https://blog.csdn.net/dongmuxg/article/details/118963669
        String url =
                "jdbc:sqlserver://localhost:1433;databaseName=TestSQL;"//databaseName=TestSQL 一定要存在
                        + "encrypt=true;trustServerCertificate=true;"; // 开发/内网快速通过
        String user = "sa";          // 1. 换成你的账号
        String pwd  = "Sa123456";      // 2. 换成你的密码

        /* 3. 自动关闭资源写法（try-with-resources） */
        try (Connection conn = DriverManager.getConnection(url, user, pwd);
             Statement  st  = conn.createStatement();
             ResultSet  rs  = st.executeQuery("select top 5 id from TEST_TABLE")) {
            //TEST_TABLE换成你们自己的字段
            while (rs.next()) {
                System.out.printf("id=%d",
                        rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // 这里再报错就是账号/表/网络问题
        }
    }
}
