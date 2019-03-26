$(function () {
    
})

//发送验证码到邮箱
var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/ //验证邮箱有效性的正则
function mainAcode() {
    var email = $("#forget_email").val() //邮箱地址
    var username = $("#forget_username").val()
    if(email == "" || email == null) {
        alert("请输入邮箱地址")
        document.getElementById("forget_email").focus() //聚焦输入框
    } else if(!reg.test(email)) {
        alert("请输入有效的邮箱地址")
    } else {
        if(username == "" || username == null) {
            document.getElementById("forget_username").focus() //聚焦输入框
        } else {
            $("#sendFreeCode").css("display", "none")
            $("#acodeFreeSentSpan").css("display", "") //提示验证码已发送
            $.ajax({  //发送验证码到邮箱,并保存邮箱验证码到数据库
                url: "sendAndSaveEcodeInfo",
                data: {
                    "nickname": username,
                    "email": email
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

//提交密码重置数据
function submitChange() {
    var username = $("#forget_username").val() //用户账号
    var password = $("#forget_pwd").val() //新密码
    var db_password = $("#forget_d_pwd").val() //再次确认新密码
    var email = $("#forget_email").val() //邮箱地址
    var ecode = $("#forget_ecode").val() //验证码
    if(username == "" || username == null) {
        alert("请输入用户账号")
        document.getElementById("forget_username").focus() //聚焦
    } else if(password == "" || password == null) {
        alert("请输入新密码")
        document.getElementById("forget_pwd").focus() //聚焦
    } else if(db_password == "" || db_password == null) {
        alert("请再次输入密码")
        document.getElementById("forget_d_pwd").focus() //聚焦
    } else if(password != db_password) {
        alert("两次输入的密码不相同")
        document.getElementById("forget_d_pwd").focus() //聚焦
    }  else if(email == "" || email == null) {
        alert("请输入邮箱地址")
        document.getElementById("forget_email").focus() //聚焦
    } else if(ecode == "" || ecode == null) {
        alert("请输入验证码")
        document.getElementById("forget_ecode").focus() //聚焦
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
                        updateUserPwdInfo(username, password)  //校验成功后保存修改信息
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
function updateUserPwdInfo(username, password) {
    $.ajax({
        url: "updateUserPwdInfo",
        data: {
            "nickname": username,
            "password": password
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
                    $("#subRemind").append("密码重置成功," + ss +"秒后将自动跳转到登录页");
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
