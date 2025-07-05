<template>
  <div>
    <div class="header">
      <div>照度记录</div>
      <div>
        <el-button size="small" type="primary" @click="clickAdd">新 增</el-button>
      </div>
    </div>
    <el-table :data="tableData" height="calc(100vh - 18em)" style="width: 100%"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
      <el-table-column label="序号" type="index" width="120">
        <template v-slot="scope">
          <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结论" min-width="180" prop="conclusion"></el-table-column>
      <el-table-column label="测试日期" min-width="180" prop="testDate" width="testDate"></el-table-column>
      <el-table-column label="检测者" min-width="180" prop="testerUser"></el-table-column>
      <el-table-column label="核查人" min-width="180" prop="checkerUser"></el-table-column>
      <el-table-column label="设备名称" min-width="180" prop="deviceName"></el-table-column>
      <el-table-column label="设备编号" min-width="180" prop="managementNumber"></el-table-column>
      <el-table-column label="校准日期" min-width="180" prop="lastCalibrationDate"></el-table-column>
      <el-table-column label="下次校准日期" min-width="180" prop="nextCalibrationDate"></el-table-column>
      <el-table-column label="创建时间" min-width="180" prop="createTime"></el-table-column>
      <el-table-column fixed="right" label="操作" min-width="140">
        <template v-slot="scope">
          <el-button size="small" type="text" @click="downLoadPost(scope.row)">导出</el-button>
          <el-button size="small" type="text" @click="edit(scope.row)">编辑</el-button>
          <el-button size="small" type="text" @click="deleteRowFun(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]" :total="search.total"
      layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" background
      @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog :visible.sync="dialogVisible" title="新增" width="50%" @open="openDialog">
      <div style="height: 50vh; overflow-y: auto">
        <el-form ref="form" :model="form" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入测试地点', trigger: 'change' }]" label="测试日期"
                prop="testDate">
                <el-date-picker v-model="form.testDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                  style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入设备名称', trigger: 'change' }]" label="设备名称"
                prop="deviceId">
                <el-select v-model="form.deviceId" class="table_input" clearable filterable placeholder="设备名称"
                  size="small" @change="getCalibrationDateFun">
                  <el-option v-for="item in equipOptions" :key="item.id" :label="item.deviceName" :value="item.id">
                    {{ item.deviceName + item.managementNumber }}
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备编号">
                <el-input v-model="form.managementNumber" disabled size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最近校准日期">
                <el-input v-model="form.lastCalibrationDate" disabled size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下次校准日期">
                <el-input v-model="form.nextCalibrationDate" disabled size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检测者" prop="recipientUser">
                <el-select v-model="form.testerId" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;">
                  <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="核查人" prop="recipientUser">
                <el-select v-model="form.checkerId" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;">
                  <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="结论">
                <el-input v-model="form.conclusion" :rows="2" placeholder="请输入内容" type="textarea">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <div style="display: flex; justify-content: flex-end; margin-bottom: 0.5em">
              <el-button size="small" type="primary" @click="feMeasuredQuantityListAdd">新增</el-button>
            </div>
            <div>
              <el-table :data="form.illuminationDetectionAreaList" height="40vh" style="width: 100%; margin: auto"
                        :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
                <el-table-column label="序号" type="index" width="80"></el-table-column>
                <el-table-column align="center" label="检测区域名称" min-width="180" prop="detectionAreaLabel">
                  <template #default="{ row }">
                    <el-input v-model="row.detectionAreaLabel" :rows="1" type="textarea"></el-input>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="检测值" min-width="300" prop="name">
                  <template>
                    <el-table-column align="center" label="第一次" min-width="100" prop="valueOne">
                      <template #default="{ row }">
                        <el-input v-model="row.valueOne" :rows="1" @blur="getAverage(row)"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="第二次" min-width="100" prop="valueTwo">
                      <template #default="{ row }">
                        <el-input v-model="row.valueTwo" :rows="1" @blur="getAverage(row)"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="第三次" min-width="100" prop="valueThree">
                      <template #default="{ row }">
                        <el-input v-model="row.valueThree" :rows="1" @blur="getAverage(row)"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="平均值" min-width="100" prop="average">
                      <template #default="{ row }">
                        <el-input v-model="row.average" :rows="1"></el-input>
                      </template>
                    </el-table-column>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="备注" min-width="180" prop="detectionAreaLabel">
                  <template #default="{ row }">
                    <el-input v-model="row.remark" :rows="1" type="textarea"></el-input>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="100">
                  <template slot-scope="scope">
                    <el-button size="small" type="text" @click="feMeasuredQuantityListDelete(scope.row, scope.$index)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="addPowerSupplyStability">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFeLightningProtection,
  deviceScopeSearch,
  selectUserCondition,
  treeDevice,
  addFeLightningProtection,
  deleteFeLightningProtection,
  getFeIlluminationDetectionArea,
  deleteFeIlluminationDetectionArea,
  exportFeIllumination
} from '@/api/cnas/resourceDemand/facilitiesEnvironment/facilitiesAndEnvironment'

