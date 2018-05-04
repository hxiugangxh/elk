(function($) {
    $.warnDialog = function(config){
        var warnContainer ="<div id='warn-tip'></div>";
        $("body").append(warnContainer);
        var $this=$("#warn-tip");
        var totalHeigh= 0,timeParam,i= 0,nowTop;
        var options = $.extend(true,{
            height:128,
            firstBtn:"",
            secBtn:"",
            url:"",
            defaultData:[
                {"id":"1","mesg":"接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"2","mesg":"接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"3","mesg":"llXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"4","mesg":"nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"5","mesg":"接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"6","mesg":"接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"},
                {"id":"7","mesg":"接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为nullXXXXXX接口参数为null",status:"未处理",time:"2017/06/02 09:09"}
            ],
            init:function(){
                if($this.children("div[id='warn_content']").innerHTML){
                    return;
                }
                if(!options.defaultData){
                    getData();
                }else{
                    creatContent(options.defaultData);
                }
                layer.open({
                    type: 1
                    ,title:'<i class="iconfont icon-svg31 red900 mr-lf-5 posSet"></i>未处理警报<sub>（报警总数：<span class="red900">888</span>）</sub>'
                    ,offset: 'rb' //具体配置参考：offset参数项
                    ,content: $('#warn-tip')
                    ,shade: 0 //不显示遮罩
                    ,area: ['500px', '200px'] //宽高
                });
                $(".layui-layer-close1").on("click",function(){
                   $(".thumbnail-block").show();
                });
                $(".thumbnail-block").on("click",function(){
                    $(".thumbnail-block").hide();
                    $.warnDialog({
                        height:128,
                        firstBtn:"处理完成",
                        secBtn:"忽略",
                        url:""
                    });
                });
            }
        },config);
        function getData(){
            //获取报警数据
            $.ajax({
                url: options.url,
                type: 'GET',
                dataType: 'json',
                cache: false,
                timeout: 15000,
//                    data: {exchangeProductId:goodsId},
                success: function (res) {
                    //请求成功
                    if (res.rt == 1) {
                        var data;
                        if(res.data){
                            data=res.data;
                        }else{
                            layer.msg('没有数据！',{time:1000});
                        }
                        creatContent(data);
                    }
                },
                error: function () {
                    layer.msg('请求失败，请稍后再试',{time:1000});
                }
            });
        }

        function creatContent(data){
            var html='<div class="content-block" id="warn_content">',htmlCont="";
            html+='<div class="content-warn-block">';
            for(var i=0;i<data.length;i++){
                htmlCont+='<div class="profile-activity clearfix" data-id="'+data[i].id+'">'
                                +'<div class="time-block">'
                                    +'<a href="javascript:void(0);" class="warn-content" title="接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX接口参数为nullXXXXXX接口参数为nullXXX">'+data[i].mesg+'</a>'
                                    +'<div class="time">'
                                        +'<span class="status-block">处理状态：<span class="status red900">'+data[i].status+'</span></span>'
                                        +'<i class="iconfont icon-yuyuexuanzhong time-icon"></i>'+data[i].time
                                    +'</div>'
                                +'</div>'
                                +'<div class="tools action-buttons">'
                                    +'<a href="javascript:void(0);" class="blue edit">'
                                        +'<button type="button" class="btn btn-primary btn-xs btn-heigh" ><i class=" iconfont icon-bianji bigger-125"></i>'+options.firstBtn+'</button>'
                                    +'</a>'
                                    +'<a href="javascript:void(0);" class="red ignore">'
                                        +'<button type="button" class="btn btn-warning btn-xs btn-heigh"><i class="icon-remove bigger-125"></i>'+options.secBtn+'</button>'
                                    +'</a>'
                                +'</div>'
                          +'</div>';
            }
            html+=htmlCont;
            html+='</div>';
            var scroll='<div href="javascript:void(0);" class="red" style="float:left;width:25px;height:100px;">'
                    +'<i class="iconfont icon-up prev"></i><br>'
                    +'<i class="iconfont icon-down next"></i>'
                +'</div>';
            html+=scroll;
            html+= '</div>';
            $this.html(html);
            var tipBlock='<div class="thumbnail-block">'+
                '<i class="iconfont icon-svg31"></i>'+
                '<div class="warn-deal">报警处理</div>'+
            '</div>';
            $("body").append(tipBlock);
            //初始化高度
            var len=$(".content-warn-block").find(".profile-activity").length;
            totalHeight=len*options.height;
            var obj=$(".content-warn-block");
            $(".content-warn-block").css("height",totalHeight+"px");

            $(".next").on("click",function(){
                nextFn(obj);
            });

            $(".prev").on("click",function(){
                prevFn(obj);
            });
            $(".edit .btn").on("click",function(){
                clearTimeout(timeParam);
                layer.msg('已处理',{time:1000});
                $(this).parents(".tools").siblings(".time-block").find(".status").html("已处理");
                $(this).hide();
                timeSet(obj);
            });
            $(".ignore .btn").on("click",function(){
                clearTimeout(timeParam);
                layer.msg('已忽略',{time:1000});
                $(this).parents(".tools").siblings(".time-block").find(".status").html("已忽略");
                $(this).hide();
                $(this).parent().siblings(".edit").hide();
                timeSet(obj);
            });
        }
        function timeSet(obj){
            timeParam=setTimeout(function(){
                nextFn(obj);
            },1000);
        }
        function nextFn(obj){
            i--;
            var spaceTop=-options.height;
            if(i==-1){
                nowTop=spaceTop;
            }else{
                if(nowTop!=(-totalHeight+options.height)){
                    nowTop=nowTop+spaceTop;
                }else{
                    layer.msg('已经是最后一条',{time:1000});
                }
            }
            if(nowTop>=(-totalHeight+options.height)&&nowTop<0){
//                obj.animate({"top":nowTop+"px"},"normal","swing");
                obj.css("top",nowTop+"px");
            }
        }
        function prevFn(obj){
            i--;
            var spaceTop=options.height;
            if(i==-1){
                nowTop=spaceTop;
            }else{
                if(nowTop!=0){
                    nowTop=nowTop+spaceTop;
                }else{
                    layer.msg('已经是第一条',{time:1000});
                }
            }
            if(nowTop<=0){
                obj.css("top",nowTop+"px");
            }
        }
        options.init();
    };
})(jQuery);