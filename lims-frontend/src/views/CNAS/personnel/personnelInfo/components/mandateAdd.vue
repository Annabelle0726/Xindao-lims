<template>
  <div class="add">
    <el-dialog
      :title="isEdit ?'编辑附件资料':'新增附件资料'"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-form
        ref="ruleForm"
        :rules="rules"
        :model="form"
        label-width="130px"
        size="medium"
      >
        <el-form-item label="证书编号" prop="certificateNumber">
          <el-input v-model="form.certificateNumber" placeholder="请输入" style="width: 100%">
          </el-input>
        </el-form-item >
        <el-form-item label="被任职人员" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择">
            <el-option
              v-for="(item, index) in userList"
              :key="index"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item >
        <el-form-item label="任职岗位" prop="post">
          <el-input v-model="form.post" placeholder="请输入"></el-input>
        </el-form-item >
        <el-form-item label="理论考试成绩" prop="num1">
          <el-input v-model="form.num1" placeholder="请输入"></el-input>
        </el-form-item >
        <el-form-item label="操作技能考试成绩" prop="num2">
          <el-input v-model="form.num2" placeholder="请输入"></el-input>
        </el-form-item >
        <el-form-item label="操作类型" prop="operationType">
          <el-input v-model="form.operationType" placeholder="请选择"></el-input>
        </el-form-item >
        <el-row>
          <el-col :span="20">
            <el-form-item label="上岗证书">
              <el-input v-model="form.systemFileName" size="small" disabled>
                <el-button slot="append" icon="el-icon-delete-solid"
                           @click="deleteFile(form.systemFileName)"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-upload ref="upload" style="float: left; margin: 0 12px 0 20px;" :action="action"
                       :headers="uploadHeader"
                       :show-file-list="false"
                       :on-success="onSuccess">
              <el-button class="uploadFile" slot="trigger" size="small" type="primary">上传</el-button>
            </el-upload>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remarks" placeholder="请选择"></el-input>
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
import {deleteCNASFile} from "@/api/cnas/personal/personalList";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {addOrUpdatePersonPostAuthorizationRecord} from "@/api/cnas/personal/personPostAuthorizationRecord";

export default {
  data() {
    return {
      isEdit: false,
      dialogVisible: false,
      form: {
        id: undefined,
        certificateNumber: undefined,
        userId: undefined,
        post: undefined,
        operationType: undefined,
        fileList: [],
        fileName: undefined,
        systemFileName: undefined,
        remarks: undefined,
        num2:undefined,
        num1:undefined
      },
      rules: {
        certificateNumber: [{ required: true, message: '请输入证书编号', trigger: 'blur' }],
        userId: [{ required: true, message: '请选择被任职人员', trigger: 'change' }],
        post: [{ required: true, message: '任职岗位', trigger: 'blur' }],
        operationType: [{ required: true, message: '操作类型', trigger: 'blur' }],
      },
      loading: false,
      userList: []
    }
  },
  computed: {
    action() {
      return this.javaApi + '/personBasicInfo/saveCNASFile'
    }
  },
  methods: {
    onSuccess(response, file, fileList) {
      this.$set(this.form, "systemFileName", response.data)
    },
    deleteFile(fileName) {
      deleteCNASFile({fileName: fileName}).then(res => {
        this.$message.success('删除成功！')
        this.$set(this.form, "systemFileName", null)
      })
    },
    async openDialog(row, type) {
      await this.getUserList()
      this.dialogVisible = true
      if(type) {
        this.isEdit = true
        this.form.id = row.id
        this.form.certificateNumber = row.certificateNumber
        this.form.userId = Number(row.userId)
        this.form.post = row.post
        this.form.operationType = row.operationType
        this.form.systemFileName = row.systemFileName
        this.form.remarks = row.remarks
        this.form.num1 = row.num1
        this.form.num2 = row.num2
      } else {
        this.resetForm('ruleForm')
      }
    },
    closeDialog() {
      this.resetForm('ruleForm')
      this.dialogVisible = false
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
    submitForm() {
      this.$refs.ruleForm.validate((valid) => {
        if(valid) {
          const params = {
            id: this.form.id,
            certificateNumber: this.form.certificateNumber,
            userId: this.form.userId,
            post: this.form.post,
            operationType: this.form.operationType,
            systemFileName: this.form.systemFileName,
            remarks: this.form.remarks,
            num1: this.form.num1,
            num2: this.form.num2
          }
          this.loading = true
          addOrUpdatePersonPostAuthorizationRecord(params).then(res => {
            this.loading = false
            this.closeDialog()
            this.$emit('refresh')
          }).catch(err => {
            this.loading = false
          })
        }
      })
    },
    /**
     * @desc 上传文件
     */
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        this.form.fileList = [file]
        this.form.fileName = file.name
        return true;
      }
    },
  }
}
</script>
<style scoped>
.foot {
    width: 100%;
}
.add >>> .el-dialog__footer {
    padding-right: 20px;
}
</style>
