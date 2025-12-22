
    // 密码强度验证
    const newPwdInput = document.getElementById('newPassword');
    const strengthBar = document.getElementById('strengthBar');
    const pwdError = document.getElementById('passwordError');

    newPwdInput.addEventListener('input', function() {
        const pwd = this.value.trim();
        strengthBar.classList.remove('green');

        if (pwd.length === 0) {
            strengthBar.style.width = '0%';
            pwdError.style.display = 'none';
            return;
        }

        if (pwd.length < 6) {
            strengthBar.style.width = (pwd.length / 6) * 100 + '%';
            strengthBar.style.backgroundColor = '#ff4757';
            pwdError.style.display = 'block';
        } else {
            strengthBar.classList.add('green');
            pwdError.style.display = 'none';
        }
    });

    // 确认密码验证
    const confirmPwdInput = document.getElementById('confirmPassword');
    confirmPwdInput.addEventListener('input', function() {
        const newPwd = newPwdInput.value;
        const confirmPwd = this.value;
        const confirmError = document.getElementById('confirmError');
        confirmError.style.display = (confirmPwd && newPwd !== confirmPwd) ? 'block' : 'none';
    });

    // 社会信用代码验证
    const codeInput = document.getElementById('creditCode');
    codeInput.addEventListener('input', function() {
        const code = this.value.trim();
        const codeError = document.getElementById('codeError');
        // 社会信用代码正则（18位，含指定字母和数字）
        const codePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
        codeError.style.display = (code && !codePattern.test(code)) ? 'block' : 'none';
    });

    // 表单提交验证
    document.getElementById('companyForgotForm').addEventListener('submit', function(e) {
        let isValid = true;
        const companyName = document.getElementById('companyName').value.trim();
        const code = codeInput.value.trim();
        const newPwd = newPwdInput.value.trim();
        const confirmPwd = confirmPwdInput.value.trim();
        const codePattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;

        // 企业名称验证
        if (!companyName) {
            document.getElementById('nameError').style.display = 'block';
            isValid = false;
        }
        // 信用代码验证
        if (!codePattern.test(code)) {
            document.getElementById('codeError').style.display = 'block';
            isValid = false;
        }
        // 密码长度验证
        if (newPwd.length < 6) {
            pwdError.style.display = 'block';
            strengthBar.style.width = (newPwd.length / 6) * 100 + '%';
            strengthBar.style.backgroundColor = '#ff4757';
            isValid = false;
        }
        // 确认密码验证
        if (newPwd !== confirmPwd) {
            confirmError.style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            alert('请检查表单填写是否正确！');
        }
    });
