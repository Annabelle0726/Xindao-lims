<template>
  <!--日工时管理-->
  <div class="capacity-scope">
    <div>
      <el-form :model="entity" ref="entity" size="small" :inline="true">
        <el-form-item label="星期" prop="weekDay" class="form-item">
          <el-select v-model="entity.weekDay" clearable placeholder="全部" size="small" @change="refreshTable()">
            <el-option v-for="item in weekList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围" prop="dateTime" class="dateTime">
          <el-date-picker v-model="entity.dateTime" clearable end-placeholder="结束日期" format="yyyy-MM-dd"
                          range-separator="至" size="small" start-placeholder="开始日期" type="daterange"
                          style="width: 100%"
                          value-format="yyyy-MM-dd" @change="refreshTable()">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="entity.name" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="state" v-if="currentTable == 'ValueTable0'">
          <el-select v-model="entity.state" placeholder="全部" size="small" @change="refreshTable()" clearable>
            <el-option v-for="item in stateList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="样品编号" prop="sample" v-if="currentTable == 'ValueTable1'">
          <el-input v-model="entity.sample" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="display: flex; align-items: center; justify-content: space-between;margin-bottom: 10px">
      <el-radio-group :key="'111'" v-model="currentTable" size="small" @change="searchList">
        <el-radio-button label="ValueTable0"> 辅助工时 </el-radio-button>
        <el-radio-button label="ValueTable1"> 产量工时 </el-radio-button>
      </el-radio-group>
      <div style="display: flex; align-items: center">
        <el-button v-show="currentTable == 'ValueTable0' &&
          checkPermi(['performance:manHour:workTimeManagement:add'])
          " size="small" type="primary" @click="openAdd">录入数据</el-button>
        <el-button v-if="down && currentTable === 'ValueTable0'" :loading="outLoading" size="small" type="primary"
          @click="handleOut">导 出</el-button>
        <el-button v-if="down && currentTable === 'ValueTable1'" :loading="outLoading" size="small" type="primary"
          @click="handleOut1">导 出</el-button>
        <el-button v-show="currentTable == 'ValueTable0' &&
          checkPermi(['performance:manHour:workTimeManagement:add'])
          " size="small" type="primary" @click="openBatchCheck(1)">批量批准</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
                  key="tableData" :height="'calc(100vh - 330px)'"
                  :isSelection="true" v-if="currentTable == 'ValueTable0'"
                  @pagination="pagination" :handleSelectionChange="handleSelectionChange"></lims-table>
      <lims-table :tableData="tableData0" :column="column0" :page="page0" :tableLoading="tableLoading"
                  key="tableData0" :height="'calc(100vh - 330px)'"
                  v-if="currentTable == 'ValueTable1'" @pagination="pagination0"></lims-table>
    </div>
    <el-dialog :before-close="handleClose" :title="formData.id ? '编辑' : '录入数据'" :visible.sync="addVisible"
      width="600px">
      <el-row style="display: flex; justify-content: space-around">
        <el-col :span="12">
          <el-form :model="formData" label-width="90px">
            <el-form-item label="录入时间:">
              <el-radio-group v-model="formData.dateTime" size="small">
                <el-radio :label="getYearAndMonthAndDays() + ' 00:00:00'">今天</el-radio>
                <el-radio :label="getYearAndMonthAndDays(
                  new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
                ) + ' 00:00:00'
                  ">昨天</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="年份:">
              <el-input v-model="formData.year" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="周次:">
              <el-input v-model="formData.week" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="星期:">
              <el-select v-model="formData.weekDay" disabled placeholder="请选择" size="small">
                <el-option v-for="item in weekList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="班次:">
              <el-input v-model="formData.shift" disabled size="small"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <el-form :model="formData" label-width="90px">
            <el-form-item label="编号:" required>
              <el-input v-model="formData.number" size="small" @blur="getInfoByCode"></el-input>
            </el-form-item>
            <el-form-item label="数量:" required>
              <el-input v-model="formData.amount" size="small"></el-input>
            </el-form-item>
            <el-form-item label="核准工时:">
              <el-input v-model="formData.approvedWorkingHour" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="辅助工时:">
              <el-input v-model="formData.nonproductiveTime" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="辅助项目:">
              <el-input v-model="formData.auxiliaryProject" disabled size="small"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-form :model="formData" label-width="90px">
        <el-form-item label="辅助说明:">
          <el-input v-model="formData.remarks" :rows="3" size="small" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="handleClose">取 消</el-button>
          <el-button :loading="addLoad" type="primary" @click="submitAdd">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!--    批准弹框-->
    <el-dialog :title="title" :visible.sync="checkVisible" width="600px">
      <el-row style="display: flex; justify-content: space-around">
        <el-col :span="12">
          <el-form :model="formData0" label-width="90px">
            <el-form-item label="年份:">
              <el-input v-model="formData0.year" :disabled="title == '批准'" size="small"></el-input>
            </el-form-item>
            <el-form-item label="周次:">
              <el-input v-model="formData0.week" :disabled="title == '批准'" size="small"></el-input>
            </el-form-item>
            <el-form-item label="星期:">
              <el-select v-model="formData0.weekDay" :disabled="title == '批准'" placeholder="请选择" size="small">
                <el-option v-for="item in weekList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="姓名:">
              <el-input v-model="formData0.name" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="班次:">
              <el-select v-model="formData0.shift" :disabled="title == '批准'" placeholder="请选择" size="small">
                <el-option v-for="item in classType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="复核说明:">
              <el-input v-model="formData0.reviewerRemark" :rows="3" size="small" type="textarea"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <el-form :model="formData0" label-width="90px">
            <el-form-item label="编号:" required>
              <el-input v-model="formData0.number" :disabled="title == '批准'" size="small"></el-input>
            </el-form-item>
            <el-form-item label="数量:" required>
              <el-input v-model="formData0.amount" :disabled="title == '批准'" size="small"></el-input>
            </el-form-item>
            <el-form-item label="复核数量:" required>
              <el-input v-model="formData0.reviewerNumber" size="small"></el-input>
            </el-form-item>
            <el-form-item label="核准工时:">
              <el-input v-model="formData0.approvedWorkingHour" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="复核工时:">
              <el-input v-model="formData0.reviewerNonproductiveTime" disabled size="small"></el-input>
            </el-form-item>
            <el-form-item label="辅助项目:">
              <el-input v-model="formData0.auxiliaryProject" disabled size="small"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button :loading="checkLoadN" @click="submitCheck(0)">不批准</el-button>
          <el-button :loading="checkLoadY" type="primary" @click="submitCheck(1)">批 准</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog :before-close="closeBatchCheckDialog" :title="batchCheckTitle" :visible.sync="batchCheckDialog"
      width="30%">
      <span>{{ batchCheckDialogMessage }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchCheck(0)">不批准</el-button>
        <el-button type="primary" @click="batchCheck(1)">批 准</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getYearAndMonthAndDays } from "@/utils/date";
