<!-- 设备故障 -->
<template>
  <div>
    <div class="search">
      <div class="search_thing">
        <div class="search_label">流程编号：</div>
        <div>
          <el-input size="small" placeholder="请输入" clearable v-model="search.processNumber" style="width: 100%;"
            @keyup.enter.native="getTableList(clickNodeVal.value)">
          </el-input>
        </div>
      </div>
      <div class="search_thing" style="padding-left: 30px;">
        <el-button size="mini" type="primary" @click="getTableList(clickNodeVal.value)">查询</el-button>
        <el-button size="mini" @click="resetSearch">重置</el-button>
      </div>
      <div class="btns">
        <el-button size="small" type="primary" @click="dialogVisible = true, addRecode()">新建</el-button>
        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading">导出</el-button>
      </div>
    </div>
    <div>
      <div class="tables" style="margin-top: 16px;">
        <el-table :data="faultParam" style="width: 100%" height="calc(100vh - 20em)"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <!-- 表格列 -->
          <el-table-column header-align="center" align="center" width="70" prop="prop" label="序号"
            type="index"></el-table-column>
          <el-table-column prop="processNumber" label="流程编号" min-width="180"></el-table-column>
          <el-table-column prop="deviceName" label="设备名称" min-width="150"></el-table-column>
          <el-table-column prop="managementNumber" label="统一编号" min-width="150"></el-table-column>
          <el-table-column prop="faultSituation" label="故障情况" min-width="130"></el-table-column>
          <el-table-column prop="submitPerson" label="提交人" min-width="130"></el-table-column>
          <el-table-column prop="createTime" label="提交日期" min-width="150"></el-table-column>
          <el-table-column prop="currentState" label="当前状态" min-width="130"></el-table-column>
          <el-table-column prop="currentResponsible" label="当前责任人" width="180"></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column label="操作" fixed="right" min-width="150">
            <template slot-scope="scope">
              <el-button size="small" type="text" @click="showDetailsDialog(scope.row)">查看详情</el-button>
              <el-button type="text" size="small" @click="handleDeleteClick(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
          style="margin-right: 5%;" :page-sizes="[10, 20, 30, 50, 100]" :page-size="search.size"
          layout="->,total, sizes, prev, pager, next, jumper" :total="search.total">
        </el-pagination>
      </div>
    </div>
    <el-dialog title="新增维修记录" top="5vh" :visible.sync="dialogVisible" width="60%" @open="openRecordAcceptance"
      :close-on-click-modal="false" :close-on-press-escape="false">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step v-for="(v, i) in steps" style="cursor:pointer" :title="v" :key="i"
          @click.native="choiceStep(i)"></el-step>
      </el-steps>
      <el-form ref="form" :model="form" label-position="right" label-width="120px">
        <!-- 提交 -->
        <div v-show="currentStepClick == 0">
          <el-card style="margin-top: 1em;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="流程编号:">
                  <el-input v-model="form.processNumber" size="small" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备名称:">
                  <el-input v-model="form.deviceName" size="small" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="规格型号:">
                  <el-input v-model="form.specificationModel" size="small" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备编号:">
                  <el-input v-model="form.managementNumber" size="small" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="准确度量值:" prop="measureOfAccuracy"
                  :rules="[{ required: currentStep === 0, message: '请输入准确度量值', trigger: 'blur' }]">
                  <el-input v-model="form.measureOfAccuracy" size="small" :disabled="currentStep !== 0"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="故障日期:" prop="faultDate">
                  <el-date-picker v-model="form.faultDate" type="date" placeholder="选择日期" size="small"
                    format="yyyy-MM-dd" style="width: 100%" value-format="yyyy-MM-dd" :disabled="currentStep !== 0">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="要求修复日期:" prop="requestRepairDate">
                  <el-date-picker v-model="form.requestRepairDate" :picker-options="{ disabledDate: this.disabledDate }"
                    type="date" placeholder="选择日期" size="small" format="yyyy-MM-dd" style="width: 100%"
                    value-format="yyyy-MM-dd" :disabled="currentStep !== 0">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="故障情况:" prop="faultSituation"
                  :rules="[{ required: currentStep === 0, message: '请输入故障情况', trigger: 'blur' }]">
                  <el-input v-model="form.faultSituation" type="textarea" size="small"
                    :disabled="currentStep !== 0"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="相关附件:">
                  <el-input v-model="form.fileName" :disabled="true" size="small" style="width: 80%;">
                    <el-button slot="append" icon="el-icon-delete-solid" @click="deleteFile"
                      :disabled="currentStep !== 0"></el-button>
                  </el-input>
                  <el-upload ref="upload" style="float: right;" :action="action" :show-file-list="false"
                    :on-success="handleSuccess">
                    <el-button style="position: relative;top: -4px;" class="uploadFile" slot="trigger" size="small"
                      type="primary" :disabled="currentStep !== 0">附件上传
                    </el-button>
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下环节负责人:" prop="submitNextPesponsible"
                  :rules="[{ required: currentStep === 0, message: '请输入下环节负责人', trigger: 'change' }]">
                  <el-select v-model="form.submitNextPesponsible" placeholder="请选择下环节负责人" size="small"
                    style="width: 100%" :disabled="currentStep !== 0">
                    <el-option v-for="(v, i) in users" :label="v.name" :value="v.name" :key="i"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.submitOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.submitDate }}
            </el-col>
          </el-row>
        </div>
        <!-- 设备管理员意见 -->
        <div v-show="currentStepClick == 1">
          <el-card style="margin-top: 1em; height: 45vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="维修方式及费用:" prop="methodCost"
                  :rules="[{ required: currentStep === 1, message: '请输入维修方式及费用', trigger: 'blur' }]">
                  <el-input v-model="form.methodCost" type="textarea" size="small"
                    :disabled="currentStep !== 1"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="审核意见:" prop="adminAuditOption"
                  :rules="[{ required: currentStep === 1, message: '请输入审核意见', trigger: 'blur' }]">
                  <el-input v-model="form.adminAuditOption" type="textarea" size="small"
                    :disabled="currentStep !== 1"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下环节负责人:" prop="adminNextPesponsible"
                  :rules="[{ required: currentStep === 1, message: '请输入下环节负责人', trigger: 'change' }]">
                  <el-select v-model="form.adminNextPesponsible" placeholder="请选择下环节负责人" size="small"
                    style="width: 100%" :disabled="currentStep !== 1">
                    <el-option v-for="(v, i) in users" :label="v.name" :value="v.name" :key="i"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.adminOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.adminDate }}
            </el-col>
          </el-row>
        </div>
        <!-- 技术负责人意见 -->
        <div v-show="currentStepClick == 2">
          <el-card style="margin-top: 1em; height: 45vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="审核意见:" prop="technicalAuditOption"
                  :rules="[{ required: currentStep === 2, message: '请输入审核意见', trigger: 'blur' }]">
                  <el-input v-model="form.technicalAuditOption" type="textarea" size="small"
                    :disabled="currentStep !== 2"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下环节负责人:" prop="technicalNextPesponsible"
                  :rules="[{ required: currentStep === 2, message: '请输入下环节负责人', trigger: 'change' }]">
                  <el-select v-model="form.technicalNextPesponsible" placeholder="请选择下环节负责人" size="small"
                    style="width: 100%" :disabled="currentStep !== 2">
                    <el-option v-for="(v, i) in users" :label="v.name" :value="v.name" :key="i"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.technicalOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.technicalDate }}
            </el-col>
          </el-row>
        </div>
        <!-- 维修情况记录 -->
        <div v-show="currentStepClick == 3">
          <el-card style="margin-top: 1em; height: 45vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="维修情况:" prop="maintainSituation"
                  :rules="[{ required: currentStep === 3, message: '请输入维修情况', trigger: 'blur' }]">
                  <el-input v-model="form.maintainSituation" type="textarea" size="small"
                    :disabled="currentStep !== 3"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下环节负责人:" prop="maintainNextPesponsible"
                  :rules="[{ required: currentStep === 3, message: '请输入下环节负责人', trigger: 'change' }]">
                  <el-select v-model="form.maintainNextPesponsible" placeholder="请选择下环节负责人" size="small"
                    style="width: 100%" :disabled="currentStep !== 3">
                    <el-option v-for="(v, i) in users" :label="v.name" :value="v.name" :key="i"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.maintainOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.maintainDate }}
            </el-col>
          </el-row>
        </div>
        <!-- 维修后的确认 -->
        <div v-show="currentStepClick == 4">
          <el-card style="margin-top: 1em; height: 45vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="验收、确认、校准情况:" prop="checkCalSituation" label-width="200"
                  :rules="[{ required: currentStep === 4, message: '请输入验收、确认、校准情况', trigger: 'blur' }]">
                  <el-input v-model="form.checkCalSituation" type="textarea" size="small" style="width: 100%"
                    :disabled="currentStep !== 4"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.afterMaintenanceOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.afterMaintenanceDate }}
            </el-col>
          </el-row>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="submitForm('3reject')" v-if="currentStep !== 0 && currentStep !== 5"
          :loading="sumbitLoading1">驳回</el-button>
        <el-button @click="submitForm('2save')" v-if="currentStep === 0" :loading="sumbitLoading2">保存</el-button>
        <el-button type="primary" v-if="currentStep !== 5" @click="submitForm('1submit')" :loading="sumbitLoading3">
          {{ currentStep === 0 ? '提交' : '通过' }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { dateFormat } from '@/utils/date'
import { mapGetters } from "vuex";
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0,
        processNumber: ''
      },
      users: [],
      currentStep: 0,
      currentStepClick: 0,
      steps: ['提交', '设备管理员意见', '技术负责人意见', '维修情况记录', '维修后的确认'],
      outLoading: false,
      form: {},
      faultParam: [],
      dialogVisible: false,
      sumbitLoading1: false,
      sumbitLoading2: false,
      sumbitLoading3: false,
    }
  },
  computed: {
    ...mapGetters(["nickName"]),
    action() {
      return this.javaApi + this.$api.personnel.saveCNASFile
    }
  },
  mounted() {
    this.getTableList(this.clickNodeVal.value)
  },
  methods: {
    //要求修复日期:禁用在故障日期前的日期
    disabledDate(time) {
      let selectDate = this.form.faultDate
      if (selectDate) {
        let selectTime = new Date(selectDate).getTime() - 28800000
        return time < selectTime
      }
      return false
    },
    //提交表单
    async submitForm(saveState) {
      let currentStepAction;
      // 设置该操作判断是否为提交，保存，驳回，通过 开启loding
      switch (saveState) {
        // 提交，通过
        case '1submit':
          currentStepAction = this.currentStep + 1
          this.sumbitLoading1 = true
          break
        // 保存
        case '2save':
          currentStepAction = this.currentStep
          this.sumbitLoading2 = true
          break
        // 驳回
        case '3reject':
          currentStepAction = this.currentStep - 1
          this.sumbitLoading3 = true
          break
        default:
          break
      }
      this.$refs.form.validate((valid) => {
        if (valid === true || saveState !== '1submit') {
          // 给当前环节设置创建人与时间
          const dateTime = dateFormat(new Date())
          // 获取当前环节操作人与日期
          switch (this.currentStep) {
            case 0:
              this.form.submitUser = this.nickName
              this.form.submitOperatingPersonnel = this.nickName
              this.form.submitDate = dateTime
              break
            case 1:
              this.form.adminOperatingPersonnel = this.nickName
              this.form.adminDate = dateTime
              break
            case 2:
              this.form.technicalOperatingPersonnel = this.nickName
              this.form.technicalDate = dateTime
              break
            case 3:
              this.form.maintainOperatingPersonnel = this.nickName
              this.form.maintainDate = dateTime
              break
            case 4:
              this.form.afterMaintenanceOperatingPersonnel = this.nickName
              this.form.afterMaintenanceDate = dateTime
              break
            default:
              break
          }
          // 获取当前环节负责人
          switch (saveState === '3reject' ? this.currentStep - 1 : this.currentStep) {
            case 0:
              this.form.currentResponsible = this.form.submitOperatingPersonnel
              break
            case 1:
              this.form.currentResponsible = this.form.departmentOperatingPersonnel
              break
            case 2:
              this.form.currentResponsible = this.form.measuringRoomOperatingPersonnel
              break
            case 3:
              this.form.currentResponsible = this.form.approvalOperatingPersonnel
              break
            default:
              break
          }
          // 获取当前状态
          this.form.currentState = currentStepAction === 5 ? '关闭' : this.steps[currentStepAction]
          this.form.deviceId = this.clickNodeVal.value
          this.$axios.post(this.$api.deviceCheck.addOrUpdateDeviceFaultOne, this.form, {
            headers: {
              'Content-Type': 'application/json'
            }
          }).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功')
              this.getTableList(this.clickNodeVal.value)
              this.dialogVisible = false
            }
            this.closeLoading()
          })
        } else {
          let step = this.steps[this.currentStep]
          this.$message.warning(step + '  流程中有必填项未填！');
          this.closeLoading()
        }
      });
    },
    closeLoading() {
      this.sumbitLoading1 = false
      this.sumbitLoading2 = false
      this.sumbitLoading3 = false
    },
    openRecordAcceptance() {
      // 获取设备基础信息
      this.$axios.get(this.$api.deviceScope.selectDeviceByCode + '?id=' + this.clickNodeVal.value).then(res => {
        this.form.deviceName = res.data.deviceName
        this.form.manufacturer = res.data.manufacturer
        this.form.specificationModel = res.data.specificationModel
        this.form.managementNumber = res.data.managementNumber
      });
      // 获取负责人信息
      this.getUserList()
    },
    // 获取负责人信息接口
    getUserList() {
      this.$axios.get(this.$api.deviceScope.selectUserList).then(res => {
        if (res.code == 200) {
          this.users = res.data
        }
      })
    },
    handleSizeChange(val) {
      this.search.size = val
      this.getTableList(this.clickNodeVal.value);
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getTableList(this.clickNodeVal.value);
    },
    deleteFile() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(this.$api.personnel.deleteCNASFile + "?fileName=" + this.form.systemFileName).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功！')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    choiceStep(index) {
      this.currentStepClick = index
    },
    //文档
    handleSuccess(response, file, fileList) {
      if (response.code == 200) {
        this.form.fileName = response.data
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message)
      }
    },
    onError(error, file, fileList) {
      // 文件上传失败时的处理逻辑
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    resetSearch() {
      this.search = {
        size: 20,
        current: 1,
        total: 0,
        processNumber: '',
      }
      this.getTableList(this.clickNodeVal.value);
    },
    //获取所有信息
    getTableList(deviceId) {
      this.$axios.get(this.$api.deviceCheck.deviceFaultOnePage + '?deviceId=' + deviceId + "&size=" + this.search.size + "&current=" + this.search.current + "&processNumber=" + this.search.processNumber).then(res => {
        if (res.code == 200) {
          this.faultParam = res.data.records
          this.search.total = res.data.total
        }
      })
    },
    //导出
    handleDown() {
      this.outLoading = true
      this.$axios.post(this.$api.deviceCheck.exportRewardAndPunishmentRecords, {
        deviceId: this.clickNodeVal.value
      }, { responseType: "blob" }).then(res => {
        this.outLoading = false
        this.$message.success('导出成功')
        const blob = new Blob([res], { type: 'application/octet-stream' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '设备故障表.xlsx';
        link.click();
      }).finally(() => {
        this.outLoading = false
      })
    },
    selectAllByOne(row) {
      this.dialogVisible = true;
      this.formData = this.HaveJson(row);
    },
    //添加记录按钮
    addRecode() {
      this.currentStep = 0;
      this.currentStepClick = 0;
    },
    //查看详情
    showDetailsDialog(row) {
      this.form = { ...row }
      this.currentStep = this.steps.indexOf(this.form.currentState) === -1 ? 5 : this.steps.indexOf(this.form.currentState)
      this.currentStepClick = this.currentStep === 5 ? 0 : this.currentStep
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
      this.dialogVisible = true
    },
    //删除操作
    handleDeleteClick(index, row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(this.$api.deviceCheck.deleteDeviceFaultOne + '?id=' + row.id).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功')
            this.getTableList(this.clickNodeVal.value)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getTableList(newVal.value);
      }
    },
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {}
        this.closeLoading()
        this.currentStep = 0 // 步骤条显示第几步
        this.currentStepClick = 0 // 点击步骤条变化
        this.$refs['form'].clearValidate()
      }
    }
  }
}
</script>

