var userId;
$(function () {
    selectUserInfo() //展示用户数据表格
    //回车事件绑定
    //为keyListener方法注册按键事件
    document.onkeydown = keyListener;

    function keyListener(e) {
        //    当按下回车键，执行我们的代码
        if (e.keyCode == 13 && "2" == $(".bx_li_click").attr("index")) {
            searchUserInfo(); //设置回车查询用户数据
        }
    }

    selectTraIdAndOpDeptidInfo() //查询收个用户的旅行社id及运营id
})

var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间初始化
//查询计调部操作员的旅行社id及运营id及操作员id
function selectTraIdAndOpDeptidInfo() {
    var content = ""
    $.ajax({
        url: "selectTraIdAndOpDeptidInfo",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.status == 200) {
                var data = data.userList
                userId = data[0].id //当前用户id
                console.log(data[0])
                console.log(data[0].operate_id)
                $("#search_userId").val(data[0].id) //当前用户id
                $("#search_traId").val(data[0].tra_id) //旅行社id
                $("#search_opDeptid").val(data[0].op_deptid) //运营部id
                $("#search_operatorId").val(data[0].operate_id) //计调部操作员id
                console.log($("#search_operatorId").val())
            }
        },
        error: function () {
            alert("用户数据请求异常!")
        }
    })
}

//查询用户信息
function selectUserInfo() {
    var content = ""
    $.ajax({
        url: "selectUserInfo",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.status == 200) {
                var data = data.userInfoList
                pages(data) //分页展示数据
                /*for (var i = 0; i < data.length; i++) {
                    var email = data[i].email == null ? "" : data[i].email
                    content += '<tr>' +
                        '<td><input class="checkbox" type="checkbox" value="' + data[i].id + '"/></td>' +
                        '<td>' + data[i].id + '</td>' +
                        '<td>' + data[i].nickname + '</td>' +
                        '<td style="display:none">' + data[i].tra_id + '</td>' +
                        '<td style="display:none">' + data[i].op_deptid + '</td>' +
                        '<td>' + email + '</td>' +
                        '<td>' + sdf.format(data[i].create_time) + '</td>' +
                        '<td>' + '<a href="javascript:;" class="td_a_edit" onclick="addOrEditNewUser(this)">修改</a>&nbsp;&nbsp;&nbsp;'
                        + '<a href="javascript:;" class="td_a_del" onclick="delUserInfo(' + data[i].id + ')">删除</a>' + '</td>' +
                        '</tr>'
                }

                $('tbody').html("") //重置
                $('tbody').append(content)*/
            }
        },
        error: function () {
            alert("用户数据请求异常!")
        }
    })
}

//查询用户分页信息(当前页)
function selectUserPageInfo(currentPage) {
    var content = ""
    $.ajax({
        url: "selectUserPageInfo",
        type: "get",
        data: {"currentPage": currentPage},
        dataType: "json",
        success: function (data) {
            if (data.status == 200) {
                var data = data.userPageInfoList
                if (data == "" || data == null) {
                    content = '<td colspan="6">未查询到任何数据</td>'
                } else {
                    for (var i = 0; i < data.length; i++) {
                        var email = data[i].email == null ? "" : data[i].email
                        content += '<tr>' +
                            '<td><input class="checkbox" type="checkbox" value="' + data[i].id + '"/></td>' +
                            '<td>' + data[i].id + '</td>' +
                            '<td>' + data[i].nickname + '</td>' +
                            '<td style="display:none">' + data[i].tra_id + '</td>' +
                            '<td style="display:none">' + data[i].op_deptid + '</td>' +
                            '<td style="display:none">' + data[i].operate_id + '</td>' +
                            '<td>' + email + '</td>' +
                            '<td>' + sdf.format(data[i].create_time) + '</td>' +
                            '<td>' + '<a href="javascript:;" class="td_a_edit" onclick="addOrEditNewUser(this)">修改</a>&nbsp;&nbsp;&nbsp;'
                            + '<a href="javascript:;" class="td_a_del" onclick="delUserInfo(' + data[i].id + ')">删除</a>' + '</td>' +
                            '</tr>'
                    }
                }
                $('tbody').html("") //重置
                $('tbody').append(content)
            }
        },
        error: function () {
            alert("用户数据请求异常!")
        }
    })
}

