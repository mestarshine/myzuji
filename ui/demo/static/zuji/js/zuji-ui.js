/**
 * 通用js方法封装处理
 * Copyright (c) 2019 zuji
 */
(function ($) {
  $.extend({
    // 弹出层封装处理
    modal: {
      // 显示图标
      icon: function (type) {
        var icon = "";
        if (type == modal_status.WARNING) {
          icon = 0;
        } else if (type == modal_status.SUCCESS) {
          icon = 1;
        } else if (type == modal_status.FAIL) {
          icon = 2;
        } else {
          icon = 3;
        }
        return icon;
      },
      // 消息提示
      msg: function (content, type) {
        if (type != undefined) {
          layer.msg(content, {icon: $.modal.icon(type), time: 1000, anim: 5,shade:0.5});
        } else {
          layer.msg(content,{time: 1000,shade:0.5});
        }
      },
      // 错误消息
      msgError: function (content) {
        $.modal.msg(content, modal_status.FAIL);
      },
      // 成功消息
      msgSuccess: function (content) {
        $.modal.msg(content, modal_status.SUCCESS);
      },
      // 警告消息
      msgWarning: function (content) {
        $.modal.msg(content, modal_status.WARNING);
      },
      // 消息提示并刷新父窗体
      msgReload: function (msg, type) {
        layer.msg(msg, {
            icon: $.modal.icon(type),
            time: 500,
            shade: [0.5, '#8F8F8F']
          },
          function () {
            $.modal.reload();
          });
      },
      // 弹出提示
      alert: function (content, type) {
        layer.alert(content, {
          icon: $.modal.icon(type),
          title: "系统提示",
          btn: ['确认'],
          btnclass: ['btn btn-primary'],
        });
      },
      // 错误提示
      alertError: function (content) {
        $.modal.alert(content, modal_status.FAIL);
      },
      // 成功提示
      alertSuccess: function (content) {
        $.modal.alert(content, modal_status.SUCCESS);
      },
      // 警告提示
      alertWarning: function (content) {
        $.modal.alert(content, modal_status.WARNING);
      },
      // 关闭窗体
      close: function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
      },
      // 关闭全部窗体
      closeAll: function () {
        layer.closeAll();
      },
      // 确认窗体
      confirm: function (content, callBack) {
        layer.confirm(content, {
          icon: 3,
          title: "系统提示",
          btn: ['确认', '取消'],
          btnclass: ['btn btn-primary', 'btn btn-danger'],
        }, function (index) {
          layer.close(index);
          callBack(true);
        });
      },
      tipeType:function(type){
        var tips = "";
        if (type == modal_tips_type.TOP) {
          tips=1;
        } else if (type == modal_tips_type.RIGHT) {
          tips=2;
        }else if (type == modal_tips_type.BOTTOM) {
          tips=3;
        }else {
          tips=4;
        }
        return tips;
      },
      tips:function(content, id_select,type,color) {
        if ($.common.isNotEmpty(type) && $.common.isEmpty(color)) {
        layer.open({
          type: 4,
          closeBtn: 0,
          tips: [$.modal.tipeType(type),'#000'],
          content: [content, '#' + id_select],
          time: 2000
        });
      }else if ($.common.isEmpty(type)&& $.common.isNotEmpty(color)) {
          layer.open({
            type: 4,
            closeBtn: 0,
            tips: [$.modal.tipeType(modal_tips_type.RIGHT),color],
            content: [content, '#' + id_select],
            time: 2000
          });
        }else if ($.common.isNotEmpty(type) && $.common.isNotEmpty(color)) {
          layer.open({
            type: 4,
            closeBtn: 0,
            tips: [$.modal.tipeType(type),color],
            content: [content, '#' + id_select],
            time: 2000
          });
        }else {
          layer.open({
            type: 4,
            closeBtn: 0,
            content: [content, '#' + id_select],
            time: 2000
          });
        }
      },
      // 错误提示
      tipsError: function (content,id) {
        $.modal.tips(content, id, null,'#dd4b39');
      },
      // 成功提示
      tipsSuccess: function (content,id) {
        $.modal.tips(content, id, null,'#00a65a');
      },
      // 警告提示
      tipsWarning: function (content,id) {
        $.modal.tips(content, id, null,'#f39c12');
      },
      // 弹出层指定宽度
      open: function (title, url, width, height, callback) {
        //如果是移动端，就使用自适应大小弹窗
        if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
          width = 'auto';
          height = 'auto';
        }
        if ($.common.isEmpty(title)) {
          title = false;
        }
        ;
        if ($.common.isEmpty(url)) {
          url = "/404.html";
        }
        ;
        if ($.common.isEmpty(width)) {
          width = 800;
        }
        ;
        if ($.common.isEmpty(height)) {
          height = ($(window).height() - 50);
        }
        ;
        if ($.common.isEmpty(callback)) {
          callback = function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.submitHandler();
          }
        }
        layer.open({
          type: 2,
          area: [width + 'px', height + 'px'],
          fix: false,
          //不固定
          maxmin: true,
          shade: 0.3,
          title: title,
          content: url,
          btn: ['确定', '关闭'],
          // 弹层外区域关闭
          shadeClose: true,
          yes: callback,
          cancel: function (index) {
            return true;
          }
        });
      },
      // 弹出层指定参数选项
      openOptions: function (options) {
        var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
        var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
        var _width = $.common.isEmpty(options.width) ? "800" : options.width;
        var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
        layer.open({
          type: 2,
          maxmin: true,
          shade: 0.3,
          title: _title,
          fix: false,
          area: [_width + 'px', _height + 'px'],
          content: _url,
          shadeClose: true,
          btn: ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'],
          yes: function (index, layero) {
            options.callBack(index, layero)
          }, cancel: function () {
            return true;
          }
        });
      },
      // 弹出层全屏
      openFull: function (title, url, width, height) {
        //如果是移动端，就使用自适应大小弹窗
        if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
          width = 'auto';
          height = 'auto';
        }
        if ($.common.isEmpty(title)) {
          title = false;
        }
        ;
        if ($.common.isEmpty(url)) {
          url = "/404.html";
        }
        ;
        if ($.common.isEmpty(width)) {
          width = 800;
        }
        ;
        if ($.common.isEmpty(height)) {
          height = ($(window).height() - 50);
        }
        ;
        var index = layer.open({
          type: 2,
          area: [width + 'px', height + 'px'],
          fix: false,
          //不固定
          maxmin: true,
          shade: 0.3,
          title: title,
          content: url,
          btn: ['确定', '关闭'],
          // 弹层外区域关闭
          shadeClose: true,
          yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.submitHandler();
          },
          cancel: function (index) {
            return true;
          }
        });
        layer.full(index);
      },
      // 选卡页方式打开
      openTab: function (title, url) {
        createMenuItem(url, title);
      },
      // 禁用按钮
      disable: function () {
        var doc = window.top == window.parent ? window.document : window.parent.document;
        $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
      },
      // 启用按钮
      enable: function () {
        var doc = window.top == window.parent ? window.document : window.parent.document;
        $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
      },
      // 打开遮罩层
      loading: function (message) {
        $.blockUI({message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>'});
      },
      // 关闭遮罩层
      closeLoading: function () {
        setTimeout(function () {
          $.unblockUI();
        }, 50);
      },
      // 重新加载
      reload: function () {
        parent.location.reload();
      }
    },
    // 通用方法封装处理
    common: {
      // 判断字符串是否为空
      isEmpty: function (value) {
        if (value == undefined ||value == null || this.trim(value) == "") {
          return true;
        }
        return false;
      },
      // 判断一个字符串是否为非空串
      isNotEmpty: function (value) {
        return !$.common.isEmpty(value);
      },
      // 空对象转字符串
      nullToStr: function (value) {
        if ($.common.isEmpty(value)) {
          return "-";
        }
        return value;
      },
      // 是否显示数据 为空默认为显示
      visible: function (value) {
        if ($.common.isEmpty(value) || value == true) {
          return true;
        }
        return false;
      },
      // 空格截取
      trim: function (value) {
        if (value == null) {
          return "";
        }
        return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
      },
      // 比较两个字符串（大小写敏感）
      equals: function (str, that) {
        return str == that;
      },
      // 比较两个字符串（大小写不敏感）
      equalsIgnoreCase: function (str, that) {
        return String(str).toUpperCase() === String(that).toUpperCase();
      },
      // 将字符串按指定字符分割
      split: function (str, sep, maxLen) {
        if ($.common.isEmpty(str)) {
          return null;
        }
        var value = String(str).split(sep);
        return maxLen ? value.slice(0, maxLen - 1) : value;
      },
      // 字符串格式化(%s )
      sprintf: function (str) {
        var args = arguments, flag = true, i = 1;
        str = str.replace(/%s/g, function () {
          var arg = args[i++];
          if (typeof arg === 'undefined') {
            flag = false;
            return '';
          }
          return arg;
        });
        return flag ? str : '';
      },
      // 指定随机数返回
      random: function (min, max) {
        return Math.floor((Math.random() * max) + min);
      },
      // 判断字符串是否是以start开头
      startWith: function (value, start) {
        var reg = new RegExp("^" + start);
        return reg.test(value)
      },
      // 判断字符串是否是以end结尾
      endWith: function (value, end) {
        var reg = new RegExp(end + "$");
        return reg.test(value)
      },
      // 数组去重
      uniqueFn: function (array) {
        var result = [];
        var hashObj = {};
        for (var i = 0; i < array.length; i++) {
          if (!hashObj[array[i]]) {
            hashObj[array[i]] = true;
            result.push(array[i]);
          }
        }
        return result;
      }
    }
  });
})(jQuery);

/** 弹窗状态码 */
modal_status = {
  SUCCESS: "success",
  FAIL: "error",
  WARNING: "warning"
};
modal_tips_type ={
  TOP:"top",
  RIGHT:"right",
  BOTTOM:"bottom",
  LEFT:"left"
}
