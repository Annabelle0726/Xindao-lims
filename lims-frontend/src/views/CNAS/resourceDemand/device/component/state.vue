<!-- 设备停用启用 -->
<template>
  <div>
    <div class="search">
      <el-form :model="search" ref="search" size="small" :inline="true">
        <el-form-item label="流程编号">
          <el-input v-model="search.processNumber" clearable placeholder="请输入" size="small" style="width: 100%"
                    @keyup.enter.native="getDeviceStatePage(clickNodeVal.value)"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="getDeviceStatePage(clickNodeVal.value)">查询</el-button>
          <el-button size="mini" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        <el-button size="small" type="primary" @click="dialogVisible = true">新建</el-button>
        <el-button :loading="outLoading" size="small" type="primary" @click="handleDown">导出Excel</el-button>
      </div>
    </div>
    <div>
      <el-table :data="tableDatalist" height="calc(100vh - 20em)" style="width: 100% ;"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <!-- 表格列 -->
        <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
          width="70"></el-table-column>
        <el-table-column label="流程编号" min-width="180" prop="processNumber"></el-table-column>
        <el-table-column label="设备名称" min-width="180" prop="deviceName"></el-table-column>
        <el-table-column label="管理编号" min-width="150" prop="managementNumber"></el-table-column>
        <el-table-column label="设备状态" min-width="130" prop="deviceStatus">
          <template #default="{ row }">
            {{ row.deviceStatus === '0scrap' ? '报废' : row.deviceStatus === '1startUsing' ? '启用' : '停用' }}
          </template>
        </el-table-column>
        <el-table-column label="停用/启用理由" min-width="180" prop="reason"></el-table-column>
        <el-table-column label="提交人" min-width="130" prop="createUser"></el-table-column>
        <el-table-column label="提交日期" min-width="150" prop="createTime"></el-table-column>
        <el-table-column label="当前状态" min-width="130" prop="currentState"></el-table-column>
        <el-table-column label="当前责任人" min-width="180" prop="currentResponsible"></el-table-column>
        <!-- 操作按钮 -->
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button size="small" type="text" @click="handleViewClick(row)">查看</el-button>
            <el-button size="small" type="text" @click="handleDownOne(row)">导出</el-button>
            <el-button size="small" type="text" @click="handleDeleteClick(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" background>
      </el-pagination>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible" title="设备情况"
      top="5vh" width="60%" @open="openRecordAcceptance">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step v-for="(v, i) in steps" :key="i" :title="v" style="cursor:pointer"
          @click.native="choiceStep(i)"></el-step>
      </el-steps>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <div v-show="currentStepClick === 0">
          <el-card style="margin-top: 1em; height: 42vh; overflow-y: scroll;">
            <!-- 卡片 -->
            <el-row>
              <el-col :span="24">
                <el-form-item label="流程编号：">
                  <el-input v-model="form.processNumber" disabled size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="管理编号：">
                  <el-input v-model="form.managementNumber" disabled size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备名称：">
                  <el-input v-model="form.deviceName" disabled size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="规格型号：">
                  <el-input v-model="form.specificationModel" disabled size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 0, message: '请输入配件', trigger: 'blur' }]" label="配件："
                  prop="accessoryPart">
                  <el-input v-model="form.accessoryPart" :disabled="currentStep !== 0" size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 0, message: '请选择设备状态', trigger: 'change' }]"
                  label="设备情况：" prop="deviceStatus">
                  <el-select v-model="form.deviceStatus" :disabled="currentStep !== 0" placeholder="请选择" size="small"
                    style="width:100%">
                    <el-option label="报废" value="0scrap"></el-option>
                    <el-option label="启用" value="1startUsing"></el-option>
                    <el-option label="停用" value="2stopUsing"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 0, message: '请输入停用启用理由', trigger: 'blur' }]"
                  label="停用启用理由：" prop="reason">
                  <el-input v-model="form.reason" :disabled="currentStep !== 0" :rows="3" size="small"
                    type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 0, message: '请选择下环节责任人', trigger: 'change' }]"
                  label="下环节责任人：" prop="submitNextPesponsible">
                  <el-select v-model="form.submitNextPesponsible" :disabled="currentStep !== 0" clearable filterable
                    placeholder="请选择下环节负责人" size="small" style="width: 100%">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
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
        <div v-show="currentStepClick === 1">
          <el-card style="margin-top: 1em; height: 42vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item label="部门负责人意见：" prop="departmentReviewOpinion">
                  <el-input v-model="form.departmentReviewOpinion" :disabled="currentStep !== 1" size="small"
                    type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 1, message: '请输入下环节责任人', trigger: 'change' }]"
                  label="下环节责任人：" prop="departmentNextPesponsible">
                  <el-select v-model="form.departmentNextPesponsible" :disabled="currentStep !== 1" clearable filterable
                    placeholder="请选择下环节负责人" size="small" style="width: 100%;">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.departmentOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.departmentDate }}
            </el-col>
          </el-row>
        </div>
        <div v-show="currentStepClick === 2">
          <el-card style="margin-top: 1em; height: 42vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item :rules="[{ required: currentStep === 2, message: '请输入计量室意见', trigger: 'blur' }]"
                  label="计量室意见：" prop="measuringRoomReviewOpinion">
                  <el-input v-model="form.measuringRoomReviewOpinion" :disabled="currentStep !== 2" size="small"
                    type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 2, message: '请输入下环节责任人', trigger: 'blur' }]"
                  label="下环节责任人：" prop="measuringRoomNextPesponsible">
                  <el-select v-model="form.measuringRoomNextPesponsible" :disabled="currentStep !== 2" clearable
                    filterable placeholder="请选择下环节负责人" size="small" style="width: 100%;">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.measuringRoomOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.measuringRoomDate }}
            </el-col>
          </el-row>
        </div>
        <div v-show="currentStepClick === 3">
          <el-card style="margin-top: 1em; height: 42vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="24">
                <el-form-item :rules="[{ required: currentStep === 3, message: '请输入批准意见', trigger: 'blur' }]"
                  label="批准意见：" prop="approvalOpinion">
                  <el-input v-model="form.approvalOpinion" :disabled="currentStep !== 3" size="small"
                    type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :rules="[{ required: currentStep === 3, message: '请输入下环节负责人', trigger: 'change' }]"
                  label="下环节责任人：" prop="approvalNextPesponsible">
                  <el-select v-model="form.approvalNextPesponsible" :disabled="currentStep !== 3" clearable filterable
                    placeholder="请选择下环节负责人" size="small" style="width: 100%;">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.approvalOperatingPersonnel }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.approvalDate }}
            </el-col>
          </el-row>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="currentStep !== 0 && currentStep !== 4" :loading="sumbitLoading1"
          @click="submitForm('3reject')">驳回</el-button>
        <el-button v-if="currentStep === 0" :loading="sumbitLoading2" @click="submitForm('2save')">保存</el-button>
        <el-button v-if="currentStep !== 4" :loading="sumbitLoading3" type="primary" @click="submitForm('1submit')">
          {{ currentStep === 0 ? '提交' : '通过' }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  saveDeviceState,
  selectDeviceByCode,
  exportDeviceStatus,
  deleteDeviceState,
  deviceStateExport,
  getDeviceStatePage,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
import { dateFormat } from '@/utils/date'
import { mapGetters } from "vuex";
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  computed: {
    ...mapGetters(["nickName"]),
  },
  data() {
    return {
      outLoading: false, // 导出loading
      dialogVisible: false,
      form: {},
      currentStep: 0, // 步骤条显示第几步
      currentStepClick: 0, // 点击步骤条变化
      steps: ['提交', '部门负责人', '计量室', '批准人'],
      responsibleOptions: [], // 下环节负责人
      tableDatalist: [], // table表格
      rules: {
        quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
      },
      search: {
        size: 20,
        current: 1,
        total: 0,
        processNumber: ''
      },
      sumbitLoading1: false,
      sumbitLoading2: false,
      sumbitLoading3: false,
    }
  },
  mounted() {
    this.getDeviceStatePage(this.clickNodeVal.value)
  },
  methods: {
    // 分页
    handleSizeChange(val) {
      this.search.size = val
      this.getDeviceStatePage(this.clickNodeVal.value)
    },
    // 分页
    handleCurrentChange(val) {
      this.search.current = val
      this.getDeviceStatePage(this.clickNodeVal.value)
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
              this.form.createUser = this.nickName
              this.form.submitOperatingPersonnel = this.nickName
              this.form.submitDate = dateTime
              break
            case 1:
              this.form.departmentOperatingPersonnel = this.nickName
              this.form.departmentDate = dateTime
              break
            case 2:
              this.form.measuringRoomOperatingPersonnel = this.nickName
              this.form.measuringRoomDate = dateTime
              break
            case 3:
              this.form.approvalOperatingPersonnel = this.nickName
              this.form.approvalDate = dateTime
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
          this.form.currentState = currentStepAction === 4 ? '关闭' : this.steps[currentStepAction]
          this.form.deviceId = this.clickNodeVal.value
          saveDeviceState(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功')
              this.getDeviceStatePage(this.clickNodeVal.value)
              this.dialogVisible = false
              this.closeLoading()
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
    choiceStep(index) {
      this.currentStepClick = index
    },
    openRecordAcceptance() {
      // 获取设备基础信息
      selectDeviceByCode({ id: this.clickNodeVal.value }).then(res => {
        this.form.deviceName = res.data.deviceName
        this.form.specificationModel = res.data.specificationModel
        this.form.managementNumber = res.data.managementNumber
        this.$nextTick(() => {
          this.$refs['form'].clearValidate()
        })
      });
      // 获取负责人信息
      this.getUserList()
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
    // 查看
    handleViewClick(row) {
      this.form = { ...row }
      // 如果索引为6表示全部通过
      this.currentStep = this.steps.indexOf(this.form.currentState) === -1 ? 4 : this.steps.indexOf(this.form.currentState)
      this.currentStepClick = this.currentStep === 4 ? 0 : this.currentStep
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
      this.dialogVisible = true
    },
    // 导出
    handleDownOne(row) {
      this.outLoading = true
      exportDeviceStatus({ deviceId: row.deviceId, processNumber: row.processNumber }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备停/启用.doc')
      })
    },
    // 删除
    handleDeleteClick(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDeviceState({ stateId: row.stateId }).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功')
            this.getDeviceStatePage(this.clickNodeVal.value)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //导出
    handleDown() {
      this.outLoading = true
      deviceStateExport({
        deviceId: this.clickNodeVal.value,
        processNumber: this.search.processNumber
      }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备停用/启用.xlsx')
      }).finally(() => {
        this.outLoading = false
      })
    },
    resetSearch() {
      this.search = {
        size: 20,
        current: 1,
        total: 0,
        processNumber: '',
      }
      this.getDeviceStatePage(this.clickNodeVal.value);
    },
    getDeviceStatePage(deviceId) {
      getDeviceStatePage({ deviceId, ...this.search }).then(res => {
        if (res.code == 200) {
          this.tableDatalist = res.data.records
          this.search.total = res.data.total
        }
      })
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.componentData.entity.deviceId = this.clickNodeVal.value
        this.entityCopy = this.HaveJson(this.componentData.entity)
        this.getDeviceStatePage(newVal.value)
      }
    },
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {
        }
        this.closeLoading()
        this.currentStep = 0 // 步骤条显示第几步
        this.currentStepClick = 0 // 点击步骤条变化
        this.$refs['form'].clearValidate()
      }
    }
  },
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
