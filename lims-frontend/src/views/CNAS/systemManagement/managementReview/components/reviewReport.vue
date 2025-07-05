<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="地点" prop="place">
            <el-input v-model="searchForm.place" clearable size="small"></el-input>
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
        :table-loading="tableLoading" :page="page" @pagination="pagination">
      </limsTable>
    </div>
    <review-report-dia v-if="reviewReportDia" ref="reviewReportDia" @closeYearDia="closeYearDia"></review-report-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import ReviewReportDia from './reviewReportDia.vue';
import {
  getPageReviewReport,
  deleteReviewReport,
  exportReviewReport,
  modifyReviewReport,
} from '@/api/cnas/systemManagement/managementReview.js'
import { mapGetters } from "vuex";
export default {
  name: 'reviewReport',
  // import 引入的组件需要注入到对象中才能使用
  components: { ReviewReportDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        place: '',
      },
      tableColumn: [
        {
          label: '目的',
          prop: 'objective',
          minWidth: '100'
        },
        {
          label: '地点',
          prop: 'place',
          minWidth: '100'
        },
        {
          label: '主持人',
          prop: 'compere',
          minWidth: '100'
        },
        {
          label: '记录人',
          prop: 'recordPeople',
          minWidth: '100'
        },
        {
          label: '日期',
          prop: 'date',
          minWidth: '100'
        },
        {
          label: '页次',
          prop: 'page',
          minWidth: '100'
        },
        {
          label: '评审方式',
          prop: 'judgingMethod',
          minWidth: '100'
        },
        {
          label: '评审依据',
          prop: 'reviewBasis',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '160',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
              disabled: (row) => {
                return !!row.audit || !!row.approval
              }
            },
            {
              name: '审核',
              type: 'text',
              clickFun: (row) => {
                this.$confirm('确定审核通过?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.submit('audit', row)
                }).catch(() => {
                });
              },
              disabled: (row) => {
                return !!row.audit
              }
            },
            {
              name: '批准',
              type: 'text',
              clickFun: (row) => {
                this.$confirm('确定批准通过?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.submit('approval', row)
                }).catch(() => {
                });
              },
              disabled: (row) => {
                return !row.audit || !!row.approval
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              }
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            },
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
      reviewReportDia: false,
    };
  },
  computed: {
    ...mapGetters(['nickName'])
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      this.tableLoading = true
      getPageReviewReport({ place: this.searchForm.place, pages: this.page.current, size: this.page.size }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 新增，编辑弹框
    openFormDia(type, row) {
      this.reviewReportDia = true
      this.$nextTick(() => {
        this.$refs.reviewReportDia.openDia(type, row)
      })
    },
    closeYearDia() {
      this.reviewReportDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.place = '';
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
        deleteReviewReport({ id: row.id }).then(res => {
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
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
    handleDown(row) {
      exportReviewReport({ id: row.id }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '评审报告.docx');
      })
    },
    submit(type, row) {
      let obj = row
      obj[type] = this.nickName;
      modifyReviewReport(obj).then(res => {
        this.$message.success('操作成功')
        this.searchList()
      }).catch(err => {
        console.log('err---', err);
      })
    }
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
