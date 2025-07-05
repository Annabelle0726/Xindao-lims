<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">物质名称</span>
          <el-input size="small" placeholder="请输入" clearable v-model="form.name"
                    @keyup.enter.native="getTableData"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" @click="getTableData" size="mini">查询</el-button>
          <el-button @click="reset" size="mini">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button icon="el-icon-plus" size="small" type="primary" @click="openDialog">添加验收</el-button>
        <el-button size="small" type="primary" @click="exportExcel">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="columns" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading">
        <template slot="action" slot-scope="{ row }">
          <el-button type="text" @click="edit(row)">编辑</el-button>
        </template>
      </lims-table>
    </div>
    <AddRecord ref="addRecordRef" v-if="addRecordRef" @submit="submit"></AddRecord>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import AddRecord from './component/AddRecord.vue';
import {
  getPageAcceptance, getAcceptanceDetails, exportFeStandardSubstanceAcceptance, updateAcceptance, addAcceptance
} from '@/api/cnas/resourceDemand/standardMaterialAccept/standardMaterialAccept'
export default {
  components: {
    limsTable,
    AddRecord
  },
  data() {
    return {
      form: {
        name: undefined
      },
      addRecordRef: false,
      columns: [
        {
          label: "出场编号",
          prop: "factoryNum"
        },
        {
          label: "有效期",
          prop: "effectiveDate"
        },
        {
          label: "生产厂家",
          prop: "factoryManufacturer"
        },
        {
          label: "文档编号",
          prop: "fileNum"
        },
        {
          label: "标准物质名称",
          prop: "name"
        },
        {
          label: "规格型号",
          prop: "model"
        },
        {
          label: "管理编号",
          prop: "manageNum"
        },
        {
          label: "存放位置",
          prop: "position"
        },
        // {
        //     label: "序列号",
        //     prop: "manageNum"
        // },
        {
          label: "提交日期",
          prop: "acquisitionDate"
        },
        {
          label: "数量",
          prop: "quantity"
        },
        {
          fixed: "right",
          label: "操作",
          align: "center",
          dataType: "slot",
          slot: "action",
        },
      ],
      tableData: [],
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      tableLoading: false,
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    getTableData() {
      this.tableLoading = true
      getPageAcceptance({...this.form, ...this.page}).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records;
        this.page.total = res.data.total;
      }).catch(error => {
        this.tableLoading = false
      })
    },
    openDialog() {
      this.addRecordRef = true;
      this.$nextTick(() => {
        this.$refs.addRecordRef.openDialog()
      })
    },
    submit() {
      this.addRecordRef = false;
      this.getTableData()
    },
    edit(row) {
      this.addRecordRef = true;
      this.$nextTick(() => {
        this.$refs.addRecordRef.openDialog(row.id)
      })
    },
    reset() {
      this.form.name = undefined
      this.getTableData()
    },
    async exportExcel() {
      exportFeStandardSubstanceAcceptance().then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '标准物质验收.xlsx');
      })
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.getTableData()
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
