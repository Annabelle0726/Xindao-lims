<template>
  <!--  设备校准计划-->
  <div>
    <div>
      <div style="margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
        <div class="title">
          <span style="font-weight: bold">年度计划表</span>
        </div>
        <div>
          <el-button size="small" type="primary" @click="getYearTableData">刷新</el-button>
          <el-button size="small" type="primary" @click="addYearPlan('add')">新增</el-button>
          <!--          <el-button size="small" type="primary" @click="record">导入</el-button>-->
        </div>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDataLoading"
                  :data="yearTableData" height="calc(60vh - 16em)"
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
            <el-form-item label="设备名称及型号" prop="deviceName">
              <el-input v-model="searchForm.deviceName" clearable
                        @keyup.enter.native="getYearTableDetailData(currentRow)" size="small"></el-input>
            </el-form-item>
            <el-form-item label="仪器编号" prop="deviceNumber">
              <el-input v-model="searchForm.deviceNumber"
                        @keyup.enter.native="getYearTableDetailData(currentRow)" clearable size="small"></el-input>
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
          <el-table-column label="设备名称及型号" min-width="180" prop="deviceName" show-overflow-tooltip></el-table-column>
          <el-table-column label="设备数量" min-width="180" prop="deviceAmount"></el-table-column>
          <el-table-column label="仪器编号" min-width="150" prop="deviceNumber"></el-table-column>
          <el-table-column label="检定单位" min-width="150" prop="verificationUnit"> </el-table-column>
          <el-table-column label="检定周期" min-width="150" prop="verificationCycles"></el-table-column>
          <el-table-column label="最近检定时间" min-width="150" prop="lastDate"></el-table-column>
          <el-table-column label="本年计划校准时间" min-width="150" prop="planDate"></el-table-column>
          <el-table-column label="备注" min-width="150" prop="remark"></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="140">
            <template slot-scope="scope">
              <el-button size="small" type="text" @click="handleForm('edit', scope.row)">编辑</el-button>
              <el-button size="small" type="text" @click="handleForm('view', scope.row)">查看</el-button>
              <el-button size="small" style="color: #f56c6c" type="text"
                @click="deleteFun1(scope.row.planDetailId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
                       background :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper"
                       @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible" title="设备校准计划"
      width="80%" @close="resetForm">
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="150px">
        <el-col :span="12">
          <el-form-item label="设备名称及型号：" prop="deviceName">
            <el-input v-model="form.deviceName" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备数量：" prop="deviceAmount">
            <el-input v-model="form.deviceAmount" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仪器编号：" prop="deviceNumber">
            <el-input v-model="form.deviceNumber" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检定单位：" prop="verificationUnit">
            <el-input v-model="form.verificationUnit" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检定周期：" prop="verificationCycles">
            <el-input v-model="form.verificationCycles" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最近检定时间：" prop="lastDate">
            <el-date-picker v-model="form.lastDate" :disabled="operationType === 'view'" clearable format="yyyy-MM-dd"
              placeholder="选择日期" size="small" style="width:100%" type="date" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本年计划校准时间：" prop="planDate">
            <el-date-picker v-model="form.planDate" :disabled="operationType === 'view'" clearable format="yyyy-MM-dd"
              placeholder="选择日期" size="small" style="width:100%" type="date" value-format="yyyy-MM-dd">
            </el-date-picker>
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
    <el-dialog :visible.sync="notificationDia" title="提交批准" width="30%" @close="closeNotificationDia">
      <span style="margin-top: 10px;display: inline-block">
        请选择批准人：
        <el-select v-model="ratifyUserId" clearable filterable size="small" style="width: 90%;">
          <el-option v-for="item in responsibleOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="notificationLoading" @click="closeNotificationDia">取 消</el-button>
        <el-button :loading="notificationLoading" type="primary" @click="notification">提 交</el-button>
      </span>
    </el-dialog>
    <!--上传报告-->
    <el-dialog :visible.sync="uploadDia" title="数据导入" width="500px">
      年份：
      <el-date-picker v-model="planYear" type="year" value-format="yyyy" clearable format="yyyy" placeholder="选择年">
      </el-date-picker>
      <div style="margin: 0 auto;">
        <el-upload ref="upload1" :action="action" :auto-upload="false" :file-list="fileList" :headers="uploadHeader"
          :limit="1" accept='.doc,.docx' :on-change="beforeUpload" :on-error="onError" :on-success="handleSuccessUp"
          drag name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitUpload()">上 传</el-button>
      </span>
    </el-dialog>
    <add-year-plan-dia v-if="addYearPlanDia" ref="addYearPlanDia" @closeDia="closeDia"></add-year-plan-dia>
  </div>
