$(function () {
    //回车事件绑定
    //为keyListener方法注册按键事件
    document.onkeydown=keyListener;
    function keyListener(e){
        //    当按下回车键，执行我们的代码
        if(e.keyCode == 13){
            checkLogin();
        }
    }
})

/**
 * @Author Breach
 * @Description 跳转到采集页面
 * @Date 2019/2/15
 */
var flag = true //设置开关，控制点击速度
function checkLogin() {
    var username = $("#username").val() //用户名
    var password = $("#password").val() //密码
    var rememberMe =$('#rememberMe').is(':checked'); //是否记住
    if (flag) {
        var doc = document.createElement("div")
        doc.setAttribute("class", "remind-info")
        if (username == "" || username == null) { //未输入账号提醒
            var str = document.createTextNode("🔊 请输入账号")
            doc.appendChild(str)
            document.getElementById("remind").appendChild(doc)
            $("#username").addClass("name-remind")
            flag = false //控制点击速度开关（关闭）
            setTimeout(function () {
                $("#username").removeClass("name-remind")
                document.getElementById("remind").removeChild(doc)
                flag = true //控制点击速度开关（打开）
            }, 1500)
        } else if (password == "" || password == null) { //未输入密码提醒
            var str = document.createTextNode("🔊 请输入密码")
            doc.appendChild(str)
            document.getElementById("remind").appendChild(doc)
            flag = false //控制点击速度开关（关闭）
            $("#password").addClass("psw-remind")
            setTimeout(function () {
                $("#password").removeClass("psw-remind")
                document.getElementById("remind").removeChild(doc)
                flag = true //控制点击速度开关（打开）
            }, 1000)
        } else {
            $.ajax({
                url: "ajaxLogin",
                data: {
                    "nickname": username,
                    "pswd": password
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data.status == 200) {
                        // debugger
                        location.href = "success"; //验证成功后跳到首页
                        /*alert Start*/
                        setTimeout(function () { //采集线路规则提示信息
                            window.alert("采集内部线路须注意以下规则：\n\n" +
                                "1、采集的线路的起点必须是5级或者6级的出入口或者是已存在的两个出入口线路连接上的点；\n\n" +
                                "2、采集的线路的终点同样遵守第一条；")
                        },0)
                        /*alert End*/
                    } else {
                        var str = document.createTextNode("　　　🔊 用户名或密码错误!")
                        doc.appendChild(str)
                        document.getElementById("remind").appendChild(doc)
                        $("#username").addClass("name-remind") //输入框表格样式标红提示
                        $("#password").addClass("psw-remind")
                        flag = false
                        setTimeout(function () {
                            $("#username").removeClass("name-remind")
                            $("#password").removeClass("psw-remind")
                            document.getElementById("remind").removeChild(doc)
                            $("#username").val("") //重置输入框
                            $("#password").val("") //重置输入框
                            document.getElementById("username").focus() //聚焦
                            flag = true
                        }, 1500)
                    }
                },
                error: function (e) {
                    alert("请求异常")
                }
            })
        }
    }
}