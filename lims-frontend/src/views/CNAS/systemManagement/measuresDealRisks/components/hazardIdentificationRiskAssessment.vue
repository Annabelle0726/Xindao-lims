<template>
  <div style="padding: 10px">
    <div class="header">
      <span></span>
      <div style="min-width: 200px">
        <el-button type="primary" size="small" @click="addFun">新 增</el-button>
        <el-button type="primary" size="small" @click="approvalFun">审 批</el-button>
        <el-button type="primary" size="small" @click="approveFun">批 准</el-button>
        <el-upload style="display: inline-block; padding: 0 6px" :headers="uploadHeader" :action="action"
          :on-error="onError" :show-file-list="false" :on-success="onSuccess">
          <el-button size="small" type="primary">导 入</el-button>
        </el-upload>
        <el-button size="small" @click="openDownloadDia">导出</el-button>
      </div>
    </div>
    <el-table :data="tableData" style="width: 100%" height="calc(100vh - 18em)" key="table1"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
      <el-table-column type="index" label="序号" width="120">
        <template v-slot="scope">
          <span>{{ (page.current - 1) * page.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="venue" label="地点/活动" min-width="180"></el-table-column>
      <el-table-column prop="hazard" label="危险因素" width="testDate" min-width="180"></el-table-column>
      <el-table-column prop="accidents" label="可能导致的事故" min-width="180"></el-table-column>
      <el-table-column prop="injury" label="对人可能造成的危害" min-width="180"></el-table-column>
      <el-table-column label="风险评价" align="center" min-width="180">
        <template>
          <el-table-column prop="riskL" label="L" min-width="80"></el-table-column>
          <el-table-column prop="riskE" label="E" min-width="80"></el-table-column>
          <el-table-column prop="riskC" label="C" min-width="80"></el-table-column>
          <el-table-column prop="riskD" label="D" min-width="80"></el-table-column>
        </template>
      </el-table-column>
      <el-table-column prop="level" label="风险等级" min-width="180"></el-table-column>
      <el-table-column prop="measures" label="控制措施" min-width="180"></el-table-column>
      <el-table-column prop="editorName" label="编制人姓名" min-width="180"></el-table-column>
      <el-table-column prop="editorDate" label="编制日期" min-width="180"></el-table-column>
      <el-table-column prop="approvalName" label="审批人姓名" min-width="180"></el-table-column>
      <el-table-column prop="approvalDate" label="审批日期" min-width="180"></el-table-column>
      <el-table-column prop="approvalStatus" label="审批状态" min-width="180">
        <template #default="{ row }">
          {{ row.approvalStatus === 1 ? '通过' : row.approvalStatus === 2 ? '不通过' : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="approveName" label="批准人姓名" min-width="180"></el-table-column>
      <el-table-column prop="approveStatus" label="批准状态" min-width="180">
        <template #default="{ row }">
          {{ row.approveStatus === 1 ? '通过' : row.approveStatus === 2 ? '不通过' : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="approveDate" label="批准日期" min-width="180"></el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template v-slot="scope">
          <el-button type="text" size="small" @click="editClick(scope.row)">编辑</el-button>
          <el-button @click="deleteClick(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="1" :page-size="page.size" :page-sizes="[10, 20, 30, 50, 100]" :total="page.total"
      layout="->,total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" background
      @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="50%">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="地点/活动">
              <el-input v-model="form.venue" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="危险因素">
              <el-input v-model="form.hazard" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可能导致的事故">
              <el-input v-model="form.accidents" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对人可能造成的危害" label-width="140px">
              <el-input v-model="form.injury" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评价/L">
              <el-input v-model="form.riskL" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评价/E">
              <el-input v-model="form.riskE" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评价/C">
              <el-input v-model="form.riskC" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评价/D">
              <el-input v-model="form.riskD" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级">
              <el-input v-model="form.level" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制措施">
              <el-input v-model="form.measures" size="small"></el-input>
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
import { getToken } from "@/utils/auth";
import {
  getPageResults,
  dangerousRiskApproval,
  hazardIdentificationAndRiskApproval,
  removeRiskFactors,
  addNewRiskFactors,
  exportHazardFactorIdentification,
} from '@/api/cnas/systemManagement/measuresDealRisks.js'
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      dialogVisible: false,
      form: {},
      page: {
        total: 0,
        size: 10,
        current: 1,
      },
      tableData: [],
      loading: false
    }
  },
  computed: {
    action() {
      return this.javaApi + '/manageRiskAssessmentResults/riskAssessmentImport'
    },
    ...mapGetters(["userId"]),
  },
  methods: {
    handleSizeChange(val) {
      this.page.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.page.current = val;
      this.initData();
    },
    // 分页表格初始化
    initData() {
      this.tableData = []
      getPageResults(this.page).then(res => {
        this.tableData = res.data.records;
        this.page.total = res.data.total;
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
      dangerousRiskApproval({ approval: userId, status: status }).then(res => {
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
      hazardIdentificationAndRiskApproval({ approve: userId, status: status }).then(res => {
        this.initData()
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
      });
    },
    // 文件上传失败
    onError() {
      this.$message({
        type: 'error',
        message: '操作失败!'
      });
    },
    // 文件上传成功
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
        removeRiskFactors({ id: row.id }).then(res => {
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
      addNewRiskFactors(this.form).then(res => {
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
      exportHazardFactorIdentification().then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '危险因素辨识与风险评价结果一览' + '.docx');
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
