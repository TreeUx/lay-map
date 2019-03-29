var map;    //创建地图
var marker; //覆盖物
var mapType; //地图类型
var cpLock = true;
var enterAndExitArr = new Array() //当前搜索的景点的出入口坐标数组
/*坐标转换需要的常量 Start*/
var PI = 3.1415926535897932384626;
var a = 6378245.0;
var ee = 0.00669342162296594323;
/*坐标转换需要的常量 End*/
var enter_flag = true;
var container = document.getElementById('popup'); //获取右键菜单div id
/*右键菜单 Start*/
var CONTENT = '<div id="contextmenu_container" class="contextmenu">' +
    '        <ul>' +
    '            <li><a href="#" onclick="showAddModel()">添加景点单元</a></li>' +
    '            <li><a href="#">开始景区路线采集</a></li>' +
    '            <li><a href="#">景区路线采集完毕</a></li>' +
    '            <li><a href="#">取消该条路线</a></li>' +
    '            <li><a href="#">采集出入口</a></li>' +
    '            <li><a href="#">结束出入口采集</a></li>' +
    '            <li><a href="#">线路规划</a></li>' +
    '        </ul>' +
    '    </div>'
/*右键菜单 End*/

$(function () {
    addRightMenu() //右键菜单
    /*切换地图 Start*/
    $("#map_type_sel").on("change", function () { //监控切换地图
        mapType = $("select  option:selected").val() //获取选中的下拉选的值
        // var queryValue = $("#input_name").val() //搜索框的值
        var type = $("#map_type_sel option:selected").val()
        switch (type) {
            case "bd-map":
                targetStr = baiduMapLayer // AMapLayer googleMapLayer
                console.log("调用高德初始化地图")
                break
            case "gd-map":
                targetStr = AMapLayer
                console.log("调用高德初始化地图")
                break
            case "gg-map":
                console.log("调用高德初始化地图")
                targetStr = googleMapLayer
                break
            default:
                targetStr = baiduMapLayer
                console.log("调用默认初始化地图")
                break
        }
        if ($("#input_name").val() != "" && $("#input_name").val() != null) {
            queryMapInfo() //调用查询
        } else {
            /*调用地图 Start*/
            $("#bx_laymap").html("")
            map = new ol.Map({
                layers: [targetStr],//AMapLayer, baiduMapLayer, googleMapLayer
                target: 'bx_laymap',
                view: new ol.View({
                    center: ol.proj.fromLonLat([114.065127, 22.548189]),
                    zoom: 14
                })
            })
            addRightMenu() //右键菜单
            /*调用地图 End*/
        }
        /*添加点击标注后的右键菜单 Start*/
        map.addEventListener('click', function(evt) {
            var overlay = new ol.Overlay({
                //设置弹出框的容器
                element: container,
                //是否自动平移，即假如标记在屏幕边缘，弹出时自动平移地图使弹出框完全可见
                autoPan: true,
                autoPanAnimation: {
                    duration: 250   //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度. 单位为毫秒（ms）
                }
            });
            coordinate = evt.coordinate;
            var pixel = map.getEventPixel(evt.originalEvent);
            map.forEachFeatureAtPixel(pixel, function (feature) {
                console.log(feature)
                /*var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
                    coordinate, 'EPSG:3857', 'EPSG:4326'));
                content.innerHTML = '<p>你点击的坐标是：</p><code>' + hdms + '</code>';
                overlay.setPosition(coordinate);
                map.addOverlay(overlay);*/

                overlay.setPosition(coordinate);
                //显示overlay
                map.addOverlay(overlay);
                $("a").click(function () { //添加鼠标左键点击菜单项时，右键菜单隐藏
                    overlay.setPosition(undefined);
                })
            })
        });
        /*添加点击标注后的右键菜单 End*/

    })
    /*切换地图 End*/
    //展开、隐藏
    $("#tog").click(function () {
        var t = $(this).text();
        if (t == "折叠") {
            $(this).text("展开");
            $("#scenery_box").fadeOut("3000");
            $("#bx_laymap").css("margin-top", "45px") //设置地图离页面顶部的距离
        } else {
            $("#scenery_box").fadeTo("3000", 0.9)
            $("#bx_laymap").css("margin-top", "220px") //设置地图离页面顶部的距离
            $(this).text("折叠");
        }
    });

    //搜索提示 start
    $('#input_name').off().bind({
        //中文输入开始
        compositionstart: function () {
            cpLock = false;
        },
        //中文输入结束
        compositionend: function () {
            cpLock = true;
        },
        //input框中的值发生变化
        input: function () {
            setTimeout(function () {
                if (cpLock) {
                    //这里处理中文输入结束的操作
                    var scenery_name = $("#input_name").val()
                    var map_type = $("#map_type_sel option:selected").val()
                    querySceneryInfo(scenery_name, map_type) //根据搜索的名称展示对应的地图信息
                    $("#input_name").bind("keyup", function () {//为搜索框添加回车事件
                        // console.log(event.keyCode)
                        if (event.keyCode == 13 && "1" == $(".bx_li_click").attr("index")) {
                            if (enter_flag) { //防止多次搜索
                                queryMapInfo();//调用查询
                                console.log("触发回车")
                                enter_flag = false
                                setTimeout(function () {
                                    enter_flag = true
                                }, 50)
                            }
                            return false;
                        }
                    })
                }
            }, 10)
        }
    })
    //搜索提示 end

    /*点击标题添加样式 Start*/
    $("#bx_title_ul li").click(function () {
        //先移除兄弟的背景
        $("#bx_title_ul").find('li').removeClass("bx_li_click")
        $(this).addClass('bx_li_click');
        var index = $(this).attr("index")
        switch (index) {
            case "1":
                $("#bx_user_manage").addClass("hide_and_show")
                $("#bx_content").removeClass("hide_and_show")
                break
            case "2":
                $("#bx_content").addClass("hide_and_show")
                $("#bx_user_manage").removeClass("hide_and_show")
                break
            default :
                $("#bx_user_manage").addClass("hide_and_show")
                $("#bx_content").removeClass("hide_and_show")
                break
        }
    });
    /*点击标题添加样式 End*/

});
/*主函数End*/

