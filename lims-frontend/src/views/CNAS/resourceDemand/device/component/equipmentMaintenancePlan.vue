<template>
  <div>
    <div>
      <div style="margin: 10px 0;text-align: right">
        <el-button size="small" type="primary" @click="getYearTableDetailData">刷新</el-button>
        <el-button size="small" type="primary" @click="handleForm('add')">新增</el-button>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDetailDataLoading" :data="yearTableDetailData"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          height="calc(100vh - 18em)" style="width: 100% ;">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="年份" min-width="80" prop="planYear"></el-table-column>
          <el-table-column label="编制人" min-width="120" prop="compiler"></el-table-column>
          <el-table-column label="编制时间" min-width="160" prop="datePreparation"></el-table-column>
          <el-table-column label="审核人" min-width="150" prop="audit"></el-table-column>
          <el-table-column label="审核时间" min-width="180" prop="auditDate"></el-table-column>
          <el-table-column label="审核状态" min-width="180" prop="status">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 1" type="success">通 过</el-tag>
              <el-tag v-if="scope.row.status === 0" type="danger">不通过</el-tag>
            </template>
          </el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="240">
            <template slot-scope="scope">
              <el-button :disabled="scope.row.status === 1" size="small" type="text"
                @click="handleForm('edit', scope.row)">编辑</el-button>
              <el-button :disabled="scope.row.status === 1" size="small" type="text"
                @click="tellApprove(scope.row.maintenancePlanId)">审核通知</el-button>
              <el-button :disabled="scope.row.status === 1 || userId != scope.row.auditId" size="small" type="text"
                @click="handleForm('check', scope.row)">审核</el-button>
              <el-button size="small" type="text" @click="handleDownOne(scope.row.maintenancePlanId)">导出</el-button>
              <el-button :disabled="scope.row.status === 1" size="small" style="color: #f56c6c" type="text"
                @click="deleteFun(scope.row.maintenancePlanId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
          :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper" background
          @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="applicationForm"
      title="设备保养计划表" width="80%" @close="closeDialog">
      <div style="display: flex;align-items: center;">
        <el-button v-if="operationType !== 'check'" size="small" type="primary" @click="addTableRow">添加</el-button>
        <span style="width: 60px;margin-left: 10px">年份：</span>
        <el-date-picker v-model="form.planYear" type="year" value-format="yyyy" clearable size="small" format="yyyy"
          placeholder="选择年">
        </el-date-picker>
      </div>
      <div style="margin: 10px 0">
        <el-table ref="yearTable" :data="deviceMaintenancePlanDetails" id="templateParamTable" row-key="deviceId"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          height="300px" style="width: 100% ;">
          <el-table-column align="center" header-align="center" label="序号" type="index" width="60"></el-table-column>
          <el-table-column label="设备名称" min-width="170" prop="deviceName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceName" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="设备编号" min-width="140" prop="deviceNumber">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceNumber" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="归属实验室" min-width="120" prop="storagePoint">
            <template slot-scope="scope">
              <el-input v-model="scope.row.storagePoint" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="保养关键部位" min-width="120" prop="maintenanceSite">
            <template slot-scope="scope">
              <el-input v-model="scope.row.maintenanceSite" :disabled="operationType === 'check'" clearable size="small"
                type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="保养内容" min-width="120" prop="maintenanceContent">
            <template slot-scope="scope">
              <el-input v-model="scope.row.maintenanceContent" :disabled="operationType === 'check'" clearable
                size="small" type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="保养周期" min-width="90" prop="maintenanceIntervals">
            <template slot-scope="scope">
              <el-input v-model="scope.row.maintenanceIntervals" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="80" align="center" v-if="operationType !== 'check'">
            <template slot-scope="scope">
              <el-button style="color: #f56c6c" type="text" @click="deleteRow(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button v-if="operationType !== 'check'" :loading="submitFormLoading" type="primary" @click="submitForm">确
          认</el-button>
        <el-button v-if="operationType === 'check'" :loading="submitFormLoading" type="primary"
          @click="checkStatus(0)">不通过</el-button>
        <el-button v-if="operationType === 'check'" :loading="submitFormLoading" type="primary"
          @click="checkStatus(1)">通
          过</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="addEquipDia" title="添加设备"
      width="50%">
      <el-table ref="multipleTable" :data="equipOptions" tooltip-effect="dark" height="500" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="设备名称" prop="label" width="190"></el-table-column>
        <el-table-column prop="value" label="设备编号" width="130"></el-table-column>
        <el-table-column prop="managementNumber" label="归属实验室"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addEquipDia = false">取 消</el-button>
        <el-button v-if="operationType !== 'check'" :loading="submitFormLoading" type="primary"
          @click="changeMachineName">确
          认</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="notificationDia" title="提交审核" width="30%" @close="closeNotificationDia">
      <span style="margin-top: 10px;display: inline-block">
        请选择审核人：
        <el-select v-model="auditId" clearable filterable size="small" style="width: 90%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="notificationLoading" @click="closeNotificationDia">取 消</el-button>
        <el-button :loading="notificationLoading" type="primary" @click="notification">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Sortable from "sortablejs";
