<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="仪器设备期间核查比对记录表" width="80%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="填写设备核查对比记录"></el-step>
        <el-step title="审核"></el-step>
      </el-steps>
      <el-row>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 110px">仪器名称：</span>
            <el-select v-model="form.deviceName" :disabled="currentStep !== 0" class="table_input" clearable filterable
              placeholder="设备名称" size="small" style="width:200px" @change="(val) => changeMachineName(val)">
              <el-option v-for="item in equipOptions" :key="item.value" :label="item.label" :value="item.label">
                {{ item.label + '-' + item.value }}
              </el-option>
            </el-select>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">仪器编号：</span>
            <el-input v-model="form.deviceNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">精度等级：</span>
            <el-input v-model="form.accuracyGrade" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <div style="margin: 10px">核查使用物质</div>
      <el-row>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">名称：</span>
            <el-input v-model="form.materialName" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">编号：</span>
            <el-input v-model="form.materialNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">精度/不确定度：</span>
            <el-input v-model="form.materialAccuracyUncertainty" :disabled="currentStep !== 0" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">型号：</span>
            <el-input v-model="form.materialModel" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">核查方式：</span>
            <el-input v-model="form.materialCheckMethod" :disabled="currentStep !== 0" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">核查项目：</span>
            <el-input v-model="form.materialCheckItems" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">温度：</span>
            <el-input v-model="form.temperature" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <span style="width: 130px">湿度：</span>
            <el-input v-model="form.humidity" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <el-button size="small" style="margin: 10px 0" type="primary" @click="addRow">添加</el-button>
      <el-table :data="recordDetailList" height="300" style="width: 100%" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column label="测试点" min-width="130" prop="testPoint">
          <template slot-scope="scope">
            <el-input v-model="scope.row.testPoint" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue1" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue1" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue2" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue2" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue3" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue3" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue4" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue4" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue5" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue5" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column min-width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="form.dataValue6" :disabled="currentStep !== 0" size="small" />
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.dataValue6" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <table border="1" cellspacing="10" class="table">
        <tr>
          <td>最大偏差</td>
          <th>
            <el-input v-model="form.maximun1" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.maximun2" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.maximun3" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.maximun4" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.maximun5" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.maximun6" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
        </tr>
        <tr>
          <td>相对偏差</td>
          <th>
            <el-input v-model="form.relative1" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.relative2" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.relative3" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.relative4" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.relative5" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="form.relative6" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </th>
        </tr>
      </table>
      <el-row>
        <el-col :span="24">
          <div class="form-item">
            <span style="width: 80px">判定：</span>
            <el-input v-model="form.determine" :disabled="currentStep !== 0" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="form-item">
            <span style="width: 80px">备注：</span>
            <el-input v-model="form.remark" :disabled="currentStep !== 0" clearable size="small"
              type="textarea"></el-input>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="form-item">
            <span style="width: 116px">审核人：</span>
            <el-select v-model="form.reviewUserId" :disabled="currentStep !== 0" placeholder="请选择" size="small"
              style="width: 100%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="currentStep === 0 && checkChargerUserId == userId" :loading="submitFormLoading" type="primary" @click="submitCheck">确
          定</el-button>
        <el-button v-if="currentStep === 1 && userId == reviewUserId" :loading="submitFormLoading" @click="examine(0)">不通过</el-button>
        <el-button v-if="currentStep === 1 && userId == reviewUserId" :loading="submitFormLoading" type="primary" @click="examine(1)">通
          过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getExamineRecord,
  addExamineRecord,
  reviewExamineRecord,
  deviceScopeSearch,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/performance/class";
