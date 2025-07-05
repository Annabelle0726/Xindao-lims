<template>
  <div>
    <div class="echart_size" :id="id" :style="`height:${config.height};width:${config.width}`"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import ResizeListener from 'element-resize-detector';
import { iuCharts } from '@/utils/echarts'
export default {
  props: {
    id: {
      type: String,
      default: ''
    },
    config: {
      type: Object,
      default: () => { }
    },
    datas: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      chart: null,
    }
  },
  watch: {
    /* 如果图表数据是后台获取的，监听父组件中的数据变化，重新触发Echarts */
    datas: {
      deep: true,
      // immediate: true,
      handler(val) {
        this.$nextTick(() => {
          this.init();
        })
      },
    },
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  mounted() {
    window.addEventListener('resize', this.windowResizeListener);
    this.chartEleResizeListener();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    init() {
      // 实例化对象
      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.showLoading({
        text: 'loading',
        color: '#3A7BFA',
        textColor: '#000',
        maskColor: 'rgba(255, 255, 255, 0.2)',
        zlevel: 0,
      });
      if (this.config.isLoading) {
        this.chart.hideLoading();
        switch (this.config.type) {
          case 'bar':
            iuCharts.drawBar(this.chart, this.datas)
            break;
          case 'line':
            iuCharts.drawLine(this.chart, this.datas)
            break;
          case 'pie':
            iuCharts.drawPie(this.chart, this.datas)
            break;
          case 'gauge':
            iuCharts.drawGauge(this.chart, this.datas)
            break;
          default:
            break;
        }
        setTimeout(() => {
          this.chart.resize()
        }, 200)
      }
    },
    /* 对chart元素尺寸进行监听，当发生变化时同步更新echart视图 */
    chartEleResizeListener() {
      const chartInstance = ResizeListener({
        strategy: 'scroll',
        callOnAdd: true
      });
      chartInstance.listenTo(this.$el, () => {
        if (!this.chart) return;
        this.chart.resize();
      });
    },
    /* 当窗口缩放时，echart动态调整自身大小 */
    windowResizeListener() {
      if (!this.chart) return;
      this.chart.resize();
    }
  }
}
</script>
<style scoped>
.echart_size {
  width: 100%;
  height: 100%;
}
</style>
