<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="header">
          <div>试验区域</div>
          <div style="display: flex;">
            <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
              <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">名称</span>
              <el-input size="small" placeholder="请输入" clearable v-model="search.testAreaName"
                        @keyup.enter.native="initData"></el-input>
            </div>
            <div style="line-height: 30px;">
              <el-button size="mini" type="primary" @click="initData">查询</el-button>
              <el-button size="mini" type="primary" @click="clickAdd">新 增</el-button>
            </div>
          </div>
        </div>
        <el-table :data="tableData" height="calc(100vh - 18em)" highlight-current-row style="width: 100%"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          @row-click="clickRow">
          <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
          <el-table-column label="月度时间" min-width="100" prop="monthDate"></el-table-column>
          <el-table-column label="试验区域名称" min-width="180" prop="testAreaName"></el-table-column>
          <el-table-column label="记录员" min-width="120" prop="registrarUserName"></el-table-column>
          <el-table-column label="温度区间" min-width="100" prop="temperatureSection"></el-table-column>
          <el-table-column label="湿度区间" min-width="100" prop="humiditySection"></el-table-column>
          <el-table-column fixed="right" label="操作" min-width="180" align="center">
            <template v-slot="scope">
              <el-button size="small" type="text" @click="downLoadPost(scope.row)">导出</el-button>
<!--              <el-button size="small" type="text" @click="edit(scope.row)">编辑</el-button>-->
              <el-button  size="small" type="text"
                @click="openApprovalDialog(scope.row)">记录员绑定</el-button>
<!--              <el-button size="small" type="text" @click="deleteRowFun(scope.row)">删除</el-button>-->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]" background
          :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </el-col>
      <el-col :span="12">
        <div class="header">
          <div>温湿度记录：{{ saveRow.monthDate }}</div>
          <div>
            <el-button v-if="saveRow.monthDate" size="small" type="primary" @click="dialogVisible1 = true">新
              增</el-button>
          </div>
        </div>
        <el-table :data="tableData1" height="calc(100vh - 18em)" style="width: 100%"  :row-class-name="tableRowClassName">
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column label="序号" type="index" width="60" align="center"></el-table-column>
          <el-table-column label="日期" min-width="100" prop="recordDate"></el-table-column>
          <el-table-column align="center" label="上午" min-width="200">
            <template>
              <el-table-column label="时间" min-width="110" prop="morningTestTime"
                show-overflow-tooltip></el-table-column>
              <el-table-column label="温度" min-width="80" prop="morningTemp" show-overflow-tooltip></el-table-column>
              <el-table-column label="湿度" min-width="80" prop="morningHum" show-overflow-tooltip></el-table-column>
            </template>
          </el-table-column>
          <el-table-column label="记录员" min-width="100" prop="morningRecorderUser"></el-table-column>
          <el-table-column align="center" label="下午" min-width="200">
            <template>
              <el-table-column label="时间" min-width="110" prop="afternoonTime" show-overflow-tooltip></el-table-column>
              <el-table-column label="温度" min-width="80" prop="afternoonTemp" show-overflow-tooltip></el-table-column>
              <el-table-column label="湿度" min-width="80" prop="afternoonHum" show-overflow-tooltip></el-table-column>
            </template>
          </el-table-column>
          <el-table-column label="记录员" min-width="100" prop="afternoonRecorderUser"></el-table-column>
          <el-table-column label="备注" min-width="100" prop="note" show-overflow-tooltip></el-table-column>
          <el-table-column fixed="right" label="操作" min-width="100">
            <template v-slot="scope">
              <el-button size="small" type="text" @click="edit1(scope.row)">编辑</el-button>
              <el-button size="small" type="text" @click="deleteRowFun1(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="search1.size" :page-sizes="[10, 20, 30, 50, 100]" background
          :total="search1.total" layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange1"
          @current-change="handleCurrentChange1">
        </el-pagination>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="dialogVisible" title="提示" width="50%">
      <div style="height: 20vh;">
        <el-form ref="form" :model="form" label-width="80px">
          <el-row>
            <el-col :span="24">
              <el-form-item :rules="[{ required: true, message: '请输入月度时间', trigger: 'blur' }]" label="月度时间"
                prop="monthDate">
                <el-date-picker v-model="form.monthDate" format="yyyy-MM" placeholder="选择月份" size="small"
                  style="width: 100%" type="month" value-format="yyyy-MM">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item :rules="[{ required: true, message: '请输入试验区域', trigger: 'blur' }]" label="试验区域"
                prop="testAreaName">
                <el-input v-model="form.testAreaName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="addPowerSupplyStability">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisible1" title="提示" width="50%" @open="openDialog">
      <div style="height: 40vh;">
        <el-form ref="form1" :model="form1" label-width="120px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="日期">
                <el-date-picker v-model="form1.recordDate" format="yyyy-MM-dd" placeholder="请选择日期" size="small"
                  style="width: 50%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上午时间">
                <el-time-picker v-model="form1.morningTestTime" size="small" value-format="HH:mm:ss" format="HH:mm:ss"
                  style="width: 100%" placeholder="请选择时间">
                </el-time-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上午温度">
                <el-input v-model="form1.morningTemp" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上午湿度">
                <el-input v-model="form1.morningHum" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上午记录员">
                <el-select v-model="form1.morningRecorderId" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;">
                  <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下午时间">
                <el-time-picker v-model="form1.afternoonTime" size="small" value-format="HH:mm:ss" format="HH:mm:ss"
                  style="width: 100%" placeholder="请选择时间">
                </el-time-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下午温度">
                <el-input v-model="form1.afternoonTemp" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下午湿度">
                <el-input v-model="form1.afternoonHum" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下午记录员">
                <el-select v-model="form1.afternoonRecorderId" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;">
                  <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input v-model="form1.note" :rows="2" placeholder="请输入内容" type="textarea">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="addPowerSupplyStability1">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="approvalDialog" title="记录员绑定" width="30%" @open="openDialog">
      <el-form ref="recordForm" :model="recordForm">
        <el-form-item label="记录员: ">
          <el-select v-model="recordForm.registrarUserId" clearable filterable placeholder="请选择" size="small"
                     style="width: 80%;">
            <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="approvalLoading" @click="approvalDialog = false">取消</el-button>
        <el-button :loading="approvalLoading" type="primary" @click="handleApproval">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFeTempHumDate,
  addFeTempHumDate,
  getFeTempHumRecordPage,
  addFeTempHumRecord,
  selectUserCondition,
  deleteFeTempHumRecord,
  deleteFeTempHumDate,
  exportTemperatureAndHumidityRecords,
  affirmFeTempHumDate
} from '@/api/cnas/resourceDemand/facilitiesEnvironment/facilitiesAndEnvironment'

