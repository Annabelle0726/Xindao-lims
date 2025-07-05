<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="不合格描述" prop="laboratoryName">
            <el-input v-model="searchForm.raiseResult" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 23em)'" :table-data="tableData"
        :table-loading="tableLoading" :page="page" @pagination="pagination">
      </limsTable>
    </div>
    <corrective-action-d-ia v-if="correctiveActionDIa" ref="correctiveActionDIa"
      @closeRectifyDia="closeRectifyDia"></corrective-action-d-ia>
    <view-test-record v-if="viewTestRecordDialog" ref="viewTestRecordDialog"></view-test-record>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import CorrectiveActionDIa from './correctiveActionDIa.vue';
import ViewTestRecord from './ViewTestRecord.vue';
import {
  pageInternalCorrect,
  exportInternalCorrect,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'

export default {
  name: 'correctiveAction',
  // import 引入的组件需要注入到对象中才能使用
  components: { CorrectiveActionDIa, limsTable, ViewTestRecord },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        raiseResult: '',
      },
      tableColumn: [
        {
          label: '不合格或偏离事实的描述',
          prop: 'raiseResult',
          minWidth: '100'
        },
        {
          label: '原因分析',
          prop: 'causeResult',
          minWidth: '100'
        },
        {
          label: '纠正措施',
          prop: 'correctResult',
          minWidth: '100'
        },
        {
          label: '实施验证结果',
          prop: 'validationResult',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '220',
          label: '操作',
          operation: [
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            },
            {
              name: '纠正',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('rectify', row);
              },
            },
            {
              name: '查看附件',
              type: 'text',
              clickFun: (row) => {
                this.viewFiles(row);
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
      correctiveActionDIa: false,
      viewTestRecordDialog: false,
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
        raiseResult: this.searchForm.raiseResult,
      }
      const page = this.page
      this.tableLoading = true
      pageInternalCorrect({ ...entity, ...page }).then(res => {
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
      this.searchForm.raiseResult = '';
      this.searchList()
    },
    // 查看附件
    viewFiles(row) {
      this.viewTestRecordDialog = true
      this.$nextTick(() => {
        this.$refs.viewTestRecordDialog.openDia(row)
      })
    },
    openFormDia(type, row) {
      this.correctiveActionDIa = true
      this.$nextTick(() => {
        this.$refs.correctiveActionDIa.openDia(type, row)
      })
    },
    // 导出
    handleDown(row) {
      exportInternalCorrect({ correctId: row.correctId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '内审纠正措施' + '.docx');
      })
    },
    closeRectifyDia() {
      this.correctiveActionDIa = false
      this.searchList()
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
  }
};
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
