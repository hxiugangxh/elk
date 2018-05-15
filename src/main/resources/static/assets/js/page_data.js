var $table = $('#table');
$table.bootstrapTable({
	classes:'table table-hover table-condensed table-responsive table-theme',
	height: getHeight(),
	url: '/echartManage/pageVisualizeEchart',
	striped: true, //隔行变色
	pagination: true,
	singleSelect: false,
  sidePagination: "server",//表格分页的位置
	sortOrder:'desc',
	pageSize: 10,
	pageList: [10, 20, 50, 100],
	toolbar:'#myop',
	columns: [{
    checkbox: true,
    width: 50
  }, {
    field: 'id',
    width: 100,
    title: '编号',
    halign: 'center',
    align: 'center',
    valign: 'middle'
  }, {
    field: 'echartName',
    title: '图表名称',
    halign: 'center',
    align: 'center',
    valign: 'middle',
    sortable: true
  }, {
    width: 130,
    field: 'field',
    title: '汇聚列名',
    halign: 'center',
    align: 'center',
    valign: 'middle',
    sortable: true
  }, {
    width: 100,
    field: 'typePo',
    title: '图表类型',
    halign: 'center',
    align: 'center',
    valign: 'middle',
    sortable: true
  }, {
    width: 300,
    field: 'operate',
    title: '操作',
    halign: 'center',
    align: 'center',
    valign: 'middle',
    formatter: operateFormatter
  }
  ]
});

function operateFormatter(value, row, index) {
	return [
		'<a class="btn btn-primary btn_info" href="javascript:void(0)" title="查看详情">',
		//'<i class="iconfont icon-207"></i>',
		'查看详情',
		'</a>  ',
        '<a class="btn btn-success" href="javascript:void(0)" title="修改">',
        //'<i class="iconfont icon-xiugai"></i>',
        '修改',
        '</a>  ',
        '<a class="btn btn-danger" href="javascript:void(0)" title="删除">',
        //'<i class="iconfont icon-shanchu"></i>',
        '删除',
        '</a>  '
	].join('');
}

$(window).resize(function() {
	$table.bootstrapTable('resetView', {
		height: getHeight()
	});
});

function getHeight() {
	return $(window).height() - $('#table').offset().top-60;
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
