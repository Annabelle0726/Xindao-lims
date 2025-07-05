<template>
  <div class="capacity-scope">
    <el-tabs type="border-card" v-model="activeName" style="height: 100%;" @tab-click="queryParams.totaldealId = ''">
      <el-tab-pane label="填写" name="填写" style="height: 100%;" :key="1">
        <el-button size="small" type="primary" @click="handleAdd0" style="margin-bottom: 10px">新增</el-button>
        <div class="table" style="height: calc(100% - 200px)" v-if="activeName == '填写'">
          <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading"
                      key="tableData" :height="'calc(100vh - 290px)'" :page="page" @pagination="pagination"></lims-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="历史记录" name="历史记录" style="height: 100%;" :key="2">
        <el-form :model="queryParams0" ref="queryParams0" size="small" :inline="true">
          <el-form-item label="年月" prop="month">
            <el-date-picker v-model="queryParams0.month" type="month" placeholder="选择月" format="yyyy-MM"
                            value-format="yyyy-MM" size="small" @change="refreshTable()">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="table">
          <lims-table :tableData="tableData0" :column="column0" :tableLoading="tableLoading"
                      key="tableData0"
            :height="'calc(100vh - 300px)'" :page="page0" @pagination="pagination0"></lims-table>
        </div>
      </el-tab-pane>
    </el-tabs>
    <!-- 新增样品 -->
    <el-dialog :title="title" :visible.sync="addDialogVisible" width="400px">
      <el-row>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">样品名称：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.sampleName"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">样品编号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.sampleCode"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">供样单位：</div>
            <div class="search_input">
              <el-select v-model="addInfo.sampleSupplier" size="small">
                <el-option :label="item.company" :value="item.company" v-for="(item, index) in customPageList"
                  :key="item.id"></el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">数量：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.num"></el-input>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">处理方式：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.dealMethod"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">时间：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.dealTime" type="date" size="small" placeholder="选择日期" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd" style="width: 100%;">
              </el-date-picker>
            </div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 详情/下载/审核/批准 -->
    <el-dialog :title="title0" :visible.sync="lookDialogVisible" width="800px" :class="{ downPdf: title0 == '下载' }"
      :modal="title0 != '下载'" top="5vh">
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.url" :currentFile="{}"
        style="max-height: 70vh;overflow-y: auto;" />
      <span slot="footer" class="dialog-footer" v-if="title0 == '审核' || title0 == '批准'">
        <el-button @click="submitCheck('不通过')" :loading="noCheckLoading">不通过</el-button>
        <el-button type="primary" @click="submitCheck('通过')" :loading="checkLoading">通 过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from "@/components/Preview/filePreview.vue";
