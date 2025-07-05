<template>
  <div style="padding: 10px">
    <div class="header">
      <div></div>
      <div style="min-width: 200px">
        <el-button type="primary" size="small" @click="addFun">新 增</el-button>
        <el-button type="primary" size="small" @click="approvalFun">审 批</el-button>
        <el-button type="primary" size="small" @click="approveFun">批 准</el-button>
        <el-upload style="display: inline-block; padding: 0 6px" :action="action" :headers="uploadHeader"
          :on-error="onError" :show-file-list="false" :on-success="onSuccess">
          <el-button size="small" type="primary">导 入</el-button>
        </el-upload>
        <el-button size="small" @click="openDownloadDia">导出</el-button>
      </div>
    </div>
    <el-table :data="tableData" style="width: 100%" height="calc(100vh - 18em)" key="table0"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
      <el-table-column type="index" label="序号" width="120">
        <template v-slot="scope">
          <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="jobActivity" label="作业活动" min-width="180"></el-table-column>
      <el-table-column prop="category" label="风险因素类别" width="testDate" min-width="180"></el-table-column>
      <el-table-column prop="description" label="风险因素描述" min-width="180"></el-table-column>
      <el-table-column prop="result" label="可导致的事故" min-width="180"></el-table-column>
      <el-table-column prop="intolerable" label="是否不可承受风险" min-width="180"></el-table-column>
      <el-table-column prop="plan" label="控制计划" min-width="180"></el-table-column>
      <el-table-column prop="editorName" label="编制人姓名" min-width="180"></el-table-column>
      <el-table-column prop="editorDate" label="编制日期" min-width="180"></el-table-column>
      <el-table-column prop="approvalName" label="审批姓名" min-width="180"></el-table-column>
      <el-table-column prop="approvalDate" label="审批日期" min-width="180"></el-table-column>
      <el-table-column prop="approvalStatus" label="审批状态" min-width="180">
        <template #default="{ row }">
          {{ row.approvalStatus === 1 ? '通过' : row.approvalStatus === 2 ? '不通过' : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="approveName" label="批准姓名" min-width="180"></el-table-column>
      <el-table-column prop="approveStatus" label="批准状态" min-width="180">
        <template #default="{ row }">
          {{ row.approveStatus === 1 ? '通过' : row.approveStatus === 2 ? '不通过' : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="approveDate" label="批准人日期" min-width="180"></el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template v-slot="scope">
          <el-button type="text" size="small" @click="editClick(scope.row)">编辑</el-button>
          <el-button @click="deleteClick(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]" :total="search.total"
      layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" background
      @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="50%">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="作业活动">
              <el-input v-model="form.jobActivity" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险因素类别">
              <el-input v-model="form.category" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险因素描述">
              <el-input v-model="form.description" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可导致的事故">
              <el-input v-model="form.result" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否不可承受风险" label-width="130px">
              <el-input v-model="form.intolerable" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制计划">
              <el-input v-model="form.plan" size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addApi" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPageList,
  riskAnalysisApprovalOfControlPlanChecklist,
  approvalOfControlPlanChecklist,
  deleteSignificantRiskFactorAnalysis,
  analysisOfMajorRiskFactorsAdded,
  exportSignificantRiskFactors,
} from '@/api/cnas/systemManagement/measuresDealRisks.js'
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      dialogVisible: false,
      form: {},
      loading: false,
      search: {
        size: 20,
        current: 1,
        total: 0
      },
      tableData: [],
    }
  },
  computed: {
    action() {
      return this.javaApi + '/manageControlPlanList/importControlPlanList'
    },
    ...mapGetters(["userId"]),
  },
  methods: {
    handleSizeChange(val) {
      this.search.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.initData();
    },
    initData() {
      getPageList(this.search).then(res => {
        this.tableData = res.data.records;
        this.search.total = res.data.total;
      });
    },
    // 审批
    approvalFun() {
      this.$confirm('是否审批通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        closeOnClickModal: false, // 禁止点击遮罩层关闭
        distinguishCancelAndClose: true,
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            this.approvalApi(this.userId, 1)
            done();
          } else if (action === 'cancel') {
            this.approvalApi(this.userId, 2)
            done();
          } else if (action === 'close') {
            // 点击“×”按钮，不允许关闭
            done();
          }
        }
      })
    },
    // 审批接口
    approvalApi(userId, status) {
      riskAnalysisApprovalOfControlPlanChecklist({ approval: userId, status }).then(res => {
        this.initData()
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
      });
    },
    // 批准
    approveFun() {
      this.$confirm('是否批准通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        closeOnClickModal: false, // 禁止点击遮罩层关闭
        distinguishCancelAndClose: true,
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            this.approveApi(this.userId, 1)
            done();
          } else if (action === 'cancel') {
            this.approveApi(this.userId, 2)
            done();
          } else if (action === 'close') {
            // 点击“×”按钮，不允许关闭
            done();
          }
        }
      })
    },
    // 批准接口
    approveApi(userId, status) {
      approvalOfControlPlanChecklist({ approve: userId, status }).then(res => {
        this.initData()
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
      });
    },
    onError() {
      this.$message({
        type: 'error',
        message: '操作失败!'
      });
    },
    onSuccess(response) {
      if (response.code != 200) {
        this.$message({
          type: 'error',
          message: response.message,
        });
        return
      }
      this.initData()
      this.$message({
        type: 'success',
        message: '操作成功!'
      });
    },
    addFun() {
      this.form = {}
      this.dialogVisible = true
    },
    // 删除
    deleteClick(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSignificantRiskFactorAnalysis({ id: row.id }).then(res => {
          this.initData()
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    addApi() {
      this.loading = true
      analysisOfMajorRiskFactorsAdded(this.form).then(res => {
        this.dialogVisible = false
        this.loading = false
        this.initData()
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
      }).catch(err => {
        this.loading = false
      });
    },
    // 编辑
    editClick(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 导出
    openDownloadDia() {
      exportSignificantRiskFactors().then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '重大风险因素分析及控制计划清单' + '.docx');
      })
    },
  },
  mounted() {
    this.initData()
  },
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