//根据查询条件查询用户数据
function searchUserInfo() {
    var username = $("#search_username").val() //用户名
    var userId = $("#search_userId").val() //当前登录用户id
    var content = ""
    if (username == "") {
        selectUserInfo() //如果搜索条件为空，则查询所有结果
    } else {
        $.ajax({
            url: "searchUserInfo",
            type: "get",
            data: {
                "username": username,
                "userId": userId
            },
            dataType: "json",
            success: function (data) {
                if (data.status == "200") {
                    var data = data.userInfoList
                    if (data == "" || data == null) {
                        content = '<td colspan="6">未查询到任何数据</td>'
                    } else {
                        // userId = data[0].id //当前用户id
                        for (var i = 0; i < data.length; i++) {
                            var email = data[i].email == null ? "" : data[i].email
                            content += '<tr>' +
                                '<td><input class="checkbox" type="checkbox" value="' + data[i].id + '"/></td>' +
                                '<td>' + data[i].id + '</td>' +
                                '<td>' + data[i].nickname + '</td>' +
                                '<td style="display:none">' + data[i].tra_id + '</td>' +
                                '<td style="display:none">' + data[i].op_deptid + '</td>' +
                                '<td style="display:none">' + data[i].operate_id + '</td>' +
                                '<td>' + email + '</td>' +
                                '<td>' + sdf.format(data[i].create_time) + '</td>' +
                                '<td>' + '<a href="javascript:;" class="td_a_edit" onclick="addOrEditNewUser(this)">修改</a>&nbsp;&nbsp;&nbsp;'
                                + '<a href="javascript:;" class="td_a_del" onclick="delUserInfo(' + data[i].id + ')">删除</a>' + '</td>' +
                                '</tr>'
                        }
                    }
                    $('tbody').html("") //重置
                    $('tbody').append(content)
                } else {
                    showSuccessOrErrorModal("操作失败!", "error")
                }
            },
            error: function () {
                alert("查询用户信息异常")
            }
        })
    }
}

