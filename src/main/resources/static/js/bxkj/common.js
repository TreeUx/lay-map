/**
 * @Author Breach
 * @Description js公共类
 * @Date 2018/12/21
 * @Param null
 * @return
 */
function showSuccessOrErrorModal(msg, info) {
    swal({
        title: "操作提示",  //弹出框的title
        text: msg,   //弹出框里面的提示文本
        type: "success"==info?"success":"error", //弹出框类型
        // showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        // cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确认",//确定按钮上面的文档
        closeOnConfirm: true
    });
}

