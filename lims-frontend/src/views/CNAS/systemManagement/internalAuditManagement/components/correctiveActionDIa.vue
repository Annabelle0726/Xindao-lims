<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="formDia" title="纠正措施处理单"
      width="60%" @close="closeRectifyDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="不合格或偏离事实的描述" @click.native="setStep(0)"></el-step>
        <el-step title="原因分析" @click.native="setStep(1)"></el-step>
        <el-step title="纠正措施" @click.native="setStep(2)"></el-step>
        <el-step title="实施验证结果" @click.native="setStep(3)"></el-step>
      </el-steps>
      <div>
        <table border="1" cellspacing="10" class="tables">
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p><span class="required-span">* </span>不合格或偏离事实的描述：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 0 && currentStep === 0" v-model="form.raiseResult" :rows="4"
                placeholder="请输入内容" size="small" type="textarea">
              </el-input>
              <span v-if="showStep === 0 && currentStep !== 0" class="td-info1"> {{ form.raiseResult }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 0">
            <td v-if="currentStep === 0" class="td-title">
              <p><span class="required-span">* </span>请选择下一步负责人：</p>
            </td>
            <td v-if="currentStep === 0" class="td-info" colspan="3">
              <el-select v-model="form.causeUserId" clearable filterable placeholder="请选择" size="small">
                <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr v-if="showStep === 0 && currentStep !== 0">
            <td class="td-title">
              <p>提出人：</p>
            </td>
            <td class="td-info">
              {{ form.raiseUserName }}
            </td>
            <td class="td-title">
              <p>提出部门：</p>
            </td>
            <td class="td-info">
              {{ form.raiseDepartment }}
            </td>
          </tr>
          <tr v-if="showStep === 0 && currentStep !== 0">
            <td class="td-title">
              <p>日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.raiseTime }}
            </td>
          </tr>
          <tr v-if="showStep === 1">
            <td class="td-title">
              <p><span class="required-span">* </span>原因分析：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 1 && currentStep === 1" v-model="form.causeResult" :rows="5"
                placeholder="请输入内容" size="small" type="textarea">
              </el-input>
              <span v-if="showStep === 1 && currentStep !== 1" class="td-info1"> {{ form.causeResult }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 1 && currentStep !== 1">
            <td class="td-title">
              <p>原因分析人：</p>
            </td>
            <td class="td-info">
              {{ form.causeUserName }}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{ form.causeDepartment }}
            </td>
          </tr>
          <tr v-if="showStep === 1 && currentStep !== 1">
            <td class="td-title">
              <p>原因分析日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.causeTime }}
            </td>
          </tr>
          <tr v-if="showStep === 1">
            <td v-if="currentStep === 1" class="td-title">
              <p><span class="required-span">* </span>请选择下一步负责人：</p>
            </td>
            <td v-if="currentStep === 1" class="td-info" colspan="3">
              <el-select v-model="form.correctUserId" clearable filterable placeholder="请选择" size="small">
                <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td class="td-title">
              <p><span class="required-span">* </span>纠正措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 2 && currentStep === 2" v-model="form.correctResult" :rows="5"
                placeholder="请输入内容" size="small" type="textarea">
              </el-input>
              <span v-if="showStep === 2 && currentStep !== 2" class="td-info1"> {{ form.correctResult }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td class="td-title">
              <p>提出要求部门确认：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 2 && currentStep === 2" v-model="form.raiseDepartmentAffirm" :rows="5"
                placeholder="请输入内容" size="small" type="textarea">
              </el-input>
              <span v-if="showStep === 2 && currentStep !== 2" class="td-info1"> {{ form.raiseDepartmentAffirm }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 2 && currentStep !== 2">
            <td class="td-title">
              <p>纠正人：</p>
            </td>
            <td class="td-info">
              {{ form.correctUserName }}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{ form.correctDepartment }}
            </td>
          </tr>
          <tr v-if="showStep === 2 && currentStep !== 2">
            <td class="td-title">
              <p>纠正日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.correctTime }}
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td v-if="currentStep === 2" class="td-title">
              <p><span class="required-span">* </span>请选择下一步负责人：</p>
            </td>
            <td v-if="currentStep === 2" class="td-info" colspan="3">
              <el-select v-model="form.validationUserId" clearable filterable placeholder="请选择" size="small">
                <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr v-if="showStep === 3">
            <td class="td-title">
              <p><span class="required-span">* </span>实施验证结果：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 3 && currentStep === 3" v-model="form.validationResult" :rows="5"
                placeholder="请输入内容" size="small" type="textarea">
              </el-input>
              <span v-if="showStep === 3 && currentStep !== 3" class="td-info1"> {{ form.validationResult }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 3 && currentStep !== 3">
            <td class="td-title">
              <p>验证人：</p>
            </td>
            <td class="td-info">
              {{ form.validationUserName }}
            </td>
            <td class="td-title">
              <p>责任部门：</p>
            </td>
            <td class="td-info">
              {{ form.validationDepartment }}
            </td>
          </tr>
          <tr v-if="showStep === 3 && currentStep !== 3">
            <td class="td-title">
              <p>验证日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.validationTime }}
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeRectifyDia">取 消</el-button>
        <el-button v-if="currentStep !== 4" :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInternalCorrect,
  addInternalCorrect,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