/*分页 Start*/
function pages(datas) {
    var content = ""
    var totalPage = parseInt(datas.length / 10) + 1 //总页数
    if(datas.length % 10 == 0) {
        totalPage = parseInt(datas.length / 10)
    }
    var totalCount = datas.length //数据总条数
    // 调用分页器
    Pagination({
        ele: '#ele',
        totalCount: totalCount, //数据总条数
        totalPage: totalPage, //总页数
        currentPage: 1, //当前页
        needTotalCount: true, //是否显示数据总条数
        callback: function (page) {
            console.log('当前是第' + page + '页')
            selectUserPageInfo(page) //展示当前页数据
        }
    });

    // 分页器
    function Pagination(data) {
        var isInit = true
        var ele = data.ele
        $(ele + '>div').html('')
        var currentPage = data.currentPage ? data.currentPage : 1
        var callback = data.callback ? data.callback : function (pageNum) {
            console.log('当前是第' + pageNum + '页')
            selectUserPageInfo(pageNum)  //展示当前页查询结果
        }

        // 跳转指定页函数
        function inputPage() {
            if (!isInit) {
                if (currentPage == $(ele + ' input').val()) return $(ele + ' input').val('')
                currentPage = $(ele + ' input').val()
            }
            if (($(ele + ' input').val() > 9 && data.totalPage > 9) || (isInit && currentPage > 10)) {
                if (currentPage % 10 < 5) {
                    var a = (currentPage - currentPage % 10) / 10
                    var b = a - 1
                    var c = a + b
                    $(ele + ' ul').animate({left: -160 * c})
                    if (currentPage - (Math.floor(data.totalPage / 10) * 10) >= 0 && currentPage - (Math.floor(data.totalPage / 10) * 10) <= 5) {
                        if (data.totalPage % 10 >= 5 && data.totalPage - currentPage <= data.totalPage - Math.floor(data.totalPage / 10) * 10 - 5) {
                            $(ele + ' .list_box').animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10 + 5) * 32})
                        } else if (data.totalPage % 10 < 5 && data.totalPage - currentPage <= data.totalPage - Math.floor(data.totalPage / 10) * 10) {
                            $(ele + ' .list_box').animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10 + 5) * 32})
                        } else {
                            $(ele + ' .list_box').width(90)
                        }
                    } else {
                        $(ele + ' .list_box').animate({width: 90})
                    }
                } else {
                    $(ele + ' ul').animate({left: -160 * ((currentPage - currentPage % 10) / 5)})
                    if (currentPage - (Math.floor(data.totalPage / 10) * 10 + 5) >= 0 && currentPage - (Math.floor(data.totalPage / 10) * 10 + 5) <= 5) {
                        $(ele + ' .list_box').animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10) * 32})
                    } else if (currentPage - (Math.floor(data.totalPage / 10) * 10) >= 0 && currentPage - (Math.floor(data.totalPage / 10) * 10) <= 5) {
                        $(ele + ' .list_box').animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10) * 32})
                    } else {
                        $(ele + ' .list_box').animate({width: 360})
                    }
                }

            } else {
                $(ele + ' ul').animate({left: 0})
                $(ele + ' .list_box').animate({width: totalPage * 30})
            }
            $(ele + ' input').val('')
            $(ele + ' ul li').eq(currentPage - 1).addClass('Pagination_active').siblings().removeClass('Pagination_active')
            callback(currentPage)
        }

        if (data.needTotalCount) {
            $(ele + ' .Pagination').append('<span class="total_num">共<i></i>条</span>')
            $(ele + ' .Pagination .total_num i').text(data.totalCount)
        }
        var PaginationLiList = ''
        if (data.totalCount != 0) {
            $(ele + ' .Pagination').append('<span class="first_page">首页</span>' +
                '<span class="prepage">上一页</span>' +
                '<div class="list_box clearfix"><ul></ul></div>' +
                '<span class="nextpage">下一页</span>' +
                '<span class="last_page">尾页</span>' +
                '<span class="total_page">共' + data.totalPage + '页</span>' +
                '<input type="text">' +
                '<span class="redir">跳转</span>')
            if (data.totalPage < 10) {
                $(ele + ' .Pagination .list_box').width(data.totalPage * 32)
            }
            inputPage()
            isInit = false
            for (var i = 0; i < data.totalPage; i++) {
                var j = i + 1
                if (i == currentPage - 1) {
                    var li = '<li class="Pagination_active" data-page="' + j + '">' + j + '</li>'
                } else {
                    var li = '<li data-page="' + j + '">' + j + '</li>'
                }
                PaginationLiList += li
            }
            $(ele + ' .Pagination ul').html('')
            $(ele + ' .Pagination ul').html(PaginationLiList)
            $(ele + ' .Pagination ul').width(data.totalPage * 32)
            // 页码点击跳到指定页数
            $(ele + ' .Pagination ul').on('click', 'li', function () {
                currentPage = $(this).data("page")
                if (!$(this).hasClass('Pagination_active')) {
                    $(this).addClass('Pagination_active').siblings().removeClass('Pagination_active')
                    if (((currentPage / 10) % 1 == 0 || ((currentPage / 5) % 1 == 0 && currentPage > 5)) && currentPage != data.totalPage) {
                        if (currentPage % 2 == 0) {
                            var a = currentPage / 10
                            var b = a - 1
                            var c = a + b
                            $(ele + ' .Pagination ul').animate({left: -160 * c})
                        } else {
                            $(ele + ' .Pagination ul').animate({left: -160 * ((currentPage / 5) - 1)})
                        }
                        if (data.totalPage - currentPage < 5) {
                            $(ele + ' .Pagination .list_box').animate({width: (data.totalPage - currentPage + 5) * 32})
                        }
                    }
                    if (((currentPage - 1) / 5) % 1 == 0 && currentPage > 5 && currentPage != data.totalPage) {
                        $(ele + ' .Pagination ul').animate({left: -160 * (((currentPage - 1) / 5) - 1)})
                        if (data.totalPage - currentPage >= 5) {
                            $(ele + ' .Pagination .list_box').animate({width: 320})
                        }
                    }
                    callback(currentPage)
                }
            })
            // 跳到第一页
            $(ele + ' .Pagination .first_page').on('click', function () {
                if (currentPage != 1) {
                    $(ele + ' .Pagination ul li').eq(0).addClass('Pagination_active').siblings().removeClass('Pagination_active')
                    $(ele + ' .Pagination ul').animate({left: 0})
                    if (data.totalPage > 9) {
                        $(ele + ' .Pagination .list_box').animate({width: 320})
                    } else {
                        $(ele + ' .Pagination .list_box').animate({width: data.totalPage * 32})
                    }
                    currentPage = 1
                    callback(currentPage)
                }
            })
            // 跳到最后一页
            $(ele + ' .Pagination .last_page').on('click', function () {
                if (currentPage != data.totalPage) {
                    currentPage = data.totalPage
                    $(ele + ' .Pagination ul li').eq(data.totalPage - 1).addClass('Pagination_active').siblings().removeClass('Pagination_active')
                    if (data.totalPage > 10) {
                        if (data.totalPage % 10 <= 5) {
                            var a = (data.totalPage - data.totalPage % 10) / 10
                            var b = a - 1
                            var c = a + b
                            $(ele + ' .Pagination ul').animate({left: -160 * c})
                            $(ele + ' .Pagination .list_box').stop().animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10 + 5) * 32})
                        } else {
                            $(ele + ' .Pagination ul').animate({left: -160 * ((data.totalPage - data.totalPage % 10) / 5)})
                            $(ele + ' .Pagination .list_box').stop().animate({width: (data.totalPage - Math.floor(data.totalPage / 10) * 10) * 32})
                        }
                    }
                    callback(currentPage)
                }
            })
            // 上一页
            $(ele + ' .Pagination .prepage').on('click', function () {
                if (currentPage > 1) {
                    currentPage--
                    if (((currentPage - 1) / 5) % 1 == 0 && currentPage > 5) {
                        $(ele + ' .Pagination ul').animate({left: -160 * (((currentPage - 1) / 5) - 1)})
                        if (data.totalPage - currentPage >= 4) {
                            $(ele + ' .Pagination .list_box').animate({width: 320})
                        }
                    }
                    $(ele + ' .Pagination ul li').eq(currentPage - 1).addClass('Pagination_active').siblings().removeClass('Pagination_active')
                    callback(currentPage)
                }
            })
            // 下一页
            $(ele + ' .Pagination .nextpage').on('click', function () {
                if (currentPage < data.totalPage) {
                    if (currentPage > 9) {
                        if (((currentPage / 10) % 1 == 0 || ((currentPage / 5) % 1 == 0 && currentPage > 5)) && currentPage != data.totalPage) {
                            if (currentPage % 2 == 0) {
                                var a = currentPage / 10
                                var b = a - 1
                                var c = a + b
                                $(ele + ' .Pagination ul').animate({left: -160 * c})
                            } else {
                                $(ele + ' .Pagination ul').animate({left: -160 * ((currentPage / 5) - 1)})
                            }
                            if (data.totalPage - currentPage < 5) {
                                $(ele + ' .Pagination .list_box').animate({width: (data.totalPage - currentPage + 5) * 32})
                            }
                        }
                    }
                    currentPage++

                    $(ele + ' .Pagination ul li').eq(currentPage - 1).addClass('Pagination_active').siblings().removeClass('Pagination_active')
                    callback(currentPage)
                }
            })
            // 输入跳转指定页
            $(ele + ' .Pagination input').on('keydown', function (e) {
                if (e.keyCode == 13 && $(this).val() <= data.totalPage && $(this).val() != 0) {
                    inputPage()
                }
            })
            // 转跳按钮
            $(ele + ' .Pagination .redir').on('click', function () {
                if ($(ele + ' .Pagination input').val() <= data.totalPage && $(ele + ' input').val() != 0) {
                    inputPage()
                }
            })
        }
    }
}

