
    // 刷新企业验证码
    function refreshCompanyVerifyCode() {
        document.getElementById('companyVerifyCodeImg').src = 'verifyCodeServlet?' + new Date().getTime();
    }

    // 企业密码强度验证
    const companyPasswordInput = document.getElementById('companyPassword');
    const companyStrengthBar = document.getElementById('companyStrengthBar');
    const companyPasswordError = document.getElementById('companyPasswordError');

    companyPasswordInput.addEventListener('input', function() {
        const password = this.value.trim();

        // 移除所有样式类
        companyStrengthBar.classList.remove('green');

        if (password.length === 0) {
            // 空密码：进度条归零，隐藏错误
            companyStrengthBar.style.width = '0%';
            companyPasswordError.style.display = 'none';
            return;
        }

        if (password.length < 6) {
            // 密码不足6位：红色进度条，显示错误
            companyStrengthBar.style.width = (password.length / 6) * 100 + '%';
            companyStrengthBar.style.backgroundColor = '#ff4757';
            companyPasswordError.style.display = 'block';
        } else {
            // 密码满足要求：绿色满条，隐藏错误
            companyStrengthBar.classList.add('green');
            companyPasswordError.style.display = 'none';
        }
    });

    // 企业确认密码验证
    const companyConfirmPasswordInput = document.getElementById('companyConfirmPassword');
    companyConfirmPasswordInput.addEventListener('input', function() {
        const password = companyPasswordInput.value;
        const confirmPassword = this.value;
        const confirmError = document.getElementById('companyConfirmPasswordError');

        confirmError.style.display = (confirmPassword && password !== confirmPassword) ? 'block' : 'none';
    });

    // 企业名称验证
    const companyNameInput = document.getElementById('companyName');
    companyNameInput.addEventListener('input', function() {
        const companyName = this.value.trim();
        const companyNameError = document.getElementById('companyNameError');

        companyNameError.style.display = companyName ? 'none' : 'none';
    });

    // 公司地点验证
    const companyAddressInput = document.getElementById('companyAddress');
    companyAddressInput.addEventListener('input', function() {
        const companyAddress = this.value.trim();
        const companyAddressError = document.getElementById('companyAddressError');

        companyAddressError.style.display = companyAddress ? 'none' : 'none';
    });

    // 企业联系电话验证（支持手机号和座机）
    const companyPhoneInput = document.getElementById('companyPhone');
    companyPhoneInput.addEventListener('input', function() {
        const phone = this.value.trim();
        const phoneError = document.getElementById('companyPhoneError');
        // 手机号正则：11位数字；座机正则：区号-号码（区号3-4位，号码7-8位）
        const phonePattern = /^(1[3-9]\d{9})|(\d{3,4}-\d{7,8})$/;

        phoneError.style.display = (phone && !phonePattern.test(phone)) ? 'block' : 'none';
    });

    // 法人姓名验证
    const legalPersonNameInput = document.getElementById('legalPersonName');
    legalPersonNameInput.addEventListener('input', function() {
        const legalName = this.value.trim();
        const legalNameError = document.getElementById('legalPersonNameError');

        legalNameError.style.display = legalName ? 'none' : 'none';
    });

    // 企业社会信用代码验证（18位，含字母和数字）
    const creditCodeInput = document.getElementById('creditCode');
    creditCodeInput.addEventListener('input', function() {
        const creditCode = this.value.trim();
        const creditCodeError = document.getElementById('creditCodeError');
        // 社会信用代码正则：18位，包含大写字母（部分位置固定）和数字，兼容常见格式
        const creditCodePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;

        creditCodeError.style.display = (creditCode && !creditCodePattern.test(creditCode)) ? 'block' : 'none';
    });

    // 企业验证码验证
    const companyVerifyCodeInput = document.getElementById('companyVerifyCode');
    companyVerifyCodeInput.addEventListener('input', function() {
        const verifyCodeError = document.getElementById('companyVerifyCodeError');
        verifyCodeError.style.display = this.value.trim() ? 'none' : 'none';
    });

    // 企业表单提交验证
    document.getElementById('companyRegisterForm').addEventListener('submit', function(e) {
        let isValid = true;

        // 企业名称验证
        const companyName = companyNameInput.value.trim();
        if (!companyName) {
            document.getElementById('companyNameError').style.display = 'block';
            isValid = false;
        }

        // 社会信用代码验证
        const creditCode = creditCodeInput.value.trim();
        const creditCodePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
        if (!creditCodePattern.test(creditCode)) {
            document.getElementById('creditCodeError').style.display = 'block';
            isValid = false;
        }

        // 法人姓名验证
        const legalPersonName = legalPersonNameInput.value.trim();
        if (!legalPersonName) {
            document.getElementById('legalPersonNameError').style.display = 'block';
            isValid = false;
        }

        // 公司地点验证
        const companyAddress = companyAddressInput.value.trim();
        if (!companyAddress) {
            document.getElementById('companyAddressError').style.display = 'block';
            isValid = false;
        }

        // 企业联系电话验证
        const companyPhone = companyPhoneInput.value.trim();
        const phonePattern = /^(1[3-9]\d{9})|(\d{3,4}-\d{7,8})$/;
        if (!phonePattern.test(companyPhone)) {
            document.getElementById('companyPhoneError').style.display = 'block';
            isValid = false;
        }

        // 企业密码验证
        const companyPassword = companyPasswordInput.value.trim();
        if (companyPassword.length < 6) {
            companyPasswordError.style.display = 'block';
            companyStrengthBar.style.width = (companyPassword.length / 6) * 100 + '%';
            companyStrengthBar.style.backgroundColor = '#ff4757';
            isValid = false;
        }

        // 企业确认密码验证
        const companyConfirmPassword = companyConfirmPasswordInput.value.trim();
        if (companyPassword !== companyConfirmPassword) {
            document.getElementById('companyConfirmPasswordError').style.display = 'block';
            isValid = false;
        }

        // 企业验证码验证
        const companyVerifyCode = companyVerifyCodeInput.value.trim();
        if (!companyVerifyCode) {
            document.getElementById('companyVerifyCodeError').style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            alert('请检查表单填写是否正确！');
            return false;
        }
    });

    // 初始化企业验证码
    window.onload = function() {
        refreshCompanyVerifyCode();
    };

