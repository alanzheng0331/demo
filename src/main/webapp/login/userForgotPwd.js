
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

    // 手机号格式验证
    const phoneInput = document.getElementById('phone');
    phoneInput.addEventListener('input', function() {
        const phone = this.value.trim();
        const phoneError = document.getElementById('phoneError');
        const phonePattern = /^1[3-9]\d{9}$/;
        phoneError.style.display = (phone && !phonePattern.test(phone)) ? 'block' : 'none';
    });

    // 表单提交验证
    document.getElementById('userForgotForm').addEventListener('submit', function(e) {
        let isValid = true;
        const username = document.getElementById('username').value.trim();
        const phone = phoneInput.value.trim();
        const newPwd = newPwdInput.value.trim();
        const confirmPwd = confirmPwdInput.value.trim();
        const phonePattern = /^1[3-9]\d{9}$/;

        // 用户名验证
        if (!username) {
            document.getElementById('usernameError').style.display = 'block';
            isValid = false;
        }
        // 手机号验证
        if (!phonePattern.test(phone)) {
            phoneError.style.display = 'block';
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
            document.getElementById('confirmError').style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            alert('请检查表单填写是否正确！');
        }
    });
