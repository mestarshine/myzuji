$(function () {
    init();
    validateRule();
    $('.imgcode').click(function () {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

function init() {
    if (top != self) {
        parent.location.href = '/login.html';
    }

    var token = localStorage.getItem("token");
    if (token != null && token.trim().length != 0) {
        $.ajax({
            type: 'get',
            url: '/users/current?token=' + token,
            success: function (data) {
                location.href = '/index.html';
            },
            error: function (xhr, textStatus, errorThrown) {
                var msg = xhr.responseText;
                var response = JSON.parse(msg);
                var code = response.code;
                var message = response.message;
                if (code == 401) {
                    localStorage.removeItem("token");
                    $.modal.msg(message)
                }
            }
        });
    }
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i>";
    $("#login-form").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}

$.validator.setDefaults({
    submitHandler: function () {
        login();
    }
});

function login() {
    $.modal.loading($("#btnSubmit").data("loading"));
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    var rememberMe = $("input[name='rememberme']").is(':checked');
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "validateCode": validateCode,
            "rememberMe": rememberMe
        },
        success: function (data) {
            localStorage.setItem("token", data.token);
            location.href = ctx + 'index.html';
        },
        error: function (xhr, textStatus, errorThrown) {
            $.modal.closeLoading();
            var msg = xhr.responseText;
            var response = JSON.parse(msg);
            $('.imgcode').click();
            $(".code").val("");
            $.modal.msg(response.message);
        }
    });
}
