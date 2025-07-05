<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="内审会议签到表" width="1000px" @close="closeYearDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="时间" prop="meetingDate">
              <el-date-picker v-model="form.meetingDate" clearable format="yyyy-MM-dd HH:mm:ss" placeholder="请选择"
                size="small" style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主持人" prop="compere">
              <el-input v-model="form.compere" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点" prop="place">
              <el-input v-model="form.place" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会议主题" prop="subject">
              <el-input v-model="form.subject" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="参会人员" prop="participant">
              <el-select v-model="form.participant" clearable filterable multiple placeholder="请选择" size="small"
                style="width: 100%;">
                <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
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
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  getInternalMeetingOne,
  addInternalMeeting,
  updateInternalMeeting,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'auditMeetingSignDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        meetingDate: '',
        compere: '',
        place: '',
        subject: '',
        participant: [],
      },
      rules: {
        meetingDate: [{ required: true, message: '请填写会议时间', trigger: 'blur' }],
        compere: [{ required: true, message: '请填写主持人', trigger: 'blur' }],
        place: [{ required: true, message: '请填写地点', trigger: 'blur' }],
        subject: [{ required: true, message: '请填写会议主题', trigger: 'blur' }],
        participant: [{ required: true, message: '请选择参加人员', trigger: 'change' }],
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
        this.searchInfo(row)
      }
    },
    // 查询详情
    searchInfo(row) {
      this.diaLoading = true
      getInternalMeetingOne({ meetingId: row.meetingId }).then(res => {
        this.diaLoading = false
        this.form = res.data
        this.form.participant = this.form.participant.split(',').map(Number)
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
          const internalMeeting = this.HaveJson(this.form)
          internalMeeting.participant = internalMeeting.participant.join(',')
          if (this.operationType === 'add') {
            addInternalMeeting(internalMeeting).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeYearDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateInternalMeeting(internalMeeting).then(res => {
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
.table {
  table-layout: fixed;
  width: 100%;
  margin-top: 10px;
}

.table td {
  height: 34px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 4px;
}
</style>
