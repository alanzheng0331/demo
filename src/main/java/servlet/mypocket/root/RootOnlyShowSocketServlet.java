package servlet.mypocket.root;

import connection.model.RootImplModel;
import connection.mypocket.AllOfImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RootOnlyShowSocketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 处理编码，避免中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 声明数据变量：用户数据 + 企业数据
        List<Map<String, Object>> userDataList = null;
        List<Map<String, Object>> companyDataList = null; // 新增：企业数据变量

        try {
            // 2. 获取RootImplModel的真实实现类对象（现在用AllOfImpl）
            RootImplModel rootModel = getRootImplModelInstance();

            // 3. 调用方法获取数据
            userDataList = rootModel.RootShowUser(); // 用户表数据
            companyDataList = rootModel.RootShowCompany(); // 新增：企业表数据（直接调用已实现的方法）

        } catch (Exception e) {
            // 异常处理：打印异常信息，存入request供JSP展示
            e.printStackTrace();
            request.setAttribute("errorMsg", "数据查询失败：" + e.getMessage());
        }

        // 4. 将查询到的数据存入request域，供JSP页面获取
        request.setAttribute("userDataList", userDataList); // 用户数据
        request.setAttribute("companyDataList", companyDataList); // 新增：企业数据

        // 5. 处理Session中的用户名，判断是否为root
//        HttpSession session = request.getSession(false); // false：不存在则返回null，不新建
//        String username = session != null ? (String) session.getAttribute("username") : null;
//        boolean isRoot = "root".equals(username); // 严格判断是否为root用户
//        request.setAttribute("isRoot", isRoot); // 存入request域，供JSP使用

        // 6. 请求转发到指定JSP页面（路径对应你的项目部署路径）
        request.getRequestDispatcher("mypocket/rootOnlyShow.jsp").forward(request, response);
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