var reportSize = 0;
var reportContent = new Array();
var pageIndex = 0;
var $report;
var zoomInFlag = false;
function getReportObject(data){
	var index = data.indexOf('<a name="JR_PAGE_ANCHOR');
	var tempIndex = 0;
	reportSize = 0;
	reportContent = [];
	pageIndex = 0;
	while(index != -1) {
		reportSize++;
		tempIndex = index;
		index = data.substring(tempIndex + 23).indexOf('<a name="JR_PAGE_ANCHOR');
		if(index != -1) {
			index = index + tempIndex + 23;
			var centerStr = data.substring(tempIndex, index);
			var centerIndex = centerStr.lastIndexOf("</table>");
			reportContent[reportSize - 1] = centerStr.substring(0, centerIndex + 8);
		} else {
			var footStr = data.substring(tempIndex);
			var footIndex = footStr.indexOf("</table>");
			reportContent[reportSize - 1] = footStr.substring(0, footIndex + 8);
		}
	}
}
function getReportDom(){
	$report = $('<div></div>');
	$report.append('<div class="report"><div class="report-op">'+
	'<span>'+'<span class="current-page">'+(pageIndex+1)+'</span>/'+reportSize+'</span>'+
	'<button type="buttton" class="btn btn-sm btn-default"><img src="assets/img/print.png">打印</button>' +
	'<button type="buttton" class="btn btn-sm btn-default"><img src="assets/img/pdf.png">导出</button>' +
	'<button type="buttton" class="btn btn-sm btn-default r-prev" disabled><img src="assets/img/prev.png">上一页</button>' +
	'<button type="buttton" class="btn btn-sm btn-default r-next" '+ (reportSize ==1?'disabled':'') +'><img src="assets/img/next.png">下一页</button>' +
	'<button type="buttton" class="btn btn-sm btn-default r-zoomin"><img src="assets/img/zoomin.png">放大</button>' +
	'<button type="buttton" class="btn btn-sm btn-default r-zoomout" disabled><img src="assets/img/zoomout.png">缩小</button></div>' +
	'<div class="report-content" id="rc">'+reportContent[pageIndex]+'</div></div>');
}
function bindEvent(){
	$('.r-prev').on("click", function() {
		btnPrev();
	});
	$('.r-next').on("click", function() {
		btnNext();
	});
	$('.r-zoomin').on("click", function() {
		zoom(2);
	});
	$('.r-zoomout').on("click", function() {
		zoom(1);
	});
}
function btnPrev(){
	zoomInFlag = false;
	if(pageIndex > 0){
		$(".r-next").removeAttr("disabled");
		$('.report-content').html(reportContent[--pageIndex]);
	}
	if(pageIndex == 0){
		$(".r-prev").attr({"disabled":"disabled"});
	}
	$('.current-page').text(pageIndex+1);
}
function btnNext(){
	zoomInFlag = false;
	if(pageIndex < reportSize-1){
		$(".r-prev").removeAttr("disabled");
		$('.report-content').html(reportContent[++pageIndex]);
	}
	if(pageIndex == reportSize-1){
		$(".r-next").attr({"disabled":"disabled"});
	}
	$('.current-page').text(pageIndex+1);
}
function zoom(zoomNum) {
	var userAgent = getUserAgent();
	var obj = document.getElementById("rc");
	if (userAgent == "Firefox") {
		var heightVal;
		var widthVal;
		var mozTrans = "scale(" + zoomNum + "," + zoomNum + ")";
		var mozTransTop;
		var mozTransLeft;
		$(".r-zoomout").removeAttr("disabled");
		if (!zoomInFlag && zoomNum == 2) {
			heightVal = obj.scrollHeight;
			widthVal = obj.scrollWidth;
			obj.getElementsByTagName("table")[0].style.MozTransform = mozTrans;
			mozTransTop = (heightVal * 2 - obj.scrollHeight) / 2;
			mozTransLeft = (obj.scrollWidth - widthVal) / 2;
			obj.getElementsByTagName("table")[0].style.MozTransform = mozTrans + " translate("
					+ mozTransLeft + "px," + mozTransTop + "px)";
			obj.scrollLeft = obj.scrollWidth / 4;
			zoomInFlag = true;
		} else if (zoomInFlag && zoomNum == 1) {
			obj.getElementsByTagName("table")[0].style.MozTransform = mozTrans;
			zoomInFlag = false;
		}
		return;
	}
	else if((userAgent.indexOf("IE") != -1 || userAgent == "Chrome") && userAgent != "IE8.0"){
		obj.style.zoom = zoomNum;
		if(zoomNum==2){
			$(".r-zoomout").removeAttr("disabled");
			$(".r-zoomin").attr({"disabled":"disabled"});
		}else if(zoomNum==1){
			$(".r-zoomin").removeAttr("disabled");
			$(".r-zoomout").attr({"disabled":"disabled"});
		}
		return;
	}
	else{
		$(".r-zoomout").removeAttr("disabled");
		if ( zoomNum == 2) {	
			dataFilter();
		}else if (zoomNum == 1) {
			var data = reportContent[pageIndex];
			$("#rc").html(data);
		}
	}
}
function getUserAgent() {
	if (navigator.userAgent.indexOf("IE") > 0) {
		var version=navigator.appVersion.split(";");
		var result=version[1].replace(/[ ]/g,"").substr(2);
		return result;
	}
	if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {
		return "Firefox";
	}
	if (isSafari = navigator.userAgent.indexOf("Chrome") > 0) {
		return "Chrome";
	}
	return "other";
}
function dataFilter(){
	var data = reportContent[pageIndex];
	var result = data.replace(/font\-size.*px/gi,"");
	result = result.replace(/1px/gi,"2px");
	console.log(result);
	$("#rc").html(result);
	var widthT = document.getElementById("rc").getElementsByTagName("table")[0].style.width;
	document.getElementById("rc").getElementsByTagName("table")[0].style.width = widthT.substr(0,widthT.length-2)*2+"px";
	document.getElementById("rc").getElementsByTagName("table")[0].style.fontSize = "24px";
}
function showReport(url) {
	$.get(url, function(data) {
		getReportObject(data);
		getReportDom();
		layer.open({
			type: 1,
			title: '预览报表',
			shadeClose: true,
			shade: false,
			area: ['100%', '100%'],
			content: $report.html()
		}); 
		bindEvent();
	})
}