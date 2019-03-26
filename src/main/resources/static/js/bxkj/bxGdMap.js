//高德地图定位到当前所在城市
function loadGdMap() {
    // var info = '<div class="info"><p id="info"></p></div>'
    // $("#bx_gdmap").append(info)
    var map = new AMap.Map("bx_gdmap", {
            mapStyle: 'amap://styles/0362cde217820ffd25e76f6646345ef0',//设置地图的显示样式
            resizeEnable: true, //设置可调整大小
            rotateEnable:true, //设置指南针控件可旋转
            pitchEnable:true, //设置指南针控件可调整上下角度
            // center: [116.397428, 39.90923],
            zoom: 12,
            pitch:10,
            rotation:-15,
            viewMode:'3D',//开启3D视图,默认为关闭
            buildingAnimation:true//楼块出现是否带动画
        }), scale = new AMap.Scale({
            visible: true
        }),
        toolBar = new AMap.ToolBar({
            visible: true
        }),
        overView = new AMap.OverView({
            visible: true
        });
    map.addControl(scale); //添加控件
    map.addControl(toolBar);
    map.addControl(overView);
    map.addControl(new AMap.ControlBar({ //指南针控件
        showZoomBar:false,
        showControlButton:true,
        position:{
            right:'10px',
            top:'10px'
        }
    }))
    //获取用户所在城市信息
    function showCityInfo() {
        //实例化城市查询类
        var citysearch = new AMap.CitySearch();
        //自动获取用户IP，返回当前城市
        citysearch.getLocalCity(function (status, result) {
            if (status === 'complete' && result.info === 'OK') {
                if (result && result.city && result.bounds) {
                    var cityinfo = result.city;
                    var citybounds = result.bounds;
                    // document.getElementById('info').innerHTML = '您当前所在城市：' + cityinfo;
                    //地图显示当前城市
                    map.setBounds(citybounds);
                }
            } else {
                // document.getElementById('info').innerHTML = result.info;
            }
        });
    }

    showCityInfo();
}

//搜索
function searchMapInfo(scenery_name) {
    /*var map = new AMap.Map("bx_gdmap", {
        resizeEnable: true
    });*/
    var map = new AMap.Map("bx_gdmap", {
            resizeEnable: true,
            // center: [116.397428, 39.90923],
            zoom: 12
        }), scale = new AMap.Scale({
            visible: true
        }),
        toolBar = new AMap.ToolBar({
            visible: true
        }),
        overView = new AMap.OverView({
            visible: true
        });
    map.addControl(scale); //添加控件
    map.addControl(toolBar);
    map.addControl(overView);

    AMap.service(["AMap.PlaceSearch"], function () {
        //构造地点查询类
        var placeSearch = new AMap.PlaceSearch({
            map: map, // 展现结果的地图实例
            panel: "panel" // 结果列表将在此容器中进行展示。
        });
        //关键字查询
        placeSearch.search(scenery_name);
    });

}









