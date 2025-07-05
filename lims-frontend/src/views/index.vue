<template>
  <div class="index">
    <el-row :gutter="25">
      <el-col :lg="14" :md="14" :sm="24" :xl="14" :xs="24">
        <div class="left-1">
          <div class="say">
            <div style="display: flex;align-items: center;flex-wrap: wrap;">
              <div class="say-1">{{nickName}} 您好！祝您开心每一天</div>
              <div class="say-2">当前时间： {{now}}</div>
            </div>
          </div>
        </div>
        <div class="left-2 card" v-loading="workLoading" style="overflow: hidden;">
          <div class="left-2-item" v-for="(item,index) in workDay" :key="index">
            <div class="left-item-title">
              <span style="font-size: 18px;">{{ item }}</span>
              <span style="font-size: 14px;color: #999999;">{{ weekdays[index] }}</span>
              <el-tag style="margin-top: 6px;" size="small">{{workList[index].length}} 条</el-tag>
            </div>
            <div class="left-item-body">
              <div class="body-item" v-for="(m,i) in workList[index]" :key="i" :class="{color0:m.type==0,color1:m.type==1,color2:m.type==2}">
                <div>
                  <span style="font-size: 12px;margin-bottom: 17px;">{{ m.text }}</span>
                  <div style="display: flex">
                    <span class="body-item-name">{{ m.name }}</span>
                    <span v-if="m.state == 0" class="body-item-insState" style="background-color: #909399;font-size: 12px;">待检验</span>
                    <span v-if="m.state == 1" class="body-item-insState" style="background-color: #E6A23C;font-size: 12px;">检验中</span>
                    <span v-if="m.state == 2" class="body-item-insState" style="background-color: #67C23A;font-size: 12px;">已检验</span>
                    <span v-if="m.state == 3" class="body-item-insState" style="background-color: #E6A23C;font-size: 12px;">待复核</span>
                    <span v-if="m.state == 4" class="body-item-insState" style="background-color: #F56C6C;font-size: 9px;">复核未通过</span>
                    <span v-if="m.state == 5" class="body-item-insState" style="background-color: #67C23A;font-size: 10px;">复核通过</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :lg="10" :md="10" :sm="24" :xl="10" :xs="24">
        <div class="right-2 card">
          <div style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 10px">
            <span style="color: #333333;font-size: 16px;">工时统计</span>
            <el-date-picker
              v-model="time.month"
              type="month"
              date-format="yyyy-MM"
              value-format="yyyy-MM"
              placeholder="选择月" size="mini" @change="changeTime" style="width: 130px;">
            </el-date-picker>
          </div>
          <el-row :gutter="10" style="width: 100%;">
            <el-col :span="12">
              <div class="right-time-1">
                <div style="text-align: center;font-size: 14px;color: #606266;line-height: 60px;">总工时(小时)</div>
                <div style="text-align: center;font-family: DIN Alternate, DIN Alternate;font-weight: 700;font-size: 26px;color: #3D3D3D;line-height: 40px;">{{totalHour}}</div>
              </div>
            </el-col>
            <el-col :span="12">
              <Echarts ref="chart"
                       :legend="pieLegend"
                       :series="materialPieSeries"
                       :tooltip="pieTooltip"
                       style="height: 150px;width: 100%;"></Echarts>
            </el-col>
          </el-row>
        </div>
        <div class="right-3 card" style="overflow: hidden;">
          <div class="right-3-tab">
            <div class="tab-item">待办事项</div>
            <div class="tab-item2">{{'您有' + total + '项任务待完成'}}</div>
          </div>
          <div v-loading="listLoading" class="right-3-list">
            <scroll-pagination v-if="list.length>0||listLoading" :finishLoding="finishLoding" :list="list" @load="getList">
              <div v-for="(m,i) in list" :key="i" class="list3-item">
                <div class="list3-item-title">
                  <span class="blue-dot">{{ m.theme }}</span>
                  <el-link class="blue-dot1" @click="goAddList(m)">查看</el-link>
                </div>
                <div class="list3-item-info">
                  <div class="ellipsis-multiline" @click="goNoticeDetail(m)">
                    {{ m.content }}
                  </div>
                  <div class="createTime-info">时间：{{ m.createTime }}</div>
                </div>
              </div>
            </scroll-pagination>
            <div v-if="list.length<1&&!listLoading" style="color:#909399;font-size:14px;text-align: center;margin-top:80px" >暂无数据</div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ScrollPagination from '@/components/index/scroll-paging.vue'
