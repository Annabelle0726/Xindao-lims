<template>
  <div class="app-container">
    <div v-if="!isShowDataCom">
      <el-form ref="entity" size="small" :inline="true">
        <el-form-item style="width: 16%;">
          <el-date-picker
            v-model="datePicker"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            placeholder="选择日期"
            range-separator="至"
            size="small"
            start-placeholder="开始日期"
            style="width: 100%;"
            type="daterange"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="样品名称" prop="sampleName">
          <el-input v-model="sampleName" clearable placeholder="请输入样品名称" size="small"></el-input>
        </el-form-item>
        <el-form-item label="型号" prop="modelName">
          <el-input v-model="modelName" clearable placeholder="请输入型号" size="small"></el-input>
        </el-form-item>
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="supplierName" clearable placeholder="请输入供应商名称" size="small"></el-input>
        </el-form-item>
        <el-form-item label="检验项名称" prop="supplierName">
          <el-select v-model="itemNames" :loading="selectLoading" clearable multiple placeholder="请选择"
                     size="small" style="width: 90%;" @focus="getItemList">
            <el-option
              v-for="item in itemNamesList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" @click="changeData">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="!isShowDataCom" style="overflow-y: auto;height: calc(100% - 70px);overflow-x: hidden">
      <el-row>
        <el-col :span="24">
          <div class="inspection-card">
            <div style="display: flex;align-items: center;margin-bottom: 10px;justify-content: space-between;">
              <div>原材料项检分析列表</div>
              <el-button size="small" type="primary" @click="openShowData">查看数据分析</el-button>
            </div>
            <lims-table :tableData="editTableData" :column="editColumn"
                        height="400" key="tableData" :isSelection="true"
                        :handleSelectionChange="handleSelectionChange"
                        :tableLoading="editLoading"></lims-table>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" style="padding-top: 14px">
          <div style="display: flex;margin-left: 10px;align-items: center">
            <span style="width: 100px">分组类型：</span>
            <el-select v-model="groupType" clearable placeholder="请选择"
                       size="small"
                       style="width: 90%;" @change="getBarInfo">
              <el-option
                v-for="item in groupTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div class="inspection-card">
            <div class="title">检验项数据对比</div>
            <Echarts ref="chart"
                     :chartStyle="chartStyle"
                     :dataset="dataset"
                     :grid="grid"
                     :options="echartsOptions"
                     :series="echartsSeries"
                     :tooltip="tooltip"
                     :xAxis="xAxis"
                     :yAxis="yAxis"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div class="inspection-card">
            <div class="title">原材料项检合格率</div>
            <Echarts ref="chart"
                     :chartStyle="chartStyle"
                     :dataset="dataset1"
                     :grid="grid"
                     :options="echartsOptions1"
                     :series="echartsSeries1"
                     :tooltip="tooltip"
                     :xAxis="xAxis"
                     :yAxis="yAxis1"
                     style="height: 40vh;"></Echarts>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog :visible.sync="dataDialogVisible" title="数据查看" width="80%">
      <div v-if="dataDialogVisible" style="height: 70vh;overflow-y: auto;">
        <lims-table :tableData="tableData" :column="column"
                    @pagination="pagination" key="tableData"
                    :page="page" :tableLoading="tableLoading"></lims-table>
      </div>
    </el-dialog>
    <DataComparison v-if="isShowDataCom" :comparisonData="comparisonData"
                    :selectRow="selectRow" @goBack="goBack"></DataComparison>
  </div>
</template>

<script>
import Echarts from "@/components/echarts/echarts.vue";
import DataComparison from '@/components/echarts/DataComparison.vue'
import limsTable from "@/components/Table/lims-table.vue";
import {
  getRawItemNames,
  getRawProductAnalysis,
  getRawProductAnalysisAllList,
  getRawProductAnalysisRawPass, getRawSupplierCompare
} from "@/api/statisticalCharts/dataAnalysis";
import {selectSampleAndProductByOrderId} from "@/api/business/rawMaterialOrder";

