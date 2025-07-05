<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="formDia" title="客户满意度调查表"
      width="70%" @close="closeFormDia">
      <table border="1" cellspacing="10" class="tables">
        <tr>
          <td class="td-title">
            <p>单位名称：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-input v-model="form.unitName" placeholder="请输入内容" size="small">
            </el-input>
          </td>
          <td class="td-title">
            <p>日期：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-date-picker v-model="form.fillDate" format="yyyy-MM-dd" placeholder="选择日期" size="small" type="date"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>姓名：</p>
          </td>
          <td class="td-info">
            <el-input v-model="form.userName" placeholder="请输入内容" size="small">
            </el-input>
          </td>
          <td class="td-title">
            <p>部门：</p>
          </td>
          <td class="td-info">
            <el-input v-model="form.department" placeholder="请输入内容" size="small">
            </el-input>
          </td>
          <td class="td-title">
            <p>联系电话：</p>
          </td>
          <td class="td-info">
            <el-input v-model="form.contactNumber" placeholder="请输入内容" size="small">
            </el-input>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>服务态度：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-radio-group v-model="form.serviceAttitude" v-removeAriaHidden>
              <el-radio :label="0">满意</el-radio>
              <el-radio :label="1">一般</el-radio>
              <el-radio :label="2">不满意</el-radio>
            </el-radio-group>
          </td>
          <td class="td-title">
            <p>建议：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-input v-model="form.serviceAttitudeSuggestion" placeholder="请输入内容" size="small">
            </el-input>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>技术能力：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-radio-group v-model="form.technicalCompetence" v-removeAriaHidden>
              <el-radio :label="0">满意</el-radio>
              <el-radio :label="1">一般</el-radio>
              <el-radio :label="2">不满意</el-radio>
            </el-radio-group>
          </td>
          <td class="td-title">
            <p>建议：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-input v-model="form.technicalCompetenceSuggestion" placeholder="请输入内容" size="small">
            </el-input>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>检测工作：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-radio-group v-model="form.inspectionWork" v-removeAriaHidden>
              <el-radio :label="0">满意</el-radio>
              <el-radio :label="1">一般</el-radio>
              <el-radio :label="2">不满意</el-radio>
            </el-radio-group>
          </td>
          <td class="td-title">
            <p>建议：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-input v-model="form.inspectionWorkSuggestion" placeholder="请输入内容" size="small">
            </el-input>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>收费合理性：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-radio-group v-model="form.reasonableFees" v-removeAriaHidden>
              <el-radio :label="0">满意</el-radio>
              <el-radio :label="1">一般</el-radio>
              <el-radio :label="2">不满意</el-radio>
            </el-radio-group>
          </td>
          <td class="td-title">
            <p>建议：</p>
          </td>
          <td class="td-info" colspan="2">
            <el-input v-model="form.reasonableFeesSuggestion" placeholder="请输入内容" size="small">
            </el-input>
          </td>
        </tr>
        <tr>
          <td class="td-title">
            <p>您对我们的希望：</p>
          </td>
          <td class="td-info" colspan="5">
            <el-input v-model="form.remark" :rows="4" placeholder="请输入内容" size="small" type="textarea">
            </el-input>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeFormDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addClientSatisfaction,
  updateClientSatisfaction
} from '@/api/cnas/systemManagement/customerSatisfaction.js'
export default {
  name: 'formDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        unitName: '',
        fillDate: '',
        userName: '',
        department: '',
        contactNumber: '',
        serviceAttitude: '',
        technicalCompetence: '',
        technicalCompetenceSuggestion: '',
        inspectionWork: '',
        inspectionWorkSuggestion: '',
        reasonableFees: '',
        reasonableFeesSuggestion: '',
        remark: '',
        clientSatisfactionId: '',
      },
      operationType: '',
      editLoad: false,

    };
  },
  // 方法集合
  methods: {
    openDia(type, row) {
      this.formDia = true;
      this.operationType = type
      if (this.operationType === 'edit') {
        this.form = { ...row }
      }
    },
    handleEdit() {
      if (!this.form.unitName) {
        this.$message.warning('请填写单位名称')
        return
      }
      if (!this.form.department) {
        this.$message.warning('请填写部门')
        return
      }
      this.editLoad = true
      if (this.operationType === 'add') {
        addClientSatisfaction(this.form).then(res => {
          this.editLoad = false
          this.$message.success('提交成功')
          this.closeFormDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      } else {
        updateClientSatisfaction(this.form).then(res => {
          this.editLoad = false
          this.$message.success('提交成功')
          this.closeFormDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      }
    },
    closeFormDia() {
      this.formDia = false;
      this.$emit('closeFormDia')
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 10vh auto 50px !important;
}

.tables {
  table-layout: fixed;
  width: 100%;
  margin-top: 10px;
}

.td-title {
  height: 40px;
  width: 170px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 6px;
}

.td-info {
  padding: 6px;
}

.td-info1 {
  display: inline-block;
  width: 100%;
  text-align: left;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}
</style>
