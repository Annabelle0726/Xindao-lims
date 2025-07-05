<template>
  <div class="flex_column">
    <div>
      <div v-if="isDepartment" style="display: flex;justify-content: space-between;margin-bottom: 10px">
        <el-button size="small" type="primary" @click="getTableData">刷新</el-button>
        <el-button size="small" type="primary" icon="el-icon-plus" @click="openDialog">新增</el-button>
      </div>
      <lims-table :tableData="tableData" :column="columnData"
                  @pagination="page" :height="'calc(100vh - 18em)'"
                  :page="pagination" :tableLoading="loading"></lims-table>
    </div>
    <Add ref="mandateModal" @refresh="getTableData"></Add>
  </div>
</template>
<script>
import Add from "../components/mandateAdd.vue"
import limsTable from "@/components/Table/lims-table.vue";
import {
  deletePersonPostAuthorizationRecord, exportPersonPostAuthorizationRecord,
  PersonPostAuthorizationRecordPage
} from "@/api/cnas/personal/personPostAuthorizationRecord";
import {delCustomById} from "@/api/system/customer";

export default {
  components: {
    limsTable,
    Add
  },
  props: {
    departId: {
      type: Number,
      default: () => {
        return null;
      }
    },
    isDepartment: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      columnData: [
        {
          label: '序号',
          prop: 'id'
        }, {
          label: '证书编号',
          prop: 'certificateNumber'
        }, {
          label: '被任职人员',
          prop: 'userName'
        }, {
          label: '任职岗位',
          prop: 'post'
        }, {
          label: '理论考试成绩',
          prop: 'num1',
          width: 120
        },{
          label: '操作技能考试成绩',
          prop: 'num2',
          width: 150
        },{
          label: '操作时间',
          prop: 'updateTime'
        }, {
          label: '备注',
          prop: 'remarks',
          width: 300
        }, {
          label: '操作',
          dataType: 'action',
          width: 160,
          fixed: 'right',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openDialog(row, true)
              }
            }, {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            }, {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.deleteNotify(row.id)
              }
            }
          ]
        },
      ],
      tableData: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      loading: false
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    openDialog(row, type=false) {
      this.$refs.mandateModal.openDialog(row, type)
    },
    /**
     * @desc 查询表格数据
     */
    async getTableData() {
      const params = this.isDepartment ? {
        departLimsId: this.departId,
        current: this.pagination.current,
        size: this.pagination.pageSize
      } : {
        userId: this.departId,
        current: this.pagination.current,
        size: this.pagination.pageSize
      }
      this.loading = true
      PersonPostAuthorizationRecordPage(params).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.pagination.total = res.data.total
      }).catch(err => {
        this.loading = false
      })
    },
    page (page) {
      this.pagination.size = page.limit
      this.getTableData()
    },
    /**
     * @desc 删除任职记录
     */
    deleteNotify(id) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deletePersonPostAuthorizationRecord({id: id}).then(res => {
          this.$message.success('删除成功')
          this.getTableData()
        }).catch(e => {
          this.$message.error('删除失败')
        })
      }).catch(() => {})
    },
    handleDown(row){
      exportPersonPostAuthorizationRecord({id:row.id}).then(res => {
        const blob = new Blob([res],{ type: 'application/octet-stream' });
        this.$download.saveAs(blob, '任职授权-'+row.certificateNumber+'-'+row.post + '.docx')
      })
    }
  },
  watch: {
    departId: {
      handler(newId, oldId) {
        if (newId) {
          this.getTableData();
        }
      }
    }
  }
}
</script>
<style scoped>
</style>
