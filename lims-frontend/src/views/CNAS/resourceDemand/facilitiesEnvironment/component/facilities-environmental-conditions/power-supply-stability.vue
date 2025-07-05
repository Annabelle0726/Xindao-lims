<template>
  <div>
    <div class="header">
      <div>电源稳定性</div>
      <div>
        <el-button size="small" type="primary" @click="openAddDia">新 增</el-button>
      </div>
    </div>
    <el-table :data="tableData" height="calc(100vh - 18em)" style="width: 100%"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
      <el-table-column label="序号" type="index" width="120">
        <template v-slot="scope">
          <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="测试地点" min-width="180" prop="testLocation"></el-table-column>
      <el-table-column label="测试日期" min-width="180" prop="testDate" width="testDate"></el-table-column>
      <el-table-column label="结论" min-width="180" prop="conclusion"></el-table-column>
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
                   background layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                   @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog :visible.sync="dialogVisible" title="新增" width="50%" @open="openDialog">
      <div style="height: 50vh; overflow-y: auto">
        <el-form ref="form" :model="form" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入测试地点', trigger: 'blur' }]" label="测试地点"
                prop="testLocation">
                <el-input v-model="form.testLocation" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入测试日期', trigger: 'change' }]" label="测试日期"
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
              <el-table :data="form.feMeasuredQuantityList" height="40vh" style="width: 100%; margin: auto"
                        :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
                <el-table-column label="序号" type="index" width="80"></el-table-column>
                <el-table-column align="center" label="测定量名称" min-width="180" prop="measuredQuantityLabel">
                  <template #default="{ row }">
                    <el-input v-model="row.measuredQuantityLabel" :rows="1" type="textarea"></el-input>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="值" min-width="300" prop="name">
                  <template>
                    <el-table-column align="center" label="A" min-width="100" prop="valueA">
                      <template #default="{ row }">
                        <el-input v-model="row.valueA" :rows="1"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="B" min-width="100" prop="valueB">
                      <template #default="{ row }">
                        <el-input v-model="row.valueB" :rows="1"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="C" min-width="100" prop="valueC">
                      <template #default="{ row }">
                        <el-input v-model="row.valueC" :rows="1"></el-input>
                      </template>
                    </el-table-column>
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
  getLaboratoryFacilityPowerStablePage,
  deviceScopeSearch,
  treeDevice,
  selectUserCondition,
  addLaboratoryFacilityPowerStable,
  deleteFeMeasuredQuantity,
  getFeMeasuredQuantityService,
  deleteLaboratoryFacilityPowerStable,
  exportFePowerStable
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
        testLocation: '',
        testDate: '',
        deviceId: '',
        managementNumber: '',
        lastCalibrationDate: '',
        nextCalibrationDate: '',
        testerId: '',
        checkerId: '',
        conclusion: '',
        feMeasuredQuantityList: []
      },
      equipOptions: [],
      cascaderList: [],
      responsibleOptions: []
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
          feMeasuredQuantityList: []
        }
      }
    }
  },
  methods: {
    // 初始化调用
    initData() {
      getLaboratoryFacilityPowerStablePage({
        ...this.search
      }).then(res => {
        if (res.code === 200) {
          this.tableData = res.data.records;
          this.search.total = res.data.total;
        }
      })
    },
    handleSizeChange(val) {
      this.search.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.initData();
    },
    // 电源稳定性 表格中的行删除按钮
    deleteRowFun(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLaboratoryFacilityPowerStable({ powerStableId: row.powerStableId }).then(res => {
          this.$message.success('删除成功！')
          this.initData()
        })
      })
    },
    openAddDia() {
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
    // 电源稳定性-打开弹框调用设备接口，查询使用的设备
    openDialog() {
      treeDevice().then(res => {
        this.cascaderList = res.data;
      })
      this.$nextTick(() => {
        this.$refs['form'].clearValidate()
      })
      this.getUserList()
    },
    //
    getCalibrationDateFun(val) {
      const index = this.equipOptions.findIndex(item => item.id === val)
      if (index > -1) {
        this.form.managementNumber = this.equipOptions[index].managementNumber
        this.form.lastCalibrationDate = this.equipOptions[index].lastCalibrationDate
        this.form.nextCalibrationDate = this.equipOptions[index].nextCalibrationDate
      }
    },
    // 电源稳定性-测定量弹框中表格的删除行
    feMeasuredQuantityListDelete(row, index) {
      if (row.measuredQuantityId) {
        deleteFeMeasuredQuantity({ measuredQuantityId: row.measuredQuantityId }).then(res => {
          this.form.feMeasuredQuantityList.splice(index, 1)
          this.$message.success('删除成功！')
        })
      } else {
        this.form.feMeasuredQuantityList.splice(index, 1)
        this.$message.success('删除成功！')
      }
    },
    // 电源稳定性-测定量表格新增行
    feMeasuredQuantityListAdd() {
      const obj = {
        measuredQuantityLabel: null,
        valueA: null,
        valueB: null,
        valueC: null,
      }
      this.form.feMeasuredQuantityList.push(obj)
    },
    // 电源稳定性 新增
    addPowerSupplyStability() {
      this.submitLoading = true
      this.$refs.form.validate((valid) => {
        if (valid) {
          addLaboratoryFacilityPowerStable(this.form).then(res => {
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
    // 导出
    downLoadPost(row) {
      exportFePowerStable({ powerStableId: row.powerStableId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '电源稳定性.docx')
      })
    },
    // 电源稳定性 点击行编辑触发
    edit(row) {
      getFeMeasuredQuantityService({ powerStableId: row.powerStableId }).then((res => {
        this.form = { ...row }
        this.form.feMeasuredQuantityList = res.data;
        this.dialogVisible = true
      }))
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          let data = []
          res.data.forEach(a => {
            data.push({
              id: a.id,
              name: a.name
            })
          })
          this.responsibleOptions = data
        }
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
