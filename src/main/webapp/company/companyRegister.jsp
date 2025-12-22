<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>企业注册 - 易兼职管理系统</title>
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

        .code-group {
            display: flex;
            gap: 10px;
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

        .submit-msg {
            text-align: center;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .submit-msg.success {
            color: #2ed573;
            background: rgba(46, 213, 115, 0.1);
            border: 1px solid #2ed573;
        }
        .submit-msg.error {
            color: #ff4757;
            background: rgba(255, 71, 87, 0.1);
            border: 1px solid #ff4757;
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
            }
            .verify-code-img {
                width: 100%;
                height: 60px;
            }
        }
    </style>
</head>
<body>
<div class="register-container">
    <div class="register-header">
        <h1>企业注册</h1>
        <p>创建企业账号，入驻易兼职管理系统</p>
    </div>

    <form id="companyRegisterForm" class="register-box" action="${pageContext.request.contextPath}/companyRegisterServlet" method="POST">
        <%
            String msg = (String) request.getAttribute("msg");
            String msgType = (String) request.getAttribute("msgType");
            if (msg != null && !msg.isEmpty()) {
        %>
        <div class="submit-msg <%= msgType %>"><%= msg %></div>
        <%
            }
        %>

        <div class="form-group">
            <label for="companyName">企业名称 <span style="color: #ff4757;">*</span></label>
            <input type="text" id="companyName" name="companyName" class="form-control" placeholder="请输入企业全称（与营业执照一致）" required value="<%= request.getAttribute("companyName") != null ? request.getAttribute("companyName") : "" %>">
            <span class="error-message" id="companyNameError">企业名称不能为空</span>
            <span class="form-tip">请填写与营业执照一致的企业全称，不可修改</span>
        </div>

        <div class="form-group">
            <label for="creditCode">统一社会信用代码 <span style="color: #ff4757;">*</span></label>
            <input type="text" id="creditCode" name="creditCode" class="form-control" placeholder="请输入18位统一社会信用代码" required value="<%= request.getAttribute("creditCode") != null ? request.getAttribute("creditCode") : "" %>">
            <span class="error-message" id="creditCodeError">社会信用代码格式不正确（需18位有效编码）</span>
            <span class="form-tip">统一社会信用代码为18位，包含字母和数字，与营业执照一致</span>
        </div>

        <div class="form-group">
            <label for="legalPersonName">法人姓名 <span style="color: #ff4757;">*</span></label>
            <input type="text" id="legalPersonName" name="legalPersonName" class="form-control" placeholder="请输入企业法人真实姓名" required value="<%= request.getAttribute("legalPersonName") != null ? request.getAttribute("legalPersonName") : "" %>">
            <span class="error-message" id="legalPersonNameError">法人姓名不能为空</span>
            <span class="form-tip">请填写与营业执照登记一致的法人真实姓名</span>
        </div>

        <div class="form-group">
            <label for="companyAddress">公司详细地址 <span style="color: #ff4757;">*</span></label>
            <input type="text" id="companyAddress" name="companyAddress" class="form-control" placeholder="请输入：XX市XX区XX路XX号XX大厦XX室" required value="<%= request.getAttribute("companyAddress") != null ? request.getAttribute("companyAddress") : "" %>">
            <span class="error-message" id="companyAddressError">公司地点不能为空</span>
            <span class="form-tip">请填写企业实际经营地址，便于用户联系</span>
        </div>

        <div class="form-group">
            <label for="companyPhone">企业联系电话 <span style="color: #ff4757;">*</span></label>
            <input type="tel" id="companyPhone" name="companyPhone" class="form-control" placeholder="手机号/座机（格式：010-12345678）" required value="<%= request.getAttribute("companyPhone") != null ? request.getAttribute("companyPhone") : "" %>">
            <span class="error-message" id="companyPhoneError">联系电话格式不正确</span>
            <span class="form-tip">支持手机号（11位）或座机（含区号，如：010-12345678）</span>
        </div>

        <div class="form-group">
            <label for="companyPassword">登录密码 <span style="color: #ff4757;">*</span></label>
            <input type="password" id="companyPassword" name="companyPassword" class="form-control" placeholder="请输入密码（至少6位，字母+数字更佳）" required>
            <div class="password-strength">
                <div id="companyStrengthBar" class="strength-bar"></div>
            </div>
            <span class="error-message" id="companyPasswordError">密码长度至少6位</span>
            <span class="form-tip">建议使用字母、数字组合，长度不少于6位，保障账号安全</span>
        </div>

        <div class="form-group">
            <label for="companyConfirmPassword">确认登录密码 <span style="color: #ff4757;">*</span></label>
            <input type="password" id="companyConfirmPassword" name="companyConfirmPassword" class="form-control" placeholder="请再次输入密码" required>
            <span class="error-message" id="companyConfirmPasswordError">两次输入的密码不一致</span>
        </div>

        <div class="form-group">
            <label for="companyVerifyCode">验证码 <span style="color: #ff4757;">*</span></label>
            <div class="code-group">
                <input type="text" id="companyVerifyCode" name="companyVerifyCode" class="form-control" placeholder="请输入验证码" required>
                <img id="companyVerifyCodeImg" class="verify-code-img" src="${pageContext.request.contextPath}/verifyCodeServlet" alt="验证码" onclick="refreshCompanyVerifyCode()">
            </div>
            <span class="error-message" id="companyVerifyCodeError">验证码不能为空</span>
            <span class="form-tip">点击验证码图片可刷新，防止机器人注册</span>
        </div>

        <button type="submit" class="register-btn">企业注册</button>

        <div class="register-links">
            <a href="${pageContext.request.contextPath}/login/login.jsp">已有企业账号？立即登录</a>
        </div>
    </form>
</div>

<script>
    function refreshCompanyVerifyCode() {
        const ctx = "${pageContext.request.contextPath}";
        document.getElementById('companyVerifyCodeImg').src = ctx + '/verifyCodeServlet?' + new Date().getTime();
    }

    const companyPasswordInput = document.getElementById('companyPassword');
    const companyStrengthBar = document.getElementById('companyStrengthBar');
    const companyPasswordError = document.getElementById('companyPasswordError');

    companyPasswordInput.addEventListener('input', function() {
        const password = this.value.trim();
        companyStrengthBar.classList.remove('green');

        if (password.length === 0) {
            companyStrengthBar.style.width = '0%';
            companyPasswordError.style.display = 'none';
            return;
        }

        if (password.length < 6) {
            companyStrengthBar.style.width = (password.length / 6) * 100 + '%';
            companyStrengthBar.style.backgroundColor = '#ff4757';
            companyPasswordError.style.display = 'block';
        } else {
            companyStrengthBar.classList.add('green');
            companyPasswordError.style.display = 'none';
        }
    });

    const companyConfirmPasswordInput = document.getElementById('companyConfirmPassword');
    companyConfirmPasswordInput.addEventListener('input', function() {
        const password = companyPasswordInput.value;
        const confirmPassword = this.value;
        const confirmError = document.getElementById('companyConfirmPasswordError');

        confirmError.style.display = (confirmPassword && password !== confirmPassword) ? 'block' : 'none';
    });

    const companyNameInput = document.getElementById('companyName');
    companyNameInput.addEventListener('input', function() {
        const companyName = this.value.trim();
        const companyNameError = document.getElementById('companyNameError');
        companyNameError.style.display = companyName ? 'none' : 'block';
    });

    const companyAddressInput = document.getElementById('companyAddress');
    companyAddressInput.addEventListener('input', function() {
        const companyAddress = this.value.trim();
        const companyAddressError = document.getElementById('companyAddressError');
        companyAddressError.style.display = companyAddress ? 'none' : 'block';
    });

    const companyPhoneInput = document.getElementById('companyPhone');
    companyPhoneInput.addEventListener('input', function() {
        const phone = this.value.trim();
        const phoneError = document.getElementById('companyPhoneError');
        const phonePattern = /^(1[3-9]\d{9})|(\d{3,4}-\d{7,8})$/;

        phoneError.style.display = (phone && !phonePattern.test(phone)) ? 'block' : 'none';
    });

    const legalPersonNameInput = document.getElementById('legalPersonName');
    legalPersonNameInput.addEventListener('input', function() {
        const legalName = this.value.trim();
        const legalNameError = document.getElementById('legalPersonNameError');
        legalNameError.style.display = legalName ? 'none' : 'block';
    });

    const creditCodeInput = document.getElementById('creditCode');
    creditCodeInput.addEventListener('input', function() {
        const creditCode = this.value.trim();
        const creditCodeError = document.getElementById('creditCodeError');
        const creditCodePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;

        creditCodeError.style.display = (creditCode && !creditCodePattern.test(creditCode)) ? 'block' : 'none';
    });

    const companyVerifyCodeInput = document.getElementById('companyVerifyCode');
    companyVerifyCodeInput.addEventListener('input', function() {
        const verifyCodeError = document.getElementById('companyVerifyCodeError');
        verifyCodeError.style.display = this.value.trim() ? 'none' : 'block';
    });

    document.getElementById('companyRegisterForm').addEventListener('submit', function(e) {
        let isValid = true;

        const companyName = companyNameInput.value.trim();
        if (!companyName) {
            document.getElementById('companyNameError').style.display = 'block';
            isValid = false;
        }

        const creditCode = creditCodeInput.value.trim();
        const creditCodePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
        if (!creditCodePattern.test(creditCode)) {
            document.getElementById('creditCodeError').style.display = 'block';
            isValid = false;
        }

        const legalPersonName = legalPersonNameInput.value.trim();
        if (!legalPersonName) {
            document.getElementById('legalPersonNameError').style.display = 'block';
            isValid = false;
        }

        const companyAddress = companyAddressInput.value.trim();
        if (!companyAddress) {
            document.getElementById('companyAddressError').style.display = 'block';
            isValid = false;
        }

        const companyPhone = companyPhoneInput.value.trim();
        const phonePattern = /^(1[3-9]\d{9})|(\d{3,4}-\d{7,8})$/;
        if (!phonePattern.test(companyPhone)) {
            document.getElementById('companyPhoneError').style.display = 'block';
            isValid = false;
        }

        const companyPassword = companyPasswordInput.value.trim();
        if (companyPassword.length < 6) {
            companyPasswordError.style.display = 'block';
            companyStrengthBar.style.width = (companyPassword.length / 6) * 100 + '%';
            companyStrengthBar.style.backgroundColor = '#ff4757';
            isValid = false;
        }

        const companyConfirmPassword = companyConfirmPasswordInput.value.trim();
        if (companyPassword !== companyConfirmPassword) {
            document.getElementById('companyConfirmPasswordError').style.display = 'block';
            isValid = false;
        }

        const companyVerifyCode = companyVerifyCodeInput.value.trim();
        if (!companyVerifyCode) {
            document.getElementById('companyVerifyCodeError').style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            return false;
        }
    });

    window.onload = function() {
        refreshCompanyVerifyCode();
        <% if (msg != null && "error".equals(msgType)) { %>
        const verifyCodeError = document.getElementById('companyVerifyCodeError');
        if (verifyCodeError) {
            verifyCodeError.style.display = '<%= msg.contains("验证码") ? "block" : "none" %>';
        }
        <% } %>
    };
</script>
</body>
</html>