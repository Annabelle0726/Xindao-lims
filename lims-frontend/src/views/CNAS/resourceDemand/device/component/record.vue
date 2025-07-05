<!-- 使用记录 -->
<template>
  <div>
    <div class="search">
      <el-form :model="search" ref="search" size="small" :inline="true">
        <el-form-item label="样品编号">
          <el-input v-model="search.sampleCode" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="getTableList(clickNodeVal.value)"></el-input>
        </el-form-item>
        <el-form-item label="管理编号">
          <el-input v-model="search.managementNumber" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="getTableList(clickNodeVal.value)"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="getTableList(clickNodeVal.value)">查询</el-button>
          <el-button size="mini" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        <el-button size="small" type="primary" @click="dialogVisible = true, openAdd()">新 建</el-button>
        <el-button :loading="outLoading" size="small" type="primary" @click="openHandleOut">导 出</el-button>
      </div>
    </div>
    <div>
      <el-table ref="Recordtable" :data="formParamList" height="calc(100vh - 20em)"
                v-loading="tableLoading"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <!-- 表格列 -->
        <el-table-column label="序号" type="index" width="70" align="center">
          <template v-slot="scope">
            <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="样品编号" min-width="180" prop="sampleCode" />
        <el-table-column label="设备名称" min-width="150" prop="deviceName" />
        <el-table-column label="管理编号" min-width="150" prop="managementNumber" />
        <el-table-column label="使用前" min-width="120" prop="useBefore">
          <template v-slot="scope">
            {{ scope.row.useBefore === 1 ? '良好' : '异常' }}
          </template>
        </el-table-column>
        <el-table-column label="使用后" min-width="120" prop="useAfter">
          <template v-slot="scope">
            {{ scope.row.useAfter === 1 ? '良好' : '异常' }}
          </template>
        </el-table-column>
        <!--        <el-table-column label="异常情况" min-width="120" prop="abnormal" />-->
        <el-table-column label="使用开始日期" min-width="170" prop="useStartDate" />
        <el-table-column label="使用结束日期" min-width="170" prop="useEndDate" />
        <el-table-column label="使用人" min-width="120" prop="usePerson" />
        <el-table-column label="备注" min-width="120" prop="remark" />
        <!-- 操作按钮 -->
        <el-table-column fixed="right" label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="small" type="text" @click="showDetailsDialog('edit', scope.row)">编辑</el-button>
            <el-button size="small" type="text" @click="showDetailsDialog('view', scope.row)">查看</el-button>
            <el-button size="small" type="text" @click="handleDeleteClick(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" background>
      </el-pagination>
    </div>
    <el-dialog :visible.sync="dialogVisible" title="仪器设备使用记录表" top="5vh" width="55%">
      <el-form ref="form" :model="form" label-width="130px">
        <el-row>
          <el-col :span="24">
            <el-form-item :rules="[{ required: true, message: '样品编号', trigger: 'blur' }]" label="样品编号:"
              prop="sampleCode">
              <el-input v-model="form.sampleCode" :disabled="operationType === 'view'" size="small"
                style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称:" required>
              <el-input v-model="form.deviceName" :disabled="operationType === 'view'" size="small"
                style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理编号:" required>
              <el-input v-model="form.managementNumber" :disabled="operationType === 'view'" size="small"
                style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请选择使用前', trigger: 'change' }]" label="使用前:"
              prop="useBefore">
              <el-radio-group v-model="form.useBefore" :disabled="operationType === 'view'">
                <el-radio :label="1">良好</el-radio>
                <el-radio :label="0">异常</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请选择使用后', trigger: 'change' }]" label="使用后:"
              prop="useAfter">
              <el-radio-group v-model="form.useAfter" :disabled="operationType === 'view'">
                <el-radio :label="1">良好</el-radio>
                <el-radio :label="0">异常</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请输入使用日期', trigger: 'blur' }]" label="使用日期:"
              prop="useDateList">
              <el-date-picker v-model="form.useDateList" :disabled="operationType === 'view'" end-placeholder="结束日期"
                format="yyyy-MM-dd HH:mm:ss" size="small" start-placeholder="开始日期" style="width:100%"
                type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请输入使用人', trigger: 'change' }]" label="使用人:"
              prop="usePersonId">
              <el-select @change="usePersonName" v-model="form.usePersonId" :disabled="operationType === 'view'"
                placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注:">
              <el-input v-model="form.remark" :disabled="operationType === 'view'" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button v-if="operationType !== 'view'" type="primary" @click="saveRecord">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="ratifyDialog" title="导出" width="30%" @close="ratifyDialog = false, exportDate = ''">
      <span>时间：
        <el-date-picker v-model="exportDate" format="yyyy-MM" placeholder="选择月份" size="small" style="width:100%"
          type="month" value-format="yyyy-MM">
        </el-date-picker>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="ratifyDialog = false, exportDate = ''">取 消</el-button>
        <el-button :loading="outLoading" type="primary" @click="handleDown">导 出</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  deviceRecordPage,
  exportUseRecord,
  selectDeviceByCode,
  saveDeviceRecord,
  updateDeviceRecord,
  deleteDeviceRecord,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    },
    // 判断是否是外部分离
    isMenuList: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0,
        sampleCode: '',
        managementNumber: ''
      },
      responsiblePersonList: {},
      isShow: false,
      outLoading: false,
      ratifyDialog: false,
      exportDate: '',
      userList: [],
      form: {
        // deviceId:'',
        processNumber: '',
        deviceName: '',
        managementNumber: '',
        //温度
        temperature: '',
        //湿度
        humidity: '',
        //使用前
        useBefore: 1,
        //使用后
        useAfter: 1,
        //异常情况
        abnormal: '',
        useDateList: [],
        //使用日期
        useStartDate: null,
        useEndDate: null,
        //使用人
        usePerson: '',
        usePersonId: '',
        //备注
        remark: '',
      },
      dialogVisible: false,
      operationType: '',
      formParamList: [],
      tableLoading: false
    }
  },
  mounted() {
    this.getTableList(this.clickNodeVal.value);
  },
  methods: {
    handleSizeChange(val) {
      this.search.size = val
      this.getTableList(this.clickNodeVal.value);
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getTableList(this.clickNodeVal.value);
    },
    resetSearch() {
      this.search = {
        size: 20,
        current: 1,
        total: 0,
        sampleCode: '',
        managementNumber: ''
      }
      this.getTableList(this.clickNodeVal.value);
    },
    async getTableList(deviceId) {
      if (deviceId === undefined) {
        deviceId = ''
      }
      this.tableLoading = true
      await deviceRecordPage({
        deviceId: deviceId,
        ...this.search
      }).then(res => {
        this.tableLoading = false
        if (res.code == 200) {
          this.formParamList = res.data.records
          this.search.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 打开导出弹框选择编制人、批准人
    openHandleOut() {
      this.ratifyDialog = true
    },
    //导出
    handleDown() {
      this.outLoading = true
      exportUseRecord({
        deviceId: this.clickNodeVal.value,
        exportDate: this.exportDate
      }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备使用记录.doc')
      })
    },
    //新建
    openAdd() {
      this.operationType = 'add'
      this.getUserList()
      selectDeviceByCode({ id: this.clickNodeVal.value }).then(res => {
        this.form.deviceName = res.data.deviceName
        this.form.managementNumber = res.data.managementNumber
        this.$nextTick(() => {
          this.$refs['form'].clearValidate()
        })
      });
    },
    usePersonName(value) {
      const index = this.userList.findIndex(item => item.id === value)
      if (index > -1) {
        this.form.usePerson = this.userList[index].name
      }
    },
    //确认添加记录
    saveRecord() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.useStartDate = this.form.useDateList[0]
          this.form.useEndDate = this.form.useDateList[1]
          this.form.deviceId = this.clickNodeVal.value
          if (this.operationType === 'add') {
            saveDeviceRecord(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('新增成功')
                this.getTableList(this.clickNodeVal.value)
                this.dialogVisible = false
              }
            })
            this.dialogVisible = false;
          } else {
            updateDeviceRecord(this.form).then(res => {
              if (res.code == 200) {
                this.$message.success('修改成功')
                this.getTableList(this.clickNodeVal.value)
                this.dialogVisible = false
              }
            })
            this.dialogVisible = false;
          }
        }
      });
    },
    //查看详情
    showDetailsDialog(type, row) {
      this.operationType = type;
      console.log(row)
      this.dialogVisible = true;
      this.getUserList()
      this.form = row;
      let list = []
      if (row.useStartDate === null) {
        row.useStartDate = ''
      }
      if (row.useEndDate === null) {
        row.useEndDate = ''
      }
      list.push(row.useStartDate)
      list.push(row.useEndDate)
      this.$set(this.form, 'useDateList', list)
    },
    handleDeleteClick(index, row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDeviceRecord({ id: row.id }).then(res => {
          this.formParamList.splice(index, 1);
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    getUserList() {
      selectUserCondition().then(res => {
        this.userList = res.data
      })
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getTableList(newVal.value);
      }
    },
    dialogVisible(newVal) {
      if (newVal === false) {
        this.form = {
          // deviceId:'',
          sampleCode: '',
          deviceName: '',
          managementNumber: '',
          //温度
          temperature: '',
          //湿度
          humidity: '',
          //使用前
          useBefore: 1,
          //使用后
          useAfter: 1,
          //异常情况
          abnormal: '',
          //使用日期
          useDateList: [],
          //使用人
          usePersonId: '',
          //备注
          remark: '',
        }
        this.$refs.form.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