export default {
  name: 'correctiveActionDIa',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      currentStep: 0,
      showStep: 0,
      form: {
        superviseDetailsId: '',
        raiseResult: '',
        vdeRaiseResult: '',
        causeUserId: '',
        raiseUserName: '',
        raiseDepartment: '',
        raiseTime: '',
        causeResult: '',
        causeUserName: '',
        causeDepartment: '',
        causeTime: '',
        correctUserId: '',
        correctResult: '',
        raiseDepartmentAffirm: '',
        correctUserName: '',
        correctDepartment: '',
        correctTime: '',
        validationUserId: '',
        validationResult: '',
        validationUserName: '',
        validationDepartment: '',
        validationTime: '',
      },
      editLoad: false,
      personList: [],
      supervisedUserList: [],
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDia(type, row) {
      this.formDia = true
      if (type !== 'add') {
        this.searchInfo(row)
        this.form.superviseDetailsId = row.superviseDetailsId
      }
      this.getAuthorizedPerson() // 获取人员列表
      this.getSupervisedUserList() // 获取当前部门人员
    },
    // 查询监控计划详情实施信息
    searchInfo(row) {
      this.form.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      getInternalCorrect({ correctId: row.correctId }).then(res => {
        if (res.data.superviseDetailsCorrectId === null) {
          this.showStep = 0
          this.currentStep = 0
        } else {
          this.form = res.data
          if (res.data.isFinish === 0) {
            if (res.data.causeUserId) {
              this.showStep = 1
              this.currentStep = 1
            }
            if (res.data.correctUserId) {
              this.showStep = 2
              this.currentStep = 2
            }
            if (res.data.validationUserId) {
              this.showStep = 3
              this.currentStep = 3
            }
          } else {
            this.currentStep = 4
            this.showStep = 3
          }
        }
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit() {
      if (this.currentStep === 0) {
        if (!this.form.raiseResult) {
          this.$message.warning('请填写不合格描述')
          return
        }
        if (!this.form.causeUserId) {
          this.$message.warning('请选择下一步负责人')
          return
        }
      } else if (this.currentStep === 1) {
        if (!this.form.causeResult) {
          this.$message.warning('请填写原因分析')
          return
        }
        if (!this.form.correctUserId) {
          this.$message.warning('请选择下一步负责人')
          return
        }
      } else if (this.currentStep === 2) {
        if (!this.form.correctResult) {
          this.$message.warning('请填写纠正措施')
          return
        }
        if (!this.form.validationUserId) {
          this.$message.warning('请选择下一步负责人')
          return
        }
      } else if (this.currentStep === 3) {
        if (!this.form.validationResult) {
          this.$message.warning('请填写实施验证结果')
          return
        }
      }
      this.editLoad = true
      this.form.supervisedTime = ''
      this.form.flowType = this.currentStep
      addInternalCorrect(this.form).then(res => {
        this.editLoad = false
        this.$message.success('提交成功')
        this.closeRectifyDia()
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 关闭弹框
    closeRectifyDia() {
      this.formDia = false
      this.$emit('closeRectifyDia')
    },
    setStep(step) {
      this.showStep = step
    },
    getAuthorizedPerson() {
      selectUserCondition().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.personList = data
      })
    },
    getSupervisedUserList() {
      selectUserCondition({ type: 2 }).then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.supervisedUserList = data
      })
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
