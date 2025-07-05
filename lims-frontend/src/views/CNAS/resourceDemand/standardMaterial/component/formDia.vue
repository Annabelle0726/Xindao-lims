<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :visible.sync="formDia"
               title="标准物质"
               width="80%" @close="closeYearDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="right" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准物质名称" prop="name">
              <el-input v-model="form.name" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="model">
              <el-input v-model="form.model" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产厂家" prop="factoryManufacturer">
              <el-input v-model="form.factoryManufacturer" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出厂编号" prop="factoryNum">
              <el-input v-model="form.factoryNum" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理编号" prop="manageNum">
              <el-input v-model="form.manageNum" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="不确定度" prop="uncertainty">
              <el-input v-model="form.uncertainty" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量" prop="quantity">
              <el-input v-model="form.quantity" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购置日期" prop="acquisitionDate">
              <el-date-picker
                v-model="form.acquisitionDate"
                clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期" prop="effectiveDate">
              <el-date-picker
                v-model="form.effectiveDate"
                clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="档案编号" prop="fileNum">
              <el-input v-model="form.fileNum" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="存放位置" prop="position">
              <el-input v-model="form.position" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" clearable size="small"></el-input>
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
  addStandardSubstance
} from "@/api/cnas/resourceDemand/standardMaterial/standardMaterial";

export default {
  name: 'formDia',
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
        factoryManufacturer: '',
        factoryNum: '',
        manageNum: '',
        uncertainty: '',
        quantity: '',
        acquisitionDate: '',
        effectiveDate: '',
        fileNum: '',
        position: '',
        remark: '',
        id: '',
      },
      rules: {
        name: [{required: true, message: '请填写标准物质名称',trigger: 'blur'}],
        model: [{required: true, message: '请填写规格型号',trigger: 'blur'}],
        factoryManufacturer: [{required: true, message: '请填写生产厂家',trigger: 'blur'}],
        factoryNum: [{required: true, message: '请填写出厂编号',trigger: 'blur'}],
        manageNum: [{required: true, message: '请填写管理编号',trigger: 'blur'}],
        uncertainty: [{required: true, message: '请填写不确定度',trigger: 'blur'}],
        quantity: [{required: true, message: '请填写数量',trigger: 'blur'}],
        acquisitionDate: [{required: true, message: '请选择购置日期',trigger: 'change'}],
        effectiveDate: [{required: true, message: '请选择有效期',trigger: 'change'}],
        fileNum: [{required: true, message: '请填写档案编号',trigger: 'blur'}],
        position: [{required: true, message: '请填写存放位置',trigger: 'blur'}],
      },
      operationType: '',
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia (type, row) {
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.form = row
      }
    },
    // 提交弹框数据
    handleEdit () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          addStandardSubstance(this.form).then(res => {
            this.loading = false
            this.$message.success('操作成功')
            this.closeYearDia()
          }).catch(err => {
            console.log('err---', err);
            this.loading = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    closeYearDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeYearDia')
    },
  }
};
</script>

<style scoped>
</style>
