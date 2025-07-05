<template>
  <div class="device-page">
    <div class="device-left">
      <el-input v-model="deviceName" class="div_left_input" clearable placeholder="输入设备名称" size="small"
        suffix-icon="el-icon-search" @blur="searchFilter" @clear="searchFilter"
        @keyup.enter.native="searchFilter"></el-input>
      <el-tree ref="tree" v-loading="loading" :data="list" :expand-on-click-node="false"
        :filter-node-method="filterNode" :props="{ children: 'children', label: 'label' }" highlight-current
        node-key="id" style="height:calc(100vh - 200px);
        overflow-y: scroll;
        scrollbar-width: none;" @node-click="handleNodeClick" @node-expand="nodeOpen" @node-collapse="nodeClose">
        <div slot-scope="{ node, data }" class="custom-tree-node">
          <el-row style="width: 100%;">
            <el-col :span="24">
              <p class="single-line-ellipsis" style="width: 100%">
                <i :class="`node_i ${data.children != undefined
                  ? data.code === '[1]'
                    ? 'el-icon-folder-opened'
                    : 'el-icon-folder'
                  : 'el-icon-tickets'
                  }`
                  "></i>
                {{ data.label }}
              </p>
              <p>
                {{ data.managementNumber === undefined ? '' : data.managementNumber }}
              </p>
            </el-col>
          </el-row>
        </div>
      </el-tree>
    </div>
    <div class="device-right">
      <div v-if="isShowAll" style="height: 100%;">
        <el-tabs v-model="menuListActiveName" class="main_right" type="border-card">
          <el-tab-pane label="设备总览" name="设备总览">
            <overview v-if="menuListActiveName == '设备总览'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备工具明细" name="设备工具明细">
            <management v-if="menuListActiveName == '设备工具明细'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备校准计划" name="设备校准计划">
            <equipment-calibration-plan v-if="menuListActiveName == '设备校准计划'"
              :clickNodeVal="clickNodeVal"></equipment-calibration-plan>
          </el-tab-pane>
          <el-tab-pane label="设备核查计划" name="设备核查计划">
            <equipment-verification-plan v-if="menuListActiveName == '设备核查计划'"
              :clickNodeVal="clickNodeVal"></equipment-verification-plan>
          </el-tab-pane>
          <el-tab-pane label="设备保养计划" name="设备保养计划">
            <equipment-maintenance-plan v-if="menuListActiveName == '设备保养计划'"
              :clickNodeVal="clickNodeVal"></equipment-maintenance-plan>
          </el-tab-pane>
          <el-tab-pane label="量值溯源计划" name="量值溯源计划">
            <quantity-value-traceability-plan v-if="menuListActiveName == '量值溯源计划'"
              :clickNodeVal="clickNodeVal"></quantity-value-traceability-plan>
          </el-tab-pane>
          <el-tab-pane label="利用外部仪器设备" name="利用外部仪器设备">
            <using-external-instruments v-if="menuListActiveName == '利用外部仪器设备'" :clickNodeVal="clickNodeVal"></using-external-instruments>
          </el-tab-pane>
          <el-tab-pane label="使用记录" name="使用记录">
            <record v-if="menuListActiveName == '使用记录'" :isMenuList="1" />
          </el-tab-pane>
        </el-tabs>
      </div>
      <div v-if="!isShowAll" style="height: 100%;">
        <el-tabs v-model="tabListActiveName" class="main_right" type="border-card" @tab-click="handleClick">
          <el-tab-pane label="设备档案" name="设备档案">
            <files v-if="tabListActiveName == '设备档案'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备验收" name="设备验收">
            <equipment-acceptance v-if="tabListActiveName == '设备验收'" :clickNodeVal="clickNodeVal"></equipment-acceptance>
          </el-tab-pane>
          <el-tab-pane label="设备校准" name="设备校准">
            <calibration v-if="tabListActiveName == '设备校准'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备维护" name="设备维护">
            <maintenance v-if="tabListActiveName == '设备维护'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备借用" name="设备借用">
            <borrow v-if="tabListActiveName == '设备借用'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="使用记录" name="使用记录">
            <record v-if="tabListActiveName == '使用记录'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备停用/启用" name="设备停用/启用">
            <state v-if="tabListActiveName == '设备停用/启用'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备报废" name="设备报废">
            <equipment-scrap v-if="tabListActiveName == '设备报废'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备故障与维修" name="设备故障与维修">
            <equipment-failure v-if="tabListActiveName == '设备故障与维修'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备点检" name="设备点检">
            <inspection-of-equipment v-if="tabListActiveName == '设备点检'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="设备事故" name="设备事故">
            <equipment-accident v-if="tabListActiveName == '设备事故'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
          <el-tab-pane label="作业指导书" name="作业指导书">
            <operationInstruction v-if="tabListActiveName == '作业指导书'" :clickNodeVal="clickNodeVal" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import operationOverview from "./component/operationOverview.vue";
import files from "./component/files.vue";
import calibration from "./component/calibration.vue";
import check from "./component/check.vue";
import maintenance from "./component/maintenance.vue";
import borrow from "./component/borrow.vue";
import fault from "./component/fault.vue";
import record from "./component/record.vue";
import state from "./component/state.vue";
import management from "./component/management.vue";
import overview from "./component/deviceOverview.vue";
import resourceReservation from "./component/resourceReservation.vue";
import operationInstruction from "./component/operationInstruction.vue";
import EquipmentCalibrationPlan from "./component/equipmentCalibrationPlan.vue";
import EquipmentVerificationPlan from "./component/equipmentVerificationPlan.vue";
import UsingExternalInstruments from "./component/usingExternalInstruments.vue";
import EquipmentScrap from "./component/equipmentScrap.vue";
import EquipmentFailure from "./component/equipmentFailure.vue";
import InspectionOfEquipment from "./component/inspectionOfEquipment.vue";
import EquipmentAccident from "./component/equipmentAccident.vue";
import EquipmentMaintenancePlan from "./component/equipmentMaintenancePlan.vue";
import EquipmentAcceptance from "./component/equipmentAcceptance.vue";
import QuantityValueTraceabilityPlan from "./component/quantityValueTraceabilityPlan.vue";
import {
  treeDevice,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  name: 'Device',
  components: {
    QuantityValueTraceabilityPlan,
    EquipmentAcceptance,
    EquipmentMaintenancePlan,
    EquipmentAccident,
    InspectionOfEquipment,
    EquipmentFailure,
    EquipmentScrap,
    UsingExternalInstruments,
    EquipmentVerificationPlan,
    EquipmentCalibrationPlan,
    operationOverview,
    files,
    calibration,
    check,
    maintenance,
    borrow,
    fault,
    record,
    state,
    management,
    overview,
    resourceReservation,
    operationInstruction
  },
  data() {
    return {
      isShowAll: true,
      deviceName: "", // 侧边栏搜索
      loading: false,
      tabListActiveName: '设备档案',
      menuListActiveName: '设备总览',
      list: [],
      clickNodeVal: {}
    };
  },
  mounted() {
    // 初始化调用
    this.geList();
  },
  methods: {
    handleClick(tab, event) {
    },
    searchFilter() {
      this.$refs.tree.filter(this.deviceName)
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 父级
    geList() {
      this.loading = true;
      treeDevice().then(res => {
        let data = res.data;
        this.list = data;
        this.loading = false
      });
    },
    handleNodeClick(val, node, el) {
      // 点击临时缓存
      this.clickNodeVal = val;
      // 存储父级节点级数
      this.clickNodeVal.level = node.level
      this.clickNodeVal.parent = node.parent.data
      // 是否显示设备详情
      if (!val.value) {
        this.isShowAll = true
      } else {
        this.isShowAll = false
      }
    },
    nodeOpen(data, node, el) {
      // $($(el.$el).find(".node_i")[0]).attr(
      //   "class",
      //   "node_i el-icon-folder-opened"
      // );
    },
    nodeClose(data, node, el) {
      // $($(el.$el).find(".node_i")[0]).attr("class", "node_i el-icon-folder");
    },
  }
};
</script>

<style scoped>
.device-page {
  display: flex;
  padding-top: 10px;
  box-sizing: border-box;
  width: 100%;
}

.device-left {
  width: 250px;
  height: 100%;
  background: #fff;
  border-radius: 16px;
  box-sizing: border-box;
  padding: 10px 16px;
  flex-shrink: 0;
  padding-right: 0;
}

.device-right {
  background: #fff;
  width: calc(100% - 250px);
  height: calc(100vh - 100px);
  border-radius: 16px;
  box-sizing: border-box;
  padding: 10px;
}

.showLargeCategory {
  width: 100%;
  height: 100%;
}

.device-right-content {
  /* margin-top: 10px; */
  height: calc(100% - 42px);
  width: 100%;
}

.custom-tree-node {
  width: 100%;
  line-height: 32px;
  font-size: 14px;
}

.node_i {
  color: orange;
  font-size: 18px;
}

.main_right {
  height: calc(100% - 5px);
  overflow: hidden;
}

>>>.el-tabs__content {
  padding-top: 0;
  max-height: 95%; /* 根据需求调整高度 */
  overflow-y: auto; /* 垂直方向超出时显示滚动条 */
}

>>>.single-line-ellipsis {
  margin: 0 !important;
}

>>>.el-tree-node__content {
  text-align: left;
  align-items: start;
  margin: 4px;
  height: 100%;
}
</style>