import {
  calendarWorkByWeek,
  currentUserWorkHourCount,
  msgRoll,
  page,
  triggerModificationStatusToRead
} from "@/api/index/report";
import Echarts from "@/components/echarts/echarts.vue";
import {mapGetters} from "vuex";
export default {
  components: {
    Echarts,
    ScrollPagination,
  },
  dicts: ['sys_sub_lab'],
  data() {
    return {
      now: null,
      calendarValue: new Date(),
      workLoading:false,
      workList:[],
      workDay:[],
      weekdays:[],
      listScheduleByMe:[],
      scheduleLoading:false,
      list:[],
      currentPage:1,
      pageSize: 8, // 一页7条
      total: 0,
      listLoading: true, // 组件loading的展示,默认为true
      finishLoding: false, // 加载完成，显示已经没有更多了
      timer:null,
      keyMap:{},
      sonLaboratory:null,
      totalHour: 0,
      materialPieSeries: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['40%', '60%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            alignTo: 'edge',
            edgeDistance: 10,
            lineHeight: 15,
            formatter: '{name|{b}}\n{time|{c} 小时}',
            rich: {
              time: {
                fontSize: 10,
                color: '#999'
              }
            },
          },
          labelLine: {
            length: 10,
            length2: 10,
          },
          data: [
            { value: 0, name: '辅助工时', itemStyle: {color: '#A4EEDA'} },
            { value: 0, name: '产量工时', itemStyle: {color: '#86C1F4'} },
          ]
        }
      ],
      pieLegend: {show: false},
      pieTooltip: {
        trigger: 'item'
      },
      type:'月',
      time:{
        week:'',
        month:'',
        year:'',
      },
    }
  },
  computed: {
    ...mapGetters(['nickName'])
  },
  mounted() {
    this.nowTime()
    setInterval(() => {
      this.nowTime()
    }, 1000)
    this.weekdays = this.getWeekdaysForNextWeek()
    this.init();
    this.currentPage = 1;
    this.keyMap = {}
    this.list = [];
    this.getList();
    this.initEchart()
    this.timer&&clearInterval(this.timer)
    this.timer = setInterval(() => {
      this.init();
      this.currentPage = 1;
      this.keyMap = {}
      this.list = [];
      this.getList();
    },1000*60*10)
  },
  methods: {
    // 跳转页面
    goAddList(m) {
      this.$router.push({name: m.jumpPath, query: { activeName: m.jumpId }})
      this.changeStatus(m)
    },
    changeStatus (m) {
      triggerModificationStatusToRead({id: m.id}).then(res => {
        console.log(res.data)
      })
    },
    getList(){
      const key = `_${this.currentPage}`
      const value = this.keyMap[key]
      // 如果value存在，表示缓存有值，那么阻止请求
      if(value) {
        return
      }
      // value不存在，表示第一次请求,设置占位
      this.keyMap[key] = 'temp'
      if(this.currentPage==1){
        this.listLoading = true
      }
      if(this.list.length==0){
        this.finishLoding = false;
      }
      const params = {
        size: this.pageSize,
        currentPage: this.currentPage,
      }
      msgRoll(params).then(res => {
        let list = res.data.records;
        this.total = res.data.total;
        if(list.length==0){
          this.finishLoding = true;
        }else{
          if(list.length<this.pageSize){
            this.finishLoding = true;
          }
          this.list = this.list.concat(list)
          if(this.total==this.list.length){
            this.finishLoding = true;
          }
          this.currentPage++;
        }
        this.listLoading = false
      }).catch(err=>{
        this.listLoading = false
      })
    },
    nowTime() {
      var date = new Date();
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      var d = date.getDate();
      var h = date.getHours();
      this.timeH = h
      var min = date.getMinutes();
      var s = date.getSeconds();
      if (s < 10) {
        s = "0" + s;
      }
      if (min < 10) {
        min = "0" + min;
      }
      if (h < 10) {
        h = "0" + h;
      }
      if (d < 10) {
        d = "0" + d;
      }
      if (m < 10) {
        m = "0" + m;
      }
      this.now = y + "-" + m + "-" + d + "  " + h + ":" + min + ":" + s;
    },
    // 获取日历任务图
    init(){
      this.workLoading = true;
      calendarWorkByWeek().then(res => {
        this.workLoading = false;
        this.workList = [];
        for(let i=0;i<7;i++){
          this.workList.push(res.data[`work${i}`])
        }
        this.workDay = res.data.weekDays.map(m=>{
          let arr = m.split('-')
          return arr[2]
        })
      }).catch(err=>{
        this.workLoading = false;
      })
    },
    getWeekdaysForNextWeek() {
      let weekdays = [];
      for (let i = 0; i < 7; i++) {
        let date = new Date();
        date.setDate(date.getDate() - i); // 今天之后的日子
        let day = date.getDay(); // 0-6 代表星期日到星期六
        weekdays.push(day);
      }
      weekdays.reverse()
      weekdays = weekdays.map(m=>{
        let day =''
        switch (m) {
          case 0:
            day = '星期天'
            break;
          case 1:
            day = '星期一'
            break;
          case 2:
            day = '星期二'
            break;
          case 3:
            day = '星期三'
            break;
          case 4:
            day = '星期四'
            break;
          case 5:
            day = '星期五'
            break;
          case 6:
            day = '星期六'
            break;
        }
        return day
      })
      return weekdays;
    },
    goNoticeDetail(row){
      // this.$axios.put(this.$api.informationNotification.triggerModificationStatusToRead+'/'+row.id).then(res => {
      //   row.num = Math.random(100);
      //   this.$bus.$emit("change", JSON.stringify(row));
      //   this.$parent.addTab({
      //     v: "消息详情",
      //     i: "el-icon-s-tools",
      //     u: "notice-detail",
      //     k:35,
      //     p: "abcd"
      //   },29);
      // })
    },
    changeTime(m){
      this.initEchart(m)
    },
    async initEchart(m){
      let month = ''
      if (m) {
        month = m
      } else {
        // 获取时间
        const nowDate = new Date();
        // 分别获取年、月
        const date = {
          year: nowDate.getFullYear(),
          month: nowDate.getMonth() + 1,
        };
        // 月份为单数时十位显示0
        const newMonth = date.month >= 10 ? date.month : "0" + date.month;
        // 返回格式为yyyymm的年月
        month = date.year + '-' + newMonth;
      }
      const params = {
        month: month,
      }
      currentUserWorkHourCount(params).then(res => {
        this.totalHour = res.data.totalHour
        this.materialPieSeries[0].data[0].value = res.data.subsidiaryHour
        this.materialPieSeries[0].data[1].value = res.data.yieldHour
      })
    },
  },
  deactivated(){
    this.timer&&clearInterval(this.timer)
  }
}
</script>

