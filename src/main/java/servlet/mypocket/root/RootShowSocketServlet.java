package servlet.mypocket.root;

import connection.mypocket.AllOfImpl; // 新增：导入你的真实实现类
import connection.model.RootImplModel;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RootShowSocketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 处理编码，避免中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 声明数据变量
        List<Map<String, Object>> userDataList = null;
        try {
            // 2. 获取RootImplModel的真实实现类对象（现在用AllOfImpl）
            RootImplModel rootModel = getRootImplModelInstance();

            // 3. 调用RootShow()方法获取用户表所有数据（此时会调用AllOfImpl的RootShow，进而调用RootShowDao的数据库逻辑）
            userDataList = rootModel.RootShow();

        } catch (Exception e) {
            // 异常处理：打印异常信息，存入request供JSP展示
            e.printStackTrace();
            request.setAttribute("errorMsg", "数据查询失败：" + e.getMessage());
        }

        // 4. 将查询到的数据存入request域，供JSP页面获取
        request.setAttribute("userDataList", userDataList);

        // 5. 处理Session中的用户名，判断是否为root（你注释掉的部分可以按需恢复）
        // 恢复后JSP就能拿到isRoot标识，判断是否是root用户
        HttpSession session = request.getSession(false); // false：不存在则返回null，不新建
        String username = session != null ? (String) session.getAttribute("username") : null;
        boolean isRoot = "root".equals(username); // 严格判断是否为root用户
        request.setAttribute("isRoot", isRoot); // 存入request域，供JSP使用

        // 6. 请求转发到指定JSP页面（路径对应你的项目部署路径）
        request.getRequestDispatcher("/mypocket/rootShow.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // GET请求复用POST逻辑
    }

    /**
     * 封装RootImplModel实现类的实例化（现在替换为你的真实实现类AllOfImpl）
     * @return RootImplModel实例（实际是AllOfImpl对象）
     */
    private RootImplModel getRootImplModelInstance() {
        // 直接返回AllOfImpl的实例（这是你的真实实现类，实现了RootImplModel接口）
        return new AllOfImpl();
    }
}