export default {
  data() {
    return {
      search: {
        size: 10,
        current: 1,
        total: 0
      },
      tableData: [],
      dialogVisible: false,
      submitLoading: false,
      form: {
        testDate: '',
        deviceId: '',
        managementNumber: '',
        lastCalibrationDate: '',
        nextCalibrationDate: '',
        testerId: '',
        checkerId: '',
        conclusion: '',
        illuminationDetectionAreaList: []
      },
      cascaderList: [],
      responsibleOptions: [],
      equipOptions: [],
    }
  },
  mounted() {
    this.initData()
  },
  watch: {
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {
          deviceId: null,
          illuminationDetectionAreaList: []
        }
      }
    }
  },
  methods: {
    getAverage(row) {
      if (row.valueOne && row.valueTwo && row.valueThree) {
        if (!isNaN(row.valueOne) && !isNaN(row.valueTwo) && !isNaN(row.valueThree)) {
          const avg = Number(row.valueOne) + Number(row.valueTwo) + Number(row.valueThree)
          console.log(avg)
          row.average = Math.round(avg / 3);
        } else {
          this.$message.warning("必须为数字！")
        }
      }
    },
    edit(row) {
      getFeIlluminationDetectionArea({ intensityIlluminationId: row.intensityIlluminationId }).then(res => {
        this.form = { ...row }
        this.form.illuminationDetectionAreaList = res.data;
        this.dialogVisible = true
      });
    },
    initData() {
      getFeLightningProtection({ ...this.search }).then(res => {
        if (res.code === 200) {
          this.tableData = res.data.records;
          this.search.total = res.data.total;
        }
      });
    },
    handleSizeChange(val) {
      this.search.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.initData();
    },
    deleteRowFun(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFeLightningProtection({ intensityIlluminationId: row.intensityIlluminationId }).then(res => {
          this.$message.success('删除成功！')
          this.initData()
        })
      })
    },
    feMeasuredQuantityListDelete(row, index) {
      if (row.detectionAreaId) {
        deleteFeIlluminationDetectionArea({ detectionAreaId: row.detectionAreaId }).then(res => {
          if (res.code === 200) {
            this.form.illuminationDetectionAreaList.splice(index, 1)
            this.$message.success('删除成功！')
          }
        })
      } else {
        this.form.illuminationDetectionAreaList.splice(index, 1)
        this.$message.success('删除成功！')
      }
    },
    openDialog() {
      treeDevice().then(res => {
        this.cascaderList = res.data;
      });
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
      this.getUserList()
    },
    clickAdd() {
      this.dialogVisible = true
      this.getEquipOptions()
    },
    // 获取所有设备
    getEquipOptions() {
      this.equipOptions = []
      deviceScopeSearch({ status: '0' }).then(res => {
        if (res.code === 200 && res.data) {
          this.equipOptions = res.data
        }
      }).catch(error => {
        console.error(error)
      })
    },
    feMeasuredQuantityListAdd() {
      const obj = {
        detectionAreaLabel: null,
        valueOne: null,
        valueTwo: null,
        valueThree: null,
        average: null,
        remark: null,
      }
      this.form.illuminationDetectionAreaList.push(obj)
    },
    addPowerSupplyStability() {
      this.submitLoading = true
      this.$refs.form.validate((valid) => {
        if (valid) {
          addFeLightningProtection(this.form).then(res => {
            this.submitLoading = false
            if (res.code === 200) {
              this.initData()
              this.dialogVisible = false
            }
          }).catch(error => {
            this.submitLoading = false
          })
        }
      });
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
    getCalibrationDateFun(val) {
      const index = this.equipOptions.findIndex(item => item.id === val)
      if (index > -1) {
        this.form.managementNumber = this.equipOptions[index].managementNumber
        this.form.lastCalibrationDate = this.equipOptions[index].lastCalibrationDate
        this.form.nextCalibrationDate = this.equipOptions[index].nextCalibrationDate
      }
    },
    // 导出
    downLoadPost(row) {
      exportFeIllumination({ intensityIlluminationId: row.intensityIlluminationId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '照度记录.docx')
      })
    },
  }
}
</script>

<style scoped>
.header {
  height: 3em;
  width: 100%;
  display: flex;
  justify-content: space-between;
}
</style>
