<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="利用外部仪器设备申请表" width="80%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="填写申请表" @click.native="setStep(0)"></el-step>
        <el-step title="申请部门负责人意见" @click.native="setStep(1)"></el-step>
        <el-step title="计量室意见" @click.native="setStep(2)"></el-step>
        <el-step title="批准人" @click.native="setStep(3)"></el-step>
      </el-steps>
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="160px">
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="单位名称：" prop="unitName">
            <el-input v-model="form.unitName" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="地址：" prop="address">
            <el-input v-model="form.address" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="仪器名称：" prop="deviceName">
            <el-input v-model="form.deviceName" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="型号：" prop="deviceModel">
            <el-input v-model="form.deviceModel" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="配件：" prop="parts">
            <el-input v-model="form.parts" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="12">
          <el-form-item label="对方仪器编号：" prop="instrumentNumber">
            <el-input v-model="form.instrumentNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
          <el-form-item label="仪器技术指标：" prop="technicalIndex">
            <el-input v-model="form.technicalIndex" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
          <el-form-item label="需求技术要求：" prop="technicalRequirements">
            <el-input v-model="form.technicalRequirements" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
          <el-form-item label="利用原因：" prop="useReason">
            <el-input v-model="form.useReason" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 0" :span="24">
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
        <el-col v-if="showStep === 1" :span="24">
          <el-form-item label="计量室负责人：" prop="meteringRoomUserId">
            <el-select v-model="form.meteringRoomUserId" :disabled="currentStep !== 1" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="计量室意见：" prop="meteringRoomOpinion">
            <el-input v-model="form.meteringRoomOpinion" :disabled="currentStep !== 2" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 2" :span="24">
          <el-form-item label="批准人：" prop="approverUserId">
            <el-select v-model="form.approverUserId" :disabled="currentStep !== 2" placeholder="请选择" size="small"
              style="width: 50%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="showStep === 3" :span="24">
          <el-form-item label="批准人意见：" prop="approverOpinion">
            <el-input v-model="form.approverOpinion" :disabled="currentStep !== 3" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="currentStep !== 4" :loading="submitFormLoading" type="primary" @click="submitForm">确
          认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeviceExternalApply,
  addDeviceExternalApply,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
  name: "applicationForm",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      form: {
        externalApplyId: '',
        unitName: '',
        address: '',
        deviceName: '',
        deviceModel: '',
        parts: '',
        instrumentNumber: '',
        technicalIndex: '',
        technicalRequirements: '',
        useReason: '',
        departmentHeadUserId: '',
        departmentHeadOpinion: '',
        meteringRoomUserId: '',
        meteringRoomOpinion: '',
        approverUserId: '',
        approverOpinion: '',
        flowType: ''
      },
      currentStep: 0,
      showStep: 0,
      rules: {
        departmentHeadUserId: [{ required: true, message: '请选择申请部门负责人', trigger: 'change' }],
        meteringRoomUserId: [{ required: true, message: '请选择计量室负责人', trigger: 'change' }],
        approverUserId: [{ required: true, message: '请选择批准人', trigger: 'change' }],
      },
      userList: [],
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDialog(id) {
      this.dialogVisible = true
      this.form.externalApplyId = id
      this.getUserList()
      if (this.form.externalApplyId) {
        this.searchInfo()
      }
    },
    // 查询详情
    searchInfo() {
      getDeviceExternalApply({ externalApplyId: this.form.externalApplyId }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
          if (res.data.isFinish === 0) {
            if (this.form.departmentHeadUserId) {
              this.currentStep = 1
              this.showStep = 1
            }
            if (this.form.meteringRoomUserId) {
              this.currentStep = 2
              this.showStep = 2
            }
            if (this.form.approverUserId) {
              this.currentStep = 3
              this.showStep = 3
            }
          } else {
            this.currentStep = 4
            this.showStep = 3
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
          addDeviceExternalApply(this.form).then(res => {
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
