<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :visible.sync="formDia"
               title="不符合工作控制单"
               width="60%" @close="closeProcessingDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="不符合工作情况记录"></el-step>
        <el-step title="批准"></el-step>
      </el-steps>
      <div style="height: 510px;overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="td-title">
              <p>发生部门：</p>
            </td>
            <td class="td-info">
              <el-input v-if="currentStep === 0" v-model="form.occurrenceDepartment"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.occurrenceDepartment }}</span>
            </td>
            <td class="td-title">
              <p>部门负责人：</p>
            </td>
            <td class="td-info">
              <el-input v-if="currentStep === 0" v-model="form.headDepartment"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.headDepartment }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>不符合工作发现途径：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.findWay" v-removeAriaHidden :disabled="currentStep !== 0">
                <el-radio :label="0">管理评审</el-radio>
                <el-radio :label="1">内部审核</el-radio>
                <el-radio :label="2">检测过程控制</el-radio>
                <el-radio :label="3">内部质量控制</el-radio>
                <el-radio :label="4">内部监督</el-radio>
                <el-radio :label="5">外部评审</el-radio>
                <el-radio :label="6">外部投诉</el-radio>
                <el-radio :label="7">其他</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>不符合工作的详细记录：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.recordDetail"
                        :rows="4"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.recordDetail }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>不符合工作的以及及条款号：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.recordAccording"
                        :rows="4"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.recordAccording }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>被监督人：</p>
            </td>
            <td class="td-info">
              <span class="td-info1"> {{ form.supervisedUserName }}</span>
            </td>
            <td class="td-title">
              <p><span class="required-span">* </span>被监督时间：</p>
            </td>
            <td class="td-info">
              <el-date-picker
                v-if="currentStep === 0"
                v-model="form.supervisedTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.supervisedTime }}</span>
            </td>
          </tr>
          <tr v-if="currentStep !== 0">
            <td class="td-title">
              <p>发现部门：</p>
            </td>
            <td class="td-info">
              {{form.foundDepartment}}
            </td>
            <td class="td-title">
              <p>时间：</p>
            </td>
            <td class="td-info">
              {{form.recordTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title" v-if="currentStep !== 0">
              <p>记录人：</p>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.recordUserName}}
            </td>
            <td class="td-title">
              <p>记录时间：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.recordTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.recordTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>消除不符合工作所采取的措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.eliminateMeasure"
                        :rows="5"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.eliminateMeasure }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title" v-if="currentStep !== 0">
              <p>当前负责人：</p>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.actionsUserName}}
            </td>
            <td class="td-title">
              <p>处理时间：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.actionsTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.actionsTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>是否需要采取纠正措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.isCorrect" v-removeAriaHidden :disabled="currentStep !== 0">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>纠正措施处理单跟踪：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.correctContent"
                        :rows="5"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.correctContent }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title" v-if="currentStep !== 0">
              <p>当前负责人：</p>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.correctUserName}}
            </td>
            <td class="td-title">
              <p>纠正填写时间：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.correctTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td v-if="currentStep !== 0" class="td-info">
              {{form.correctTime}}
            </td>
          </tr>
          <tr>
            <td rowspan="3">
              <p>是否通知客户及可恢复工作</p>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>通知客户：</p>
            </td>
            <td class="td-info" colspan="2">
              <el-radio-group v-model="form.notifyCustomer" v-removeAriaHidden :disabled="currentStep !== 0">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>恢复工作：</p>
            </td>
            <td class="td-info" colspan="2">
              <el-radio-group v-model="form.backToWork" v-removeAriaHidden :disabled="currentStep !== 0">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title" v-if="currentStep !== 0">
              <p>当前负责人：</p>
            </td>
            <td class="td-info" v-if="currentStep !== 0">
              {{form.qualityManagerUserName}}
            </td>
            <td class="td-title">
              <p>质量负责人填写时间：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.qualityManagerTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td v-if="currentStep !== 0" class="td-info">
              {{form.qualityManagerTime}}
            </td>
          </tr>
          <tr v-if=" currentStep === 0">
            <td v-if="currentStep === 0" class="td-title">
              <p><span class="required-span">* </span>请选择批准人：</p>
            </td>
            <td v-if="currentStep === 0" class="td-info" colspan="3">
              <el-select v-model="form.approverUserId" clearable filterable
                         placeholder="请选择" size="small">
                <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeProcessingDia">取 消</el-button>
        <el-button v-if="currentStep === 0 && userId == supervisedUserId" :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
        <el-button v-if="currentStep === 1 && userId == approverUserId" :loading="editLoad" @click="handleEdit(0)">不通过</el-button>
        <el-button v-if="currentStep === 1 && userId == approverUserId" :loading="editLoad" type="primary" @click="handleEdit(1)">通
          过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addEquipSuperviseDetailAccording, approverEquipSuperviseDetailAccording,
  getSuperviseDetailAccording
} from "@/api/cnas/process/ensureResults/qualitySupervise";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {mapGetters} from "vuex";

