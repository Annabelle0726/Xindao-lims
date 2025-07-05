<!-- 设备维护 -->
<template>
  <div>
    <div class="search">
      <el-form :model="search" ref="search" size="small" :inline="true">
        <el-form-item label="流程编号">
          <el-input v-model="search.deviceNumber" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="getAllMessage(clickNodeVal.value)"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="getAllMessage(clickNodeVal.value)">查询</el-button>
          <el-button size="mini" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        <el-button size="small" type="primary" @click="dialogVisible = true; add()">添加维护记录</el-button>
        <el-button :loading="outLoading" size="small" type="primary" @click="handleDownOne">导出</el-button>
      </div>
    </div>
    <div>
      <el-table ref="table" :data="MaintainParam" height="calc(100vh - 20em)"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column label="序号" type="index" width="120">
          <template v-slot="scope">
            <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="流程编号" min-width="180" prop="deviceNumber" />
        <el-table-column label="设备名称" min-width="150" prop="deviceName" />
        <el-table-column label="管理编号" min-width="150" prop="managementNumber" />
        <el-table-column label="维护内容" min-width="150" prop="content" />
        <el-table-column label="维护时间" min-width="150" prop="date" />
        <el-table-column label="提交人" min-width="150" prop="name" />
        <el-table-column label="提交日期" min-width="150" prop="date">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="110">
          <template slot-scope="scope">
            <el-button size="small" type="text" @click="handleViewClick(scope.row)">查看</el-button>
            <el-button size="small" type="text" @click="handleDeleteClick(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" background>
      </el-pagination>
    </div>
    <!-- 新建维护 -->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible" title="添加维护记录"
      top="5vh" width="60%">
      <el-form ref="form" :model="formData" label-width="130px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="流程编号:" prop="deviceNumber">
              <el-input v-model="formData.deviceNumber" clearable disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称:" prop="deviceName">
              <el-input v-model="formData.deviceName" clearable disabled placeholder="请输入" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理编号:" prop="managementNumber">
              <el-input v-model="formData.managementNumber" clearable disabled placeholder="请输入"
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :rules="[{ required: true, message: '请输入维护内容', trigger: 'blur' }]" label="维护内容:"
              prop="content">
              <el-input v-model="formData.content" placeholder="请输入" size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请输入维护时间', trigger: 'blur' }]" label="维护时间:" prop="date">
              <el-date-picker v-model="formData.date" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width:100%" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请输入维护时间', trigger: 'blur' }]" label="维护类型:"
              prop="maintenanceType">
              <el-radio-group v-model="formData.maintenanceType" :disabled="!this.editMode">
                <el-radio :label="0">使用前后维护</el-radio>
                <el-radio :label="1">计划中维护</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请选择下次维护时间', trigger: 'blur' }]" label="下次维护时间:"
              prop="nextDate">
              <el-date-picker v-model="formData.nextDate" :picker-options="{ disabledDate: this.disabledDate }"
                format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width:100%" type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :rules="[{ required: true, message: '请输入维护人', trigger: 'blur' }]" label="维护人:" prop="name">
              <el-input v-model="formData.name" clearable placeholder="请输入" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注:">
              <el-input v-model="formData.comments" placeholder="请输入" size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="editMode" @click="dialogVisible = false">取 消</el-button>
        <el-button v-if="editMode" type="primary" @click="addRecord">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  exportMaintenanceRecord,
  deleteDeviceMaintenance,
  selectDeviceByCode,
  addDeviceMaintenance,
  getDeviceMaintenancePage,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0,
        deviceNumber: ''
      },
      editMode: false,
      dialogVisible: false,
      //表单维护内容
      formData: {},
      //表头显示
      MaintainParam: [],
      outLoading: false
    }
  },
  mounted() {
    this.getAllMessage(this.clickNodeVal.value)
  },
  methods: {
    //下次维护日期:禁用在维护日期前的日期
    disabledDate(time) {
      let selectDate = this.formData.date
      if (selectDate) {
        let oldDate = new Date(selectDate)
        return time <= oldDate.getTime()
      }
      return false
    },
    handleSizeChange(val) {
      this.search.size = val
      this.getAllMessage(this.clickNodeVal.value)
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getAllMessage(this.clickNodeVal.value)
    },
    //操作详情查看
    handleViewClick(row) {
      this.editMode = false;
      this.dialogVisible = true;
      this.formData = row;
    },
    // 导出
    handleDownOne() {
      this.outLoading = true
      exportMaintenanceRecord({ deviceId: this.clickNodeVal.value }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备维护保养记录.doc')
      })
    },
    //操作详情删除
    handleDeleteClick(index, row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDeviceMaintenance({ id: row.id }).then(res => {
        })
        // this.MaintainParam.splice(index, 1);
        this.getAllMessage(this.clickNodeVal.value)
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //点击后可编辑
    add() {
      selectDeviceByCode({ id: this.clickNodeVal.value }).then(res => {
        this.formData.deviceName = res.data.deviceName
        this.formData.managementNumber = res.data.managementNumber
        this.$nextTick(() => {
          this.$refs['form'].clearValidate()
        })
      });
      this.editMode = true;
    },
    //添加维护记录
    addRecord() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.formData.deviceId = this.clickNodeVal.value;
          addDeviceMaintenance(this.formData).then(res => {
            if (res.code == 200) {
              this.$message.success('添加成功');
              this.getAllMessage(this.clickNodeVal.value)
              this.dialogVisible = false;
              this.formData = {}; //清空表单
            }
          })
          // this.MaintainParam.push(this.formData)
        }
      })
    },
    resetSearch() {
      this.search = {
        size: 20,
        current: 1,
        total: 0,
        deviceNumber: '',
      }
      this.getAllMessage(this.clickNodeVal.value);
    },
    //获取表单设备维护信息
    getAllMessage(deviceId) {
      getDeviceMaintenancePage({
        deviceId,
        ...this.search
      }).then(res => {
        if (res.code == 200) {
          this.MaintainParam = res.data.records
          this.search.total = res.data.total
        }
      })
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getAllMessage(newVal.value)
      }
    },
    dialogVisible(newVal) {
      if (newVal === false) {
        this.formData = {}
        this.$refs['form'].clearValidate()
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
  align-items: flex-start;
}
</style>
