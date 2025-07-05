<template>
  <!--  设备核查计划-->
  <div>
    <div>
      <div style="margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
        <div class="title">
          <span style="font-weight: bold">年度计划表</span>
        </div>
        <div style="display: flex;align-items: center;">
          <el-button size="small" type="primary" @click="getYearTableData">刷新</el-button>
          <el-button size="small" type="primary" @click="addYearPlan('add')">新增</el-button>
          <!--          <el-upload ref="upload" :action="action"-->
          <!--                     :before-upload="beforeUpload" :headers="uploadHeader" :on-error="onError"-->
          <!--                     :on-success="handleSuccessUp" :show-file-list="false" accept='.xls,.xlsx'>-->
          <!--            <el-button :loading="upLoading" size="small" type="primary">导入</el-button>-->
          <!--          </el-upload>-->
        </div>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDataLoading" :data="yearTableData"
                  height="calc(60vh - 16em)"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                  highlight-current-row @current-change="currentChange">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="年份" min-width="80" prop="planYear"></el-table-column>
          <el-table-column label="计划名称" min-width="180" prop="planName" show-overflow-tooltip></el-table-column>
          <el-table-column label="编制人" min-width="100" prop="writeName"></el-table-column>
          <el-table-column label="编制日期" min-width="160" prop="writeTime"></el-table-column>
          <el-table-column label="批准状态" min-width="100" prop="ratifyStatus">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.ratifyStatus === 1" type="success">批准</el-tag>
              <el-tag v-if="scope.row.ratifyStatus === 0" type="danger">不批准</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="批准内容" min-width="150" prop="ratifyRemark"></el-table-column>
          <el-table-column label="批准人" min-width="100" prop="ratifyName"></el-table-column>
          <el-table-column label="批准时间" min-width="150" prop="ratifyTime"></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="240">
            <template slot-scope="scope">
              <el-button :disabled="scope.row.ratifyStatus === 1" size="small" type="text"
                @click="addYearPlan('edit', scope.row)">编辑</el-button>
              <el-button :disabled="scope.row.ratifyStatus === 1" size="small" type="text"
                @click="tellApprove(scope.row.planId)">批准通知</el-button>
              <el-button :disabled="scope.row.ratifyStatus === 1 || userId != scope.row.ratifyUserId" size="small"
                type="text" @click="handleApprove(scope.row.planId)">批准</el-button>
              <el-button size="small" type="text" @click="downLoadPost(scope.row.planId)">导出</el-button>
              <el-button :disabled="scope.row.ratifyStatus === 1" size="small" style="color: #f56c6c" type="text"
                @click="deleteFun(scope.row.planId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination.size" :page-sizes="[10, 20, 30, 50, 100]"
                       background :total="pagination.total" layout="->,total, sizes, prev, pager, next, jumper"
                       @size-change="handleSizeChange" @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </div>
    <div>
      <div class="title">
        <span style="font-weight: bold">年度计划明细表</span>
      </div>
      <div class="search">
        <div>
          <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
            <el-form-item label="设备编号" prop="deviceNumber">
              <el-input v-model="searchForm.deviceNumber" clearable size="small"
                        @keyup.enter.native="getYearTableDetailData(currentRow)"></el-input>
            </el-form-item>
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="searchForm.deviceName" clearable size="small"
                        @keyup.enter.native="getYearTableDetailData(currentRow)"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="getYearTableDetailData(currentRow)">查询</el-button>
              <el-button size="mini" @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <el-button size="small" type="primary" @click="handleForm('add')">新增</el-button>
        </div>
      </div>
      <div>
        <el-table ref="yearTableDetailData" v-loading="yearTableDetailDataLoading" :data="yearTableDetailData"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                  :height="'calc(100vh - 34em)'" style="width: 100% ;">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="设备编号" min-width="180" prop="deviceNumber"></el-table-column>
          <el-table-column label="设备名称" min-width="180" prop="deviceName" show-overflow-tooltip></el-table-column>
          <el-table-column label="核查时间" min-width="150" prop="checkTime"></el-table-column>
          <el-table-column label="核查指标" min-width="150" prop="checkIndex"> </el-table-column>
          <el-table-column label="核查方法" min-width="150" prop="checkMethod"></el-table-column>
          <el-table-column label="结果如何判定" min-width="150" prop="howResults"></el-table-column>
          <el-table-column label="核查负责人" min-width="150" prop="checkChargerUser"></el-table-column>
          <el-table-column label="记录状态" min-width="100" prop="recordStatus">
            <template slot-scope="scope">
              <el-tag type="" v-if="scope.row.recordStatus === 0" disable-transitions>未开始</el-tag>
              <el-tag type="warning" v-if="scope.row.recordStatus === 1" disable-transitions>待批准</el-tag>
              <el-tag type="success" v-if="scope.row.recordStatus === 2" disable-transitions>通过</el-tag>
              <el-tag type="danger" v-if="scope.row.recordStatus === 3" disable-transitions>不通过</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="对比状态" min-width="100" prop="recordContrastStatus">
            <template slot-scope="scope">
              <el-tag type="" v-if="scope.row.recordContrastStatus === 0" disable-transitions>未开始</el-tag>
              <el-tag type="warning" v-if="scope.row.recordContrastStatus === 1" disable-transitions>待批准</el-tag>
              <el-tag type="success" v-if="scope.row.recordContrastStatus === 2" disable-transitions>通过</el-tag>
              <el-tag type="danger" v-if="scope.row.recordContrastStatus === 3" disable-transitions>不通过</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="150" prop="remark"></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="260">
            <template slot-scope="scope">
              <el-button size="small" type="text" @click="handleForm('edit', scope.row)">编辑</el-button>
              <el-button size="small" type="text" @click="handleForm('view', scope.row)">查看</el-button>
              <el-button size="small" type="text" @click="handleRecord(scope.row)">记录</el-button>
              <el-button size="small" type="text" @click="handleCheck(scope.row)">对比</el-button>
              <el-button size="small" type="text" @click="handleDown(scope.row)">导出</el-button>
              <el-button size="small" style="color: #f56c6c" type="text"
                @click="deleteFun1(scope.row.planDetailsId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
          :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper" background
          @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible" title="设备核查计划"
      width="80%" @close="resetForm">
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="110px">
        <el-col :span="12">
          <el-form-item label="设备编号：" prop="deviceNumber">
            <el-input v-model="form.deviceNumber" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备名称：" prop="deviceName">
            <el-input v-model="form.deviceName" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核查时间：" prop="checkTime">
            <el-date-picker v-model="form.checkTime" :disabled="operationType === 'view'" clearable format="yyyy-MM"
              placeholder="选择日期" size="small" style="width:100%" type="month" value-format="yyyy-MM">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核查指标：" prop="checkIndex">
            <el-input v-model="form.checkIndex" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核查方法：" prop="checkMethod">
            <el-input v-model="form.checkMethod" :disabled="operationType === 'view'" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结果如何判定：" prop="howResults">
            <el-input v-model="form.howResults" :disabled="operationType === 'view'" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核查责任人：" prop="checkChargerUserId">
            <el-select v-model="form.checkChargerUserId" @change="setCheckChargerUser"
              :disabled="operationType === 'view'" placeholder="请选择" size="small" style="width: 100%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注：" prop="remark">
            <el-input v-model="form.remark" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button :loading="submitFormLoading" type="primary" @click="submitForm" v-if="operationType !== 'view'">确
          认</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="approvalDialog" title="批准" width="30%" @close="approvalDialog = false">
      <span>
        批准备注：
        <el-input v-model="ratifyRemark" type="textarea"></el-input>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="approvalLoading" @click="handleApproval(0)">不批准</el-button>
        <el-button :loading="approvalLoading" type="primary" @click="handleApproval(1)">批 准</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="downloadDialog" title="导出" width="600px">
      <span>
        <el-button :loading="outLoading" plain type="primary" @click="controlDown">核查记录</el-button>
        <el-button :loading="outLoading" plain type="primary" @click="processingDown">核查比对记录</el-button>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="downloadDialog = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="notificationDia" title="提交批准" width="30%" @close="closeNotificationDia">
      <span style="margin-top: 10px;display: inline-block">
        请选择批准人：
        <el-select v-model="ratifyUserId" clearable filterable size="small" style="width: 90%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="notificationLoading" @click="closeNotificationDia">取 消</el-button>
        <el-button :loading="notificationLoading" type="primary" @click="notification">提 交</el-button>
      </span>
    </el-dialog>
    <equipment-check v-if="equipmentCheck" ref="equipmentCheck" @closeDialog="closeDialog"></equipment-check>
    <check-record v-if="checkRecord" ref="checkRecord" @closeDialog1="closeDialog1"></check-record>
    <add-verification-year-plan-dia v-if="addYearPlanDia" ref="addYearPlanDia"
      @closeDia="closeDia"></add-verification-year-plan-dia>
  </div>