import { selectCustomPageList } from "@/api/system/customer";
import {
  doProcessDeal,
  addProcessDeal,
  submitProcessTotaldeal,
  pageProcessDeal,
  checkProcessTotaldeal,
  ratifyProcessTotaldeal,
  delProcessDeal,
  pageProcessTotaldeal,
} from "@/api/cnas/process/sampleDisposal";
export default {
  name: 'SampleDisposal',
  components: {
    limsTable,
    filePreview,
  },
  data() {
    return {
      activeName: '填写',
      title: '新增',
      addDialogVisible: false,
      addLoading: false,
      outLoading: false,
      editDialogVisible: false,
      lookDialogVisible: false,
      title0: '查看',
      noCheckLoading: false,
      checkLoading: false,
      // 历史列表
      addInfo: {},//新增样品
      customPageList: [],
      currentInfo: {
        arr: []
      },//查看的详情
      outPower: false,
      addPower: false,
      queryParams: {},
      tableData: [],
      column: [
        { label: "样品名称", prop: "sampleName" },
        { label: "样品编号", prop: "sampleCode" },
        { label: "供样单位", prop: "sampleSupplier" },
        { label: "数量", prop: "num" },
        { label: "处理方式", prop: "dealMethod" },
        { label: "时间", prop: "dealTime" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleAdd0(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
      queryParams0: {},
      tableData0: [],
      column0: [
        { label: "月份", prop: "month" },
        { label: "总数量", prop: "totalNum" },
        { label: "提交人", prop: "submitUserName" },
        { label: "审核人", prop: "examineUserName" },
        { label: "批准人", prop: "ratifyUserName" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "查看",
              type: "text",
              clickFun: (row) => {
                this.handleLook(row);
              },
            },
            {
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleDown0(row);
              },
              disabled: (row) => {
                return !row.url
              }
            },
            {
              name: "提交",
              type: "text",
              clickFun: (row) => {
                this.handleSubmit(row);
              },
              disabled: (row) => {
                return !!row.submitState && row.submitState != '待提交'
              }
            },
            {
              name: "审核",
              type: "text",
              clickFun: (row) => {
                this.handleCheck(row);
              },
              disabled: (row) => {
                return row.examineState == '通过' || row.submitState == '待提交'
              }
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleApproval(row);
              },
              disabled: (row) => {
                return row.ratifyState == '通过' || row.submitState == '待提交'
              }
            },
          ],
        },
      ],
      page0: {
        total: 0,
        size: 10,
        current: 0,
      },
    };
  },
  mounted() {
    this.getCustomPageList()
    this.getList()
    this.getList0()
  },
  methods: {
    // 获取送样单位列表
    getCustomPageList() {
      selectCustomPageList({
        current: -1,
        size: -1
      }).then(res => {
        this.customPageList = res.data.records
      }).catch(err => { });
    },
    handleDown() { },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageProcessDeal({ ...param })
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
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    refresh() {
      this.queryParams0 = {};
      this.page0.current = 1;
      this.getList0();
    },
    refreshTable() {
      this.page0.current = 1;
      this.getList0();
    },
    getList0() {
      this.tableLoading = true;
      let param = { ...this.queryParams0, ...this.page };
      delete param.total;
      pageProcessTotaldeal({ ...param })
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
    pagination0({ page, limit }) {
      this.page0.current = page;
      this.page0.size = limit;
      this.getList0();
    },
    // 填写
    // handleAdd(row){
    //   this.componentData0.entity.totaldealId = row.id
    //   this.editDialogVisible = true
    // },
    // 打开新增界面
    handleAdd0(row) {
      if (row) {
        this.addInfo = this.HaveJson(row)
        this.title = '编辑'
      } else {
        this.title = '新增'
        this.addInfo = {}
      }
      this.addDialogVisible = true
    },
    // 提交新增
    submitAdd() {
      if (this.addInfo.id) {
        // 编辑
        this.addLoading = true
        doProcessDeal({
          totaldealId: this.queryParams.totaldealId,
          ...this.addInfo
        }).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '编辑成功!'
          });
          this.page.current = 0;
          this.getList();
        }).catch(err => { });
      } else {
        // 新增
        this.addLoading = true
        addProcessDeal({
          totaldealId: this.queryParams.totaldealId,
          ...this.addInfo
        }).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '新增成功!'
          });
          this.page.current = 0;
          this.getList();
        }).catch(err => { });
      }
    },
    // 提交
    handleSubmit(row) {
      this.$confirm('是否提交 ' + row.month + ' 月份的数据', '提交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitProcessTotaldeal({
          id: row.id
        }).then(res => {
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          this.refreshTable()
        }).catch(err => { });
      })
    },
    // 查看
    handleLook(row) {
      this.activeName = '填写'
      this.queryParams.totaldealId = row.id
      this.$nextTick(() => {
        this.page.current = 1;
        this.getList();
      })
    },
    commonFun(row, callbanck) {
      this.currentInfo = row
      this.queryParams.totaldealId = row.id
      pageProcessDeal({
        current: -1,
        size: -1, ...this.queryParams
      }).then(res => {
        this.lookDialogVisible = true
        this.currentInfo.arr = res.data.records
        if (callbanck) {
          callbanck()
        }
      }).catch(err => { });
    },
    // 审核
    handleCheck(row) {
      this.title0 = '审核'
      this.commonFun(row)
    },
    // 批准
    handleApproval(row) {
      this.title0 = '批准'
      this.commonFun(row)
    },
    // 提交审核/批准
    submitCheck(state) {
      if (state == '通过') {
        this.checkLoading = true
      } else {
        this.noCheckLoading = true
      }
      if (this.title0 == '审核') {
        checkProcessTotaldeal({
          id: this.currentInfo.id,
          state: state
        }).then(res => {
          this.checkLoading = false
          this.noCheckLoading = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
          this.lookDialogVisible = false
        }).catch(err => { });
      } else if (this.title0 == '批准') {
        ratifyProcessTotaldeal({
          id: this.currentInfo.id,
          state: state
        }).then(res => {
          this.checkLoading = false
          this.noCheckLoading = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
          this.lookDialogVisible = false
        }).catch(err => { });
      }
    },
    // 导出详情
    handleDown0(row) {
      if (!row.url) {
        this.$message.warning('暂无文件')
        return
      }
      // 后端下载
      this.$download.saveAs(row.url, row.month + ' 样品处理申请表');
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delProcessDeal({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.page.current = 0;
            this.getList();
          });
        })
        .catch(() => { });
    },
  },
}
</script>

<style scoped>
.search_thing {
  width: 350px;
  display: flex;
  align-items: center;
}

.search_label {
  width: 110px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 110px);
}

.downPdf {
  opacity: 0 !important;
}

.tables td {
  height: 40px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}

.user-info .el-button {
  margin: 0;
}

>>>.el-tabs__content {
  height: 100%;
}
</style>
