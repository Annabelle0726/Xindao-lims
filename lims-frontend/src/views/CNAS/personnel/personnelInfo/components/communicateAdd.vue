<template><div class="add">
  <el-dialog
    :title="isEdit ?'编辑附件资料':'新增附件资料'"
    :visible.sync="dialogVisible"
    width="40%"
  >
    <el-form
      :model="form"
      ref="form"
      :rules="rules"
      label-width="80px"
      size="small"
    >
      <el-form-item label="沟通人" prop="userId">
        <el-select
          v-model="form.userId"
          placeholder="请选择"
          style="width: 100%" multiple
        >
          <el-option
            v-for="(item, index) in userList"
            :key="index"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="沟通时间" prop="communicationTime">
        <el-date-picker
          v-model="form.communicationTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择日期时间"
          style="width: 100%"
        >
        </el-date-picker>
      </el-form-item >
      <el-form-item label="沟通地点" prop="communicationPlace">
        <el-input v-model="form.communicationPlace" placeholder="请填写沟通地点"></el-input>
      </el-form-item >
      <el-form-item label="沟通内容" prop="communicationContent">
        <el-input v-model="form.communicationContent" placeholder="请填写沟通内容"></el-input>
      </el-form-item >
    </el-form>
    <div slot="footer" class="foot">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="submitForm">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>
<script>
import {selectUserCondition} from "@/api/business/inspectionTask";
import {addOrUpdatePersonCommunicationAbility} from "@/api/cnas/personal/personPersonCommunicationAbilityPage";

export default {
  data() {
    return {
      isEdit: false,
      dialogVisible: false,
      form: {
        id: '',
        userId: [],
        communicationTime: undefined,
        communicationPlace: undefined,
        communicationContent: undefined
      },
      rules: {
        userId: [{ required: true, message: '请选择沟通人', trigger: 'change' }],
        communicationTime: [{ required: true, message: '请选择沟通时间', trigger: 'change' }],
        communicationPlace: [{ required: true, message: '请填写沟通地点', trigger: 'blur' }],
        communicationContent: [{ required: true, message: '请填写沟通内容', trigger: 'blur' }],
      },
      loading: false,
      userList: []
    }
  },
  methods: {
    /**
     * @desc 显示模态框
     * @param {*} row
     * @param {*} type
     */
    openDialog(row, type) {
      this.getUserList()
      this.dialogVisible = true
      if(type) {
        this.isEdit = true
        this.form.id = row.id
        this.form.userId = row.userId.split(',').map(m=>Number(m))
        this.form.communicationTime = row.communicationTime
        this.form.communicationPlace = row.communicationPlace
        this.form.communicationContent = row.communicationContent
      } else {
        this.form.id = ''
        this.resetForm('form')
      }
    },
    /**
     * @desc 获取用户信息
     */
    async getUserList() {
      selectUserCondition({type: 2}).then((res) => {
        this.userList = res.data;
      })
    },
    /**
     * @desc 提交表单
     */
    async submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          const params = {
            id: this.form.id,
            userId: this.form.userId.join(','),
            communicationTime: this.form.communicationTime,
            communicationPlace: this.form.communicationPlace,
            communicationContent: this.form.communicationContent,
          }
          addOrUpdatePersonCommunicationAbility(params).then((res) => {
            this.loading = false
            this.resetForm('form')
            this.$emit('submit')
            this.dialogVisible = false
          }).catch((err) => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>
<style scoped>
</style>