//地图添加右键菜单
var coordinate //右键点击处的坐标信息
var menu_overlay; //添加景点的右键菜单
function addRightMenu() {
    // $("#bx_content").find("#contextmenu_container").remove()
    $("#bx_laymap").append(CONTENT)
    menu_overlay = new ol.Overlay({ //右键菜单
        element: document.getElementById("contextmenu_container"),
        positioning: 'center-center'
    });
    /*鼠标右键监听 Start*/
    $(map.getViewport()).on("contextmenu", function (event) {
        event.preventDefault(); //屏蔽自带的右键事件
        menu_overlay.setMap(map);
        coordinate = map.getEventCoordinate(event.originalEvent); //获取右键点击处的坐标信息
        menu_overlay.setPosition(coordinate)
        $("a").click(function () { //添加鼠标左键点击菜单项时，右键菜单隐藏
            menu_overlay.setPosition(undefined);
        })
    });
    /*鼠标右键监听 End*/

    $(map.getViewport()).on("click", function (e) {
        e.preventDefault();
        menu_overlay.setPosition(undefined);
    });
}

var time;//申明全局变量
function getTimeNow() {//获取此刻时间
    var now = new Date();
    return now.getTime();
}

/*百度转GPS坐标修正 Start*/
var transedPointStrs = "" //transedPointStrs转换坐标后的字符串
function transToGps(pointList) { //pointList转换前的坐标点数组
    var pointArr = new Array()
    var index = 0
    for (var i = 0; i < pointList.length; i++) {
        var convertor = new BMap.Convertor()
        var x1 = pointList[i].lng
        var y1 = pointList[i].lat
        pointArr.push(pointList[i]);
        index++
    }

    var len = pointArr.length
    if (len <= 10) {
        convertor.translate(pointArr, 5, 3, function (data) {
            console.log(data)
            if (data.status === 0) {
                for (var k = 0; k < pointArr.length; k++) {
                    var x2 = data.points[k].lng
                    var y2 = data.points[k].lat
                    var tempPoint = gcj02towgs84(x2, y2)
                    var x = tempPoint.lng
                    var y = tempPoint.lat
                    console.log(x + "," + y)
                    transedPointStrs = transedPointStrs + (x.toFixed(10) + "," + y.toFixed(10) + " ")  //拼接转换后的坐标
                }
                setTimeout(function () {
                    if (index == pointList.length) { //保存线路点信息
                        debugger
                        var end_pointbl = false //判断最后的坐标点是否是出入口坐标使用
                        for (let i = 0; i < enterAndExitArr.length; i++) {
                            let end_point = pointList[pointList.length - 1] //采集线路的最后一个点坐标
                            if (end_point.equals(enterAndExitArr[i])) {
                                end_pointbl = true
                            }
                        }
                        if (!end_pointbl) {
                            alert("终点必须是出入口坐标!")
                        } else {
                            addPointlineInfos(transedPointStrs, pointList)
                            transedPointStrs = "" //重置
                        }
                    }
                }, 50)
            }
        })
    } else {
        var math = parseInt(len / 10)
        var index1 = 0
        var index2 = 0
        var pointLists = new Array()
        for (var m = 0; m < math + 1; m++) {
            translateCallback = function (data) { //百度坐标系转换为原始坐标系（回调函数）
                debugger
                if (data.status === 0) {
                    for (var t = 0; t < data.points.length; t++) {
                        var x2 = data.points[t].lng //百度经度
                        var y2 = data.points[t].lat //百度纬度
                        var tempPoint = gcj02towgs84(x2, y2) //百度坐标系转换大地坐标系
                        var x = tempPoint.lng //大地坐标系经度
                        var y = tempPoint.lat //大地坐标系纬度
                        transedPointStrs = transedPointStrs + (x.toFixed(10) + "," + y.toFixed(10) + " ")  //拼接转换后的大地坐标
                    }
                    index2++
                    if (index2 == (math + 1)) { //保存线路点信息
                        debugger
                        console.log(transedPointStrs)
                        var end_pointbl = false //判断最后的坐标点是否是出入口坐标
                        for (let i = 0; i < enterAndExitArr.length; i++) {
                            let end_point = pointList[pointList.length - 1] //采集线路的最后一个点坐标
                            if (end_point.equals(enterAndExitArr[i])) {
                                end_pointbl = true
                            }
                        }
                        if (!end_pointbl) {
                            alert("终点必须是出入口坐标!")
                        } else {
                            addPointlineInfos(transedPointStrs, pointList) //保存采集后的线路数据
                            transedPointStrs = "" //重置
                            index2 = 0
                        }
                    }
                }
            }
            pointLists = pointArr.slice(index1 * 10, index1 * 10 + 10) //存放一组10个坐标
            index1++
            console.log(pointLists)
            convertor.translate(pointLists, 5, 3, translateCallback) //百度坐标系转换为原始坐标系
        }
    }
}

