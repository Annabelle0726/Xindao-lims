<template>
  <div>
    <el-dialog :visible.sync="formDia" title="编辑" width="80%" @close="closeDia">
      <el-form ref="form" :model="form" :rules="formRules" label-width="auto">
        <el-col :span="8">
          <el-form-item label="标准名称：" prop="methodName">
            <el-input v-model="form.methodName" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="标准号：" prop="standardNo">
            <el-input v-model="form.standardNo" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="文件编号：" prop="fileNo">
            <el-input v-model="form.fileNo" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否是新标准：" prop="isNewStandard">
            <el-radio-group v-model="form.isNewStandard">
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="查新记录来源：" prop="searchNewSource">
            <el-radio-group v-model="form.searchNewSource">
              <el-radio :label="0">标准网</el-radio>
              <el-radio :label="1">委托情报</el-radio>
              <el-radio :label="2">标准数</el-radio>
              <el-radio :label="3">其他</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="备注：" prop="remark">
            <el-select v-model="form.remark"
                       clearable size="small">
              <el-option :value="0" label="作废"></el-option>
              <el-option :value="1" label="替换"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isNewStandard == 1" :span="8">
          <el-form-item label="新标准名称：" prop="newMethodName">
            <el-input v-model="form.newMethodName" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isNewStandard == 1" :span="8">
          <el-form-item label="新标准号：" prop="newStandardNo">
            <el-input v-model="form.newStandardNo" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  updateMethodSearchNew
} from '@/api/cnas/process/method/standardNoveltyRetrieval'

export default {
  name: 'FormDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        methodName: '',
        standardNo: '',
        isNewStandard: '',
        newMethodName: '',
        newStandardNo: '',
        searchNewSource: '',
        remark: '',
        fileNo: '',
      },
      formRules: {
        methodName: [{required: true, message: '请填写标准名称',trigger: 'blur'}],
        standardNo: [{required: true, message: '请填写标准号',trigger: 'blur'}],
        isNewStandard: [{required: true, message: '请选择是否是新标准',trigger: 'change'}],
        newMethodName: [{required: false, message: '请填写新标准名称',trigger: 'blur'}],
        newStandardNo: [{required: false, message: '请填写新标准号',trigger: 'blur'}],
        searchNewSource: [{required: true, message: '请选择查新记录来源',trigger: 'change'}],
        fileNo: [{required: true, message: '请填写文件号',trigger: 'blur'}],
      },
      editLoad: false,
      info: {},
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.form = {...row}
    },
    handleEdit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.editLoad = true
          const processMethodSearchNews = this.HaveJson(this.form)
          delete processMethodSearchNews.createTime
          updateMethodSearchNew(processMethodSearchNews).then(res => {
            this.editLoad = false
            if (res.code === 200){
              this.$message.success('操作成功')
              this.closeDia()
            }
          }).catch(err => {
            console.log('err---', err);
            this.editLoad = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    closeDia() {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeEditDia');
    },
  }
};
</script>

<style scoped>
.form-info {
  display: inline-block;
  max-height: 600px;
  overflow-y: auto;
}
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}
</style>
