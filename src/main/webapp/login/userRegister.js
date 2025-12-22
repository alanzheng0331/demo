
    // 刷新验证码
    function refreshVerifyCode() {
        document.getElementById('verifyCodeImg').src = 'verifyCodeServlet?' + new Date().getTime();
    }

    // 修复密码进度条变色逻辑
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

    // 验证码验证
    const verifyCodeInput = document.getElementById('verifyCode');
    verifyCodeInput.addEventListener('input', function() {
        const verifyCodeError = document.getElementById('verifyCodeError');
        verifyCodeError.style.display = this.value.trim() ? 'none' : 'none';
    });

    // 表单提交验证
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        let isValid = true;

        // 用户名验证
        const username = usernameInput.value.trim();
        if (!/^[a-zA-Z0-9_]{3,16}$/.test(username)) {
            document.getElementById('usernameError').style.display = 'block';
            isValid = false;
        }

        // 密码验证
        const password = passwordInput.value.trim();
        if (password.length < 6) {
            passwordError.style.display = 'block';
            strengthBar.style.width = (password.length / 6) * 100 + '%';
            strengthBar.style.backgroundColor = '#ff4757';
            isValid = false;
        }

        // 确认密码验证
        const confirmPassword = confirmPasswordInput.value.trim();
        if (password !== confirmPassword) {
            document.getElementById('confirmPasswordError').style.display = 'block';
            isValid = false;
        }

        // 手机号验证
        const phone = phoneInput.value.trim();
        if (phone && !/^1[3-9]\d{9}$/.test(phone)) {
            document.getElementById('phoneError').style.display = 'block';
            isValid = false;
        }

        // 验证码验证
        const verifyCode = verifyCodeInput.value.trim();
        if (!verifyCode) {
            document.getElementById('verifyCodeError').style.display = 'block';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            alert('请检查表单填写是否正确！');
            return false;
        }
    });

    // 初始化验证码
    window.onload = function() {
        refreshVerifyCode();
    };
