<template>
  <div>
    <div>
      <el-row class="title">
        <el-col :span="6" style="padding-left: 20px;text-align: left;">数据分析</el-col>
        <el-col :span="18" style="text-align: right;">
          <el-button size="small" @click="$emit('goBack')">返回</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="container">
      <div>
        <div style="padding: 0 0 10px 10px">填写厂家数据</div>
        <el-form ref="supplierForm" :model="supplierForm" :rules="rules" class="demo-ruleForm" label-width="40px">
          <el-row :gutter="20">
            <el-col :span="3">
              <el-form-item label="1：" prop="one">
                <el-input v-model="supplierForm.one" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="2：" prop="two">
                <el-input v-model="supplierForm.two" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="3：" prop="three">
                <el-input v-model="supplierForm.three" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="4：" prop="four">
                <el-input v-model="supplierForm.four" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="5：" prop="five">
                <el-input v-model="supplierForm.five" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button size="small" type="primary" @click="submitForm()">数据分析</el-button>
                <el-button size="small" @click="resetForm()">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <table :border='true' class="table">
        <tr>
          <th class="thTitle">          </th>
          <th class="thTitle">1</th>
          <th class="thTitle">2</th>
          <th class="thTitle">3</th>
          <th class="thTitle">4</th>
          <th class="thTitle">5</th>
          <th class="thTitle">平均值</th>
          <th class="thTitle">标准偏差</th>
          <th class="thTitle">相对偏差</th>
          <th class="thTitle">平均标准偏差</th>
        </tr>
        <tr>
          <td class="thBack">厂家检测数据</td>
          <td v-for="(item,index) in supplierData" :key="index" class="tdData">{{item}}</td>
        </tr>
        <tr>
          <td class="thBack">检测中心数据</td>
          <td v-for="(item,index) in localData" :key="index" class="tdData">{{item}}</td>
        </tr>
        <tr>
          <td class="thBack">绝对偏差</td>
          <td v-for="(item,index) in absoluteDeviation" :key="index" class="tdData">{{item}}</td>
        </tr>
        <tr>
          <td class="thBack">平均值</td>
          <td v-for="(item,index) in average" :key="index" class="tdData">{{item}}</td>
        </tr>
        <tr>
          <td class="thBack2">厂家</td>
        </tr>
        <tr>
          <td class="thBack">UCL</td>
          <td v-for="(item,index) in supplierULC" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'a' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">LCL</td>
          <td v-for="(item,index) in supplierLCL" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'b' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">平均值</td>
          <td v-for="(item,index) in supplierAverage" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'c' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">极差</td>
          <td v-for="(item,index) in supplierRange" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'d' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack2">检测中心</td>
        </tr>
        <tr>
          <td class="thBack">UCL</td>
          <td v-for="(item,index) in localULC" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'e' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">LCL</td>
          <td v-for="(item,index) in localLCL" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'f' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">平均值</td>
          <td v-for="(item,index) in localAverage" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'g' + index" class="tdData"></td>
        </tr>
        <tr>
          <td class="thBack">极差</td>
          <td v-for="(item,index) in localRange" :key="index" class="tdData">{{item}}</td>
          <td v-for="(item,index) in empty" :key="'h' + index" class="tdData"></td>
        </tr>
      </table>
      <el-row>
        <el-col :span="12">
          <div class="inspection-card">
            <div class="echartsTitle">检测数据对比</div>
            <Echarts ref="chart"
                     :grid="grid"
                     :legend="legend"
                     :lineColors="lineColors"
                     :series="echartsSeries"
                     :tooltip="tooltip"
                     :xAxis="xAxis"
                     :yAxis="yAxis"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="inspection-card">
            <div class="echartsTitle">测量数据标准差对比</div>
            <Echarts ref="chart"
                     :barColors="barColors"
                     :chartStyle="chartStyle"
                     :grid="grid"
                     :legend="legend1"
                     :series="echartsSeries1"
                     :tooltip="tooltip"
                     :xAxis="xAxis1"
                     :yAxis="yAxis1"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div class="inspection-card">
            <div class="echartsTitle">偏差分析</div>
            <Echarts ref="chart"
                     :grid="grid"
                     :legend="legend2"
                     :lineColors="lineColors"
                     :series="echartsSeries2"
                     :tooltip="tooltip"
                     :xAxis="xAxis"
                     :yAxis="yAxis2"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="inspection-card">
            <div class="echartsTitle">检测中心数据</div>
            <Echarts ref="chart"
                     :grid="grid"
                     :legend="legend"
                     :lineColors="lineColors"
                     :series="echartsSeries3"
                     :tooltip="tooltip"
                     :xAxis="xAxis"
                     :yAxis="yAxis"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import Echarts from "./echarts.vue";

