<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="内部审核年度计划" width="1000px" @close="closeYearDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="内审目的" prop="purpose">
              <el-input v-model="form.purpose" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内审范围" prop="scope">
              <el-input v-model="form.scope" :disabled="operationType === 'examine' || operationType === 'ratify'"
                :rows="3" clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内审依据" prop="basis">
              <el-input v-model="form.basis" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组长" prop="leader">
              <el-input v-model="form.leader" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组员" prop="crew">
              <el-input v-model="form.crew" :disabled="operationType === 'examine' || operationType === 'ratify'"
                clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <table border="1" cellspacing="10" class="table">
        <tr>
          <td class="div-with-line">
            <span style="float: left;">部门</span>
            <span style="float: right;">月份</span>
          </td>
          <th v-for="(item, index) in dic1" :key="index">{{ item }}</th>
        </tr>
        <tr v-for="(item, index) in planDetailList" :key="index">
          <td>{{ item.department }}</td>
          <th>
            <el-input v-model="item.january" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.february" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.march" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.april" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.may" :disabled="operationType === 'examine' || operationType === 'ratify'" clearable
              size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.june" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.july" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.august" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.september" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.october" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.november" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
          <th>
            <el-input v-model="item.december" :disabled="operationType === 'examine' || operationType === 'ratify'"
              clearable size="small"></el-input>
          </th>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="operationType === 'examine'" :loading="loading" @click="examine(0)">不通过</el-button>
        <el-button v-if="operationType === 'examine'" :loading="loading" type="primary" @click="examine(1)">通
          过</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" @click="approval(0)">不批准</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" type="primary" @click="handleApproval(1)">批
          准</el-button>
        <el-button v-if="operationType !== 'ratify' && operationType !== 'examine'" @click="closeYearDia">取
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
  getInternalPlanOne,
  addInternalPlan,
  updateInternalPlan,
  examineInternalPlan,
  ratifyInternalPlan,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'yearPlanDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        purpose: '',
        scope: '',
        basis: '',
        leader: '',
        crew: '',
      },
      rules: {
        purpose: [{ required: true, message: '请填写内审目的', trigger: 'blur' }],
        scope: [{ required: true, message: '请填写内审范围', trigger: 'blur' }],
        basis: [{ required: true, message: '请填写内审依据', trigger: 'blur' }],
        leader: [{ required: true, message: '请填写组长', trigger: 'blur' }],
        crew: [{ required: true, message: '请填写组员', trigger: 'blur' }],
      },
      operationType: '',
      approvalDialog: false,
      approvalLoading: false,
      examineDialog: false,
      examineLoading: false,
      ratifyRemark: '',
      examineRemark: '',
      dic1: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      planDetailList: [{ department: '装备电缆实验室' }, { department: '通信产品实验室' }, { department: '电力产品实验室' }, { department: '储能产品实验室' }, { department: '射频线缆实验室' }],
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
      getInternalPlanOne({ planId: row.planId }).then(res => {
        this.diaLoading = false
        this.form = res.data
        this.planDetailList = this.form.planDetailList
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
          const internalPlan = this.HaveJson(this.form)
          internalPlan.planDetailList = this.HaveJson(this.planDetailList)
          if (this.operationType === 'add') {
            addInternalPlan(internalPlan).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeYearDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateInternalPlan(internalPlan).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeYearDia()
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
      internalReport.examineRemark = this.examineRemark
      examineInternalPlan(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.closeYearDia(this.departId);
        }
        this.examineLoading = false
      }).catch(() => {
        this.examineLoading = false
      })
    },
    // 提交批准信息
    approval(ratifyStatus) {
      if (ratifyStatus === 0) {
        this.approvalDialog = true
      } else {
        this.handleApproval(ratifyStatus)
      }
    },
    handleApproval(ratifyStatus) {
      this.approvalLoading = true
      const internalReport = this.HaveJson(this.form)
      internalReport.ratifyStatus = ratifyStatus
      internalReport.ratifyRemark = this.ratifyRemark
      ratifyInternalPlan(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.closeYearDia(this.departId);
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    closeYearDia() {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeYearDia')
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

.table {
  width: 100%;
  margin-top: 20px;
}

.table th {
  width: 70px;
}

.table td {
  width: 70px;
  height: 70px;
  text-align: center;
}

.div-with-line {
  width: 70px;
  height: 70px;
  position: relative;
  /*overflow: hidden; /* 隐藏溢出内容 */
}

.div-with-line::after {
  content: '';
  position: absolute;
  bottom: 0;
  height: 1px;
  background-color: #000000;
  left: 50%;
  transform: translateX(-50%) rotate(45deg);
  transform-origin: center 50%;
  top: 50%;
  width: 100px;
}
</style>
