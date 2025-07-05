<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="formDia" title="不符合工作控制单"
      width="80%" @close="closeProcessingDia">
      <div style="height: 660px; overflow-y: auto">
        <table border="1" cellspacing="10" class="tables">
          <tr>
            <td class="first-title" rowspan="8">
              <p>不符合工作情况记录</p>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>发生部门：</p>
            </td>
            <td class="td-info">
              <span class="td-info1"> {{ form.occurrenceDepartment }}</span>
            </td>
            <td class="td-title">
              <p>部门负责人：</p>
            </td>
            <td class="td-info">
              <span class="td-info1"> {{ form.headDepartment }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>不符合工作发现途径：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.findWay" v-removeAriaHidden disabled>
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
              <span class="td-info1"> {{ form.recordDetail }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>不符合工作的以及及条款号：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.recordAccording }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>被监督人：</p>
            </td>
            <td class="td-info">
              <span class="td-info1"> {{ form.supervisedUserName }}</span>
            </td>
            <td class="td-title">
              <p>被监督日期：</p>
            </td>
            <td class="td-info">
              <span class="td-info1"> {{ form.supervisedTime }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>发现部门：</p>
            </td>
            <td class="td-info">
              {{ form.foundDepartment }}
            </td>
            <td class="td-title">
              <p>时间：</p>
            </td>
            <td class="td-info">
              {{ form.recordTime }}
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>记录人：</p>
            </td>
            <td class="td-info">
              {{ form.recordUserName }}
            </td>
            <td class="td-title">
              <p>记录时间：</p>
            </td>
            <td class="td-info">
              {{ form.recordTime }}
            </td>
          </tr>
          <tr>
            <td class="first-title" rowspan="3">
              <p>处理措施</p>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>消除不符合工作所采取的措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <span class="td-info1"> {{ form.eliminateMeasure }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>负责人：</p>
            </td>
            <td class="td-info">
              {{ form.actionsUserName }}
            </td>
            <td class="td-title">
              <p>处理时间：</p>
            </td>
            <td class="td-info">
              {{ form.actionsTime }}
            </td>
          </tr>
          <tr>
            <td class="first-title" rowspan="4">
              <p>纠正措施</p>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>是否需要采取纠正措施：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.isCorrect" v-removeAriaHidden disabled>
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
              <span class="td-info1"> {{ form.correctContent }}</span>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>负责人：</p>
            </td>
            <td class="td-info">
              {{ form.correctUserName }}
            </td>
            <td class="td-title">
              <p>处理时间：</p>
            </td>
            <td class="td-info">
              {{ form.correctTime }}
            </td>
          </tr>
          <tr>
            <td class="first-title" rowspan="4">
              <p>是否通知客户及可恢复工作</p>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>通知客户：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.notifyCustomer" v-removeAriaHidden disabled>
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>恢复工作：</p>
            </td>
            <td class="td-info" colspan="3">
              <el-radio-group v-model="form.backToWork" v-removeAriaHidden disabled>
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td class="td-title">
              <p>负责人：</p>
            </td>
            <td class="td-info">
              {{ form.qualityManagerUserName }}
            </td>
            <td class="td-title">
              <p>处理时间：</p>
            </td>
            <td class="td-info">
              {{ form.qualityManagerTime }}
            </td>
          </tr>
        </table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSuperviseDetailAccording
} from '@/api/cnas/process/nonconformingWork.js'
export default {
  name: 'qualityInfo',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      form: {
        occurrenceDepartment: '',
        headDepartment: '',
        findWay: '',
        recordDetail: '',
        recordAccording: '',
        supervisedUserName: '',
        supervisedTime: '',
        actionsUserName: '',
        eliminateMeasure: '',
        correctUserName: '',
        isCorrect: '',
        correctContent: '',
        recordUserName: '',
        recordTime: '',
        foundDepartment: '',
        actionsTime: '',
        correctTime: '',
        notifyCustomer: '',
        backToWork: '',
        qualityManagerUserName: '',
        qualityManagerTime: '',
      },
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.formDia = true
      this.searchInfo(row)
    },
    // 查询监控计划详情实施信息
    searchInfo(row) {
      this.form.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      getSuperviseDetailAccording({ superviseDetailsId: row.superviseDetailsId }).then(res => {
        this.form = res.data
      }).catch(err => {
        console.log('err---', err);
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

.first-title {
  display: table-cell;
  width: 100px;
  /* 设置一个固定宽度 */
  height: 200px;
  /* 设置一个固定高度 */
  text-align: center;
  /* 水平居中 */
  vertical-align: middle;
  /* 垂直居中 */
  writing-mode: vertical-rl;
  /* 文字竖直排列 */
  padding: 10px 0;
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
  padding: 10px;
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
