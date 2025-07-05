<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="审核目的" prop="purposes">
            <el-input v-model="searchForm.purpose" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 23em)'" :table-data="tableData"
        :table-loading="tableLoading" @pagination="pagination" :page="page">
      </limsTable>
    </div>
    <implement-plan-dia v-if="implementPlanDia" ref="implementPlanDia"
      @closeImplementDia="closeImplementDia"></implement-plan-dia>
  </div>
</template>

<script>
import {
  pageInternalImplement,
  delInternalImplement,
  exportInternalImplement,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
import limsTable from "@/components/Table/lims-table.vue";
import ImplementPlanDia from './implementPlanDia.vue';

export default {
  name: 'implementationPlan',
  // import 引入的组件需要注入到对象中才能使用
  components: { ImplementPlanDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        purposes: '',
      },
      tableColumn: [
        {
          label: '审核目的',
          prop: 'purposes',
          minWidth: '100'
        },
        {
          label: '审核性质',
          prop: 'nature',
          minWidth: '100'
        },
        {
          label: '审核范围',
          prop: 'scope',
          minWidth: '100'
        },
        {
          label: '审核依据',
          prop: 'basis',
          minWidth: '100'
        },
        {
          label: '审核组长',
          prop: 'teamLeader',
          minWidth: '100'
        },
        {
          label: '内审员',
          prop: 'internalAuditor',
          minWidth: '100',
        },
        {
          label: '审核日期',
          prop: 'reviewDate',
          minWidth: '100',
        },
        {
          label: '审核方法',
          prop: 'auditMethod',
          minWidth: '100',
        },
        {
          label: '首次会议时间',
          prop: 'firstMeetingTime',
          minWidth: '100',
        },
        {
          label: '末次会议时间',
          prop: 'lastMeetingTime',
          minWidth: '100',
        },
        {
          label: '审核报告提交日期',
          prop: 'submitTime',
          minWidth: '100',
        },
        {
          label: '审核报告发放范围',
          prop: 'submitScope',
          minWidth: '100',
        }, {
          dataType: 'tag',
          label: '批准状态',
          prop: 'ratifyStatus',
          minWidth: '100',
          formatData: (params) => {
            if (params === 0) {
              return '不批准';
            } else if (params === 1) {
              return '批准';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return 'danger';
            } else if (params === 1) {
              return 'success';
            } else {
              return null;
            }
          }
        }, {
          label: '批准内容',
          prop: 'ratifyRemark',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '220',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            },
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
              disabled: (row) => {
                if (row.ratifyStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '批准',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('ratify', row);
              },
              disabled: (row) => {
                if (row.ratifyStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              },
              disabled: (row) => {
                if (row.ratifyStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            }
          ]
        }
      ],
      tableData: [],
      tableLoading: false,
      page: {
        size: 20,
        current: 1,
        total: 0,
      },
      implementPlanDia: false,
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      const entity = this.searchForm
      const page = this.page
      this.tableLoading = true
      pageInternalImplement({ ...entity, ...page }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 新增，编辑，批准弹框
    openFormDia(type, row) {
      this.implementPlanDia = true
      this.$nextTick(() => {
        this.$refs.implementPlanDia.openDia(type, row)
      })
    },
    closeImplementDia() {
      this.implementPlanDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.purposes = '';
      this.searchList()
    },
    // 删除
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delInternalImplement({ implementId: row.implementId }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 导出
    handleDown(row) {
      exportInternalImplement({ implementId: row.implementId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '内审实施计划' + '.docx');
      })
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
  }
};
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
