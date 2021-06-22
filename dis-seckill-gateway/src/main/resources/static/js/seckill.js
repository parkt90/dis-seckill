 /*秒杀接口隐藏*/
 function getSkPath() {
    var goodsId = $("#goodsId").val();
    g_showLoading();
    $.ajax({
        url: "/seckill/path",
        type: "GET",
        data: {
            goodsId: goodsId,
            verifyCode: $("#verifyCode").val() // 获取验证码的值
        },
        success: function (data) {
            if (data.code == 0) {
                var path = data.data;// 获取服务端返回的秒杀地址
                doSeckill(path);// 真正做秒杀的接口
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}

/* 秒杀接口压力测试时，注释掉：验证码和秒杀地址隐藏 */
/*真正做秒杀的接口, path为服务端返回的秒杀接口地址*/
function doSeckill(path) {
    $.ajax({
        url: "/seckill/" + path + "/doSeckill",
        type: "POST",
        data: {
            goodsId: $("#goodsId").val()
        },
        success: function (data) {
            if (data.code == 0) {
                //window.location.href="/order_detail.htm?orderId="+data.data.id;
                getSkResult($("#goodsId").val());
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}

/*获取秒杀的结果*/
function getSkResult(goodsId) {
    g_showLoading();// 加载中
    $.ajax({
        url: "/seckill/result",
        type: "GET",
        data: {
            goodsId: $("#goodsId").val()
        },
        success: function (data) {
            if (data.code == 0) {
                var result = data.data;
                // console.log(data);
                if (result < 0) {
                    layer.msg("对不起，秒杀失败");
                } else if (result == 0) {// 继续轮询发送秒杀请求
                    setTimeout(function () {
                        getSkResult(goodsId);
                    }, 500);// 轮询间隔500ms
                } else {
                    layer.confirm("恭喜你，秒杀成功！查看订单？", {btn: ["确定", "取消"]},
                        // 确定的回调
                        function () {
                            window.location.href = "/order_detail.htm?orderId=" + result;
                        },
                        // 取消的回调
                        function () {
                            layer.closeAll();
                        });
                }
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}

// 使用ajax从服务端请求页面数据, 跳转到这个页面时就会执行(function有$)
$(function () {
    // countDown();
    getDetail();
});

/*获取商品详情*/
function getDetail() {
    var goodsId = g_getQueryString("goodsId");// goodsId为goods_list.html中详情url中的参数
    $.ajax({
        url: "/goods/getDetails/" + goodsId,
        type: "GET",
        success: function (data) {
            if (data.code == 0) {
                render(data.data);
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}

/*渲染页面*/
function render(detail) {
    var seckillStatus = detail.seckillStatus;
    var remainSeconds = detail.remainSeconds;
    var goods = detail.goods;
    var user = detail.user;
    console.log(detail);
    // console.log(goods);
    // console.log(user);
    if (user != null) {
        // $("#userTip").hide();
        $("#userTip").text(user.nickname+" 你好！");
        console.log(user.nickname);
    }

    $("#goodsName").text(goods.goodsName);
    $("#goodsImg").attr("src", goods.goodsImg);
    $("#startTime").text(new Date(goods.startDate).format("yyyy-MM-dd hh:mm:ss"));
    $("#remainSeconds").val(remainSeconds);
    $("#goodsId").val(goods.id);
    $("#goodsPrice").text(goods.goodsPrice);
    $("#seckillPrice").text(goods.seckillPrice);
    $("#stockCount").text(goods.stockCount);
    countDown();
}

function countDown() {
    var remainSeconds = $("#remainSeconds").val();
    var timeout;
    if (remainSeconds > 0) {// 秒杀还没开始，倒计时
        $("#buyButton").attr("disabled", true);
        $("#skTip").html("秒杀倒计时：" + remainSeconds + "秒");
        timeout = setTimeout(function () {
            $("#countDown").text(remainSeconds - 1);
            $("#remainSeconds").val(remainSeconds - 1);
            countDown();
        }, 1000);
    } else if (remainSeconds == 0) { // 秒杀进行中
        $("#buyButton").attr("disabled", false);
        if (timeout) {
            clearTimeout(timeout);
        }
        $("#skTip").html("秒杀进行中");
        // 在倒计时结束时获取验证码（使用ajax异步向服务器请求验证码图片）
        $("#verifyCodeImg").attr("src", "/seckill/verifyCode?goodsId=" + $("#goodsId").val());
        $("#verifyCodeImg").show();// 从服务器加载完验证码图片后，显示出来
        $("#verifyCode").show();
    } else {//秒杀已经结束
        $("#buyButton").attr("disabled", true);
        $("#skTip").html("秒杀已经结束");
        $("#verifyCodeImg").hide();
        $("#verifyCode").hide();
    }
}

/*点击验证码图片时，从服务器异步获取另一张验证码码图片*/
function refreshVerifyCode() {
    console.log("刷新验证码");
    $("#verifyCodeImg").attr("src", "/seckill/verifyCode?goodsId=" + $("#goodsId").val() + "&timestamp=" + new Date().getTime());
}