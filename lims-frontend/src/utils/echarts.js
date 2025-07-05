import * as echarts from "echarts";

const iuCharts = {
  drawBar: function (chart, params) {
    var color = ["#4379EE", "#FEC53D", "#65789B", "#F6BD16"];
    let series = params.yData.map((m, i) => {
      let obj = {
        name: m.title,
        type: "bar",
        barWidth: "16%",
        barGap: "30%",
        itemStyle: {
          normal: {
            color: color[i],
            barBorderRadius: [50, 50, 0, 0],
          },
        },
        data: m.data,
      };
      return obj;
    });
    var option = {
      backgroundColor: "#fff",
      tooltip: {
        trigger: "axis",
        axisPointer: {
          lineStyle: {
            color: "#4379EE",
          },
        },
      },
      grid: {
        top: "25px",
        left: "40px",
        right: "3%",
        bottom: "40px",
        // containLabel: true
      },
      legend: {
        show: true,
        orient: "horizontal",
        y: -2,
        right: 15,
        itemWidth: 12,
        itemHeight: 12,
        itemGap: 24,
        textStyle: {
          color: "rgba(43,48,52)",
          fontSize: "14px",
        },
      },
      xAxis: [
        {
          type: "category",
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "rgba(43,48,52, 0.4)",
            margin: 20,
          },
          axisTick: {
            show: false,
          },
          splitLine: {
            show: false,
          },
          boundaryGap: false,
          data: params.xData,
        },
      ],
      yAxis: [
        {
          minInterval: 1,
          type: "value",
          min: 0,
          splitNumber: 4,
          splitLine: {
            show: true,
            lineStyle: {
              color: "rgba(43,48,52, 0.1)",
            },
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "rgba(43,48,52, 0.4)",
            margin: 20,
          },
          axisTick: {
            show: false,
          },
        },
      ],
      series: series,
    };
    chart.setOption(option);
  },
  drawLine: function (chart, params) {
    let series = [];
    params.yData.forEach((m) => {
      series.push({
        name: m.title,
        type: "line",
        showAllSymbol: true,
        symbol: "circle",
        symbolSize: 10,
        lineStyle: {
          normal: {
            color: "#4379EE",
            width: 3,
          },
        },
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: "rgba(67,121,238,0.3)",
                },
                {
                  offset: 1,
                  color: "rgba(67,121,238,0)",
                },
              ],
              false
            ),
          },
        },
        data: m.data,
      });
    });
    let option = {
      backgroundColor: "#fff",
      tooltip: {
        trigger: "axis",
        axisPointer: {
          lineStyle: {
            color: "#4379EE",
          },
        },
      },
      grid: {
        top: "25px",
        left: "40px",
        right: "3%",
        bottom: "40px",
        // containLabel: true
      },
      xAxis: [
        {
          type: "category",
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "rgba(43,48,52, 0.4)",
            margin: 20,
          },
          axisTick: {
            show: false,
          },
          splitLine: {
            show: false,
          },
          boundaryGap: false,
          data: params.xData,
        },
      ],
      yAxis: [
        {
          minInterval: 1,
          type: "value",
          min: 0,
          splitNumber: 4,
          splitLine: {
            show: true,
            lineStyle: {
              color: "rgba(43,48,52, 0.1)",
            },
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "rgba(43,48,52, 0.4)",
            margin: 20,
          },
          axisTick: {
            show: false,
          },
        },
      ],
      series: series,
    };
    chart.setOption(option);
  },
  drawPie: function (chart, params) {
    let option = {
      color: params.color,
      backgroundColor: "#fff",
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b} : {c}",
      },
      title: {
        text: params.percentage,
        textStyle: {
          color: "#4379EE",
          fontSize: 14,
          align: "center",
          fontWeight: "normal",
        },
        x: "center",
        y: "center",
      },
      grid: {
        top: 20,
        bottom: 20,
        left: 0,
        right: 0,
      },
      legend: {
        show: false,
      },
      series: [
        // 主要展示层的
        {
          radius: ["55%", "90%"],
          center: ["50%", "50%"],
          type: "pie",
          label: {
            show: false,
          },
          name: params.title,
          data: params.data,
        },
        // 边框的设置
        {
          radius: ["55%", "65%"],
          center: ["50%", "50%"],
          type: "pie",
          label: {
            normal: {
              show: false,
            },
            emphasis: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false,
            },
            emphasis: {
              show: false,
            },
          },
          animation: false,
          tooltip: {
            show: false,
          },
          data: [
            {
              value: 1,
              itemStyle: {
                color: "rgba(250,250,250,0.3)",
              },
            },
          ],
        },
      ],
    };
    chart.setOption(option);
  },
  drawGauge: function (chart, params) {
    let option = {
      tooltip: {
        formatter: "{a} <br/>{b} : {c}%",
      },
      toolbox: {
        feature: {
          restore: {},
          saveAsImage: {},
        },
      },
      series: [
        {
          type: "gauge",
          axisLine: {
            lineStyle: {
              width: 28,
              color: [
                [0.3, "#67e0e3"],
                [0.7, "#37a2da"],
                [1, "#fd666d"],
              ],
            },
          },
          pointer: {
            itemStyle: {
              color: "auto",
            },
          },
          axisTick: {
            distance: -28,
            length: 8,
            lineStyle: {
              color: "#fff",
              width: 2,
            },
          },
          splitLine: {
            distance: -28,
            length: 28,
            lineStyle: {
              color: "#fff",
              width: 4,
            },
          },
          axisLabel: {
            color: "inherit",
            distance: 40,
            fontSize: 14,
          },
          detail: {
            valueAnimation: true,
            formatter: "{value} %",
            color: "inherit",
            textStyle: {
              fontSize: 30,
              align: "center",
            },
          },
          data: [
            {
              value: 70,
            },
          ],
        },
      ],
    };
    chart.setOption(option, true);
  },
};

export { iuCharts };
