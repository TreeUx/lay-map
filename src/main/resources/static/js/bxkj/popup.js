/**
 * Elements that make up the popup.
 */
var container = document.getElementById('popup');
var content = document.getElementById('popup-content');
var closer = document.getElementById('popup-closer');
var characterBl = false //判断是否选择资源特色

/**
 * Add a click handler to hide the popup.
 * @return {boolean} Don't follow the href.
 */
closer.onclick = function() {
    overlay.setPosition(undefined);
    closer.blur();
    return false;
};


/**
 * Create an overlay to anchor the popup to the map.
 */
var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
    element: container,
    autoPan: true,
    autoPanAnimation: {
        duration: 250   //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度. 单位为毫秒（ms）
    }
}));


/**
 * Add a click handler to the map to render the popup.
 */
var coordinate
/*map.addEventListener('click', function(evt) {
    coordinate = evt.coordinate;
    var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(
        coordinate, 'EPSG:3857', 'EPSG:4326'));
    content.innerHTML = '<p>你点击的坐标是：</p><code>' + hdms + '</code>';
    overlay.setPosition(coordinate);
    map.addOverlay(overlay);
});*/

/**
 * 展示添加景点单元模态框
 */
function showAddModel() {
    $('#myAddSceneryInfoModal').modal('show') //展示模态框
    $('#myAddSceneryInfoModal').draggable(); //设置模态框可以拖拽
    // $("#myAddSceneryInfoModal").draggable({ //设置模态框可以拖拽
    //     handle: ".modal-header",
    //     cursor: 'move',
    //     refreshPositions: false
    // });
    $("#myAddSceneryInfoModal").css("overflow", "hidden");//禁止模态对话框的半透明背景滚动
}

$(function () {
    $('#myAddSceneryInfoModal').on('shown.bs.modal', function () {
        listenTitleShow(); //添加图片
        $("#scenery_location").val(coordinate) //设置点击处的坐标
        $("#location").text(coordinate) //设置点击处的坐标
        //初始化下拉选
        for (var i = 0; i < GlobalCity.length; i++) { //国家
            $("#continents").append(
                '<option value="' + GlobalCity[i].id + '">' + GlobalCity[i].value + '</option>'
            )
        }
        for (var i = 1; i < GlobalCity[0].child.length; i++) { //省
            $("#state").append(
                '<option value="' + GlobalCity[0].child[i].id + '">' + GlobalCity[0].child[i].value + '</option>'
            );
        }
        for (var i = 1; i < GlobalCity[0].child[1].child.length; i++) { //市
            $("#city").append(
                '<option value="' + GlobalCity[0].child[1].child[i].id + '">' + GlobalCity[0].child[1].child[i].value + '</option>'
            );
        }

        $("#continents").change(queryProvinceInfo)
        $("#state").change(queryCityInfo)
        $("#city").change(function () {
            var city = $("#city").find("option:selected").text();
        })
    })

    //选择特色星级
    $(".save-cstc").on("click", function () {
        var fea={
            "epidemic":$(".fashion .axis").val()||0,       //int   流行性-对应等级
            "recreational":$(".relex .axis").val()||0,   //int   休闲性-对应等级
            "nostalgic":$(".nos .axis").val()||0,      //int   怀旧性-对应等级
            "romantic":$(".romantic .axis").val()||0,       //int   浪漫性-对应等级
            "parent_child":$(".parentage .axis").val()||0,   //int   亲子性-对应等级
            "naturalness":$(".natural .axis").val()||0,    //int   自然性-对应等级
            "singularity":$(".special .axis").val()||0,    //int   奇特性-对应等级
            "excitement":$(".excit .axis").val()||0,     //int   刺激性-对应等级
            "culture":$(".culture .axis").val()||0,        //int   文化性-对应等级
            "ornamental":$(".watching .axis").val()||0,     //int   观赏性-对应等级
            "participatory":$(".participate .axis").val()||0,  //int   参与性-对应等级
            "iconic":$(".symbol .axis").val()||0          //int   标志性:对应等级
        }
        console.log(JSON.stringify(fea))
        $("#character_type").val(JSON.stringify(fea)) //资源特色
        console.log(typeof $("#character_type").val())
        $("#modal-cstc").modal("hide") //隐藏特色星级模态框
        characterBl = true //判断是否点击进行特色星级选择
    })
})

//展示特色星级选择模态框
function showStarModal() {
    $("#modal-cstc").modal("show")
    $('#modal-cstc').draggable(); //设置模态框可以拖拽
}


//根据选择的国家查询省份下拉选信息
function queryProvinceInfo() {
    var state = $("#continents").val()
    for (var i = 0; i < GlobalCity.length; i++) {
        if (state == GlobalCity[i].id) {
            $("#state").html("")
            if (GlobalCity[i].child != undefined) {
                for (var j = 1; j < GlobalCity[i].child.length; j++) {
                    $("#state").append(
                        '<option value="' + GlobalCity[i].child[j].id + '">' + GlobalCity[i].child[j].value + '</option>'
                    );
                }
                queryCityInfo()
                break
            } else {
                $("#city").html("")
            }
        }
    }
}

//根据选择的省份信息查询城市下拉选信息
function queryCityInfo() {
    var state = $("#continents").val()
    var province = $("#state").val()
    for (var i = 0; i < GlobalCity.length; i++) {
        if (state == GlobalCity[i].id) {
            for (var j = 0; j < GlobalCity[i].child.length; j++) {
                if (province == GlobalCity[i].child[j].id) {
                    $("#city").html("")
                    if (GlobalCity[i].child[j].child != undefined) {
                        for (var k = 1; k < GlobalCity[i].child[j].child.length; k++) {
                            $("#city").append(
                                '<option value="' + GlobalCity[i].child[j].child[k].id + '">' + GlobalCity[i].child[j].child[k].value + '</option>'
                            );
                        }
                    }
                    var city = $("#city").find("option:selected").text();
                    var province = $("#state").find("option:selected").text();
                    var state = $("#continents").find("option:selected").text();
                    console.log(city == "")
                    city = city == "" ? (province == "" ? state : province) : city
                    break
                }
            }
        }
    }
}

//清空添加模态框内容
function clear() {
    $("#fileList1").html("") //重置上传图片
    $("#scenery_name").val("") //重置名称
    $("#scenery_address").val("") //重置地址
    $("#com_best").val("") //重置游玩时间
    $("#scenery_remark").val("") //重置备注
}

//在地图上添加标记
function addVectorLayer(map, lng, lat) {
    /*1.首先创建空的矢量容器*/
    var vectorSource = new ol.source.Vector({})
    /*2.创建Feature，并添加进矢量容器中*/
    //创建图标特性
    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point([lng, lat], "XY"),
        name: "my Icon",
    });
    //将图标特性添加进矢量中
    vectorSource.addFeature(iconFeature);
    /*3.创建标记的样式*/
    //创建图标样式
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon({
            //控制标注图片和文字之间的距离
            anchor: [0.5, 60],
            //图标大小
            size: [50, 50],
            //偏移起点位置的方向
            // offsetOrigin: 'top-right',
            opacity: 0.75,
            src: "/images/map1.png"
        }),
    });
    /*4.创建矢量层，并添加进map层*/
    //创建矢量层
    var vectorLayer = new ol.layer.Vector({
        source: vectorSource,
        style: iconStyle
    });
    //添加进map层
    map.addLayer(vectorLayer);
}