$(function () {
    validateRule();
    $('.imgcode').click(function () {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

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
        success : function(data) {
            localStorage.setItem("token", data.token);
            location.href = ctx+'index.html?token='+data.token;
        },
        error : function(xhr, textStatus, errorThrown) {
            $.modal.closeLoading();
            var msg = xhr.responseText;
            var response = JSON.parse(msg);
            $('.imgcode').click();
            $(".code").val("");
            $.modal.msg(response.message);
        }
    });
}

