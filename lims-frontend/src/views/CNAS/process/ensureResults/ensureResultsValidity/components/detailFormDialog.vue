<template>
  <div>
    <el-dialog title="编辑" :visible.sync="formDia"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               width="80%" @close="closeDia">
      <el-form :model="form" :rules="rules" ref="form" label-width="140px">
        <el-col :span="12">
          <el-form-item label="监控目的" prop="monitorPurpose">
            <el-input v-model="form.monitorPurpose" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划开展时间" prop="plannedTime">
            <el-input v-model="form.plannedTime" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控项目" prop="monitorProject">
            <el-input v-model="form.monitorProject" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参加单位（人员）" prop="participant">
            <el-input v-model="form.participant" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算（元）" prop="budget">
            <el-input v-model="form.budget" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织人员" prop="organization">
            <el-input v-model="form.organization" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控方式" prop="monitorWay">
            <el-input v-model="form.monitorWay" size="small" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
					<el-button @click="closeDia">取 消</el-button>
					<el-button type="primary" @click="submitForm" :loading="upLoad">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addQualityMonitorDetail,
  updateQualityMonitorDetail
} from "@/api/cnas/process/ensureResults/qualityMonitor";

export default {
  name: 'detailFormDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: ['qualityMonitorId'],
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        qualityMonitorDetailsId: '',
        monitorPurpose: '',
        plannedTime: '',
        monitorProject: '',
        participant: '',
        budget: '',
        organization: '',
        monitorWay: '',
      },
      rules: {
        monitorPurpose: [{ required: true, message: '请输入监控目的', trigger: 'blur' }],
        monitorProject: [{ required: true, message: '请输入监控项目', trigger: 'blur' }],
      },
      upLoad: false,
      operationType: '',
    };
  },
  // 方法集合
  methods: {
    openDia(type, row) {
      this.formDia = true
      this.operationType = type
      if (type === 'edit') {
        this.searchInfo(row)
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
      entity.qualityMonitorId = this.qualityMonitorId
      this.upLoad = true
      addQualityMonitorDetail(entity).then(res => {
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
      updateQualityMonitorDetail(entity).then(res => {
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
  }
};
</script>

<style scoped>
>>>.el-dialog__body {
  max-height: 720px;
  overflow-y: auto;
}
</style>
