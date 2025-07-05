<template>
  <div class="app-container">
    <el-row>
      <el-col :span="4">
        <el-radio-group v-model="dateType" @change="changeDate">
          <el-radio-button label="1">本周</el-radio-button>
          <el-radio-button label="2">本月</el-radio-button>
          <el-radio-button label="3">今年</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="20">
        <el-form ref="entity" size="small" :inline="true">
          <el-form-item style="width: 20%;">
            <el-date-picker
              v-model="datePicker"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              placeholder="选择日期"
              range-separator="至"
              size="small"
              start-placeholder="开始日期"
              type="daterange"
              style="width: 100%;"
              value-format="yyyy-MM-dd"
              @change="changeDatePicker">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="样品名称" prop="sampleName">
            <el-input v-model="sampleName" clearable placeholder="请输入样品名称" size="small" @change="changeDate"></el-input>
          </el-form-item>
          <el-form-item label="型号" prop="modelName">
            <el-input v-model="modelName" clearable placeholder="请输入型号" size="small" @change="changeDate"></el-input>
          </el-form-item>
          <el-form-item label="供应商名称" prop="supplierName">
            <el-input v-model="supplierName" clearable placeholder="请输入供应商名称" size="small" @change="changeDate"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="pie-card">
          <div class="title">原材料合格率</div>
          <span class="data">{{passRate}}</span>
          <Echarts ref="chart"
                   :legend="pieLegend"
                   :series="materialPieSeries"
                   :tooltip="pieTooltip"
                   style="height: 36vh;"></Echarts>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="pie-card">
          <div class="title"><span style="color: #F56C6C">本月</span>检验类型数量</div>
          <Echarts ref="chart"
                   :chartStyle="chartStyle2"
                   :legend="pieLegend"
                   :series="materialPieSeries1"
                   :tooltip="pieTooltip"
                   style="height: 36vh;"></Echarts>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="pie-card">
          <div class="title">原材料<span style="color: #F56C6C">本月</span>与<span style="color: #F56C6C">上月</span>合格率对比</div>
          <Echarts ref="chart"
                   :barColors="barColors2"
                   :chartStyle="chartStyle"
                   :grid="grid"
                   :legend="barLegend"
                   :series="barSeries"
                   :tooltip="tooltip"
                   :xAxis="xAxis1"
                   :yAxis="yAxis1"
                   style="height: 36vh;"></Echarts>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="inspection-card">
          <div class="title">合格率</div>
          <Echarts ref="chart"
                   :barColors="barColors"
                   :grid="grid"
                   :legend="legend"
                   :lineColors="lineColors"
                   :options="echartsOptions"
                   :series="echartsSeries"
                   :tooltip="tooltip"
                   :xAxis="xAxis"
                   :yAxis="yAxis"
                   style="height: 40vh;"></Echarts>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Echarts from "@/components/echarts/echarts.vue";
import {
  getOrderTypeCookie,
  getRawPassRateByBarChart,
  getRawPassRateByCake,
  getRawUpMonth
} from "@/api/statisticalCharts/dataAnalysis";

