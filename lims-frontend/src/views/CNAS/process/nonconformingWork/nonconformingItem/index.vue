<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">发生部门</span>
          <el-input size="small" placeholder="请输入" clearable v-model="searchForm.occurrenceDepartment"
                    @keyup.enter.native="searchList"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="searchList">查询</el-button>
          <el-button size="mini" @click="resetSearchForm">重置</el-button>
        </div>
      </div>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 19em)'" :table-data="tableData"
                 :table-loading="tableLoading" :page="page"
                 @pagination="pagination">
      </limsTable>
    </div>
    <quality-info v-if="qualityInfo" ref="qualityInfo"></quality-info>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import QualityInfo from './components/qualityInfo.vue';
import {
  pageSuperviseDetailAccording,
  superviseDetailAccordingExport
} from '@/api/cnas/process/nonconformingWork.js'

export default {
  name: 'NonconformingItem',
  // import 引入的组件需要注入到对象中才能使用
  components: { QualityInfo, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        occurrenceDepartment: '',
      },
      tableColumn: [
        {
          label: '发生部门',
          prop: 'occurrenceDepartment',
          minWidth: '100'
        },
        {
          label: '部门负责人',
          prop: 'headDepartment',
          minWidth: '100'
        },
        {
          label: '发现途径',
          prop: 'findWay',
          minWidth: '100'
        },
        {
          label: '详细记录',
          prop: 'recordDetail',
          minWidth: '100'
        },
        {
          label: '依据和条款号',
          prop: 'recordAccording',
          minWidth: '100'
        },
        {
          label: '发现部门',
          prop: 'foundDepartment',
          minWidth: '100'
        },
        {
          label: '被监督人',
          prop: 'supervisedUserName',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '60',
          label: '操作',
          operation: [
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.viewInfo(row);
              },
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.openDownloadDia(row);
              },
            },
          ]
        }
      ],
      tableData: [],
      tableLoading: false,
      page: {
        size: 20,
        current: 1,
        total: 0,
      },
      qualityInfo: false,
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      const entity = {
        occurrenceDepartment: this.searchForm.occurrenceDepartment,
      }
      const page = this.page
      this.tableLoading = true
      pageSuperviseDetailAccording({ ...entity, ...page }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.occurrenceDepartment = '';
      this.searchList()
    },
    viewInfo(row) {
      this.qualityInfo = true
      this.$nextTick(() => {
        this.$refs.qualityInfo.openDia(row)
      })
    },
    // 导出
    openDownloadDia(row) {
      superviseDetailAccordingExport({ superviseDetailsId: row.superviseDetailsId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '不符合项导出' + '.docx');
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 分页
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
  }
};
</script>

<style scoped>
</style>
