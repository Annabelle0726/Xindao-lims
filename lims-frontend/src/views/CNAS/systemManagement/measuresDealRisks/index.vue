<template>
  <div class="capacity-scope">
    <el-tabs v-model="activeName" type="border-card" :lazy="true">
      <el-tab-pane label="危险因素辨识与风险评价结果一览" name="危险因素辨识与风险评价结果一览">
        <HazardIdentificationRiskAssessment v-if="activeName === '危险因素辨识与风险评价结果一览'" />
      </el-tab-pane>
      <el-tab-pane label="重大风险因素分析及控制计划清单" name="重大风险因素分析及控制计划清单">
        <listRiskAnalysisControlPlans v-if="activeName === '重大风险因素分析及控制计划清单'" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

import HazardIdentificationRiskAssessment
  from "./components/hazardIdentificationRiskAssessment.vue";
import listRiskAnalysisControlPlans
  from "./components//listRiskAnalysisControlPlans.vue";
export default {
  name: 'MeasuresDealRisks',
  components: { HazardIdentificationRiskAssessment, listRiskAnalysisControlPlans },
  data() {
    return {
      activeName: '危险因素辨识与风险评价结果一览',
    }
  }
}
</script>

<style scoped>
/deep/ .el-tabs--border-card>.el-tabs__content {
  height: calc(100vh - 9em);
  padding: 0;
}
</style>