</template>

<script>
import EquipmentCheck from "./equipmentCheck.vue";
import CheckRecord from "./checkRecord.vue";
import AddVerificationYearPlanDia from "./addVerificationYearPlanDia.vue";
import {
  pageDeviceExaminePlan,
  submitRatifyDeviceExaminePlan,
  exportDeviceExaminePlanDetail,
  delDeviceExaminePlan,
  delDeviceExaminePlanDetail,
  pageDeviceExaminePlanDetail,
  addDeviceExaminePlanDetail,
  updateDeviceExaminePlanDetail,
  ratifyDeviceExaminePlan,
  exportReviewExamineRecordDetail,
  exportReviewExamineRecordContrast,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/performance/class";
import { mapGetters } from "vuex";
export default {
  name: "equipment-verification-plan",
  // import 引入的组件需要注入到对象中才能使用
  components: { AddVerificationYearPlanDia, CheckRecord, EquipmentCheck },
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    // 这里存放数据
    return {
      upLoading: false,
      dialogVisible: false,
      searchForm: {
        deviceName: '',
        deviceNumber: '',
      },
      yearTableData: [],
      yearTableDetailData: [],
      pagination: {
        size: 10,
        current: 1,
        total: 0,
      },
      pagination1: {
        size: 10,
        current: 1,
        total: 0,
      },
      form: {
        deviceNumber: '',
        deviceName: '',
        checkTime: '',
        checkIndex: '',
        checkMethod: '',
        howResults: '',
        checkChargerUserId: '',
        checkChargerUser: '',
        remark: '',
        planId: '',
        planDetailsId: '',
      },
      operationType: '',
      rules: {
        deviceNumber: [{ required: true, message: '请填写设备编号', trigger: 'blur' }],
        checkMethod: [{ required: true, message: '请填写核查方法', trigger: 'blur' }],
      },
      yearTableDataLoading: false,
      yearTableDetailDataLoading: false,
      currentRow: {},
      approvalDialog: false,
      approvalLoading: false,
      submitFormLoading: false,
      ratifyRemark: '',
      currentRowId: '',
      userList: [],
      equipmentCheck: false,
      checkRecord: false,
      addYearPlanDia: false,
      downloadDialog: false,
      outLoading: false,
      downRow: {},
      responsibleOptions: [],
      notificationDia: false,
      notificationLoading: false,
      ratifyUserId: '',
      planId: ''
    }
  },
  mounted() {
    this.getYearTableData()
  },
  // 方法集合
  methods: {
    // 查询年度计划表
    getYearTableData() {
      this.yearTableDataLoading = true
      pageDeviceExaminePlan({
        current: 1,
        size: 99,
      }).then(res => {
        if (res.code == 200) {
          this.yearTableData = res.data.records
          this.pagination.total = res.data.total
          if (this.yearTableData.length > 0) {
            this.currentRow = this.yearTableData[0]
            this.$refs.yearTable.setCurrentRow(this.currentRow)
          }
        }
        this.yearTableDataLoading = false
      }).catch(err => {
        this.yearTableDataLoading = false
      })
    },
    // 分页
    handleSizeChange(val) {
      this.pagination.size = val
      this.getYearTableData()
    },
    // 分页
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getYearTableData()
    },
    // 打开新增弹框
    addYearPlan(type, row) {
      this.addYearPlanDia = true
      this.$nextTick(() => {
        this.$refs.addYearPlanDia.openDialog(type, row)
      })
    },
    closeDia() {
      this.addYearPlanDia = false
      this.getYearTableData()
    },
    // 打开提交批准弹框
    tellApprove(planId) {
      this.getUserList1()
      this.notificationDia = true
      this.planId = planId
    },
    // 关闭提交批准弹框
    closeNotificationDia() {
      this.notificationDia = false
      this.ratifyUserId = ''
    },
    // 提交批准通知
    notification() {
      if (!this.ratifyUserId) {
        this.$message.warning('请选择批准人')
        return
      }
      this.notificationLoading = true
      submitRatifyDeviceExaminePlan({
        ratifyUserId: this.ratifyUserId,
        planId: this.planId,
      }).then(res => {
        this.notificationLoading = false
        if (res.code == 200) {
          this.closeNotificationDia()
          this.getYearTableData()
        }
      }).catch(err => {
        this.notificationLoading = false
      })
    },
    // 年度计划表-批准
    handleApprove(id) {
      this.currentRowId = id
      this.approvalDialog = true
    },
    // 年度计划表-导出
    downLoadPost(planId) {
      exportDeviceExaminePlanDetail({ planId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '仪器设备期间核查计划表.docx')
      })
    },
    // 年度计划表-删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDeviceExaminePlan({ planId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableData();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 年度计划详情表-删除
    deleteFun1(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDeviceExaminePlanDetail({ planDetailsId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableDetailData(this.currentRow)
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    currentChange(row) {
      this.currentRow = row
      this.getYearTableDetailData(this.currentRow)
    },
    getYearTableDetailData(row) {
      this.yearTableDetailDataLoading = true
      pageDeviceExaminePlanDetail({
        current: this.pagination1.current,
        size: this.pagination1.size,
        planId: row.planId,
        deviceName: this.searchForm.deviceName,
        deviceNumber: this.searchForm.deviceNumber,
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
    reset() {
      this.searchForm = {
        deviceName: '',
        deviceNumber: '',
      }
      this.getYearTableDetailData(this.currentRow)
    },
    // 分页
    handleSizeChange1(val) {
      this.pagination1.size = val
      this.getYearTableDetailData(this.currentRow)
    },
    // 分页
    handleCurrentChange1(val) {
      this.pagination1.current = val
      this.getYearTableDetailData(this.currentRow)
    },
    // 打开弹框
    handleForm(type, row) {
      this.getUserList()
      this.operationType = type
      this.dialogVisible = true
      this.form.planId = this.currentRow.planId
      if (this.operationType !== 'add') {
        this.$nextTick(() => {
          this.form = this.HaveJson(row)
        })
      } else {
        this.form = {
          deviceNumber: '',
          deviceName: '',
          checkTime: '',
          checkIndex: '',
          checkMethod: '',
          howResults: '',
          checkChargerUserId: '',
          checkChargerUser: '',
          remark: '',
          planId: this.currentRow.planId,
          planDetailsId: '',
        }
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.modelForm.validate(valid => {
        if (valid) {
          if (this.operationType === 'add') {
            this.submitFormLoading = true
            addDeviceExaminePlanDetail(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('新增成功')
                this.dialogVisible = false
                this.getYearTableData()
              }
              this.submitFormLoading = false
            }).catch(err => {
              this.submitFormLoading = false
            })
          } else {
            this.submitFormLoading = true
            updateDeviceExaminePlanDetail(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('新增成功')
                this.dialogVisible = false
                this.getYearTableData()
              }
              this.submitFormLoading = false
            }).catch(err => {
              this.submitFormLoading = false
            })
          }
        }
      })
    },
    // 提交批准
    handleApproval(status) {
      const personTrainingUpdateDto = {
        planId: this.currentRowId,
        ratifyRemark: this.ratifyRemark,
        ratifyStatus: status
      }
      this.approvalLoading = true
      ratifyDeviceExaminePlan(personTrainingUpdateDto).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.getYearTableData();
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    resetForm() {
      this.$refs.modelForm.resetFields();
      this.dialogVisible = false
    },
    // 打开对比流程弹框
    handleCheck(row) {
      this.equipmentCheck = true
      this.$nextTick(() => {
        this.$refs.equipmentCheck.openDialog(row)
      })
    },
    // 关闭对比弹框
    closeDialog() {
      this.equipmentCheck = false
      this.getYearTableDetailData(this.currentRow)
    },
    // 打开记录弹框
    handleRecord(row) {
      this.checkRecord = true
      this.$nextTick(() => {
        this.$refs.checkRecord.openDialog(row)
      })
    },
    // 关闭记录弹框
    closeDialog1() {
      this.checkRecord = false
      this.getYearTableDetailData(this.currentRow)
    },
    // 打开导出弹框
    handleDown(row) {
      this.downRow = row
      this.downloadDialog = true
    },
    // 核查记录导出
    controlDown() {
      this.outLoading = true
      exportReviewExamineRecordDetail({ planDetailsId: this.downRow.planDetailsId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '仪器设备期间核查记录表.doc')
      })
    },
    // 核查对比导出
    processingDown() {
      this.outLoading = true
      exportReviewExamineRecordContrast({ planDetailsId: this.downRow.planDetailsId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '仪器设备期间核查比对记录表.doc')
      })
    },
    // 导入流程
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        this.upLoading = true;
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.getYearTableData()
      } else {
        this.$message.error(response.message)
      }
    },
    getUserList() {
      selectUserCondition().then(res => {
        this.userList = res.data
      })
    },
    setCheckChargerUser(val) {
      const index = this.userList.findIndex(item => item.id === val)
      if (index > -1) {
        this.form.checkChargerUser = this.userList[index].name
      }
    },
    // 获取负责人信息接口
    getUserList1() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      });
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/deviceExaminePlan/importDeviceExaminePlan'
    },
    ...mapGetters(["userId"]),
  },
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
.title {
  position: relative;
  font-size: 16px;
  color: #333;
  font-weight: 400;
  padding-left: 10px;
  margin-bottom: 10px;
}

.title::before {
  position: absolute;
  left: 0;
  top: 4px;
  content: '';
  width: 4px;
  height: 16px;
  background-color: #3A7BFA;
  border-radius: 2px;
}
</style>
