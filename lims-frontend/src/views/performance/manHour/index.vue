<template>
  <div class="work-time-management">
    <div style="text-align: left">
      <el-radio-group v-model="currentComponent" size="small" style="margin-top: 16px; margin-left: 16px">
        <el-radio-button v-if="checkPermi(['performance:manHour:workTimeStatistics'])" label="workTimeStatistics">
          工时汇总
        </el-radio-button>
        <el-radio-button v-if="checkPermi(['performance:manHour:workTimeManagement'])" label="workTimeManagement">
          日工时管理
        </el-radio-button>
        <el-radio-button v-if="checkPermi(['performance:manHour:workTimeConfig'])" label="workTimeConfig">
          辅助工时配置
        </el-radio-button>
      </el-radio-group>
    </div>
    <component :is="currentComponent"></component>
  </div>
</template>

<script>
import workTimeStatistics from "./workTimeStatistics.vue";
import workTimeManagement from "./workTimeManagement.vue";
import workTimeConfig from "./workTimeConfig.vue";
export default {
  name: "ManHour",
  components: {
    workTimeStatistics,
    workTimeManagement,
    workTimeConfig,
  },
  created() { },
  data() {
    return {
      currentComponent: "workTimeStatistics",
    };
  },
};
</script>

<style scoped></style>