</template>

<script>
import AddYearPlanDia from "./addYearPlanDia.vue";
import AddVerificationYearPlanDia from "./addVerificationYearPlanDia.vue";
import {
  pageDeviceCalibrationPlan,
  submiatRatifyDeviceCalibrationPlan,
  exportDeviceCalibrationPlanDetail,
  delDeviceCalibrationPlan,
  delDeviceCalibrationPlanDetail,
  pageDeviceCalibrationPlanDetail,
  addDeviceCalibrationPlanDetail,
  updateDeviceCalibrationPlanDetail,
  ratifyDeviceCalibrationPlan,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/performance/class";
import { mapGetters } from "vuex";
export default {
  name: "equipment-calibration-plan",
  // import 引入的组件需要注入到对象中才能使用
  components: { AddVerificationYearPlanDia, AddYearPlanDia },
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
        deviceName: '',
        deviceAmount: '',
        deviceNumber: '',
        verificationUnit: '',
        verificationCycles: '',
        lastDate: '',
        planDate: '',
        remark: '',
        planId: '',
        planDetailId: '',
      },
      operationType: '',
      rules: {
        deviceNumber: [{ required: true, message: '请填写仪器编号', trigger: 'blur' }],
        planDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
      },
      yearTableDataLoading: false,
      yearTableDetailDataLoading: false,
      currentRow: {},
      approvalDialog: false,
      approvalLoading: false,
      submitFormLoading: false,
      ratifyRemark: '',
      currentRowId: '',
      uploadDia: false,
      uploading: false,
      addYearPlanDia: false,
      fileList: [],
      planYear: '',
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
      pageDeviceCalibrationPlan({
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
    // 打开提交批准弹框
    tellApprove(planId) {
      this.getUserList()
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
      submiatRatifyDeviceCalibrationPlan({
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
      exportDeviceCalibrationPlanDetail({ planId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '设备校准计划.docx')
      })
    },
    // 年度计划表-删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDeviceCalibrationPlan({ planId: id }).then(res => {
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
        delDeviceCalibrationPlanDetail({ planDetailsId: id }).then(res => {
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
      pageDeviceCalibrationPlanDetail({
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
      this.operationType = type
      this.dialogVisible = true
      this.form.planId = this.currentRow.planId
      if (this.operationType !== 'add') {
        this.$nextTick(() => {
          this.form = this.HaveJson(row)
        })
      } else {
        this.form = {
          deviceName: '',
          deviceAmount: '',
          deviceNumber: '',
          verificationUnit: '',
          verificationCycles: '',
          lastDate: '',
          planDate: '',
          remark: '',
          planId: this.currentRow.planId,
          planDetailId: '',
        }
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.modelForm.validate(valid => {
        if (valid) {
          if (this.operationType === 'add') {
            this.submitFormLoading = true
            addDeviceCalibrationPlanDetail(this.form).then(res => {
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
            updateDeviceCalibrationPlanDetail(this.form).then(res => {
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
    submitUpload() {
      if (!this.planYear) {
        this.$message.warning('请选择年份')
        return;
      }
      if (this.$refs.upload1.uploadFiles.length === 0) {
        this.$message.error('未选择文件')
        return
      }
      this.uploading = true
      this.$refs.upload1.submit();
    },
    // 提交批准
    handleApproval(status) {
      const personTrainingUpdateDto = {
        planId: this.currentRowId,
        ratifyRemark: this.ratifyRemark,
        ratifyStatus: status
      }
      this.approvalLoading = true
      ratifyDeviceCalibrationPlan(personTrainingUpdateDto).then(res => {
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
    // 打开报告弹框
    record(row) {
      this.uploadDia = true
    },
    // 导入流程
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response) {
      this.uploading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.getYearTableData()
      } else {
        this.$message.error('上传失败')
      }
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          let data = [];
          res.data.forEach((a) => {
            data.push({
              label: a.name,
              value: a.id,
            });
          });
          this.responsibleOptions = data
        }
      });
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/deviceCalibrationPlan/importDeviceCalibrationPlan' + '?planYear=' + this.planYear
    },
    ...mapGetters(["userId"]),
  },
}
</script>

<style scoped>
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
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