export default {
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0,
        testAreaName: ''
      },
      search1: {
        size: 20,
        current: 1,
        total: 0
      },
      tableData: [],
      tableData1: [],
      dialogVisible: false,
      submitLoading: false,
      dialogVisible1: false,
      form: {},
      form1: {},
      saveRow: {},
      responsibleOptions: [],
      approvalDialog: false,
      approvalLoading: false,
      approvalRow: {},
      recordForm: {
        registrarUserId: ''
      }
    }
  },
  mounted() {
    this.initData()
  },
  watch: {
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {
        }
      }
    },
    dialogVisible1(newVal) {
      if (!newVal) {
        this.form1 = {
        }
      }
    }
  },
  methods: {
    openApprovalDialog(row) {
      this.approvalDialog = true
      this.approvalRow = row
      this.recordForm.registrarUserId = row.registrarUserId
    },
    handleApproval() {
      this.approvalLoading = true
      const params = {
        dateId: this.approvalRow.dateId,
        registrarUserId: this.recordForm.registrarUserId
      }
      affirmFeTempHumDate(params).then(res => {
        this.approvalLoading = false
        if (res.code === 200) {
          this.$message.success('确认成功！')
          this.approvalDialog = false
          this.initData()
        }
      }).catch(err => {
        this.approvalDialog = false
      })
    },
    openDialog() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
    addPowerSupplyStability() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          addFeTempHumDate(this.form).then(res => {
            if (res.code === 200) {
              this.$message.success('新增成功!')
              this.submitLoading = false
              this.initData()
              this.dialogVisible = false
            }
          }).catch(err => {
            this.submitLoading = false
          })
        }
      });
    },
    addPowerSupplyStability1() {
      if (!this.saveRow) {
        this.$message.warning("请选择试验区域！")
        return
      }
      this.$refs.form1.validate((valid) => {
        this.submitLoading = true
        if (valid) {
          this.form1.dateId = this.saveRow.dateId
          addFeTempHumRecord(this.form1).then(res => {
            if (res.code === 200) {
              this.$message.success('新增成功!')
              this.submitLoading = false
              this.initData1(this.saveRow.dateId)
              this.dialogVisible1 = false
            }
          }).catch(err => {
            this.submitLoading = false
          })
        }
      });
    },
    clickRow(row) {
      this.saveRow = row
      this.search1.current = 1
      this.initData1(row.dateId)
    },
    edit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    deleteRowFun(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFeTempHumDate({ dateId: row.dateId }).then(res => {
          this.$message.success('删除成功！')
          this.initData()
        })
      })
    },
    edit1(row) {
      this.form1 = { ...row }
      this.dialogVisible1 = true
    },
    // 导出
    downLoadPost(row) {
      exportTemperatureAndHumidityRecords({ dateId: row.dateId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '温湿度记录.docx')
      })
    },
    deleteRowFun1(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFeTempHumRecord({ tempHumId: row.tempHumId }).then(res => {
          this.$message.success('删除成功！')
          this.initData1(this.saveRow.dateId)
        })
      })
    },
    initData() {
      getFeTempHumDate({
        ...this.search,
        ...this.search
      }).then(res => {
        if (res.code === 200) {
          this.tableData = res.data.records;
          this.search.total = res.data.total;
          if (res.data.records.length === 0) {
            this.tableData1 = []
            this.saveRow.monthDate = ''
          }
        }

      })
    },
    initData1(dateId) {
      let form = { dateId: dateId }
      getFeTempHumRecordPage({
        ...this.search1,
        ...form
      }).then(res => {
        if (res.code === 200) {
          this.tableData1 = res.data.records;
          this.search1.total = res.data.total;
        }
      });
    },
    // 判断温湿度区间变色
    tableRowClassName({row, rowIndex}) {
      if (row.isIssue == 1) {
        return 'warning-row';
      }
      return '';
    },
    handleSizeChange(val) {
      this.search.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.initData();
    },
    handleSizeChange1(val) {
      this.search1.size = val;
      this.initData1(this.saveRow.dateId);
    },
    handleCurrentChange1(val) {
      this.search1.current = val;
      this.initData1(this.saveRow.dateId);
    },
    clickAdd() {
      this.dialogVisible = true
    }
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
>>>.warning-row {
  background: #eea7ae;
}
</style>
