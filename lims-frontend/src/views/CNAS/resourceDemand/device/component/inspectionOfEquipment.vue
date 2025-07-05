<template>
  <div>
    <div>
      <div style="margin: 10px 0;text-align: right">
        <el-button size="small" type="primary" @click="getYearTableDetailData(clickNodeVal.value)">刷新</el-button>
        <el-button size="small" type="primary" @click="handleForm('', 'add')">新增</el-button>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDetailDataLoading" :data="yearTableDetailData"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          height="calc(100vh - 18em)" style="width: 100% ;">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="名称" min-width="150" prop="materialName"></el-table-column>
          <el-table-column label="规格型号" min-width="100" prop="materialModel"></el-table-column>
          <el-table-column label="管理编号" min-width="150" prop="materialManagementNumber"></el-table-column>
          <el-table-column label="精度等级" min-width="180" prop="materialAccuracyGrade"></el-table-column>
          <el-table-column label="温度" min-width="100" prop="temperature"></el-table-column>
          <el-table-column label="湿度" min-width="100" prop="humidity"></el-table-column>
          <el-table-column label="复核状态" min-width="180" prop="status">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 1" type="success">通 过</el-tag>
              <el-tag v-if="scope.row.status === 0" type="danger">不通过</el-tag>
            </template>
          </el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="180">
            <template slot-scope="scope">
              <el-button :disabled="scope.row.status === 1" size="small" type="text"
                @click="handleForm(scope.row.inspectionRecordId, 'edit')">编辑</el-button>
              <el-button :disabled="scope.row.status === 1" size="small" type="text"
                @click="handleForm(scope.row.inspectionRecordId, 'check')">审核</el-button>
              <el-button size="small" type="text" @click="handleDownOne(scope.row.inspectionRecordId)">导出</el-button>
              <el-button :disabled="scope.row.status === 1" size="small" style="color: #f56c6c" type="text"
                @click="deleteFun(scope.row.inspectionRecordId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
          :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper" background
          @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <inspection-form v-if="applicationForm" ref="applicationForm" @closeDialog="closeDialog"></inspection-form>
  </div>
</template>

<script>
import InspectionForm from "./inspectionForm.vue";
import {
  getDeviceInspectionRecordByPage,
  exportDeviceInspectionRecord,
  deleteDeviceInspectionRecord,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  name: "inspection-of-equipment",
  // import 引入的组件需要注入到对象中才能使用
  components: { InspectionForm },
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    // 这里存放数据
    return {
      pagination1: {
        size: 10,
        current: 1,
        total: 0,
      },
      yearTableDetailDataLoading: false,
      yearTableDetailData: [],
      applicationForm: false
    }
  },
  mounted() {
    this.getYearTableDetailData(this.clickNodeVal.value)
  },
  // 方法集合
  methods: {
    // 查询
    getYearTableDetailData(deviceId) {
      this.yearTableDetailDataLoading = true
      getDeviceInspectionRecordByPage({
        current: this.pagination1.current,
        size: this.pagination1.size,
        deviceId: deviceId,
      }).then(res => {
        if (res.code == 200) {
          this.yearTableDetailData = res.data.records
          this.pagination1.total = res.data.total
        }
        this.yearTableDetailDataLoading = false
      }).catch(err => {
        this.yearTableDetailDataLoading = false
      })
    },
    handleForm(id, type) {
      this.applicationForm = true
      this.$nextTick(() => {
        this.$refs.applicationForm.openDialog(id, this.clickNodeVal.value, type)
      })
    },
    closeDialog() {
      this.applicationForm = false
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 分页
    handleSizeChange1(val) {
      this.pagination1.size = val
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 分页
    handleCurrentChange1(val) {
      this.pagination1.current = val
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 导出
    handleDownOne(id) {
      this.outLoading = true
      exportDeviceInspectionRecord({
        inspectionRecordId: id
      }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备点检记录表.doc')
      })
    },
    // 删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDeviceInspectionRecord({ inspectionRecordId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableDetailData(this.clickNodeVal.value);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getYearTableDetailData(newVal.value);
      }
    },
  }
}
</script>

<style scoped>
.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-search {
  display: flex;
  align-items: center;
  margin: 10px 0;
}
</style>
