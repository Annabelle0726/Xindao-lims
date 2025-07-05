<template>
  <div class="capacity-scope">
    <el-tabs v-model="activeName" class="tab-panel" type="border-card">
      <el-tab-pane label="管理评审计划" name="yearPlan">
        <management-review-plan></management-review-plan>
      </el-tab-pane>
      <el-tab-pane label="会议记录" name="implementationPlan">
        <meeting-records></meeting-records>
      </el-tab-pane>
      <el-tab-pane label="评审报告" name="meetingSignIn">
        <review-report></review-report>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ManagementReviewPlan from './components/managementReviewPlan.vue';
import MeetingRecords from './components/meetingRecords.vue';
import ReviewReport from './components/reviewReport.vue';

export default {
  name: 'a9-management-review',
  // import 引入的组件需要注入到对象中才能使用
  components: { ReviewReport, MeetingRecords, ManagementReviewPlan },
  data() {
    // 这里存放数据
    return {
      activeName: 'yearPlan',
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {}
};
</script>

<style scoped>
</style>
