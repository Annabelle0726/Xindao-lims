<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div class="search_box">
        <div class="search_item">
          <span class="search_label">编号</span>
          <el-input v-model="entity.quarterNo" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()">
          </el-input>
        </div>
        <div class="search_button">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div>
        <el-button v-if="tabIndex === 1" size="small" type="primary" @click="yearSample('add')">年度抽样</el-button>
      </div>
    </div>
    <div>
      <div class="table">
        <ul class="tab">
          <li v-for="(m, i) in tabList" :key="i" :class="{ active: i === tabIndex }" @click="handleTab(m, i)">{{ m.label }}</li>
        </ul>
        <!--季度-->
        <lims-table :tableData="tableData" :column="column" v-if="tabIndex === 0" @pagination="pagination"
          :height="'calc(100vh - 290px)'" :page="page" :tableLoading="tableLoading"></lims-table>
        <!--年度-->
        <lims-table :tableData="tableData1" :column="column1" v-if="tabIndex === 1" @pagination="pagination1"
          :height="'calc(100vh - 290px)'" key="tableData1" :page="page1" :tableLoading="tableLoading1"></lims-table>
      </div>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="yearSampleDia" title="年度抽样"
      width="70%" @close="closeYearSampleDia">
      <div style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 10px">
        <div style="width: 30%;display: flex;align-items: center;">
          <el-input v-if="operationType !== 'view'" v-model="currentYear" size="small" />
          <span v-if="operationType === 'view'"
            style="width: 160px;font-size: 18px;font-weight: 600">{{ currentYear }}</span>
        </div>
        <div v-if="operationType !== 'view'">
          <el-button size="small" type="primary" @click="addQuarter">添加</el-button>
          <el-button size="small" type="danger" @click="clearTable">清空</el-button>
        </div>
      </div>
      <div>
        <el-table :data="yearItems" border height="450" style="width: 100%"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
          <el-table-column label="类别" prop="yearType" width="240">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.yearType" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.yearType }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="1" prop="january" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.january" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.january }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="2" prop="february" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.february" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.february }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="3" prop="march" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.march" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.march }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="4" prop="april" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.april" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.april }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="5" prop="may" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.may" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.may }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="6" prop="june" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.june" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.june }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="7" prop="july" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.july" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.july }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="8" prop="august" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.august" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.august }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="9" prop="september" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.september" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.september }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="10" prop="october" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.october" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.october }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="11" prop="november" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.november" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.november }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="12" prop="december" width="160">
            <template slot-scope="{row}">
              <template v-if="operationType !== 'view'">
                <el-input v-model="row.december" size="small" type="textarea" :rows="4" />
              </template>
              <template v-else>
                <span size="small">{{ row.december }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType !== 'view'" fixed="right" label="操作" width="100">
            <template slot-scope="scope">
              <el-button size="small" style="color: #f56c6c" type="text"
                @click="deleteScope(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="display: flex;align-items: center;margin-top: 10px">
        <span style="width: 70px">注意事项：</span>
        <el-input v-model="yearSampleForm.tableRemark" :disabled="operationType === 'view'" size="small"
          style="width: 50%" type="textarea"></el-input>
      </div>
      <div style="display: flex;align-items: center;margin-top: 10px">
        <span style="width: 70px">备注：</span>
        <el-input v-model="yearSampleForm.remark" :disabled="operationType === 'view'" size="small" style="width: 50%"
          type="textarea"></el-input>
      </div>
      <div v-if="operationType !== 'add'">
        <el-form ref="form" :model="editYearFormRow" label-width="70px">
          <el-col :span="12">
            <el-form-item label="编制人：">
              <el-select v-model="editYearFormRow.writeUser" :disabled="operationType !== 'edit'" placeholder="请选择"
                size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会签人：">
              <el-select v-model="editYearFormRow.countersignUser" :disabled="operationType !== 'edit'" multiple
                placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核人：">
              <el-select v-model="editYearFormRow.examineUser" :disabled="operationType !== 'edit'" placeholder="请选择"
                size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批准人：">
              <el-select v-model="editYearFormRow.ratifyUser" :disabled="operationType !== 'edit'" placeholder="请选择"
                size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeYearSampleDia">取 消</el-button>
        <el-button v-if="operationType !== 'view'" type="primary" @click="handleSample">确 定</el-button>
      </span>
    </el-dialog>
    <add-quarter-item ref="addQuarterItem"></add-quarter-item>
  </div>
</template>

<script>
import AddQuarterItem from "./components/addQuarterItem.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {
  addSpotCheckYear,
  deleteQuarter, deleteSpotCheckYear,
  finalReportQuarter,
  finalReportSpotCheckYear,
  getQuarterPage, getSpotCheckYear, getSpotCheckYearPage, updateSpotCheckYear
} from "@/api/business/productSamplingInfo";
import { selectUserCondition } from "@/api/performance/class";