<style scoped>
.index {
  width: 100%;
  height: calc(100% - 50px);
  overflow-y: auto;
  padding: 14px;
  background: #F5F7FB;
}

.left-1 {
  background-image: url("~@/assets/index_image/index-img1.png");
  background-size: 100% 100%;
  height: 118px;
  margin-bottom: 16px;
  border-radius: 16px;
}
.right-time-1 {
  background: url("~@/assets/images/bg1.png") no-repeat;
  background-size: 100% 100%;
  padding: 4px 0 4px 50px;
  box-sizing: border-box;
  width: 100%;
  height: 122px;
}

.left-1 .say {
  height: 100%;
  display: flex;
  align-items: center;
  margin-left: 15%;
  width: 45%;
}

.left-1 .say div {
  color: #fff;
  margin: 4px 0;
}

.left-1 .say-1 {
  font-size: 18px;
}

.left-1 .say-2 {
  font-size: 17px;
}

::-webkit-scrollbar {
  width: 0px;
}

::-webkit-scrollbar-thumb {
  background-color: transparent;
  border-radius: 3px;
}

.card{
  background: #FFFFFF;
  border-radius: 16px;
  width: 100%;
  box-sizing: border-box;
}

.right-1-item .mun{
  margin-left: 6px;
  color: #0166E2;
}

