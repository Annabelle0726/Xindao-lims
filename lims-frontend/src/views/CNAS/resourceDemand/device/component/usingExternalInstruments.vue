<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;margin-top: 10px;">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">单位名称</span>
            <el-input size="small" placeholder="请输入" clearable v-model="searchForm.unitName"
                      @keyup.enter.native="getYearTableDetailData"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearTableDetailData">查询</el-button>
            <el-button size="mini" @click="reset">重置</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="handleForm('')">新增</el-button>
        </div>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDetailDataLoading" :data="yearTableDetailData"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          height="calc(100vh - 18em)" style="width: 100% ;">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="单位名称" min-width="150" prop="unitName"></el-table-column>
          <el-table-column label="地址" min-width="100" prop="address"></el-table-column>
          <el-table-column label="仪器名称" min-width="150" prop="deviceName"></el-table-column>
          <el-table-column label="型号" min-width="100" prop="deviceModel"></el-table-column>
          <el-table-column label="配件" min-width="150" prop="parts"></el-table-column>
          <el-table-column label="对方仪器编号" min-width="150" prop="instrumentNumber"></el-table-column>
          <el-table-column label="仪器技术指标" min-width="150" prop="technicalIndex" show-overflow-tooltip></el-table-column>
          <el-table-column label="需求技术指标" min-width="150" prop="technicalRequirements"
            show-overflow-tooltip></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" fixed="right" label="操作" min-width="150">
            <template slot-scope="scope">
              <el-button :disabled="scope.row.ratifyStatus === 1" size="small" type="text"
                @click="handleForm(scope.row.externalApplyId)">操作</el-button>
              <el-button size="small" type="text" @click="handleDownOne(scope.row.externalApplyId)">导出</el-button>
              <el-button size="small" style="color: #f56c6c" type="text"
                @click="deleteFun(scope.row.externalApplyId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
          :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper" background
          @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <application-form v-if="applicationForm" ref="applicationForm" @closeDialog="closeDialog"></application-form>
  </div>
</template>

<script>
import ApplicationForm from "./applicationForm.vue";
import {
  pageDeviceExternalApply,
  exportDeviceExternalApply,
  delDeviceExternalApply,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  name: "using-external-instruments",
  // import 引入的组件需要注入到对象中才能使用
  components: { ApplicationForm },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        unitName: '',
      },
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
    this.getYearTableDetailData()
  },
  // 方法集合
  methods: {
    // 查询
    getYearTableDetailData() {
      this.yearTableDetailDataLoading = true
      pageDeviceExternalApply({
        current: this.pagination1.current,
        size: this.pagination1.size,
        unitName: this.searchForm.unitName,
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
    handleForm(id) {
      this.applicationForm = true
      this.$nextTick(() => {
        this.$refs.applicationForm.openDialog(id)
      })
    },
    closeDialog() {
      this.applicationForm = false
      this.getYearTableDetailData()
    },
    // 重置
    reset() {
      this.searchForm = {
        unitName: '',
      }
      this.getYearTableDetailData()
    },
    // 分页
    handleSizeChange1(val) {
      this.pagination1.size = val
      this.getYearTableDetailData()
    },
    // 分页
    handleCurrentChange1(val) {
      this.pagination1.current = val
      this.getYearTableDetailData()
    },
    // 导出
    handleDownOne(id) {
      this.outLoading = true
      exportDeviceExternalApply({ externalApplyId: id }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '利用外部仪器设备申请表.doc')
      })
    },
    // 删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDeviceExternalApply({ externalApplyId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableDetailData();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  },
}
</script>

<style scoped>
.title {
  height: 46px;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
