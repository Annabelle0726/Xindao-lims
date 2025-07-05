<template>
  <div>
    <el-dialog title="数据查看" :visible.sync="isShow" width="80%" @closed="$emit('closeDataLook')">
      <ul class="tab">
        <li v-for="(m,i) in dataVisibleTitle" :key="i" :class="{active:i===dataVisibleIndex}" @click="handleDataVisibleTab(m,i)">{{m.label}}</li>
      </ul>
      <div>
        <lims-table :tableData="tableData" :column="column"
                    @pagination="pagination" height="500px" key="tableData"
                    :page="page" :tableLoading="tableLoading"></lims-table>
      </div>
    </el-dialog>
    <un-pass-retest-result :retestVisible="retestVisible" :retestInfo="retestInfo" @closeRetestLook="closeRetestLook" v-if="retestVisible"></un-pass-retest-result>
  </div>
</template>

<script>
import UnPassRetestResult from "./unPassRetestResult.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {getRetestResult, selectSampleAndProductByOrderId} from "@/api/business/rawMaterialOrder";

export default {
  name: "dataLookVisible",
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable, UnPassRetestResult},
  props: {
    dataDialogVisible: {
      type: Boolean,
      default: () => false
    },
    dataLookInfo: {
      type: Object,
      default: () => {}
    },
  },
  data() {
    // 这里存放数据
    return {
      tableData: [],
      tableLoading: false,
      column: [
        {label: '样品编号', prop: 'sampleCode'},
        {label: '样品名称', prop: 'sample'},
        {label: '检验项分类', prop: 'inspectionItemClass'},
        {label: '检验项', prop: 'inspectionItem'},
        {label: '检验子项', prop: 'inspectionItemSubclass'},
        {label: '单位', prop: 'unit'},
        {label: '样品型号', prop: 'model'},
        {label: '条件', prop: 'radius'},
        {label: '电缆标识', prop: 'cableTag'},
        {label: '试验要求', prop: 'tell'},
        {label: '检验结果', prop: 'lastValue'},
        {
          dataType: 'tag',
          label: '结果判定',
          prop: 'insResult',
          formatData: (params) => {
            if (params == 1) {
              return '合格'
            } else if (params == 0) {
              return '不合格'
            } else if (params == 3) {
              return '不判定'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 0) {
              return 'danger'
            } else if (params == 3) {
              return ''
            }  else {
              return null
            }
          }
        },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '170px',
          operation: [
            {
              name: '不合格复测查看',
              type: 'text',
              clickFun: (row) => {
                this.getRetestResult(row);
              },
              disabled: (row, index) =>  {
                return row.insResult!=0
              }
            },
          ]
        }
      ],
      page: {
        total:0,
        size:10,
        current:1
      },
      isShow: this.dataDialogVisible,
      dataVisibleTitle: [
        {
          label: '进厂检验',
          value: 0
        },
        {
          label: '季度检验',
          value: 1
        },
      ],
      dataVisibleIndex: 0, // 数据查看tab栏选择值
      entity: {
        id: null,
      },
      retestVisible: false,
      retestInfo: []
    }
  },
  mounted() {
    this.refreshTable()
  },
  // 方法集合
  methods: {
    // 切换数据查看tab栏
    handleDataVisibleTab (m, i) {
      this.dataVisibleIndex = i
      this.refreshTable()
    },
    // 查询回调
    refreshTable() {
      if (this.dataVisibleIndex === 0) {
        this.entity.id = this.dataLookInfo.enterOrderId
      } else {
        this.entity.id = this.dataLookInfo.quarterOrderId
      }
      this.tableLoading = true
      const params = {...this.entity, ...this.page}
      selectSampleAndProductByOrderId(params).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    pagination (page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    // 查看不合格复测结果
    getRetestResult (row) {
      getRetestResult({insProductId: row.insProductId}).then(res => {
        this.retestVisible = true
        this.retestInfo = res.data
      })
    },
    closeRetestLook () {
      this.retestVisible = false
    },
  },
}
</script>

<style scoped>
.tab {
  list-style-type: none;
  display: flex;
  margin-top: 0 !important;
  padding-left: 0 !important;
}

.tab li {
  line-height: 24px;
  padding: 6px 14px;
  font-size: 14px;
  color: #333333;
  border: 1px solid #EEEEEE;
  cursor: pointer;
}

.tab li:nth-child(1) {
  border-radius: 8px 0 0 8px;
}

.tab li:nth-child(2) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}
</style>
