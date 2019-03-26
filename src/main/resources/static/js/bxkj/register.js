$(function () {

})

//发送验证码到邮箱
var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ //验证邮箱有效性的正则
function getFreeCode() {
    var username = $("#register_username").val() //用户名
    var pswd = $("#register_pwd").val() //密码
    var email = $("#register_email").val() //邮箱
    var traId= $("#register_traId").val() //旅行社id
    var opDeptid = $("#register_opDeptid").val() //运营部id
    var operateId = $("#operate_id").val() //计调部操作员id
    if(username == "" || username == null) {
        alert("请输入用户名")
        document.getElementById("register_email").focus() //聚焦输入框
    } else if(pswd == "" || pswd == null) {
        alert("请输入密码")
    }  else if(email == "" || email == null) {
        alert("请输入邮箱地址")
    }  else if(!reg.test(email)) {
        alert("请输入有效的邮箱地址")
    } else {
        if(username == "" || username == null) {
            document.getElementById("register_username").focus() //聚焦输入框
        } else {
            $("#sendFreeCode").css("display", "none") //
            $("#acodeFreeSentSpan").css("display", "") //提示验证码已发送
            $.ajax({  //发送验证码到邮箱,并保存邮箱验证码到数据库
                url: "saveFirstUserInfo",
                data: {
                    "nickname": username,
                    "pswd": pswd,
                    "email": email,
                    "traId": traId,
                    "opDeptid": opDeptid,
                    "operateId": operateId
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data.status == "200") {
                        // $("#sendFreeCode").css("display", "none")
                        // $("#acodeFreeSentSpan").css("display", "")
                        setTimeout(function () {
                            $("#acodeFreeSentSpan").css("display", "none")
                            $("#sendFreeCode").css("display", "")
                        }, 5000)
                    } else {
                        alert("验证码发送失败")
                    }
                },
                error: function (e) {
                    alert("请求异常")
                }
            })
        }
    }
}

//激活用户账号
function registerInfo() {
    var username = $("#register_username").val() //用户账号
    var password = $("#register_pwd").val() //密码
    var email = $("#register_email").val() //邮箱地址
    var ecode = $("#register_ecode").val() //验证码
    if(username == "" || username == null) {
        alert("请输入用户账号")
        document.getElementById("register_username").focus() //聚焦
    } else if(password == "" || password == null) {
        alert("请输入密码")
        document.getElementById("register_pwd").focus() //聚焦
    } else if(email == "" || email == null) {
        alert("请输入邮箱地址")
        document.getElementById("register_email").focus() //聚焦
    } else if(ecode == "" || ecode == null) {
        alert("请输入验证码")
        document.getElementById("register_ecode").focus() //聚焦
    } else {
        if(!reg.test(email)) {
            alert("请输入有效的邮箱地址")
        } else {
            $.ajax({
                url: "checkUserEcodeInfo",
                data: {
                    "nickname": username,
                    "email": email,
                    "ecode": ecode
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data.status == "200") { //查询用户输入的验证码是否正确
                        // debugger
                        updateFirstUserInfo(username, email)  //校验成功后激活用户账号
                    } else {
                        alert("输入的验证码有误!")
                    }
                },
                error: function (e) {
                    alert("请求异常")
                }
            })
        }
    }
}

//校验验证码成功后保存修改的用户信息
function updateFirstUserInfo(username, email) {
    $.ajax({
        url: "updateFirstUserInfo",
        data: {
            "nickname": username,
            "email": email
        },
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.status == "200") {
                // debugger
                // showSuccessOrErrorModal("密码重置成功,将在3秒后自动跳转到登录页", "success")
                var time = 3*1000;//计算出毫秒数
                var se;
                se = setInterval(function (args) {
                    //倒计时
                    // var mm = parseInt(time/60/1000%60,10);//剩余的分钟数
                    var ss = parseInt(time/1000%60,10);//剩余的秒数
                    // mm = checkTime(mm);
                    // ss = checkTime(ss);
                    $("#subRemind").html("")
                    $("#subRemind").append("用户注册成功," + ss +"秒后将自动跳转到登录页");
                    time = time-1000;
                    if(ss==1) {
                        clearInterval(se);
                        location.href = "login" //跳转到登录页
                    }
                },1000);

            } else {
                showSuccessOrErrorModal("操作失败", "error")
            }
        },
        error: function (e) {
            alert("请求异常")
        }
    })
}
