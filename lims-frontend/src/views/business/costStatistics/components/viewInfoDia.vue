<template>
  <div>
    <el-dialog title="详情" :visible.sync="viewInfoDia" width="1000px">
      <limsTable :tableData="tableData" :column="column"
                 height="600" @pagination="pagination"
                 :page="page" :tableLoading="tableLoading"></limsTable>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {selectRatesDetail} from "@/api/business/insOrderRates";
export default {
  name: '',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
  data() {
    // 这里存放数据
    return {
      tableData: [],
      column: [
        { label: '委托编号', prop: 'entrustCode', width: 160 },
        {label: '检验项分类', prop: 'inspectionItemClass', width: 160},
        {label: '检验项', prop: 'inspectionItem', width: 160},
        {label: '检验子项', prop: 'inspectionItemSubclass', width: 160},
        { label: '收费标准(元/次)', prop: 'rates' },
        { label: '分组系数', prop: 'manHourGroup' },
      ],
      viewInfoDia: false,
      tableLoading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      info: {}
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDia(row) {
      this.viewInfoDia = true
      this.info = this.HaveJson(row)
      this.page.current = 1
      this.page.size = 10
      this.getList();
    },
    getList() {
      this.tableLoading = true;
      selectRatesDetail({insOrderId: this.info.id, ...this.page}).then(res => {
        this.tableLoading = false;
        this.tableData = res.data.records;
        this.page.total = res.data.total;
      }).catch(err => {
        this.tableLoading = false;
      })
    },
    pagination(page) {
      this.page.size = page.limit;
      this.getList();
    },
  }
};
</script>

<style scoped>
</style>
