<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="时间" prop="meetingDate">
            <el-input v-model="searchForm.meetingDate" clearable size="small"></el-input>
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
    <audit-meeting-sign-dia v-if="auditMeetingSignDia" ref="auditMeetingSignDia"
      @closeYearDia="closeYearDia"></audit-meeting-sign-dia>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import AuditMeetingSignDia from './auditMeetingSignDia.vue';
import {
  pageInternalMeeting,
  delInternalMeeting,
  exportInternalMeeting,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'

export default {
  name: 'auditMeetingSign',
  // import 引入的组件需要注入到对象中才能使用
  components: { AuditMeetingSignDia, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        meetingDate: '',
      },
      tableColumn: [
        {
          label: '时间',
          prop: 'meetingDate',
          minWidth: '100'
        },
        {
          label: '主持人',
          prop: 'compere',
          minWidth: '100'
        },
        {
          label: '地点',
          prop: 'place',
          minWidth: '100'
        },
        {
          label: '会议主题',
          prop: 'subject',
          minWidth: '100'
        },
        {
          label: '参会人员',
          prop: 'participantName',
          minWidth: '120'
        },
        {
          dataType: 'action',
          fixed: 'right',
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
            }
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
      auditMeetingSignDia: false,
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      const entity = this.searchForm
      const page = this.page
      this.tableLoading = true
      pageInternalMeeting({ ...entity, ...page }).then(res => {
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
      this.auditMeetingSignDia = true
      this.$nextTick(() => {
        this.$refs.auditMeetingSignDia.openDia(type, row)
      })
    },
    closeYearDia() {
      this.auditMeetingSignDia = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.meetingDate = '';
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
        delInternalMeeting({ meetingId: row.meetingId }).then(res => {
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
    // 导出
    handleDown(row) {
      exportInternalMeeting({ meetingId: row.meetingId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '内审会议签到' + '.docx');
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
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
