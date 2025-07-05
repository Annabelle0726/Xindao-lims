<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :title="operationType === 'edit'? '编辑' : '新增'"
               :visible.sync="formDia"
               width="80%" @close="closeDia">
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-col :span="12">
          <el-form-item label="监督日期" prop="superviseTime">
            <el-date-picker
              v-model="form.superviseTime"
              clearable
              format="yyyy.M"
              placeholder="选择日期"
              size="small"
              style="width: 100%"
              type="month"
              value-format="yyyy.M">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监督目的" prop="supervisePurpose">
            <el-input v-model="form.supervisePurpose" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控项目" prop="superviseProject">
            <el-input v-model="form.superviseProject" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被监督人员" prop="supervisedUserId">
            <el-select @change="usePersonName" v-model="form.supervisedUserId" placeholder="请选择" size="small" style="width: 100%">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监督原因" prop="superviseReason">
            <el-input v-model="form.superviseReason" clearable disabled size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
					<el-button @click="closeDia">取 消</el-button>
					<el-button :loading="upLoad" type="primary" @click="submitForm">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addQualitySuperviseDetail,
  updateQualitySuperviseDetail
} from "@/api/cnas/process/ensureResults/qualitySupervise";
import {selectUserCondition} from "@/api/business/inspectionTask";

export default {
  name: 'detailFormDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: ['superviseId'],
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        qualityMonitorDetailsId: '',
        superviseTime: '',
        supervisePurpose: '',
        superviseProject: '',
        supervisee: '',
        supervisedUserId: '',
        superviseReason: '',
        remark: '',
        superviseDetailsId: '',
      },
      rules: {
        superviseTime: [{ required: true, message: '请输入监督日期', trigger: 'blur' }],
        supervisePurpose: [{ required: true, message: '请输入监督目的', trigger: 'blur' }],
        superviseProject: [{ required: false, message: '请输入监控项目', trigger: 'blur' }],
      },
      upLoad: false,
      operationType: '',
      userList: [],
    };
  },
  // 方法集合
  methods: {
    openDia(type, row, causeType) {
      this.formDia = true
      this.operationType = type
      this.getUserList()
      if (causeType == 1) {
        this.form.superviseReason = '定期监督'
      } else {
        this.form.superviseReason = '动态监督'
      }
      if (type === 'edit') {
        this.searchInfo(row)
      }
    },
    usePersonName (value) {
      const index = this.userList.findIndex(item => item.id === value)
      if (index > -1) {
        this.form.supervisee = this.userList[index].name
      }
    },
    searchInfo (row) {
      this.form = {...row}
    },
    // 提交表单
    submitForm () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.operationType === 'add') {
            this.handleAdd()
          } else {
            this.handleEdit()
          }
        }
      })
    },
    // 提交新增
    handleAdd () {
      let entity = this.HaveJson(this.form)
      entity.superviseId = this.superviseId
      this.upLoad = true
      addQualitySuperviseDetail(entity).then(res => {
        this.upLoad = false
        this.$message.success('新增成功')
        this.closeDia()
      }).catch(err => {
        console.log('err---', err);
        this.upLoad = false
      })
    },
    // 提交修改
    handleEdit () {
      const entity = this.HaveJson(this.form)
      this.upLoad = true
      updateQualitySuperviseDetail(entity).then(res => {
        this.upLoad = false
        this.$message.success('修改成功')
        this.closeDia()
      }).catch(err => {
        console.log('err---', err);
        this.upLoad = false
      })
    },
    // 关闭弹框
    closeDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeDia')
    },
    getUserList(){
      selectUserCondition({ type: 2 }).then((res) => {
        this.userList = res.data;
      })
    },
  }
};
</script>

<style scoped>
>>>.el-dialog__body {
  max-height: 720px;
  overflow-y: auto;
}
</style>
