<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="管理评审会议记录" width="1000px" @close="closeYearDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目的" prop="objective">
              <el-input v-model="form.objective" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点" prop="place">
              <el-input v-model="form.place" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主持人" prop="compere">
              <el-input v-model="form.compere" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="记录人" prop="recordPeople">
              <el-input v-model="form.recordPeople" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日期" prop="date">
              <el-date-picker v-model="form.date" clearable format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" size="small"
                style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="页次" prop="page">
              <el-input v-model="form.page" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评审方式" prop="judgingMethod">
              <el-input v-model="form.judgingMethod" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评审依据" prop="reviewBasis">
              <el-input v-model="form.reviewBasis" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="出席人员" prop="attendess">
              <!-- <el-input v-model="form.attendess	" clearable size="small"></el-input> -->
              <el-select v-model="form.attendess" size="small" style="width: 100%;" filterable clearable multiple>
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审输入情况" prop="reviewInputs">
              <el-input v-model="form.reviewInputs" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评审过程概况" prop="reviewProcess">
              <el-input v-model="form.reviewProcess" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="主要议题概述" prop="mainTopic">
              <el-input v-model="form.mainTopic" :rows="5" clearable placeholder="请输入内容，可输入几千字" size="small"
                type="textarea">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="事项" prop="matters">
              <el-input v-model="form.matters" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="负责人" prop="head">
              <el-input v-model="form.head" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="完成日期" prop="completionDate">
              <el-date-picker v-model="form.completionDate" clearable format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="跟踪确认人" prop="trackingConfirmed">
              <el-input v-model="form.trackingConfirmed" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="跟踪情况确认记录" prop="follerUp">
              <el-input v-model="form.follerUp" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="体系评价" prop="overallEvaluation">
              <el-input v-model="form.overallEvaluation" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeYearDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addReviewReport,
  modifyReviewReport,
} from '@/api/cnas/systemManagement/managementReview.js'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
export default {
  name: 'reviewReportDia',
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
        objective: '',
        place: '',
        compere: '',
        recordPeople: '',
        date: '',
        page: '',
        judgingMethod: '',
        reviewBasis: '',
        attendess: [],
        reviewInputs: '',
        reviewProcess: '',
        mainTopic: '',
        matters: '',
        head: '',
        completionDate: '',
        trackingConfirmed: '',
        follerUp: '',
        overallEvaluation: '',
      },
      rules: {
        objective: [{ required: true, message: '请填写目的', trigger: 'blur' }],
        place: [{ required: true, message: '请填写地点', trigger: 'blur' }],
        compere: [{ required: true, message: '请填写主持人', trigger: 'blur' }],
        recordPeople: [{ required: true, message: '请填写记录人', trigger: 'blur' }],
        date: [{ required: true, message: '请选择日期', trigger: 'change' }],
        completionDate: [{ required: true, message: '请选择完成日期', trigger: 'change' }],
        page: [{ required: true, message: '请填写页次', trigger: 'blur' }],
        judgingMethod: [{ required: true, message: '请填写评审方式', trigger: 'blur' }],
        reviewBasis: [{ required: true, message: '请填写评审依据', trigger: 'blur' }],
        attendess: [{ required: true, message: '请填写出席人员', trigger: 'change' }],
        reviewInputs: [{ required: true, message: '请填写评审输入情况', trigger: 'blur' }],
        reviewProcess: [{ required: true, message: '请填写评审输入情况', trigger: 'blur' }],
        mainTopic: [{ required: true, message: '请填写主要议题概述', trigger: 'blur' }],
        matters: [{ required: true, message: '请填写主要议题概述', trigger: 'blur' }],
        head: [{ required: true, message: '请填写负责人', trigger: 'blur' }],
        trackingConfirmed: [{ required: true, message: '请填写跟踪确认人', trigger: 'blur' }],
        follerUp: [{ required: true, message: '请填写跟踪确认人', trigger: 'blur' }],
        overallEvaluation: [{ required: true, message: '请填写体系评价', trigger: 'blur' }],
      },
      operationType: '',
      personList: []
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
      this.getAuthorizedPerson()
      if (type !== 'add') {
        this.form = row
        this.form.attendess = this.form.attendess ? this.form.attendess.split(',').map(m => Number(m)) : []
      }
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          const internalMeeting = this.HaveJson(this.form)
          internalMeeting.attendess = internalMeeting.attendess ? internalMeeting.attendess.join(',') : ''
          if (this.operationType === 'add') {
            addReviewReport(internalMeeting).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeYearDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            modifyReviewReport(internalMeeting).then(res => {
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
    closeYearDia() {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeYearDia')
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
  margin: 4vh auto 50px !important;
}

>>>.el-dialog__body {
  max-height: 600px;
  overflow-y: auto;
}
</style>
