

function operateFormatter(value, row, index) {
    return [
        '<a class="btn btn-primary" href="javascript:void(0)" title="修改配置">',
        '修改配置',
        '</a>  ',
        '<a class="btn btn-primary btn-history" href="javascript:void(0)" title="监控历史记录">',
        '监控历史记录',
        '</a>  ',
        '<a class="btn btn-danger" href="javascript:void(0)" title="停 止">',
        '停 止',
        '</a>  '
    ].join('');
}
function alertWin(data) {
    layer.open({
        type: 1,
        skin: 'layui-layer-demo', //样式类名
        anim: 2,
        shadeClose: false, //开启遮罩关闭
        content: createRowTable(data)
    });
}


function getHeight() {
    return $(window).height() - $('#table').offset().top-60;
    //return  parent.$('.page-content').height()- $('#table').offset().top-90;
}

function icheckstyle() {
    $('.bs-checkbox input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
    $('input[name=btSelectAll]').on('ifClicked', function(event) {
        $('input[name=btSelectAll]').iCheck('toggle');
        if($(this).parent().hasClass('checked')) {
            $('.bs-checkbox input').iCheck('check');
        } else {
            $('.bs-checkbox input').iCheck('uncheck');
        }
    });
    $('input[name=btSelectItem]').on('ifClicked', function(event) {
        var i = 1;
        $('input[name=btSelectItem]').each(function() {
            if($(this).parent().hasClass('checked')) {
                i++;
            }
        })
        if(i == $('input[name=btSelectItem]').length) {
            $('input[name=btSelectAll]').iCheck('check');
        }
    });
    $('input[name=btSelectItem]').on('ifChecked', function(event) {
        $(this).parents('tr').addClass('selected');
    });
    $('input[name=btSelectItem]').on('ifUnchecked', function(event) {
        $(this).parents('tr').removeClass('selected');
        $('input[name=btSelectAll]').iCheck('uncheck');
    });
}
window.operateEvents = {
    'click .lookinfo': function(e, value, row, index) {
        /*alertWin(JSON.stringify(row));*/
        layer.open({
            type: 2,
            area: ['600px', '300px'],
            shadeClose: false, //开启遮罩关闭
            content:'page_read_form.html?data='+row
        });
    }
};