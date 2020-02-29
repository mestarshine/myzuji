
$(function () {

  //通过遍历给菜单项加上data-index属性
  $(".menu_item").each(function (index) {
    if (!$(this).attr('data-index')) {
      $(this).attr('data-index', index+1);
    }
  });

  // 全屏显示
  $("#fullScreen").on('click', function () {
    $("#wrapper").fullScreen();
  });

  //计算元素集合的总宽度
  function calSumWidth(elements, margin) {
    var width = 0;
    $(elements).each(function () {
      width += $(this).outerWidth(margin);
    });
    return width;
  }

  //滚动到指定选项卡
  function scrollToTab(element) {
    var marginLeftVal = calSumWidth($(element).prevAll(),true),
      marginRightVal = calSumWidth($(element).nextAll(),true);
    // 可视区域非tab宽度
    var tabOuterWidth = calSumWidth($(".nav-tabs-header").children().not(".nav-tabs-header-content"),true);
    //可视区域tab宽度
    var visibleWidth = calSumWidth($(".nav-tabs-header"),true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if (calSumWidth($("#nav-tabs"),false) < visibleWidth) {
      scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - calSumWidth($(element),true) - calSumWidth($(element).next()),true)) {
      if ((visibleWidth - calSumWidth($(element).next(),true)) > marginRightVal) {
        scrollVal = marginLeftVal;
        var tabElement = element;
        while ((scrollVal - calSumWidth($(tabElement),false)) > (calSumWidth($("#nav-tabs"),false) - visibleWidth)) {
          scrollVal -= calSumWidth($(tabElement).prev(),true);
          tabElement = $(tabElement).prev();
        }
      }
    } else if (marginLeftVal > (visibleWidth - calSumWidth($(element),true) - calSumWidth($(element).prev(),true))) {
      scrollVal = marginLeftVal - calSumWidth($(element).prev(),true);
    }
    $('#nav-tabs').animate({
      marginLeft: 0 - scrollVal + "px"
    }, "slow");
  }

  //查看左侧隐藏的选项卡
  function scrollTabLeft() {
    var marginLeftVal = Math.abs(parseInt($('#nav-tabs').css('margin-left')));
    // 可视区域非tab宽度
    var tabOuterWidth = calSumWidth($(".nav-tabs-header").children().not(".nav-tabs-header-content"),true);
    //可视区域tab宽度
    var visibleWidth = calSumWidth($(".nav-tabs-header"),true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if (calSumWidth($("#nav-tabs"),false) < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".tabs-header-menu:first");
      var offsetVal = 0;
      while ((offsetVal + calSumWidth($(tabElement),true)) <= marginLeftVal) { //找到离当前tab最近的元素
        offsetVal += calSumWidth($(tabElement),true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      if (calSumWidth($(tabElement).prevAll(),true) > visibleWidth) {
        while ((offsetVal + calSumWidth($(tabElement),true) < (visibleWidth) && tabElement.length > 0)) {
          offsetVal += calSumWidth($(tabElement),true);
          tabElement = $(tabElement).prev();
        }
        scrollVal = calSumWidth($(tabElement).prevAll(),true);
      }
    }
    $("#nav-tabs").animate({
        marginLeft: 0 - scrollVal + 'px'
      }, "slow");
  }

  //查看右侧隐藏的选项卡
  function scrollTabRight() {
    var marginLeftVal = Math.abs(parseInt($("#nav-tabs").css('margin-left')));
    // 可视区域非tab宽度
    var tabOuterWidth = calSumWidth($(".nav-tabs-header").children().not(".nav-tabs-header-content"),true);
    //可视区域tab宽度
    var visibleWidth = calSumWidth($(".nav-tabs-header"),true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if (calSumWidth($("#nav-tabs"),false) < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".tabs-header-menu:first");
      var offsetVal = 0;
      while ((offsetVal + calSumWidth($(tabElement),true)) <= marginLeftVal) { //找到离当前tab最近的元素
        offsetVal += calSumWidth($(tabElement),true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      while ((offsetVal + calSumWidth($(tabElement),true)) < (visibleWidth) && tabElement.length > 0) {
        offsetVal += calSumWidth($(tabElement),true);
        tabElement = $(tabElement).next();
      }
      scrollVal = calSumWidth($(tabElement).prevAll(),true);
      if (scrollVal > 0) {
        $("#nav-tabs").animate({
            marginLeft: 0 - scrollVal + "px"
          }, "slow");
      }
    }
  }
  function menuItem() {
    // 获取标识数据
    var dataUrl = $(this).attr('data-url'),
      dataIndex = $(this).data('index'),
      menuName = $.trim($(this).text()),
      flag = true;
    if (dataIndex == undefined || $.trim(dataIndex).length == 0) return false;
    $(".sidebar-menu .treeview-menu li").removeClass("active").parents(".treeview").removeClass("active");
    $(this).parent().addClass("active").parents(".treeview").addClass("active");
    // 选项卡菜单已存在
    $("#nav-tabs .tabs-header-menu").each(function () {
      if ($(this).data('index') == dataIndex) {
        if (!$(this).hasClass('active')) {
          $(this).addClass('active').siblings('.tabs-header-menu').removeClass('active');
          scrollToTab(this);
          // 显示tab对应的内容区
          $('.mainContent .zuji_iframe').each(function () {
            if ($(this).data('index') == dataIndex) {
              $(this).show().siblings('.zuji_iframe').hide();
              return false;
            }
          });
        }
        flag = false;
        return false;
      }
    });
    // 选项卡菜单不存在
    if (flag) {
      var str = '<li class="tabs-header-menu active" data-index="' + dataIndex + '"><a href="javascript:void(0)">' + menuName
        + '<i class="fa fa-times-circle"></i></a></li>';
      $('.tabs-header-menu').removeClass('active');

      // 添加选项卡对应的iframe
      var str1 = '<iframe class="zuji_iframe" name="mainFrame' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-index="' + dataIndex + '" seamless></iframe>';
      $("#content-main").find('iframe.zuji_iframe').hide().parents("#content-main").append(str1);
      $.modal.loading("数据加载中，请稍后...");

      $('.mainContent iframe:visible').on('load',function () {
        $.modal.closeLoading();
      });
      // 添加选项卡
      $("#nav-tabs").append(str);
      scrollToTab($('.tabs-header-menu.active'));
    }
    return false;
  }

  // 关闭选项卡菜单
  function closeTab() {
    var closeTabId = $(this).parents('.tabs-header-menu').data('index');
    var currentWidth = $(this).parents('.tabs-header-menu').parent().width();
    var panelUrl = $(this).parents('.tabs-header-menu').data('panel');
    // 当前元素处于活动状态
    if ($(this).parents('.tabs-header-menu').hasClass('active')) {

      // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
      if ($(this).parents('.tabs-header-menu').next('.tabs-header-menu').length) {

        var activeId = $(this).parents('.tabs-header-menu').next('.tabs-header-menu:eq(0)').data('index');
        $(this).parents('.tabs-header-menu').next('.tabs-header-menu:eq(0)').addClass('active');

        $('#content-main .zuji_iframe').each(function () {
          if ($(this).data('index') == activeId) {
            $(this).show().siblings('.zuji_iframe').hide();
            return false;
          }
        });

        var marginLeftVal = parseInt($('#nav-tabs').css('margin-left'));
        if (marginLeftVal < 0) {
          $('#nav-tabs').animate({
            marginLeft: (marginLeftVal - sccurrentWidthrollVal) + 'px'
          }, "slow");
        }

        //  移除当前选项卡
        $(this).parents('.tabs-header-menu').remove();

        // 移除tab对应的内容区
        $('#content-main .zuji_iframe').each(function () {
          if ($(this).data('index') == closeTabId) {
            $(this).remove();
            return false;
          }
        });
      }

      // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
      if ($(this).parents('.tabs-header-menu').prev('.tabs-header-menu').length) {
        var activeId = $(this).parents('.tabs-header-menu').prev('.tabs-header-menu:last').data('index');
        $(this).parents('.tabs-header-menu').prev('.tabs-header-menu:last').addClass('active');
        $('#content-main .zuji_iframe').each(function () {
          if ($(this).data('index') == activeId) {
            $(this).show().siblings('.zuji_iframe').hide();
            return false;
          }
        });

        //  移除当前选项卡
        $(this).parents('.tabs-header-menu').remove();

        // 移除tab对应的内容区
        $('#content-main .zuji_iframe').each(function () {
          if ($(this).data('index') == closeTabId) {
            $(this).remove();
            return false;
          }
        });

        if ($.common.isNotEmpty(panelUrl)) {
          $('.tabs-header-menu[data-id="' + panelUrl + '"]').addClass('active').siblings('.tabs-header-menu').removeClass('active');
          $('.mainContent .zuji_iframe').each(function () {
            if ($(this).data('index') == panelUrl) {
              $(this).show().siblings('.zuji_iframe').hide();
              return false;
            }
          });
        }
      }
    }
    // 当前元素不处于活动状态
    else {
      //  移除当前选项卡
      $(this).parents('.tabs-header-menu').remove();

      // 移除相应tab对应的内容区
      $('#content-main .zuji_iframe').each(function () {
        if ($(this).data('index') == closeTabId) {
          $(this).remove();
          return false;
        }
      });
    }
    scrollToTab($('.tabs-header-menu.active'));
    return false;
  }

  //关闭其他选项卡
  function closeOtherTabs() {
    $('#nav-tabs').children("[data-index]").not(":first").not(".active").each(function () {
      $('.zuji_iframe[data-index="' + $(this).data('index') + '"]').remove();
      $(this).remove();
    });
    showActiveTab();
  }
  //滚动到已激活的选项卡
  function showActiveTab() {
    scrollToTab($('.tabs-header-menu.active'));
  }
  // 点击选项卡菜单
  function activeTab() {
    if (!$(this).hasClass('active')) {
      var currentId = $(this).data('index');
      var index = $(this).data('index');
      // 显示tab对应的内容区
      $('#content-main .zuji_iframe').each(function () {
        if ($(this).data('index') == currentId) {
          $(this).show().siblings('.zuji_iframe').hide();
          return false;
        }
      });
      $(this).addClass('active').siblings('.tabs-header-menu').removeClass('active');
      scrollToTab(this);
      activeSidebar(index);
    }
  }

  function activeSidebar(index){
    $(".main-sidebar .sidebar .menu_item").each(function () {
      if ($(this).data('index') == index) {
        $(".sidebar-menu .treeview-menu li").removeClass("active").parent().hide(500);
        $(".sidebar-menu .treeview-menu li").parents(".treeview").each(function () {
          $(this).removeClass("active").removeClass("menu-open");
        });
        $(this).parents(".treeview").each(function(){
          $(this).children(":first").trigger('click');
        });
        $(this).trigger('click');
      }
    });
  }

  //刷新iframe
  function refreshTab() {
    var currentId = $('#nav-tabs').find('.active').attr('data-index');
    var target = $('.zuji_iframe[data-index="' + currentId + '"]');
    var url = target.attr('src');
    target.attr('src', url).ready();
  }

  //选项卡全屏显示
  function activeTabMax() {
    $('#content-main').fullScreen();
  }

  // 左移按扭
  $('.nav-tabs-header').on('click', '.tab_left', scrollTabLeft);

  // 右移按扭
  $('.nav-tabs-header').on('click','.tab_right', scrollTabRight);

  // 菜单事件
  $('.menu_item').on('click', menuItem);

  // 关闭选项卡菜单
  $('#nav-tabs').on('click','.tabs-header-menu i', closeTab);

  // 刷新按钮
  $('.nav-tabs-header').on('click','.tab_reload', refreshTab);

  // 关闭当前
  $('.tab_close_current').on('click', function () {
    $('.nav-tabs-header-content').find('.active i').trigger("click");
  });

  // 关闭其他选项卡
  $('.tab_close_other').on('click', closeOtherTabs);

  // 关闭全部
  $('.tab_close_all').on('click', function () {
    $('#nav-tabs').children("[data-index]").not(":first").each(function () {
      $('.zuji_iframe[data-index="' + $(this).data('index') + '"]').remove();
      $(this).remove();
    });
    $('#nav-tabs').children("[data-index]:first").each(function () {
      $('.zuji_iframe[data-index="' + $(this).data('index') + '"]').show();
      $(this).addClass("active");
    });
    showActiveTab();
  });

  // 选项卡全屏显示
  $('.nav-tabs-header .tab_max_current').on('click', activeTabMax);

  // 滚动到已激活的选项卡
  $('.tab_show_active').on('click', showActiveTab);

  // 点击选项卡菜单
  $('#nav-tabs').on('click','.tabs-header-menu', activeTab);

});
