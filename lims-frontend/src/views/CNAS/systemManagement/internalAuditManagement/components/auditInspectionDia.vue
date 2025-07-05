<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="内部审核实施计划" width="80%" @close="closeImplementDia">
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-col :span="12">
          <el-form-item label="受审部门" prop="department">
            <el-input v-model="form.department" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门负责人" prop="departmentHead">
            <el-input v-model="form.departmentHead" :disabled="operationType === 'ratify'" clearable
              size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核员" prop="auditor">
            <el-input v-model="form.auditor" :disabled="operationType === 'ratify'" clearable size="small"></el-input>
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
      </el-form>
      <div v-if="operationType !== 'ratify'" style="text-align: right;margin-bottom: 10px">
        <el-button size="small" type="primary" @click="addRow">添加</el-button>
        <el-button size="small" type="danger" @click="clearTable">清空</el-button>
      </div>
      <el-table :data="checkDetailList" border height="300" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column header-align="center" label="涉及要素和质量体系文件条款" prop="element">
          <template slot-scope="{row}">
            <el-input v-model="row.element" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="审核内容" prop="content">
          <template slot-scope="{row}">
            <el-input v-model="row.content" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="审核方式" prop="method">
          <template slot-scope="{row}">
            <el-input v-model="row.method" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="审核结果记录" prop="resultRecords" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.resultRecords" :disabled="operationType === 'ratify'" size="small" />
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="不符合性质" prop="nonNature" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.nonNature" :disabled="operationType === 'ratify'" size="small" />
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
  getInternalCheckOne,
  addInternalCheck,
  updateInternalCheck,
  ratifyInternalCheck,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'auditInspectionDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        department: '',
        departmentHead: '',
        auditor: '',
        reviewDate: '',
      },
      rules: {
        department: [{ required: true, message: '请填写受审部门', trigger: 'blur' }],
        departmentHead: [{ required: true, message: '请填写部门负责人', trigger: 'blur' }],
        auditor: [{ required: true, message: '请填写审核员', trigger: 'blur' }],
        reviewDate: [{ required: true, message: '请填写审核日期', trigger: 'blur' }],
      },
      checkDetailList: [],
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
      getInternalCheckOne({ checkId: row.checkId }).then(res => {
        this.diaLoading = false
        this.form = res.data
        this.checkDetailList = this.form.checkDetailList
      }).catch(err => {
        console.log(err)
        this.diaLoading = false
      })
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.checkDetailList.length === 0) {
            this.$message.warning('请添加表格数据')
            return
          }
          this.loading = true
          const internalCheckDto = this.HaveJson(this.form)
          internalCheckDto.checkDetailList = this.HaveJson(this.checkDetailList)
          if (this.operationType === 'add') {
            addInternalCheck(internalCheckDto).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeImplementDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateInternalCheck(internalCheckDto).then(res => {
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
      const internalCheckDto = this.HaveJson(this.form)
      internalCheckDto.ratifyStatus = ratifyStatus
      internalCheckDto.ratifyRemark = ratifyStatus === 0 ? this.ratifyRemark : ''
      ratifyInternalCheck(internalCheckDto).then(res => {
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
      this.checkDetailList.push({
        element: '',
        content: '',
        method: '',
        resultRecords: '',
        nonNature: '',
      })
    },
    // 清空表格数据
    clearTable() {
      this.checkDetailList = []
    },
    closeImplementDia() {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeImplementDia')
    },
  }
};
</script>

<style scoped></style>
