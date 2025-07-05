<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="仪器设备验收单" width="80%" @close="resetForm">
      <el-form ref="modelForm" :model="form" :rules="rules" label-width="180px">
        <el-col :span="12">
          <el-form-item label="到货日期：" prop="arrivalDate">
            <el-date-picker v-model="form.arrivalDate" type="date" placeholder="选择日期" size="small" format="yyyy-MM-dd"
              style="width: 100%" value-format="yyyy-MM-dd" :disabled="operationType === 'view'">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额：" prop="goldAmount">
            <el-input v-model="form.goldAmount" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维修单位：" prop="maintenanceunit">
            <el-input v-model="form.maintenanceunit" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收设备主机和备件情况：" prop="spareParts">
            <el-input v-model="form.spareParts" :disabled="operationType === 'view'" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="安装、调试情况：" prop="installationDebugging">
            <el-input v-model="form.installationDebugging" :disabled="operationType === 'view'" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="验收情况：" prop="checkSituation">
            <el-input v-model="form.checkSituation" :disabled="operationType === 'view'" clearable size="small"
              type="textarea"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接收签字：" prop="receivingSignature">
            <el-input v-model="form.receivingSignature" :disabled="operationType === 'view'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="厂家代表：" prop="producer">
            <el-input v-model="form.producer" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接收人：" prop="recipient">
            <el-input v-model="form.recipient" :disabled="operationType === 'view'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接收日期：" prop="recipientDate">
            <el-date-picker v-model="form.recipientDate" type="date" placeholder="选择日期" size="small" format="yyyy-MM-dd"
              style="width: 100%" value-format="yyyy-MM-dd" :disabled="operationType === 'view'">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="operationType !== 'view'" :loading="submitFormLoading" type="primary" @click="submitForm">确
          认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeviceAcceptance,
  addDeviceAcceptance,
  updateDeviceAcceptance,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
  name: "acceptance-form",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      form: {
        arrivalDate: '',
        goldAmount: '',
        maintenanceunit: '',
        spareParts: '',
        installationDebugging: '',
        checkSituation: '',
        receivingSignature: '',
        producer: '',
        recipient: '',
        recipientDate: '',
        deviceId: '',
      },
      operationType: '',
      rules: {
        arrivalDate: [{ required: true, message: '请选择到货日期', trigger: 'change' }],
      },
      userList: [],
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDialog(type, id, deviceId) {
      this.dialogVisible = true
      this.operationType = type
      this.form.acceptanceId = id
      this.form.deviceId = deviceId
      this.getUserList()
      if (this.form.acceptanceId) {
        this.searchInfo()
      }
    },
    // 查询详情
    searchInfo() {
      getDeviceAcceptance({ acceptanceId: this.form.acceptanceId }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
        }
      }).catch(error => {
        console.error(error)
      })
    },
    // 提交表单
    submitForm() {
      this.$refs.modelForm.validate((valid) => {
        if (valid) {
          if (this.operationType === 'add') {
            addDeviceAcceptance(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('新增成功')
                this.resetForm()
              }
              this.submitFormLoading = false
            }).catch(err => {
              this.submitFormLoading = false
            })
          } else {
            updateDeviceAcceptance(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('修改成功')
                this.resetForm()
              }
              this.submitFormLoading = false
            }).catch(err => {
              this.submitFormLoading = false
            })
          }
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

<style scoped>
.form-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
</style>
