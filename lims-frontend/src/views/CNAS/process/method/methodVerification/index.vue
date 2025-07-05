<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">标准方法</span>
          <el-input size="small" placeholder="请输入" clearable v-model="searchForm.methodName"
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
      <limsTable :column="tableColumn" :height="'calc(100vh - 23em)'" :table-data="tableData"
                 :table-loading="tableLoading" :page="page"
                 @pagination="pagination">
      </limsTable>
    </div>
    <formDIa v-if="formDIa" ref="formDIa" :operationType="operationType" @closeDia="closeDia"></formDIa>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import formDIa from './component/formDIa.vue';
import { delMethodVerify, exportMethodVerify, pagesMethodVerify } from '@/api/cnas/process/method/methodVerification'

export default {
  name: 'MethodVerification',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, formDIa },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        methodName: '',
        operationType: 1,
      },
      options: [
        { label: '上半年', value: '1' },
        { label: '下半年', value: '2' },
      ],
      tableColumn: [
        {
          label: '标准方法',
          prop: 'methodName',
          minWidth: '100'
        },
        {
          label: '验证原因',
          prop: 'verifyReason',
          minWidth: '100'
        },
        {
          label: '主要技术变化',
          prop: 'technologyChange',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '60',
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
                this.downLoadPost(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.deleteRow(row);
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
        total: 0
      },
      total: 0,
      formDIa: false,
      operationType: '',
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      const entity = {
        methodName: this.searchForm.methodName,
        operationType: this.searchForm.operationType,
      }
      const page = this.page
      this.tableLoading = true
      pagesMethodVerify({ ...page, ...entity }).then(res => {
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
    deleteRow(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delMethodVerify({ methodVerifyId: row.methodVerifyId }).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.searchList()
          }
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      })
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.methodName = '';
      this.searchList()
    },
    openFormDia(type, row) {
      this.formDIa = true
      this.operationType = type
      this.$nextTick(() => {
        this.$refs.formDIa.openDia(row)
      })
    },
    // 导出
    downLoadPost(row) {
      exportMethodVerify({ methodVerifyId: row.methodVerifyId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '标准（方法）确认记录.docx')
      })
    },
    // 关闭弹框
    closeDia() {
      this.formDIa = false
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