/*百度转GPS坐标修正 End*/

/**
 * @Author Breach
 * @Description 输入名称进行搜索（根据输入内容查询相应地图信息）
 * @Date 2018/12/20
 */
function queryMapInfo() {
    mapType = $("#map_type_sel").val() //地图类型
    var scenery_name = $("#input_name").val() //搜索名称
    var map_type = $("#map_type_sel option:selected").val() //地图类型
    $.ajax({
        url: "queryCenterPoi",
        type: "post",
        data: {
            "scenery_name": scenery_name,
            "mapType": mapType
        },
        dataType: "json",
        success: function (data) {
            if (data.status == 200) {
                var lng = data.lng //获取百度地图经纬度
                var lat = data.lat //获取百度地图经纬度
                showSceneryInfoMap(lng, lat, map_type);//展示地图查询
            } else {
                showSuccessOrErrorModal(data.msg, "error");
            }
        },
        error: function (e) {
            showSuccessOrErrorModal("网络异常！", "error");
        }
    })
}

//获取景区信息（搜索提示）
function querySceneryInfo(scenery_name, map_type) {
    if (scenery_name != "" && scenery_name != null) {
        $.ajax({
            url: "querySceneryInfo",
            type: "post",
            data: {
                "scenery_name": scenery_name
            },
            dataType: "json",
            success: function (data) {
                if (data.status == "success") {
                    data = $.parseJSON(JSON.stringify(data)).data
                    if (data != "" && data != undefined) {
                        $("#input_name").autocomplete({//输入框提示
                            matchSubset: true, //是否启用缓存
                            delay: 50, //指定在按键发生后多少毫秒后才触发执行自动完成
                            source: data,
                            select: function (e, ui) { //Autocomplete的结果列表任意一项选中时，ui.item为选中的项
                                queryMapInfo() //选值后自动搜索景点地图数据
                                console.log("选值搜索")
                            }
                        });
                    }
                    fireKeyEvent(document.getElementById("input_name"), 'keydown', 49); //模拟操作键盘方向键

                } else {
                    showSuccessOrErrorModal(data.msg, "error");
                }
            },
            error: function (e) {
                showSuccessOrErrorModal("网络异常！", "error");
            }
        });
    }
}