/*分页 End*/

//删除用户数据
function delUserInfo(id) {
    if (confirm("删除该用户后，该用户下的所有用户也将被删除，是否确认删除？")) {
        // alert("删除成功")
        $.ajax({
            url: "delUserInfo",
            type: "post",
            data: {
                "id": id
            },
            dataType: "json",
            success: function (data) {
                if (data.status == "200") {
                    selectUserInfo() //刷新表格数据
                    showSuccessOrErrorModal("操作成功", "success");
                } else {
                    showSuccessOrErrorModal("操作失败!", "error")
                }
            },
            error: function () {
                alert("删除用户信息异常")
            }
        })
    }
}

//新增用户信息
function addOrEditNewUser(obj) {
    var username = obj.parentNode.parentNode.childNodes[2].innerHTML //用户名
    var tra_id = obj.parentNode.parentNode.childNodes[3].innerHTML //旅行社id
    var op_deptid = obj.parentNode.parentNode.childNodes[4].innerHTML //运营部id
    var email = obj.parentNode.parentNode.childNodes[6].innerHTML //邮箱
    // 获取弹窗
    var modal = document.getElementById('addOrEditModal')
    modal.style.display = "block"; //设置弹窗显示
    $("#addOrEditModal").find('h3').text(obj.innerHTML) //修改弹窗标题
    if ("修改" == obj.innerHTML) {  //修改
        var modal_content = '<div id="content_add_edit"><div><label for="">用户名：&nbsp;&nbsp;&nbsp;</label>' +
            '<input id="userId_modal" value="' + userId + '" style="display: none;">' +
            '<input id="traId_modal" value="' + tra_id + '" style="display: none;">' +
            '<input id="opDeptid_modal" value="' + op_deptid + '" style="display: none;">' +
            '<div id="username_modal" style="display: inline">' + username + '</div></div>' +
            '<div><label for="">&nbsp;&nbsp;&nbsp;邮箱：&nbsp;&nbsp;&nbsp;</label>' +
            '<input id="email_modal" style="height: 21px;" placeholder="请输入邮箱地址" value="' + email + '"></div>' +
            // '<div><label for="">&nbsp;&nbsp;&nbsp;密码：&nbsp;&nbsp;&nbsp;</label>' +
            // '<input id="password_modal" type="password" style="height: 21px;" placeholder="请输入用户密码" value="' + pswd + '"></div>' +
            '</div>'
        $(".user_modal-body").html("") //重置
        $(".user_modal-body").append(modal_content) //添加内容
    } else {  //新增
        var modal_content = '<div id="content_add_edit"><div><label for="">用户名：&nbsp;&nbsp;&nbsp;</label>' +
            '<input id="userId_modal" value="' + userId + '" style="display: none;">' +
            '<input id="traId_modal" value="' + $("#search_traId").val() + '" style="display: none;">' +
            '<input id="opDeptid_modal" value="' + $("#search_opDeptid").val() + '" style="display: none;">' +
            '<input id="operateId_modal" value="' + $("#search_operatorId").val() + '" style="display: none;">' +
            '<input id="username_modal" style="height: 21px;" placeholder="请输入用户名"></div>' +
            '<div><label for="">&nbsp;&nbsp;&nbsp;邮箱：&nbsp;&nbsp;&nbsp;</label>' +
            '<input id="email_modal" style="height: 21px;" placeholder="请输入邮箱地址"></div>' +
            '<div><label for="">&nbsp;&nbsp;&nbsp;密码：&nbsp;&nbsp;&nbsp;</label>' +
            '<input id="password_modal" type="password" style="height: 21px;" placeholder="请输入用户密码"></div></div>'
        $(".user_modal-body").html("") //重置
        $(".user_modal-body").append(modal_content) //添加内容
    }

}

