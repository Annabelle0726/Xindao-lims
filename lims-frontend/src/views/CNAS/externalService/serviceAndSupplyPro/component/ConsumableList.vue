<template>
  <div>
    <div class="flex">
      <el-button icon="el-icon-plus" size="small" type="primary" @click="showDialog(undefined)">
        新建
      </el-button>
      <el-button icon="el-icon-upload2" size="small" @click="exportExcel">
        导出Excel
      </el-button>
    </div>
    <limsTable :column="columns" :currentChange="rowClick" :height="'25vh'" :highlightCurrentRow="true"
               :isSelection="false" :rowStyle="tableRowStyle" :table-data="tableData" rowKey="id"
               @pagination="pagination" :page="page" style="margin-top: 18px;">
      <template v-slot:consumablesTypeSlot="{ row }">
        {{ findType(row.consumablesType) }}
      </template>
      <template v-slot:operation="scope">
        <el-button size="small" type="text" @click="showDialog(scope.row)">编辑</el-button>
        <el-button size="small" style="color: #f56c6c" type="text" @click="handleDelete(scope.row)">删除</el-button>
      </template>
    </limsTable>
    <el-divider></el-divider>
    <div>
      <ConsumableProject ref="consumableProject"></ConsumableProject>
    </div>
    <Edit ref="editRef" :contentsId="contentsId" @submit="fetchData" />
  </div>
</template>

<script>
import Edit from "./Edit.vue"
import limsTable from '@/components/Table/lims-table.vue'
import {
  deleteProcurementSuppliesList,
  procurementSuppliesList,
  exportProcurementSuppliesList
} from "@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro";
import ConsumableProject from "./ConsumableProject.vue"

export default {
  dicts: ["consumables_type"],
  components: {limsTable, Edit, ConsumableProject},
  props: {
    contentsId: {
      type: Number,
      required: true,
    }
  },
  watch: {
    contentsId(newVal, oldVal) {
      if (newVal !== 0) {
        this.fetchData()
      }
    }
  },
  data() {
    return {
      columns: [
        {
          label: "货号",
          prop: "itemNumber"
        },
        {
          label: "类别",
          prop: "consumablesType",
          dataType: "slot",
          slot: "consumablesTypeSlot"
        },
        {
          label: "名称",
          prop: "consumablesName"
        },
        {
          label: "规格",
          prop: "specifications"
        },
        {
          label: "参考供应商",
          prop: "supplierName"
        },
        {
          label: "库存下限",
          prop: "lowerLimit"
        },
        {
          label: "当前库存",
          prop: "currentAmount"
        },
        {
          label: "计量单位",
          prop: "unit"
        },
        {
          label: "备注",
          prop: "remark"
        },
        {
          label: "负责人",
          prop: "personInChargeName"
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
          fixed: 'right',
          label: "操作",
          width: 120,
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
      options: [],
    }
  },
  mounted() {
    this.fetchData()
    this.options = this.dict.type.consumables_type;
  },
  methods: {
    async fetchData() {
      procurementSuppliesList({ contentId: this.contentsId, ...this.page }).then(res => {
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
          if (this.tableData.length > 0) {
            this.rowClick(this.tableData[0])
          }
        }
      })
    },
    findType(val) {
      this.$nextTick()
      let res
      const e = this.options.find(item => item.value == val)
      if (e) {
        res = e.label
      } else {
        res = '-'
      }
      return res
    },
    showDialog(row) {
      this.$refs.editRef.openDialog(row);
    },
    async exportExcel() {
      exportProcurementSuppliesList({ parentId: this.contentsId }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '耗材列表.xlsx')
      })
    },
    pagination(page) {
      this.page.size = page.limit
      this.fetchData()
    },
    handleDelete(row) {
      deleteProcurementSuppliesList({ id: row.id }).then(res => {
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        }
      })
    },
    rowClick(row) {
      this.$refs.consumableProject.fetchListId(row)
    },
    tableRowStyle({ row }) {
      if(row.currentAmount <= row.lowerLimit) {
        return { background: '#ffcaca' }
      } else {
        return { background: '#fff' }
      }
    }
  }
}
</script>


<style scoped>
.flex {
  text-align: right;
}
</style>
