<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :title="operationType === 'edit' ? '编辑' : '新增'"
               :visible.sync="formDia"
               width="90%" @close="closeDia">
      <el-form :model="model" ref="modelForm" label-width="150px" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierName">
              <el-input v-model="model.supplierName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编号" prop="supplierRef">
              <el-input v-model="model.supplierRef" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="供应商物品服务名称" prop="supplierItemServiceName">
              <el-input v-model="model.supplierItemServiceName" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮编">
              <el-input v-model="model.postalCode" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址">
              <el-input v-model="model.adress" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="model.contacts" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="model.phone" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="户名">
              <el-input v-model="model.householdName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传真">
              <el-input v-model="model.fax" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开户行">
              <el-input v-model="model.openingName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网址">
              <el-input v-model="model.website" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="账号">
              <el-input v-model="model.accountName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Email">
              <el-input v-model="model.email" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer">
            <el-button @click="closeDia">取 消</el-button>
            <el-button type="primary" @click="submit" :loading="editLoad">保 存</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addSupplierManagement,
  updateSupplierManagement
} from '@/api/cnas/externalService/supplierManage/supplierManage'

export default {
  name: "formDia",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      operationType: '',
      formDia: false,
      model: {
        supplierName: '',
        supplierRef: '',
        supplierItemServiceName: '',
        postalCode: '',
        adress: '',
        contacts: '',
        phone: '',
        householdName: '',
        fax: '',
        openingName: '',
        website: '',
        accountName: '',
        email: '',
        supplierManagementId: '',
      },
      rules: {
        supplierName: [{ required: true, message: '请输入供应商', trigger: 'blur' }],
        supplierRef: [{ required: true, message: '请输入供应商编号', trigger: 'blur' }],

      },
      editLoad: false,
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDialog (type, row) {
      this.operationType = type
      this.formDia = true
      if (this.operationType === 'edit') {
        this.model = this.HaveJson(row)
      }
    },
    // 提交表单
    submit () {
      this.$refs.modelForm.validate((valid) => {
        if (valid) {
          this.editLoad = true
          if (this.operationType === 'edit') {
            updateSupplierManagement(this.model).then(res => {
              this.editLoad = false
              if (res.code === 200){
                this.$message.success('修改成功')
                this.closeDia()
              }
            }).catch(err => {
              console.log('err---', err);
              this.editLoad = false
            })
          } else {
            addSupplierManagement(this.model).then(res => {
              this.editLoad = false
              if (res.code === 200){
                this.$message.success('新增成功')
                this.closeDia()
              }
            }).catch(err => {
              console.log('err---', err);
              this.editLoad = false
            })
          }
        }
      })
    },
    closeDia () {
      this.$refs.modelForm.resetFields()
      this.formDia = false
      this.$emit('closeDia')
    }
  },
}
</script>

<style scoped>
</style>