<style scoped>
h4 {
  font-weight: 400;
  font-size: 16px;
  /* display: flex;
  justify-content: space-between; */
  margin: 10px 0;
}

/*
保存提交按钮样式
*/
h4 .title {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

h4 .title .line {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: #3A7BFA;
  margin-right: 4px;
}

.tables {
  width: 100%;
  height: calc(100vh - 230px);
}

.el-from {
  max-width: 400px;
  margin: 0 auto;
}

.form-row {
  display: flex;
  justify-content: space-between;
}

.dialogBottom {
  margin-top: 14px;
  display: flex;
  justify-content: space-between;
}

.form-operation {
  display: flex;
  justify-content: center;
}

.form-item {
  display: flex;
  margin-right: 100px;
}

.label {
  margin-right: 20px;
  line-height: 40px;
}

.value {
  font-weight: normal;
  line-height: 40px;
}

.form-row .el-form-item {
  flex: 1;
  /* 每个表单项占据相同比例的空间 */
  margin-right: 100px;
  /* 可选：为右侧元素添加间距 */
}

.search {
  background-color: #fff;
  height: 40px;
  display: flex;
  align-items: center;
  position: relative;
}

.search_thing {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 40px;
}

.search_label {
  width: 70px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 120px);
}

.btns {
  position: absolute;
  right: 0px;
  top: 50%;
  transform: translate(0, -50%);
}

.form .search_label {
  width: 120px;
}
</style>
