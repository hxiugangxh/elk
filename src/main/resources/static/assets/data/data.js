var navdata=[{
    "id":"1",
    "name":"首页",
    "selected":"yes",
    },{
    "id":"2",
    "name":"表单页",
    "selected":"no",
    },{
    "id":"3",
    "name":"搜索页",
    "selected":"no",
    },{
    "id":"4",
    "name":"详情页",
    "selected":"no",
    },{
    "id":"5",
    "name":"图表页",
    "selected":"no",
    }
];


var zNodes =[
    { id:1, pId:0, name:"表单管理", /*open:true,*/iconSkin:"glyphicon glyphicon-list-alt"},
    //{ id:11, pId:1, name:"基本表单"/*,open:true,*/},
    { id:11, pId:1, name:"表单分组","url":"form/page_form_base.html"},
    /*{ id:120, pId:11, name:"表单样式1","url":"page_form_base1.html"},*/
    { id:12, pId:1, name:"表单样式2","url":"form/page_form_style2.html"},
    { id:13, pId:1, name:"实名认证导航","url":"form/real_name_authentication_flow_entrance.html"},
    //{ id:14, pId:1, name:"基本表单（验证）","url":"form/page_form_apply.html"},
    { id:15, pId:1, name:"表单（上下结构）","url":"form/page_form_updown_structure.html"},

    { id:2, pId:0, name:"表格管理",iconSkin:"glyphicon glyphicon-list"},
    { id:21, pId:2, name:"只读表格","url":"form/page_form_readonly.html"},
    { id:22, pId:2, name:"基本表格","url":"tables/page_table_search.html"},
    { id:23, pId:2, name:"可编辑表格","url":"tables/page_table_editable.html"},
    { id:24, pId:2, name:"统计表格","url":"tables/page_table_statistics.html"},


    { id:3, pId:0, name:"场景页",iconSkin:"glyphicon glyphicon-leaf"},
    { id:31, pId:3, name:"layer插件", open:false},
    { id:311, pId:31, name:"加载","url":"page_dialog_form.html"},
    { id:32, pId:3, name:"监控", open:false},
    { id:321, pId:32, name:"监控页面","url":"page_warning_conf.html"},
    { id:33, pId:3, name:"错误页面"},
    { id:331, pId:33, name:"404页面","url":"404.html"},
    { id:332, pId:33, name:"500页面","url":"500.html"},
    { id:34, pId:3, name:"图表展示"},
    { id:341, pId:34, name:"echarts","url":"page_echarts.html"},
    { id:35, pId:3, name:"使用指南"},
    { id:351, pId:35, name:"icon列表","url":"page_icon_document.html"},
    { id:352, pId:35, name:"button列表","url":"page_button_document.html"},
    { id:36, pId:3, name:"日志模块"},
    { id:361, pId:36, name:"日志列表查询","url":"page_data_search.html"},
    { id:362, pId:36, name:"图表统计","url":"page_data_view.html"},
    { id:363, pId:36, name:"显示面板","url":"page_data_show_panel.html"},
    { id:364, pId:36, name:"管理页","url":"page_data_manage.html"},


    //{ id:9, pId:0, name:"日子查询页面",iconSkin:"glyphicon glyphicon-leaf","url":"page_data_search"},
    //{ id:8, pId:0, name:"日子查询页面",iconSkin:"glyphicon glyphicon-leaf","url":"page_data_search"},
    //
    //{ id:7, pId:0, name:"场景页",iconSkin:"glyphicon glyphicon-leaf"},
    ////{ id:211, pId:21, name:"加载","url":"page_dialog_form.html"},
    //{ id:22, pId:2, name:"监控", open:false},
    //{ id:221, pId:22, name:"监控页面","url":"page_warning_conf.html"},
    //{ id:23, pId:2, name:"错误页面"},
    //{ id:231, pId:23, name:"404页面","url":"404.html"},
    //{ id:232, pId:23, name:"500页面","url":"500.html"},
    //{ id:44, pId:2, name:"图表展示"},
    //{ id:441, pId:44, name:"echarts","url":"page_echarts.html"},
    //{ id:55, pId:2, name:"内页布局"},
    //{ id:551, pId:55, name:"左右布局","url":"page_layout.html"},
    //{ id:66, pId:2, name:"使用指南"},
    //{ id:662, pId:66, name:"icon列表","url":"page_icon_document.html"},
    //{ id:663, pId:66, name:"button列表","url":"page_button_document.html"},
    //{ id:664, pId:66, name:"z-tree参考地址","url":"page_ztree_document.html"},
    //{ id:665, pId:66, name:"确认框","url":"page_confirm_dialog.html"},
    //
    //{ id:3, pId:0, name:"其他功能",iconSkin:"glyphicon glyphicon-home"},
    //{ id:31, pId:2, name:"layer插件", open:true},
    //{ id:312, pId:21, name:"加载","url":"page_dialog_form.html"},
    //{ id:31, pId:2, name:"监控", open:false},
    //{ id:313, pId:31, name:"监控页面","url":"page_warning_conf.html"},
    //{ id:33, pId:2, name:"错误页面"},
    //{ id:331, pId:33, name:"404页面","url":"404.html"},
    //{ id:332, pId:33, name:"500页面","url":"500.html"},
    //{ id:44, pId:2, name:"图表展示"},
    //{ id:441, pId:44, name:"echarts","url":"page_echarts.html"},
    //{ id:55, pId:2, name:"内页布局"},
    //{ id:551, pId:55, name:"左右布局","url":"page_layout.html"},
    //{ id:66, pId:2, name:"使用指南"},
    //{ id:662, pId:66, name:"icon列表","url":"page_icon_document.html"},
    //{ id:663, pId:66, name:"button列表","url":"page_button_document.html"},
    //{ id:664, pId:66, name:"z-tree参考地址","url":"page_ztree_document.html"},
    //{ id:665, pId:66, name:"确认框","url":"page_confirm_dialog.html"}
];