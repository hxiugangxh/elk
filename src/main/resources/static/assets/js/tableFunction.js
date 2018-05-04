//var format = {
//	AAB162:0,
//	AAE149:201601,
//	AAB160	0
//	ZGRS00	30
//	AAB001	1
//	AAE238	45030
//	AAB004	易联众信息技术股份有限公司
//	AAB159	0
//	AAE180
//}

function operateFormatter(value, row, index) {
	return [
		'<a class="btn btn-primary" href="javascript:void(0)" title="查看详情">',
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
function alertWin(data) {
	layer.open({
		type: 1,
		skin: 'layui-layer-demo', //样式类名
		anim: 2,
		shadeClose: false, //开启遮罩关闭
		content: createRowTable(data)
	});
}

function createRowTable(data) {
	var d = JSON.parse(data);
	var tablehtml = '<table class="rowtable">';
	$.each(d, function(i, n) {
		tablehtml += '<tr><td>' + i + '</td><td>' + n + '</td></tr>';
	});
	tablehtml += '</table>';
	return tablehtml;
}

function getHeight() {
	return $(window).height() - $('#table').offset().top-60;
}

function icheckstyle() {
	$('.bs-checkbox input').iCheck({
		checkboxClass: 'icheckbox_minimal-theme',
		radioClass: 'iradio_minimal-theme'
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