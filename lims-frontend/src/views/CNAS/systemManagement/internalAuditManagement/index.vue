<template>
  <div class="capacity-scope">
    <el-tabs v-model="activeName" class="tab-panel" type="border-card">
      <el-tab-pane label="年度计划" name="yearPlan">
        <year-plan></year-plan>
      </el-tab-pane>
      <el-tab-pane label="内部实施计划" name="implementationPlan">
        <implementation-plan></implementation-plan>
      </el-tab-pane>
      <el-tab-pane label="内审会议签到" name="meetingSignIn">
        <audit-meeting-sign></audit-meeting-sign>
      </el-tab-pane>
      <el-tab-pane label="内审检查" name="auditInspection">
        <audit-inspection></audit-inspection>
      </el-tab-pane>
      <el-tab-pane label="纠正措施" name="correctiveAction">
        <corrective-action></corrective-action>
      </el-tab-pane>
      <el-tab-pane label="内审报告" name="auditReport">
        <audit-report></audit-report>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import YearPlan from './components/yearPlan.vue';
import implementationPlan from './components/implementationPlan.vue';
import AuditInspection from './components/auditInspection.vue';
import AuditReport from './components/auditReport.vue';
import AuditMeetingSign from './components/auditMeetingSign.vue';
import CorrectiveAction from './components/correctiveAction.vue';

export default {
  name: 'InternalAuditManagement',
  // import 引入的组件需要注入到对象中才能使用
  components: { CorrectiveAction, AuditMeetingSign, AuditReport, AuditInspection, YearPlan, implementationPlan },
  data() {
    // 这里存放数据
    return {
      activeName: 'yearPlan',
    };
  },
  // 方法集合
  methods: {}
};
</script>

<style scoped>
</style>
