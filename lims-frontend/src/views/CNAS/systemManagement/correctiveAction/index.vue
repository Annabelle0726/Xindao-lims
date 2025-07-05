<template>
  <div class="capacity-scope">
    <div>
      <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
        <el-form-item label="不合格描述" prop="raiseResult">
          <el-input v-model="searchForm.raiseResult" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="searchList">查询</el-button>
          <el-button size="mini" @click="resetSearchForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 17em)'" :table-data="tableData"
                 :table-loading="tableLoading" @pagination="pagination"
                 :page="page">
      </limsTable>
    </div>
    <corrective-info v-if="correctiveInfo" ref="correctiveInfo"></corrective-info>
    <ViewTestRecord v-if="viewTestRecordDialog" ref="viewTestRecordDialog"></ViewTestRecord>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import CorrectiveInfo from './components/correctiveInfo.vue';
import ViewTestRecord from './components/ViewTestRecord.vue';
import {
  pageSuperviseDetailCorrect,
  exportSuperviseDetaillCorrect,
} from '@/api/cnas/systemManagement/correctiveAction.js'

export default {
  name: 'CorrectiveAction',
  // import 引入的组件需要注入到对象中才能使用
  components: {
    // QualityInfo,
    CorrectiveInfo,
    limsTable,
    ViewTestRecord
  },
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
                this.handleDown(row)
              }
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
      correctiveInfo: false,
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
      pageSuperviseDetailCorrect({ ...entity, ...page }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 导出
    handleDown(row) {
      exportSuperviseDetaillCorrect({ superviseDetailsCorrectId: row.superviseDetailsCorrectId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '监督纠正措施' + '.docx');
      })
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.raiseResult = '';
      this.searchList()
    },
    // 查看详情
    viewInfo(row) {
      this.correctiveInfo = true
      this.$nextTick(() => {
        this.$refs.correctiveInfo.openDia(row)
      })
    },
    // 查看附件
    viewFiles(row) {
      this.viewTestRecordDialog = true
      this.$nextTick(() => {
        this.$refs.viewTestRecordDialog.openDia(row)
      })
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
.view-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding-left: 20px;
}

.search-background {
  width: 100%;
  height: 80px;
  line-height: 80px;
  background-color: #ffffff;
  display: flex;
}

.search-group {
  display: flex;
  align-items: center;
  margin: 0 20px;
}

.table {
  background-color: #ffffff;
}
</style>
