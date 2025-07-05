<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :title="operationType === 'edit' ? '编辑' : '新增'"
               :visible.sync="formDia"
               width="90%" @close="closeDia">
      <div v-if="operationType === 'edit'" style="text-align: right">
        <el-button :disabled="form.confirmDate" size="small" type="primary" @click="validation">验证确认</el-button>
      </div>
      <table border="1" cellspacing="10" class="tables">
        <tr>
          <td>
            <p>标准方法</p>
          </td>
          <td>
            <p>验证原因</p>
          </td>
          <td>
            <p>主要技术变化</p>
          </td>
          <td>
            <p>涉及方面</p>
          </td>
          <td>
            <p>标准要求</p>
          </td>
          <td>
            <p>准备情况</p>
          </td>
          <td>
            <p>是否满足</p>
          </td>
          <td>
            <p>备注</p>
          </td>
        </tr>
        <tr>
          <td rowspan="9">
            <el-input v-model="form.methodName" :rows="6"
                      placeholder="请输入内容"
                      size="small"
                      type="textarea">
            </el-input>
          </td>
          <td rowspan="9">
            <el-input v-model="form.verifyReason" :rows="6"
                      placeholder="请输入内容"
                      size="small"
                      type="textarea">
            </el-input>
          </td>
          <td rowspan="9">
            <el-input v-model="form.technologyChange" :rows="6"
                      placeholder="请输入内容"
                      size="small"
                      type="textarea">
            </el-input>
          </td>
        </tr>
        <tr>
          <td>人：</td>
          <td>
            <el-input v-model="form.personRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.personReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.personIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-link type="primary" @click="viewWorkPermit">查看上岗证</el-link>
          </td>
        </tr>
        <tr>
          <td>机：</td>
          <td>
            <el-input v-model="form.machineRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.machineReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.machineIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-link type="primary" @click="viewDevice">查看设备</el-link>
          </td>
        </tr>
        <tr>
          <td>料：</td>
          <td>
            <el-input v-model="form.materialRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.materialReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.materialIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-input v-model="form.materialRemark" size="small"></el-input>
          </td>
        </tr>
        <tr>
          <td>法：</td>
          <td>
            <el-input v-model="form.methodRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.methodReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.methodIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-link type="primary" @click="viewTestRecord">查看检测记录</el-link>
          </td>
        </tr>
        <tr>
          <td>环：</td>
          <td>
            <el-input v-model="form.environmentRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.environmentReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.environmentIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-input v-model="form.traceabilityRemark" size="small"></el-input>
          </td>
        </tr>
        <tr>
          <td>测量溯源性：</td>
          <td>
            <el-input v-model="form.traceabilityRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.traceabilityReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.traceabilityIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-link type="primary" @click="viewCalibrationsFileDia">查看校准证书</el-link>
          </td>
        </tr>
        <tr>
          <td>样品管理需求：</td>
          <td>
            <el-input v-model="form.managementRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.managementReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.managementIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-input v-model="form.managementRemark" size="small"></el-input>
          </td>
        </tr>
        <tr>
          <td>其他：</td>
          <td>
            <el-input v-model="form.otherRequirements" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-input v-model="form.otherReadiness" size="small" type="textarea"></el-input>
          </td>
          <td>
            <el-radio-group v-model="form.otherIsSatisfied" v-removeAriaHidden>
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </td>
          <td>
            <el-input v-model="form.otherRemark" size="small"></el-input>
          </td>
        </tr>
        <tr>
          <td colspan="3">
            <p>是否引用此标准开展检测:</p>
          </td>
          <td colspan="3">可以引用此标准开展检测</td>
          <td>
            <p>确认时间:</p>
          </td>
          <td>{{form.confirmDate}}</td>
        </tr>
        <tr>
          <td colspan="3">
            <p>参加确认人签名:</p>
          </td>
          <td colspan="5">
            <el-select v-model="form.confirmUser" multiple placeholder="请选择" size="small" style="width: 100%" :multiple-limit="5">
              <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
    <ViewDeviceDialog v-if="viewDeviceDialog" ref="viewDeviceDialog" @closDeviceDia="closDeviceDia" @handleDeviceInfo="handleDeviceInfo"></ViewDeviceDialog>
    <ViewTestRecord v-if="viewTestRecordDialog" ref="viewTestRecordDialog"></ViewTestRecord>
    <ViewWorkPermitDia v-if="viewWorkPermitDia" ref="viewWorkPermitDia"></ViewWorkPermitDia>
    <calibrations-file-dia v-if="calibrationsFileDia" ref="calibrationsFileDia"></calibrations-file-dia>
  </div>
</template>

