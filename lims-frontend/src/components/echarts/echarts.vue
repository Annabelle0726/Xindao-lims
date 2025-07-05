<template>
  <div>
    <div id="id" ref="chart" :style="chartStyle"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ResizeListener from 'element-resize-detector';

export default {
  name: 'EChart',
  props: {
    options: {
      type: Object,
      default: () => ({})
    },
    chartStyle: {
      type: Object,
      default: () => ({
        height: '80%',
        width: '100%'
      })
    },
    dataset: {
      type: Object,
      default: () => {}
    },
    xAxis: {
      type: Array,
      default: () => []
    },
    yAxis: {
      type: Array,
      default: () => []
    },
    series: {
      type: Array,
      default: () => []
    },
    grid: {
      type: Object,
      default: () => ({})
    },
    legend: {
      type: Object,
      default: () => ({})
    },
    tooltip: {
      type: Object,
      default: () => ({})
    },
    lineColors: {
      type: Array,
      default: () => ['#A4EEDA', '#86C1F4', '#91A0FC', '#F6C18B', '#F09595']
    },
    barColors: {
      type: Array,
      default: () => ['#A4EEDA', '#86C1F4', '#91A0FC', '#F6C18B', '#F09595']
    },
    pieColors: {
      type: Array,
      default: () => ['#A4EEDA', '#86C1F4', '#91A0FC', '#F6C18B', '#F09595']
    },
    loadingOption: {
      type: Object,
      default: () => ({
        text: '数据加载中...',
        color: '#00BAFF',
        textColor: '#000',
        maskColor: 'rgba(255, 255, 255, 0.8)',
        zlevel: 0
      })
    }
  },
  data() {
    return {
      chartInstance: null,
    }
  },
  watch: {
    options: {
      deep: true,
        // immediate: true,
        handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    series: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    dataset: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    xAxis: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    yAxis: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    grid: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    legend: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    tooltip: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    lineColors: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    barColors: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
    pieColors: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.renderChart()
        })
      },
    },
  },
  mounted() {
    this.chartInstance = echarts.init(this.$refs.chart)
    this.renderChart()
    window.addEventListener('resize', this.windowResizeListener);
  },
  beforeDestroy() {
    this.dispose()
  },
  methods: {
    /* 当窗口缩放时，echart动态调整自身大小 */
    windowResizeListener() {
      if (!this.chartInstance) return;
      this.dispose()
      this.chartInstance = echarts.init(this.$refs.chart)
      this.renderChart()
      this.chartInstance.resize();
    },
    renderChart() {
      const option = {
        backgroundColor: this.options.backgroundColor || '#fff',
        xAxis: this.xAxis,
        yAxis: this.yAxis,
        dataset: this.dataset,
        series: this.series,
        grid: this.grid,
        legend: this.legend,
        tooltip: this.tooltip
      }
      // 根据传入的数据和配置参数生成图表
      this.generateChart(option)
    },

    generateChart(option) {
      // 配置折线图和柱状图的样式
      if (option.series && option.series.length > 0) {
        option.series.forEach((s, index) => {
          if (s.type === 'line') {
            s.itemStyle = {
              color: this.lineColors[index] || this.lineColors[0]
            }
            s.lineStyle = {
              color: this.lineColors[index] || this.lineColors[0]
            }
          } else if (s.type === 'bar') {
            s.itemStyle = {
              color: this.barColors[index] || this.barColors[0]
            }
          }
        })
      }
      this.chartInstance.clear()
      // 渲染图表
      this.chartInstance.setOption(option)
    },

    dispose() {
      if (this.chartInstance) {
        window.removeEventListener('resize', this.chartInstance.resize);//销毁图表监听事件
        this.chartInstance.dispose()
        this.chartInstance = null
      }
    }
  },
}
</script>

<style scoped>
/* 在这里可以写样式，比如设置图表容器的宽度和高度 */
</style>
