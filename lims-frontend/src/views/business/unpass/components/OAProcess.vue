<template>
  <div>
    <el-dialog :close-on-press-escape="false" :visible.sync="isShow" title="不合格处理"
               width="1040px"
               @close="$emit('closeOAProcess')">

      <table :border='true' class="table">
        <tr>
          <th class="th-title">1检验员</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{oneOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{oneInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{oneName}}<span v-if="oneName">：</span>{{oneTime}}</th>
        </tr>
        <tr>
          <th class="th-title">2检测主管确认</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{twoOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{twoInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{twoName}}<span v-if="twoName">：</span>{{twoTime}}</th>
        </tr>
        <tr>
          <th class="th-title">3物流部确认</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{threeOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{threeInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{threeName}}<span v-if="threeName">：</span>{{threeTime}}</th>
        </tr>
        <tr>
          <th class="th-title">4产品工程师处理意见</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{fourOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{fourInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{fourName}}<span v-if="fourName">：</span>{{fourTime}}</th>
        </tr>
        <tr>
          <th class="th-title">5.总工或者副经理的处理意见</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{fiveOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{fiveInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{fiveName}}<span v-if="fiveName">：</span>{{fiveTime}}</th>
        </tr>
        <tr>
          <th class="th-title">6质量部</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{sixOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{sixInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{sixName}}<span v-if="sixName">：</span>{{sixTime}}</th>
        </tr>
        <tr>
          <th class="th-title">7质量部经理</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{sevenOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{sevenInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{sevenName}}<span v-if="sevenName">：</span>{{sevenTime}}</th>
        </tr>
        <tr>
          <th class="th-title">8核算员</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{eightOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{eightInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{eightName}}<span v-if="eightName">：</span>{{eightTime}}</th>
        </tr>
        <tr>
          <th class="th-title">9物流部索赔结果</th>
          <th class="th-titleSec">处理结果</th>
          <th class="th-info1">{{nineOperation}}</th>
          <th class="th-titleSec">处理意见</th>
          <th class="th-info" colspan="3">{{nineInfo}}</th>
          <th style="display:none;"></th>
          <th style="display:none;"></th>
          <th >{{nineName}}<span v-if="nineName">：</span>{{nineTime}}</th>
        </tr>
      </table>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "OAProcess",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    OAProcess: {
      type: Boolean,
      default: () => false
    },
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.OAProcess,
      oneInfo: '',
      twoInfo: '',
      threeInfo: '',
      fourInfo: '',
      fiveInfo: '',
      sixInfo: '',
      sevenInfo: '',
      eightInfo: '',
      nineInfo: '',
      oneTime: '',
      twoTime: '',
      threeTime: '',
      fourTime: '',
      fiveTime: '',
      sixTime: '',
      sevenTime: '',
      eightTime: '',
      nineTime: '',
      oneName: '',
      twoName: '',
      threeName: '',
      fourName: '',
      fiveName: '',
      sixName: '',
      sevenName: '',
      eightName: '',
      nineName: '',
      oneOperation: '',
      twoOperation: '',
      threeOperation: '',
      fourOperation: '',
      fiveOperation: '',
      sixOperation: '',
      sevenOperation: '',
      eightOperation: '',
      nineOperation: '',
    }
  },
  // 方法集合
  methods: {
    getInfo (id) {
      this.$axios.post(this.$api.unqualifiedHandler.getOaFlow, {
        id: id
      }).then(res => {
        if (res.code === 200) {
          const data = res.data
          if (data.length > 0) {
            data.forEach(item => {
              if (item.nodeName === '1检验员') {
                this.oneInfo = item.approvalOpinion
                this.oneTime = item.approvalDate
                this.oneName = item.approver
                this.oneOperation = item.operation
              } else if (item.nodeName === '2检测主管确认') {
                this.twoInfo = item.approvalOpinion
                this.twoTime = item.approvalDate
                this.twoName = item.approver
                this.twoOperation = item.operation
              } else if (item.nodeName === '3物流部确认') {
                this.threeInfo = item.approvalOpinion
                this.threeTime = item.approvalDate
                this.threeName = item.approver
                this.threeOperation = item.operation
              } else if (item.nodeName === '4产品工程师处理意见') {
                this.fourInfo = item.approvalOpinion
                this.fourTime = item.approvalDate
                this.fourName = item.approver
                this.fourOperation = item.operation
              } else if (item.nodeName === '5.总工或者副经理的处理意见') {
                this.fiveInfo = item.approvalOpinion
                this.fiveTime = item.approvalDate
                this.fiveName = item.approver
                this.fiveOperation = item.operation
              } else if (item.nodeName === '6质量部') {
                this.sixInfo = item.approvalOpinion
                this.sixTime = item.approvalDate
                this.sixName = item.approver
                this.sixOperation = item.operation
              } else if (item.nodeName === '7质量部经理') {
                this.sevenInfo = item.approvalOpinion
                this.sevenTime = item.approvalDate
                this.sevenName = item.approver
                this.sevenOperation = item.operation
              } else if (item.nodeName === '8核算员') {
                this.eightInfo = item.approvalOpinion
                this.eightTime = item.approvalDate
                this.eightName = item.approver
                this.eightOperation = item.operation
              } else if (item.nodeName === '9物流部索赔结果') {
                this.nineInfo = item.approvalOpinion
                this.nineTime = item.approvalDate
                this.nineName = item.approver
                this.nineOperation = item.operation
              }
            })
          }
        }
      }).catch(err => {
        this.submitDeclareLoading = false
        console.log(err)
      })
    }
  },
}
</script>

<style scoped>
.table {
  width: 100%;
  height: 500px;
}
.th-title {
  width: 160px;
  text-align: left;
  background-color: #e0eaf5;
  font-size: 16px;
}
.th-titleSec {
  width: 70px;
  background-color: #e0eaf5;
  font-size: 16px;
}
.th-info {
  width: 210px;
  text-align: left;
  font-size: 16px;
}
.th-info1 {
  width: 100px;
  text-align: center;
  font-size: 16px;
}
</style>
