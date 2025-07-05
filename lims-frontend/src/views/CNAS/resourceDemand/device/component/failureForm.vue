<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="仪器设备修理申请表" width="80%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="填写申请表" @click.native="setStep(0)"></el-step>
        <el-step title="申请部门负责人意见" @click.native="setStep(1)"></el-step>
        <el-step title="维修记录" @click.native="setStep(2)"></el-step>
      </el-steps>
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="160px">
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="安装地点：" prop="location">
            <el-input v-model="form.location" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="损坏或故障情况：" prop="damageOrMalfunction">
            <el-input v-model="form.damageOrMalfunction" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="要求修复日期：" prop="repairDate">
            <el-date-picker v-model="form.repairDate" :disabled="currentStep !== 0" format="yyyy-MM-dd"
              placeholder="选择日期" size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0 && currentStep !== 0" :span="12">
          <el-form-item label="申请人：" prop="applicantUser">
            <el-input v-model="form.applicantUser" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="申请部门负责人：" prop="departmentHeadUserId">
            <el-select v-model="form.departmentHeadUserId" :disabled="currentStep !== 0" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 1" :span="24">
          <el-form-item label="申请部门负责人意见：" prop="departmentHeadOpinion">
            <el-input v-model="form.departmentHeadOpinion" :disabled="currentStep !== 1" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="维修记事：" prop="maintenanceRecord">
            <el-input v-model="form.maintenanceRecord" :disabled="currentStep !== 2" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="维修人：" prop="maintenanceUser">
            <el-input v-model="form.maintenanceUser" :disabled="currentStep !== 2" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="currentStep !== 3" :loading="submitFormLoading" type="primary" @click="submitForm">确
          认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeviceBreakdownMaintenance,
  addDeviceBreakdownMaintenance,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
  name: "failure-form",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      form: {
        location: '',
        damageOrMalfunction: '',
        repairDate: '',
        departmentHeadUserId: '',
        departmentHeadOpinion: '',
        maintenanceRecord: '',
        maintenanceUser: '',
        applicantUser: '',
        flowType: '',
        deviceId: '',
        isFinish: '',
      },
      currentStep: 0,
      showStep: 0,
      rules: {
        departmentHeadUserId: [{ required: true, message: '请选择申请部门负责人', trigger: 'change' }],
        departmentHeadOpinion: [{ required: true, message: '请填写意见', trigger: 'blur' }],
      },
      userList: [],
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDialog(id, deviceId) {
      this.dialogVisible = true
      this.form.maintenanceId = id
      this.form.deviceId = deviceId
      this.getUserList()
      if (this.form.maintenanceId) {
        this.searchInfo()
      }
    },
    // 查询详情
    searchInfo() {
      getDeviceBreakdownMaintenance({
        maintenanceId: this.form.maintenanceId
      }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
          if (this.form.isFinish === 0) {
            if (this.form.departmentHeadUserId) {
              this.currentStep = 1
              this.showStep = 1
            }
            if (this.form.departmentHeadOpinion) {
              this.currentStep = 2
              this.showStep = 2
            }
          } else {
            this.currentStep = 3
            this.showStep = 2
          }
        }
      }).catch(error => {
        console.error(error)
      })
    },
    setStep(step) {
      this.showStep = step
    },
    // 提交表单
    submitForm() {
      this.form.flowType = this.currentStep
      this.$refs.modelForm.validate((valid) => {
        if (valid) {
          addDeviceBreakdownMaintenance(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('新增成功')
              this.resetForm()
            }
            this.submitFormLoading = false
          }).catch(err => {
            this.submitFormLoading = false
          })
        }
      })
    },
    // 关闭弹框
    resetForm() {
      this.dialogVisible = false
      this.$emit('closeDialog')
    },
    getUserList() {
      selectUserCondition().then(res => {
        this.userList = res.data
      })
    },
  },
}
</script>

<style scoped>
.form-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
</style>
