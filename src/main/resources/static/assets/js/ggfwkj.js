$(function(){
    //头部右上角的信息事件
    $('.header-tools>li').mouseover(function () {
        $(this).find(".header-child").show();
    }).mouseout(function(){
        $(this).find(".header-child").hide();
    });
    // 其他操作
    $("#other_operation").mouseover(function () {
        $(this).find(".dropdown-menu-right").show();
    }).mouseout(function(){
        $(this).find(".dropdown-menu-right").hide();
    });

    setiframeHeight();
    $(window).resize(function() {
        setiframeHeight();
    });

    $('.page-tabs .J_menuTab').on("click", J_menuTab_a);
    //收缩
    $('.shrink').click(function(){
        if(parseInt($(".left").css("margin-left"))==0){
            $('.left').animate({marginLeft:"-215px"}, "fast");
            $(this).addClass('hide-nav');
            $('.page-wrapper,.footer').animate({marginLeft:"5px"}, "fast");
        }else{
            $(this).removeClass('hide-nav');
            $('.left').animate({marginLeft:"0px"}, "fast");
            $('.page-wrapper,.footer').animate({marginLeft:"220px"}, "fast");
        }
    });

    //【关闭操作】按钮事件
    // $('.closeopers').on("click",function(){
    //     $('.dropdown-menu-right').slideToggle("slow");
    // });

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

        $(".page-tabs").css("margin-left", "0px");

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

    //【刷新当前选项卡】按钮事件
    $('.J_tabRefreshActive').on("click",function(){
        var $activeTab=$('.J_iframe[data-id="' + $('.page-wrapper .page-tabs').find('.J_menuTab.active').data('id')+ '"]');
        $activeTab.attr('src',$activeTab.attr('src'));
        // $activeTab.load();
    });

})


//加载头部菜单栏
function loadnavdata(idtag, datas){
    $(datas).each(function(index,data){
        var $nav;
        if(data.selected=="yes"){
            $nav=$("<a class='on' id='"+data.id+"'>"+data.name+"</a>");
            $("#"+idtag).append($nav);
            /***通过大菜单的id获取对应左侧的子菜单**/
            loadleftnav('treeDemo','',zNodes);
        }else{
            $nav=$("<a id='"+data.id+"'>"+data.name+"</a>")
            $("#"+idtag).append($nav);
        }
        $nav.click(function(){
            $(this).addClass('on').siblings().removeClass('on');
            /***通过大菜单的id获取对应左侧的子菜单**/
            loadleftnav('treeDemo','',zNodes);
        });
        $nav.mouseover(function(){
            $(this).addClass('hover');
        }).mouseout(function(){
            $(this).removeClass('hover');
        });

    });
}

//加载左侧菜单数据otherParam:{"otherParam":"zTreeAsyncTest"}
function loadleftnav(idtag,options,zNodes){
    var setting = {
        view: {
            showIcon: showIconForTree,
            showLine: false,
            dblClickExpand: false,
            addDiyDom: addDiyDom
        },
        data: {
            key:{
                url:"gourl"
            },
            simpleData: {
                enable: true//使用简单数据模式
            }
        }

    };
    $.extend(setting, options);

    function showIconForTree(treeId, treeNode) {
        return !treeNode.isParent&&treeNode.isParent;
    };
    function onClick(e,treeId, treeNode, callback) {
        var zTree = $.fn.zTree.getZTreeObj(idtag);
        zTree.expandNode(treeNode);
        if(treeNode.isParent){//父亲节点
            if(treeNode.open){//打开
                $('#'+treeNode.tId+'_a').find('.iconright').removeClass("iconfont icon-right").addClass("iconfont icon-down");
            }else{//关闭
                $('#'+treeNode.tId+'_a').find('.iconright').removeClass("iconfont icon-down").addClass("iconfont icon-right");
            }
        }
        if($.isFunction(callback)){
            callback&&callback(e,treeId, treeNode);
        }
        //加载右侧iframe框架
        loadrightiframe(e,treeId, treeNode);
    }
    function addDiyDom(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a" );
        if(treeNode.isParent){//是父节点
            var iconleft ="<i class='iconleft "+treeNode.iconSkin+"'></i>";
            if(treeNode.open){//打开
                var iconright ="<i class='iconright iconfont icon-down' id='iconfont_"+treeNode.id+"'></i>";
            }else{
                var iconright ="<i class='iconright iconfont icon-right' id='iconfont_"+treeNode.id+"'></i>";
            }
            aObj.prepend(iconleft);
            aObj.append(iconright);
        }
    }

    if(setting&&setting.callback&&setting.callback.onClick){
        var onclick = setting.callback.onClick;
        setting.callback.onClick = function(e,treeId, treeNode){
            onClick(e,treeId, treeNode,onclick);
        };
    }else if(setting.callback){
        setting.callback.onClick = onClick;
    }else{
        setting.callback = {
            onClick: onClick
        };
    }
    var $zTree = $("#"+idtag);
    $.fn.zTree.init($zTree, setting,zNodes);
    return $zTree;
}