/**
 * @Author Breach
 * @Description 根据搜索的名称展示相应的地图
 * @return
 */
var temp_flag = true //控制地图展示问题
function showSceneryInfoMap(lng, lat, map_type) {
    switch (map_type) {
        case "bd-map": //百度搜索
            targetStr = baiduMapLayer // AMapLayer googleMapLayer
            break
        case "gd-map": //高德搜索
            targetStr = AMapLayer
            console.log("调用了高德地图")
            break
        case "gg-map": //谷歌搜索
            targetStr = googleMapLayer
            console.log("调用了谷歌地图")
            temp_flag = true //控制高德地图搜索展示问题
            break
        default: //默认百度搜索
            targetStr = baiduMapLayer
            console.log("调用了默认地图")
            temp_flag = true //控制高德地图搜索展示问题
            break
    }
    /*调用地图 Start*/
    $("#bx_laymap").html("")
    map = new ol.Map({
        layers: [targetStr],//AMapLayer, baiduMapLayer, googleMapLayer
        target: 'bx_laymap',
        view: new ol.View({
            center: ol.proj.fromLonLat([lng, lat]),
            zoom: 14
        })
    })
    addRightMenu() //右键菜单
    /*调用地图 End*/

    /*添加点击标注后的右键菜单 Start*/
    map.addEventListener('click', function(evt) {
        var overlay = new ol.Overlay({
            //设置弹出框的容器
            element: container,
            //是否自动平移，即假如标记在屏幕边缘，弹出时自动平移地图使弹出框完全可见
            autoPan: true,
            autoPanAnimation: {
                duration: 250   //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度. 单位为毫秒（ms）
            }
        });
        coordinate = evt.coordinate;
        var pixel = map.getEventPixel(evt.originalEvent);
        map.forEachFeatureAtPixel(pixel, function (feature) {
            console.log(feature)
            /*var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
                coordinate, 'EPSG:3857', 'EPSG:4326'));
            content.innerHTML = '<p>你点击的坐标是：</p><code>' + hdms + '</code>';
            overlay.setPosition(coordinate);
            map.addOverlay(overlay);*/

            overlay.setPosition(coordinate);
            //显示overlay
            map.addOverlay(overlay);
            $("a").click(function () { //添加鼠标左键点击菜单项时，右键菜单隐藏
                overlay.setPosition(undefined);
            })
        })
    });
    /*添加点击标注后的右键菜单 End*/

}

/*function showSceneryInfoMap(scenery_name, map_type) {
    switch (map_type) {
        case "bd-map": //百度搜索
            map = new BMap.Map("bx_laymap", {enableMapClick: false}); //enableMapClick:false关闭地图可点功能
            map.enableScrollWheelZoom(true);//滚动缩放
            map.centerAndZoom(scenery_name, 14);//初始化地图，设置地图级别
            console.log("调用了百度地图")
            temp_flag = true //控制高德地图搜索展示问题
            break
        case "gd-map": //高德搜索
            if (temp_flag) { //控制地图展示问题
                map = new AMap.Map("bx_laymap", {
                    resizeEnable: true
                })
                temp_flag = false
            }
            AMap.service(["AMap.PlaceSearch"], function () {
                //构造地点查询类
                var placeSearch = new AMap.PlaceSearch({
                    map: map // 展现结果的地图实例
                });
                //关键字查询
                placeSearch.search(scenery_name);
            });
            console.log("调用了高德地图")
            break
        case "gg-map": //谷歌搜索
            console.log("调用了谷歌地图")
            $("#bx_laymap").html("<iframe width=\"100%\" height=\"100%\" frameborder=\"0\" style=\"border:0\" src=\"https://www.google.com/maps/embed/v1/search?zoom=14&q=" + scenery_name + "&key=AIzaSyDFNA6uiTeujVvWsfXJ6l_MYNp7Np5rhgw\" allowfullscreen></iframe>")
            temp_flag = true //控制高德地图搜索展示问题
            break
        default: //默认百度搜索
            map = new BMap.Map("bx_laymap", {enableMapClick: false}); //enableMapClick:false关闭地图可点功能
            map.enableScrollWheelZoom(true);//滚动缩放
            map.addControl(new BMap.GeolocationControl());  // 定位控件
            map.centerAndZoom(scenery_name, 14);//初始化地图，设置地图级别
            console.log("调用了默认地图")
            temp_flag = true //控制高德地图搜索展示问题
            break
    }
    addRightMenu() //右键菜单
}*/

