<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%
    // 设置编码
    response.setCharacterEncoding("UTF-8");
    request.setCharacterEncoding("UTF-8");

    // ******************** 新增：获取登录用户信息（session中存储的用户对象，可根据你的实际项目调整key和属性） ********************
    // 假设登录成功后，将用户对象存入session，key为"loginUser"，用户头像属性为"avatar"，若没有自定义头像则使用默认头像
    Object loginUser = session.getAttribute("loginUser");
    String userAvatar = null;
    boolean isLogin = false;
    if (loginUser != null) {
        isLogin = true;
        // 这里替换为你的实际用户头像获取逻辑，例如：userAvatar = ((User)loginUser).getAvatar();
        // 先使用默认头像占位，后续可替换为用户自定义头像路径
        userAvatar = "${pageContext.request.contextPath}/images/avatar-default.png";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>易兼职 - 靠谱的兼职平台</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Microsoft YaHei', Arial, sans-serif;
        }

        body {
            background: #f8f9fa;
            color: #333;
            min-height: 100vh;
        }

        /* 顶部导航栏 */
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 15px 20px;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar-container {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            display: flex;
            align-items: center;
            gap: 10px;
            color: white;
            text-decoration: none;
        }

        .logo h1 {
            font-size: 24px;
            font-weight: bold;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }

        .nav-buttons {
            display: flex;
            gap: 15px;
            align-items: center; /* ******************** 新增：垂直居中，保证头像与其他按钮对齐 ******************** */
        }

        .nav-btn {
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            padding: 10px 20px;
            border-radius: 30px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
            text-decoration: none;
        }

        .nav-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        .nav-btn.login {
            background: white;
            color: #764ba2;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .nav-btn.logout {
            background: rgba(255, 80, 80, 0.2);
        }

        .nav-btn.logout:hover {
            background: rgba(255, 80, 80, 0.3);
        }

        /* ******************** 新增：用户头像样式 ******************** */
        .user-avatar {
            width: 40px; /* 头像大小 */
            height: 40px;
            border-radius: 50%; /* 圆形头像 */
            object-fit: cover; /* 保证图片不变形 */
            border: 2px solid rgba(255, 255, 255, 0.8); /* 白色边框，提升美观度 */
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .user-avatar:hover {
            transform: translateY(-2px);
            border-color: white;
            box-shadow: 0 0 10px rgba(255, 255, 255, 0.5); /*  hover时发光效果 */
        }

        /* 横幅区 */
        .banner {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin-top: 70px;
            padding: 60px 20px;
            color: white;
            text-align: center;
        }

        .banner-title {
            font-size: 40px;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }

        .banner-subtitle {
            font-size: 18px;
            margin-bottom: 30px;
            opacity: 0.9;
            max-width: 800px;
            margin-left: auto;
            margin-right: auto;
        }

        .banner-btn {
            background: white;
            color: #764ba2;
            border: none;
            padding: 15px 40px;
            border-radius: 30px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .banner-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        /* 主内容区 */
        .main-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 60px 20px;
        }

        /* 功能入口区 */
        .function-section {
            margin-bottom: 80px;
        }

        .section-title {
            font-size: 28px;
            text-align: center;
            margin-bottom: 40px;
            position: relative;
            padding-bottom: 15px;
        }

        .section-title::after {
            content: '';
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            bottom: 0;
            width: 80px;
            height: 4px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 2px;
        }

        .function-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 30px;
        }

        .function-card {
            background: white;
            border-radius: 20px;
            padding: 40px 20px;
            text-align: center;
            box-shadow: 0 10px 20px rgba(0,0,0,0.05);
            transition: all 0.3s ease;
            cursor: pointer;
            text-decoration: none;
            color: #333;
        }

        .function-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 30px rgba(102, 126, 234, 0.15);
        }

        .function-icon {
            width: 80px;
            height: 80px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0 auto 20px;
            color: white;
            font-size: 32px;
        }

        .function-title {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .function-desc {
            font-size: 14px;
            color: #666;
            line-height: 1.6;
        }

        /* 快捷导航区 */
        .quick-nav-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 20px;
            padding: 40px;
            margin-bottom: 80px;
            color: white;
        }

        .quick-nav-title {
            font-size: 24px;
            margin-bottom: 30px;
            text-align: center;
        }

        .quick-nav-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 20px;
        }

        .quick-nav-item {
            background: rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 20px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
            backdrop-filter: blur(10px);
        }

        .quick-nav-item:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-5px);
        }

        .quick-nav-name {
            font-size: 16px;
            font-weight: 600;
        }

        /* 底部 */
        .footer {
            background: #333;
            color: white;
            padding: 60px 20px 30px;
        }

        .footer-container {
            max-width: 1200px;
            margin: 0 auto;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 40px;
            margin-bottom: 40px;
        }

        .footer-col h3 {
            font-size: 18px;
            margin-bottom: 20px;
            position: relative;
            padding-bottom: 10px;
        }

        .footer-col h3::after {
            content: '';
            position: absolute;
            left: 0;
            bottom: 0;
            width: 40px;
            height: 2px;
            background: #667eea;
            border-radius: 1px;
        }

        .footer-col ul {
            list-style: none;
        }

        .footer-col ul li {
            margin-bottom: 10px;
        }

        .footer-col ul li a {
            color: rgba(255, 255, 255, 0.8);
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .footer-col ul li a:hover {
            color: white;
            margin-left: 5px;
        }

        .copyright {
            text-align: center;
            padding-top: 20px;
            border-top: 1px solid rgba(255, 255, 255, 0.2);
            font-size: 14px;
            color: rgba(255, 255, 255, 0.7);
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .navbar-container {
                flex-direction: column;
                gap: 15px;
            }

            .logo h1 {
                font-size: 20px;
            }

            .banner {
                margin-top: 120px;
                padding: 40px 20px;
            }

            .banner-title {
                font-size: 30px;
            }

            .function-grid {
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            }

            .quick-nav-section {
                padding: 30px 20px;
            }
        }

        @media (max-width: 480px) {
            .nav-buttons {
                flex-direction: column;
                width: 100%;
            }

            .nav-btn {
                width: 100%;
                text-align: center;
            }

            /* ******************** 新增：响应式下头像居中 ******************** */
            .user-avatar {
                margin: 0 auto;
            }

            .banner-title {
                font-size: 24px;
            }

            .function-grid {
                grid-template-columns: 1fr;
            }

            .quick-nav-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }
    </style>
</head>
<body>
<!-- 顶部导航栏：使用绝对路径跳转，排除JS拦截 -->
<nav class="navbar">
    <div class="navbar-container">
        <!-- 主页logo绝对路径 -->
        <a href="${pageContext.request.contextPath}/user/index.jsp" class="logo">
            <h1>易兼职</h1>
        </a>
        <div class="nav-buttons">
            <!-- 兼职检索绝对路径 -->
            <a href="${pageContext.request.contextPath}/user/search.jsp" class="nav-btn">兼职检索</a>

            <%-- ******************** 核心修改：根据登录状态动态切换元素 ******************** --%>
            <% if (!isLogin) { %>
            <!-- 未登录：显示登录/注册按钮 -->
            <a href="${pageContext.request.contextPath}/login/login.jsp" class="nav-btn login login-link">登录/注册</a>
            <% } else { %>
            <!-- 已登录：显示用户头像，跳转至个人主页 -->
            <a href="${pageContext.request.contextPath}/user/personal.jsp" title="我的主页">
                <img src="<%= userAvatar %>" alt="用户头像" class="user-avatar">
            </a>
            <!-- 可选：新增退出登录按钮（根据需求保留/删除） -->
            <a href="${pageContext.request.contextPath}/login/logoutServlet" class="nav-btn logout">退出登录</a>
            <% } %>
        </div>
    </div>
</nav>

<!-- 横幅区 -->
<div class="banner">
    <h2 class="banner-title">找靠谱兼职，上易兼职</h2>
    <p class="banner-subtitle">海量优质兼职岗位，日结/周结/月结多种结算方式，学生、宝妈、上班族都能找到合适的兼职</p>
    <a href="${pageContext.request.contextPath}/user/search.jsp" class="banner-btn">立即找兼职</a>
</div>

<!-- 主内容区 -->
<main class="main-container">
    <!-- 核心功能入口 -->
    <section class="function-section">
        <h2 class="section-title">核心功能</h2>
        <div class="function-grid">
            <!-- 兼职检索 -->
            <a href="${pageContext.request.contextPath}/user/search.jsp" class="function-card">
                <div class="function-icon">🔍</div>
                <h3 class="function-title">兼职检索</h3>
                <p class="function-desc">按关键词、地点、薪资、结算方式精准检索，找到最适合你的兼职</p>
            </a>

            <!-- 兼职指南 -->
            <a href="${pageContext.request.contextPath}/user/guide.jsp" class="function-card">
                <div class="function-icon">📚</div>
                <h3 class="function-title">兼职指南</h3>
                <p class="function-desc">兼职防骗、面试技巧、薪资谈判，全方位指导你的兼职之路</p>
            </a>

            <!-- 薪资查询 -->
            <a href="${pageContext.request.contextPath}/user/salary-query.jsp" class="function-card">
                <div class="function-icon">💰</div>
                <h3 class="function-title">薪资查询</h3>
                <p class="function-desc">查询各行业兼职薪资标准，避免被低薪坑，保障自身权益</p>
            </a>

            <!-- 我的兼职 -->
            <a href="${pageContext.request.contextPath}/user/personal.jsp" class="function-card">
                <div class="function-icon">📝</div>
                <h3 class="function-title">我的兼职</h3>
                <p class="function-desc">查看已应聘、已接单、已完成的兼职，管理你的兼职记录</p>
            </a>

            <!-- 新增：在线联系 -->
            <a href="${pageContext.request.contextPath}/user/contact.jsp" class="function-card">
                <div class="function-icon">📞</div>
                <h3 class="function-title">在线联系</h3>
                <p class="function-desc">直接联系雇主，及时解决兼职过程中的各种问题</p>
            </a>

            <!-- 意见反馈 -->
            <a href="${pageContext.request.contextPath}/user/feedback.jsp" class="function-card">
                <div class="function-icon">💬</div>
                <h3 class="function-title">意见反馈</h3>
                <p class="function-desc">对平台有任何建议或问题，随时反馈，我们会尽快处理</p>
            </a>
        </div>
    </section>

    <!-- 快捷导航 -->
    <section class="quick-nav-section">
        <h2 class="quick-nav-title">热门兼职分类</h2>
        <div class="quick-nav-grid">
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">家教辅导</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">餐饮服务</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">电商客服</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">文字创作</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">设计美工</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">技术开发</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">跑腿配送</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">摄影摄像</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">活动执行</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">翻译口译</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">市场推广</div>
            </div>
            <div class="quick-nav-item" onclick="window.location.href='${pageContext.request.contextPath}/user/search.jsp'">
                <div class="quick-nav-name">其他兼职</div>
            </div>
        </div>
    </section>
</main>

<!-- 底部 -->
<footer class="footer">
    <div class="footer-container">
        <div class="footer-col">
            <h3>关于我们</h3>
            <ul>
                <li><a href="#">平台介绍</a></li>
                <li><a href="#">联系方式</a></li>
                <li><a href="#">加入我们</a></li>
                <li><a href="#">企业合作</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>用户服务</h3>
            <ul>
                <li><a href="#">帮助中心</a></li>
                <li><a href="#">安全保障</a></li>
                <li><a href="#">投诉建议</a></li>
                <li><a href="#">兼职防骗</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>商家服务</h3>
            <ul>
                <li><a href="#">商家入驻</a></li>
                <li><a href="#">发布兼职</a></li>
                <li><a href="#">招聘管理</a></li>
                <li><a href="#">商家认证</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h3>法律条款</h3>
            <ul>
                <li><a href="#">用户协议</a></li>
                <li><a href="#">隐私政策</a></li>
                <li><a href="#">版权声明</a></li>
                <li><a href="#">免责条款</a></li>
            </ul>
        </div>
    </div>
    <div class="copyright">
        © 2025 易兼职 All Rights Reserved. 京ICP备12345678号
    </div>
</footer>

<script>
    // 所有跳转都是静态的，仅做页面跳转提示（排除登录链接和头像链接）
    document.addEventListener('DOMContentLoaded', function() {
        const links = document.querySelectorAll('a');
        links.forEach(function(link) {
            // 排除登录按钮、头像链接（带有user-avatar的父级a标签）、jsp链接
            if (!link.classList.contains('login-link') &&
                !link.querySelector('.user-avatar') &&
                (link.href === '#' || !link.href.includes('.jsp'))) {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    var text = this.textContent.trim();
                    alert("【" + text + "】功能正在建设中，敬请期待！");
                });
            }
        });
    });
</script>
</body>
</html>