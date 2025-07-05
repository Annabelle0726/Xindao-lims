<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="applicationForm"
      title="添加设备核查计划" width="80%" @close="closeDialog">
      <div style="display: flex;align-items: center;">
        <el-button style="margin-right: 10px" size="small" type="primary" @click="addTableRow">添加</el-button>
        <span style="width: 80px">计划名称：</span>
        <el-input style="width: 300px;margin-right: 10px" v-model="form.planName" clearable size="small"></el-input>
        <span style="width: 60px">年份：</span>
        <el-date-picker v-model="form.planYear" type="year" value-format="yyyy" clearable size="small" format="yyyy"
          placeholder="选择年">
        </el-date-picker>
      </div>
      <div style="margin: 10px 0">
        <el-table ref="yearTable" :data="examinePlanDetailsList" id="templateParamTable" row-key="deviceId"
                  v-loading="yearTableLoading"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border height="500px">
          <el-table-column label="设备名称" min-width="190" prop="deviceName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceName" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="设备编号" min-width="190" prop="deviceNumber">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceNumber" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="核查时间" min-width="170" prop="checkTime">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.checkTime" clearable format="yyyy-MM" placeholder="选择日期" size="small"
                style="width:100%" type="month" value-format="yyyy-MM">
              </el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="核查指标" min-width="120" prop="checkIndex">
            <template slot-scope="scope">
              <el-input v-model="scope.row.checkIndex" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="核查方法" min-width="150" prop="checkMethod">
            <template slot-scope="scope">
              <el-input v-model="scope.row.checkMethod" clearable size="small" type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="结果如何判定" min-width="150" prop="howResults">
            <template slot-scope="scope">
              <el-input v-model="scope.row.howResults" clearable size="small" type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="核查责任人" min-width="170" prop="checkChargerUserId">
            <template slot-scope="scope">
              <el-select v-model="scope.row.checkChargerUserId" placeholder="请选择" size="small" style="width: 100%"
                @change="setCheckChargerUser(scope.row.checkChargerUserId, scope.row)">
                <el-option v-for="item in userList" :key="item.value" :label="item.label"
                  :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="170" prop="remark">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="80" align="center">
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
      <el-table ref="multipleTable" :data="equipOptions" tooltip-effect="dark" height="500" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="设备名称" prop="label"></el-table-column>
        <el-table-column prop="value" label="设备编号"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addEquipDia = false">取 消</el-button>
        <el-button :loading="submitFormLoading" type="primary" @click="changeMachineName">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Sortable from "sortablejs";
import {
  getDeviceExaminePlan,
  addDeviceExaminePlan,
  updateDeviceExaminePlan,
  deviceScopeSearch,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/performance/class";
export default {
  name: "addVerificationYearPlanDia",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      applicationForm: false,
      addEquipDia: false,
      submitFormLoading: false,
      equipOptions: [],
      examinePlanDetailsList: [],
      selectionRows: [],
      userList: [],
      form: {
        planName: '',
        planYear: '',
        planId: '',
        examinePlanDetailsList: [],
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
          examinePlanDetailsList: [],
        }
        this.examinePlanDetailsList = []
      } else {
        this.getRowInfo(row)
      }
      this.applicationForm = true
      this.getUserList()
    },
    getRowInfo(row) {
      this.yearTableLoading = true
      getDeviceExaminePlan({ planId: row.planId }).then(res => {
        this.yearTableLoading = false
        if (res.code == 200) {
          this.examinePlanDetailsList = res.data.examinePlanDetailsList
          this.form.planName = res.data.planName
          this.form.planYear = res.data.planYear
          this.form.planId = res.data.planId
        }
      }).catch(error => {
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
    setCheckChargerUser(id, row) {
      const index = this.userList.findIndex(item => item.id === id)
      if (index > -1) {
        row.checkChargerUser = this.userList[index].name
      }
    },
    changeMachineName() {
      this.selectionRows.map(val => {
        this.examinePlanDetailsList.push({ deviceId: val.id, deviceName: val.label, deviceNumber: val.value, specificationModel: val.specificationModel })
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
            const oldRow = that.examinePlanDetailsList[dragged.rowIndex] //旧位置数据
            const newRow = that.examinePlanDetailsList[related.rowIndex] //被拖拽的新数据
          },
          //拖拽结束事件
          onEnd: evt => {
            const curRow = that.examinePlanDetailsList.splice(evt.oldIndex, 1)[0];
            that.examinePlanDetailsList.splice(evt.newIndex, 0, curRow);
          }
        })
      }
    },
    // 删除表格行
    deleteRow(index) {
      this.examinePlanDetailsList.splice(index, 1)
    },
    // 提交新增
    submitForm() {
      this.form.examinePlanDetailsList = this.HaveJson(this.examinePlanDetailsList)
      this.submitFormLoading = true
      if (this.operationType === 'add') {
        addDeviceExaminePlan(this.form).then(res => {
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
        updateDeviceExaminePlan(this.form).then(res => {
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
    getUserList() {
      selectUserCondition().then(res => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.userList = data
      })
    },
  },
}
</script>

<style scoped></style>
