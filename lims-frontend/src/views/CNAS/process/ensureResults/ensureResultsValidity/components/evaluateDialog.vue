<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :visible.sync="formDia"
               title="质量监控评价"
               width="60%" @close="closeEvaDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="监控计划实施情况" @click.native="setStep(0)"></el-step>
        <el-step title="监控结果评价" @click.native="setStep(1)"></el-step>
        <el-step title="评审结论（是否采取措施）" @click.native="setStep(2)"></el-step>
      </el-steps>
      <div>
        <table border="1" cellspacing="10" class="tables">
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p><span class="required-span">* </span>评审目的：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 0 && currentStep === 0" v-model="form.reviewPurpose"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-if="showStep === 0 && currentStep !== 0" class="td-info1"> {{ form.reviewPurpose }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p><span class="required-span">* </span>评审人员：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 0 && currentStep === 0" v-model="form.reviewUser"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-if="showStep === 0 && currentStep !== 0" class="td-info1"> {{ form.reviewUser }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p><span class="required-span">* </span>评审日期：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 0 && currentStep === 0" v-model="form.reviewTime"
                        placeholder="请输入内容"
                        size="small">
              </el-input>
              <span v-if="showStep === 0 && currentStep !== 0" class="td-info1"> {{ form.reviewTime }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p><span class="required-span">* </span>监控计划实施情况：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-input v-if="showStep === 0 && currentStep === 0" v-model="form.implementCondition"
                        :rows="5"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="showStep === 0 && currentStep !== 0" class="td-info1"> {{ form.implementCondition }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 0">
            <td class="td-title">
              <p>实施部门：</p>
            </td>
            <td class="td-info">
              <span> {{ form.implementDepartment }}</span>
            </td>
            <td v-if="currentStep === 0" class="td-title">
              <p>请选择下一步负责人：</p>
            </td>
            <td v-if="currentStep === 0" class="td-info">
              <el-select v-model="form.implementUserId" clearable filterable
                         placeholder="请选择" size="small">
                <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr v-if="showStep === 1">
            <td class="td-title">
              <p>监控结果评价：</p>
            </td>
            <td class="td-info" colspan="4">
              <el-input v-if="showStep === 1 && currentStep === 1" v-model="form.implementResult"
                        :rows="5"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="showStep === 1 && currentStep !== 1" class="td-info1"> {{ form.implementResult }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 1">
            <td class="td-title">
              <p>当前负责人：</p>
            </td>
            <td class="td-info" colspan="4">
              {{form.implementName}}
            </td>
          </tr>
          <tr v-if="showStep === 1">
            <td v-if="currentStep === 1" class="td-title">
              <p>请选择下一步负责人：</p>
            </td>
            <td v-if="currentStep === 1" class="td-info" colspan="4">
              <el-select v-model="form.ratifyUserId" clearable filterable
                         placeholder="请选择" size="small">
                <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td class="td-title">
              <p>评审结论（是否采取措施）：</p>
            </td>
            <td class="td-info" colspan="4">
              <el-input v-if="showStep === 2 && currentStep === 2" v-model="form.ratifyOpinion"
                        :rows="5"
                        placeholder="请输入内容"
                        size="small"
                        type="textarea">
              </el-input>
              <span v-if="showStep === 2 && currentStep !== 2" class="td-info1"> {{ form.ratifyOpinion }}</span>
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td class="td-title">
              <p>当前负责人：</p>
            </td>
            <td class="td-info" colspan="4">
              {{form.ratifyUserName}}
            </td>
          </tr>
          <tr v-if="showStep === 2">
            <td class="td-title">
              <p>审批日期：</p>
            </td>
            <td class="td-info" colspan="4">
              {{form.ratifyTime}}
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEvaDia">取 消</el-button>
        <el-button v-if="currentStep !== 3" :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
    <view-record v-if="ViewRecord" ref="ViewRecord"></view-record>
  </div>
</template>

<script>
import ViewRecord from './ViewRecord.vue';
import {
  addMonitorEvaluateOpinion, addQualityMonitorEvaluate,
  getQualityMonitorEvaluate
} from "@/api/cnas/process/ensureResults/qualityMonitor";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {selectUserDepartmentLimsName} from "@/api/system/user";

export default {
  name: 'evaluateDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: { ViewRecord },
  data() {
    // 这里存放数据
    return {
      formDia: false,
      currentStep: 0,
      showStep: 0,
      form: {
        implementDepartment: '',
        reviewPurpose: '',
        reviewUser: '',
        reviewTime: '',
        implementCondition: '',
        implementName: '',
        implementUserId: '',
        implementResult: '',
        ratifyUserName: '',
        ratifyUserId: '',
        ratifyOpinion: '',
        qualityMonitorDetailsId: '',
        ratifyTime: '',
        detailsEvaluateId: '',
      },
      editLoad: false,
      personList: [],
      ViewRecord: false,
    };
  },
  // 方法集合
  methods: {
    openDia (row) {
      this.formDia = true
      this.searchInfo(row)
      this.getAuthorizedPerson()
      this.getDepartment()
    },
    // 查询监控计划详情实施信息
    searchInfo (row) {
      this.form.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      getQualityMonitorEvaluate({qualityMonitorDetailsId: row.qualityMonitorDetailsId}).then(res => {
        if (res.data === null) {
          this.showStep = 0
          this.currentStep = 0
        } else {
          this.form = res.data
          if (res.data.isFinish === 0) {
            if (res.data.implementUserId && !res.data.ratifyUserId) {
              this.showStep = 1
              this.currentStep = 1
            } else if (res.data.implementUserId && res.data.ratifyUserId) {
              this.showStep = 2
              this.currentStep = 2
            }
          } else {
            this.currentStep = 3
            this.showStep = 2
          }
        }
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit () {
      if (this.currentStep === 2) {
        this.editLoad = true
        addMonitorEvaluateOpinion(this.form).then(res => {
          this.editLoad = false
          this.$message.success('操作成功')
          this.closeEvaDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      } else {
        if (!this.form.reviewPurpose) {
          this.$message.warning('请填写评审目的')
          return
        }
        if (!this.form.reviewUser) {
          this.$message.warning('请填写评审人员')
          return
        }
        if (!this.form.reviewTime) {
          this.$message.warning('请填写评审日期')
          return
        }
        if (!this.form.implementCondition) {
          this.$message.warning('请填写监控计划实施情况')
          return
        }
        if (!this.form.implementUserId) {
          this.$message.warning('请选择下一步负责人')
          return
        }
        if (this.currentStep === 1) {
          if (!this.form.ratifyUserId) {
            this.$message.warning('请选择下一步负责人')
            return
          }
        }
        this.editLoad = true
        addQualityMonitorEvaluate(this.form).then(res => {
          this.editLoad = false
          this.$message.success('操作成功')
          this.closeEvaDia()
        }).catch(err => {
          console.log('err---', err);
          this.editLoad = false
        })
      }
    },
    // 关闭弹框
    closeEvaDia () {
      this.formDia = false
      this.$emit('closeEvaDia')
    },
    setStep (step) {
      this.showStep = step
    },
    viewTestRecord () {
      this.ViewRecord = true
      this.$nextTick(() => {
        this.$refs.ViewRecord.openDia( this.form)
      })
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
    getDepartment() {
      selectUserDepartmentLimsName().then(res => {
        this.form.implementDepartment = res.data
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
