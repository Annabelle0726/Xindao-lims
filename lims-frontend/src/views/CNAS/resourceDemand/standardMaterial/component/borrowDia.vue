<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :visible.sync="formDia"
               title="标准物质借用"
               width="80%" @close="closeBorrowDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="right" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准物质名称">
              <el-input v-model="form.name" disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号">
              <el-input v-model="form.model" disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完好性" prop="integrity">
              <el-input v-model="form.integrity" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借用人" prop="borrowUser">
              <el-input v-model="form.borrowUser" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="phone">
              <el-input v-model="form.phone" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借出日期" prop="borrowDate">
              <el-date-picker
                v-model="form.borrowDate"
                clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="约定归还日期" prop="borrowReturnDate">
              <el-date-picker
                v-model="form.borrowReturnDate"
                clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借出人" prop="lender">
              <el-input v-model="form.lender" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeBorrowDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  borrowSubstance
} from "@/api/cnas/resourceDemand/standardMaterial/standardMaterial";

export default {
  name: 'borrowDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        name: '',
        model: '',
        integrity: '',
        borrowUser: '',
        phone: '',
        borrowDate: '',
        borrowReturnDate: '',
        lender: '',
        substanceId: ''
      },
      rules: {
        integrity: [{required: true, message: '请填写完好性',trigger: 'blur'}],
        borrowUser: [{required: true, message: '请填写借用人',trigger: 'blur'}],
        phone: [{required: true, message: '请填写联系方式',trigger: 'blur'}],
        borrowDate: [{required: true, message: '请选择借出日期',trigger: 'change'}],
        borrowReturnDate: [{required: true, message: '请选择约定归还日期',trigger: 'change'}],
        lender: [{required: true, message: '请选择lender',trigger: 'blur'}],
      },
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia (row) {
      this.formDia = true
      this.form.name = row.name
      this.form.model = row.model
      this.form.substanceId = row.id
    },
    // 提交弹框数据
    handleEdit () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.form.status = '0';
          borrowSubstance(this.form).then(res =>{
            this.loading = false
            if (res.code === 200) {
              this.$message.success('操作成功')
              this.closeBorrowDia()
            }
          })
        } else {
          return false;
        }
      });
    },
    closeBorrowDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeBorrowDia')
    },
  }
};
</script>

<style scoped>
</style>