.right-2{
  padding: 10px;
  margin-bottom: 20px;
}
>>>.el-calendar__header {
  display: none;
}
>>>.el-calendar__body{
  padding: 0;
}
>>>.el-calendar-table:not(.is-range) td.next {
  /*隐藏下个月的日期*/
  display: none;
}
>>>.el-calendar-day{
  height: 40px;
}
>>>.el-calendar-table td{
  border: 0;
  font-size: 14px;
}
>>>.el-calendar-table tr td:first-child{
  border-left: 0;
}
.right-3 {
  height: 600px;
}
.right-3-tab{
  padding: 20px 20px 10px 20px;
  display: flex;
  justify-content: space-between;
}
.tab-item{
  font-weight: 500;
  font-size: 18px;
  color: #3D3D3D;
  line-height: 25px;
  text-align: left;
  font-style: normal;
  text-transform: none;
}
.tab-item2{
  font-weight: 500;
  font-size: 14px;
  color: #FD8504;
}
.right-3-list{
  padding: 0 20px 20px 20px;
  box-sizing: border-box;
  height: 544px;
  overflow-y: auto;
}
.list3-item{
  padding: 10px;
  margin: 8px 0;
  height: 94px;
  background: #F7F7F7;
}
.list3-item:hover{
  background: rgba(58, 123, 250,0.05);
}
.list3-item-title{
  margin-bottom: 6px;
  font-weight: 600;
  font-size: 14px;
  color: #3D3D3D;
  display: flex;
  justify-content: space-between;
}
.blue-dot::before {
  content: "•"; /* 这是小蓝点的字符 */
  color: #3A7BFA; /* 设置颜色为蓝色 */
  font-size: 20px; /* 设置大小 */
  line-height: 20px;
  margin-right: 5px; /* 与<span>内容之间的间距 */
  vertical-align: middle;
}
.blue-dot1 {
  font-weight: 500;
  font-size: 14px;
  color: #3A7BFA;
}
.list3-item-info{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-weight: 400;
  font-size: 14px;
  color: #3D3D3D;
  height: 60%;
}
.ellipsis-multiline {
  font-weight: 400;
  font-size: 14px;
  color: #3D3D3D;
}
.createTime-info {
  font-weight: 400;
  font-size: 12px;
  color: #9F9F9F;
}
.left-2{
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  height: 777px;
}
.left-2-item{
  border-right: 1px solid #F1F1F1;
  overflow: hidden !important;
}
.left-2-item:nth-last-child(1){
  border-right: 0;
}
.left-2-item:hover{
  background: rgba(58,123,250,0.05);
}
.left-2-item:hover .left-item-title span{
  color: #3A7BFA !important;
}
.left-item-title{
  border-bottom: 1px solid #F1F1F1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
.left-item-body{
  box-sizing: border-box;
  padding: 5px;
  height: 693px;
  overflow-y: auto;
}
.body-item{
  background: #70A090;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 8px;
}
.body-item.color0{
  background: #70A090;
}
.body-item.color1{
  background: #EBD476;
}
.body-item.color2{
  background: #FF3838;
}
.body-item>div{
  height: calc(100% - 16px);
  margin-top: 16px;
  background: rgba(255, 255, 255,0.9);
  padding: 8px 8px 16px;
  display: flex;
  flex-direction: column;
}
>>>.el-calendar-day span{
  display: inline-block;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 4px;
}
>>>.is-selected{
  background: transparent !important;
}
>>>.is-selected .el-calendar-day{
  background: transparent !important;
}
>>>.is-selected .el-calendar-day span{
  background: #3A7BFA !important;
  color: #fff !important;
}
>>>.el-calendar-day:hover{
  background: transparent;
}
>>>.el-calendar-day span:hover{
  background: #3A7BFA !important;
  color: #fff !important;
}
>>>.el-tag--dark{
  border: 0;
}
.body-item-name {
  display: inline-block;
  height: 22px;
  width: 60px;
  border-radius: 10px;
  line-height: 22px;
  text-align: center;
  background: #C0C4CC;
  color: #fff;
  font-size: 12px
}
.body-item-insState {
  margin-left: 2px;
  display: inline-block;
  height: 22px;width: 60px;
  border-radius: 10px;
  line-height: 22px;
  text-align: center;
  color: #fff;
  font-size: 12px
}
</style>
