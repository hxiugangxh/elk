//主tab的事件
$(function(){
    //【关闭操作】按钮事件
    $('.closeopers').on("click",function(){
        $('.dropdown-menu-right').toggle();
    });

    //按【上一个】tab
    $('.roll-left').click(function(){
        var n = parseInt($(".page-wrapper .page-tabs").css("margin-left"));
        if(n<0){
            var active_tab= $('.page-wrapper .page-tabs').find('.J_menuTab.active').innerWidth();
            $('.page-wrapper .page-tabs').animate({marginLeft: (n +active_tab) + "px"}, "fast");
        }else{
            $('.page-wrapper .page-tabs').animate({marginLeft: "0px"}, "fast");
        }
    });

    //按【下一个】tab
    $('.J_tabRight').click(function(){
        var alltab_w=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab')); //获取当前所有tab的宽度
        var ml= Math.abs(parseInt($(".page-wrapper .page-tabs").css("margin-left")));//获取当前的margin-left值
        if(( alltab_w-ml)>tabviewport()){//
            var active_tab= $('.page-wrapper .page-tabs').find('.J_menuTab.active').innerWidth();
            $('.page-wrapper .page-tabs').animate({marginLeft: (-(ml+active_tab)) + "px"}, "fast");
        }
    });

    //关闭全部选项卡
    $('.J_tabCloseAll').click(function(){
        $(".page-tabs").children("[data-id]").not(":first").each(function () {
            $('.J_iframe[data-id="' + $(this).data("id") + '"]').remove();
            $(this).remove();
        });
        //设置第一个为选中tab
        $(".page-tabs").find('.J_menuTab').eq(0).addClass("active");
        $('.J_iframe[data-id="' +  $(".page-tabs").find('.J_menuTab').eq(0).data("id") + '"]').show();

        $(".page-tabs").css("margin-left", "0px")
    })

    //关闭其他选项卡
    $(".J_tabCloseOther").on("click", i);

    //定位当前选项卡
    $(".J_tabShowActive").on("click", function(){
        var ml= parseInt($(".page-wrapper .page-tabs").css("margin-left"));//获取当前的margin-left值
        var a=tabviewport();//视口
        var c_b=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab'),$('.page-wrapper .page-tabs').find('.J_menuTab.active').data('id'),true);//选中tab距离【首页】tab的宽度
        var currentTabWidth=$('.page-wrapper .page-tabs').find('.J_menuTab.active').innerWidth();//获取当前选中tab的宽度

        if(Math.abs(ml)>(c_b-currentTabWidth)||Math.abs(ml)==(c_b-currentTabWidth)){//选中tab在左侧
            ml=-(Math.round(tabviewport()/2)>c_b?c_b:Math.round(tabviewport()/2));
            if(c_b<(Math.round(tabviewport()/2))||(Math.round(tabviewport()/2))==c_b){
                ml=0;
            }else{
                ml=ml+((Math.round(tabviewport()/2))+(Math.abs(ml)-c_b))
            }

        }else if(Math.abs(ml)< c_b){//选中tab在右侧或者选中tab在视口中
            if((c_b-Math.abs(ml))>tabviewport()||(c_b-Math.abs(ml))==tabviewport()){//选中tab在右侧
                ml=ml-(c_b-Math.abs(ml)-Math.round(tabviewport()/2));
            }
        }
        $(".page-tabs").animate({marginLeft:ml + "px"}, "fast");

    });

})

function i() {
    $(".page-tabs").children("[data-id]").not(":first").not(".active").each(function () {
        $('.J_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove()
    });
    $(".page-tabs").animate({marginLeft:"0px"}, "fast");

}

//为tab的右侧【关闭】图标添加事件
function close_tab(){
    var tab_data_id=$(this).parent('.J_menuTab').data('id');
    $(this).parent('.J_menuTab').remove();
    $('.J_iframe[data-id="' + tab_data_id+ '"]').remove();
}