function loadrightiframe(e,treeId, treeNode){

    if(treeNode.url&&treeNode.url!=''){
        var hastag=false;
        var $hasTab;//存在的tab
        $('.page-wrapper .page-tabs').find('.J_menuTab.active').removeClass('active');//先去掉选中

        $('.page-wrapper .page-tabs').find('a.J_menuTab').find('[data-id="'+treeNode.id+'"]')
        $('.page-wrapper .page-tabs').find('a.J_menuTab').each(function(i){
            if($('.page-wrapper .page-tabs').find('a.J_menuTab').eq(i).data('id')==treeNode.id){
                $hasTab=$('.page-wrapper .page-tabs').find('a.J_menuTab').eq(i);
                hastag=true;
                return false;
            }
        });

        if(hastag){//存在选中的tab
            //设置选中tab为在可视窗口中显示
            setSelectTabShow(treeNode.id);
            $('.page-wrapper .page-tabs').find('.J_menuTab.active').removeClass('active');//先去掉选中
            $('.page-wrapper .page-tabs').find('.J_menuTab[data-id="'+treeNode.id+'"]').addClass('active');
            $('.page-wrapper .page-content').find('.J_iframe').hide();
            $('.page-wrapper .page-content').find('.J_iframe[data-id="' + treeNode.id + '"]').show();
            $('.page-wrapper .page-content').find('.J_iframe[data-id="' + treeNode.id + '"]').src=treeNode.url;
            var  activeTabName=$('.page-wrapper .page-tabs').find('.J_menuTab[data-id="'+treeNode.id+'"]').html().split('<i')[0];
            $('.page-wrapper .page-tabs').find('.J_menuTab[data-id="'+treeNode.id+'"]').html().replace(activeTabName,treeNode.name);

        }else{//不存在选中的tab
            $('.page-wrapper .page-tabs').find('.J_menuTab').removeClass('last');//先去掉选中

            //创建头部tab
            var $add_a=$('<a href="javascript:;" class="J_menuTab active last" data-id="'+treeNode.id+'">'+treeNode.name+'<i class="iconfont icon-guanbi2"></i></a>');
            //创建对应的iframe页面
            var $iframe=$('<iframe width="100%" class="J_iframe" height="100%" frameborder="0" data-id="'+treeNode.id+'" data-src="'+treeNode.url+'" src="'+treeNode.url+'"></iframe>');
            $('.content-tabs .page-tabs').append($add_a);
            $('.page-wrapper .page-content').find('.J_iframe').hide();//先弄隐藏掉显示的iframe页面
            $('.page-wrapper .page-content').append($iframe);
            if(ptabs_ctabs()){//大于
                var y=parseInt($(".page-wrapper .page-tabs").css("margin-left"));//原来margin-left
                var n=-parseInt($add_a.innerWidth());
                $('.page-wrapper .page-tabs').animate({marginLeft: (n + y) + "px"}, "fast");
            }
            $add_a.on("click", J_menuTab_a);
            $add_a.find('.icon-guanbi2').on("click", close_tab);

        }


    }
}
//获取元素数组的中宽度
// flag:true 当前tab到第一个tab的总长度
//flag:false 当前tab-1到第一个tab的总长度
function alltabWidth($tags,tabid,flag){
    var w1 = 0;
    $tags.each(function(){
        if(flag){
            w1 += $(this).innerWidth()+1;//参数true表示包括边距(margin) 加1是右侧边框
            if(tabid&&$(this).data('id')==tabid){
                return false;
            }
        }else{
            if(tabid&&$(this).data('id')==tabid){
                return false;
            }
            w1 += $(this).innerWidth()+1;//参数true表示包括边距(margin) 加1是右侧边框
        }
    });
    return w1;
}

//判断可放置tab的总宽度 和 已加载的tab的宽度
function ptabs_ctabs(){
    var alltab_w=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab')); //获取当前所有tab的宽度
    if(alltab_w>tabviewport()){//判断当前显示tab的总宽度（alltab_w）是否大于可视窗口   大于
        return true;
    }else{//小于
        return false;
    }
}