export default {
  name: "QualificationRateStatistics",
  // import 引入的组件需要注入到对象中才能使用
  components: {Echarts},
  data() {
    // 这里存放数据
    return {
      dateType: '1',
      datePicker: [],
      beginDate: '',
      endDate: '',
      sampleName: '',
      modelName: '',
      supplierName: '',
      echartsOptions: {
        title: {
          text: '示例图表',
          left: 'center'
        }
      },
      xAxis: [
        {
          type: 'category',
          data: [],
          axisPointer: {
            type: 'shadow'
          }
        }
      ],
      yAxis: [
        {
          type: 'value',
          name: '总数',
          min: 0,
        },
        {
          type: 'value',
          name: '合格率',
          min: 0,
          max: 100,
          axisLabel: {
            formatter: '{value} %'
          }
        }
      ],
      echartsSeries: [
        {
          name: '总数',
          type: 'bar',
          data: [],
          label: {
            show: true,
            position: 'top'
          },
        },
        {
          name: '合格率',
          type: 'line',
          smooth: true,
          tooltip: {
            valueFormatter: function (value) {
              return value + ' %';
            }
          },
          label: {
            show: true,
            position: 'top',
            formatter: function (value) {
              return value.value + ' %';
            },
            distance: 14
          },
          yAxisIndex: 1,
          data: []
        },
      ],
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      legend: {
        data: ['总数','合格率']
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      lineColors: ['#91A0FC'],
      barColors: ['#86C1F4'],
      barColors2: ['#A4EEDA'],
      pieTooltip: {
        trigger: 'item'
      },
      pieLegend: {
        orient: 'vertical',
        right: 20,
        top: 'middle',
      },
      materialPieSeries: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
          right: '22%',
          avoidLabelOverlap: false,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            alignTo: 'edge',
            formatter: '{name|{b}}\n{time|{c}}',
            edgeDistance: 10,
            lineHeight: 15,
            rich: {
              time: {
                fontSize: 10,
                color: '#999'
              }
            },
          },
          labelLine: {
            length: 20,
            length2: 40,
          },
          data: [
            { value: 0, name: '不合格数量' },
            { value: 0, name: '合格数量' },
          ]
        }
      ],
      materialPieSeries1: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
          right: '22%',
          avoidLabelOverlap: false,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            alignTo: 'edge',
            formatter: '{name|{b}}\n{time|{c}}',
            edgeDistance: 10,
            lineHeight: 15,
            rich: {
              time: {
                fontSize: 10,
                color: '#999'
              }
            },
          },
          labelLine: {
            length: 20,
            length2: 50,
          },
          data: [
            { value: 0, name: '委托检验' },
            { value: 0, name: '进厂检验' },
            { value: 0, name: '季度检验' },
            { value: 0, name: '抽样' },
          ]
        }
      ],
      barLegend: {},
      chartStyle: {
        width: '90%',
        height: '100%',
      },
      chartStyle2: {
        width: '90%',
        height: '80%',
      },
      xAxis1: [
        {
          type: 'value',
          min: 0,
          max: 100,
          axisLabel: {
            formatter: '{value} %'
          }
        }
      ],
      yAxis1: [
        {
          type: 'category',
          data: []
        }
      ],
      barSeries: [
        {
          type: 'bar',
          data: [],
          tooltip: {
            valueFormatter: function (value) {
              return value + ' %';
            }
          },
          label: {
            show: true,
            formatter: (params) => Math.round(params.value * 100) / 100 + '%'
          }
        },
      ],
      tableData: [],
      passRate: '',
      sum: '',
    }
  },
  mounted() {
    this.getBar()
    this.getRawPass()
    this.getOrderType()
    this.getPassRateCom()
  },
  // 方法集合
  methods: {
    // 获取合格率图表数据
    getBar() {
      const params = {
        dateType: this.dateType,
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
      }
      getRawPassRateByBarChart(params).then((res) => {
        let lineData = []
        let barData = []
        let xAxis = []
        res.data.forEach(item => {
          barData.push(item.sum)
          lineData.push(item.passRate)
          xAxis.push(item.searchTime)
        })
        this.echartsSeries[0].data = barData
        this.echartsSeries[1].data = lineData
        this.yAxis[0].max = Math.ceil(Math.max.apply(null, barData) / 50) * 50
        this.yAxis[0].interval = this.yAxis[0].max / 5
        this.xAxis[0].data = xAxis
      })
    },
    // 获取原材料合格率图表数据
    getRawPass() {
      const params = {
        dateType: this.dateType,
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
      }
      getRawPassRateByCake(params).then((res) => {
        this.materialPieSeries[0].data[0].value = res.data.unQualified
        this.materialPieSeries[0].data[1].value = res.data.qualified
        this.passRate = res.data.passRate + '%'
      })
    },
    // 获取本月检验类型数量
    getOrderType() {
      getOrderTypeCookie().then((res) => {
        this.materialPieSeries1[0].data[0].value = res.data.customer // 委托
        this.materialPieSeries1[0].data[1].value = res.data.enter // 进厂
        this.materialPieSeries1[0].data[2].value = res.data.quarterly // 季度
        this.materialPieSeries1[0].data[3].value = res.data.spotCheck // 抽样
      })
    },
    // 本月与上月合格率对比
    getPassRateCom() {
      getRawUpMonth().then((res) => {
        let month = []
        let barData = []
        res.data.forEach(item => {
          month.push(item.month)
          barData.push(item.passRate)
        })
        this.yAxis1[0].data = month
        this.barSeries[0].data = barData
      })
    },
    changeDate () {
      this.getBar()
      this.getRawPass()
      // this.getOrderType()
      // this.getPassRateCom()
    },
    changeDatePicker (val) {
      if (val) {
        this.beginDate = val[0] + ' 00:00:00'
        this.endDate = val[1] + ' 23:59:59'
      } else {
        this.beginDate = ''
        this.endDate = ''
      }
      this.getBar()
      this.getRawPass()
      // this.getOrderType()
      // this.getPassRateCom()
    },
  },
}
</script>

<style scoped>
.title {
  padding: 10px 0 0 20px;
}
.table {
  padding: 0 10px 10px;
}
.pie-card {
  width: 100%;
  background: #FFFFFF;
  margin-top: 10px;
  position: relative;
}
.data {
  position: absolute;
  font-size: 20px;
  transform: translate(-50%);
  left: 40%;
  top: 42%;
  z-index: 1;
}
.inspection-card{
  width: 100%;
  background: #FFFFFF;
  margin-top: 10px;
}
</style>
