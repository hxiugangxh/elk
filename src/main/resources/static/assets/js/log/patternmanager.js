//常量
appConst = {
    ERROR_DETAIL : "系统出错，请联系管理员！",
    RESP_SUCCESS : "0",
    RESP_ERROR : "1"
};



//根据indexPattern获取索引对应得esIndex对应得field
function fieldsByPattern(pattern) {
    if(Boolean(pattern)) {
        $.ajax({
            url: "/patternManager/getElasticFieldsByPattern/" + pattern,
            method: "post",
            dataType: "json",
            success: function(data){
                $("#indexFields").empty();
                var content = "<li class='list-group-item'>字段名称</li>";
                if(data != null && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        content += "<li class=\"list-group-item\">"+ data[i]+"</li>";
                    }
                }
                $("#indexFields").append(content);
            }
        });
    } else {
        $("#indexFields").empty();
    }
}

//设置默认得indexPattern
function setDefaultPattern() {
    var currentPattern = $("#name_info").text();
    if(!Boolean(currentPattern))
        return;

    layer.confirm('您确定要将' + currentPattern + '设置为默认得索引么？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.ajax({
            url: "/patternManager/setDefaultPattern/" + currentPattern,
            method: "post",
            dataType: "json",
            success: function(data){
                layer.closeAll('dialog');
                if(data.code == appConst.RESP_SUCCESS) {
                    layer.alert(data.message);
                } else {
                    layer.alert(data.message);
                }
            },
            error:function (error) {
                layer.alert(data.message == null ? appConst.ERROR_DETAIL : data.message);
            }
        });
    }, function(){

    });
}

function deletePattern(pattern) {
    if(!Boolean(pattern)) return;

    layer.confirm('您确定要删除索引' + pattern + '？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.ajax({
            url: "/patternManager/deletePattern/" + pattern,
            method: "post",
            dataType: "json",
            success: function(data){
                layer.closeAll('dialog');
                if(data.code == appConst.RESP_SUCCESS) {
                    $("#list_info li:first-child").addClass("active");
                    act();
                } else {
                    layer.alert(data.message);
                }
            },
            error:function (data) {
                layer.alert(data.message == null ? appConst.ERROR_DETAIL : data.message);
            }
        });
    }, function(){

    });
}


function addPattern(pattern, esIndex) {

    layer.confirm('您确定要新增索引' + pattern + '？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        var submitData = {
            pattern: pattern,
            esIndexList: esIndex
        };
        $.ajax({
            url: "/patternManager/addPattern",
            method: "post",
            dataType: "json",
            contentType:"application/json;charset=UTF-8",
            data: JSON.stringify(submitData),
            success: function(data){
                layer.closeAll('dialog');
                if(data.code == appConst.RESP_SUCCESS) {
                    $('#myModal').modal('hide');
                    var newpattern = "<li class=\"list-group-item\"><a href=\"javascript:void(0)\">"+pattern+"</a></li>";
                    $("#list_info").append(newpattern);
                } else {
                    layer.alert(data.message);
                }
            },
            error:function (error) {
                layer.alert(data.message == null ? appConst.ERROR_DETAIL : data.message);
            }
        });
    }, function(){

    });
}