//设置选中tab为在可视窗口中显示
function setSelectTabShow(treeNode_id){
    var a=tabviewport();//视口
    var b=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab')); //所有选中的tab宽度
    var n_1_tab=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab'),treeNode_id);//选中tab前一个tab距离【首页】tab的宽度  40px
    if(n_1_tab < a){
        $('.page-wrapper .page-tabs').animate({marginLeft: "0px"}, "fast"); //40px;
    }else if((n_1_tab+a)>b){
        $('.page-wrapper .page-tabs').animate({marginLeft: (-(b-a)) + "px"}, "fast");
    } else{
        $('.page-wrapper .page-tabs').animate({marginLeft: (-n_1_tab) + "px"}, "fast");
    }
}

//获取主tab可看见的宽度 （视口）
function tabviewport(){
    var alltab=$('.page-wrapper .content-tabs').innerWidth();//获取当前可放置tab的宽度
    var next_tab=$('.page-wrapper .content-tabs').find('.roll-nav.roll-left').outerWidth();//右侧【下一个tab】按钮的宽度
    var close_tab=$('.page-wrapper .content-tabs').find('.roll-right.btn-group').outerWidth();//右侧关闭操作按钮的宽度
    var obligate_w=close_tab+next_tab*2;//右侧可预留空间和右侧按钮的总宽度
    return alltab-obligate_w;
}

function J_menuTab_a(){
    $(this).addClass('active').siblings().removeClass('active');
    var currentTabWidth=$(this).innerWidth();//获取当前选中tab的宽度
    var ml= parseInt($(".page-wrapper .page-tabs").css("margin-left"));//获取当前的margin-left值
    var n_tab=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab'),$(this).data('id'),true);//选中tab距离【首页】tab的宽度
    var n_1_tab=alltabWidth($('.page-wrapper .page-tabs').find('.J_menuTab'),$(this).data('id'));//选中tab前一个tab距离【首页】tab的宽度

    if((n_tab-Math.abs(ml))<tabviewport()){//先判断跟左侧的距离
        if((n_tab-currentTabWidth)<Math.abs(ml) || (n_tab-currentTabWidth)==Math.abs(ml)){//小于或者等于margin-left
            if((ml+currentTabWidth)>0||(ml+currentTabWidth)==0){//特殊条件 如果最后一个是【首页】tab
                ml=0;
            }else{
                ml=ml+currentTabWidth;
            }
        }
    }else if((n_tab-Math.abs(ml))>tabviewport()){//再判断跟右侧的距离
        ml=ml-currentTabWidth;
    }
    $('.page-wrapper .page-tabs').animate({marginLeft: ml+ "px"}, "fast");
    //加载iframe页面
    $('.J_iframe[data-id="' + $(this).data('id') + '"]').show().siblings().hide();
    /* $('.J_iframe[data-id="' + $(this).data('id') + '"]').src=$('.J_iframe[data-id="' + $(this).data('id') + '"]').attr('src')*/


}

//动态设置iframe的高度
function setiframeHeight(){
    //设置page-wrapper高度
    var h=window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    var header=$('.header').innerHeight();
    var nav=$('.nav').innerHeight();
    //var footer=$('.footer').innerHeight();
    /* $('.page-wrapper').innerHeight(h-header-nav-footer);*/
    $('.page-wrapper .page-content').innerHeight(h-header-nav-$('.content-tabs').innerHeight()-1);
}

function i() {
    $(".page-tabs").children("[data-id]").not(":first").not(".active").each(function () {
        $('.J_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove()
    });
    $(".page-tabs").animate({marginLeft:"0px"}, "fast");
    $(".page-tabs").children('.active').removeClass('last').addClass('last');

}

//为tab的右侧【关闭】图标添加事件
function close_tab(){
    var tab_data_id=$(this).parent('.J_menuTab').data('id');
    var prev_tab=$(this).parent('.J_menuTab').prev();
    $(this).parent('.J_menuTab').remove();
    $('.J_iframe[data-id="' + tab_data_id+ '"]').remove();
    if($(this).parent('.J_menuTab').parent().find('a.J_menuTab.active').length==0){
        prev_tab.addClass('active').addClass('last');
        $('.J_iframe[data-id="' +  prev_tab.data('id')+ '"]').show().siblings().hide();
    }
}

function showallnews(id,name,url){
    var treeNode={id:id, name:name, url:url}
    loadrightiframe(null,null, treeNode)
}
function opennewWindow(id,name,url,refresh){
    var treeNode={id:id, name:name, url:url}
    loadrightiframe(null,null, treeNode)
    if(refresh){
        var $activeTab=$('.J_iframe[data-id="' + $('.page-wrapper .page-tabs').find('.J_menuTab.active').data('id')+ '"]');
        $activeTab.attr('src',$activeTab.attr('src'));
    }
}