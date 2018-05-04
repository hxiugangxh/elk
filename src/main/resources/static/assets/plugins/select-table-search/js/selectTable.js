(function($) {
    $.fn.dropDownGrid = function(config){
        var $this = $(this);
        var options = $.extend(true,{
                columns:[],
                url:"",
                pagination:true,
                data:[
                    {"num":"122","name":"厦门人社"},
                    {"num":"122","name":"三明人社"},
                    {"num":"122","name":"三明人社"},
                    {"num":"122","name":"三明人社"},
                    {"num":"122","name":"三明人社"},
                    {"num":"122","name":"三明人社"},
                    {"num":"122","name":"三明人社"}
                ],
                init:function(){
                    if($this.children("table[id='dg']").innerHTML){
                        return;
                    }
                    var html='<table id="dg" title="My Users" url="'+options.url+'" class="easyui-datagrid" style="width:400px;height:250px">'+
                        '</table>'
                        +'<div id="toolbar"><div id="td" style="position:relative;margin:5px;">'
                        +'<span>单位名称：</span><input id="search" prompt="请输入单位名称" style="width: 130px; vertical-align: middle;padding:1px 3px;border:1px solid #ccc;line-height: 22px;" /><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" onclick="doSearch()" style="border:1px solid #ccc;margin-left:3px;">搜索</a>'
                        +'</div></div>';
                    $(".block-table").append(html);

                    $('#dg').datagrid({
//                        url:options.url,
                        pagination:options.pagination,
                        rownumbers:true,
                        fitColumns:true,
                        singleSelect:false,
                        checkbox:true,
                        toolbar:"#toolbar",
                        columns:options.columns,
                        singleSelect: false,
                        selectOnCheck: true,
                        checkOnSelect: true,
                        data:options.data,
                        onClickRow:function(rowIndex,rowData){
                            checkedFn(rowIndex,rowData);
                        },
                        onSelect:function(rowIndex,rowData){
                            selectionFn(rowIndex,rowData);
                        },
                        onUnselect:function(rowIndex,rowData){
                            selectionFn(rowIndex,rowData);
                        },
                        onSelectAll:function(rows){
                            var selRows = rows;
                            var ids="",values="";
                            for(var i=0;i<selRows.length;i++){
                                if((i+1)<selRows.length){
                                    values+=selRows[i].name+",";
                                    ids+=selRows[i].num+",";
                                }else{
                                    values+=selRows[i].name;
                                    ids+=selRows[i].num;
                                }
                            }
                            $("#selected_result").val(values);
                            $("#ids").val(ids);
                        },
                        onUnselectAll:function(rows){
                            $("#ids").val("");
                            $("#selected_result").val("");
                        }
                    });
                    setTimeout(function(){
                        $(".block-table").hide();
                    },500);

                }
        },config);
        function checkedFn(rowIndex,rowData){
             var selRows = $('#dg').datagrid('getChecked');
             var ids="",values="";
             for(var i=0;i<selRows.length;i++){
                 if((i+1)<selRows.length){
                     values+=selRows[i].name+",";
                     ids+=selRows[i].num+",";
                 }else{
                     values+=selRows[i].name;
                     ids+=selRows[i].num;
                 }
             }
             $("#selected_result").val(values);
             $("#ids").val(ids);
        }
        function selectionFn(rowIndex,rowData){
            var selRows = $('#dg').datagrid('getSelections');
            var ids="",values="";
            for(var i=0;i<selRows.length;i++){
                if((i+1)<selRows.length){
                    values+=selRows[i].name+",";
                    ids+=selRows[i].num+",";
                }else{
                    values+=selRows[i].name;
                    ids+=selRows[i].num;
                }
            }
            $("#selected_result").val(values);
            $("#ids").val(ids);
//            console.log(JSON.stringify(selRows));
        }
        $this.mouseover(function(){
            $(".block-table").show();
        }).mouseout(function(){
            $(".block-table").hide();
        });
        options.init();

    };
})(jQuery);