//提交
function submitSceneryInfo(e) {//lng, lat为当前点击的点的经纬度坐标
    var objReg = /^[0-9]+$/;  //正则判断最佳游玩时间是否为正整数
    title = "必填项提示"
    var continents = $("#continents").val() == null ? "" : $("#continents").val()
    var state = $("#state").val() == null ? "" : $("#state").val()
    var city = $("#city").val() == null ? "" : $("#continents").val()
    //表单验证
    if ($("#scenery_name").val() == "") {//景点名称
        msg = "请输入景点名称"
        showWarning(title, msg)
    } else if (continents == "" && $("#continents").find("option").length != 0) {//国家
        msg = "请选择地区"
        showWarning(title, msg)
    } else if ($("#state").val() == "" && $("#state").find("option").length != 0) {//省份
        msg = "请选择国家"
        showWarning(title, msg)
    } else if ($("#city").val() == "" && $("#city").find("option").length != 0) {//城市
        msg = "请选择城市"
        showWarning(title, msg)
    } else if ($("#scenery_address").val() == "") {//详细地址
        msg = "请输入详细地址"
        showWarning(title, msg)
    } else if (!characterBl) {//景点特色
        msg = "请选择资源特色"
        showWarning(title, msg)
    } else if ($("#scenery_type").val() == "") {//类型
        msg = "请选择类型"
        showWarning(title, msg)
    } else if ($("#scenery_start_time").val() == "") {//开放时间
        msg = "请输入开放时间"
        showWarning(title, msg)
    } else if ($("#scenery_end_time").val() == "") {//开放时间
        msg = "请输入开放时间"
        showWarning(title, msg)
    } else if ($("#com_best").val() == "") {//最佳游玩时间
        msg = "请输入游玩时间"
        showWarning(title, msg)
    } else if (!objReg.test($("#com_best").val())) {//最佳游玩时间
        msg = "请输入有效游玩时间"
        showWarning(title, msg)
    } else { //提交表单
        console.log("提交表单")
        saveNewSceneryInfo(e)// 调用新增函数
    }
}

/**
 * @Author Breach
 * @Description 新增采集的景区单元信息
 * @Date 2018/12/19
 */
function saveNewSceneryInfo(e) {
    $.ajax({
        url: "addNewSceneryInfo",
        type: "post",
        data: $("#addSceneryModalForm").serialize(),
        dataType: "json",
        success: function (data) {
            if (data.status == "success") {
                console.log(data)
                // map.closeInfoWindow()
                clear() //重置添加模态框内容
                $("#myAddSceneryInfoModal").modal("hide") //隐藏添加模态框
                showSuccessOrErrorModal(data.msg, "success");//保存成功后，需要添加一个标记点
                var lng = data.lng
                var lat = data.lat
                addVectorLayer(map, lng, lat) //添加标记点
                console.log(lng + "," + lat + "位置添加标注成功")
                var comCode = data.comCode;//获取保存的景点的商品编码
                // var lng = data.lng //转换后的坐标
                // var lat = data.lat//转换后的坐标
                // addMarkImg(map, lng, lat, comCode, e);//添加标记
            } else {
                showSuccessOrErrorModal(data.msg, "error");
            }
        },
        error: function (e) {
            showSuccessOrErrorModal("网络异常！", "error");
        }
    });
}

//修改景点信息
function updateSceneryInfo(map, lng, lat, title, marker) {
    console.log(marker)
    var comCode = marker.customData.myProperty
    $.ajax({
        url: "querySceneryInfoByCode",
        type: "post",
        data: {"comCode": comCode},
        dataType: "json",
        success: function (data) {
            if (data.status == "success") {
                data = data.sceneryInfoList[0];
                console.log(data)
                // addNewSceneryInfos(map, lng, lat, title, data, marker); //弹出修改菜单框
            } else {
                showSuccessOrErrorModal(data.msg, "error");
            }
        },
        error: function (e) {
            showSuccessOrErrorModal("网络异常！", "error");
        }
    });
}

