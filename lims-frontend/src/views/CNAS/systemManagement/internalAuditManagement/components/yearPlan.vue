<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="内审目的" prop="purpose">
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
    <year-plan-dia v-if="yearPlanDia" ref="yearPlanDia" @closeYearDia="closeYearDia"></year-plan-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import YearPlanDia from './yearPlanDia.vue';
import {
  pageInternalPlan,
  delInternalPlan,
  exportInternalPlan,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'

export default {
  name: 'yearPlan',
  // import 引入的组件需要注入到对象中才能使用
  components: { YearPlanDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        purpose: '',
      },
      tableColumn: [
        {
          label: '内审目的',
          prop: 'purpose',
          minWidth: '100'
        },
        {
          label: '内审范围',
          prop: 'scope',
          minWidth: '100'
        },
        {
          label: '内审依据',
          prop: 'basis',
          minWidth: '100'
        },
        {
          label: '组长',
          prop: 'leader',
          minWidth: '100'
        },
        {
          label: '组员',
          prop: 'crew',
          minWidth: '100'
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
        }, {
          label: '审核内容',
          prop: 'examineRemark',
          minWidth: '100'
        }, {
          label: '审核人',
          prop: 'examineUserName',
          minWidth: '100'
        }, {
          label: '审核日期',
          prop: 'examineTime',
          minWidth: '160'
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
        }, {
          label: '批准人',
          prop: 'ratifyUserName',
          minWidth: '100'
        }, {
          label: '批准日期',
          prop: 'ratifyTime',
          minWidth: '160'
        },
        {
          dataType: 'action',
          fixed: 'right',
          minWidth: '220',
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
              name: '批准',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('ratify', row);
              },
              disabled: (row) => {
                if (row.ratifyStatus === 1 || row.examineStatus === 0 || row.examineStatus === null) {
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
      yearPlanDia: false,
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
      pageInternalPlan({ ...entity, ...page }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 删除
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delInternalPlan({ planId: row.planId }).then(res => {
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
    // 新增，编辑，批准弹框
    openFormDia(type, row) {
      this.yearPlanDia = true
      this.$nextTick(() => {
        this.$refs.yearPlanDia.openDia(type, row)
      })
    },
    // 导出
    handleDown(row) {
      exportInternalPlan({ planId: row.planId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '内审年度计划' + '.docx');
      })
    },
    closeYearDia() {
      this.yearPlanDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.purpose = '';
      this.searchList()
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
