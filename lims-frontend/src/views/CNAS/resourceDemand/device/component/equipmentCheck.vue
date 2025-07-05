<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="仪器设备期间核查比对记录表" width="80%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="填写设备核查对比记录"></el-step>
        <el-step title="审核"></el-step>
      </el-steps>
      <div class="form">
        <div class="form-item">
          <span style="width: 116px">核查方式：</span>
          <el-input v-model="form.checkMethod" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
      </div>
      <div class="form">
        <div class="form-item">
          <span style="width: 80px">A仪器名称：</span>
          <el-select v-model="form.adeviceId" :disabled="currentStep !== 0" class="table_input" clearable filterable
            placeholder="设备名称" size="small" style="width:200px" @change="(val) => changeMachineName(val, 'A')">
            <el-option v-for="item in equipOptions" :key="item.value" :label="item.label" :value="item.id">
              {{ item.label + '-' + item.value }}
            </el-option>
          </el-select>
        </div>
        <div class="form-item">
          <span style="width: 116px">A仪器编号：</span>
          <el-input v-model="form.adeviceNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
        <div class="form-item">
          <span style="width: 150px">A仪器测定范围/不确定度：</span>
          <el-input v-model="form.arangeUncertainty" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
      </div>
      <div class="form">
        <div class="form-item">
          <span style="width: 80px">B仪器名称：</span>
          <el-select v-model="form.bdeviceId" :disabled="currentStep !== 0" class="table_input" clearable filterable
            placeholder="设备名称" size="small" style="width:200px" @change="(val) => changeMachineName(val, 'B')">
            <el-option v-for="item in equipOptions" :key="item.value" :label="item.label" :value="item.id">
              {{ item.label + '-' + item.value }}
            </el-option>
          </el-select>
        </div>
        <div class="form-item">
          <span style="width: 116px">B仪器编号：</span>
          <el-input v-model="form.bdeviceNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
        <div class="form-item">
          <span style="width: 150px">B仪器测定范围/不确定度：</span>
          <el-input v-model="form.brangeUncertainty" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
      </div>
      <div class="form">
        <div class="form-item">
          <span style="width: 80px">C仪器名称：</span>
          <el-select v-model="form.cdeviceId" :disabled="currentStep !== 0" class="table_input" clearable filterable
            placeholder="设备名称" size="small" style="width:200px" @change="(val) => changeMachineName(val, 'C')">
            <el-option v-for="item in equipOptions" :key="item.value" :label="item.label" :value="item.id">
              {{ item.label + '-' + item.value }}
            </el-option>
          </el-select>
        </div>
        <div class="form-item">
          <span style="width: 116px">C仪器编号：</span>
          <el-input v-model="form.cdeviceNumber" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
        <div class="form-item">
          <span style="width: 150px">C仪器测定范围/不确定度：</span>
          <el-input v-model="form.crangeUncertainty" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
      </div>
      <div>
        <el-button v-if="currentStep === 0" size="small" type="primary" @click="addTableRow">添加</el-button>
      </div>
      <div style="margin: 10px 0">
        <el-table ref="yearTable" :data="recordContrastDetailsList" height="300px" style="width: 100% ;"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column align="center" header-align="center" label="序号" type="index" width="60"></el-table-column>
          <el-table-column label="核查项目" min-width="170" prop="checkItems">
            <template slot-scope="scope">
              <el-input v-model="scope.row.checkItems" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="A仪器示值" min-width="120" prop="indicationA">
            <template slot-scope="scope">
              <el-input v-model="scope.row.indicationA" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="B仪器示值" min-width="120" prop="indicationB">
            <template slot-scope="scope">
              <el-input v-model="scope.row.indicationB" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="C仪器示值" min-width="120" prop="indicationC">
            <template slot-scope="scope">
              <el-input v-model="scope.row.indicationC" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="差值" min-width="90" prop="dvalue">
            <template slot-scope="scope">
              <el-input v-model="scope.row.dvalue" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="相对偏差" min-width="90" prop="deviation">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviation" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="判定" min-width="110" prop="determine">
            <template slot-scope="scope">
              <el-input v-model="scope.row.determine" :disabled="currentStep !== 0" clearable size="small"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="form">
        <div class="form-item">
          <span style="width: 116px">综合判定：</span>
          <el-input v-model="form.judgment" :disabled="currentStep !== 0" clearable size="small"></el-input>
        </div>
        <div class="form-item">
          <span style="width: 116px">审核人：</span>
          <el-select v-model="form.reviewUserId" :disabled="currentStep !== 0" placeholder="请选择" size="small"
            style="width: 100%">
            <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="currentStep === 0 && userId == checkChargerUserId" :loading="submitFormLoading" type="primary" @click="submitCheck">确
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
  getExamineRecordContrast,
  reviewExamineRecordContrast,
  deviceScopeSearch,
  addExamineRecordContrast,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
import {mapGetters} from "vuex";
export default {
  name: "equipment-check",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      currentStep: 0,
      showStep: 0,
      form: {
        checkerTime: '',
        checkMethod: '',
        adeviceId: '',
        adeviceNumber: '',
        arangeUncertainty: '',
        bdeviceId: '',
        bdeviceNumber: '',
        brangeUncertainty: '',
        cdeviceId: '',
        cdeviceNumber: '',
        crangeUncertainty: '',
        recordContrastDetailsList: [],
        judgment: '',
        reviewUserId: '',
        reviewStatus: '',
        planDetailsId: '',
      },
      equipOptions: [],
      recordContrastDetailsList: [],
      userList: [],
      checkChargerUserId: '',
      reviewUserId: ''
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
      getExamineRecordContrast({ planDetailsId: this.form.planDetailsId }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
          this.recordContrastDetailsList = this.form.recordContrastDetailsList || []
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
    changeMachineName(value, type) {
      const index = this.equipOptions.findIndex(item => item.id === value)
      if (index > -1) {
        if (type === 'A') {
          this.$set(this.form, 'adeviceNumber', this.equipOptions[index].value)
        }
        if (type === 'B') {
          this.$set(this.form, 'bdeviceNumber', this.equipOptions[index].value)
        }
        if (type === 'C') {
          this.$set(this.form, 'cdeviceNumber', this.equipOptions[index].value)
        }
      }
    },
    // 添加表格数据
    addTableRow() {
      this.recordContrastDetailsList.push({ checkItems: '' })
    },
    submitCheck() {
      this.form.recordContrastDetailsList = this.HaveJson(this.recordContrastDetailsList)
      this.submitFormLoading = true
      addExamineRecordContrast(this.form).then(res => {
        if (res.code == 200) {
          this.$message.success('新增成功')
          this.resetForm()
        }
        this.submitFormLoading = false
      }).catch(err => {
        this.submitFormLoading = false
      })
    },
    examine(statue) {
      const params = {
        planDetailsId: this.form.planDetailsId,
        reviewStatus: statue,
      }
      reviewExamineRecordContrast(params).then(res => {
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
      this.$emit('closeDialog')
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
.form {
  display: flex;
  align-items: center;
}

.form-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
</style>
