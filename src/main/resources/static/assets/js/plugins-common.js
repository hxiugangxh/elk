$(function(){
    //单选框和复选框
    $('input').size()>0&&$('input').iCheck({
        checkboxClass: 'icheckbox_minimal-theme',
        radioClass: 'iradio_minimal-theme',
        /* increaseArea: '10%' // optional*/
    });
;
    // 下拉插件单选
    $(".js-example-basic-multiple").size()>0&&$(".js-example-basic-multiple").select2({theme: "classic"});
    // 下拉插件多选
    $(".js-example-basic-single").size()>0&&$(".js-example-basic-single").select2({
        theme: "classic"
    });

    //下拉选框
    $('.selectpicker').size()>0&&$('.selectpicker').selectpicker({});
    //日期插件
    $('.date').size()>0&&$('.date').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });
    $('.input-daterange').size()>0&&$('.input-daterange').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });



});