//关闭新增模态框
function user_close() {
    // 获取弹窗
    var modal = document.getElementById('addOrEditModal');
    modal.style.display = "none"; //设置模态框隐藏
}

/*保存用户信息*/
var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/ //验证邮箱有效性的正则
function addOrEditUserInfo() {
    var title = $("#addOrEditModal").find("h3").text()
    var parent_id = $("#userId_modal").val()
    var username = $("#username_modal").val()
    var email = $("#email_modal").val() //邮箱地址
    var password = $("#password_modal").val()
    var tra_id = $("#traId_modal").val() //旅行社id
    var op_deptid = $("#opDeptid_modal").val() //运营部id
    var operate_id = $("#operateId_modal").val() //计调部操作员id
    if ("修改" == title) {
        username = $("#username_modal").text()
    }
    if (username == null || username == "") {
        sweetAlert("请输入用户名！")
    } else if ((password == null || password == "") && "修改" != title) {
        sweetAlert("请输入用户密码！")
    } else {
        if (!reg.test(email)) {
            sweetAlert("请输入有效的邮箱地址!")
        } else {
            if ("修改" == title) {
                updateUserInfo(username, email)
            } else {
                /*查询用户名是否存在*/
                $.ajax({
                    url: "checkUsername",
                    type: "get",
                    data: {
                        "username": username
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.status == "200") {
                            sweetAlert("该用户名已存在！")
                            $("#username_modal").val("")
                            $("#password_modal").val("")
                        } else {
                            saveUserInfo(username, password, email, parent_id, tra_id, op_deptid, operate_id)
                        }
                    },
                    error: function () {
                        alert("请求异常异常")
                    }
                })
            }
        }
    }
}

