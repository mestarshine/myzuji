/**
 * 首页方法封装处理
 * Copyright (c) 2020 zuji
 */
layer.config({
    extend: 'moon/style.css',
    skin: 'layer-ext-moon'
});

$(function () {
    showLoginInfo();
    // MetsiMenu
    $('#side-menu').metisMenu();

    //固定菜单栏
    $(function () {
        $('.sidebar-collapse').slimScroll({
            height: '100%',
            railOpacity: 0.9,
            alwaysVisible: false
        });
    });

    // 菜单切换
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });

    $('#side-menu>li').click(function () {
        if ($('body').hasClass('mini-navbar')) {
            NavToggle();
        }
    });
    $('#side-menu>li li a').click(function () {
        if ($(window).width() < 769) {
            NavToggle();
        }
    });

    $('.nav-close').click(NavToggle);

    //ios浏览器兼容性处理
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
        $('#mainFrame').css('overflow-y', 'auto');
    }

});

function showLoginInfo() {
    $.ajax({
        type: 'post',
        url: '/user/current',
        async: false,
        success: function (data) {
            if (data == null || data == "") {
                location.href = '/login.html';
                return;
            }
            $("#user-head-name").text(data.username);
            $("#user-head-img").attr("src", data.headImgUrl);
        }
    });
}

function resetPwd() {
    var url = ctx + 'system/user/profile/resetPwd';
    $.modal.open("重置密码", url, '800', '500');
}

$(window).bind("load resize",
    function () {
        if ($(this).width() < 769) {
            $('body').addClass('mini-navbar');
            $('.navbar-static-side').fadeIn();
            $(".sidebar-collapse .logo").addClass("hide");
        }
    });

function NavToggle() {
    $('.navbar-minimalize').trigger('click');
}

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar')) {
        $('#side-menu').hide();
        $(".sidebar-collapse .logo").removeClass("hide");
        setTimeout(function () {
                $('#side-menu').fadeIn(500);
            },
            100);
    } else if ($('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        $(".sidebar-collapse .logo").addClass("hide");
        setTimeout(function () {
                $('#side-menu').fadeIn(500);
            },
            300);
    } else {
        $('#side-menu').removeAttr('style');
    }
}

/**
 * iframe处理
 */
function cmainFrame() {
    var hmain = document.getElementById("mainFrame");
    var bheight = document.documentElement.clientHeight;
    hmain.style.width = '100%';
    hmain.style.height = (bheight - 51) + 'px';
    var bkbgjz = document.getElementById("bkbgjz");
    bkbgjz.style.height = (bheight - 41) + 'px';

}

window.onresize = function () {
    cmainFrame();
};
$(function () {

});
