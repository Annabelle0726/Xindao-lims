<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="内部审核报告" width="80%" @close="closeImplementDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="审核目的" prop="purposes">
              <el-input v-model="form.purposes" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核依据" prop="basis">
              <el-input v-model="form.basis" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核日期" prop="reviewDate">
              <el-date-picker v-model="form.reviewDate"
                :disabled="operationType === 'examine' || operationType === 'ratify'" clearable format="yyyy-MM-dd"
                placeholder="选择日期" size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审核方法" prop="method">
              <el-input v-model="form.method" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审核范围" prop="scope">
              <el-input v-model="form.scope" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核责任者" prop="responsible">
              <el-input v-model="form.responsible" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核组长" prop="leader">
              <el-input v-model="form.leader" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审核员" prop="auditor">
              <el-input v-model="form.auditor" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审核组分工情况" prop="division">
              <el-input v-model="form.division" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审核概况" prop="overview">
              <el-input v-model="form.overview" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内审组对中心管理体系的结论性评价" prop="conclusion">
              <el-input v-model="form.conclusion" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="改进意见" prop="suggest">
              <el-input v-model="form.suggest" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预计受审核部门完成纠正措施所需时间" prop="actionDate">
              <el-input v-model="form.actionDate" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="不符合情况整改总体跟进确认人" prop="followUser">
              <el-input v-model="form.followUser" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="不符合情况整改总体跟进确认记录" prop="followRecord">
              <el-input v-model="form.followRecord"
                :disabled="operationType === 'examine' || operationType === 'ratify'" :rows="3" clearable size="small"
                type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="本报告发放范围" prop="reportScope">
              <el-input v-model="form.reportScope" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col v-if="operationType === 'ratify'" :span="24">
            <el-form-item label="质量负责人意见" prop="qualityRemark">
              <el-input v-model="form.qualityRemark" :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="operationType === 'examine'" :loading="loading" @click="examine(0)">不通过</el-button>
        <el-button v-if="operationType === 'examine'" :loading="loading" type="primary" @click="examine(1)">通
          过</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" @click="handleApproval(0)">不批准</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" type="primary" @click="handleApproval(1)">批
          准</el-button>
        <el-button v-if="operationType !== 'ratify' && operationType !== 'examine'" @click="closeImplementDia">取
          消</el-button>
        <el-button v-if="operationType !== 'ratify' && operationType !== 'examine'" :loading="loading" type="primary"
          @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="examineDialog" title="审核" width="30%" @close="examineDialog = false">
      <span>
        审核备注：
        <el-input v-model="examineRemark" type="textarea"></el-input>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="examineLoading" @click="examineDialog = false">取 消</el-button>
        <el-button :loading="examineLoading" type="primary" @click="handleExamine(0)">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="approvalDialog" title="批准" width="30%" @close="approvalDialog = false">
      <span>
        批准备注：
        <el-input v-model="qualityRemark" type="textarea"></el-input>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="approvalLoading" @click="approvalDialog = false">取 消</el-button>
        <el-button :loading="approvalLoading" type="primary" @click="handleApproval(0)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInternalReportOne,
  addInternalReport,
  updateInternalReport,
  examineInternalReport,
  qualityInternalReport,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'auditReportDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        purposes: '',
        basis: '',
        reviewDate: '',
        method: '',
        scope: '',
        responsible: '',
        leader: '',
        auditor: '',
        division: '',
        overview: '',
        conclusion: '',
        suggest: '',
        actionDate: '',
        followUser: '',
        followRecord: '',
        reportScope: '',
        qualityRemark: '',
      },
      rules: {
        purposes: [{ required: true, message: '请填写审核目的', trigger: 'blur' }],
        basis: [{ required: true, message: '请填写审核依据', trigger: 'blur' }],
        reviewDate: [{ required: true, message: '请填写审核日期', trigger: 'change' }],
        method: [{ required: true, message: '请填写审核方法', trigger: 'blur' }],
        scope: [{ required: true, message: '请填写审核范围', trigger: 'blur' }],
        responsible: [{ required: true, message: '请填写审核责任者', trigger: 'blur' }],
        leader: [{ required: true, message: '请填写审核组长', trigger: 'blur' }],
        auditor: [{ required: true, message: '请填写审核员', trigger: 'blur' }],
        division: [{ required: true, message: '请填写审核组分工情况', trigger: 'blur' }],
        overview: [{ required: true, message: '请填写审核概况', trigger: 'blur' }],
        conclusion: [{ required: true, message: '请填写', trigger: 'blur' }],
        suggest: [{ required: true, message: '请填写', trigger: 'blur' }],
        actionDate: [{ required: true, message: '请填写', trigger: 'blur' }],
        followUser: [{ required: true, message: '请填写', trigger: 'blur' }],
        followRecord: [{ required: true, message: '请填写', trigger: 'blur' }],
        reportScope: [{ required: true, message: '请填写', trigger: 'blur' }],
        qualityRemark: [{ required: true, message: '请填写', trigger: 'blur' }],
      },
      operationType: '',
      approvalDialog: false,
      approvalLoading: false,
      examineDialog: false,
      examineLoading: false,
      qualityRemark: '',
      examineRemark: '',
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia(type, row) {
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.searchInfo(row)
      }
    },
    // 查询详情
    searchInfo(row) {
      this.diaLoading = true
      getInternalReportOne({ reportId: row.reportId }).then(res => {
        this.diaLoading = false
        this.form = res.data
      }).catch(err => {
        console.log(err)
        this.diaLoading = false
      })
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          const internalReport = this.HaveJson(this.form)
          if (this.operationType === 'add') {
            addInternalReport(internalReport).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeImplementDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateInternalReport(internalReport).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeImplementDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 审核流程
    examine(examineStatus) {
      if (examineStatus === 0) {
        this.examineDialog = true
      } else {
        this.handleExamine(examineStatus)
      }
    },
    handleExamine(examineStatus) {
      this.examineLoading = true
      const internalReport = this.HaveJson(this.form)
      internalReport.examineStatus = examineStatus
      examineInternalReport(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.closeImplementDia(this.departId);
        }
        this.examineLoading = false
      }).catch(() => {
        this.examineLoading = false
      })
    },
    // 提交审核信息
    handleApproval(qualityStatus) {
      this.approvalLoading = true
      const internalReport = this.HaveJson(this.form)
      internalReport.qualityStatus = qualityStatus
      qualityInternalReport(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.closeImplementDia(this.departId);
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    closeImplementDia() {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeImplementDia')
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}

>>>.el-dialog__body {
  max-height: 42em;
  overflow-y: auto;
}

>>>.is-required {
  margin-bottom: 6px;
}
</style>
