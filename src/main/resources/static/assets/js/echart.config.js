// 基于准备好的dom，初始化echarts实例

// 饼状图
var option_pie = {
  title: {
    text: '饼状图',
    textStyle: {
      fontSize: '25'
    }
  },
  tooltip: {
    formatter: "{a} <br/>{b} : {c} ({d}%)"
  },
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
      fontSize: '25'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    boundaryGap: false,
    data: ["A", "B", "C", "D", "E", "F"],
    axisLabel : {
      show : true,
      formatter:function(val) {
        return val.split(" ").join("\n")
      }
    }

  },
  yAxis: {},
  series: [{
    name: '记录',
    type: 'line',
    data: [5, 20, 36, 10, 10, 20]
  }]
};

// 柱状图
var option_bar = {
  title: {
    text: '柱状图',
    textStyle: {
      fontSize: '25'
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
