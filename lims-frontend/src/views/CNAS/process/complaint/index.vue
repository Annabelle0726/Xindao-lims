<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="样品编号" prop="sampleCode">
            <el-input v-model="queryParams.sampleCode" clearable placeholder="请输入" size="small"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="投诉名称" prop="complainName">
            <el-input v-model="queryParams.complainName" clearable placeholder="请输入" size="small"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button :loading="outLoading" size="small" type="primary" @click="handleDown">导出</el-button>
        <el-button size="small" type="primary" @click="openAdd">新增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 290px)'"
        :page="page" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :visible.sync="addDialogVisible" title="新增" width="400px">
      <el-row>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">投诉方名称：</div>
            <div class="search_input"><el-input v-model="addInfo.complainName" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">检验报告编号：</div>
            <div class="search_input">
              <el-input v-model="addInfo.code" clearable placeholder="请输入" size="small"></el-input>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">样品编号：</div>
            <div class="search_input"><el-input v-model="addInfo.sampleCode" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="search_thing">
            <div class="search_label">投诉方式：</div>
            <div class="search_input"><el-input v-model="addInfo.complainMethod" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="handleAdd">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :class="{ downPdf: title == '导出' }" :modal="title != '导出'" :title="title"
      :visible.sync="handleDialogVisible" width="800px">
      <div class="dialog-body">
        <div id="dialogBody">
          <h4 style="display: flex;align-items: center;flex-direction: column;justify-content: center;">
            <span style="font-size: 20px;">客户投诉受理单</span>
            <span>Customer complaint receipts</span>
          </h4>
          <p style="display: flex;justify-content: space-between;margin-top: 16px;">
            <span>{{ currentInfo0.complainNo }}</span>
            <span>NO:</span>
          </p>
          <table border="1" cellpadding="10" class="tables">
            <tr>
              <td colspan="3">
                <p>投诉方名称</p>
                <p class="en">Name of the complaining party</p>
              </td>
              <td colspan="3">{{ currentInfo0.complainName }}</td>
            </tr>
            <tr>
              <td>
                <p>检测报告编号</p>
                <p class="en">Test report number</p>
              </td>
              <td colspan="3">{{ currentInfo0.code }}</td>
              <td>
                <p>样品编号</p>
                <p class="en">Sample number</p>
              </td>
              <td>{{ currentInfo0.sampleCode }}</td>
            </tr>
            <tr>
              <td>
                <p>投诉人</p>
                <p class="en">Complainant</p>
              </td>
              <td>{{ currentInfo0.createUser }}</td>
              <td>
                <p>电话</p>
                <p class="en">Phone</p>
              </td>
              <td>{{ currentInfo0.phone }}</td>
              <td>
                <p>E-Mail</p>
                <p class="en">E-mail</p>
              </td>
              <td>{{ currentInfo0.email }}</td>
            </tr>
            <tr>
              <td>
                <p>投诉方式</p>
                <p class="en">Complaint method</p>
              </td>
              <td colspan="3">{{ currentInfo0.complainMethod }}</td>
              <td>
                <p>投诉日期</p>
                <p class="en">Date of complaint</p>
              </td>
              <td>{{ currentInfo0.createTime ? currentInfo0.createTime.split(' ')[0] : '' }}</td>
            </tr>
            <tr>
              <td>
                <p>问题记录</p>
                <p class="en">Problem logging</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <el-input v-if="title == '处理投诉'" v-model="currentInfo0.problemRecords" :rows="3" placeholder="请输入内容"
                    type="textarea">
                  </el-input>
                  <p v-else style="text-align: left;line-height: 26px;">{{ currentInfo0.problemRecords }}</p>
                </div>
                <div v-if="title != '处理投诉'" class="user-info">
                  <div style="width: 200px;margin-right: 10px;">
                    <p style="text-align: end;">质量负责人:</p>
                    <p class="en" style="text-align: end;">Quality Manager:</p>
                  </div>
                  <span>{{ currentInfo0.problemRecordsUserName }}</span>
                  <div style="width: 70px;">
                    <p>日期:</p>
                    <p class="en">Date:</p>
                  </div>
                  <span style="margin-right: 16px;">{{ currentInfo0.problemRecordsTime }}</span>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <p>责任归属及投诉是否成立</p>
                <p class="en">Attribution of responsibility and whether the complaint is established</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <el-input v-if="title == '处理投诉'" v-model="currentInfo0.dutyOwnership" :rows="3" placeholder="请输入内容"
                    type="textarea">
                  </el-input>
                  <p v-else style="text-align: left;line-height: 26px;">{{ currentInfo0.dutyOwnership }}</p>
                </div>
                <div v-if="title != '处理投诉'" class="user-info">
                  <div style="width: 200px;margin-right: 10px;">
                    <p style="text-align: end;">质量负责人:</p>
                    <p class="en" style="text-align: end;">Quality Manager:</p>
                  </div>
                  <span>{{ currentInfo0.dutyOwnershipUserName }}</span>
                  <div style="width: 70px;">
                    <p>日期:</p>
                    <p class="en">Date:</p>
                  </div>
                  <span style="margin-right: 16px;">{{ currentInfo0.dutyOwnershipTime }}</span>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <p>原因分析</p>
                <p class="en">Cause analysis</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <el-input v-if="title == '处理投诉'" v-model="currentInfo0.causeAnalysis" :rows="3" placeholder="请输入内容"
                    type="textarea">
                  </el-input>
                  <p v-else style="text-align: left;line-height: 26px;">{{ currentInfo0.causeAnalysis }}</p>
                </div>
                <div v-if="title != '处理投诉'" class="user-info">
                  <div style="width: 200px;margin-right: 10px;">
                    <p style="text-align: end;">责任部门负责人:</p>
                    <p class="en" style="text-align: end;">Head of Responsible Department:</p>
                  </div>
                  <span>{{ currentInfo0.causeAnalysisUserName }}</span>
                  <div style="width: 70px;">
                    <p>日期:</p>
                    <p class="en">Date:</p>
                  </div>
                  <span style="margin-right: 16px;">{{ currentInfo0.causeAnalysisTime }}</span>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <p>纠正措施</p>
                <p class="en">Corrective actions</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <el-input v-if="title == '处理投诉'" v-model="currentInfo0.correctiveAction" :rows="3" placeholder="请输入内容"
                    type="textarea">
                  </el-input>
                  <p v-else style="text-align: left;line-height: 26px;">{{ currentInfo0.correctiveAction }}</p>
                </div>
                <div v-if="title != '处理投诉'" class="user-info">
                  <div style="width: 200px;margin-right: 10px;">
                    <p style="text-align: end;">责任部门负责人:</p>
                    <p class="en" style="text-align: end;">Head of Responsible Department:</p>
                  </div>
                  <span>{{ currentInfo0.correctiveActionUserName }}</span>
                  <div style="width: 70px;">
                    <p>日期:</p>
                    <p class="en">Date:</p>
                  </div>
                  <span style="margin-right: 16px;">{{ currentInfo0.correctiveActionTime }}</span>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <p>纠正措施确认</p>
                <p class="en">Corrective actions confirmation</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <el-input v-if="title == '处理投诉'" v-model="currentInfo0.correctiveActionConfirmation" :rows="3"
                    placeholder="请输入内容" type="textarea">
                  </el-input>
                  <p v-else style="text-align: left;line-height: 26px;">{{ currentInfo0.correctiveActionConfirmation }}
                  </p>
                </div>
                <div v-if="title != '处理投诉'" class="user-info">
                  <div style="width: 200px;margin-right: 10px;">
                    <p style="text-align: end;">质量负责人:</p>
                    <p class="en" style="text-align: end;">Quality Manager:</p>
                  </div>
                  <span>{{ currentInfo0.correctiveActionConfirmationUserName }}</span>
                  <div style="width: 70px;">
                    <p>日期:</p>
                    <p class="en">Date:</p>
                  </div>
                  <span style="margin-right: 16px;">{{ currentInfo0.correctiveActionConfirmationTime }}</span>
                </div>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <span v-if="title == '处理投诉'" slot="footer" class="dialog-footer">
        <el-button @click="handleDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="submit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import { getYearAndMonthAndDays } from '@/utils/date'