//删除景点
function deleteSceneryInfo(map, marker2) {
    console.log(marker2)
    console.log(marker2.customData.myProperty)
    var comCode = marker2.customData.myProperty
    $.confirm({
        title: '友情提示',
        content: '是否确认删除该景点信息？',
        useBootstrap: true,
        theme: 'supervan',
        confirmButton: '确认',
        cancelButton: '取消',
        animation: 'zoom', // 弹出的效果
        closeAnimation: 'scale', //关闭的效果
        confirm: function () {
            $.ajax({
                url: "deleteNewSceneryInfo",
                type: "post",
                data: {"comCode": comCode},
                dataType: "json",
                success: function (data) {
                    if (data.status == "success") {
                        console.log(data)
                        showSuccessOrErrorModal(data.msg, "success");
                        marker2.enableMassClear(); //设置允许覆盖物在map.clearOverlays方法中被清除
                        map.redraw()
                        marker2.redraw()
                        map.clearOverlays(); //删除当前覆盖物
                    } else {
                        showSuccessOrErrorModal(data.msg, "error");
                    }
                },
                error: function (e) {
                    showSuccessOrErrorModal("网络异常！", "error");
                }
            });
        },
        cancel: function () {
            return
        }
    });
}

/*鼠标按下/松开监听 Start*/
function mousedown(event) {
    var e = window.event;
    var obj = e.srcElement;
    alert("按下")
}

function mouseup(event) {
    var e = window.event;
    var obj = e.srcElement;
    alert("松开")
}

/*鼠标按下/松开监听 End*/

/**
 * 计算一个点是否在多边形里
 * @param {Object} pt 标注点
 * @param {Object} poly 多边形数组
 */
function isInsidePolygon(pt, poly) {
    for (var c = false, i = -1, l = poly.length, j = l - 1; ++i < l; j = i)
        ((poly[i].lat <= pt.lat && pt.lat < poly[j].lat) || (poly[j].lat <= pt.lat && pt.lat < poly[i].lat)) &&
        (pt.lng < (poly[j].lng - poly[i].lng) * (pt.lat - poly[i].lat) / (poly[j].lat - poly[i].lat) + poly[i].lng) &&
        (c = !c);
    return c;
}

//必填项提示框信息
function showWarning(title, msg) {
    $.alert({
        title: title,
        useBootstrap: true,
        // theme: 'supervan',
        content: msg
    });
}

/**
 * @Author Breach
 * @Description 监听ctrl+z、ctrl+y、ctrl+x键盘按键事件
 * @Date 2018/12/21
 * @Param null
 * @return
 */
function listenKey(map, pointList, pointListY) {
    var allOverlays = map.getOverlays(); //获取多有添加的标注点
    document.onkeyup = function (event) {//设置监听键盘事件
        event = event || window.event;
        if (event.ctrlKey == true && event.keyCode == 88) {//Ctrl+x
            console.log("ctrl+x已捕获")
        }
        if (event.keyCode == 8 || event.keyCode == 46) {//监听BackSpace和Delete键盘按钮事件
            console.log("BackSpace或Delete已捕获")
            if (pointList.length > 0) {
                // pointList.pop();//删除最后一个元素
                var location = pointList.pop()
                pointListY.push(location);
                map.clearOverlays();
                createOverlayAndLine(map, pointList) //创建标注点和连线
            }
        }
        if (event.ctrlKey == true && event.keyCode == 90) {//Ctrl+Z
            console.log("ctrl+z已捕获")
            if (pointListY.length > 0) {
                // pointListY.shift() //删除第一个元素
                var location = pointListY.pop() //删除最后一个元素
                pointList.push(location)
                map.clearOverlays();
                createOverlayAndLine(map, pointList) //创建标注点和连线
            }
        }
    }
}

/*GCJ02转WGS84坐标系 Start*/
function gcj02towgs84(lng, lat) {
    var transedpoint = {}
    if (out_of_china(lng, lat)) {
        return [lng, lat]
    }
    else {
        var dlat = transformlat(lng - 105.0, lat - 35.0);
        var dlng = transformlng(lng - 105.0, lat - 35.0);
        var radlat = lat / 180.0 * PI;
        var magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        var sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        mglat = lat + dlat;
        mglng = lng + dlng;
        transedpoint.lng = lng * 2 - mglng
        transedpoint.lat = lat * 2 - mglat
        return transedpoint
    }
}

