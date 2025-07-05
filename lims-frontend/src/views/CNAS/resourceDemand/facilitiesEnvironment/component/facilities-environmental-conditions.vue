<template>
  <el-row>
    <el-col :span="2">
      <el-menu @select="clickSelect" :default-active="activeName">
        <el-menu-item index="电源稳定性">
          <span slot="title">电源稳定性</span>
        </el-menu-item>
        <el-menu-item index="防雷检测">
          <span slot="title">防雷检测</span>
        </el-menu-item>
        <el-menu-item index="照度记录">
          <span slot="title">照度记录</span>
        </el-menu-item>
        <el-menu-item index="温湿度记录">
          <span slot="title">温湿度记录</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="22" style="padding: 10px">
      <power-supply-stability v-if="activeName === '电源稳定性'"/>
      <lightning-protection-detection v-if="activeName === '防雷检测'" />
      <illuminance-recording v-if="activeName === '照度记录'"/>
      <temperature-humidity-record v-if="activeName === '温湿度记录'"/>
    </el-col>
  </el-row>
</template>

<script>
// 电源稳定性
import PowerSupplyStability from "./facilities-environmental-conditions/power-supply-stability.vue";
// 防雷检测
import LightningProtectionDetection from "./facilities-environmental-conditions/lightning-protection-detection.vue";
// 照度记录
import IlluminanceRecording from "./facilities-environmental-conditions/illuminance-recording.vue";
// 温湿度记录
import TemperatureHumidityRecord from "./facilities-environmental-conditions/temperature-humidity-record.vue";

export default {
  components: {TemperatureHumidityRecord, IlluminanceRecording, LightningProtectionDetection, PowerSupplyStability},
  data() {
    return {
      activeName: "电源稳定性"
    }
  },
  methods: {
    clickSelect(index) {
      this.activeName = index;
    }
  }
}
</script>
