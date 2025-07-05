<!-- 设备运行总览 -->
<template>
  <div>
    <div class="page">
      <echart-module id="'page-left'" :config="chartConfig" :datas="chartData" class="page-left"></echart-module>
      <div class="page-right">
        <div class="form-item">
          <label>启用时长(年)</label>
          <span>{{ deviceData.usedYears }}</span>
        </div>
        <div class="form-item">
          <label>故障次数</label>
          <span>{{ deviceData.faultCount }}</span>
        </div>
        <div class="form-item">
          <label>最近故障日期</label>
          <span>{{ deviceData.faultDate }}</span>
        </div>
        <div class="form-item">
          <label>最近校准日期</label>
          <span>{{ deviceData.lastCalibrationDate }}</span>
        </div>
        <div class="form-item">
          <label>下次校准日期</label>
          <span>{{ deviceData.nextCalibrationDate }}</span>
        </div>
        <div class="form-item">
          <label>校准总结论</label>
          <span :class="formatColorStyle(deviceData.calibrateStatus)">{{ deviceData.calibrateStatus }}</span>
        </div>
        <div class="form-item">
          <label>最近核查日期</label>
          <span>{{ deviceData.lastExamineDate }}</span>
        </div>
        <div class="form-item">
          <label>下次核查日期</label>
          <span>{{ deviceData.nextExamineDate }}</span>
        </div>
        <div class="form-item">
          <label>核查总结论</label>
          <span :class="formatColorStyle(deviceData.examineStatus)">{{ deviceData.examineStatus }}</span>
        </div>
        <div class="form-item">
          <label>设备运行状态</label>
          <span :class="formatColorStyle(deviceData.deviceStatus)">{{ deviceData.deviceStatus }}</span>
        </div>
        <div class="form-item">
          <label>最近维护日期</label>
          <span>{{ deviceData.maintenanceDate }}</span>
        </div>
        <div class="form-item">
          <label>下次维护日期</label>
          <span>{{ deviceData.nextMaintenanceDate }}</span>
        </div>
        <div class="form-item">
          <label>维护类型</label>
          <span>{{ deviceData.maintenanceType }}</span>
        </div>
        <div class="form-item">
          <label>测量项目</label>
          <span>{{ deviceData.insProduct }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EchartModule from '@/components/Echart/echart.vue'

export default {
  components: { EchartModule },
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    return {
      chartConfig: {
        height: '',
        isLoading: true,
        type: 'gauge'
      },
      chartData: {
        formatter: "已过校准日期百分比: {c}%",
        color: [
          [0.3, '#21a700'],
          [0.7, '#0066ff'],
          [1, '#d80000']
        ],
        value: 0,
      },
      form: {
        value1: ''
      },
      formData: {
        usedAge: 12
      },
      //设备数据收集
      deviceData: {

      }
    }
  },
  mounted() {
    //获取设备故障的信息
    this.getDeviceInfo(this.clickNodeVal.value);
  },
  methods: {
    formatColorStyle(status) {
      if (status == null || status == "" || status == undefined) {
        return ""
      }
      let styleStr = ''
      switch (status) {
        case "合格":
          styleStr = 'success'
          break;
        case "不合格":
          styleStr = 'failed'
          break;
        case "其他":
          styleStr = 'other'
          break;
        case "正常":
          styleStr = 'success'
          break;
        case "维修":
          styleStr = 'other'
          break;
        case "停用":
          styleStr = 'stop'
          break;
        case "报废":
          styleStr = 'failed'
          break;
      }
      return styleStr;
    },
    //获取设备运行总览信息
    getDeviceInfo(deviceId) {
      this.$axios.get(this.$api.deviceFault.getDevice + "/" + deviceId).then(res => {
        if (res.code == 200) {
          this.deviceData = res.data
          this.chartData.value = res.data.progress
        }
      })
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      // 是否存在value，存在value代表为三级
      if (this.clickNodeVal.value) {
        this.getDeviceInfo(newVal.value)
      }
    }
  }
}
</script>

<style scoped>
.page {
  width: 100%;
  height: 100vh;
  display: flex;
}

.page-left {
  width: 420px;
  height: 420px;
  margin-top: 40px;
  margin-right: 40px;
}

.page-right {
  flex: 1;
  height: calc(100% - 200px);
  overflow: scroll;
  /* background: red; */
}

.form-item {
  line-height: 42px;
  font-size: 14px;
}

.page-right label {
  color: #999;
  width: 110px;
  display: inline-block;
  text-align: right;
  margin-right: 20px;
}

.el-radio__input.is-disabled+span.el-radio__label {
  color: #606266 !important;
}

.success {
  color: #21a700
}

.failed {
  color: #d80000
}

.other {
  color: #e8a849;
}

.stop {
  color: #909399;
}
</style>
