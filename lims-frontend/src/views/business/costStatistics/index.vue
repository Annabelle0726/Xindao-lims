<template>
<!--  费用统计页面-->
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="entity" ref="entity" size="small" :inline="true">
          <el-form-item label="委托编号" prop="entrustCode">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.entrustCode"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="样品名称" prop="sample">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.sample"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="委托单位" prop="company">
            <el-select @focus="getCompanyOptions" @change="refreshTable()" clearable
                       size="small" v-model="entity.company" style="width: 100%">
              <el-option v-for="item in companyOptions" :key="item.value"
                         :label="item.label" :value="item.label">
              </el-option>
            </el-select>
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
    <viewInfoDia ref="viewInfoDia" v-if="viewInfoDia"></viewInfoDia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import viewInfoDia from "./components/viewInfoDia.vue";
import {selectRatesPage} from "@/api/business/insOrderRates";
import {selectCustomPageList} from "@/api/system/customer";

export default {
  components: {limsTable,viewInfoDia},
  data() {
    return {
      entity: {
        sample: null,
        company: null,
      },
      tableData: [],
      tableLoading: false,
      column: [
        { label: '下单时间', prop: 'createTime', width: 150 },
        { label: '委托编号', prop: 'entrustCode', width: 160 },
        { label: '样品名称', prop: 'sample', width: 160 },
        { label: '委托单位', prop: 'company' },
        {
          label: "下单类型",
          prop: "typeSource",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "成品下单";
            } else if(params == 1) {
              return "原材料下单";
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "warning";
            } else if(params == 1) {
              return "info";
            } else {
              return "null";
            }
          },
        },
        { label: '委托人', prop: 'prepareUser' },
        { label: '生产单位', prop: 'production' },
        { label: '总价', prop: 'totalPrice' },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "查看详情",
              type: "text",
              clickFun: (row) => {
                this.openDia(row)
              },
            },
          ],
        }
      ],
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      companyOptions: [],
      viewInfoDia: false
    }
  },
  mounted() {
    this.refreshTable()
    this.getCompanyOptions()
  },
  methods :{
    refreshTable() {
      this.tableLoading = true
      selectRatesPage({ ...this.page, ...this.entity }).then(res => {
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
    // 查询委托单位下拉框数据
    getCompanyOptions () {
      selectCustomPageList({
        current: -1,
        size: -1
      }).then(res => {
        const list = res.data.records
        this.companyOptions = []
        list.map((item) => {
          const obj = Object.assign({
            value: item.id,
            label: item.company,
          })
          this.companyOptions.push(obj)
        })
      }).catch(err => { });
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    // 查看详情
    openDia (row) {
      this.viewInfoDia = true
      this.$nextTick(() => {
        this.$refs.viewInfoDia.openDia(row)
      })
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
