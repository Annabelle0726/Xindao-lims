<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible"
      title="设备点检记录表" width="80%" @close="resetForm">
      <el-row>
        <el-col :span="24">
          <div class="form-item">
            <span style="width: 103px">测量范围：</span>
            <el-input v-model="form.measurementScope" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="24" style="text-align: center;margin-bottom: 10px">点检使用物质</el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">名称：</span>
            <el-input v-model="form.materialName" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">规格型号：</span>
            <el-input v-model="form.materialModel" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">管理编号：</span>
            <el-input v-model="form.materialManagementNumber" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">精度等级：</span>
            <el-input v-model="form.materialAccuracyGrade" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="24" style="text-align: center;margin-bottom: 10px">测试环境</el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">温度：</span>
            <el-input v-model="form.temperature" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">湿度：</span>
            <el-input v-model="form.humidity" :disabled="operationType === 'check'" clearable size="small"></el-input>
          </div>
        </el-col>
      </el-row>
      <div>
        <el-button v-if="operationType === 'add'" size="small" type="primary" @click="addTableRow">添加</el-button>
      </div>
      <div style="text-align: center">测试数据记录</div>
      <div style="margin: 10px 0">
        <el-table ref="yearTable" :data="details" height="300px" style="width: 100% ;"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column align="center" header-align="center" label="序号" type="index" width="60"></el-table-column>
          <el-table-column label="测试项目" min-width="170" prop="testItems">
            <template slot-scope="scope">
              <el-input v-model="scope.row.testItems" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="标准值" min-width="120" prop="standardValue">
            <template slot-scope="scope">
              <el-input v-model="scope.row.standardValue" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="实测值" min-width="120" prop="measuredValue">
            <template slot-scope="scope">
              <el-input v-model="scope.row.measuredValue" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="示值误差" min-width="120" prop="indicationError">
            <template slot-scope="scope">
              <el-input v-model="scope.row.indicationError" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="允许误差" min-width="90" prop="allowableError">
            <template slot-scope="scope">
              <el-input v-model="scope.row.allowableError" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="单项结论" min-width="90" prop="singleItemConclusion">
            <template slot-scope="scope">
              <el-input v-model="scope.row.singleItemConclusion" :disabled="operationType === 'check'" clearable
                size="small"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-row>
        <el-col :span="24">
          <div class="form-item">
            <span style="width: 103px">测试结论：</span>
            <el-input v-model="form.testConclusion" :disabled="operationType === 'check'" clearable
              size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">备注：</span>
            <el-input v-model="form.remark" :disabled="operationType === 'check'" clearable size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="form-item">
            <span style="width: 116px">请选择复核人：</span>
            <el-select v-model="form.reviewerId" :disabled="operationType === 'check'" placeholder="请选择" size="small"
              style="width: 100%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button v-if="operationType !== 'check'" :loading="submitFormLoading" type="primary" @click="submitCheck">确
          定</el-button>
        <el-button v-if="operationType === 'check'" :loading="submitFormLoading" @click="examine(0)">不通过</el-button>
        <el-button v-if="operationType === 'check'" :loading="submitFormLoading" type="primary" @click="examine(1)">通
          过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeviceInspectionRecord,
  addDeviceInspectionRecord,
  updateDeviceInspectionRecord,
  reviewDeviceInspectionRecord,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
  name: "inspection-form",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      submitFormLoading: false,
      form: {
        measurementScope: '',
        materialName: '',
        materialModel: '',
        materialManagementNumber: '',
        materialAccuracyGrade: '',
        temperature: '',
        humidity: '',
        testConclusion: '',
        remark: '',
        reviewerId: '',
        deviceId: '',
        inspectionRecordId: '',
      },
      operationType: '',
      equipOptions: [],
      details: [],
      userList: [],
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDialog(id, deviceId, type) {
      this.dialogVisible = true
      this.operationType = type
      this.form.deviceId = deviceId
      this.form.inspectionRecordId = id
      this.getUserList()
      if (this.form.inspectionRecordId) {
        this.searchInfo()
      }
    },
    searchInfo() {
      getDeviceInspectionRecord({
        inspectionRecordId: this.form.inspectionRecordId
      }).then(res => {
        if (res.code === 200) {
          this.form = { ...res.data }
          this.details = this.form.details || []
        }
      }).catch(error => {
        console.error(error)
      })
    },
    // 添加表格数据
    addTableRow() {
      this.details.push({ testItems: '' })
    },
    submitCheck() {
      this.form.details = this.HaveJson(this.details)
      this.submitFormLoading = true
      if (this.operationType === 'add') {
        addDeviceInspectionRecord(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('新增成功')
            this.resetForm()
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      } else {
        updateDeviceInspectionRecord(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success('新增成功')
            this.resetForm()
          }
          this.submitFormLoading = false
        }).catch(err => {
          this.submitFormLoading = false
        })
      }
    },
    examine(status) {
      const params = {
        inspectionRecordId: this.form.inspectionRecordId,
        status: status,
      }
      reviewDeviceInspectionRecord(params).then(res => {
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
  margin-bottom: 10px;
}
</style>
