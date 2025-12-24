<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%
    // 设置编码，防止中文乱码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户注册</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Microsoft YaHei', Arial, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .register-container {
            width: 100%;
            max-width: 1200px;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 20px;
        }

        .register-header {
            text-align: center;
            margin-bottom: 40px;
            color: white;
        }

        .register-header h1 {
            font-size: 36px;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        .register-header p {
            font-size: 18px;
            opacity: 0.9;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
        }

        .register-box {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            padding: 50px;
            width: 100%;
            max-width: 450px;
            backdrop-filter: blur(10px);
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 600;
            font-size: 16px;
        }

        .form-control {
            width: 100%;
            padding: 15px;
            border: 2px solid #e1e1e1;
            border-radius: 10px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }

        /* 验证码输入行样式 */
        .code-group {
            display: flex;
            gap: 10px;
            align-items: center; /* 新增：垂直对齐，避免点击区域重叠 */
        }
        .code-group .form-control {
            flex: 1;
        }
        .verify-code-img {
            width: 120px;
            height: 52px;
            border-radius: 10px;
            cursor: pointer;
            border: 2px solid #e1e1e1;
            transition: all 0.3s ease;
            pointer-events: auto; /* 明确：仅自身可触发点击 */
        }
        .verify-code-img:hover {
            border-color: #667eea;
            transform: translateY(-2px);
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-tip {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
            display: block;
        }

        .register-btn {
            width: 100%;
            padding: 16px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
            pointer-events: auto; /* 明确：按钮可触发点击 */
        }

        .register-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .register-links {
            display: flex;
            justify-content: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .register-links a {
            color: #667eea;
            text-decoration: none;
            font-size: 14px;
            transition: color 0.3s ease;
        }

        .register-links a:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        /* 密码强度样式 - 修复进度条变色问题 */
        .password-strength {
            height: 8px;
            border-radius: 4px;
            margin-top: 8px;
            background: #eee;
            overflow: hidden;
            position: relative;
        }

        .strength-bar {
            position: absolute;
            left: 0;
            top: 0;
            height: 100%;
            width: 0%;
            transition: all 0.3s ease;
            background-color: #ff4757; /* 默认红色 */
        }

        /* 满足要求时的绿色样式 */
        .strength-bar.green {
            width: 100%;
            background-color: #2ed573 !important; /* 绿色，强制生效 */
        }

        .error-message {
            color: #ff4757;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        /* 全局错误提示 */
        .global-error {
            text-align: center;
            color: #ff4757;
            font-size: 14px;
            margin-bottom: 20px;
            padding: 10px;
            border-radius: 8px;
            background: rgba(255, 71, 87, 0.1);
            display: none;
        }

        /* 加载中样式 */
        .loading {
            text-align: center;
            color: #667eea;
            font-size: 14px;
            margin-bottom: 15px;
            display: none;
        }

        @media (max-width: 768px) {
            .register-container {
                padding: 20px;
            }

            .register-box {
                padding: 30px;
            }

            .register-header h1 {
                font-size: 28px;
            }

            .code-group {
                flex-direction: column;
                gap: 15px; /* 增大间距，避免点击重叠 */
            }
            .verify-code-img {
                width: 100%;
                height: 60px;
            }
        }
    </style>
    <!-- 引入图标库（可选） -->
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="register-container">
    <div class="register-header">
        <h1>用户注册</h1>
        <p>创建您的账号，开始使用管理系统</p>
    </div>

    <!-- 注册表单：移除action和method，改为AJAX提交 -->
    <div class="register-box" id="registerBox">
        <div class="global-error" id="globalError"></div>
        <div class="loading" id="registerLoading">正在注册，请稍候...</div>

        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" class="form-control" placeholder="请输入用户名（3-16位字母/数字/下划线）">
            <span class="error-message" id="usernameError">用户名格式不正确</span>
            <span class="form-tip">用户名长度为3-16位，可包含字母、数字和下划线</span>
        </div>

        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" class="form-control" placeholder="请输入密码（至少6位，建议字母+数字）">
            <div class="password-strength">
                <div id="strengthBar" class="strength-bar"></div>
            </div>
            <span class="error-message" id="passwordError">密码长度至少6位</span>
            <span class="form-tip">建议使用字母、数字组合，长度不少于6位</span>
        </div>

        <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input type="password" id="confirmPassword" class="form-control" placeholder="请再次输入密码">
            <span class="error-message" id="confirmPasswordError">两次输入的密码不一致</span>
        </div>

        <div class="form-group">
            <label for="phone">手机号码</label>
            <input type="tel" id="phone" class="form-control" placeholder="请输入您的手机号码" maxlength="11">
            <span class="error-message" id="phoneError">手机号码格式不正确</span>
        </div>

        <div class="form-group">
            <label for="email">邮箱（选填）</label>
            <input type="email" id="email" class="form-control" placeholder="请输入您的邮箱地址">
        </div>

        <!-- 验证码：修改为绝对路径，增加点击隔离 -->
        <div class="form-group">
            <label for="verifyCode">验证码</label>
            <div class="code-group">
                <input type="text" id="verifyCode" class="form-control" placeholder="请输入验证码">
                <!-- 新增：阻止事件冒泡，避免点击验证码时触发父元素/按钮事件 -->
                <img id="verifyCodeImg" class="verify-code-img" src="${pageContext.request.contextPath}/verifyCodeServlet" alt="验证码" onclick="refreshVerifyCode(event)">
            </div>
            <span class="error-message" id="verifyCodeError">验证码不能为空</span>
            <span class="form-tip">点击验证码图片可刷新</span>
        </div>

        <button class="register-btn" id="registerBtn">注册</button>

        <div class="register-links">
            <!-- 改为绝对路径，确保跳转正确 -->
            <a href="login.jsp">已有账号？立即登录</a>
        </div>
    </div>
</div>

<script>
    // 刷新图片验证码（阻止事件冒泡，避免误触其他元素）
    function refreshVerifyCode(e) {
        // 阻止事件向上冒泡，避免触发按钮/表单的点击事件
        e.stopPropagation();
        const verifyCodeImg = document.getElementById('verifyCodeImg');
        // 拼接项目根路径 + Servlet路径 + 时间戳防缓存
        verifyCodeImg.src = "${pageContext.request.contextPath}/verifyCodeServlet?" + new Date().getTime();

        // 刷新验证码时隐藏验证码错误提示
        document.getElementById('verifyCodeError').style.display = 'none';
        // 隐藏全局错误提示
        document.getElementById('globalError').style.display = 'none';
    }

    // 显示全局错误提示
    function showGlobalError(message) {
        const globalError = document.getElementById('globalError');
        globalError.innerText = message;
        globalError.style.display = 'block';
        // 延长提示时间，确保用户能看到
        setTimeout(() => {
            globalError.style.display = 'none';
        }, 5000);
    }

    // 密码强度校验
    const passwordInput = document.getElementById('password');
    const strengthBar = document.getElementById('strengthBar');
    const passwordError = document.getElementById('passwordError');

    passwordInput.addEventListener('input', function() {
        const password = this.value.trim();

        // 移除所有样式类
        strengthBar.classList.remove('green');

        if (password.length === 0) {
            // 空密码：进度条归零，隐藏错误
            strengthBar.style.width = '0%';
            passwordError.style.display = 'none';
            return;
        }

        if (password.length < 6) {
            // 密码不足6位：红色进度条，显示错误
            strengthBar.style.width = (password.length / 6) * 100 + '%';
            strengthBar.style.backgroundColor = '#ff4757';
            passwordError.style.display = 'block';
        } else {
            // 密码满足要求：绿色满条，隐藏错误
            strengthBar.classList.add('green');
            passwordError.style.display = 'none';
        }
    });

    // 确认密码验证
    const confirmPasswordInput = document.getElementById('confirmPassword');
    confirmPasswordInput.addEventListener('input', function() {
        const password = passwordInput.value;
        const confirmPassword = this.value;
        const confirmError = document.getElementById('confirmPasswordError');

        confirmError.style.display = (confirmPassword && password !== confirmPassword) ? 'block' : 'none';
    });

    // 用户名验证
    const usernameInput = document.getElementById('username');
    usernameInput.addEventListener('input', function() {
        const username = this.value;
        const usernameError = document.getElementById('usernameError');
        const usernamePattern = /^[a-zA-Z0-9_]{3,16}$/;

        usernameError.style.display = (username && !usernamePattern.test(username)) ? 'block' : 'none';
    });

    // 手机号验证
    const phoneInput = document.getElementById('phone');
    phoneInput.addEventListener('input', function() {
        const phone = this.value;
        const phoneError = document.getElementById('phoneError');
        const phonePattern = /^1[3-9]\d{9}$/;

        phoneError.style.display = (phone && !phonePattern.test(phone)) ? 'block' : 'none';
    });

    // 验证码验证（修复逻辑错误）
    const verifyCodeInput = document.getElementById('verifyCode');
    verifyCodeInput.addEventListener('input', function() {
        const verifyCodeError = document.getElementById('verifyCodeError');
        verifyCodeError.style.display = this.value.trim() ? 'none' : 'block';
    });

    // 注册核心逻辑（修改AJAX请求路径为绝对路径，优化校验提示）
    function doRegister() {
        let isValid = true;
        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();
        const confirmPassword = confirmPasswordInput.value.trim();
        const phone = phoneInput.value.trim();
        const email = document.getElementById('email').value.trim();
        const verifyCode = verifyCodeInput.value.trim();

        // 先隐藏所有错误提示，再重新校验
        document.getElementById('usernameError').style.display = 'none';
        passwordError.style.display = 'none';
        document.getElementById('confirmPasswordError').style.display = 'none';
        document.getElementById('phoneError').style.display = 'none';
        document.getElementById('verifyCodeError').style.display = 'none';

        // 前端校验（明确每个校验项，避免静默失败）
        if (username === '') {
            showGlobalError('用户名不能为空！');
            isValid = false;
        } else if (!/^[a-zA-Z0-9_]{3,16}$/.test(username)) {
            document.getElementById('usernameError').style.display = 'block';
            isValid = false;
        }

        if (password === '') {
            showGlobalError('密码不能为空！');
            isValid = false;
        } else if (password.length < 6) {
            passwordError.style.display = 'block';
            strengthBar.style.width = (password.length / 6) * 100 + '%';
            strengthBar.style.backgroundColor = '#ff4757';
            isValid = false;
        }

        if (confirmPassword === '') {
            showGlobalError('确认密码不能为空！');
            isValid = false;
        } else if (password !== confirmPassword) {
            document.getElementById('confirmPasswordError').style.display = 'block';
            isValid = false;
        }

        if (phone === '') {
            showGlobalError('手机号不能为空！');
            isValid = false;
        } else if (!/^1[3-9]\d{9}$/.test(phone)) {
            document.getElementById('phoneError').style.display = 'block';
            isValid = false;
        }

        if (!verifyCode) {
            document.getElementById('verifyCodeError').style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            showGlobalError('请检查表单填写是否完整且格式正确！');
            return; // 校验失败，直接返回，不执行后续逻辑
        }

        // 显示加载中，隐藏错误
        document.getElementById('registerLoading').style.display = 'block';
        document.getElementById('globalError').style.display = 'none';

        // AJAX提交请求（修改为绝对路径）
        const xhr = new XMLHttpRequest();
        try {
            // 打印请求路径，便于调试
            console.log('注册请求路径：', "${pageContext.request.contextPath}/user/register");
            xhr.open('POST', "${pageContext.request.contextPath}/user/register", true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    document.getElementById('registerLoading').style.display = 'none';
                    console.log('注册请求状态码：', xhr.status);
                    console.log('注册请求响应：', xhr.responseText);
                    if (xhr.status !== 200) {
                        showGlobalError('服务器错误（状态码：' + xhr.status + '），请检查后端配置');
                        return;
                    }
                    try {
                        const res = JSON.parse(xhr.responseText);
                        if (res.code === 200) {
                            alert('注册成功！即将跳转到登录页面');
                            // 改为绝对路径，确保跳转正确
                            window.location.href = "login.jsp";
                        } else {
                            showGlobalError(res.msg || '注册失败，请重试');
                            // 仅当验证码错误时刷新，避免不必要的刷新
                            if (res.msg && res.msg.includes('验证码')) {
                                refreshVerifyCode({stopPropagation: function(){}});
                            }
                        }
                    } catch (e) {
                        showGlobalError('服务器响应格式错误，请联系管理员：' + e.message);
                    }
                }
            };

            xhr.onerror = function() {
                document.getElementById('registerLoading').style.display = 'none';
                showGlobalError('网络异常，请检查网络连接');
            };
            xhr.ontimeout = function() {
                document.getElementById('registerLoading').style.display = 'none';
                showGlobalError('请求超时，请稍后重试');
            };
            xhr.timeout = 10000;

            // 构造参数（与UserServlet接收的参数名一致）
            const params = 'role=job_seeker' +
                '&name=' + encodeURIComponent(username) +
                '&phone=' + encodeURIComponent(phone) +
                '&email=' + encodeURIComponent(email) +
                '&password=' + encodeURIComponent(password) +
                '&confirmPwd=' + encodeURIComponent(confirmPassword) +
                '&code=' + encodeURIComponent(verifyCode) +
                '&skillTags=' + encodeURIComponent('') + // 补充可选参数，避免后端接收不到
                '&expectedSalary=' + encodeURIComponent(''); // 补充可选参数，避免后端接收不到
            console.log('注册请求参数：', params);
            xhr.send(params);
        } catch (e) {
            document.getElementById('registerLoading').style.display = 'none';
            showGlobalError('请求初始化失败：' + e.message);
        }
    }

    // 绑定事件（确保DOM加载完成后绑定，避免元素未找到）
    window.addEventListener('DOMContentLoaded', function() {
        const registerBtn = document.getElementById('registerBtn');
        // 绑定点击事件，阻止事件冒泡
        registerBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            doRegister();
        });

        // 回车触发注册（仅在表单内有效）
        document.getElementById('registerBox').addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault(); // 阻止表单默认提交
                doRegister();
            }
        });

        // 初始化图片验证码
        refreshVerifyCode({stopPropagation: function(){}});
    });
</script>
</body>
</html>