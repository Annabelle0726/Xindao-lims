<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="年份" prop="laboratoryName">
            <el-date-picker
              v-model="searchForm.distributionYear"
              value-format="yyyy"
              format="yyyy"
              clearable
              @change="searchList"
              type="year"
              placeholder="选择年">
            </el-date-picker>
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
      <limsTable :column="tableColumn" :height="'calc(100vh - 19em)'" :table-data="tableData"
                 :table-loading="tableLoading" :page="page"
                 @pagination="pagination">
      </limsTable>
    </div>
    <form-dia v-if="formDia" ref="formDia" @closeDia="closeDia"></form-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import FormDia from './components/formDia.vue';
import {
  pageInconsistentDistribution,
  delInconsistentDistribution,
  exportInconsistentDistribution,
} from '@/api/cnas/process/nonconformingWork.js'

export default {
  name: 'NonconformingDistribution',
  // import 引入的组件需要注入到对象中才能使用
  components: { FormDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        distributionYear: '',
      },
      tableColumn: [
        {
          label: '年份',
          prop: 'distributionYear',
          minWidth: '100'
        },
        {
          label: '创建人',
          prop: 'createUserName',
          minWidth: '100'
        },
        {
          label: '创建时间',
          prop: 'createTime',
          minWidth: '100'
        },
        {
          label: '修改人',
          prop: 'updateUserName',
          minWidth: '100'
        },
        {
          label: '修改时间',
          prop: 'updateTime',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '220',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
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
      formDia: false,
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
      pageInconsistentDistribution({ ...entity, ...page }).then(res => {
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
        delInconsistentDistribution({ distributionId: row.distributionId }).then(res => {
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
      this.formDia = true
      this.$nextTick(() => {
        this.$refs.formDia.openDia(type, row)
      })
    },
    // 导出
    handleDown(row) {
      exportInconsistentDistribution({ distributionId: row.distributionId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '不符合项的分布' + '.docx');
      })
    },
    closeDia() {
      this.formDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.distributionYear = '';
      this.searchList()
    },
    // 分页
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
