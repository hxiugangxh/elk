function onlyOneFile(uploader){
	var files = uploader.getFiles();
	if(files.length != 0){
		$.each(files,function(n,value) {
			if(n<files.length-1){
				uploader.removeFile( value, true );
			}
	    	$('.uploader-list').html('');
		});
	}
}
function uploaderOnMethod(uploader){	
	//显示用户上传的文件
	uploader.on( 'fileQueued', function( file ) {
		onlyOneFile(uploader);
	    $('.uploader-list').append( '<div id="' + file.id + '" class="item">' +
	        '<span class="info">' + file.name + '</span>' +
	        '<span class="state">等待上传...</span>' +
	        '<span class="del btn btn-xs btn-danger">删除</span>' +
	        '<span class="rel btn btn-xs btn-info" style="display:none;">重试</span>' +
	    '</div>' );
	    $( '#'+file.id ).on('click', '.del', function() {
	    	uploader.removeFile( file,true );
	    	$(this).parent().remove();
		});
	});
	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).find('span.state').text('上传成功').addClass('text-success');
	    $( '#'+file.id ).find('span.rel').hide();
	});
	
	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('span.state').text('上传出错').addClass('text-danger');
	    $( '#'+file.id ).find('span.rel').show();
	    $( '#'+file.id ).on('click', '.rel', function() {
	    	uploader.retry(file);
		});
	});
	
	uploader.on( 'uploadComplete', function( file ) {
	    console.log("上传成功");
	});
}