import {mapGetters} from "vuex";
export default {
  name: "check-record",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      recordDetailList: [],
      search: "",
      form: {
        deviceName: '',
        deviceNumber: '',
        accuracyGrade: '',
        materialName: '',
        materialNumber: '',
        materialAccuracyUncertainty: '',
        materialModel: '',
        materialCheckMethod: '',
        materialCheckItems: '',
        temperature: '',
        humidity: '',
        recordDetailList: [],
        dataValue1: '',
        dataValue2: '',
        dataValue3: '',
        dataValue4: '',
        dataValue5: '',
        dataValue6: '',
        maximun1: '',
        maximun2: '',
        maximun3: '',
        maximun4: '',
        maximun5: '',
        maximun6: '',
        relative1: '',
        relative2: '',
        relative3: '',
        relative4: '',
        relative5: '',
        relative6: '',
        determine: '',
        remark: '',
        planDetailsId: '',
        reviewUserId: '',
      },
      currentStep: 0,
      distributionDetailList: [
        { label: '最大偏差', value: 1 },
        { label: '相对偏差', value: 2 },
      ],
      equipOptions: [],
      submitFormLoading: false,
      userList: [],
      reviewUserId: '',
      checkChargerUserId: '',
    }
  },
  mounted() {

  },
  computed: {
    ...mapGetters(["userId"]),
  },
  // 方法集合
  methods: {
    openDialog(row) {
      this.dialogVisible = true
      this.form.planDetailsId = row.planDetailsId
      this.checkChargerUserId = row.checkChargerUserId
      this.getEquipOptions()
      this.getUserList()
      this.searchInfo()
    },
    searchInfo() {
      getExamineRecord({ planDetailsId: this.form.planDetailsId }).then(res => {
        if (res.code === 200) {
          let planDetailsId = this.form.planDetailsId
          this.form = { ...res.data }
          this.form.planDetailsId = planDetailsId
          this.recordDetailList = this.form.recordDetailList || []
          if (!this.form.reviewUserId) {
            this.currentStep = 0
          } else {
            this.currentStep = 1
          }
          if (this.form.reviewStatus === 1) {
            this.currentStep = 2
          }
          this.reviewUserId = this.form.reviewUserId
        }
      }).catch(error => {
        console.error(error)
      })
    },
    // 赋值仪器编号
    changeMachineName(value) {
      const index = this.equipOptions.findIndex(item => item.deviceName === value)
      this.form.deviceNumber = this.equipOptions[index].managementNumber
    },
    addRow() {
      this.recordDetailList.push({ testPoint: '' })
    },
    // 提交表单
    submitCheck() {
      console.log(333, this.form)
      this.form.recordDetailList = this.HaveJson(this.recordDetailList)
      this.submitFormLoading = true
      addExamineRecord(this.form).then(res => {
        if (res.code == 200) {
          this.$message.success('新增成功')
          this.resetForm()
        }
        this.submitFormLoading = false
      }).catch(err => {
        this.submitFormLoading = false
      })
    },
    // 提交审核
    examine(statue) {
      const params = {
        planDetailsId: this.form.planDetailsId,
        reviewStatus: statue,
      }
      reviewExamineRecord(params).then(res => {
        if (res.code == 200) {
          this.$message.success('审核成功')
          this.resetForm()
        }
        this.submitFormLoading = false
      }).catch(err => {
        this.submitFormLoading = false
      })
    },
    resetForm() {
      this.dialogVisible = false
      this.$emit('closeDialog1')
    },
    // 获取所有设备
    getEquipOptions() {
      this.equipOptions = []
      deviceScopeSearch({ status: 0 }).then(res => {
        if (res.code === 200 && res.data) {
          this.equipOptions = res.data.map(m => {
            m.value = m.managementNumber
            m.label = m.deviceName
            m.id = m.id
            return m
          })
        }
      }).catch(error => {
        console.error(error)
      })
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
.table td {
  padding: 10px;
}

.table th {
  padding: 10px;
}

.form {
  display: flex;
  align-items: center;
}

.form-item {
  display: flex;
  align-items: center;
  margin-right: 10px;
  margin-top: 10px;
}

>>>.el-dialog__body {
  max-height: 650px;
  overflow-y: auto;
}
</style>
