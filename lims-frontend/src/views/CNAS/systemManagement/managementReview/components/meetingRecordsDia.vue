<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="管理评审会议记录" width="1000px" @close="closeYearDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="时间" prop="meetingTime">
              <el-date-picker v-model="form.meetingTime" clearable format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
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
            <el-form-item label="会议内容摘要" prop="content">
              <el-input v-model="form.content" clearable size="small"></el-input>
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
  addMeeting,
  modifyMeeting,
} from '@/api/cnas/systemManagement/managementReview.js'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
export default {
  name: 'meetingRecordsDia',
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
        meetingTime: '',
        compere: '',
        place: '',
        content: '',
        participant: [],
      },
      rules: {
        meetingTime: [{ required: true, message: '请填写会议时间', trigger: 'blur' }],
        compere: [{ required: true, message: '请填写主持人', trigger: 'blur' }],
        place: [{ required: true, message: '请填写地点', trigger: 'blur' }],
        content: [{ required: true, message: '请填写会议内容摘要', trigger: 'blur' }],
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
      this.operationType = type
      this.getAuthorizedPerson()
      if (type !== 'add') {
        this.form = row
        this.form.participant = row.participant.split(',').map(a => {
          a = Number(a)
          return a
        })
      }
      this.formDia = true
    },
    // 提交弹框数据
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          const internalMeeting = this.HaveJson(this.form)
          internalMeeting.participant = internalMeeting.participant.join(',')
          if (this.operationType === 'add') {
            addMeeting(internalMeeting).then(res => {
              this.loading = false
              this.$message.success('操作成功')
              this.closeYearDia()
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            modifyMeeting(internalMeeting).then(res => {
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

<style scoped></style>
