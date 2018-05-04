/**
 * Created by chenyt on 2017/5/27.
 */
//表单验证--有表单都需要添加对应表单id的验证
$("#add11_form,#add3_form,#add2_form,#add1_form,#myform").Validform({
    showAllError: true,
    tiptype: function (msg, o, cssctl) {
        if (!o.obj.is('form')) {
            if (o.type != 2) {
                if (o.obj.parent().find('.Validform_checktip').length == 0) {
                    o.obj.parent().append('<span class="Validform_checktip"></span>');
                    o.obj.parent().next().find('.Validform_checktip').remove();
                }
                var objtip = o.obj.parent().find('.Validform_checktip');
                cssctl(objtip, o.type);
                objtip.text(msg);
            } else if (o.type == 2) {
                o.obj.parent().find('.Validform_checktip').remove();
            }
        }
    }
});
//添加表单弹窗
$(".btn11").on("click", function () {
    var layerIndex = layer.open({
        type: 1,
        title: '新增',
        area: ['450px', '340px'], //宽高
        shadeClose: true,
        content: $('#add1'),
        btn: [ '添加', '取消' ],
        yes: function (index, layero) {
            $("#add1_form").submit();
        },
        bnt2: function (index, layero) {
            layer.close(index);
        }
    });
});
// 多列弹窗表单
$(".btn14").on("click", function () {
    var layerIndex = layer.open({
        type: 1,
        title: '多列表单',
        area: ['900px', '340px'], //宽高
        shadeClose: true,
        content: $('#add11'),
        btn: [ '添加', '取消' ],
        yes: function (index, layero) {
            $("#add11_form").submit();
        },
        bnt2: function (index, layero) {
            layer.close(index);
        }
    });
});


//修改密码弹窗表单
$(".btn12").on("click", function () {
    var layerIndex = layer.open({
        type: 1,
        title: '修改密码',
        area: ['465px', '270px'], //宽高
        shadeClose: true,
        content: $('#add2'),
        btn: [ '提交', '取消' ],
        yes: function (index, layero) {
            $("#add2_form").submit();
        },
        bnt2: function (index, layero) {
            layer.close(index);
        }
    });
});
//文件上传弹窗表单
$(".btn13").on("click", function () {
    var layerIndex = layer.open({
        type: 1,
        title: '文件上传',
        area: ['550px', '400px'], //宽高
        shadeClose: true,
        content: $('#add3'),
        btn: [ '提交', '取消' ],
        yes: function (index, layero) {
            $("#add3_form").submit();

        },
        bnt2: function (index, layero) {
            layer.close(index);
        }
    });
});

//基础表单到弹窗内部
$(".btn15").on("click",function(){
    var layerIndex = layer.open({
        type: 1,
        title: '基础表单',
        area: ['1150px', '400px'], //宽高
        shadeClose: true,
        content: $('#layer-form-content'),
        btn: [ '提交', '取消' ],
        yes: function (index, layero) {
            $("#myform").submit();
        },
        bnt2: function (index, layero) {
            layer.close(index);
        }
    });
});
//单选框和复选框事件绑定
$('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '10%' // optional
});


//日期插件事件绑定
$('.date').datepicker({
    format: 'yyyy-mm-dd',
    weekStart: 1,
    autoclose: true,
    todayBtn: 'linked',
    language: 'zh-CN'
});
$('.input-daterange').datepicker({
    format: 'yyyy-mm-dd',
    weekStart: 1,
    autoclose: true,
    todayBtn: 'linked',
    language: 'zh-CN'
});


//弹窗按钮事件绑定
$('.btn-info').click(function () {
    layer.load(2, {time: 1000}); //又换了种风格，并且设定最长等待10秒
});
//初体验
$('.btn1').click(function () {
    layer.alert('内容');
});
//第三方扩展皮肤
$('.btn2').click(function () {
    layer.alert('内容', {
        icon: 1,
        skin: 'layer-ext-moon' //该皮肤由layer.seaning.com友情扩展。关于皮肤的扩展规则，去这里查阅
    });
});
//询问框
$('.btn3').click(function () {
    layer.confirm('您是如何看待前端开发？', {
        btn: ['重要', '很重要'] //按钮
    }, function () {
        layer.msg('的确很重要', {icon: 1});
    }, function () {
        layer.msg('也可以这样', {
            time: 20000, //20s后自动关闭
            btn: ['明白了', '知道了']
        });
    });
});
$('.btn4').click(function () {
    layer.msg('玩命提示中');
});
//墨绿深蓝风
$('.btn5').click(function () {
    layer.alert('墨绿风格，点击确认看深蓝', {
        skin: 'layui-layer-molv' //样式类名
        , closeBtn: 0
    }, function () {
        layer.alert('偶吧深蓝style', {
            skin: 'layui-layer-lan', closeBtn: 0, anim: 4 //动画类型
        });
    });
});
//捕获页
layer.open({
    type: 1,
    shade: false,
    title: false, //不显示标题
    content: $('.layer_notice'), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
    cancel: function () {
        layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', {time: 5000, icon: 6});
    }
});
//页面层
$(".btn6").click(function () {
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '240px'], //宽高
        content: 'html内容'
    });
});

layer.tips('Hi，我是tips', '#btn7');
//iframe层

$(".btn10").click(function () {
    layer.open({
        type: 2,
        title: 'layer mobile页',
        shadeClose: true,
        shade: 0.8,
        area: ['70%', '380px'],
        content: 'page_warning_content_list.html' //iframe的url
    });
});

//tab层
$(".btn8").click(function () {
    layer.tab({
        area: ['600px', '300px'],
        tab: [
            {
                title: 'TAB1',
                content: '内容1'
            },
            {
                title: 'TAB2',
                content: '内容2'
            },
            {
                title: 'TAB3',
                content: '内容3'
            }
        ]
    });
});
