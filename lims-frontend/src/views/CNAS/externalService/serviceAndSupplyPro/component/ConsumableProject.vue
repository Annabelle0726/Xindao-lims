<template>
  <div>
    <div class="flex">
      <el-button icon="el-icon-plus" size="small" type="primary" @click="showDialog">
        添加项目
      </el-button>
<!--      <el-button icon="el-icon-upload2" size="small" @click="exportExcel">-->
<!--        导出-->
<!--      </el-button>-->
    </div>
    <limsTable
      :column="columns"
      :height="'25vh'"
      :isSelection="true"
      :table-data="tableData"
      @pagination="pagination"
      :page="page"
      style="margin-top: 18px;"
    >
      <template v-slot:operation="scope">
        <el-button size="small" type="text" @click="deleteData(scope.row)">删除</el-button>
      </template>
    </limsTable>
    <AddProject ref="AddProjectRef" @submit="fetchData"/>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import Edit from "./Edit.vue"
import AddProject from './AddProject.vue';
import {
  deleteProcurementSuppliesExpends,
  procurementSuppliesExpendlist
} from "@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro";

export default {
  components: {limsTable, Edit, AddProject},
  data() {
    return {
      columns: [
        // {
        //   label: "编号"
        // },
        {
          label: "项目名称",
          prop: "listName"
        },
        {
          label: "消耗数量",
          prop: "amount"
        },
        {
          label: "录入人",
          prop: "enterUserName"
        },
        {
          label: "最近更新人",
          prop: "updateUserName"
        },
        {
          label: "最近更新日期",
          prop: "updateTime"
        },
        {
          label: "操作",
          dataType: "slot",
          slot: "operation"
        }
      ],
      tableData: [],
      page: {
        current: 1,
        size: 20,
        total: 0
      },
      listId: 0,
      row: undefined
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchListId(row) {
      if(row) {
        this.listId = row.id
        this.row = row
      }
      this.fetchData()
    },
    async fetchData() {
      if (this.listId === 0) return
      procurementSuppliesExpendlist({
        procurementSuppliesListId:this.listId,
        ...this.page
      }).then(res => {
        if (res.code === 200) {
          this.tableData = res.data
        }
      })
    },
    pagination (page) {
      this.page.size = page.limit
      this.fetchData()
    },
    showDialog() {
      this.$refs.AddProjectRef.openDialog(this.row);
    },
    deleteData(row) {
      deleteProcurementSuppliesExpends({ expendId:row.expendId}).then(res => {
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        }
      })
    },
    exportExcel() {
    }
  }
}
</script>

<style scoped>
.flex {
  text-align: right;
}

.pagination {
  padding-top: 15px;
  padding-right: 10px;
  display: flex;
  justify-content: space-between
}
</style>