/*保存用户信息*/
function saveUserInfo(username, password, email, parent_id, tra_id, op_deptid, operate_id) {
    $.ajax({
        url: "saveUserInfo",
        type: "get",
        data: {
            "username": username,
            "email": email,
            "password": password,
            "parent_id": parent_id,
            "tra_id": tra_id,
            "op_deptid": op_deptid,
            "operate_id": operate_id
        },
        dataType: "json",
        success: function (data) {
            if (data.status == "200") {
                user_close() //关闭模态框
                selectUserInfo() //刷新表格数据
                showSuccessOrErrorModal("用户信息保存成功", "success");
            } else {
                showSuccessOrErrorModal("用户信息保存失败!", "error")
            }
        },
        error: function () {
            alert("保存用户信息异常")
        }
    })
}

/*修改用户信息*/
function updateUserInfo(username, email) {
    $.ajax({
        url: "updateUserInfo",
        type: "get",
        data: {
            "username": username,
            "email": email
        },
        dataType: "json",
        success: function (data) {
            if (data.status == "200") {
                user_close() //关闭模态框
                selectUserInfo() //刷新表格数据
                showSuccessOrErrorModal("用户信息修改成功", "success");
            } else {
                showSuccessOrErrorModal("用户信息修改失败!", "error")
            }
        },
        error: function () {
            alert("修改用户信息异常")
        }
    })
}

//批量删除用户数据
function batchDelUsersInfo() {
    var inputs = document.getElementsByClassName("checkbox"); //获取所有复选框
    var num = 0 // 计算复选框选中的个数的变量
    var ids = "" //选中的用户id字符串
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
            num++
            ids += inputs[i].value + "," //拼接选中的用户id字符串
        }
    }
    console.log("选中的个数为：" + num)
    console.log("选中的用户的id拼接后的字符串为：" + ids)
    if (num == 0) {
        alert("请选择需要删除的用户!")
    } else {
        if (confirm("是否确认批量删除？")) {
            $.ajax({
                url: "batchDelUsersInfo",
                type: "post",
                data: {
                    "id": ids
                },
                dataType: "json",
                success: function (data) {
                    if (data.status == "200") {
                        selectUserInfo() //刷新表格数据
                        showSuccessOrErrorModal("操作成功", "success");
                    } else {
                        showSuccessOrErrorModal("操作失败!", "error")
                    }
                },
                error: function () {
                    alert("删除用户信息异常")
                }
            })
        }
    }
}

/**
 * @Author Breach
 * @Description 全选、取消全选
 * @Date 2019/2/21
 */
function siteChooseOrNoChoose(obj) {
    var flag = $(obj).prop("checked");
    if (flag) {
        $(".checkbox").prop("checked", true);
        $(".checkbox:disabled").prop("checked", false);
    } else {
        $(".checkbox").prop("checked", false);
    }
}

/**
 * 时间格式化
 */
function SimpleDateFormat(pattern) {
    var fmt = new Object();
    fmt.pattern = pattern;

    fmt.parse = function (source) {
        try {
            return new Date(source);
        } catch (e) {
            console.log("字符串 " + source + " 转时间格式失败！");
            return null;
        }
    };

    fmt.format = function (date) {
        if (typeof(date) == "undefined" || date == null || date == "") {
            return "";
        }

        try {
            date = new Date(date);
        } catch (e) {
            console.log("时间 " + date + " 格式化失败！");
            return "";
        }

        var strTime = this.pattern;//时间表达式的正则

        var o = {
            "M+": date.getMonth() + 1, //月份
            "d+": date.getDate(), //日
            "H+": date.getHours(), //小时
            "m+": date.getMinutes(), //分
            "s+": date.getSeconds(), //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };

        if (/(y+)/.test(strTime)) {
            strTime = strTime
                .replace(RegExp.$1, (date.getFullYear() + "")
                    .substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(strTime)) {
                strTime = strTime.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }

        return strTime;
    };
    return fmt;
}

