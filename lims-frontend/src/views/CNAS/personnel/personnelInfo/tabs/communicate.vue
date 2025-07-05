<template>
  <div class="flex_column">
    <div v-if="isDepartment" style="display: flex;justify-content: space-between;margin-bottom: 10px">
      <el-button size="small" type="primary" @click="getTableData">刷新</el-button>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="openDialog">新增</el-button>
    </div>
    <lims-table :tableData="tableData" :column="columnData"
                @pagination="page" :height="'calc(100vh - 18em)'"
                :page="pagination" :tableLoading="loading"></lims-table>
    <Add ref="communicateModal" @submit="getTableData"></Add>
  </div>
</template>
<script>
import CommunicateAdd from "../components/communicateAdd.vue"
import limsTable from "@/components/Table/lims-table.vue";
import {
  deletePersonCommunicationAbility, exportPersonCommunicationAbility,
  personPersonCommunicationAbilityPage
} from "@/api/cnas/personal/personPersonCommunicationAbilityPage";

export default {
  components: {
    limsTable,
    Add: CommunicateAdd
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
          label: '沟通人',
          prop: 'userName'
        }, {
          label: '沟通时间',
          prop: 'communicationTime'
        }, {
          label: '沟通地点',
          prop: 'communicationPlace'
        }, {
          label: '沟通内容',
          prop: 'communicationContent'
        }, {
          label: '操作',
          dataType: 'action',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openDialog(row, true)
              }
            }, {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            }, {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPerson(row.id)
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
      this.$refs.communicateModal.openDialog(row, type)
    },
    getTableData() {
      this.loading = true
      const params = this.isDepartment ? {
        departLimsId: this.departId,
        current: this.pagination.current,
        size: this.pagination.size
      } : {
        userId: this.departId,
        current: this.pagination.current,
        size: this.pagination.size
      }
      personPersonCommunicationAbilityPage(params).then(res => {
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
     * @desc 删除沟通记录
     */
    delPerson(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        deletePersonCommunicationAbility({id: id}).then(res => {
          this.$message.success('删除成功!');
          this.getTableData()
        })
      })
    },
    async handleDown(row){
      exportPersonCommunicationAbility({id:row.id}).then(res => {
        const blob = new Blob([res],{ type: 'application/octet-stream' });
        this.$download.saveAs(blob, row.userName+'-沟通记录'+'.docx');
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