import {
  selectDeviceMaintenancePlanByPage,
  submitReviewMaintenancePlanStatus,
  getMaintenancePlanDetail,
  addMaintenancePlan,
  updateMaintenancePlan,
  exportDeviceMaintenancePlan,
  deleteMaintenancePlan,
  deviceScopeSearch,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
import { mapGetters } from "vuex";
export default {
  name: "equipment-maintenance-plan",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    // 这里存放数据
    return {
      pagination1: {
        size: 10,
        current: 1,
        total: 0,
      },
      yearTableDetailDataLoading: false,
      yearTableDetailData: [],
      deviceMaintenancePlanDetails: [],
      selectionRows: [],
      deviceIds: [],
      applicationForm: false,
      submitFormLoading: false,
      operationType: '',
      equipOptions: [],
      form: {
        planYear: '',
        deviceMaintenancePlanDetails: []
      },
      addEquipDia: false,
      responsibleOptions: [],
      notificationDia: false,
      notificationLoading: false,
      auditId: '',
      maintenancePlanId: ''
    }
  },
  mounted() {
    this.getYearTableDetailData()
  },
  // 方法集合
  methods: {
    // 查询
    getYearTableDetailData() {
      this.yearTableDetailDataLoading = true
      selectDeviceMaintenancePlanByPage({
        current: this.pagination1.current,
        size: this.pagination1.size,
      }).then(res => {
        if (res.code == 200) {
          this.yearTableDetailData = res.data.records
          this.pagination1.total = res.data.total
        }
        this.yearTableDetailDataLoading = false
      }).catch(err => {
        this.yearTableDetailDataLoading = false
      })
    },
    // 打开提交批准弹框
    tellApprove(maintenancePlanId) {
      this.getUserList()
      this.notificationDia = true
      this.maintenancePlanId = maintenancePlanId
    },
    // 关闭提交批准弹框
    closeNotificationDia() {
      this.notificationDia = false
      this.auditId = ''
    },
    // 提交批准通知
    notification() {
      if (!this.auditId) {
        this.$message.warning('请选择审核人')
        return
      }
      this.notificationLoading = true
      submitReviewMaintenancePlanStatus({
        auditId: this.auditId,
        maintenancePlanId: this.maintenancePlanId,
      }).then(res => {
        this.notificationLoading = false
        if (res.code == 200) {
          this.closeNotificationDia()
          this.getYearTableDetailData()
        }
      }).catch(err => {
        this.notificationLoading = false
      })
    },
    // 打开新增和编辑弹框
    handleForm(type, row) {
      this.operationType = type
      this.applicationForm = true
      this.form = {
        planYear: '',
        deviceMaintenancePlanDetails: [],
      }
      this.deviceMaintenancePlanDetails = []
      if (row) {
        this.maintenancePlanId = row.maintenancePlanId
        getMaintenancePlanDetail({ maintenancePlanId: this.maintenancePlanId }).then(res => {
          if (res.code === 200) {
            this.form = res.data
            this.deviceMaintenancePlanDetails = this.form.deviceMaintenancePlanDetails
          }
        }).catch(error => {
          console.error(error)
        })
      }
      this.getEquipOptions()
    },
    // 添加设备
    addTableRow() {
      this.addEquipDia = true
      this.getEquipOptions()
    },
    handleSelectionChange(selection) {
      this.selectionRows = selection
    },
    // 赋值仪器编号
    changeMachineName() {
      this.deviceMaintenancePlanDetails = []
      this.selectionRows.map(val => {
        this.deviceMaintenancePlanDetails.push({ deviceId: val.id, deviceName: val.label, deviceNumber: val.value, storagePoint: val.storagePoint })
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
            const oldRow = that.deviceMaintenancePlanDetails[dragged.rowIndex] //旧位置数据
            const newRow = that.deviceMaintenancePlanDetails[related.rowIndex] //被拖拽的新数据
          },
          //拖拽结束事件
          onEnd: evt => {
            const curRow = that.deviceMaintenancePlanDetails.splice(evt.oldIndex, 1)[0];
            that.deviceMaintenancePlanDetails.splice(evt.newIndex, 0, curRow);
          }
        })
      }
    },
    // 删除表格行
    deleteRow(index) {
      this.deviceMaintenancePlanDetails.splice(index, 1)
    },
    // 提交新增和修改
    submitForm() {
      this.form.deviceMaintenancePlanDetails = this.HaveJson(this.deviceMaintenancePlanDetails)
      this.submitFormLoading = true
      if (this.operationType === 'add') {
        addMaintenancePlan(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('新增成功')
            this.applicationForm = false
            this.getYearTableDetailData()
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      } else {
        updateMaintenancePlan(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('修改成功')
            this.applicationForm = false
            this.getYearTableDetailData()
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      }
    },
    // 提交审核
    checkStatus(status) {
      const params = {
        status: status,
        maintenancePlanId: this.maintenancePlanId
      }
      addMaintenancePlan(params).then(res => {
        if (res.code == 200) {
          this.$message.success('审核成功')
          this.applicationForm = false
          this.getYearTableDetailData()
        }
        this.submitFormLoading = false
      }).catch(err => {
        this.submitFormLoading = false
      })
    },
    closeDialog() {
      this.applicationForm = false
      this.getYearTableDetailData()
    },
    // 分页
    handleSizeChange1(val) {
      this.pagination1.size = val
      this.getYearTableDetailData()
    },
    // 分页
    handleCurrentChange1(val) {
      this.pagination1.current = val
      this.getYearTableDetailData()
    },
    handleDownOne(id) {
      this.outLoading = true
      exportDeviceMaintenancePlan({ maintenancePlanId: id }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备维护保养记录.doc')
      })
    },
    // 删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMaintenancePlan({ maintenancePlanId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableDetailData();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
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
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      });
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getYearTableDetailData();
      }
    },
  },
  computed: {
    ...mapGetters(["userId"]),
  },
}
</script>

<style scoped>
.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-search {
  display: flex;
  align-items: center;
  margin: 10px 0;
}
</style>