<script>
import { dateFormat } from '@/utils/date'
import ViewDeviceDialog from '../../standardMethodsChange/component/ViewDeviceDialog.vue';
import ViewTestRecord from '../../standardMethodsChange/component/ViewTestRecord.vue';
import ViewWorkPermitDia from '../../standardMethodsChange/component/viewWorkPermitDia.vue';
import CalibrationsFileDia from '../../standardMethodsChange/component/calibrationsFileDia.vue';
import {
  addMethodVerify,
  getMethodVerifyOne,
  methodVerifyAffirm,
  updateMethodVerify
} from '@/api/cnas/process/method/methodVerification'
import { selectUserCondition } from '@/api/cnas/process/method/standardMethodsChange'
export default {
  name: 'formDIa',
  // import 引入的组件需要注入到对象中才能使用
  components: { CalibrationsFileDia, ViewWorkPermitDia, ViewTestRecord, ViewDeviceDialog },
  props: {
    operationType: {
      type: String,
      default: () => ''
    }
  },
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        methodName: '',
        verifyReason: '',
        technologyChange: '',
        personRequirements: '',
        personReadiness: '',
        personIsSatisfied: '',
        personRemark: '',
        machineRequirements: '',
        machineReadiness: '',
        machineIsSatisfied: '',
        materialRequirements: '',
        materialReadiness: '',
        materialIsSatisfied: '',
        materialRemark: '',
        methodRequirements: '',
        methodReadiness: '',
        methodIsSatisfied: '',
        environmentRequirements: '',
        environmentReadiness: '',
        environmentIsSatisfied: '',
        traceabilityRequirements: '',
        traceabilityReadiness: '',
        traceabilityIsSatisfied: '',
        traceabilityRemark: '',
        managementRequirements: '',
        managementReadiness: '',
        managementIsSatisfied: '',
        managementRemark: '',
        otherRequirements: '',
        otherReadiness: '',
        otherIsSatisfied: '',
        otherRemark: '',
        machineAttachmentList: []
      },
      editLoad: false,
      info: {
        methodVerifyId: ''
      },
      userList: [],
      viewDeviceDialog: false,
      viewTestRecordDialog: false,
      viewWorkPermitDia: false,
      calibrationsFileDia: false,
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.info = row
      this.getUserList()
      if (this.operationType === 'edit') {
        this.searchInfo(row)
      }
    },
    // 查询详情信息
    searchInfo (row) {
      getMethodVerifyOne({methodVerifyId:row.methodVerifyId}).then(res => {
        if (res.code === 200){
          this.form = {...res.data}
          if (this.form.confirmUser) {
            this.form.confirmUser = this.form.confirmUser.split(',').map(Number)
          }
        }
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit() {
      this.editLoad = true
      const processMethodSearchNews = this.HaveJson(this.form)
      processMethodSearchNews.confirmUser = processMethodSearchNews.confirmUser && processMethodSearchNews.confirmUser.join(',')
      processMethodSearchNews.operationType = 1
      if (this.operationType === 'edit') {
        this.editInfo(processMethodSearchNews)
      } else {
        this.addInfo(processMethodSearchNews)
      }
    },
    // 查看上岗证
    viewWorkPermit () {
      this.viewWorkPermitDia = true
      this.$nextTick(() => {
        this.$refs.viewWorkPermitDia.openDia(this.form)
      })
    },
    // 查看校准证书
    viewCalibrationsFileDia () {
      this.calibrationsFileDia = true
      this.$nextTick(() => {
        this.$refs.calibrationsFileDia.openDia(this.form)
      })
    },
    // 查看设备
    viewDevice () {
      this.viewDeviceDialog = true
      this.$nextTick(() => {
        this.$refs.viewDeviceDialog.openDia(this.form)
      })
    },
    // 关闭设备弹框
    closDeviceDia () {
      this.viewDeviceDialog = false
    },
    // 提交设备信息
    handleDeviceInfo (machineAttachmentList) {
      this.viewDeviceDialog = false
      this.form.machineAttachmentList = machineAttachmentList
    },
    // 查看检测记录
    viewTestRecord () {
      this.viewTestRecordDialog = true
      this.$nextTick(() => {
        this.$refs.viewTestRecordDialog.openDia(this.info)
      })
    },
    // 提交编辑
    editInfo (processMethodSearchNews) {
      updateMethodVerify(processMethodSearchNews).then(res => {
        this.editLoad = false
        if (res.code === 200){
          this.$message.success('操作成功')
          this.closeDia()
        }
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 提交新增
    addInfo (processMethodSearchNews) {
      addMethodVerify(processMethodSearchNews).then(res => {
        this.editLoad = false
        if (res.code === 200){
          this.$message.success('操作成功')
          this.closeDia()
        }
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 验证确认
    validation (){
      methodVerifyAffirm({methodVerifyId:this.info.methodVerifyId}).then(res => {
        if (res.code === 200){
          this.form.confirmDate = dateFormat(new Date())
        }

      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 关闭弹框
    closeDia() {
      this.formDia = false
      this.$emit('closeDia');
    },
    getUserList(){
      selectUserCondition().then(res => {
        if (res.code === 200) {
          this.userList = res.data
        }
      })
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin-top: 2vh !important;
}
>>>.el-dialog__body {
  max-height: 720px;
  overflow-y: auto;
}
.tables {
  table-layout: fixed;
  width: 100%;
  margin-top: 10px;
}
.tables td {
  height: 34px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 4px;
}
</style>
