<template>
  <div>
    <el-dialog :visible.sync="formDia" title="新增" width="80%" @close="closeDia">
      <el-form ref="form" :model="form" :rules="formRules" label-width="auto">
        <el-button size="small" type="primary" @click="addBtn">添加一条</el-button>
        <div class="form-info">
          <div v-for="(items,index) in form.taskRel" :key="index">
            <el-col :span="12">
              <el-form-item :prop="`taskRel.${index}.newMethodName`" :rules="{required: true, message: '请填写标准名称',trigger: 'blur',}" label="新标准名称：">
                <el-input v-model="items.newMethodName" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :prop="`taskRel.${index}.newStandardNo`" :rules="{required: true,message: '请填写标准号',trigger: 'blur',}" label="新标准号：">
                <el-input v-model="items.newStandardNo" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :prop="`taskRel.${index}.searchNewSource`" :rules="{required: true,message: '请选择查新记录来源',trigger: 'change',}" label="查新记录来源：">
                <el-radio-group v-model="items.searchNewSource">
                  <el-radio :label="0">标准网</el-radio>
                  <el-radio :label="1">委托情报</el-radio>
                  <el-radio :label="2">标准数</el-radio>
                  <el-radio :label="3">其他</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :prop="`taskRel.${index}.remark`" :rules="{required: false,message: '请选择备注',trigger: 'change',}" label="备注：">
                <el-select v-model="items.remark" :prop="`taskRel.${index}.remark`"
                           :rules="{required: false,message: '请选择备注',trigger: 'change',}"
                           clearable size="small">
                  <el-option :value="0" label="作废"></el-option>
                  <el-option :value="1" label="替换"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button v-if="index !== 0" circle icon="el-icon-delete" size="small" type="danger" @click="deleteHeaderRow(index)"></el-button>
            </el-col>
            <el-col :span="24">
              <el-divider></el-divider>
            </el-col>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addMethodSearchNew } from '@/api/cnas/process/method/standardNoveltyRetrieval'

export default {
  name: 'FormDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        taskRel: [
          {
            newMethodName: '',
            newStandardNo: '',
            searchNewSource: '',
            remark: '',
          }
        ]
      },
      formRules: {},
      editLoad: false,
      info: {},
    };
  },
  // 方法集合
  methods: {
    openDia (row) {
      this.formDia = true
    },
    // 添加一条
    addBtn() {
      this.form.taskRel.push({ newMethodName: '', newStandardNo:'', searchNewSource: '', remark: ''});
    },
    // 删除一条
    deleteHeaderRow (index) {
      this.form.taskRel.splice(index, 1);
    },
    handleEdit () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.editLoad = true
          addMethodSearchNew(this.form.taskRel).then(res => {
            this.editLoad = false
            this.$message.success('操作成功')
            this.closeDia()
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
    closeDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeDia');
    },
  }
};
</script>

<style scoped>
.form-info {
  display: inline-block;
  max-height: 42em;
  overflow-y: auto;
}
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}
</style>
