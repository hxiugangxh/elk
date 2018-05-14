// 基于准备好的dom，初始化echarts实例

// 饼状图
var option_pie = {
  title: {
    text: '饼状图',
    textStyle: {
      fontSize: '30'
    }
  },
  tooltip: {},
  series: [
    {
      name: '记录',
      type: 'pie',
      center: ['50%', '50%'],
      data: [
        {value: 5, name: 'A'},
        {value: 20, name: 'B'},
        {value: 36, name: 'C'},
        {value: 10, name: 'D'},
        {value: 10, name: 'E'},
        {value: 20, name: 'F'}
      ]
    }
  ]
};

// 折线图
var option_line = {
  title: {
    text: '折线图',
    textStyle: {
      fontSize: '30'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    boundaryGap: false,
    data: ["A", "B", "C", "D", "E", "F"]
  },
  yAxis: {},
  series: [{
    name: '记录',
    type: 'line',
    data: [5, 20, 36, 10, 10, 20]
  }]
};

// 柱状图
var myChart_bar = echarts.init(document.getElementById('main_bar'));
var option_bar = {
  title: {
    text: '柱状图',
    textStyle: {
      fontSize: '30'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    data: ["A", "B", "C", "D", "E", "F"]
  },
  yAxis: {},
  series: [{
    name: '记录',
    type: 'bar',
    barMaxWidth: 30,
    data: [5, 20, 36, 10, 10, 20]
  }]
};


myChart_bar.setOption(option_bar);

// 树图
/*
var myChart_tree = echarts.init(document.getElementById('main_tree'));
var option_tree = {
   title : {
        text: '树图',
        subtext: '虚构数据'
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : false,

    series : [
        {
            name:'树图',
            type:'tree',
            orient: 'vertical',  // vertical horizontal
            rootLocation: {x: 'center',y: 50}, // 根节点位置  {x: 100, y: 'center'}
            nodePadding: 1,
            itemStyle: {
                normal: {
                    label: {
                        show: false,
                        formatter: "{b}"
                    },
                    lineStyle: {
                        color: '#48b',
                        shadowColor: '#000',
                        shadowBlur: 3,
                        shadowOffsetX: 3,
                        shadowOffsetY: 5,
                        type: 'curve' // 'curve'|'broken'|'solid'|'dotted'|'dashed'

                    }
                },
                emphasis: {
                    label: {
                        show: true
                    }
                }
            },

            data: [
                {
                    name: '根节点',
                    value: 6,
                    children: [
                        {
                            name: '节点1',
                            value: 4,
                            children: [
                                {
                                    name: '叶子节点1',
                                    value: 4
                                },
                                {
                                    name: '叶子节点2',
                                    value: 4
                                },
                                {
                                    name: '叶子节点3',
                                    value: 2
                                },
                                 {
                                    name: '叶子节点4',
                                    value: 2
                                },
                                {
                                    name: '叶子节点5',
                                    value: 2
                                },
                                {
                                    name: '叶子节点6',
                                    value: 4
                                }
                            ]
                        },
                        {
                            name: '节点2',
                            value: 4,
                            children: [{
                                name: '叶子节点7',
                                value: 4
                            },
                            {
                                name: '叶子节点8',
                                value: 4
                            }]
                        },
                        {
                            name: '节点3',
                            value: 1,
                            children: [
                                {
                                    name: '叶子节点9',
                                    value: 4
                                },
                                {
                                    name: '叶子节点10',
                                    value: 4
                                },
                                {
                                    name: '叶子节点11',
                                    value: 2
                                },
                                 {
                                    name: '叶子节点12',
                                    value: 2
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
};


myChart_tree.setOption(option_tree);*/