function transformlat(lng, lat) {
    var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
    ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
    return ret
}

function transformlng(lng, lat) {
    var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
    ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
    return ret
}

//判断是否在国内，不在国内则不做偏移
function out_of_china(lng, lat) {
    return (lng < 72.004 || lng > 137.8347) || ((lat < 0.8293 || lat > 55.8271) || false);
}

/*GCJ02转WGS84坐标系 End*/

/*JS模拟键盘鼠标操作 Start*/
function fireKeyEvent(el, evtType, keyCode) {
    var doc = el.ownerDocument,
        win = doc.defaultView || doc.parentWindow,
        evtObj;
    if (doc.createEvent) {
        if (win.KeyEvent) {
            evtObj = doc.createEvent('KeyEvents');
            evtObj.initKeyEvent(evtType, true, true, win, false, false, false, false, keyCode, 0);
        }
        else {
            evtObj = doc.createEvent('UIEvents');
            Object.defineProperty(evtObj, 'keyCode', {
                get: function () {
                    return this.keyCodeVal;
                }
            });
            Object.defineProperty(evtObj, 'which', {
                get: function () {
                    return this.keyCodeVal;
                }
            });
            evtObj.initUIEvent(evtType, true, true, win, 1);
            evtObj.keyCodeVal = keyCode;
            if (evtObj.keyCode !== keyCode) {
                console.log("keyCode " + evtObj.keyCode + " 和 (" + evtObj.which + ") 不匹配");
            }
        }
        el.dispatchEvent(evtObj);
    }
    else if (doc.createEventObject) {
        evtObj = doc.createEventObject();
        evtObj.keyCode = keyCode;
        el.fireEvent('on' + evtType, evtObj);
    }
}

/*JS 模拟键盘鼠标点击 End*/

//监听新增景点弹窗，展示后为照片按钮添加功能
function listenTitleShow() {
    //上传图片
    var $ = jQuery,
        $list = $('#fileList1'),
        ratio = window.devicePixelRatio || 1,
        // 设置预览图的宽高
        thumbnailWidth = 240 * ratio,
        thumbnailHeight = 200 * ratio,
        // Web Uploader实例
        uploader;
    // 创建Web Uploader实例
    uploader = WebUploader.create({
        auto: true, // 选完文件后，是否自动上传。
        // swf文件路径
        swf: '@{/js/swf/Uploader.swf}',  //swf路径采用thymeleaf模板引擎的路径方式 (@{})
        // 文件接收服务端。（请求地址）
        server: 'uploadImages',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
            id: '#filePicker1',
            multiple: true //上传多张照片
        },
        thumb: {
            width: 140,
            height: 100,
            quality: 100, // 图片质量，只有type为`image/jpeg`的时候才有效。
            allowMagnify: false,// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            crop: true,// 是否允许裁剪。
            type: "image/jpeg"
        },
        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });

    // 当有文件添加进来的时候
    uploader.on('fileQueued', function (file) {
        var fileName = new Date().getTime() + ".jpg"; //自定义上传图片的名称
        var $li = $(
            '<div id="' + file.id + '" class="file-item thumbnail" style="float:left;width:140px;border: 1px solid #00BFFF;' +
            '    border-left: 5px solid #00BFFF;' +
            '    border-bottom: 3px solid #00BFFF;margin-right: 1px;">' +
            '<img>' +
            // '<div class="info">' + file.name + '</div>' +
            '<div class="info">' + fileName + '</div>' +
            '</div>'
            ),
            $img = $li.find('img');
        // $list为容器jQuery实例
        // $list.html("")  //限制只上传一张图片时加上这个
        $list.append($li);
        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        uploader.makeThumb(file, function (error, src) {
            if (error) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }
            $img.attr('src', src);
        }, thumbnailWidth, thumbnailHeight);
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress span');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<p class="progress"><span></span></p>')
                .appendTo($li)
                .find('span');
        }
        $percent.css('width', percentage * 100 + '%');
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on('uploadSuccess', function (file) {
        $('#' + file.id).addClass('upload-state-done');
    });

    // 文件上传失败，显示上传出错。
    uploader.on('uploadError', function (file) {
        var $li = $('#' + file.id),
            $error = $li.find('div.error');
        // 避免重复创建
        if (!$error.length) {
            $error = $('<div class="error"></div>').appendTo($li);
        }
        $error.text('上传失败');
    });
    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').remove();
    });
}