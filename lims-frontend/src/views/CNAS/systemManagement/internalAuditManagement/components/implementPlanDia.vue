<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="内部审核实施计划" width="80%" @close="closeImplementDia">
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-col :span="24">
          <el-form-item label="审核目的" prop="purposes">
            <el-input v-model="form.purposes" :disabled="operationType === 'ratify'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审核性质" prop="nature">
            <el-input v-model="form.nature" :disabled="operationType === 'ratify'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审核范围" prop="scope">
            <el-input v-model="form.scope" :disabled="operationType === 'ratify'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审核依据" prop="basis">
            <el-input v-model="form.basis" :disabled="operationType === 'ratify'" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核组长" prop="teamLeader">
            <el-input v-model="form.teamLeader" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内审员" prop="internalAuditor">
            <el-input v-model="form.internalAuditor" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核日期" prop="reviewDate">
            <el-date-picker v-model="form.reviewDate" :disabled="operationType === 'ratify'" clearable
              format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width: 100%" type="date"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核方法" prop="auditMethod">
            <el-input v-model="form.auditMethod" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次会议时间" prop="firstMeetingTime">
            <el-date-picker v-model="form.firstMeetingTime" :disabled="operationType === 'ratify'" clearable
              format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width: 100%" type="date"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="末次会议时间" prop="lastMeetingTime">
            <el-date-picker v-model="form.lastMeetingTime" :disabled="operationType === 'ratify'" clearable
              format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width: 100%" type="date"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核报告提交日期" prop="submitTime">
            <el-date-picker v-model="form.submitTime" :disabled="operationType === 'ratify'" clearable
              format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width: 100%" type="date"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核报告发放范围" prop="submitScope">
            <el-input v-model="form.submitScope" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div v-if="operationType !== 'ratify'" style="text-align: right;margin-bottom: 10px">
        <el-button size="small" type="primary" @click="addRow">添加</el-button>
        <el-button size="small" type="danger" @click="clearTable">清空</el-button>
      </div>
      <el-table :data="implementDetailList" height="300" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column header-align="center" label="时间" prop="implement">
          <template slot-scope="{row}">
            <el-input v-model="row.implement" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="受审核部门" prop="department">
          <template slot-scope="{row}">
            <el-input v-model="row.department" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="责任人" prop="responsible">
          <template slot-scope="{row}">
            <el-input v-model="row.responsible" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="审核员" prop="auditor" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.auditor" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="审核内容" prop="reviewContent" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.reviewContent" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="operationType === 'ratify'" :loading="loading" @click="ratify(0)">不批准</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" type="primary" @click="ratify(1)">批
          准</el-button>
        <el-button v-if="operationType !== 'ratify'" @click="closeImplementDia">取 消</el-button>
        <el-button v-if="operationType !== 'ratify'" :loading="loading" type="primary" @click="handleEdit">提
          交</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="approvalDialog" title="批准" width="30%" @close="approvalDialog = false">
      <span>
        批准备注：
        <el-input v-model="ratifyRemark" type="textarea"></el-input>
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
  getInternalImplementOne,
  addInternalImplement,
  updateInternalImplement,
  ratifyInternalImplement,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'implementPlanDia',
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
        nature: '',
        scope: '',
        basis: '',
        teamLeader: '',
        internalAuditor: '',
        reviewDate: '',
        auditMethod: '',
        firstMeetingTime: '',
        lastMeetingTime: '',
        submitTime: '',
        submitScope: '',
      },
      rules: {
        purposes: [{ required: true, message: '请填写审核目的', trigger: 'blur' }],
        nature: [{ required: true, message: '请填写审核性质', trigger: 'blur' }],
        scope: [{ required: true, message: '请填写审核范围', trigger: 'blur' }],
        basis: [{ required: true, message: '请填写审核依据', trigger: 'blur' }],
        teamLeader: [{ required: true, message: '请填写审核组长', trigger: 'blur' }],
        internalAuditor: [{ required: true, message: '请填写内审员', trigger: 'blur' }],
        reviewDate: [{ required: true, message: '请填写审核时间', trigger: 'blur' }],
        auditMethod: [{ required: true, message: '请填写审核方法', trigger: 'blur' }],
        firstMeetingTime: [{ required: true, message: '请填写首次会议时间', trigger: 'blur' }],
        lastMeetingTime: [{ required: true, message: '请填写末次会议时间', trigger: 'blur' }],
        submitTime: [{ required: true, message: '请填写审核报告提交日期', trigger: 'blur' }],
        submitScope: [{ required: true, message: '请填写审核报告发放范围', trigger: 'blur' }],
      },
      implementDetailList: [],
      operationType: '',
      approvalDialog: false,
      approvalLoading: false,
      ratifyRemark: '',
    };
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
      getInternalImplementOne({ implementId: row.implementId }).then(res => {
        this.diaLoading = false
        this.form = res.data
        this.implementDetailList = this.form.implementDetailList
      }).catch(err => {
        console.log(err)
        this.diaLoading = false
      })
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.implementDetailList.length === 0) {
            this.$message.warning('请添加表格数据')
            return
          }
          this.loading = true
          const internalImplementDto = this.HaveJson(this.form)
          internalImplementDto.implementDetailList = this.HaveJson(this.implementDetailList)
          if (this.operationType === 'add') {
            addInternalImplement(internalImplementDto).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeImplementDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateInternalImplement(internalImplementDto).then(res => {
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
    ratify(ratifyStatus) {
      // 不批准需要填写批准内容
      if (ratifyStatus === 0) {
        this.approvalDialog = true
      } else {
        this.handleApproval(ratifyStatus)
      }
    },
    // 提交批准信息
    handleApproval(ratifyStatus) {
      this.approvalLoading = true
      const internalImplementDto = this.HaveJson(this.form)
      internalImplementDto.ratifyStatus = ratifyStatus
      internalImplementDto.ratifyRemark = ratifyStatus === 0 ? this.ratifyRemark : ''
      ratifyInternalImplement(internalImplementDto).then(res => {
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
    // 增加表格行数据
    addRow() {
      this.implementDetailList.push({
        implement: '',
        department: '',
        responsible: '',
        auditor: '',
        reviewContent: '',
      })
    },
    // 清空表格数据
    clearTable() {
      this.implementDetailList = []
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
  margin: 10vh auto 50px !important;
}

>>>.el-dialog__body {
  max-height: 38em;
  overflow-y: auto;
}
</style>
