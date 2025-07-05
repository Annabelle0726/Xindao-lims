<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :visible.sync="formDia"
               title="纠正措施处理单"
               width="60%" @close="closeRectifyDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="不合格或偏离事实的描述" @click.native="setStep(0)"></el-step>
        <el-step title="批准" @click.native="setStep(2)"></el-step>
      </el-steps>
      <div style="height: 510px;overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>培训计划：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-select v-model="form.personTrainingDetailedId" clearable filterable
                         style="width: 100%"
                         :disabled="currentStep !== 0"
                         placeholder="请选择" size="small">
                <el-option v-for="item in yearTrainingDetailed" :key="item.id" :label="item.trainingObjectives" :value="item.id">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>不合格或偏离事实的描述：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.raiseResult"
                        :rows="2"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.raiseResult }}</span>
            </td>
          </tr>
          <tr v-if="currentStep !== 0">
            <td class="td-title">
              <p>提出人：</p>
            </td>
            <td class="td-info">
              {{form.raiseUserName}}
            </td>
            <td class="td-title">
              <p>提出部门：</p>
            </td>
            <td class="td-info">
              {{form.raiseDepartment}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>提出日期：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.raiseTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td class="td-info" colspan="3" v-if="currentStep !== 0">
              {{form.raiseTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>原因分析：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.causeResult"
                        :rows="2"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.causeResult }}</span>
            </td>
          </tr>
          <tr v-if="currentStep !== 0">
            <td class="td-title">
              <p>原因分析人：</p>
            </td>
            <td class="td-info">
              {{form.causeUserName}}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{form.causeDepartment}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>原因分析日期：</p>
            </td>
            <td class="td-info" colspan="3" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.causeTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td class="td-info" colspan="3" v-if="currentStep !== 0">
              {{form.causeTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>纠正措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.correctResult"
                        :rows="2"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.correctResult }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>提出要求部门确认：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.raiseDepartmentAffirm"
                        :rows="2"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.raiseDepartmentAffirm }}</span>
            </td>
          </tr>
          <tr v-if="currentStep !== 0">
            <td class="td-title">
              <p>纠正人：</p>
            </td>
            <td class="td-info">
              {{form.correctUserName}}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{form.correctDepartment}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>纠正日期：</p>
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
            <td class="td-info" colspan="3" v-if="currentStep !== 0">
              {{form.correctTime}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p><span class="required-span">* </span>实施验证结果：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.validationResult"
                        :rows="2"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="currentStep !== 0" class="td-info1"> {{ form.validationResult }}</span>
            </td>
          </tr>
          <tr v-if="currentStep !== 0">
            <td class="td-title">
              <p>验证人：</p>
            </td>
            <td class="td-info">
              {{form.validationUserName}}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{form.validationDepartment}}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>验证日期：</p>
            </td>
            <td class="td-info" v-if="currentStep === 0">
              <el-date-picker
                v-model="form.validationTime"
                format="yyyy-MM-dd"
                placeholder="选择日期"
                size="small"
                style="width: 80%"
                type="date"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </td>
            <td class="td-info" colspan="3" v-if="currentStep !== 0">
              {{form.validationTime}}
            </td>
          </tr>
          <tr v-if="currentStep === 0">
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
        <el-button @click="closeRectifyDia">取 消</el-button>
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
  addEquipSuperviseDetailCorrect, approveEquipSuperviseDetailCorrect,
  getSuperviseDetailCorrect
} from "@/api/cnas/process/ensureResults/qualitySupervise";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {getThisYearTrainingDetailed} from "@/api/cnas/systemManagement/correctiveAction";
import {mapGetters} from "vuex";

export default {
  name: 'rectifyDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      currentStep: 0,
      form: {
        superviseDetailsId: '',
        raiseResult: '',
        vdeRaiseResult: '',
        // causeUserId: '',
        raiseUserName: '',
        raiseDepartment: '',
        raiseTime: '',
        causeResult: '',
        causeUserName: '',
        causeDepartment: '',
        causeTime: '',
        // correctUserId: '',
        correctResult: '',
        raiseDepartmentAffirm: '',
        correctUserName: '',
        correctDepartment: '',
        correctTime: '',
        approverUserId: '',
        validationResult: '',
        validationUserName: '',
        validationDepartment: '',
        validationTime: '',
        superviseDetailsCorrectId: '',
        approveId: '',
        personTrainingDetailedId: '',
      },
      editLoad: false,
      personList: [],
      yearTrainingDetailed: [],
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
      this.searchInfo(row)
      this.form.superviseDetailsId = row.superviseDetailsId
      this.form.approveId = row.approveId
      this.supervisedUserId = row.supervisedUserId
      this.getAuthorizedPerson() // 获取人员列表
      this.getYearTrainingDetailed() // 获取培训计划
    },
    // 查询监控计划详情实施信息
    searchInfo (row) {
      this.form.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      getSuperviseDetailCorrect({superviseDetailsId: row.superviseDetailsId}).then(res => {
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
        this.approverUserId = res.data.approverUserId
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit (isFinish) {
      if (this.currentStep === 0) {
        if (!this.form.raiseResult) {
          this.$message.warning('请填写不合格描述')
          return
        }
        if (!this.form.causeResult) {
          this.$message.warning('请填写原因分析')
          return
        }
        if (!this.form.correctResult) {
          this.$message.warning('请填写纠正措施')
          return
        }
        if (!this.form.validationResult) {
          this.$message.warning('请填写实施验证结果')
          return
        }
      }
      this.editLoad = true
      this.form.flowType = this.currentStep
      if (this.currentStep === 0) {
        addEquipSuperviseDetailCorrect(this.form).then(res => {
          this.editLoad = false
          this.$message.success('提交成功')
          this.closeRectifyDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      } else {
        this.form.isFinish = isFinish
        approveEquipSuperviseDetailCorrect(this.form).then(res => {
          this.editLoad = false
          this.$message.success('批准成功')
          this.closeRectifyDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      }
    },
    // 关闭弹框
    closeRectifyDia () {
      this.formDia = false
      this.$emit('closeRectifyDia')
    },
    setStep (step) {
      this.showStep = step
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
    getYearTrainingDetailed () {
      getThisYearTrainingDetailed().then(res => {
        this.yearTrainingDetailed = res.data
      })
    },
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
