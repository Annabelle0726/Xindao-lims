<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="formDia" title="纠正措施处理单"
      width="60%" @close="closeProcessingDia">
      <div style="height: 660px; overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="td-title">
              <p>培训计划：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-select v-model="form.personTrainingDetailedId" clearable filterable style="width: 100%" disabled
                placeholder="请选择" size="small">
                <el-option v-for="item in yearTrainingDetailed" :key="item.id" :label="item.trainingObjectives"
                  :value="item.id">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>不合格或偏离事实的描述：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.raiseResult }}</span>
            </td>
          </tr>
          <tr>
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
          <tr>
            <td class="td-title">
              <p>日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.raiseTime }}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>原因分析：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.causeResult }}</span>
            </td>
          </tr>
          <tr>
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
          <tr>
            <td class="td-title">
              <p>原因分析日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.causeTime }}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>纠正措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.correctResult }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>提出要求部门确认：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.raiseDepartmentAffirm }}</span>
            </td>
          </tr>
          <tr>
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
          <tr>
            <td class="td-title">
              <p>纠正日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.correctTime }}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>实施验证结果：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.validationResult }}</span>
            </td>
          </tr>
          <tr>
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
          <tr>
            <td class="td-title">
              <p>验证日期：</p>
            </td>
            <td class="td-info" colspan="3">
              {{ form.validationTime }}
            </td>
          </tr>
        </table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSuperviseDetailCorrect,
  getThisYearTrainingDetailed,
} from '@/api/cnas/systemManagement/correctiveAction.js'
export default {
  name: 'correctiveInfo',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      yearTrainingDetailed: [],
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
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.searchInfo(row)
      this.form.superviseDetailsId = row.superviseDetailsId
      this.getYearTrainingDetailed() // 获取培训计划
    },
    // 查询监控计划详情实施信息
    searchInfo(row) {
      this.form.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      getSuperviseDetailCorrect({ superviseDetailsId: row.superviseDetailsId }).then(res => {
        this.form = res.data
      }).catch(err => {
        console.log('err---', err);
      })
    },
    getYearTrainingDetailed() {
      getThisYearTrainingDetailed().then(res => {
        this.yearTrainingDetailed = res.data
      })
    },
    // 关闭弹框
    closeProcessingDia() {
      this.formDia = false
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 5vh auto 50px !important;
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
