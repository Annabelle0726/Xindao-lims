<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="仪器设备事故报告单" width="80%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="填写报告单" @click.native="setStep(0)"></el-step>
        <el-step title="事故损失情况评估" @click.native="setStep(1)"></el-step>
        <el-step title="部门负责人意见" @click.native="setStep(2)"></el-step>
        <el-step title="技术负责人意见" @click.native="setStep(3)"></el-step>
        <el-step title="主任意见" @click.native="setStep(4)"></el-step>
      </el-steps>
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="160px">
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="地点：" prop="address">
            <el-input v-model="form.address" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="日期：" prop="accidentDate">
            <el-date-picker v-model="form.accidentDate" :disabled="currentStep !== 0" format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期" size="small" style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
          <el-form-item label="事故情况描述：" prop="descriptionOfAccident">
            <el-input v-model="form.descriptionOfAccident" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
          <el-form-item label="请选择评估人：" prop="assessorUserId">
            <el-select v-model="form.assessorUserId" :disabled="currentStep !== 0" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 1" :span="24">
          <el-form-item label="事故损失情况评估：" prop="assessorOpinion">
            <el-input v-model="form.assessorOpinion" :disabled="currentStep !== 1" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 1" :span="24">
          <el-form-item label="请选择部门负责人：" prop="departmentHeadUserId">
            <el-select v-model="form.departmentHeadUserId" :disabled="currentStep !== 1" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="部门负责人意见：" prop="departmentHeadOpinion">
            <el-input v-model="form.departmentHeadOpinion" :disabled="currentStep !== 2" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="请选择技术负责人：" prop="technicalDirectorUserId">
            <el-select v-model="form.technicalDirectorUserId" :disabled="currentStep !== 2" placeholder="请选择"
              size="small" style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 3" :span="24">
          <el-form-item label="技术负责人意见：" prop="technicalDirectorOpinion">
            <el-input v-model="form.technicalDirectorOpinion" :disabled="currentStep !== 3" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 3" :span="24">
          <el-form-item label="请选择主任：" prop="directorHeadUserId">
            <el-select v-model="form.directorHeadUserId" :disabled="currentStep !== 3" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 4" :span="24">
          <el-form-item label="主任意见：" prop="directorHeadOpinion">
            <el-input v-model="form.directorHeadOpinion" :disabled="currentStep !== 4" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="currentStep !== 5" :loading="submitFormLoading" type="primary" @click="submitForm">确
          认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { selectUserCondition } from "@/api/business/inspectionTask";
import {
  getDeviceAccidentReport,
  addDeviceAccidentReport,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  name: "accident-form",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      form: {
        accidentReportId: '',
        address: '',
        accidentDate: '',
        descriptionOfAccident: '',
        assessorUserId: '',
        assessorOpinion: '',
        departmentHeadUserId: '',
        departmentHeadOpinion: '',
        technicalDirectorUserId: '',
        technicalDirectorOpinion: '',
        directorHeadUserId: '',
        directorHeadOpinion: '',
        flowType: '',
        deviceId: '',
      },
      currentStep: 0,
      showStep: 0,
      rules: {
        assessorUserId: [{ required: true, message: '请选择评估人', trigger: 'change' }],
        departmentHeadUserId: [{ required: true, message: '请选择部门负责人', trigger: 'change' }],
        technicalDirectorUserId: [{ required: true, message: '请选择技术负责人', trigger: 'change' }],
        directorHeadUserId: [{ required: true, message: '请选择主任', trigger: 'change' }],
        directorHeadOpinion: [{ required: true, message: '请填写意见', trigger: 'blur' }],
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
      this.form.accidentReportId = id
      this.form.deviceId = deviceId
      this.getUserList()
      if (this.form.accidentReportId) {
        this.searchInfo()
      }
    },
    // 查询详情
    searchInfo() {
      getDeviceAccidentReport({ accidentReportId: this.form.accidentReportId }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
          if (res.data.isFinish === 0) {
            if (this.form.assessorUserId) {
              this.currentStep = 1
              this.showStep = 1
            }
            if (this.form.departmentHeadUserId) {
              this.currentStep = 2
              this.showStep = 2
            }
            if (this.form.technicalDirectorUserId) {
              this.currentStep = 3
              this.showStep = 3
            }
            if (this.form.directorHeadUserId) {
              this.currentStep = 4
              this.showStep = 4
            }
          } else {
            this.currentStep = 5
            this.showStep = 4
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
          addDeviceAccidentReport(this.form).then(res => {
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
