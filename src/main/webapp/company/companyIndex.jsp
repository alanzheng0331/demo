<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<%
    // 统一编码设置
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    // 1. 校验企业登录状态（关键修正：Session的key要和Servlet中一致，Servlet存的是"company_name"）
    String companyName = (String) session.getAttribute("company_name");
    // 解码避免中文乱码
    if (companyName != null && !companyName.trim().isEmpty()) {
        companyName = URLDecoder.decode(companyName, "UTF-8");
    }
    boolean isLogin = (companyName != null && !companyName.trim().isEmpty());

    // 2. 获取项目上下文路径（适配不同部署环境，避免路径错误）
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html> <!-- 恢复DOCTYPE，保证页面渲染标准 -->
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>易兼职 - 企业端首页</title>
    <style>
        /* 全局样式重置 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: "Microsoft Yahei", sans-serif;
        }

        body {
            background-color: #f8f9fa;
            color: #333;
            line-height: 1.6;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        ul {
            list-style: none;
        }

        button {
            cursor: pointer;
            border: none;
            outline: none;
            transition: all 0.3s ease;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 0 auto;
        }

        /* 顶部导航栏 - 保留原版样式 */
        .navbar {
            background-color: #2d3e50;
            color: #ffffff;
            padding: 15px 0;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            position: sticky;
            top: 0;
            z-index: 999;
        }

        .navbar-wrap {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo h1 {
            font-size: 26px;
            font-weight: 700;
            letter-spacing: 2px;
        }

        .nav-menu {
            display: flex;
            gap: 30px;
        }

        .nav-item a {
            font-size: 16px;
            font-weight: 500;
            padding: 5px 0;
            border-bottom: 2px solid transparent;
        }

        .nav-item a:hover, .nav-item a.active {
            border-bottom: 2px solid #3498db;
            color: #3498db;
        }

        .auth-btns {
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .login-btn {
            background-color: transparent;
            color: #ffffff;
            font-size: 15px;
            padding: 6px 12px;
            border: 1px solid #3498db;
            border-radius: 4px;
        }

        .login-btn:hover {
            background-color: #3498db;
            color: #fff;
        }

        .register-btn {
            background-color: #3498db;
            color: #ffffff;
            font-size: 15px;
            padding: 6px 12px;
            border-radius: 4px;
        }

        .register-btn:hover {
            background-color: #2980b9;
        }

        /* 登录后显示的企业信息样式 */
        .company-info {
            font-size: 15px;
            color: #3498db;
            font-weight: 500;
        }

        .logout-btn {
            background-color: #e74c3c;
            color: #ffffff;
            font-size: 15px;
            padding: 6px 12px;
            border-radius: 4px;
        }

        .logout-btn:hover {
            background-color: #c0392b;
        }

        /* 横幅区域 - 保留原版样式 */
        .banner {
            background: linear-gradient(rgba(45, 62, 80, 0.8), rgba(45, 62, 80, 0.9)),
            url("https://picsum.photos/1920/600?random=10") center/cover no-repeat;
            color: #ffffff;
            padding: 80px 0;
            text-align: center;
            border-radius: 8px;
            margin: 20px 0;
        }

        .banner-title {
            font-size: 36px;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .banner-desc {
            font-size: 18px;
            max-width: 800px;
            margin: 0 auto 30px;
            opacity: 0.9;
        }

        .banner-btn {
            background-color: #e67e22;
            color: #ffffff;
            font-size: 18px;
            padding: 12px 30px;
            border-radius: 6px;
            font-weight: 600;
        }

        .banner-btn:hover {
            background-color: #d35400;
            transform: translateY(-2px);
        }

        /* 核心优势区域 - 保留样式 */
        .advantage {
            padding: 50px 0;
        }

        .section-title {
            text-align: center;
            font-size: 30px;
            font-weight: 700;
            color: #2d3e50;
            margin-bottom: 40px;
            position: relative;
        }

        .section-title::after {
            content: "";
            display: block;
            width: 80px;
            height: 3px;
            background-color: #3498db;
            margin: 10px auto 0;
        }

        .advantage-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
            max-width: 900px;
            margin: 0 auto;
        }

        .advantage-link {
            display: block;
            width: 100%;
            height: 100%;
        }

        .advantage-item {
            background-color: #ffffff;
            padding: 30px 20px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
            text-align: center;
            transition: transform 0.3s ease;
            height: 100%;
        }

        .advantage-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .advantage-icon {
            font-size: 40px;
            color: #3498db;
            margin-bottom: 20px;
        }

        .advantage-title {
            font-size: 20px;
            font-weight: 600;
            color: #2d3e50;
            margin-bottom: 15px;
        }

        .advantage-desc {
            font-size: 14px;
            color: #7f8c8d;
        }

        /* 快速操作区域 - 保留原版样式 */
        .quick-op {
            padding: 40px 0;
            background-color: #f1f5f9;
            border-radius: 8px;
            margin: 30px 0;
        }

        .op-list {
            display: flex;
            justify-content: center;
            gap: 40px;
            flex-wrap: wrap;
        }

        .op-item {
            text-align: center;
            cursor: pointer; /* 增加鼠标指针样式，提示可点击 */
        }

        .op-icon {
            width: 80px;
            height: 80px;
            background-color: #3498db;
            color: #fff;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 30px;
            margin: 0 auto 15px;
        }

        .op-title {
            font-size: 18px;
            font-weight: 600;
            color: #2d3e50;
        }

        /* 底部区域 - 保留原版样式 */
        .footer {
            background-color: #2d3e50;
            color: #ffffff;
            padding: 40px 0;
            margin-top: 50px;
        }

        .footer-wrap {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 30px;
        }

        .footer-col h3 {
            font-size: 18px;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #3498db;
        }

        .footer-col ul li {
            margin-bottom: 10px;
        }

        .footer-col ul li a {
            color: #bdc3c7;
            font-size: 14px;
        }

        .footer-col ul li a:hover {
            color: #3498db;
        }

        .copyright {
            text-align: center;
            padding-top: 30px;
            margin-top: 20px;
            border-top: 1px solid #34495e;
            font-size: 14px;
            color: #bdc3c7;
        }
    </style>
</head>
<body>
<!-- 顶部导航栏 - 增加登录状态判断 -->
<nav class="navbar">
    <div class="container navbar-wrap">
        <a href="<%= contextPath %>/company/companyIndex.jsp" class="logo">
            <h1>易兼职·企业端</h1>
        </a>
        <ul class="nav-menu">
            <li class="nav-item"><a href="<%= contextPath %>/company/companyIndex.jsp" class="active">首页</a></li>
            <li class="nav-item"><a href="<%= contextPath %>/company/publish-job.jsp">发布兼职</a></li>
            <li class="nav-item"><a href="<%= contextPath %>/company/resume-pool.jsp">简历库</a></li>
            <li class="nav-item"><a href="<%= contextPath %>/company/HelpCenter.jsp">帮助中心</a></li>
        </ul>

        <%-- 动态显示：登录后展示企业名称+退出按钮，未登录展示登录/注册 --%>
        <div class="auth-btns">
            <% if (isLogin) { %>
            <span class="company-info">欢迎，<%= companyName %></span>
            <button class="logout-btn" onclick="logout()">退出登录</button>
            <% } else { %>
            <%-- 修正登录跳转路径：指向之前的login.jsp（根目录） --%>
            <button class="login-btn" onclick="window.location.href='<%= contextPath %>/login/login.jsp'">企业登录</button>
            <button class="register-btn" onclick="window.location.href='<%= contextPath %>/company/companyRegister.jsp'">企业注册</button>
            <% } %>
        </div>
    </div>
</nav>

<!-- 横幅区域 - 路径改为JSP适配 -->
<div class="container">
    <div class="banner">
        <h2 class="banner-title">高效招募兼职人才，助力企业灵活用工</h2>
        <p class="banner-desc">易兼职企业端为您提供优质兼职求职者资源，便捷发布职位、快速筛选简历、高效沟通面试，一站式解决企业临时用工需求</p>
        <button class="banner-btn" onclick="window.location.href='<%= contextPath %>/companyRegister.html'">立即入驻，发布兼职</button>
    </div>

    <!-- 核心优势区域 - 路径改为JSP适配 -->
    <section class="advantage">
        <h3 class="section-title">企业服务核心指南</h3>
        <div class="advantage-list">
            <div class="advantage-item">
                <a href="<%= contextPath %>/company/Advantage.jsp" class="advantage-link">
                    <div class="advantage-icon">✅</div>
                    <h4 class="advantage-title">用工优势</h4>
                    <p class="advantage-desc">降低人力成本 30%-50%<br>灵活调配人力，应对业务波动<br>无需承担全职员工社保等附加成本</p>
                </a>
            </div>
            <div class="advantage-item">
                <a href="<%= contextPath %>/company/companyGuide.jsp" class="advantage-link">
                    <div class="advantage-icon">📋</div>
                    <h4 class="advantage-title">用工指南</h4>
                    <p class="advantage-desc">涵盖 10+ 行业用工模板<br>从发布职位到入职全流程指导<br>提供合规用工合同参考范本</p>
                </a>
            </div>
            <div class="advantage-item">
                <a href="<%= contextPath %>/company/HelpCenter.jsp" class="advantage-link">
                    <div class="advantage-icon">❓</div>
                    <h4 class="advantage-title">帮助中心</h4>
                    <p class="advantage-desc">7×12小时在线客服支持<br>累计解决 5w+ 企业用工问题<br>常见问题一键查询，快速答疑</p>
                </a>
            </div>
        </div>
    </section>

    <!-- 快速操作区域 - 路径改为JSP适配 -->
    <section class="quick-op">
        <h3 class="section-title">快速操作入口</h3>
        <div class="op-list">
            <div class="op-item" onclick="window.location.href='<%= contextPath %>/company/publish-job.jsp'">
                <div class="op-icon">📝</div>
                <p class="op-title">发布兼职职位</p>
            </div>
            <div class="op-item" onclick="window.location.href='<%= contextPath %>/company/resume-pool.jsp'">
                <div class="op-icon">📂</div>
                <p class="op-title">浏览简历库</p>
            </div>
            <div class="op-item" onclick="window.location.href='<%= contextPath %>/company/application-record.jsp'">
                <div class="op-icon">📬</div>
                <p class="op-title">查看应聘记录</p>
            </div>
            <div class="op-item" onclick="window.location.href='<%= contextPath %>/company/enterprise-profile.jsp'">
                <div class="op-icon">🏢</div>
                <p class="op-title">完善企业信息</p>
            </div>
        </div>
    </section>
</div>

<!-- 底部区域 - 路径改为JSP适配 -->
<footer class="footer">
    <div class="container footer-wrap">
        <div class="footer-col">
            <h3>关于我们</h3>
            <ul>
                <li><a href="#">平台介绍</a></li>
                <li><a href="#">企业合作</a></li>
                <li><a href="#">新闻动态</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>服务支持</h3>
            <ul>
                <li><a href="<%= contextPath %>/company/HelpCenter.jsp">帮助中心</a></li>
                <li><a href="#">常见问题</a></li>
                <li><a href="#">联系客服</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>法律条款</h3>
            <ul>
                <li><a href="#">用户协议</a></li>
                <li><a href="#">隐私政策</a></li>
                <li><a href="#">知识产权</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>联系我们</h3>
            <ul>
                <li><a href="#">电话：400-123-4567</a></li>
                <li><a href="#">邮箱：service@yijianzhi.com</a></li>
                <li><a href="#">地址：北京市朝阳区XX大厦</a></li>
            </ul>
        </div>
    </div>
    <div class="container copyright">
        © 2025 易兼职 版权所有 | 京ICP备12345678号
    </div>
</footer>

<script>
    // 导航栏滚动效果（保留原版）
    window.addEventListener('scroll', function() {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.style.padding = '10px 0';
        } else {
            navbar.style.padding = '15px 0';
        }
    });

    // 退出登录功能（JSP适配，增加默认跳转）
    function logout() {
        if (confirm("确定要退出登录吗？")) {
            // 1. 清空Session（如果有LogoutServlet则用下面的路径，否则直接跳转并清空）
            <%
                session.removeAttribute("company_name");
                session.invalidate(); // 销毁Session
            %>
            // 2. 跳转到登录页
            window.location.href = "<%= contextPath %>/company/companyIndex.jsp";
        }
    }
</script>
</body>
</html>