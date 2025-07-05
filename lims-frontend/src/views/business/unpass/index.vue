<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="entity" ref="entity" size="small" :inline="true">
          <el-form-item label="规格型号" prop="model">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.model"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="样品名称" prop="sample">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.sample"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div>
      <lims-table :tableData="tableData" :column="column"
                  :height="'calc(100vh - 250px)'" @pagination="pagination"
                  :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {pageInsUnPass} from "@/api/business/unpass";

export default {
  name: 'Unpass',
  components: {limsTable},
  data() {
    return {
      entity: {
        sample: null,
        model: null,
      },
      tableData: [],
      tableLoading: false,
      column: [
        { label: '编号', prop: 'no' },
        // {
        //   label: "OA审核状态",
        //   prop: "oaState",
        //   width: "100px",
        //   dataType: "tag",
        //   formatData: (params) => {
        //     if (params == 0) {
        //       return "否";
        //     } else {
        //       return "是";
        //     }
        //   },
        //   formatType: (params) => {
        //     if (params == 0) {
        //       return "danger";
        //     } else {
        //       return "primary";
        //     }
        //   },
        // },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '180px',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openAdd('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.delete(row);
              },
            },
            {
              name: '印章管理',
              type: 'text',
              clickFun: (row) => {
                this.fileManagement(row);
              },
            },
          ]
        }
      ],
      page: {
        total: 0,
        size: 20,
        current: 1
      },
    }
  },
  mounted() {
    this.refreshTable()
  },
  methods :{
    refreshTable() {
      this.tableLoading = true
      pageInsUnPass({ ...this.page, ...this.entity }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
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
