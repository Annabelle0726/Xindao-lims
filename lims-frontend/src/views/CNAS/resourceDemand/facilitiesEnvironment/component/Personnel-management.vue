<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="日期" prop="registerDate">
            <el-date-picker v-model="searchForm.registerDate" clearable format="yyyy-MM-dd" placeholder="选择日期"
                            @change="searchList" size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" @click="handleDown">导 出</el-button>
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="tableColumn" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <personnel-management-dia v-if="threeWastesDia" ref="threeWastesDia"
      @closeThreeWastesDia="closeThreeWastesDia"></personnel-management-dia>
  </div>
</template>

<script>
import PersonnelManagementDia from '../component/Personnel-management-dia.vue';
import limsTable from '@/components/Table/lims-table.vue'

import {
  pageForeignRegister,
  delForeignRegister,
  exportForeignRegister
} from '@/api/cnas/resourceDemand/foreignRegister/foreignRegister'

export default {
  name: 'Personnel-management',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, PersonnelManagementDia },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        registerDate: '',
      },
      tableColumn: [
        {
          label: '日期',
          prop: 'registerDate',
          minWidth: '100'
        },
        {
          label: '进入区域',
          prop: 'area',
          minWidth: '100'
        },
        {
          label: '进入人员',
          prop: 'personnel',
          minWidth: '100'
        },
        {
          label: '进入原因',
          prop: 'reason',
          minWidth: '100'
        },
        {
          label: '陪同人员',
          prop: 'accompanyingName',
          minWidth: '100'
        },
        {
          label: '批准人签名',
          prop: 'approveName',
          minWidth: '100'
        },
        {
          label: '保密及其他情况',
          prop: 'confidentiality',
          minWidth: '100'
        },
        {
          label: '备注',
          prop: 'remark',
          minWidth: '100'
        },
        {
          dataType: 'action',
          fixed: 'right',
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
      pageForeignRegister({
        ...this.page,
        ...this.searchForm
      }).then(res => {
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
        delForeignRegister({ registerId: row.registerId }).then(res => {
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
    handleDown() {
      exportForeignRegister(this.searchForm).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '外来人员登记.docx')
      })
    },
    closeThreeWastesDia() {
      this.threeWastesDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.registerDate = '';
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