import {
  selectAuxiliaryWorkingHoursDay,
  selectAuxiliaryOutputWorkingHours,
  deleteAuxiliaryWorkingHoursDay,
  insertAuxiliaryWorkingHoursDay,
  updateAuxiliaryWorkingHoursDay,
  approve,
  collectWorkingHours,
  selectshiftByUser,
  selectAuxiliaryWorkingHoursByNumber,
  exportAssistantHours,
  exportOutputHours,
} from "@/api/performance/manHour";
import limsTable from "@/components/Table/lims-table.vue";
export default {
  components: {
    limsTable,
  },
  dicts: ["sys_class_type"],
  data() {
    return {
      entity: {
        dateTime: [],
      },
      down: false,
      addFileVisible: false,
      weekList: [
        { label: "周一", value: "1", type: "primary" },
        { label: "周二", value: "2", type: "primary" },
        { label: "周三", value: "3", type: "primary" },
        { label: "周四", value: "4", type: "primary" },
        { label: "周五", value: "5", type: "primary" },
        { label: "周六", value: "6", type: "primary" },
        { label: "周日", value: "0", type: "primary" },
      ],
      currentTable: "ValueTable0",
      addVisible: false,
      addLoad: false,
      formData: {
        dateTime: getYearAndMonthAndDays() + " 00:00:00",
        year: new Date().getFullYear(),
        week: this.getCurrentWeekNumber(),
        weekDay: this.getWeek(),
        shift: "",
        number: "",
        amount: "",
        approvedWorkingHour: "", // 核准工时
        nonproductiveTime: "",
        auxiliaryProject: "",
        remarks: "",
      },
      formData0: {
        dateTime: 0,
        year: new Date().getFullYear(),
        week: this.getCurrentWeekNumber(),
        weekDay: this.getWeek(),
        createUser: "",
        shift: "",
        number: "",
        reviewerNumber: "", // 复核数量
        approvedWorkingHour: "", // 核准工时
        reviewerNonproductiveTime: "", // 复核工时
        auxiliaryProject: "",
        reviewerRemark: "",
        nameUser: "",
        name: "",
      },
      checkVisible: false,
      checkLoadN: false,
      checkLoadY: false,
      title: "批准",
      classType: [],
      stateList: [
        {
          value: "已提交",
          label: "已提交",
        },
        {
          value: "已审核",
          label: "已审核",
        },
        {
          value: "已批准",
          label: "已批准",
        },
        {
          value: "不批准",
          label: "不批准",
        },
      ],
      totalInfo: null,
      auxiliaryWorking: null,
      outLoading: false,
      batchCheckTitle: "批量审核",
      batchCheckDialog: false,
      ValueTable0Selected: [],
      batchCheckDialogMessage: "",
      tableData: [],
      column: [
        { label: "姓名", prop: "name" },
        { label: "编号", prop: "number" },
        { label: "辅助项目名称", prop: "auxiliaryProject", width: "120px" },
        {
          label: "状态",
          prop: "state",
          dataType: "tag",
          formatType: (params) => {
            if (params == "已提交") {
              return "primary";
            } else if (params == "已审核") {
              return "warning";
            } else if (params == "已批准") {
              return "success";
            } else if (params == "不批准") {
              return "danger";
            }
          },
        },
        { label: "核准工时", prop: "approvedWorkingHour" },
        { label: "数量", prop: "amount" },
        { label: "辅助工时", prop: "nonproductiveTime" },
        { label: "辅助说明", prop: "remarks" },
        { label: "复核人", prop: "reviewer" },
        { label: "复核数量", prop: "reviewerNumber" },
        { label: "复核工时", prop: "reviewerNonproductiveTime" },
        { label: "复核说明", prop: "reviewerRemark" },
        { label: "年", prop: "year" },
        {
          label: "班次",
          prop: "shift",
          dataType: "tag",
          formatData: (params) => {
            if (this.shifList.find(m => m.value == params)) {
              return this.shifList.find(m => m.value == params).label
            } else {
              return null
            }
          },
        },
        { label: "周次", prop: "week" },
        {
          label: "星期",
          prop: "weekDay",
          dataType: "tag",
          formatData: (params) => {
            return this.weekList.find(m => m.value == params).label
          },
        },
        { label: "创建时间", prop: "createTime" },
        { label: "日期", prop: "dateTime" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleEdit(row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "performance:manHour:workTimeManagement:edit",
                ]);
              },
              disabled: (row) => {
                return row.state === "已批准";
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "sperformance:manHour:workTimeManagement:del",
                ]);
              },
              disabled: (row) => {
                return row.state === "已批准";
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleRatify(row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "performance:manHour:workTimeManagement:ratify",
                ]);
              },
              disabled: (row) => {
                return row.state === "已批准";
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 20,
        current: 1,
      },
      tableLoading: false,
      tableData0: [],
      column0: [
        { label: "检测人", prop: "name" },
        { label: "检测项分类", prop: "inspectionItemClass" },
        { label: "检测项", prop: "inspectionItem" },
        { label: "检测子项", prop: "inspectionItemSubclass" },
        { label: "样品编号", prop: "sample" },
        { label: "委托单号", prop: "orderNo" },
        { label: "电缆标识", prop: "cableTag" },
        { label: "产量工时", prop: "outputWorkTime" },
        { label: "工时分组", prop: "manHourGroup" },
        { label: "单价", prop: "price" },
        { label: "日期", prop: "dateTime" },
        { label: "周次", prop: "week" },
        { label: "星期", prop: "weekDay" },
      ],
      page0: {
        total: 0,
        size: 20,
        current: 1,
      },
      shifList: [],
    };
  },
  watch: {
    "formData.dateTime"(val) {
      if (val == getYearAndMonthAndDays() + " 00:00:00") {
        this.formData.year = new Date().getFullYear();
        this.formData.week = this.getCurrentWeekNumber();
        this.formData.weekDay = this.getWeek();
      } else {
        var today = new Date(); // 获取当前日期
        var yesterday = new Date(today); // 复制当前日期
        yesterday.setDate(today.getDate() - 1); // 设置为前一天
        var timestamp = new Date(yesterday.getTime());
        this.formData.year = timestamp.getFullYear();
        this.formData.week = this.getCurrentWeekNumber(timestamp);
        this.formData.weekDay = this.getWeek(timestamp);
      }
    },
    "formData.amount"(val) {
      if (val) {
        if (!isNaN(val)) {
          if (this.formData.approvedWorkingHour) {
            this.formData.nonproductiveTime =
              this.formData.approvedWorkingHour * val;
          }
        } else {
          this.$message.error("请输入数字");
          this.formData.amount = "";
        }
      }
    },
    "formData.approvedWorkingHour"(val) {
      if (val && this.formData.amount) {
        this.formData.nonproductiveTime = this.formData.amount * val;
      }
    },
    "formData0.reviewerNumber"(val) {
      if (val) {
        if (!isNaN(val)) {
          if (this.formData0.approvedWorkingHour) {
            this.formData0.reviewerNonproductiveTime =
              this.formData0.approvedWorkingHour * val;
          }
        } else {
          this.$message.error("请输入数字");
          this.formData0.reviewerNumber = "";
        }
      }
    },
    "formData0.approvedWorkingHour"(val) {
      if (val && this.formData0.reviewerNumber) {
        this.formData0.reviewerNonproductiveTime =
          this.formData0.reviewerNumber * val;
      }
    },
  },
  created() {
    this.setDate();
    this.getDicts("sys_class_type").then((response) => {
      this.shifList = response.data;
    });
  },
  mounted() {
    this.getCurrentWeekNumber();
    // this.collectWorkingHours();
    this.selectshiftByUser();
    this.searchList()
  },
  methods: {
    searchList() {
      if (this.currentTable == 'ValueTable0') {
        this.getList();
      } else {
        this.getList0();
      }
    },
    // 查询辅助工时列表
    getList(entity) {
      this.tableLoading = true;
      let param = {};
      if (entity) {
        param = { ...entity, ...this.page };
      } else {
        param = { ...this.entity, ...this.page };
        param.dateTime = param.dateTime?.length > 0 ? JSON.stringify(param.dateTime) : null;
      }
      delete param.total;
      selectAuxiliaryWorkingHoursDay({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData = res.data.records;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    // 查询产量工时列表
    getList0(entity) {
      this.tableLoading = true;
      let param = {};
      if (entity) {
        param = { ...entity, ...this.page0 };
      } else {
        param = { ...this.entity, ...this.page0 };
        param.dateTime = param.dateTime?.length > 0 ? JSON.stringify(param.dateTime) : null
      }
      delete param.total;
      delete param.state;
      selectAuxiliaryOutputWorkingHours({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData0 = res.data.records;
            this.page0.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    pagination0({ page, limit }) {
      this.page0.current = page;
      this.page0.size = limit;
      this.getList0();
    },
    /** 将时间选择器默认选为当天的日期 */
    setDate() {
      let currentDate = new Date();
      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, "0");
      let day = String(currentDate.getDate()).padStart(2, "0");
      let formattedDate1 = `${year}-${month}-${day}`;
      let formattedDate2 = `${year}-${month}-${day}`;
      this.entity.dateTime.push(formattedDate1);
      this.entity.dateTime.push(formattedDate2);
      this.refreshTable();
    },
    getYearAndMonthAndDays(date) {
      return getYearAndMonthAndDays(date);
    },
    refreshTable(e) {
      let entity = {};
      if (this.entity.week1 && this.entity.week2) {
        entity.week = JSON.stringify([this.entity.week1, this.entity.week2]);
      } else {
        entity.week = "";
      }
      if (this.entity.dateTime) {
        entity.dateTime = JSON.stringify(this.entity.dateTime);
      } else {
        entity.dateTime = "";
      }
      entity.weekDay = this.entity.weekDay;
      entity.name = this.entity.name;
      entity.sample = this.entity.sample;
      if (this.currentTable == "ValueTable0") {
        entity.state = this.entity.state;
        this.page.current = 1;
        this.getList(entity);
      } else {
        this.page0.current = 1;
        this.getList0(entity);
      }
      // this.collectWorkingHours();
    },
    refresh() {
      this.entity = {};
      this.entity.dateTime = []
      this.setDate()
      this.refreshTable();
    },
    openAdd() {
      this.formData = {
        dateTime: getYearAndMonthAndDays() + " 00:00:00",
        year: new Date().getFullYear(),
        week: this.getCurrentWeekNumber(),
        weekDay: this.getWeek(),
        shift: "",
        number: "",
        amount: "",
        approvedWorkingHour: "",
        nonproductiveTime: "",
        auxiliaryProject: "",
        remarks: "",
      };
      this.addVisible = true;
    },
    handleEdit(row) {
      this.formData = row;
      this.addVisible = true;
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteAuxiliaryWorkingHoursDay({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refreshTable("page");
          });
        })
        .catch(() => { });
    },
    handleClose() {
      this.addVisible = false;
    },
    submitAdd() {
      if (!this.formData.number) {
        this.$message.error("请输入编号");
        return;
      }
      if (!this.formData.amount) {
        this.$message.error("请输入数量");
        return;
      }
      this.addLoad = true;
      if (!this.formData.id) {
        insertAuxiliaryWorkingHoursDay(this.formData).then((res) => {
          this.addLoad = false;
          this.$message.success("已提交");
          this.formData.number = "";
          this.formData.amount = "";
          this.formData.remarks = "";
          this.formData.approvedWorkingHour = "";
          this.formData.nonproductiveTime = "";
          this.formData.auxiliaryProject = "";
          this.addVisible = false;
          this.refreshTable("page");
          // this.collectWorkingHours();
        });
      } else {
        updateAuxiliaryWorkingHoursDay(this.formData).then((res) => {
          this.addLoad = false;
          this.$message.success("操作成功");
          this.formData.number = "";
          this.formData.amount = "";
          this.formData.remarks = "";
          this.formData.approvedWorkingHour = "";
          this.formData.nonproductiveTime = "";
          this.formData.auxiliaryProject = "";
          this.refreshTable("page");
          // this.collectWorkingHours();
        });
      }
    },
    handleRatify(row) {
      this.title = "批准";
      this.formData0 = row;
      this.checkVisible = true;
      this.formData0.reviewerNumber = this.formData0.amount;
      if (this.formData0.approvedWorkingHour && this.formData0.reviewerNumber) {
        this.formData0.reviewerNonproductiveTime =
          this.formData0.approvedWorkingHour * this.formData0.reviewerNumber;
      }
    },
    handleChangeTask(row) {
      this.ValueTable0Selected = JSON.parse(JSON.stringify(row));
    },
    openBatchCheck(e) {
      if (this.ValueTable0Selected.length > 0) {
        const isApprove = this.ValueTable0Selected.every(
          (item) => item.state !== "已批准"
        );
        this.batchCheckTitle = "批量批准";
        this.batchCheckDialogMessage = "是否全部通过批准?";
        if (isApprove) {
          this.batchCheckDialog = true;
        } else {
          this.$message.error("请选择未批准的数据");
        }
      } else {
        this.$message.error("请选择至少一条数据");
      }
    },
    batchCheck(e) {
      const list = JSON.parse(JSON.stringify(this.ValueTable0Selected));
      const state = e === 0 ? "不批准" : "已批准";
      let auxiliaryWorkingHoursDays = [];
      list.forEach((item) => {
        delete item.orderBy;
        item.state = state;
        auxiliaryWorkingHoursDays.push(item);
      });
      approve({ auxiliaryWorkingHoursDays: auxiliaryWorkingHoursDays }).then(
        (res) => {
          this.submitBatchCheckDialog();
          this.$message.success("操作成功");
        }
      );
    },
    closeBatchCheckDialog() {
      this.batchCheckDialog = false;
    },
    submitBatchCheckDialog() {
      this.batchCheckDialog = false;
      this.refreshTable("page");
      // this.collectWorkingHours();
    },
    submitCheck(e) {
      if (!this.formData0.number) {
        this.$message.error("请输入编号");
        return;
      }
      if (!this.formData0.amount) {
        this.$message.error("请输入数量");
        return;
      }
      if (e == 0) {
        this.checkLoadN = true;
      } else {
        this.checkLoadY = true;
      }
      this.formData0.state = e == 0 ? "不批准" : "已批准";
      approve({
        auxiliaryWorkingHoursDays: [this.formData0],
      }).then((res) => {
        if (e == 0) {
          this.checkLoadN = false;
        } else {
          this.checkLoadY = false;
        }
        this.$message.success("操作成功");
        this.checkVisible = false;
        this.refreshTable("page");
        // this.collectWorkingHours();
      });
    },
    getCurrentWeekNumber(now = new Date()) {
      const startOfWeek = new Date(
        now.getFullYear(),
        now.getMonth(),
        now.getDate() - now.getDay()
      ); // 周天
      const firstWeekOfYear = new Date(startOfWeek.getFullYear(), 0, 0); // 当年的第一个周天
      const firstWeekOfNextYear = new Date(
        firstWeekOfYear.getFullYear() + 1,
        0,
        0
      ); // 下一年的第一个周天
      const daysSinceNewYear =
        (startOfWeek - firstWeekOfYear) / (24 * 60 * 60 * 1000); // 计算自新年以来的天数
      const weeksSinceNewYear = Math.floor(daysSinceNewYear / 7); // 天数除以7得到周数

      if (startOfWeek > now) {
        // 如果当前周的周天还没到，则返回上年的最后一周
        return weeksSinceNewYear;
      } else if (firstWeekOfNextYear <= now) {
        // 如果当前周的周天已经过了，则返回当年的周数
        return weeksSinceNewYear + 1;
      } else {
        // 否则返回当年的最后一周
        return weeksSinceNewYear + 2;
      }
    },
    getWeek(e = new Date()) {
      // let str = '日一二三四五六'
      let d = e.getDay();
      // let c = str.charAt(d)
      // return `周${c}`
      return String(d);
    },
    // 统计产量工时汇总和辅助工时汇总
    collectWorkingHours() {
      let entity = {};
      if (this.entity.week1 && this.entity.week2) {
        entity.week = JSON.stringify([this.entity.week1, this.entity.week2]);
      } else {
        entity.week = "";
      }
      if (this.entity.dateTime) {
        entity.dateTime = JSON.stringify(this.entity.dateTime);
      } else {
        entity.dateTime = "";
      }
      entity.weekDay = this.entity.weekDay;
      entity.name = this.entity.name;
      entity.state = this.entity.state;
      collectWorkingHours(entity).then((res) => {
        this.totalInfo = res.data;
      });
    },
    selectshiftByUser() {
      selectshiftByUser().then((res) => {
        this.formData.shift = res.data;
      });
    },
    getInfoByCode() {
      if (this.formData.number) {
        selectAuxiliaryWorkingHoursByNumber({
          number: this.formData.number,
        }).then((res) => {
          this.auxiliaryWorking = res.data;
          if (this.addVisible) {
            this.formData.approvedWorkingHour =
              this.auxiliaryWorking.approvedWorkingHour;
            this.formData.auxiliaryProject =
              this.auxiliaryWorking.auxiliaryProject;
          } else if (this.title == "审核" && this.checkVisible) {
            this.formData0.approvedWorkingHour =
              this.auxiliaryWorking.approvedWorkingHour;
            this.formData0.auxiliaryProject =
              this.auxiliaryWorking.auxiliaryProject;
          }
        });
      }
    },
    handleOut() {
      let entity = this.HaveJson(this.entity);
      if (this.entity.dateTime) {
        entity.dateTime = JSON.stringify(this.entity.dateTime);
      } else {
        entity.dateTime = "";
      }
      this.outLoading = true;
      exportAssistantHours({ entity: entity })
        .then((res) => {
          this.outLoading = false;
          const blob = new Blob([res]);
          this.$download.saveAs(blob, "辅助工时信息导出.xlsx");
        })
        .catch((err) => {
          this.outLoading = false;
        });
    },
    handleOut1() {
      let entity = this.HaveJson(this.entity);
      if (this.entity.dateTime) {
        entity.dateTime = JSON.stringify(this.entity.dateTime);
      } else {
        entity.dateTime = "";
      }
      this.outLoading = true;
      exportOutputHours({ entity: entity })
        .then((res) => {
          this.outLoading = false;
          const blob = new Blob([res]);
          this.$download.saveAs(blob, "产量工时信息导出.xlsx");
        })
        .catch((err) => {
          this.outLoading = false;
        });
    },
    handleSelectionChange(val) {
      // console.log(111, val);
      this.ValueTable0Selected = val;
    },
  },
};
</script>

<style scoped>
.form-item >>>.el-form-item__content {
  width: 120px;
}
.dateTime >>>.el-form-item__content {
  width: 260px;
}
</style>
