<template>
  <div class="class-page">
    <div class="search">
      <div class="search_thing">
        <div class="search_label">选择时间：</div>
        <div class="search_input">
          <el-date-picker v-model="query.year" type="year" size="small" format="yyyy" placeholder="选择年"
            @change="refreshTable()" style="width: 140px" :clearable="false">
          </el-date-picker>
          <el-select v-model="query.month" clearable placeholder="选择月" style="width: 140px; margin-left: 16px"
            size="small" @change="refreshTable()">
            <el-option v-for="item in monthOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-input v-model="query.userName" placeholder="请输入人员名称" size="small" style="width: 140px; margin: 0 16px"
            clearable @keyup.enter.native="refreshTable()"></el-input>
          <el-select v-model="query.laboratory" placeholder="请选择实验室" style="width: 140px" size="small" clearable
            @change="refreshTable()">
            <el-option v-for="item in laboratory" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="search_thing" style="padding-left: 30px">
        <el-button size="mini" type="primary" @click="refreshTable()"> 询</el-button>
        <el-button size="mini" @click="refresh()">重置</el-button>
      </div>
      <div class="search_thing btns" style="padding-left: 30px">
        <el-button size="small" type="primary" v-if="checkPermi(['performance:class:time'])"
          @click="configTime">时间配置</el-button>
        <el-button size="small" type="primary" v-if="checkPermi(['performance:class:down'])" @click="handleDown"
          :loading="downLoading">导 出</el-button>
        <el-button size="small" type="primary" @click="schedulingVisible = true"
          v-if="checkPermi(['performance:class:add'])">排 班</el-button>
      </div>
    </div>
    <div class="center" v-loading="pageLoading">
      <div class="clearfix" style="width: 100%" v-show="query.month">
        <div class="fixed-left">
          <div class="content-title" style="padding-left: 16px; box-sizing: border-box">
            人员名称
          </div>
          <div class="content-user" :class="{ hoverType: currentUserIndex == index }" v-for="(item, index) in list"
            :key="'e' + index" v-on:mouseenter="onMouseEnter(index)" v-on:mouseleave="currentUserIndex = null">
            <div class="user-pic">
              {{ item.name ? item.name.charAt(0) : "" }}
            </div>
            <div class="user-info">
              <p style="
                  font-size: 14px;
                  color: #3a7bfa;
                  line-height: 24px;
                  margin: 0;
                ">
                {{ item.name }}
              </p>
              <p style="
                  color: #999999;
                  font-size: 12px;
                  transform: scale(0.8) translateX(-20px);
                  white-space: nowrap;
                  width: 150px;
                  overflow-x: show;
                  margin: 0;
                ">
                早:{{ item.day0 }},中:{{ item.day1 }},夜:{{ item.day2 }},休:{{
                  item.day3
                }},假:{{ item.day4 }},差:{{ item.day6 }}
              </p>
              <p style="margin-top: 4px; margin: 0">
                <span style="
                    color: #999999;
                    font-size: 12px;
                    display: inline-block;
                    transform: scale(0.8) translateX(-10px);
                  ">合计出勤: </span><span style="font-size: 16px; color: #ff4902">{{
                    query.month
                      ? item.monthlyAttendance.totalAttendance
                      : item.sidebarAnnualAttendance.totalAttendance
                  }}天</span>
              </p>
            </div>
          </div>
        </div>
        <div class="scroll-right">
          <div class="content">
            <div class="content-title content-title-right" style="border-bottom: 0">
              <div class="content-title-item" v-for="(item, index) in weeks" :key="'b' + index">
                <span class="month" style="position: absolute; top: 0px" v-if="item.week == '周日'">{{ item.weekNum
                  }}周</span>
                <p style="height: 26px; position: absolute; bottom: 12px">
                  <span class="day">{{ item.day }}</span>
                  <span class="week">{{ item.week.charAt(1) }}</span>
                </p>
              </div>
            </div>
            <div class="content-body" v-for="(item, index) in list" :key="'c' + index"
              v-on:mouseenter="onMouseEnter(index)" v-on:mouseleave="currentUserIndex = null">
              <div class="content-body-item" v-for="(m, i) in item.list" :key="'d' + i"
                :class="{ hoverType: currentUserIndex == index }">
                <el-dropdown trigger="click" placement="bottom" @command="(e) => handleCommand(e, m)"
                  :disabled="!checkPermi(['performance:class:edit'])"
                  style="width: 100%; height: 100%; cursor: pointer">
                  <div class="work-box" :class="{
                    type0: m.shift === '0',
                    type1: m.shift === '1',
                    type2: m.shift === '2',
                    type3: m.shift === '3',
                    type4: m.shift === '4',
                    type5: m.shift === '5',
                    type6: m.shift === '6',
                  }">
                    <span style="cursor: pointer" :style="`opacity: ${getShiftByDic(m.shift) == '无' ? 0 : 1
                      };`">{{ getShiftByDic(m.shift) }}</span>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="(n, j) in classType" :key="'h' + j" :command="n.dictValue">{{ n.dictLabel
                      }}</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="clearfix year-table" style="width: 100%" v-show="!query.month">
        <div class="fixed-left">
          <div class="content-title" style="padding-left: 16px; box-sizing: border-box">
            人员名称
          </div>
          <div class="content-user" :class="{ hoverType: currentUserIndex == index }" v-for="(item, index) in yearList"
            :key="'e' + index" v-on:mouseenter="onMouseEnter(index)" v-on:mouseleave="currentUserIndex = null">
            <div class="user-pic">
              {{ item.name ? item.name.charAt(0) : "" }}
            </div>
            <div class="user-info">
              <p style="
                  font-size: 14px;
                  color: #3a7bfa;
                  line-height: 24px;
                  margin: 0;
                ">
                {{ item.name }}
              </p>
              <p style="
                  color: #999999;
                  font-size: 12px;
                  transform: scale(0.8) translateX(-20px);
                  white-space: nowrap;
                  width: 150px;
                  overflow-x: show;
                  margin: 0;
                ">
                早:{{ item.day0 }},中:{{ item.day1 }},夜:{{ item.day2 }},休:{{
                  item.day3
                }},假:{{ item.day4 }},差:{{ item.day6 }}
              </p>
              <p style="margin-top: 4px; margin: 0">
                <span style="
                    color: #999999;
                    font-size: 12px;
                    display: inline-block;
                    transform: scale(0.8) translateX(-10px);
                  ">合计出勤: </span><span style="font-size: 16px; color: #ff4902">{{ item.work_time }}天</span>
              </p>
            </div>
          </div>
        </div>
        <div class="scroll-right">
          <div class="content">
            <div>
              <div class="content-title content-title-right" style="border-bottom: 0; height: 52px" :style="`display: grid;
                grid-template-columns: repeat(${monthList.length}, 1fr);`">
                <div class="content-title-item" v-for="(item, index) in monthList" :key="'b' + index"
                  style="height: 52px">
                  <span class="month">{{ item }}月</span>
                </div>
              </div>
              <div class="content-body" v-for="(item, index) in yearList" :key="'c' + index"
                v-on:mouseenter="onMouseEnter(index)" v-on:mouseleave="currentUserIndex = null" :style="`display: grid;
              grid-template-columns: repeat(${monthList.length}, 1fr);`">
                <div class="content-body-item" v-for="(m, i) in item.monthList" :key="'d' + i"
                  :class="{ hoverType: currentUserIndex == index }">
                  <p style="color: rgb(153, 153, 153); font-size: 12px">
                    合计出勤：<span style="font-size: 14px; color: #000">{{
                      m.totalMonthAttendance
                    }}</span>
                  </p>
                  <p style="color: rgb(153, 153, 153); font-size: 12px">
                    早:{{ m.day0 }},中:{{ m.day1 }},夜:{{ m.day2 }},休:{{
                      m.day3
                    }},假:{{ m.day4 }},差:{{ m.day6 }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-pagination background @current-change="currentChange" :page-size="pageSize" :current-page="currentPage"
      layout="total, prev, pager, next, jumper" :total="total"
      style="margin-top: 10px; text-align: right; margin-right: 30px">
    </el-pagination>

    <el-dialog title="时间配置" :visible.sync="configTimeVisible" width="620px">
      <div v-loading="configTimeVisibleLoading" style="min-height: 200px">
        <div v-for="(item, index) in timeQuery">
          <div class="form" style="display: flex; justify-content: space-between">
            <div style="margin-bottom: 12px; width: 200px">
              <span class="form_label">班次：</span>
              <span v-if="!item.isEdit"> {{ item.type }} </span>
              <span class="form_input" v-if="item.isEdit">
                <el-select v-model="item.shift" placeholder="请选择" style="width: 70%; margin-right: 8px" clearable
                  size="small">
                  <el-option v-for="obj in timeTypeList" :key="obj.dictValue" :label="obj.dictLabel"
                    :value="obj.dictValue">
                  </el-option>
                </el-select>
              </span>
            </div>
            <div style="width: calc(100% - 260px)">
              <span class="form_label">时间：</span>
              <span v-if="!item.isEdit">
                {{
                  item.startTime && item.endTime
                    ? item.startTime + "~" + item.endTime
                    : ""
                }}
              </span>
              <span class="form_input" v-if="item.isEdit">
                <el-time-select placeholder="起始时间" v-model="item.startTime" size="small" :picker-options="{
                  start: '00:00',
                  step: '00:15',
                  end: '24:00',
                }" style="width: 120px">
                </el-time-select>
                <el-time-select style="width: 120px" placeholder="结束时间" v-model="item.endTime" size="small"
                  :picker-options="{
                    start: '00:00',
                    step: '00:15',
                    end: '24:00',
                  }">
                </el-time-select>
              </span>
            </div>
            <span>
              <i class="el-icon-circle-check" v-if="item.isEdit"
                style="margin-left: 10px; color: #4b79f2; cursor: pointer" @click="saveEdit(item, index)"></i>
              <i class="el-icon-edit" v-if="!item.isEdit" style="margin-left: 10px; color: #4b79f2; cursor: pointer"
                @click="item.isEdit = true"></i>
              <i class="el-icon-delete" v-if="timeQuery.length > 1"
                style="margin-left: 10px; color: #ff4902; cursor: pointer" @click="deleteTime(item, index)"></i>
            </span>
          </div>
          <el-divider></el-divider>
          <div @click="addTimeForm" style="color: #4b79f2; cursor: pointer" v-if="index === timeQuery.length - 1">
            添加时间配置
          </div>
        </div>
        <div @click="addTimeForm" style="color: #4b79f2" v-if="timeQuery.length === 0">
          添加时间配置
        </div>
      </div>
    </el-dialog>
    <el-dialog title="排班" :visible.sync="schedulingVisible" width="400px">
      <div class="search_thing">
        <div class="search_label" style="width: 90px">
          <span style="color: red; margin-right: 4px">*</span>周次：
        </div>
        <div class="search_input" style="width: calc(100% - 90px)">
          <el-date-picker v-model="schedulingQuery.week" type="week" format="yyyy 第 WW 周" placeholder="选择周次"
            style="width: 100%">
          </el-date-picker>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label" style="width: 90px">
          <span style="color: red; margin-right: 4px">*</span>人员名称：
        </div>
        <div class="search_input" style="width: calc(100% - 90px)">
          <el-select v-model="schedulingQuery.userId" placeholder="请选择" style="width: 100%" multiple clearable
            collapse-tags>
            <el-option v-for="item in personList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label" style="width: 90px">
          <span style="color: red; margin-right: 4px">*</span>班次：
        </div>
        <div class="search_input" style="width: calc(100% - 90px)">
          <el-select v-model="schedulingQuery.shift" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in classType" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
            </el-option>
          </el-select>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="schedulingVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmScheduling" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getYearAndMonthAndDays } from "@/utils/date";
import {
  page,
  pageYear,
  add,
  list,
  shiftAdd,
  shiftUpdate,
  shiftRemove,
  exportFile,
  obtainItemParameterList,
  update,
  selectUserCondition,
} from "@/api/performance/class";
export default {
  name: 'Class',
  data() {
    return {
      query: {
        userName: "",
        laboratory: "",
        year: new Date(),
        month: new Date().getMonth() + 1,
        // month:''
      },
      monthOptions: [
        {
          value: 1,
          label: "1月",
        },
        {
          value: 2,
          label: "2月",
        },
        {
          value: 3,
          label: "3月",
        },
        {
          value: 4,
          label: "4月",
        },
        {
          value: 5,
          label: "5月",
        },
        {
          value: 6,
          label: "6月",
        },
        {
          value: 7,
          label: "7月",
        },
        {
          value: 8,
          label: "8月",
        },
        {
          value: 9,
          label: "9月",
        },
        {
          value: 10,
          label: "10月",
        },
        {
          value: 11,
          label: "11月",
        },
        {
          value: 12,
          label: "12月",
        },
      ],
      laboratory: [],
      weeks: [],
      classType: [],
      currentUserIndex: null,
      schedulingVisible: false,
      personList: [],
      loading: false,
      schedulingQuery: {
        week: "",
        userId: null,
        shift: "",
      },
      list: [],
      currentPage: 1, // 当前页
      pageSize: 6, // 一页10条
      total: 0,
      pageLoading: false, // 组件loading的展示,默认为true
      finishLoding: false, // 加载完成，显示已经没有更多了
      monthList: [],
      yearList: [],
      downLoading: false,
      configTimeVisible: false, // 时间配置弹框
      configTimeVisibleLoading: false, // 时间配置弹框loading
      timeTypeList: [],
      timeQuery: [],
    };
  },
  watch: {
    // 'query.year'(val){
    //   this.monthList = []
    //   if(val.getFullYear()==new Date().getFullYear()){
    //     for(let i=new Date().getMonth()+1;i>0;i--){
    //       this.monthList.push(i)
    //     }
    //   }else{
    //     for (let i=12;i>0;i--) {
    //       this.monthList.push(i)
    //     }
    //   }
    //   this.monthList.reverse()
    // },
    // 'query.month'(val){
    //   if(!val){
    //     this.currentPage = 1;
    //     this.yearList = []
    //     this.initYear()
    //   }
    // }
  },
  mounted() {
    this.selectEnumByCategory();
    this.obtainItemParameterList();
    this.getUsers();
    if (this.query.month) {
      this.init();
    } else {
      this.initYear();
    }
    this.monthList = [];
    for (let i = 12; i > 0; i--) {
      this.monthList.push(i);
    }
    this.monthList.reverse();
  },
  methods: {
    refresh() {
      this.list = [];
      this.yearList = [];
      this.currentPage = 1;
      this.query = {
        userName: "",
        laboratory: "",
        year: new Date(),
        month: new Date().getMonth() + 1,
      };
      if (this.query.month) {
        this.init();
      } else {
        this.initYear();
      }
    },
    refreshTable() {
      this.currentPage = 1;
      if (this.query.month) {
        this.list = [];
        this.init();
      } else {
        this.yearList = [];
        this.initYear();
      }
    },
    currentChange(num) {
      this.currentPage = num;
      if (this.query.month) {
        this.init();
      } else {
        this.initYear();
      }
    },
    transFromNumber(num) {
      let changeNum = [
        "零",
        "一",
        "二",
        "三",
        "四",
        "五",
        "六",
        "七",
        "八",
        "九",
      ]; //changeNum[0] = "零"
      let unit = ["", "十", "百", "千", "万"];
      num = parseInt(num);
      let getWan = (temp) => {
        let strArr = temp.toString().split("").reverse();
        let newNum = "";
        for (var i = 0; i < strArr.length; i++) {
          newNum =
            (i == 0 && strArr[i] == 0
              ? ""
              : i > 0 && strArr[i] == 0 && strArr[i - 1] == 0
                ? ""
                : changeNum[strArr[i]] + (strArr[i] == 0 ? unit[0] : unit[i])) +
            newNum;
        }
        return newNum;
      };
      let overWan = Math.floor(num / 10000);
      let noWan = num % 10000;
      if (noWan.toString().length < 4) noWan = "0" + noWan;
      return overWan ? getWan(overWan) + "万" + getWan(noWan) : getWan(num);
    },
    init() {
      this.pageLoading = true;
      let year = this.query.year.getFullYear();
      let month0 = this.query.month
        ? this.query.month
        : new Date().getMonth() + 1;
      let month = month0 > 9 ? month0 : "0" + month0;
      page({
        size: this.pageSize,
        current: this.currentPage,
        time: year + "-" + month + "-01 00:00:00",
        userName: this.query.userName,
        laboratory: this.query.laboratory,
      }).then((res) => {
        this.pageLoading = false;
        this.total = res.data.page.total;
        this.list = res.data.page.records.map((item) => {
          for (let key in item.monthlyAttendance) {
            let type = this.getDayByDic(key);
            if (type != undefined || type != null) {
              item[`day${type}`] = item.monthlyAttendance[key];
            }
          }
          return item;
        });
        let headerList = res.data.headerList;
        this.weeks = [];
        headerList.forEach((item) => {
          let obj = {
            weekNum: item.weekly,
            week: item.headerTime.split(" ")[1],
            day: item.headerTime.split(" ")[0],
          };
          this.weeks.push(obj);
        });
      }).catch(() => {
        this.pageLoading = false;
      })
    },
    initYear() {
      this.pageLoading = true;
      let year = this.query.year.getFullYear();
      pageYear({
        size: this.pageSize,
        current: this.currentPage,
        time: year + "-01-01 00:00:00",
        userName: this.query.userName,
        laboratory: this.query.laboratory,
      }).then((res) => {
        this.pageLoading = false;
        this.total = res.data.total;
        this.yearList = res.data.records.map((item) => {
          for (let key in item.year) {
            let type = this.getDayByDic(key);
            if (type != undefined || type != null) {
              item[`day${type}`] = item.year[key];
            }
          }
          item.monthList = [];
          for (let m in item.month) {
            let obj = {};
            for (let key in item.month[m]) {
              let type = this.getDayByDic(key);
              if (type != undefined || type != null) {
                obj[`day${type}`] = item.month[m][key];
              }
            }
            obj.totalMonthAttendance = item.month[m].totalMonthAttendance;
            item.monthList.push(obj);
          }
          return item;
        });
      });
    },
    onMouseEnter(index) {
      this.currentUserIndex = index;
    },
    confirmScheduling() {
      if (!this.schedulingQuery.week) {
        this.$message.error("请选择周次");
        return;
      }
      let time = this.schedulingQuery.week.getTime();
      let startWeek =
        getYearAndMonthAndDays(new Date(time - 24 * 60 * 60 * 1000)) +
        " 00:00:00";
      let endWeek =
        getYearAndMonthAndDays(new Date(time + 24 * 60 * 60 * 1000 * 5)) +
        " 00:00:00";
      if (
        !this.schedulingQuery.userId ||
        this.schedulingQuery.userId.length == 0
      ) {
        this.$message.error("请选择人员");
        return;
      }
      if (!this.schedulingQuery.shift) {
        this.$message.error("请选择班次");
        return;
      }
      this.loading = true;
      add({
        startWeek,
        endWeek,
        userId: this.schedulingQuery.userId.join(","),
        shift: this.schedulingQuery.shift,
      })
        .then((res) => {
          this.loading = false;
          this.$message.success("操作成功");
          this.schedulingVisible = false;
          this.schedulingQuery = {
            week: "",
            userId: null,
            shift: "",
          };
          this.refresh();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    configTime() {
      this.getDicts("sys_class_type").then((response) => {
        this.timeTypeList = response.data;
      });
      this.getTimeList();
      this.configTimeVisible = true;
    },
    getTimeList() {
      this.configTimeVisibleLoading = true;
      list()
        .then((res) => {
          if (res.data.length > 0) {
            res.data.forEach((item) => {
              item.isEdit = false;
              const index = this.timeTypeList.findIndex(
                (val) => val.dictValue === item.shift
              );
              if (index > -1) {
                item.type = this.timeTypeList[index].dictLabel;
              }
            });
            this.timeQuery = res.data;
          }
          this.configTimeVisibleLoading = false;
        })
        .catch((e) => {
          this.configTimeVisibleLoading = false;
          console.log("e--", e);
        });
    },
    addTimeForm() {
      this.timeQuery.push({
        type: "",
        shift: "",
        time: null,
        isEdit: true,
      });
    },
    saveEdit(item, index) {
      if (item.shift) {
        const index = this.timeTypeList.findIndex(
          (val) => val.dictValue === item.shift
        );
        if (index > -1) {
          item.type = this.timeTypeList[index].dictLabel;
        }
      }
      delete item.orderBy;
      delete item.time;
      const isEmpty = this.isObjectEmpty(item);
      if (isEmpty) {
        this.$message.error("请填写完整");
        return;
      }
      let newObj = {};
      newObj.shift = item.shift;
      newObj.startTime = item.startTime;
      newObj.endTime = item.endTime;
      if (item.id) {
        // 编辑
        newObj.id = item.id;
        shiftUpdate(newObj).then((res) => {
          this.$message.success("操作成功");
          this.getTimeList();
        });
      } else {
        // 新增
        shiftAdd(newObj).then((res) => {
          this.$message.success("操作成功");
          this.getTimeList();
        });
      }
    },
    deleteTime(item, index) {
      if (item.id) {
        shiftRemove({
          id: item.id,
        }).then((res) => {
          this.$message.success("操作成功");
          this.getTimeList();
        });
      } else {
        this.timeQuery.splice(index, 1);
      }
    },
    isObjectEmpty(obj) {
      return Object.keys(obj).some((key) => !obj[key]);
    },
    handleDown() {
      let year = this.query.year.getFullYear();
      let time = "";
      if (this.query.month) {
        let month =
          this.query.month > 9 ? this.query.month : "0" + this.query.month;
        time = year + "-" + month + "-01 00:00:00";
      } else {
        time = year + "-01-01 00:00:00";
      }
      this.downLoading = true;
      exportFile({
        time,
        userName: this.query.userName,
        laboratory: this.query.laboratory,
        isMonth: this.query.month ? true : false,
      })
        .then((res) => {
          this.$message.success("下载成功");
          this.downLoading = false;
          const blob = new Blob([res], {
            type: "application/force-download",
          });
          let fileName = "";
          if (this.query.month) {
            fileName = year + "-" + this.query.month + " 班次信息";
          } else {
            fileName = year + " 班次汇总";
          }
          this.$download.saveAs(blob, fileName + ".xlsx");
        })
        .catch((err) => {
          this.downLoading = false;
        });
    },
    selectEnumByCategory() {
      this.getDicts("sys_class_type").then((response) => {
        this.classType = response.data;
      });
    },
    obtainItemParameterList() {
      if (this.PROJECT == "检测中心") {
        this.laboratory = [
          {
            label: "通信产品实验室",
            value: "通信产品实验室",
          },
          {
            label: "电力产品实验室",
            value: "电力产品实验室",
          },
        ];
      } else {
        obtainItemParameterList().then((res) => {
          let data = [];
          res.data.forEach((a) => {
            data.push({
              label: a.laboratoryName,
              value: a.id,
            });
          });
          this.laboratory = data;
        });
      }
    },
    handleCommand(e, m) {
      if (e != m.shift) {
        update({
          id: m.id,
          shift: e,
        }).then((res) => {
          this.$message.success("操作成功");
          m.shift = e;
        });
      }
    },
    getUsers() {
      selectUserCondition({ type: 1 }).then((res) => {
        let arr = res.data;
        this.personList = arr;
      });
    },
    getDayByDic(e) {
      let obj = this.classType.find((m) => m.dictLabel == e);
      if (obj) {
        return obj.dictValue;
      }
    },
    getShiftByDic(e) {
      let obj = this.classType.find((m) => m.dictValue == e);
      if (obj) {
        return obj.dictLabel;
      }
      return "无";
    },
  },
};
</script>

<style scoped>
.class-page {
  padding: 10px;
}

.form_title {
  height: 36px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-weight: 800;
}

.search {
  height: 50px;
  display: flex;
  align-items: center;
  position: relative;
}

.search_thing {
  display: flex;
  align-items: center;
  height: 50px;
}

.search_label {
  width: 70px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  display: flex;
  align-items: center;
}

.btns {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translate(0, -50%);
}

.center {
  width: 100%;
  height: calc(100vh - 220px);
  background-color: #fff;
  overflow-y: auto;
  display: flex;
}

>>>.scroll-pagination {
  overflow-y: scroll;
  scrollbar-width: none;
}

>>>.scroll-pagination::-webkit-scrollbar {
  display: none;
}

.fixed-left {
  float: left;
  width: 220px;
  /* 左边区域宽度 */
  background-color: #fff;
  box-shadow: 2px -2px 5px rgba(51, 51, 51, 0.12);
  /* 左边阴影 */
}

.clearfix::after {
  content: "";
  display: table;
  clear: both;
}

.scroll-right {
  width: calc(100% - 220px);
  /* 减去左边区域宽度 */
  min-height: calc(100% - 10px);
  /* 视口高度 */
  margin-left: 220px;
  overflow-x: scroll;
}

.content {
  min-height: calc(100% - 10px);
  /* 视口高度 */
}

.content-title {
  height: 58px;
  line-height: 58px;
  border-bottom: 1px solid #eeeeee;
}

.content-title-right {
  display: flex;
  align-items: center;
}

.content-title-item {
  height: 100%;
  width: 50px;
  flex-shrink: 0;
  border-bottom: 1px solid #eeeeee;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  position: relative;
}

.content-title-item .month {
  font-size: 12px;
  color: #3a7bfa;
  box-sizing: border-box;
  padding: 0 1px;
  border-radius: 50%;
  background: #d6e4ff;
  text-align: center;
  line-height: 22px;
}

.content-title-item .day {
  font-size: 14px;
  color: #333333;
  margin-right: 4px;
}

.content-title-item .week {
  font-size: 12px;
  color: #999999;
}

.content-body {
  display: flex;
  align-items: center;
}

.content-body-item {
  height: 70px;
  width: 50px;
  flex-shrink: 0;
  font-size: 12px;
  box-sizing: border-box;
  padding: 4px;
  border-right: 1px solid #eeeeee;
  border-bottom: 1px solid #eeeeee;
}

.work-box {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
  background: #edeff2;
  border-radius: 8px 8px 8px 8px;
  color: #999;
  font-size: 14px;
}

.work-box.type0 {
  background: rgba(58, 123, 250, 0.15);
  color: #3a7bfa !important;
}

.work-box.type0 span {
  color: #3a7bfa !important;
}

.work-box.type1 {
  background: #e3dcfe;
  color: #635998 !important;
}

.work-box.type1 span {
  color: #635998 !important;
}

.work-box.type2 {
  background: #fae2ca;
  color: #bc8d5e !important;
}

.work-box.type2 span {
  color: #bc8d5e !important;
}

.work-box.type3 {
  background: #e1f3d8;
  color: #67c23a !important;
}

.work-box.type3 span {
  color: #67c23a !important;
}

.work-box.type4 {
  background: #fde2e2;
  color: #f56c6c !important;
}

.work-box.type4 span {
  color: #f56c6c !important;
}

.work-box.type5 {
  background: #ff46c145;
  color: #ff46c0 !important;
}

.work-box.type5 span {
  color: #ff46c0 !important;
}

.work-box.type6 {
  background: #00036418;
  color: #000464 !important;
}

.work-box.type6 span {
  color: #000464 !important;
}

.work-box-left {
  display: flex;
  justify-content: center;
  flex-direction: column;
  line-height: 24px;
}

.content-user {
  width: 100%;
  height: 70px;
  box-sizing: border-box;
  border-bottom: 1px solid #eeeeee;
  display: flex;
  align-items: center;
}

.user-pic {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: #c0c4cc;
  color: #fff;
  font-size: 20px;
  text-align: center;
  line-height: 50px;
  margin-left: 10px;
}

.user-info {
  flex: 1;
  margin-left: 10px;
}

.hoverType {
  background: rgba(58, 123, 250, 0.03);
}

.year-table {
  width: 100%;
}

.year-table .scroll-right {
  flex: 1;
}

.year-table .month {
  font-size: 14px;
  color: #3a7bfa;
  box-sizing: border-box;
  padding: 0 4px;
  border-radius: 50%;
  background: #d6e4ff;
  text-align: center;
  line-height: 30px;
}

.year-table .content-title-item {
  width: 100%;
}

.year-table .content-body-item {
  width: 100%;
  height: 70px;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
}
</style>
