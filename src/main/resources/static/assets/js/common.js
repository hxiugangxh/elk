$(function(){
    setWapperContentInnerHeight();
    $(window).resize(function() {setWapperContentInnerHeight();});

    //tab在头部的选项卡
    //$('.tabs-container .tabs-top').find('li').eq(0).addClass('on');
    //$('.tabs-container .tabs-panels').find('.panels').eq(0).show();
    //$('.tabs-container .tabs-top li').click(function(){
    //    $(this).addClass('on').siblings().removeClass('on');
    //    $(this).parents('.tabs-container').find('.tabs-panels').find('.panels').eq($(this).index()).show().siblings().hide();
    //});

    /**小插件-分组**/
    $('.widget-shrink').click(function(){
        $(this).parents('.widget-box').find('.widget-body').slideToggle("slow");
        if($(this).find('.icon-down').length>0){
            $(this).find('.icon-down').removeClass('icon-down').addClass('icon-up');
        }else{
            $(this).find('.icon-up').removeClass('icon-up').addClass('icon-down');
        }
    });
    $('.widget-close').click(function(){
        $(this).parents('.widget-box').remove();
    });

})

//日期转字符串
function dateTOString(date,format){
    var  month=(date.getMonth()+1)>9?(date.getMonth()+1):'0'+(date.getMonth()+1);
    var  day=date.getDate()>9?date.getDate():'0'+date.getDate();
    return date.getFullYear()+format+month+format+day;
}

/**
 * 设置内页高度
 * ***/
function setWapperContentInnerHeight(){
    var iframeheight=$(parent.document.getElementsByClassName('page-content')).height(); //内页的总高度
    var iframefooterheight=$(parent.document.getElementsByClassName('footer')).height(); //内页的总高度
    if($('.wrapper-content').find('.wrapper-content-left').size()>0 && $('.wrapper-content').find('.wrapper-content-right').size()>0){//左右布局
        $('.wrapper-content').find('.wrapper-content-left,.wrapper-content-right').css("min-height",(iframeheight-iframefooterheight-23));//设置左边和右边的高度
        if($('.wrapper-content').find('.wrapper-content-left').find('.zTreeDemoBackground').size()>0){//设置左侧ztree的高度
            var boxheader=$('.wrapper-content').find('.box-header').innerHeight();
            $('.wrapper-content').find('.wrapper-content-left').find('.zTreeDemoBackground').css("min-height",((iframeheight-iframefooterheight-32)-boxheader-22));
        }
    }else if($('.wrapper-content').find('.wrapper-content-all').size()>0){//不是左右布局
        $('.wrapper-content').find('.wrapper-content-all').css("min-height",(iframeheight-iframefooterheight-43));
    }
}

$(window).resize(function () {
    var smy_hei = $(window).height() - 100;
    $("#left-nav").height(smy_hei - 135);
});

