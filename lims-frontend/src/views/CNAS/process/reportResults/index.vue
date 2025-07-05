<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 128px;font-size: 14px;font-weight: 700;color: #606266;">检验报告编号</span>
          <el-input size="small" placeholder="请输入" clearable v-model="queryParams.insReportCode"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading" v-if="outPower">导出</el-button>
        <el-button size="small" type="primary" @click="openAdd" v-if="addPower">新增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 270px)'"
        :page="page" @pagination="pagination" :isSelection="true"
        :handleSelectionChange="handleSelectionChange"></lims-table>
    </div>
    <el-dialog :title="title" :visible.sync="addDialogVisible" width="400px" top="6vh">
      <el-row>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">检验报告编号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.insReportCode"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">页数：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.pages"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发送份数：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.number"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发往何处：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.send"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发送方式：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.method"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发送日期：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.sendTime" type="date" size="small" placeholder="选择日期" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd" style="width: 100%;">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发送人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.sendUser" placeholder="请选择" size="small" style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">签收人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.signatory" placeholder="请选择" size="small" style="width: 100%;" multiple>
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">备注：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.remark"
                type="textarea" :rows="2"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  exportProcessReport,
  doProcessReport,
  addProcessReport,
  pageProcessReport,
  delProcessReport
} from '@/api/cnas/process/reportResults.js'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
export default {
  name: 'ReportResults',
  components: {
    limsTable
  },
  data() {
    return {
      outLoading: false,
      addPower: true,
      outPower: true,
      personList: [],
      title: '新增',
      addDialogVisible: false,
      addInfo: {
        signatory: []
      },
      addLoading: false,
      mutilSelect: [],
      queryParams: {
        insReportCode: ''
      },
      tableData: [],
      column: [
        { label: "检验报告编号", prop: "insReportCode" },
        { label: "页数", prop: "pages", width: "120px" },
        {
          label: "发送份数",
          prop: "number",
        },
        { label: "发往何处", prop: "send" },
        { label: "发送方式", prop: "method" },
        { label: "发送日期", prop: "sendTime" },
        { label: "发送人", prop: "sendUserName" },
        { label: "签收人", prop: "signatoryName" },
        { label: "备注", prop: "remark" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleWork(row);
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
        size: 20,
        current: 0,
      },
      tableLoading: false,
    }
  },
  mounted() {
    // this.entityCopy = this.HaveJson(this.componentData.entity);
    this.getList()
    this.getAuthorizedPerson()
  },
  methods: {
    handleDown() {
      if (this.mutilSelect.length == 0) {
        this.$message.warning('请选择要导出的数据')
        return
      }
      this.outLoading = true
      exportProcessReport({ ids: this.mutilSelect.map(m => m.id) }).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.data, "报告结果");
      })
    },
    openAdd() {
      this.title = '新增'
      this.addInfo = {
        signatory: []
      }
      this.addDialogVisible = true;
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageProcessReport({ ...param })
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
      this.queryParams = {};
      this.page.current = 1;
      this.getList();
    },
    refreshTable() {
      this.page.current = 1;
      this.getList();
    },
    handleSelectionChange(val) {
      this.mutilSelect = val
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
    handleAdd() {
      this.addLoading = true
      delete this.addInfo.createTime
      delete this.addInfo.createUser
      delete this.addInfo.updateTime
      delete this.addInfo.signatoryUrl
      delete this.addInfo.updateUserer
      delete this.addInfo.sendUserName
      let addInfo = this.HaveJson(this.addInfo)
      addInfo.signatory = addInfo.signatory.join(',')
      if (this.title == '新增') {
        addProcessReport(addInfo).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      } else {
        doProcessReport(addInfo).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      }
    },
    handleWork(row) {
      this.title = '修改'
      this.addInfo = row
      console.log(this.addInfo)
      this.addInfo.signatory = this.addInfo.signatory ? this.addInfo.signatory.split(',').map(m => Number(m)) : []
      this.addDialogVisible = true;
    },
    handleChangeTask(list) {
      this.mutilSelect = list
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delProcessReport({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refresh();
          });
        })
        .catch(() => { });
    },
  }
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}

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
</style>
