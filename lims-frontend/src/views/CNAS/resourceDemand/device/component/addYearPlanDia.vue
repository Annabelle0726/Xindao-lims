<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="applicationForm"
      title="添加设备校准计划" width="80%" @close="closeDialog">
      <div style="display: flex;align-items: center;">
        <el-button size="small" style="margin-right: 10px" type="primary" @click="addTableRow">添加</el-button>
        <span style="width: 80px">计划名称：</span>
        <el-input v-model="form.planName" clearable size="small" style="width: 300px;margin-right: 10px"></el-input>
        <span style="width: 60px">年份：</span>
        <el-date-picker v-model="form.planYear" clearable format="yyyy" placeholder="选择年" size="small" type="year"
          value-format="yyyy">
        </el-date-picker>
      </div>
      <div style="margin: 10px 0">
        <el-table id="templateParamTable" ref="yearTable" :data="calibrationPlanDetailList" height="300px"
                  v-loading="yearTableLoading"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          row-key="deviceId" style="width: 100% ;">
          <el-table-column label="设备名称及型号" min-width="190" prop="deviceName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceName" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="仪器编号" min-width="190" prop="deviceNumber">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceNumber" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="设备数量" min-width="120" prop="deviceAmount">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceAmount" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="检定单位" min-width="120" prop="verificationUnit">
            <template slot-scope="scope">
              <el-input v-model="scope.row.verificationUnit" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="核准周期(月)" min-width="120" prop="verificationCycles">
            <template slot-scope="scope">
              <el-input v-model="scope.row.verificationCycles" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="最近校准时间" min-width="170" prop="lastDate">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.lastDate" clearable format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width:100%" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="本年计划校准时间" min-width="170" prop="planDate">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.planDate" clearable format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width:100%" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="170" prop="remark">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" fixed="right" label="操作" width="80">
            <template slot-scope="scope">
              <el-button style="color: #f56c6c" type="text" @click="deleteRow(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button :loading="submitFormLoading" type="primary" @click="submitForm">确 认</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="addEquipDia" title="新增年度计划"
      width="50%">
      <el-table ref="multipleTable" :data="equipOptions" height="500" style="width: 100%" tooltip-effect="dark"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="设备名称" prop="label"></el-table-column>
        <el-table-column label="设备编号" prop="value"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addEquipDia = false">取 消</el-button>
        <el-button :loading="submitFormLoading" type="primary" @click="changeMachineName">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeviceCalibrationPlan,
  addDeviceCalibrationPlan,
  updateDeviceCalibrationPlan,
  deviceScopeSearch
} from '@/api/cnas/resourceDemand/device.js'
import Sortable from "sortablejs";

export default {
  name: "addYearPlanDia",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      applicationForm: false,
      addEquipDia: false,
      submitFormLoading: false,
      equipOptions: [],
      calibrationPlanDetailList: [],
      selectionRows: [],
      form: {
        planName: '',
        planYear: '',
        planId: '',
        calibrationPlanDetailList: [],
      },
      operationType: '',
      yearTableLoading: false
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDialog(type, row) {
      this.operationType = type
      if (type === 'add') {
        this.form = {
          planName: '',
          planYear: '',
          calibrationPlanDetailList: [],
        }
        this.calibrationPlanDetailList = []
      } else {
        this.getRowInfo(row)
      }
      this.applicationForm = true
    },
    getRowInfo(row) {
      this.yearTableLoading = true
      getDeviceCalibrationPlan({ planId: row.planId }).then(res => {
        this.yearTableLoading = false
        if (res.code == 200) {
          this.calibrationPlanDetailList = res.data.calibrationPlanDetailList
          this.form.planName = res.data.planName
          this.form.planYear = res.data.planYear
          this.form.planId = res.data.planId
        }
      }).catch(err => {
        this.yearTableLoading = false
      })
    },
    // 添加设备
    addTableRow() {
      this.addEquipDia = true
      this.getEquipOptions()
    },
    handleSelectionChange(selection) {
      this.selectionRows = selection
    },
    changeMachineName() {
      this.selectionRows.map(val => {
        // 将 val.nextCalibrationDate 转换为 Date 对象并减去 5 天
        let nextCalibrationDate = val.nextCalibrationDate ? new Date(val.nextCalibrationDate) : null;
        if (nextCalibrationDate) {
          nextCalibrationDate.setDate(nextCalibrationDate.getDate() - 5); // 减去 5 天
        }
        this.calibrationPlanDetailList.push({
          deviceId: val.id, deviceName: val.label, deviceNumber: val.value,
          specificationModel: val.specificationModel,
          verificationCycles: val.calibrationDate,
          lastDate: val.lastCalibrationDate && val.lastCalibrationDate.substring(0, 10),
          planDate: nextCalibrationDate ? formatDate(nextCalibrationDate) : null,
          verificationUnit: val.calibrationServices,
          deviceAmount: 1
        })
      })
      this.addEquipDia = false
      this.rowDrop()
    },
    rowDrop() {
      const that = this
      const tbody = document.querySelector(
        '#templateParamTable .el-table__body-wrapper tbody'
      )
      if (!this.sortTable) {
        this.sortTable = Sortable.create(tbody, {
          animation: 200, //动画时长
          handle: ".el-table__row", //可拖拽区域class
          //拖拽中事件
          onMove: ({ dragged, related }) => {
            const oldRow = that.calibrationPlanDetailList[dragged.rowIndex] //旧位置数据
            const newRow = that.calibrationPlanDetailList[related.rowIndex] //被拖拽的新数据
          },
          //拖拽结束事件
          onEnd: evt => {
            const curRow = that.calibrationPlanDetailList.splice(evt.oldIndex, 1)[0];
            that.calibrationPlanDetailList.splice(evt.newIndex, 0, curRow);
          }
        })
      }
    },
    // 删除表格行
    deleteRow(index) {
      this.calibrationPlanDetailList.splice(index, 1)
    },
    // 提交新增
    submitForm() {
      this.form.calibrationPlanDetailList = this.HaveJson(this.calibrationPlanDetailList)
      this.submitFormLoading = true
      if (this.operationType === 'add') {
        addDeviceCalibrationPlan(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('新增成功')
            this.closeDialog()
            this.$emit('closeDia')
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      } else {
        updateDeviceCalibrationPlan(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('新增成功')
            this.closeDialog()
            this.$emit('closeDia')
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      }
    },
    closeDialog() {
      this.applicationForm = false
    },
    // 获取所有设备
    getEquipOptions() {
      this.equipOptions = []
      deviceScopeSearch({ status: 0 }).then(res => {
        if (res.code === 200 && res.data) {
          this.equipOptions = res.data.map(m => {
            m.value = m.managementNumber
            m.label = m.deviceName
            return m
          })
        }
      }).catch(error => {
        console.error(error)
      })
    },
  },
}
// 格式化日期为 YYYY-MM-DD 格式
function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要+1
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
</script>

<style scoped></style>
