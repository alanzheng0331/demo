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
    <title>登录</title>

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

        .login-container {
            width: 100%;
            max-width: 1200px;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 20px;
        }

        .login-header {
            text-align: center;
            margin-bottom: 40px;
            color: white;
        }

        .login-header h1 {
            font-size: 36px;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        .login-tabs {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-bottom: 40px;
        }

        .tab-btn {
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            padding: 15px 40px;
            font-size: 18px;
            border-radius: 30px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: bold;
            backdrop-filter: blur(10px);
        }

        .tab-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        .tab-btn.active {
            background: white;
            color: #764ba2;
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }

        .login-box {
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

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .login-btn {
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
        }

        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .login-links {
            display: flex;
            justify-content: space-between;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .login-links a {
            color: #667eea;
            text-decoration: none;
            font-size: 14px;
            transition: color 0.3s ease;
        }

        .login-links a:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        .hidden {
            display: none;
        }

        /* 错误提示样式 */
        .error-tip {
            text-align: center;
            color: #ff4d4f;
            font-size: 14px;
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 8px;
            background: rgba(255, 77, 79, 0.1);
            display: none; /* 默认隐藏 */
        }

        /* 加载中样式 */
        .loading {
            text-align: center;
            color: #667eea;
            font-size: 14px;
            margin-bottom: 15px;
            display: none; /* 默认隐藏 */
        }

        /* 企业入口样式 */
        .admin-entrance {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: rgba(180, 190, 250, 0.8);
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
            transition: all 0.3s ease;
        }

        .admin-entrance:hover {
            background: rgba(255,255,255,0.9);
            transform: scale(1.1);
        }

        .admin-entrance i {
            color: #764ba2;
            font-size: 18px;
        }
    </style>
    <!-- 引入图标库（可选） -->
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1>兼职平台登录</h1>
    </div>

    <div class="login-tabs">
        <button class="tab-btn active" id="jobSeekerTab">用户登录</button>
        <button class="tab-btn" id="companyTab">企业登录</button>
    </div>

    <!-- 求职者登录表单（保持不变，匹配t_user表） -->
    <div class="login-box" id="jobSeekerLogin">
        <div class="error-tip" id="jobSeekerError"></div>
        <div class="loading" id="jobSeekerLoading">正在登录，请稍候...</div>

        <div class="form-group">
            <label for="jsPhone">手机号</label>
            <input type="tel" id="jsPhone" class="form-control" placeholder="请输入您的手机号" maxlength="11">
        </div>

        <div class="form-group">
            <label for="jsPassword">密码</label>
            <input type="password" id="jsPassword" class="form-control" placeholder="请输入您的密码">
        </div>

        <button class="login-btn" id="jsLoginBtn">登录</button>

        <div class="login-links">
            <a href="userRegister.jsp">还没有账号？立即注册</a>
            <a href="forgetPwd.jsp">忘记密码？</a>
        </div>
    </div>

    <!-- 企业登录表单（修改后，匹配t_company表） -->
    <div class="login-box hidden" id="companyLogin">
        <div class="error-tip" id="companyError"></div>
        <div class="loading" id="companyLoading">正在登录，请稍候...</div>

        <div class="form-group">
            <label for="companyName">企业名称</label>
            <!-- 改为text类型，移除手机号限制，匹配t_company.company_name -->
            <input type="text" id="companyName" class="form-control" placeholder="请输入企业名称" maxlength="200">
        </div>

        <div class="form-group">
            <label for="companyPwd">企业密码</label>
            <input type="password" id="companyPwd" class="form-control" placeholder="请输入企业密码">
        </div>

        <button class="login-btn" id="companyLoginBtn">企业登录</button>

        <div class="login-links">
            <a href="../company/companyRegister.jsp">还没有企业账号？立即注册</a>
            <a href="companyForgetPwd.jsp">忘记企业密码？</a>
        </div>
    </div>
</div>

<!-- 企业快捷入口 -->
<div class="admin-entrance" onclick="switchTab('companyTab')">
    <i class="fa fa-cog"></i>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // 切换登录标签（修改为支持企业登录）
        window.switchTab = function(tabId) {
            document.getElementById('jobSeekerLogin').classList.add('hidden');
            document.getElementById('companyLogin').classList.add('hidden');
            document.getElementById('jobSeekerTab').classList.remove('active');
            document.getElementById('companyTab').classList.remove('active');

            if (tabId === 'jobSeekerTab') {
                document.getElementById('jobSeekerTab').classList.add('active');
                document.getElementById('jobSeekerLogin').classList.remove('hidden');
            } else if (tabId === 'companyTab') {
                document.getElementById('companyTab').classList.add('active');
                document.getElementById('companyLogin').classList.remove('hidden');
            }

            document.getElementById('jobSeekerError').style.display = 'none';
            document.getElementById('companyError').style.display = 'none';
        };

        window.showError = function(errorElement, message) {
            errorElement.innerText = message;
            errorElement.style.display = 'block';
            setTimeout(() => {
                errorElement.style.display = 'none';
            }, 3000);
        };

        // 求职者登录逻辑（保持不变，匹配t_user表）
        window.jobSeekerLogin = function() {
            const phone = document.getElementById('jsPhone').value.trim();
            const password = document.getElementById('jsPassword').value.trim();
            const errorTip = document.getElementById('jobSeekerError');
            const loading = document.getElementById('jobSeekerLoading');

            if (!phone) {
                showError(errorTip, '请输入手机号');
                return;
            }
            if (phone.length !== 11) {
                showError(errorTip, '请输入11位有效手机号');
                return;
            }
            if (!password) {
                showError(errorTip, '请输入密码');
                return;
            }

            loading.style.display = 'block';
            errorTip.style.display = 'none';

            const xhr = new XMLHttpRequest();
            try {
                // 求职者登录接口
                xhr.open('POST', 'user/login', true);
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');

                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4) {
                        loading.style.display = 'none';
                        if (xhr.status !== 200) {
                            showError(errorTip, '服务器错误（状态码：' + xhr.status + '），请联系管理员');
                            return;
                        }
                        try {
                            const res = JSON.parse(xhr.responseText);
                            if (res.code === 200) {
                                alert('用户登录成功！');
                                window.location.href = '../user/index.jsp';
                            } else {
                                showError(errorTip, res.msg || '用户登录失败，请重试');
                            }
                        } catch (e) {
                            showError(errorTip, '服务器响应格式错误，请联系管理员');
                        }
                    }
                };

                xhr.onerror = function() {
                    loading.style.display = 'none';
                    showError(errorTip, '网络异常，请检查网络连接');
                };
                xhr.ontimeout = function() {
                    loading.style.display = 'none';
                    showError(errorTip, '请求超时，请重试');
                };
                xhr.timeout = 10000;

                const params = 'phone=' + encodeURIComponent(phone) +
                    '&password=' + encodeURIComponent(password) +
                    '&role=job_seeker';
                xhr.send(params);
            } catch (e) {
                loading.style.display = 'none';
                showError(errorTip, '请求初始化失败：' + e.message);
            }
        };

        // 企业登录逻辑（修改后，匹配t_company表）
        window.companyLogin = function() {
            const companyName = document.getElementById('companyName').value.trim();
            const companyPwd = document.getElementById('companyPwd').value.trim();
            const errorTip = document.getElementById('companyError');
            const loading = document.getElementById('companyLoading');

            // 验证规则匹配t_company表字段
            if (!companyName) {
                showError(errorTip, '请输入企业名称');
                return;
            }
            if (companyName.length < 2 || companyName.length > 200) {
                showError(errorTip, '企业名称长度需在2-200个字符之间');
                return;
            }
            if (!companyPwd) {
                showError(errorTip, '请输入企业密码');
                return;
            }
            // 匹配t_company.company_pwd字段长度限制
            if (companyPwd.length < 6 || companyPwd.length > 32) {
                showError(errorTip, '企业密码长度需在6-32个字符之间');
                return;
            }

            loading.style.display = 'block';
            errorTip.style.display = 'none';

            const xhr = new XMLHttpRequest();
            try {
                // 企业登录独立接口（对应后端处理t_company表的登录逻辑）
                xhr.open('POST', 'company/login', true);
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');

                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4) {
                        loading.style.display = 'none';
                        if (xhr.status !== 200) {
                            showError(errorTip, '服务器错误（状态码：' + xhr.status + '），请联系管理员');
                            return;
                        }
                        try {
                            const res = JSON.parse(xhr.responseText);
                            if (res.code === 200) {
                                alert('企业登录成功！');
                                // 跳转到企业后台首页
                                window.location.href = '../company/companyIndex.jsp';
                            } else {
                                showError(errorTip, res.msg || '企业登录失败，请重试');
                            }
                        } catch (e) {
                            showError(errorTip, '服务器响应格式错误，请联系管理员');
                        }
                    }
                };

                xhr.onerror = function() {
                    loading.style.display = 'none';
                    showError(errorTip, '网络异常，请检查网络连接');
                };
                xhr.ontimeout = function() {
                    loading.style.display = 'none';
                    showError(errorTip, '请求超时，请重试');
                };
                xhr.timeout = 10000;

                // 参数匹配t_company表字段：company_name和company_pwd
                const params = 'companyName=' + encodeURIComponent(companyName) +
                    '&companyPwd=' + encodeURIComponent(companyPwd);
                xhr.send(params);
            } catch (e) {
                loading.style.display = 'none';
                showError(errorTip, '请求初始化失败：' + e.message);
            }
        };

        // 绑定事件
        document.getElementById('jobSeekerTab').addEventListener('click', function() {
            switchTab('jobSeekerTab');
        });
        document.getElementById('companyTab').addEventListener('click', function() {
            switchTab('companyTab');
        });

        document.getElementById('jsLoginBtn').addEventListener('click', jobSeekerLogin);
        document.getElementById('companyLoginBtn').addEventListener('click', companyLogin);

        // 回车登录支持
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                if (!document.getElementById('jobSeekerLogin').classList.contains('hidden')) {
                    jobSeekerLogin();
                } else if (!document.getElementById('companyLogin').classList.contains('hidden')) {
                    companyLogin();
                }
            }
        });
    });
</script>
</body>
</html>