import { exportHtmlToPDF } from '@/utils/downHtmlToPDF'
import {
  addProcessComplain,
  getProcessComplain,
  doProcessComplain,
  exportProcessComplain,
  pageProcessComplain,
  delProcessComplain
} from '@/api/cnas/process/complaint.js'
import { mapGetters } from "vuex";
export default {
  name: 'Complaint',
  components: {
    limsTable
  },
  data() {
    return {
      tableLoading: false,
      addPower: false,
      outLoading: false,
      addDialogVisible: false,
      addLoading: false,
      handleDialogVisible: false,
      title: '处理投诉',
      addInfo: {},//新增信息
      currentInfo: null,//接口请求回来的信息
      currentInfo0: {},//用户编辑过后的信息
      outPower: false,
      queryParams: {},
      tableData: [],
      column: [
        { label: "投诉名称", prop: "complainName" },
        { label: "投诉人", prop: "complainant" },
        { label: "投诉日期", prop: "createTime" },
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
              name: "处理",
              type: "text",
              clickFun: (row) => {
                this.handleWork(row);
              },
            },
            {
              name: "导出",
              type: "text",
              clickFun: (row) => {
                this.handleOut(row);
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
    };
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  mounted() {
    this.getList()
  },
  methods: {
    openAdd() {
      this.addInfo = {}
      this.addDialogVisible = true
    },
    handleAdd() {
      this.addLoading = true
      addProcessComplain(this.addInfo).then((res) => {
        this.addLoading = false
        this.$message({
          type: 'success',
          message: '新增成功'
        })
        this.addDialogVisible = false
        this.refresh()
      })
    },
    // 处理投诉
    handleWork(row) {
      getProcessComplain({ id: row.id }).then((res) => {
        this.currentInfo = res.data
        this.currentInfo0 = this.HaveJson(res.data)
        this.title = '处理投诉'
        this.handleDialogVisible = true
      })
    },
    submit() {
      this.handleParam('problemRecords')
      this.handleParam('dutyOwnership')
      this.handleParam('causeAnalysis')
      this.handleParam('correctiveAction')
      this.handleParam('correctiveActionConfirmation')
      this.addLoading = true
      for (let i in this.currentInfo0) {
        if (!this.currentInfo0[i]) {
          delete this.currentInfo0[i]
        }
      }
      doProcessComplain(this.currentInfo0).then((res) => {
        this.addLoading = false
        this.$message({
          type: 'success',
          message: '提交成功'
        })
        this.handleDialogVisible = false
        this.refresh()
      })
    },
    /**
     * 处理参数
     *
     * @param {string} type - 需要处理的参数类型
     */
    handleParam(type) {
      if (this.currentInfo0[type] != this.currentInfo[type]) {
        this.currentInfo0[type + 'User'] = this.userId
        this.currentInfo0[type + 'Time'] = getYearAndMonthAndDays()
      }
    },
    // 查看投诉
    handleLook(row) {
      addProcessComplain({ id: row.id }).then((res) => {
        this.currentInfo = res.data
        this.currentInfo0 = this.HaveJson(res.data)
        this.title = '查看投诉'
        this.handleDialogVisible = true
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageProcessComplain({ ...param })
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
    handleDown() {
      this.outLoading = true
      exportProcessComplain(this.queryParams).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '投诉情况汇总表.xlsx');
      })
    },
    handleOut(row) {
      getProcessComplain({ id: row.id }).then((res) => {
        this.currentInfo = res.data
        this.currentInfo0 = this.HaveJson(res.data)
        this.title = '导出'
        this.handleDialogVisible = true
        setTimeout(() => {
          this.$nextTick(() => {
            const element = document.getElementById("dialogBody");
            exportHtmlToPDF(element, '投诉详情').then(res => {
              this.handleDialogVisible = false
            })
          })
        }, 500);
      })
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delProcessComplain({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refresh();
          });
        })
        .catch(() => { });
    },
  },
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

.dialog-body {
  max-height: 75vh;
  overflow-y: auto;
}

.tables {
  table-layout: fixed;
  width: 100%;
}

.tables td {
  height: 40px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}

.en {
  font-size: 12px;
  word-break: break-word;
  /* 自动断行 */
  overflow-wrap: break-word;
  /* 防止溢出 */
  white-space: normal;
  /* 默认换行 */
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.user-content {
  min-height: 60px;
}

.downPdf {
  opacity: 0 !important;
}

.btn {
  position: absolute;
  top: 16px;
  right: 20px;
}
</style>