export default {
  name: "DataComparison",
  // import 引入的组件需要注入到对象中才能使用
  components: {Echarts},
  props: {
    comparisonData : {
      type: Object,
      default: () => {}
    },
    selectRow : {
      type: Object,
      default: () => {}
    }
  },
  data() {
    // 这里存放数据
    return {
      supplierForm: {
        one: '',
        two: '',
        three: '',
        four: '',
        five: '',
      },
      rules: {
        one: [{ required: true, message: '必填', trigger: 'blur' }],
        two: [{ required: true, message: '必填', trigger: 'blur' }],
        three: [{ required: true, message: '必填', trigger: 'blur' }],
        four: [{ required: true, message: '必填', trigger: 'blur' }],
        five: [{ required: true, message: '必填', trigger: 'blur' }],
      },
      comparisonData1: {},
      supplierData: [], // 厂家数据
      localData: [], // 检测中心检测数据
      absoluteDeviation: [], // 绝对偏差
      average: [], // 平均值
      supplierULC: [], // 厂家UCL
      supplierLCL: [], // 厂家LCL
      supplierAverage: [], // 厂家平均值
      supplierRange: [], // 厂家极差
      localULC: [], // 检测中心UCL
      localLCL: [], // 检测中心LCL
      localAverage: [], // 检测中心平均值
      localRange: [], // 检测中心极差
      manufacturerData: [],
      manufacturer: [
        {value: '1.1'},
        {value: '2.2'},
        {value: '3.3'},
        {value: '4.4'},
        {value: '5.5'},
      ],
      testCenter: [
        {value: '1.2'},
        {value: '2.33'},
        {value: '3.64'},
        {value: '4.3'},
        {value: '5.9'},
      ],
      empty: [
        {data: '1'},
        {data: '2'},
        {data: '3'},
        {data: '4'},
      ],
      lineColors: ['#ed7d31', '#409EFF'],
      barColors: ['#ed7d31', '#409EFF', '#a5a5a5'],
      chartStyle: {
        width: '100%',
        height: '96%' // 设置图表容器的高度
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      legend: {
        data: ['厂家检测数据','检测中心检测数据']
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: [{
        type: 'category',
        data: ['1', '2', '3', '4', '5']
      }],
      yAxis: [{
        type: 'value'
      }],
      echartsSeries: [
        {
          name: '厂家检测数据',
          type: 'line',
          label: {
            show: true,
            position: 'top',
          },
          data: []
        },
        {
          name: '检测中心检测数据',
          type: 'line',
          label: {
            show: true,
            position: 'top',
          },
          data: []
        }
      ],
      xAxis1: [{
        type: 'category',
        data: ['厂家', '检测中心']
      }],
      legend1: {
        data: ['UCL','LCL', '平均值']
      },
      yAxis1: [
        {
          type: 'value',
        },
      ],
      echartsSeries1: [
        {
          name: 'UCL',
          type: 'bar',
          label: {
            show: true,
            position: 'top'
          },
          data: []
        },
        {
          name: 'LCL',
          type: 'bar',
          label: {
            show: true,
            position: 'top'
          },
          data: []
        },
        {
          name: '平均值',
          type: 'bar',
          label: {
            show: true,
            position: 'top'
          },
          data: []
        },
      ],
      legend2: {
        data: ['平均值','绝对偏差']
      },
      yAxis2: [{
        type: 'value',
        min: 0,
        max: 1,
      }],
      echartsSeries2: [
        {
          name: '平均值',
          type: 'line',
          label: {
            show: true,
            position: 'top',
            formatter: (params) => Math.round(params.value * 1000) / 10 + '%'
          },
          data: [],
        },
        {
          name: '绝对偏差',
          type: 'line',
          label: {
            show: true,
            position: 'top',
            formatter: (params) => Math.round(params.value * 1000) / 10 + '%'
          },
          data: [],
        }
      ],
      echartsSeries3: [
        {
          name: '检测中心检测数据',
          type: 'line',
          data: []
        }
      ],
    }
  },
  mounted() {
    this.getInfo()
  },
  // 方法集合
  methods: {
    getInfo () {
      this.localData = this.comparisonData.localData === null ? ['', '', '', '', '','','', '', ''] : this.comparisonData.localData
      this.localData = this.localData.map((number, index) => index === 7 ? `${Math.round(number * 100)}%` : number)
      this.echartsSeries3[0].data = this.localData.slice(0, 5)
      this.localULC = this.comparisonData.localULC
      this.localLCL = this.comparisonData.localLCL
      this.localAverage = this.comparisonData.localAverage
      this.localRange = this.comparisonData.localRange
    },
    getInfo1 () {
      this.echartsSeries1[0].data = []
      this.supplierData = this.comparisonData1.supplierData === null ? ['', '', '', '', '','','', '', ''] : this.comparisonData1.supplierData
      this.localData = this.comparisonData1.localData === null ? ['', '', '', '', '','','', '', ''] : this.comparisonData1.localData
      this.localData = this.localData.map((number, index) => index === 7 ? `${Math.round(number * 100)}%` : number)
      this.echartsSeries3[0].data = this.localData.slice(0, 5)
      this.echartsSeries[0].data = this.supplierData
      this.echartsSeries[1].data = this.localData
      this.absoluteDeviation = this.comparisonData1.absoluteDeviation === null ? ['', '', '', '', '','','', '', ''] : this.comparisonData1.absoluteDeviation
      this.echartsSeries2[1].data = this.absoluteDeviation
      this.absoluteDeviation = this.absoluteDeviation.map(number => `${Math.round(number * 100)}%`) // 绝对偏差百分比转换
      this.average = this.comparisonData1.average === null ? ['', '', '', '', '','','', '', ''] : this.comparisonData1.average
      this.echartsSeries2[0].data = this.average
      this.average = this.average.map(number => `${Math.round(number * 100)}%`) // 平均值百分比转换
      this.supplierULC = this.comparisonData1.supplierULC === null ? ['', '', '', '', ''] : this.comparisonData1.supplierULC
      this.echartsSeries1[0].data.push(this.supplierULC[0]) // 测量数据标准差对比柱状图数据
      this.supplierLCL = this.comparisonData1.supplierLCL === null ? ['', '', '', '', ''] : this.comparisonData1.supplierLCL
      this.echartsSeries1[1].data.push(this.supplierLCL[0]) // 测量数据标准差对比柱状图数据
      this.supplierAverage = this.comparisonData1.supplierAverage === null ? ['', '', '', '', ''] : this.comparisonData1.supplierAverage
      this.echartsSeries1[2].data.push(this.supplierAverage[0]) // 测量数据标准差对比柱状图数据
      this.supplierRange = this.comparisonData1.supplierRange === null ? ['', '', '', '', ''] : this.comparisonData1.supplierRange
      this.localULC = this.comparisonData1.localULC
      this.echartsSeries1[0].data.push(this.localULC[0]) // 测量数据标准差对比柱状图数据
      this.localLCL = this.comparisonData1.localLCL
      this.echartsSeries1[1].data.push(this.localLCL[0]) // 测量数据标准差对比柱状图数据
      this.localAverage = this.comparisonData1.localAverage
      this.echartsSeries1[2].data.push(this.localAverage[0]) // 测量数据标准差对比柱状图数据
      this.localRange = this.comparisonData1.localRange
    },
    submitForm () {
      this.$refs['supplierForm'].validate((valid) => {
        if (valid) {
          const params = {
            orderIds: this.selectRow.orderIds,
            itemNames: this.selectRow.itemNames,
            supplierDataList: Object.values(this.supplierForm)
          }
          this.$axios.post(this.$api.dataAnalysis.getRawSupplierCompare, params, {
            headers: {
              'Content-Type': 'application/json'
            },
            noQs: true
          }).then(res => {
            this.comparisonData1 = res.data
            this.getInfo1()
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm () {
      this.$refs['supplierForm'].resetFields();
    },
  },
}
</script>

<style scoped>
.title {
  height: 60px;
  line-height: 60px;
}
.container {
  width: calc(100% - 20px);
  height: calc(100vh - 170px);
  background-color: #fff;
  padding: 10px;
  overflow-y: auto;
}
.table {
  width: 100%;
}
.thTitle {
  background-color: #e0eaf5;
  padding: 6px 2px;
}
.thBack {
  text-align: center;
  background-color: #e0eaf5;
  padding: 2px;
}
.thBack2 {
  text-align: center;
  background-color: #e4f2da;
  padding: 2px;
}
.tdData {
  padding: 4px;
  text-align: center;
  font-size: 13px;
  width: 10%;
}
.inspection-card{
  width: 100%;
  background: #FFFFFF;
  margin-top: 10px;
}
.echartsTitle {
  text-align: center;
  padding-bottom: 10px;
}
</style>