export default {
  name: "ProductSamplingInfo",
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, AddQuarterItem },
  data() {
    // 这里存放数据
    return {
      upIndex: 0,
      tabIndex: 0,
      tabList: [
        {
          label: '季度',
          value: 0
        },
        {
          label: '年度',
          value: 1
        },
      ],
      entity: {
        quarterNo: null,
      },
      tableData: [],
      tableLoading: false,
      column: [
        { label: '编号', prop: 'quarterNo' },
        { label: '备注', prop: 'remark' },
        { label: '创建人', prop: 'createUserName' },
        { label: '创建时间', prop: 'createTime' },
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.editForm(row);
              },
            },
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.viewQuarterInfo(row);
              },
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteQuarterInfo(row);
              },
            }
          ]
        }
      ],
      page: {
        total: 0,
        size: 20,
        current: 1
      },
      tableData1: [],
      tableLoading1: false,
      column1: [
        { label: '名称', prop: 'yearHead' },
        { label: '创建人', prop: 'createUserName' },
        { label: '创建时间', prop: 'createTime' },
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.editYearForm(row);
              },
            },
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.viewYearInfo(row);
              },
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.download(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteYearInfo(row);
              },
            },
          ]
        }
      ],
      page1: {
        total: 0,
        size: 20,
        current: 1
      },
      yearSampleDia: false, // 年度抽样
      yearSampleForm: {
        tableRemark: '',
        remark: ''
      },
      yearItems: [],
      currentYear: '',
      operationType: '',
      userList: [],
      yearRow: {},
      editYearFormRow: {
        yearId: '',
        writeUser: '', // 编制人
        countersignUser: null, // 会签人
        examineUser: '', // 审核人
        ratifyUser: '', // 批准人
      }
    }
  },
  mounted() {
    this.refreshTable()
  },
  // 方法集合
  methods: {
    // 查询回调
    refreshTable() {
      if (this.tabIndex === 0) {
        this.getQuarterPageList()
      } else if (this.tabIndex === 1) {
        this.getSpotCheckYearPageList()
      }
    },
    getQuarterPageList() {
      this.tableLoading = true
      getQuarterPage({
        ...this.page, ...this.entity
      }).then(res => {
        this.tableLoading = false
        this.page.total = res.data.total
        this.tableData = res.data.records
      }).catch(err => {
        this.tableLoading = false
      })
    },
    getSpotCheckYearPageList() {
      this.tableLoading1 = true
      getSpotCheckYearPage({
        ...this.page1
      }).then(res => {
        this.tableLoading1 = false
        this.page1.total = res.data.total
        this.tableData1 = res.data.records
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 重置
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    pagination1(page) {
      this.page1.size = page.limit
      this.refreshTable()
    },
    // 编辑季度抽样
    editForm(row) {
      this.$refs.addQuarterItem.openDia(row, 'edit')
    },
    // 查看季度抽样
    viewQuarterInfo(row) {
      this.$refs.addQuarterItem.openDia(row, 'view')
    },
    // 删除季度抽样
    deleteQuarterInfo(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteQuarter({ quarterId: row.quarterId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.refreshTable()
          }
        })
      }).catch(() => { })
    },
    // 导出
    handleDown(row) {
      let randomNum = Math.random();
      finalReportQuarter({ quarterId: row.quarterId, random: randomNum }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '季度抽样信息导出.docx')
      })
    },
    // 年度下载
    download(row) {
      finalReportSpotCheckYear({ yearId: row.yearId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, row.yearHead + '.docx')
      })
    },
    // 删除年度抽样
    deleteYearInfo(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteSpotCheckYear({ yearId: row.yearId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.refreshTable()
          }
        })
      }).catch(() => { })
    },
    // 查看年度详情
    viewYearInfo(row) {
      this.operationType = 'view'
      this.getUserList()
      this.yearSampleDia = true
      this.yearRow = row
      getSpotCheckYear({ yearId: row.yearId }).then(res => {
        if (res.code === 200) {
          this.currentYear = res.data.yearHead
          this.yearItems = res.data.yearItems
          this.yearSampleForm.tableRemark = res.data.tableRemark
          this.yearSampleForm.remark = res.data.remark
          this.editYearFormRow.yearId = res.data.yearId
          this.editYearFormRow.writeUser = res.data.writeUser
          this.editYearFormRow.examineUser = res.data.examineUser
          this.editYearFormRow.ratifyUser = res.data.ratifyUser
          if (res.data.countersignUser) {
            this.editYearFormRow.countersignUser = res.data.countersignUser.split(",").map(Number)
          }
        }
      })
    },
    // 编辑年度抽样
    editYearForm(row) {
      this.operationType = 'edit'
      this.getUserList()
      this.yearSampleDia = true
      getSpotCheckYear({ yearId: row.yearId }).then(res => {
        if (res.code === 200) {
          this.currentYear = res.data.yearHead
          this.yearItems = res.data.yearItems
          this.yearSampleForm.tableRemark = res.data.tableRemark
          this.yearSampleForm.remark = res.data.remark
          this.editYearFormRow.yearId = res.data.yearId
          this.editYearFormRow.writeUser = res.data.writeUser
          this.editYearFormRow.examineUser = res.data.examineUser
          this.editYearFormRow.ratifyUser = res.data.ratifyUser
          if (res.data.countersignUser) {
            this.editYearFormRow.countersignUser = res.data.countersignUser.split(",").map(Number)
          }
        }
      })
    },
    // 提交年度计划
    handleSample() {
      if (this.operationType === 'add') {
        this.yearSampleForm.yearItems = JSON.parse(JSON.stringify(this.yearItems))
        this.yearSampleForm.yearHead = this.currentYear
        addSpotCheckYear(this.yearSampleForm).then(res => {
          if (res.code == 200) {
            this.$message.success('提交成功')
            this.yearSampleForm = {}
            this.yearItems = []
            this.closeYearSampleDia()
            this.refreshTable()
          }
        })
      } else {
        const params = { ...this.editYearFormRow }
        if (params.countersignUser != null) {
          params.countersignUser = params.countersignUser.join(',')
        }
        params.yearItems = JSON.parse(JSON.stringify(this.yearItems))
        params.tableRemark = this.yearSampleForm.tableRemark
        params.remark = this.yearSampleForm.remark
        updateSpotCheckYear(params).then(res => {
          if (res.code == 200) {
            this.$message.success('修改成功')
            this.yearSampleForm = {}
            this.editYearFormRow = {
              yearId: '',
              writeUser: '', // 编制人
              countersignUser: [], // 会签人
              examineUser: '', // 审核人
              ratifyUser: '', // 批准人
            }
            this.yearItems = []
            this.closeYearSampleDia()
            this.refreshTable()
          }
        })
      }
    },
    yearSample(type) {
      this.operationType = type
      const currentDate = new Date();
      this.yearSampleDia = true
      this.currentYear = currentDate.getFullYear() + '年年度抽检计划'
    },
    // 添加年度计划
    addQuarter() {
      this.yearItems.push({})
    },
    // 清空抽样计划
    clearTable() {
      this.yearItems = []
    },
    // 手动删除
    deleteScope(index) {
      this.yearItems.splice(index, 1)
    },
    // 切换下单tab表格
    handleTab(m, i) {
      this.tabIndex = i;
      this.refreshTable()
    },
    closeYearSampleDia() {
      this.yearSampleForm = {
        tableRemark: '',
        remark: ''
      }
      this.yearItems = []
      this.yearSampleDia = false
    },
    getStyle() {
      return 'height: calc(100% - ' + '44' + 'px)'
    },
    getUserList() {
      selectUserCondition().then((res) => {
        this.userList = res.data;
      })
    },
  },
}
</script>

<style scoped>
.tab {
  list-style-type: none;
  display: flex;
  margin-bottom: 12px;
  margin-top: 0;
  padding-left: 0;
}

.tab li {
  line-height: 24px;
  padding: 6px 14px;
  font-size: 14px;
  color: #333333;
  border: 1px solid #EEEEEE;
  cursor: pointer;
}

.tab li:nth-child(1) {
  border-radius: 8px 0 0 8px;
}

.tab li:nth-child(2) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}
.search_box {
  display: flex;
}
.search_item {
  margin-bottom: 18px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  line-height: 32px;
}
.search_label {
  width: 52px;
  font-size: 14px;
  font-weight: 700;
  color: #606266;
}
.search_button {
  line-height: 30px;
}
</style>
