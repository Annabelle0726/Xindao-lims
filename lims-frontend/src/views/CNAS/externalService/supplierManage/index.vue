<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="名称" prop="supplierName">
            <el-input v-model="searchForm.supplierName" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="getTableData">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
<!--        <el-button size="medium" @click="exportExcel">导 出</el-button>-->
        <el-button size="small" type="primary" @click="showDialog('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="tableColumn" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <form-dia ref="formDia" v-if="formDia" @closeDia="closeDia"></form-dia>
  </div>
</template>

<script>
import FormDia from "../supplierManage/component/formDia.vue";
import limsTable from '@/components/Table/lims-table.vue'
import {
  selectQualifiedSupplierManagementPage,
  exportSupplierManagement,
  delSupplierManagement
} from '@/api/cnas/externalService/supplierManage/supplierManage'

export default {
  name: "SupplierManage",
  // import 引入的组件需要注入到对象中才能使用
  components: {
    limsTable,
    FormDia
  },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        supplierName: ''
      },
      tableLoading: false,
      tableColumn: [
        {
          label: "供应商编号",
          prop: "supplierRef"
        },
        {
          label: "供应商",
          prop: "supplierName"
        },
        {
          label: "供应物品(服务)名称",
          prop: "supplierItemServiceName"
        },
        {
          label: "地址",
          prop: "adress"
        },
        {
          label: "联系电话",
          prop: "phone"
        },
        {
          dataType: 'action',
          fixed: "right",
          minWidth: '60',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.showDialog('edit', row);
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
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      formDia: false,
    }
  },
  mounted() {
    this.getTableData()
  },
  // 方法集合
  methods: {
    // 获取表格数据
    async getTableData() {
      this.tableLoading = true;
      selectQualifiedSupplierManagementPage({...this.searchForm, ...this.page}).then(res => {
        this.tableLoading = false;
        if (res.code === 200) {
          this.tableData = res.data.records;
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置
    resetSearchForm() {
      this.page.current = 1
      this.page.size = 10
      this.searchForm.supplierName = ''
      this.getTableData()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.getTableData();
    },
    // 打开新增弹框
    showDialog(type, row) {
      this.formDia = true
      this.$nextTick(() => {
        this.$refs.formDia.openDialog(type, row)
      })
    },
    // 关闭弹框
    closeDia() {
      this.formDia = false
      this.getTableData()
    },
    // 删除记录
    deleteRow(row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSupplierManagement({ supplierManagementId: row.supplierManagementId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功！')
            this.getTableData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 导出excel
    async exportExcel() {
      exportSupplierManagement({ deviceId: this.clickNodeVal.value }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '合格供应商.xlsx')
      })
    }
  },
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