export default {
  name: 'processingSheet',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      currentStep: 0,
      form: {
        occurrenceDepartment: '',
        headDepartment: '',
        findWay: '',
        recordDetail: '',
        recordAccording: '',
        supervisedUserId: '',
        supervisedUserName: '',
        supervisedTime: '',
        actionsUserName: '',
        eliminateMeasure: '',
        // correctUserId: '',
        correctUserName: '',
        isCorrect: '',
        // qualityManagerUserId: '',
        correctContent: '',
        superviseDetailsId: '',
        superviseDetailsAccordingId: '',
        flowType: '',
        recordUserName: '',
        recordTime: '',
        foundDepartment: '',
        actionsTime: '',
        correctTime: '',
        notifyCustomer: '',
        backToWork: '',
        qualityManagerUserName: '',
        qualityManagerTime: '',
        approverUserId: '',
        approveId: '',
      },
      editLoad: false,
      personList: [],
      supervisedUserList: [],
      supervisedUserId: '',
      approverUserId: ''
    };
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  // 方法集合
  methods: {
    openDia (row) {
      this.formDia = true
      this.form.superviseDetailsId = row.superviseDetailsId
      this.supervisedUserId = row.supervisedUserId
      this.searchInfo(row)
      this.form.approveId = row.approveId
      this.getAuthorizedPerson() // 获取人员列表
      // this.getSupervisedUserList() // 获取当前部门人员
    },
    // 查询监控计划详情实施信息
    searchInfo (row) {
      getSuperviseDetailAccording({superviseDetailsId: this.form.superviseDetailsId}).then(res => {
        this.form.supervisedUserId = res.data.supervisedUserId
        this.form.supervisedUserName = res.data.supervisedUserName
        if (res.data.approverUserId) {
          // 是否结束0:未结束, 1:已结束
          if (res.data.isFinish != 1) {
            this.currentStep = 1
          } else if (res.data.isFinish == 1) {
            this.currentStep = 2
          }
        } else {
          this.currentStep = 0
        }
        this.form = res.data
        this.form.superviseDetailsId = row.superviseDetailsId
        this.approverUserId = res.data.approverUserId
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit (isFinish) {
      if (this.currentStep === 0) {
        if (this.form.findWay === null) {
          this.$message.warning('请选择不符合工作发现途径')
          return
        }
        if (!this.form.supervisedTime) {
          this.$message.warning('请选择被监督时间')
          return
        }
        if (this.form.notifyCustomer === null) {
          this.$message.warning('请选择是否通知客户')
          return
        }
        if (this.form.backToWork === null) {
          this.$message.warning('请选择是否恢复工作')
          return
        }
        if (this.form.isCorrect === null) {
          this.$message.warning('请选择是否需要采取纠正措施')
          return
        }
      }
      this.editLoad = true
      this.form.flowType = this.currentStep
      if (this.currentStep === 0) {
        addEquipSuperviseDetailAccording(this.form).then(res => {
          this.editLoad = false
          this.$message.success('提交成功')
          this.closeProcessingDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      } else {
        this.form.isFinish = isFinish
        approverEquipSuperviseDetailAccording(this.form).then(res => {
          this.editLoad = false
          this.$message.success('批准成功')
          this.closeProcessingDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      }
    },
    // 关闭弹框
    closeProcessingDia () {
      this.formDia = false
      this.$emit('closeProcessingDia')
    },
    getAuthorizedPerson() {
      selectUserCondition({ type: 2 }).then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.personList = data;
      });
    },
    // getSupervisedUserList () {
    //   this.$axios.get(this.$api.user.selectDepartmentLimsUserList).then(res => {
    //     let data = []
    //     res.data.forEach(a => {
    //       data.push({
    //         label: a.name,
    //         value: a.id
    //       })
    //     })
    //     this.supervisedUserList = data
    //   })
    // },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 20px auto 50px !important;
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
