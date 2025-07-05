<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="标准物质名称" prop="name">
            <el-input size="small" placeholder="请输入" clearable v-model="searchForm.name"
              @keyup.enter.native="searchList"></el-input>
          </el-form-item>
          <el-form-item label="生产厂家" prop="factoryManufacturer">
            <el-input size="small" placeholder="请输入" clearable v-model="searchForm.factoryManufacturer"
              @keyup.enter.native="searchList"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" @click="exportFun">导 出</el-button>
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="tableColumn" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <form-dia v-if="formDia" ref="formDia" @closeYearDia="closeYearDia"></form-dia>
    <borrow-dia v-if="borrowDia" ref="borrowDia" @closeYearDia="closeBorrowDia"></borrow-dia>
    <return-dia v-if="returnDia" ref="returnDia" @closeYearDia="closeReturnDia"></return-dia>
  </div>

</template>

<script>

import limsTable from '@/components/Table/lims-table.vue'
import {
  getPageStandardSubstance,
  removeStandardSubstance,
  exportOfStandardSubstanceList
} from '@/api/cnas/resourceDemand/standardMaterial/standardMaterial'
import FormDia from '../standardMaterial/component/formDia.vue';
import BorrowDia from '../standardMaterial/component/borrowDia.vue';
import ReturnDia from '../standardMaterial/component/returnDia.vue';
export default {
  name: 'StandardMaterial',
  components: {
    limsTable,
    FormDia,
    BorrowDia,
    ReturnDia
  },
  data() {
    return {
      searchForm: {
        name: '',
        factoryManufacturer: '',
      },
      tableColumn: [
        {
          label: '标准物质名称',
          prop: 'name',
          minWidth: '120'
        },
        {
          label: '规格型号',
          prop: 'model',
          minWidth: '100'
        },
        {
          label: '生产厂家',
          prop: 'factoryManufacturer',
          minWidth: '100'
        },
        {
          label: '出厂编号',
          prop: 'factoryNum',
          minWidth: '100'
        },
        {
          label: '管理编号',
          prop: 'manageNum',
          minWidth: '100'
        },
        {
          label: '不确定度',
          prop: 'uncertainty',
          minWidth: '100'
        },
        {
          label: '数量',
          prop: 'quantity',
          minWidth: '100'
        },
        {
          label: '购置日期',
          prop: 'acquisitionDate',
          minWidth: '150'
        },
        {
          label: '有效期',
          prop: 'effectiveDate',
          minWidth: '150'
        },
        {
          label: '档案编号',
          prop: 'fileNum',
          minWidth: '100'
        },
        {
          label: '存放位置',
          prop: 'position',
          minWidth: '100'
        },
        {
          label: '备注',
          prop: 'remark',
          minWidth: '100'
        },
        {
          label: '创建时间',
          prop: 'createTime',
          minWidth: '150'
        },
        {
          dataType: 'action',
          fixed: 'right',
          minWidth: '180',
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
              name: '借用',
              type: 'text',
              clickFun: (row) => {
                this.borrow(row);
              },
            },
            {
              name: '归还',
              type: 'text',
              clickFun: (row) => {
                this.return(row);
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
        total: 0,
        size: 10,
        current: 1
      },
      total: 0,
      formDia: false,
      borrowDia: false,
      returnDia: false,
    }
  },
  mounted() {
    this.searchList()
  },
  methods: {
    exportFun() {
      this.outLoading = true
      exportOfStandardSubstanceList(this.searchForm).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' }, false);
        this.$download.saveAs(blob, '标准物质清单.xlsx')
      })
    },
    // 查询列表
    searchList() {
      this.tableLoading = true
      getPageStandardSubstance({
        ...this.page,
        ...this.searchForm
      }).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
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
        removeStandardSubstance({ id: row.id }).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.searchList()
          }
        }).catch(err => {
          this.tableLoading = false
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
    closeYearDia() {
      this.formDia = false
      this.searchList()
    },
    // 借用
    borrow(row) {
      this.borrowDia = true
      this.$nextTick(() => {
        this.$refs.borrowDia.openDia(row)
      })
    },
    closeBorrowDia() {
      this.borrowDia = false
      this.searchList()
    },
    // 归还
    return(row) {
      this.returnDia = true
      this.$nextTick(() => {
        this.$refs.returnDia.openDia(row)
      })
    },
    closeReturnDia() {
      this.returnDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.name = '';
      this.searchForm.factoryManufacturer = '';
      this.searchList()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.searchList();
    },
    // 分页
  }
}
</script>
<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