export default {
  name: "ItemInspectionAnalysis",
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable, Echarts, DataComparison},
  data() {
    // 这里存放数据
    return {
      selectChangeRows: [], // 选中的数据
      groupTypeList: [
        {label: '样品', value: '0'},
        {label: '同一厂家, 同一型号, 不同批次', value: '1'},
        {label: '同一型号, 不同厂家', value: '2'},
      ],
      itemNames: [], // 检验项名称
      itemNamesList: [],
      selectLoading: false,
      datePicker: [], // 时间
      groupType: '', // 分组
      beginDate: '',
      endDate: '',
      sampleName: '', // 样品名称
      modelName: '', // 型号
      supplierName: '', // 供应商名称
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      echartsOptions: {},
      echartsOptions1: {},
      dataset: {
        dimensions: [],
        source: [],
      },
      dataset1: {
        dimensions: [],
        source: [],
      },
      xAxis: [{ type: 'category' }],
      yAxis: [{}],
      yAxis1: [{
        axisLabel: {
          formatter: '{value} %'
        }
      }],
      echartsSeries: [],
      echartsSeries1: [],
      tooltip: {},
      chartStyle: {
        width: '100%',
        height: '96%' // 设置图表容器的高度
      },
      editColumn: [
        {
          label: '批号',
          minWidth: '120px',
          prop: 'updateBatchNo'
        }, {
          label: '委托编号',
          minWidth: '120px',
          prop: 'entrustCode'
        }, {
          label: '零件号',
          minWidth: '120px',
          prop: 'partNo'
        }, {
          label: '零件描述',
          minWidth: '120px',
          prop: 'partDesc'
        }, {
          label: '供应商名称',
          minWidth: '120px',
          prop: 'supplierName',
        }, {
          dataType: 'tag',
          label: '检验状态',
          prop: 'inspectStatus',
          minWidth: '120px',
          formatData: (params) => {
            if (params == 0) {
              return '检验中'
            } else if (params == 1) {
              return '合格'
            } else if (params == 2) {
              return '不合格'
            } else if (params == 3) {
              return '未下单'
            } else if (params == 4) {
              return '让步放行'
            }
          },
          formatType: (params) => {
            if (params == 1 || params == 4) {
              return 'success'
            } else if (params == 3) {
              return 'warning'
            } else if (params == 0 || params == 2) {
              return 'danger'
            }
          }
        }, {
          label: '样品名称',
          minWidth: '300px',
          prop: 'sampleName'
        }, {
          label: '样品型号',
          minWidth: '300px',
          prop: 'sampleModel'
        },{
          label: '下发时间',
          minWidth: '120px',
          prop: 'sendTime'
        },{
          label: '抵达的采购数量',
          minWidth: '130px',
          prop: 'qtyArrived'
        },{
          label: '单位',
          minWidth: '120px',
          prop: 'buyUnitMeas'
        },
        {
          dataType: 'action',
          width: '80px',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              },
            },
          ]
        }
      ],
      editTableData: [],
      editLoading: false,
      dataDialogVisible: false,
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
        }
      ],
      page: {
        total:0,
        size:10,
        current:1
      },
      currentRow: {},
      isShowDataCom: false,
      comparisonData: {},
      selectRow: {},
    }
  },
  mounted() {
    // this.getBar()
  },
  // 方法集合
  methods: {
    getBarInfo (val) {
      if (val === '1') {
        if (!this.modelName || !this.supplierName) {
          this.$message.warning('型号或供应商不能为空')
          this.groupType = ''
          return
        }
      } else if (val === '2') {
        if (!this.modelName) {
          this.$message.warning('型号不能为空')
          this.groupType = ''
          return
        }
        if (this.supplierName) {
          this.$message.warning('不可选供应商名称')
          this.groupType = ''
          return
        }
      }
      this.getBar()
      this.getBar1()
    },
    // 获取合格率图表数据
    getBar() {
      this.echartsSeries = []
      const params = {
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
        itemNames: this.itemNames,
        groupType: this.groupType,
      }
      getRawProductAnalysis(params).then((res) => {
        if (res.data === null) {
          this.$message.warning('暂无数据')
          return
        }
        for (let i = 0; i < res.data.itemNames.length; i++) {
          this.echartsSeries.push({type: 'line', label: {
              show: true,
              position: 'top'
            },})
        }
        this.dataset.dimensions = this.HaveJson(res.data.itemNames)
        this.dataset.dimensions.unshift('product')
        this.dataset.source = this.HaveJson(res.data.productList)

      })
    },
    getBar1() {
      this.echartsSeries1 = []
      const params = {
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
        itemNames: this.itemNames,
        groupType: this.groupType,
      }
      getRawProductAnalysisRawPass(params).then((res) => {
        if (res.data === null) {
          this.$message.warning('暂无数据')
          return
        }
        for (let i = 0; i < res.data.itemNames.length; i++) {
          this.echartsSeries1.push({type: 'line',tooltip: {
              valueFormatter: function (value) {
                return value + '%';
              }
            },label: {
              show: true,
              position: 'top',
              formatter: (params) => params.value[params.dimensionNames[params.encode.y[0]]] + '%'
            },})
        }
        this.dataset1.dimensions = this.HaveJson(res.data.itemNames)
        this.dataset1.dimensions.unshift('product')
        this.dataset1.source = this.HaveJson(res.data.productList)

      })
    },
    // 数据查看
    handleDataLook(row) {
      this.dataDialogVisible = true;
      this.currentRow = row
      this.refreshTable(row)
    },
    // 查询回调
    refreshTable(row) {
      selectSampleAndProductByOrderId({id: row.id, ...this.page}).then(res => {
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
      this.refreshTable(this.currentRow)
    },
    changeData () {
      if (this.datePicker !== null && this.datePicker.length > 0) {
        this.beginDate = this.datePicker[0] + ' 00:00:00'
        this.endDate = this.datePicker[1] + ' 23:59:59'
      } else {
        this.beginDate = ''
        this.endDate = ''
      }
      this.getBar()
      this.getBar1()
      this.getTableData()
    },
    getTableData () {
      this.editLoading = true
      const params = {
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
        itemNames: this.itemNames,
      }
      getRawProductAnalysisAllList(params).then(res => {
        this.editLoading = false
        this.editTableData = res.data
      }).catch(() => {
        this.editLoading = false
      })
    },
    // 获取检验项下拉框
    getItemList () {
      this.selectLoading = true
      this.itemNamesList = []
      const params = {
        beginDate: this.beginDate,
        endDate: this.endDate,
        sampleName: this.sampleName,
        modelName: this.modelName,
        supplierName: this.supplierName,
      }
      getRawItemNames(params).then((res) => {
        this.selectLoading = false
        res.data.map(val => {
          const obj = Object.assign({
            label: val,
            value: val,
          })
          this.itemNamesList.push(obj)
        })
      }).catch(() => {
        this.selectLoading = false
      })
    },
    goBack () {
      this.isShowDataCom = false
    },
    openShowData () {
      if (this.selectChangeRows.length !== 5) {
        this.$message.warning('请选择5条数据')
        return
      }
      if (this.itemNames.length !== 1) {
        this.$message.warning('只能选择1项检验项名称')
        return
      }
      let ids = []
      let itemNames = []
      this.selectChangeRows.forEach(item => {
        ids.push(item.enterOrderId)
      })
      this.itemNames.forEach(item => {
        itemNames.push(item)
      })
      const params = {
        orderIds: ids,
        itemNames: itemNames
      }
      getRawSupplierCompare(params).then(res => {
        this.comparisonData = res.data
        this.selectRow = params
        this.isShowDataCom = true
      })
    },
    handleSelectionChange (val) {
      this.selectChangeRows = val
    },
  },
}
</script>

<style scoped>
.inspection-card{
  width: 100%;
  margin-top: 10px;
}
</style>
