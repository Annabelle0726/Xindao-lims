<template>
  <div class="capacity-scope">
    <el-tabs v-model="activeName" :lazy="true" type="border-card">
      <el-tab-pane label="设施和环境条件要求" name="设施和环境条件要求">
        <FacilitiesEnvironmentalConditions v-if="activeName === '设施和环境条件要求'"/>
      </el-tab-pane>
      <el-tab-pane label="三废处理" name="三废处理">
        <a6-three-wastes-treatment v-if="activeName === '三废处理'"></a6-three-wastes-treatment>
      </el-tab-pane>
      <el-tab-pane label="进出人员管理" name="进出人员管理">
        <personnel-management v-if="activeName === '进出人员管理'"></personnel-management>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import FacilitiesEnvironmentalConditions from "../facilitiesEnvironment/component/facilities-environmental-conditions.vue";
import A6ThreeWastesTreatment from '../facilitiesEnvironment/component/three-wastes-treatment.vue';
import PersonnelManagement from '../facilitiesEnvironment/component/Personnel-management.vue';
export default {
  name: 'FacilitiesEnvironment',
  components: {
    A6ThreeWastesTreatment,
    PersonnelManagement,
    FacilitiesEnvironmentalConditions
  },
  data() {
    return {
      activeName: '设施和环境条件要求',
    }
  }
}
</script>
<style scoped>
.main {
  width: 100%;
  border-radius: 15px;
}

</style>
