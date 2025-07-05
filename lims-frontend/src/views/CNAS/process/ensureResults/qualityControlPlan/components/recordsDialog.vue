<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :visible.sync="formDia"
               title="检测工作监督记录表"
               width="70%" @close="closeRecordsDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="实施"></el-step>
        <el-step title="批准"></el-step>
      </el-steps>
      <div style="height: 470px;overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>检测人员：</p>
            </td>
            <td colspan="2">
              <el-input v-if="currentStep === 0" v-model="form.testMember"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.testMember }}</span>
            </td>
            <td class="td-title">
              <p><span class="required-span">* </span>监督员：</p>
            </td>
            <td colspan="2">
              <el-select v-if="currentStep === 0" @change="usePersonName" v-model="form.supervisor" placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in recordUserList" :key="item.userName" :label="item.userName" :value="item.userName"></el-option>
              </el-select>
              <span v-else class="td-info"> {{ form.supervisor }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>检测项目：</p>
            </td>
            <td>
              <el-input v-if="currentStep === 0" v-model="form.testItem"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.testItem }}</span>
            </td>
            <td class="td-title">
              <p><span class="required-span">* </span>样品编号：</p>
            </td>
            <td>
              <el-input v-if="currentStep === 0" v-model="form.sampleNumber"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.sampleNumber }}</span>
            </td>
            <td class="td-title">
              <p><span class="required-span">* </span>检测日期：</p>
            </td>
            <td>
              <el-input v-if="currentStep === 0" v-model="form.testDate"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.testDate }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>人员：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.personnel"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.personnel }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>仪器设备：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.device"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.device }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>工作环境：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.environment"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.environment }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>样品采集：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.sampleCollection"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.sampleCollection }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>样品的准备：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.samplePreparation"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.samplePreparation }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>检测方法：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.detectionMethod"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.detectionMethod }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>检测记录：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.inspectionRecord"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.inspectionRecord }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>检测报告：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.examiningReport"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.examiningReport }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>监督情况评价：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.supervisionEvaluation"
                        :rows="4"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-else class="td-info"> {{ form.supervisionEvaluation }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>不符合处理意见：</p>
            </td>
            <td colspan="5">
              <el-input v-if="currentStep === 0" v-model="form.handlingAdvice"
                        :rows="4"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-else class="td-info"> {{ form.handlingAdvice }}</span>
            </td>
          </tr>
          <tr>
            <td v-if="currentStep === 0" class="td-title">
              <p><span class="required-span">* </span>请选择下一步批准人：</p>
            </td>
            <td v-if="currentStep === 0" colspan="5">
              <el-select v-model="form.ratifyUserId" clearable filterable
                         placeholder="请选择" size="small">
                <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
            <td v-if="currentStep === 1" class="td-title">
              <p>批准负责人：</p>
            </td>
            <td v-if="currentStep === 1" class="td-info" colspan="5">
              {{form.ratifyUserName}}
            </td>
          </tr>
          <tr v-if="currentStep === 1 || currentStep === 2">
            <td class="td-title">
              <p><span class="required-span">* </span>审批结论：</p>
            </td>
            <td colspan="2">
              <el-input v-if="currentStep === 1"
                        v-model="form.ratifyOpinion"
                        :rows="4"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep === 2" class="td-info"> {{ form.ratifyOpinion }}</span>
            </td>
            <td class="td-title">
              <p v-if="currentStep === 1">请选择是否符合：</p>
              <p v-if="currentStep === 2">是否符合：</p>
            </td>
            <td colspan="2">
              <el-radio-group v-if="currentStep === 1" v-model="form.isAccording">
                <el-radio :label="0">不符合</el-radio>
                <el-radio :label="1">符合</el-radio>
              </el-radio-group>
              <span v-if="currentStep === 2" class="td-info"> {{ form.isAccording === 0 ? '不符合' : '符合' }}</span>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeRecordsDia">取 消</el-button>
        <el-button v-if="currentStep === 0 && userId == supervisedUserId" :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
        <el-button v-if="currentStep === 1 && userId == ratifyUserId" :loading="editLoad" @click="handleEdit(0)">不通过</el-button>
        <el-button v-if="currentStep === 1 && userId == ratifyUserId" :loading="editLoad" type="primary" @click="handleEdit(1)">通
          过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addSuperviseDetailRecord,
  addSuperviseRecordOpinion, getRecordUser,
  getSuperviseDetailRecord
} from "@/api/cnas/process/ensureResults/qualitySupervise";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {mapGetters} from "vuex";

export default {
  name: 'recordsDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        superviseDetailsId: '',
        superviseDetailsRecordId: '',
        testMember: '',
        supervisor: '',
        supervisorId: '',
        testItem: '',
        sampleNumber: '',
        testDate: '',
        personnel: '',
        device: '',
        environment: '',
        sampleCollection: '',
        samplePreparation: '',
        detectionMethod: '',
        inspectionRecord: '',
        examiningReport: '',
        supervisionEvaluation: '',
        handlingAdvice: '',
        ratifyUserId: '',
        ratifyUserName: '',
        ratifyOpinion: '',
        isAccording: '',
      },
      currentStep: 0,
      editLoad: false,
      personList: [],
      recordUserList: [],
      supervisedUserId: '',
      ratifyUserId: '',
    };
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.supervisedUserId = row.supervisedUserId
      this.searchInfo(row)
      this.getAuthorizedPerson()
    },
    usePersonName (value) {
      const index = this.recordUserList.findIndex(item => item.userId === value)
      if (index > -1) {
        this.form.supervisor = this.recordUserList[index].userName
      }
    },
    // 查询监督计划详情记录流程信息
    searchInfo (row) {
      getSuperviseDetailRecord({superviseDetailsId: row.superviseDetailsId}).then(res => {
        // 有superviseDetailsRecordId说明提交过记录
        if (res.data.ratifyUserId) {
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
        this.ratifyUserId = res.data.ratifyUserId
        this.getRecordUser()
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交流程
    handleEdit (isFinish) {
      if (!this.form.testMember || !this.form.supervisor || !this.form.testItem || !this.form.sampleNumber
        || !this.form.testDate) {
        this.$message.warning('请填写完整')
        return
      }
      if (this.currentStep === 0) {
        this.addInfo()
      } else {
        this.editInfo(isFinish)
      }
    },
    // 提交记录
    addInfo () {
      this.editLoad = true
      addSuperviseDetailRecord(this.form).then(res => {
        this.editLoad = false
        this.$message.success('操作成功')
        this.closeRecordsDia()
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 提交批准
    editInfo (isFinish) {
      if (!this.form.ratifyOpinion) {
        this.$message.warning('请填写审批意见')
        return
      }
      if (this.form.isAccording === null) {
        this.$message.warning('请选择是否符合')
        return
      }
      this.editLoad = true
      this.form.isFinish = isFinish
      addSuperviseRecordOpinion(this.form).then(res => {
        this.editLoad = false
        this.$message.success('操作成功')
        this.closeRecordsDia()
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 关闭弹框
    closeRecordsDia () {
      this.formDia = false
      this.$emit('closeRecordsDia')
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
    getRecordUser () {
      getRecordUser({superviseDetailsId: this.form.superviseDetailsId}).then(res => {
        this.recordUserList = res.data
      })
    },
  }
};
</script>

<style scoped>
.tables {
  table-layout: fixed;
  width: 100%;
  margin-top: 10px;
}
.td-title {
  height: 40px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 6px;
}
.td-info {
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}
.tables td {
  height: 40px;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 6px;
}
</style>
