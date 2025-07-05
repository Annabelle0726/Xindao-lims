<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="会议地点" prop="place">
            <el-input v-model="searchForm.place" clearable size="small"></el-input>
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
    <meeting-records-dia v-if="meetingRecordsDia" ref="meetingRecordsDia"
      @closeYearDia="closeYearDia"></meeting-records-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import MeetingRecordsDia from './meetingRecordsDia.vue';
import ManagementFormDIa from './managementFormDIa.vue';
import {
  getPageMeeting,
  deleteMeeting,
  exportMeeting,
} from '@/api/cnas/systemManagement/managementReview.js'

export default {
  name: 'meetingRecords',
  // import 引入的组件需要注入到对象中才能使用
  components: { ManagementFormDIa, MeetingRecordsDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        place: '',
      },
      tableColumn: [
        {
          label: '时间',
          prop: 'meetingTime',
          minWidth: '100'
        },
        {
          label: '主持人',
          prop: 'compere',
          minWidth: '100'
        },
        {
          label: '会议地点',
          prop: 'place',
          minWidth: '100'
        },
        {
          label: '会议内容摘要',
          prop: 'content',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '120',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              }
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
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
      meetingRecordsDia: false
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      this.tableLoading = true
      getPageMeeting({ place: this.searchForm.place, pages: this.page.current, size: this.page.size }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 新增，编辑弹框
    openFormDia(type, row) {
      this.meetingRecordsDia = true
      this.$nextTick(() => {
        this.$refs.meetingRecordsDia.openDia(type, row)
      })
    },
    closeYearDia() {
      this.meetingRecordsDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.place = '';
      this.searchList()
    },
    // 删除
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        deleteMeeting({ id: row.id }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
    handleDown(row) {
      exportMeeting({ id: row.id }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '会议记录.docx');
      })
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
