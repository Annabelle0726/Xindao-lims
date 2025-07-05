<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">备注</span>
          <el-input size="small" placeholder="请输入" clearable v-model="searchForm.remark"
                    @keyup.enter.native="searchList"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="searchList">查询</el-button>
          <el-button size="mini" @click="resetSearchForm">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="tableColumn" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <three-wastes-dialog v-if="threeWastesDia" ref="threeWastesDia"
      @closeThreeWastesDia="closeThreeWastesDia"></three-wastes-dialog>
  </div>
</template>

<script>
import ThreeWastesDialog from './three-wastes-dialog.vue';
import limsTable from '@/components/Table/lims-table.vue'
import {
  pageInternalWastes,
  removeStandardSubstance,
  exportInternalWastes
} from '@/api/cnas/resourceDemand/facilitiesEnvironment/internalWastes'
export default {
  name: 'a6-three-wastes-treatment',
  // import 引入的组件需要注入到对象中才能使用
  components: {
    ThreeWastesDialog,
    limsTable
  },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        remark: '',
      },
      tableColumn: [
        {
          label: '创建时间',
          prop: 'createTime',
          minWidth: '100'
        },
        {
          label: '创建人',
          prop: 'createUserName',
          minWidth: '100'
        },
        {
          label: '备注',
          prop: 'remark',
          minWidth: '100'
        },
        {
          label: '修改时间',
          prop: 'updateTime',
          minWidth: '100'
        },
        {
          label: '修改人',
          prop: 'updateUserName',
          minWidth: '100'
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
        total: 0,
        size: 10,
        current: 1
      },
      total: 0,
      threeWastesDia: false
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      this.tableLoading = true
      pageInternalWastes({ ...this.page, ...this.searchForm }).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
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
        removeStandardSubstance({ wastesId: row.wastesId }).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.searchList()
          }
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
      this.threeWastesDia = true
      this.$nextTick(() => {
        this.$refs.threeWastesDia.openDia(type, row)
      })
    },
    // 导出
    handleDown(row) {
      exportInternalWastes({ wastesId: row.wastesId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '三废处理导出.docx')
      })
    },
    closeThreeWastesDia() {
      this.threeWastesDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.remark = '';
      this.searchList()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
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
