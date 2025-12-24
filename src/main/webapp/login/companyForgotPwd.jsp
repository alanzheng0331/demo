<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>企业忘记密码 - 易兼职管理系统</title>
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

        .forgot-container {
            width: 100%;
            max-width: 1200px;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 20px;
        }

        .forgot-header {
            text-align: center;
            margin-bottom: 40px;
            color: white;
        }

        .forgot-header h1 {
            font-size: 36px;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        .forgot-header p {
            font-size: 18px;
            opacity: 0.9;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
        }

        .forgot-box {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            padding: 50px;
            width: 100%;
            max-width: 450px;
            backdrop-filter: blur(10px);
        }

        /* 全局提示样式：区分错误/成功 */
        .global-tip {
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            text-align: center;
            display: none;
        }

        .global-tip.error {
            background-color: #ffebee;
            color: #d32f2f;
            display: block;
        }

        .global-tip.success {
            background-color: #e8f5e9;
            color: #2e7d32;
            display: block;
        }

        /* 弹窗样式（修正文案） */
        .confirm-modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            display: none;
        }
        .modal-content {
            background: #fff;
            border-radius: 8px;
            width: 300px;
            padding: 20px;
            text-align: center;
        }
        .modal-btn-group {
            margin-top: 20px;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }
        .modal-btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .modal-btn.confirm {
            background: #409EFF;
            color: #fff;
        }
        .modal-btn.cancel {
            background: #f5f5f5;
            color: #666;
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
            background-color: #ff4757;
        }

        .strength-bar.green {
            width: 100%;
            background-color: #2ed573 !important;
        }

        .error-message {
            color: #ff4757;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .forgot-btn {
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

        .forgot-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .forgot-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }

        .forgot-links {
            display: flex;
            justify-content: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .forgot-links a {
            color: #667eea;
            text-decoration: none;
            font-size: 14px;
            transition: color 0.3s ease;
        }

        .forgot-links a:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        @media (max-width: 768px) {
            .forgot-container {
                padding: 20px;
            }
            .forgot-box {
                padding: 30px;
            }
            .forgot-header h1 {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>
<div class="forgot-container">
    <div class="forgot-header">
        <h1>企业找回密码</h1>
        <p>验证社会信用代码后设置新密码</p>
    </div>

    <div class="forgot-box">
        <%-- 全局提示框 --%>
        <div class="global-tip" id="globalTip"></div>

        <%-- 表单 --%>
        <form id="companyForgotForm">
            <!-- 步骤1：输入企业名称 -->
            <div class="form-group">
                <label for="companyName">企业名称 <span style="color: #ff4757;">*</span></label>
                <input type="text" id="companyName" name="companyName" class="form-control"
                       placeholder="请输入注册的企业全称" required>
                <span class="error-message" id="nameError">企业名称不能为空</span>
            </div>

            <!-- 步骤2：验证社会信用代码 -->
            <div class="form-group">
                <label for="creditCode">统一社会信用代码 <span style="color: #ff4757;">*</span></label>
                <input type="text" id="creditCode" name="creditCode" class="form-control"
                       placeholder="请输入18位社会信用代码" required>
                <span class="error-message" id="codeError">社会信用代码格式不正确（需18位）</span>
            </div>

            <!-- 步骤3：设置新密码 -->
            <div class="form-group">
                <label for="newPassword">新密码 <span style="color: #ff4757;">*</span></label>
                <input type="password" id="newPassword" name="newPassword" class="form-control"
                       placeholder="请输入至少6位新密码" required>
                <div class="password-strength">
                    <div id="strengthBar" class="strength-bar"></div>
                </div>
                <span class="error-message" id="passwordError">密码长度至少6位</span>
            </div>

            <!-- 步骤4：确认新密码 -->
            <div class="form-group">
                <label for="confirmPassword">确认新密码 <span style="color: #ff4757;">*</span></label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                       placeholder="请再次输入新密码" required>
                <span class="error-message" id="confirmError">两次输入的密码不一致</span>
            </div>

            <button type="button" class="forgot-btn" id="resetBtn">重置密码</button>

            <div class="forgot-links">
                <a href="${pageContext.request.contextPath}/login/login.jsp">返回登录页面</a>
            </div>
        </form>
    </div>
</div>

<%-- 确认弹窗（修正为密码重置成功提示） --%>
<div class="confirm-modal" id="successModal">
    <div class="modal-content">
        <p>密码重置成功！是否返回登录页面？</p>
        <div class="modal-btn-group">
            <button class="modal-btn confirm" id="confirmBtn">确定</button>
            <button class="modal-btn cancel" id="cancelBtn">取消</button>
        </div>
    </div>
</div>

<script>
    // 页面加载完成后执行
    window.onload = function() {
        // 绑定表单提交事件
        bindFormSubmit();
        // 绑定密码强度检测
        bindPasswordStrength();
        // 绑定表单实时校验
        bindFormValidate();
        // 绑定弹窗交互
        bindModalEvent();
    }

    // 绑定表单提交事件（改为URL编码格式提交）
    function bindFormSubmit() {
        const resetBtn = document.getElementById('resetBtn');

        resetBtn.addEventListener('click', function() {
            // 前端预校验
            if (!validateForm()) {
                return;
            }

            // 禁用按钮，防止重复提交
            resetBtn.disabled = true;
            resetBtn.textContent = '处理中...';

            // 手动收集所有参数（包括action），确保参数不丢失
            const params = new URLSearchParams();
            params.append('action', 'resetPwd'); // 强制添加action参数
            params.append('companyName', document.getElementById('companyName').value.trim());
            params.append('creditCode', document.getElementById('creditCode').value.trim());
            params.append('newPassword', document.getElementById('newPassword').value.trim());
            params.append('confirmPassword', document.getElementById('confirmPassword').value.trim());

            // 发送AJAX请求（使用URL编码格式，适配Servlet的getParameter）
            fetch('${pageContext.request.contextPath}/company/forgetPwd', {
                method: 'POST',
                body: params,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8', // 关键：指定格式
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => {
                    // 处理响应（兼容非JSON返回的情况）
                    if (!response.ok) {
                        throw new Error('服务器响应异常');
                    }
                    return response.json();
                })
                .then(data => {
                    // 恢复按钮状态
                    resetBtn.disabled = false;
                    resetBtn.textContent = '重置密码';

                    // 显示全局提示
                    showGlobalTip(data.msg, data.code === 200 ? 'success' : 'error');

                    // 如果重置成功，显示弹窗
                    if (data.code === 200) {
                        setTimeout(() => {
                            document.getElementById('successModal').style.display = 'flex';
                        }, 500);
                    }
                })
                .catch(error => {
                    // 网络错误/解析错误处理
                    resetBtn.disabled = false;
                    resetBtn.textContent = '重置密码';
                    showGlobalTip('请求失败：' + error.message, 'error');
                    console.error('请求错误:', error);
                });
        });
    }

    // 表单前端校验
    function validateForm() {
        let isValid = true;
        const companyName = document.getElementById('companyName').value.trim();
        const creditCode = document.getElementById('creditCode').value.trim();
        const newPassword = document.getElementById('newPassword').value.trim();
        const confirmPassword = document.getElementById('confirmPassword').value.trim();

        if (companyName === '') {
            showError('nameError');
            isValid = false;
        } else {
            hideError('nameError');
        }

        if (creditCode === '' || creditCode.length !== 18) {
            showError('codeError');
            isValid = false;
        } else {
            hideError('codeError');
        }

        if (newPassword === '' || newPassword.length < 6) {
            showError('passwordError');
            isValid = false;
        } else {
            hideError('passwordError');
        }

        if (confirmPassword === '' || confirmPassword !== newPassword) {
            showError('confirmError');
            isValid = false;
        } else {
            hideError('confirmError');
        }

        return isValid;
    }

    // 绑定表单实时校验
    function bindFormValidate() {
        document.getElementById('companyName').addEventListener('blur', function() {
            this.value.trim() === '' ? showError('nameError') : hideError('nameError');
        });

        document.getElementById('creditCode').addEventListener('blur', function() {
            (this.value.trim() === '' || this.value.length !== 18) ? showError('codeError') : hideError('codeError');
        });

        document.getElementById('newPassword').addEventListener('blur', function() {
            (this.value.trim() === '' || this.value.length < 6) ? showError('passwordError') : hideError('passwordError');
        });

        document.getElementById('confirmPassword').addEventListener('blur', function() {
            (this.value.trim() === '' || this.value !== document.getElementById('newPassword').value.trim()) ? showError('confirmError') : hideError('confirmError');
        });
    }

    // 密码强度检测
    function bindPasswordStrength() {
        const newPassword = document.getElementById('newPassword');
        const strengthBar = document.getElementById('strengthBar');

        newPassword.addEventListener('input', function() {
            const pwd = this.value.trim();
            strengthBar.className = pwd.length >= 6 ? 'strength-bar green' : 'strength-bar';
            strengthBar.style.width = (pwd.length / 6) * 100 + '%';
        });
    }

    // 绑定弹窗交互
    function bindModalEvent() {
        const modal = document.getElementById('successModal');
        const confirmBtn = document.getElementById('confirmBtn');
        const cancelBtn = document.getElementById('cancelBtn');

        confirmBtn.addEventListener('click', function() {
            window.location.href = '${pageContext.request.contextPath}/login/login.jsp';
        });

        cancelBtn.addEventListener('click', function() {
            modal.style.display = 'none';
            // 清空表单
            document.getElementById('companyForgotForm').reset();
            document.getElementById('strengthBar').className = 'strength-bar';
            document.getElementById('strengthBar').style.width = '0%';
            document.getElementById('globalTip').style.display = 'none';
        });
    }

    // 显示全局提示
    function showGlobalTip(message, type) {
        const globalTip = document.getElementById('globalTip');
        globalTip.textContent = message;
        globalTip.className = 'global-tip ' + type;
        globalTip.scrollIntoView({ behavior: 'smooth' });

        // 5秒后自动隐藏提示（错误提示）
        if (type === 'error') {
            setTimeout(() => {
                globalTip.style.display = 'none';
            }, 5000);
        }
    }

    // 显示/隐藏错误提示
    function showError(id) { document.getElementById(id).style.display = 'block'; }
    function hideError(id) { document.getElementById(id).style.display = 'none'; }
</script>
</body>
</html>