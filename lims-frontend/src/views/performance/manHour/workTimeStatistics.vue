<template>
  <div class="capacity-scope">
    <div>
      <div class="search">
        <div>
          <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
            <el-form-item label="日期" prop="month">
              <el-date-picker
                v-model="queryParams.month"
                :clearable="false"
                format="yyyy-MM"
                placeholder="选择月"
                size="small"
                style="width: 100%"
                type="month"
                value-format="yyyy-MM"
                @change="refreshTable()"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="员工" prop="name">
              <el-input
                v-model="queryParams.name"
                clearable
                placeholder="请输入"
                size="small"
                @keyup.enter.native="refreshTable()"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
              <el-button size="mini" @click="refresh">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <limsTable
        :column="tableColumn"
        :table-data="tableData"
        :table-loading="tableLoading"
        :page="page"
        :height="'calc(100vh - 290px)'"
        @pagination="pagination"
      >
      </limsTable>
    </div>
  </div>
</template>

<script>
import { getYearAndMonthAndDays, getYearAndMonthAndDaysZTZB } from "@/utils/date";
import limsTable from "@/components/Table/lims-table.vue";
import { selectAuxiliaryAllByMonth } from "@/api/performance/manHour";
export default {
  components: {
    limsTable
  },
  data() {
    return {
      queryParams: {
        month: getYearAndMonthAndDaysZTZB().slice(0, 7),
        name: "",
        departLims: "",
      },
      tableColumn: [
        {
          label: "姓名",
          minWidth: "120px",
          prop: "userName",
        },
        {
          label: "月份",
          minWidth: "120px",
          prop: "month",
        },
        {
          label: "产量工时",
          minWidth: "120px",
          prop: "yieldHour",
        },
        {
          label: "辅助工时",
          minWidth: "120px",
          prop: "subsidiaryHour",
        },
        {
          label: "总工时",
          minWidth: "120px",
          prop: "totalHour",
        },
      ],
      tableData: [],
      tableLoading: false,
      page: {
        total: 0,
        size: 20,
        current: 0,
      },
    };
  },
  mounted() {
    this.refreshTable();
  },
  methods: {
    refreshTable() {
      this.tableLoading = true;
      selectAuxiliaryAllByMonth(this.queryParams)
        .then((res) => {
          this.tableLoading = false;
          this.tableData = res.data;
        })
        .catch(() => {
          this.tableLoading = false;
        });
    },
    refresh() {
      this.queryParams = {
        month: getYearAndMonthAndDaysZTZB().slice(0, 7),
        name: "",
        departLims: "",
      };
      this.refreshTable();
    },
    // 分页
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.refreshTable();
    },
  },
};
</script>

<style scoped>

</style>
