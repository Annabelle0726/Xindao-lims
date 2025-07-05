<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="审核目的" prop="laboratoryName">
            <el-input v-model="searchForm.purposes" clearable size="small"></el-input>
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
        :table-loading="tableLoading" style="padding: 0 10px;margin-bottom: 16px" :page="page" @pagination="pagination">
      </limsTable>
    </div>
    <audit-report-dia v-if="auditReportDia" ref="auditReportDia"
      @closeImplementDia="closeImplementDia"></audit-report-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import AuditReportDia from './auditReportDia.vue';
import {
  delInternalReport,
  pageInternalReport,
  exportInternalReport,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'auditReport',
  // import 引入的组件需要注入到对象中才能使用
  components: { AuditReportDia, limsTable },
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
        },
        {
          label: '审核依据',
          prop: 'basis',
        },
        {
          label: '审核日期',
          prop: 'reviewDate',
        },
        {
          label: '审核方法',
          prop: 'method',
        },
        {
          label: '审核范围',
          prop: 'scope',
        },
        {
          label: '审核责任者',
          prop: 'responsible',
        },
        {
          label: '审核组长',
          prop: 'leader',
        },
        {
          label: '审核员',
          prop: 'auditor',
        },
        {
          label: '审核组分工情况',
          prop: 'division',
        },
        {
          dataType: 'tag',
          label: '审核状态',
          prop: 'examineStatus',
          minWidth: '100',
          formatData: (params) => {
            if (params === 0) {
              return '不通过';
            } else if (params === 1) {
              return '通过';
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
        },
        {
          label: '审核内容',
          prop: 'examineRemark',
          minWidth: '140'
        },
        {
          dataType: 'tag',
          label: '质量负责人状态',
          prop: 'qualityStatus',
          minWidth: '100',
          formatData: (params) => {
            if (params === 0) {
              return '不通过';
            } else if (params === 1) {
              return '通过';
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
        },
        {
          label: '质量负责人意见',
          prop: 'qualityRemark',
          minWidth: '140'
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
                if (row.examineStatus === 1 || row.examineStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '审核',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('examine', row);
              },
              disabled: (row) => {
                if (row.examineStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '意见',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('ratify', row);
              },
              disabled: (row) => {
                if (row.qualityStatus === 1 || row.examineStatus === 0 || row.examineStatus === null) {
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
                if (row.qualityStatus === 1) {
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
      auditReportDia: false
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
      pageInternalReport({ ...entity, ...page }).then(res => {
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
      this.auditReportDia = true
      this.$nextTick(() => {
        this.$refs.auditReportDia.openDia(type, row)
      })
    },
    closeImplementDia() {
      this.auditReportDia = false
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
        delInternalReport({ reportId: row.reportId }).then(res => {
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
      exportInternalReport({ reportId: row.reportId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '内审报告' + '.docx');
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
