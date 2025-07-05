<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="管理评审计划" width="80%" @close="closeImplementDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="right" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评审时间" prop="reviewTime">
              <el-date-picker v-model="form.reviewTime" :disabled="operationType === 'ratify'" clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" size="small" style="width: 100%" type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评审地点" prop="judgingLocation">
              <el-input v-model="form.judgingLocation" :disabled="operationType === 'ratify'" clearable
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审目的" prop="judgingPurpose">
              <el-input v-model="form.judgingPurpose" :disabled="operationType === 'ratify'" clearable
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审方式" prop="judgingMethod">
              <el-input v-model="form.judgingMethod" :disabled="operationType === 'ratify'" clearable
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="参加人员" prop="participants">
              <!-- <el-input v-model="form.participants" :disabled="operationType === 'ratify'" clearable size="small"></el-input> -->
              <el-select v-model="form.participants" size="small" style="width: 100%;" filterable
                :disabled="operationType === 'ratify'" clearable multiple>
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审范围" prop="judgingScope">
              <el-input v-model="form.judgingScope" :disabled="operationType === 'ratify'" clearable
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审依据" prop="judgingBasis">
              <el-input v-model="form.judgingBasis" :disabled="operationType === 'ratify'" :rows="3" clearable
                size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审主要内容" prop="mainContext">
              <el-input v-model="form.mainContext" :disabled="operationType === 'ratify'" :rows="3" clearable
                size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="准备工作要求" prop="preparationRequirements">
              <el-input v-model="form.preparationRequirements" :disabled="operationType === 'ratify'" :rows="3"
                clearable size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeImplementDia">取 消</el-button>
        <el-button v-if="operationType === 'ratify'" :loading="loading" type="primary" @click="handleApproval(1)">批
          准</el-button>
        <el-button v-if="operationType !== 'ratify'" :loading="loading" type="primary" @click="handleEdit">提
          交</el-button>
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
  addReviewProgram,
  modifyReviewProgram,
} from '@/api/cnas/systemManagement/managementReview.js'
import { dateFormat } from '@/utils/date'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import { mapGetters } from "vuex";
export default {
  name: 'managementFormDIa',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        id: '',
        reviewTime: '',
        judgingLocation: '',
        judgingPurpose: '',
        judgingMethod: '',
        participants: [],
        judgingScope: '',
        judgingBasis: '',
        mainContext: '',
        preparationRequirements: '',
      },
      rules: {
        reviewTime: [{ required: true, message: '请选择评审时间', trigger: 'blur' }],
        judgingLocation: [{ required: true, message: '请填写评审地点', trigger: 'blur' }],
        judgingPurpose: [{ required: true, message: '请填写评审目的', trigger: 'blur' }],
        judgingMethod: [{ required: true, message: '请填写评审方式', trigger: 'blur' }],
        participants: [{ required: true, message: '请填写参加人员', trigger: 'change' }],
        judgingScope: [{ required: true, message: '请填写评审范围', trigger: 'blur' }],
        judgingBasis: [{ required: true, message: '请填写评审依据', trigger: 'blur' }],
        mainContext: [{ required: true, message: '请填写评审主要内容', trigger: 'blur' }],
        preparationRequirements: [{ required: true, message: '请填写准备工作要求', trigger: 'blur' }],
      },
      operationType: '',
      approvalDialog: false,
      approvalLoading: false,
      qualityRemark: '',
      personList: [],
    };
  },
  computed: {
    ...mapGetters(['nickName'])
  },
  mounted() {
    this.getAuthorizedPerson()
  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia(type, row) {
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.form = row
        this.form.participants = row.participants ? row.participants.split(',').map(m => Number(m)) : []
      }
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          const internalReport = this.HaveJson(this.form)
          internalReport.participants = internalReport.participants.join(',')
          if (this.operationType === 'add') {
            addReviewProgram(internalReport).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeImplementDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            modifyReviewProgram(internalReport).then(res => {
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
    // 提交批准信息
    handleApproval(qualityStatus) {
      this.approvalLoading = true
      const internalReport = this.HaveJson(this.form)
      internalReport.approve = this.nickName
      internalReport.approveDate = dateFormat(new Date())
      internalReport.participants = internalReport.participants.join(',')
      modifyReviewProgram(internalReport).then(res => {
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
    getAuthorizedPerson() {
      selectUserCondition().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.personList = data
      })
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}
</style>
