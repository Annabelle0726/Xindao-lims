<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="标准物质名称" prop="name">
            <el-input size="small" placeholder="请输入" clearable v-model="searchForm.name"
              @keyup.enter.native="searchList"></el-input>
          </el-form-item>
          <el-form-item label="规格型号" prop="model">
            <el-input size="small" placeholder="请输入" clearable v-model="searchForm.model"
              @keyup.enter.native="searchList"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" type="primary" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="tableColumn" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import {
  getPageSubstanceRecord
} from '@/api/cnas/resourceDemand/standardMaterailRequistion/standardMaterailRequistion'

export default {
  name: 'StandardMaterailRequistion',
  components: {
    limsTable
  },
  data() {
    return {
      searchForm: {
        name: '',
        model: '',
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
          label: '出厂编号',
          prop: 'factoryNum',
          minWidth: '100'
        },
        {
          label: '数量',
          prop: 'factoryNum',
          minWidth: '100'
        },
        {
          label: '领用人',
          prop: 'borrowUser',
          minWidth: '100'
        },
        {
          label: '领用日期',
          prop: 'borrowDate',
          minWidth: '100'
        },
        {
          label: '归还人',
          prop: 'returnIntegrity',
          minWidth: '100'
        },
        {
          label: '归还日期',
          prop: 'returnDate',
          minWidth: '100'
        },
        {
          label: '归还时状态',
          prop: 'returnIntegrity',
          minWidth: '100'
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
    // 查询列表
    searchList() {
      this.tableLoading = true
      getPageSubstanceRecord({
        ...this.page,
        ...this.searchForm
      }).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.name = '';
      this.searchForm.model = '';
      this.searchList()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.searchList();
    },
  }
}
</script>
<style scoped>
.capacity-scope {
  padding: 20px !important;
}

.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
