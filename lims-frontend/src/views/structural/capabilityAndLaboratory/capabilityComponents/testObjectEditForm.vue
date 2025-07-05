<template>
  <div>
    <el-dialog :title="operationType === 'add' ? '新增' : '编辑'" :visible.sync="editFormDia" width="500px" @close="closeDia">
      <el-form ref="editForm" :model="editForm" :rules="editFormRules" label-width="120px" label-position="right">
        <el-form-item label="场所：" prop="laboratoryId">
          <el-select v-model="editForm.laboratoryId" clearable placeholder="请选择" size="small" style="width: 100%">
            <el-option v-for="item in laboratoryList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="检验对象：" prop="specimenName">
          <el-input v-model="editForm.specimenName" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="检验对象EN：" prop="specimenNameEn">
          <el-input v-model="editForm.specimenNameEn" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="对象代号：" prop="code">
          <el-input v-model="editForm.code" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="对象类型：" prop="objectType">
          <el-select v-model="editForm.objectType" clearable placeholder="请选择" size="small" style="width: 100%">
            <el-option v-for="item in dict.type.object_type" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {obtainItemParameterList} from "@/api/structural/laboratoryScope";
import {addTestObject, upTestObject} from "@/api/structural/capability";

export default {
  name: "EditForm",
  // import 引入的组件需要注入到对象中才能使用
  dicts: ['object_type'],
  components: {},
  data() {
    // 这里存放数据
    return {
      editFormDia: false,
      editLoad: false,
      editForm: {
        id: '',
        laboratoryId: '', // 场所
        specimenName: '', // 检验对象
        specimenNameEn: '', // 检验对象EN
        code: '', // 对象代号
        objectType: '', // 对象类型
      },
      laboratoryList: [],
      editFormRules: {
        laboratoryId: [
          { required: true, message: '请选择场所', trigger: 'change' }
        ],
        specimenName: [
          { required: true, message: '请输入检验对象', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入对象代号', trigger: 'blur' }
        ],
      },
      operationType: ''
    }
  },
  // 方法集合
  methods: {
    openDia (type, row) {
      this.operationType = type
      this.obtainItemParameterList()
      this.editFormDia = true
      if (type === 'add') {
        this.resetForm("editForm");
      } else {
        this.editForm = {...row}
      }
    },
    // 提交编辑
    handleEdit () {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.editLoad = true
          if(this.editForm.id){
            // 修改
            upTestObject(this.editForm).then(res => {
              this.editLoad = false
              if (res.code === 500) {
                return
              }
              this.$message.success('修改成功')
              this.closeDia()
              this.$emit('refreshList')
            }).catch(e => {
              this.editLoad = false
            })
          }else{
            // 新增
            addTestObject(this.editForm).then(res => {
              this.editLoad = false
              if (res.code === 500) {
                return
              }
              this.$message.success('添加成功')
              this.closeDia()
              this.$emit('refreshList')
            }).catch(e => {
              this.editLoad = false
            })
          }
        }
      })
    },
    // 关闭弹框
    closeDia () {
      this.editFormDia = false
      this.resetForm("editForm");
    },
    // 获取场所下拉框的值
    obtainItemParameterList() {
      obtainItemParameterList().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.laboratoryName,
            value: a.id
          })
        })
        this.laboratoryList = data
      })
    },
  },
}
</script>

<style scoped>

</style>
