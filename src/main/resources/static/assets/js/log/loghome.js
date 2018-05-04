
//根据索引名称、索引字段、内容获取完整的日志记录
function searchLogs(pageNumber) {
    var searchContent = $("#searchContent").val();
    var indexPattern = $("#indexPattern").val();
    var indexField = $("#indexFields").val();

    if(!Boolean(indexPattern) || !Boolean(indexField)) {
        return;
    }

    var searchRequest = {
        indexPattern: indexPattern,
        field: indexField,
        searchContent: searchContent,
        pageNumber: pageNumber
    };

    $.ajax({
        url: "/loghome/searchLogs",
        method: "post",
        dataType: "json",
        contentType:"application/json;charset=UTF-8",
        data: JSON.stringify(searchRequest),
        success: function(data){
            if(data != null) {
                $("#logWrapper").empty();

                if(data.total == 0) {
                    $("#pagination-ele").bootstrapPaginator({totalPages: data.totalPage});
                } else {
                    $("#pagination-ele").bootstrapPaginator({currentPage : pageNumber,totalPages: data.totalPage});
                }
                $("#totalHits").text(data.total);

                var logContent = data.data;
                if(logContent != null) {
                    for(var i = 0; i < logContent.length; i++) {
                        createLogContent(logContent[i]);
                    }
                }
            }
        }
    });
}

//创建日志内容
function createLogContent(data) {
    var content = "<div class=\"cf data-line data-line-odd\">\n" +
        "<i class=\"iconfont icon-xiangyou2  text-center\" data-status=\"0\"></i>\n" +
        "<div class=\"col-xs-12 data-cont\">\n" +
        "<div class=\"cont-elips\">\n" +
        "</div>\n" + data +
        "</div>" +
        "</div>";
    $("#logWrapper").append(content);
}

//根据索引名称获取索引对应的字段
function findFieldsByPattern(indexPattern) {
    if(Boolean(indexPattern)) {
        $.ajax({
            url: "/patternManager/getElasticFieldsByPattern/" + indexPattern,
            method: "post",
            dataType: "json",
            success: function(data){
                if(data != null && data.length > 0) {
                    var indexFields = $("#indexFields").empty();
                    for ( var i = 0; i < data.length; i++) {
                        indexFields.append("<option value='"+data[i]+"'>"+ data[i]+"</option>");
                    }
                }
            }
        });
    }
}

$("#indexPattern").change(function () {
    var indexPattern = $("#indexPattern").val();
    findFieldsByPattern(indexPattern);
});

$("#querylogBtn").click(function () {
    searchLogs(1);
});

$(function(){
    searchLogs(1);
});
