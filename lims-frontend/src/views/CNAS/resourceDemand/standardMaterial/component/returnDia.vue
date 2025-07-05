<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :visible.sync="formDia"
               title="标准物质归还"
               width="80%" @close="closeReturnDia">
      <el-form ref="form" :model="form" :rules="rules" label-position="right" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准物质名称" prop="name">
              <el-input v-model="form.name" disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归还日期" prop="returnDate">
              <el-date-picker
                v-model="form.returnDate"
                clearable
                format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归还人" prop="returnedPerson">
              <el-input v-model="form.returnedPerson" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完好性" prop="returnIntegrity">
              <el-input v-model="form.returnIntegrity" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查人" prop="rummager">
              <el-input v-model="form.rummager" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeReturnDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  returnSubstance
} from "@/api/cnas/resourceDemand/standardMaterial/standardMaterial";

export default {
  name: 'returnDia',
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
        returnedPerson: '',
        returnDate: '',
        returnIntegrity: '',
        rummager: '',
      },
      rules: {
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
          this.form.status = '1';
          returnSubstance(this.form).then(res => {
            this.loading = false
            if (res.code === 200) {
              this.$message.success('操作成功')
              this.closeReturnDia()
            }
          })
          let internalReport = this.HaveJson(this.form)

        } else {
          return false;
        }
      });
    },
    closeReturnDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeReturnDia')
    },
  }
};
</script>

<style scoped>
</style>
