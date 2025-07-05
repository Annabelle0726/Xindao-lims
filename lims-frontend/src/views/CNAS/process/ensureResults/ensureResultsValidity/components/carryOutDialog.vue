<template>
<!--质量监控-实施流程页面-->
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="formDia" title="实施"
      width="60%" @close="closeCarryOutDia">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step title="实施"></el-step>
        <el-step title="批准"></el-step>
      </el-steps>
      <div style="height: 520px;overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="td-title">
              <p>监控项目：</p>
            </td>
            <td colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.monitorProject" placeholder="请输入内容" size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.monitorProject }}</span>
            </td>
            <td class="td-title">
              <p>监控时间：</p>
            </td>
            <td colspan="3">
              <el-input v-if="currentStep === 0" v-model="form.monitorData" placeholder="请输入内容" size="small">
              </el-input>
              <span v-else class="td-info"> {{ form.monitorData }}</span>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>监控目的：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.monitorPurpose" :rows="3" placeholder="请输入内容"
                  size="small" type="textarea">
                </el-input>
                <span v-else class="td-info2"> {{ form.monitorPurpose }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>监控方法：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.monitorMethod" :rows="4" placeholder="请输入内容"
                  size="small" type="textarea">
                </el-input>
                <span v-else class="td-info2"> {{ form.monitorMethod }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>参加人员：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.participant" :rows="3" placeholder="请输入内容" size="small"
                  type="textarea">
                </el-input>
                <span v-else class="td-info2"> {{ form.participant }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>过程控制：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.processControl" :rows="3" placeholder="请输入内容"
                  size="small" type="textarea">
                </el-input>
                <span v-else class="td-info2"> {{ form.processControl }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>结果评价标准（如何评价）：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.howEvaluate" :rows="3" placeholder="请输入内容" size="small"
                  type="textarea">
                </el-input>
                <span v-else class="td-info2"> {{ form.howEvaluate }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="8">
              <div>
                <p>经费预算：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.budget" placeholder="请输入内容" size="small">
                </el-input>
                <span v-else class="td-info2"> {{ form.budget }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="4">
              <div>
                <p>检测部门：</p>
              </div>
              <div>
                <el-input v-if="currentStep === 0" v-model="form.inspectionDepartment" placeholder="请输入内容" size="small">
                </el-input>
                <span v-else class="td-info2"> {{ form.inspectionDepartment }}</span>
              </div>
            </td>
            <td colspan="4">
              <div v-if="currentStep === 0">
                <div>批准人：</div>
                <div>
                  <el-select v-if="currentStep === 0" v-model="form.ratifyUserId" clearable filterable placeholder="请选择"
                    size="small">
                    <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <span v-else class="td-info2"> {{ form.ratifyName }}</span>
                </div>
              </div>
              <div>
                <div v-if="currentStep !== 0">
                  <p>批准意见：</p>
                </div>
                <div>
                  <el-input v-if="currentStep === 1" v-model="form.ratifyOpinion" :rows="3" placeholder="请输入内容"
                    size="small" type="textarea">
                  </el-input>
                  <span v-if="currentStep === 2" class="td-info2"> {{ form.ratifyOpinion }}</span>
                  <span v-if="currentStep !== 0" class="td-info3"> {{ '批准人：' + form.ratifyName }}</span>
                </div>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeCarryOutDia">取 消</el-button>
        <el-button v-if="currentStep === 0" :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
        <el-button v-if="currentStep === 1 && userId == ratifyUserId" :loading="editLoad" @click="handleEdit(0)">不通过</el-button>
        <el-button v-if="currentStep === 1 && userId == ratifyUserId" :loading="editLoad" type="primary" @click="handleEdit(1)">通
          过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {
  addQualityMonitorRatify, addQualityMonitorRatifyOpinion,
  getQualityMonitorRatify
} from "@/api/cnas/process/ensureResults/qualityMonitor";
import { selectUserCondition } from "@/api/business/inspectionTask";
import {mapGetters} from "vuex";

export default {
  name: 'carryOutDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      currentStep: 0,
      form: {
        monitorProject: '',
        monitorData: '',
        monitorPurpose: '',
        monitorMethod: '',
        participant: '',
        processControl: '',
        howEvaluate: '',
        budget: '',
        inspectionDepartment: '',
        ratifyUserId: '',
        ratifyOpinion: '',
        qualityMonitorDetailsId: '',
        ratifyName: '',
      },
      personList: [],
      editLoad: false,
      isCarryOut: false, // 是否为实施
      ratifyUserId: ''
    };
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.searchInfo(row)
      this.getAuthorizedPerson()
    },
    // 查询监控计划详情实施信息
    searchInfo(row) {
      getQualityMonitorRatify({ qualityMonitorDetailsId: row.qualityMonitorDetailsId }).then(res => {
        // 有detailsRatifyId则说明提交过实施信息
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
        this.ratifyUserId = res.data.ratifyUserId
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 提交
    handleEdit(isFinish) {
      this.editLoad = true
      if (this.currentStep == 0) {
        this.addInfo()
      } else {
        this.editInfo(isFinish)
      }
    },
    // 提交实施
    addInfo() {
      addQualityMonitorRatify(this.form).then(res => {
        this.editLoad = false
        this.$message.success('操作成功')
        this.closeCarryOutDia()
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 提交批准
    editInfo(isFinish) {
      this.form.isFinish = isFinish
      addQualityMonitorRatifyOpinion(this.form).then(res => {
        this.editLoad = false
        this.$message.success('操作成功')
        this.closeCarryOutDia()
      }).catch(err => {
        console.log('err---', err);
        this.editLoad = false
      })
    },
    // 关闭弹框
    closeCarryOutDia() {
      this.formDia = false
      this.$emit('closeCarryOutDia')
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
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 6px;
}

.td-info {
  display: inline-block;
  width: 100%;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}

.td-info2 {
  display: inline-block;
  width: 90%;
  text-align: left;
  font-size: 16px;
  word-wrap: break-word;
  white-space: normal;
  margin-left: 20px;
}

.tables td {
  height: 40px;
  width: 100px;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 6px;
}

.td-info3 {
  width: 90%;
  display: inline-block;
  text-align: right;
}
</style>
