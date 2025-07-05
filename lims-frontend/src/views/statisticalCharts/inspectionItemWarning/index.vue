<template>
  <div class="app-container">
    <div>
      <lims-table :tableData="tableData" :column="column"
                  :height="'calc(100vh - 500px)'" @pagination="pagination"
                  :rowClick="rowClick"
                  :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <div>
      <el-row>
        <el-col :span="24">
          <div class="inspection-card">
            <div class="title">检验项偏差预警数据详情</div>
            <Echarts ref="chart"
                     :chartStyle="chartStyle"
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
    </div>
    <el-dialog :visible.sync="viewDia" title="查看详情" width="1100px">
      <lims-table :tableData="tableData1" :column="column1"
                  height="570"
                  :tableLoading="tableLoading1"></lims-table>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {selectDeviationWarning, selectDeviationWarningPage} from "@/api/statisticalCharts/dataAnalysis";
import Echarts from "@/components/echarts/echarts.vue";

export default {
  name: '',
  // import 引入的组件需要注入到对象中才能使用
  components: {Echarts, limsTable},
  data() {
    // 这里存放数据
    return {
      viewDia: false,
      queryParams: {},
      tableData: [],
      tableLoading: false,
      column: [
        { label: '样品编号', prop: 'sampleCode',width: 150 },
        { label: '样品名称', prop: 'sampleName'},
        { label: '型号', prop: 'sampleModel',width: 150  },
        { label: '供应商名称', prop: 'supplierName',width: 150 },
        { label: '检验项名称', prop: 'inspectionItemName'},
        {
          label: "偏差值%",
          prop: "deviationValue",
          width: 150,
          dataType: "tag",
          formatType: (params) => {
            return 'danger'
          },
        },
        { label: '检测时间', prop: 'detectionTime',width: 160 },
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.openDia(row);
              },
            }
          ]
        }
      ],
      page: {
        total: 0,
        size: 20,
        current: 1
      },
      tableData1: [],
      tableLoading1: false,
      column1: [
        { label: '样品编号', prop: 'sampleCode'},
        { label: '供应商名称', prop: 'supplierName'},
        {
          label: "检测值",
          prop: "testValue",
          width: 150,
          dataType: "tag",
          formatType: (params) => {
            if (this.tableData1.find((m) => m.testValue == params).isIssue == 1) {
              return 'danger'
            } else {
              return 'success'
            }
          },
        },
        { label: '检测时间', prop: 'detectionTime',width: 160 },
      ],
      chartStyle: {
        width: '100%',
        height: '96%' // 设置图表容器的高度
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      tooltip: {},
      echartsOptions: {},
      echartsSeries: [
        {
          name: '检测值',
          type: 'line',
          smooth: true,
          tooltip: {
            valueFormatter: function (value) {
              return value;
            }
          },
          label: {
            show: true,
            position: 'top',
            formatter: function (value) {
              return value.value;
            },
            distance: 14
          },
          data: [],
          markArea: {},
          markPoint: {}
        },
      ],
      xAxis: [
        {
          type: 'category',
          data: [],
          boundaryGap: false,
          axisLabel: {
            interval: 0, // 强制显示所有标签
            rotate: 0,   // 不旋转（可以根据需要调整）
            formatter: function (value) {
              // 每隔一定长度添加换行符
              const maxLength = 9; // 每段最大字符数
              let result = '';
              for (let i = 0; i < value.length; i += maxLength) {
                result += value.substring(i, i + maxLength) + '\n';
              }
              return result.trim(); // 去掉末尾多余的换行符
            },
            margin: 10, // 标签与轴线的距离
          },
        }
      ],
      yAxis: [{
        type: 'value',
        axisLabel: {
          formatter: '{value}'
        }
      }],
    };
  },
  mounted() {
    this.refreshTable()
  },
  // 方法集合
  methods: {
    // 查询列表信息
    refreshTable() {
      this.tableLoading = true
      selectDeviationWarningPage({ ...this.page}).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 查询折线图信息
    rowClick (row) {
      selectDeviationWarning({deviationWarningId: row.deviationWarningId}).then(res => {
        console.log('res---', res)
        if (res.data === null) {
          this.$message.warning('暂无数据')
          return
        }
        let lineData = []
        let xAxis = []
        let markAreas = []; // 存储 markArea 的数组
        let markPoints = [];
        const sampleCodeCount = {};
        res.data.forEach((item, index) => {
          lineData.push(item.testValue)
          // 添加索引以确保唯一性
          // 检查是否需要区分 sampleCode
          if (!sampleCodeCount[item.sampleCode]) {
            sampleCodeCount[item.sampleCode] = 1; // 第一次出现
            xAxis.push(item.sampleCode); // 直接使用原始 sampleCode
          } else {
            sampleCodeCount[item.sampleCode]++; // 增加计数
            const uniqueSampleCode = `${item.sampleCode}-${sampleCodeCount[item.sampleCode]}`;
            xAxis.push(uniqueSampleCode); // 添加后缀以区分
          }

          // 如果 isIssue 为 1，则在此索引处添加一个 markArea
          if (item.isIssue == 1) {
            const startColumn = index > 0 ? xAxis[index - 1] : xAxis[index]; // 起点：前一列或当前列
            const endColumn = xAxis[index]; // 终点：当前列

            markAreas.push([
              {
                xAxis: startColumn, // 从前一列开始
              },
              {
                xAxis: endColumn, // 到当前列结束
              }
            ]);
            markPoints.push({
              name: '问题点',
              coord: [item.sampleCode, item.testValue],
              value: item.testValue,
              itemStyle: {
                color: 'rgba(255, 173, 177, 0.8)'
              },
              label: {
                show: true,
                formatter: function(params) {
                  return params.value; // 自定义标签内容
                },
                color: 'black', // 标签文字颜色
                fontSize: 12,
                distance: 5, // 调整标签与气泡的距离
                padding: [0,0],
                backgroundColor: 'rgba(255, 173, 177, 0.8)', // 标签背景色及透明度
                borderRadius: 4
              }
            });
          }
        })
        this.xAxis[0].data = xAxis
        this.echartsSeries[0].data = lineData
        // 更新 markArea 配置
        this.echartsSeries[0].markArea = {
          itemStyle: {
            color: 'rgba(255, 173, 177, 0.4)' // 设置背景颜色
          },
          data: markAreas // 动态生成的 markArea 数据
        };
        this.echartsSeries[0].markPoint = {
          data: markPoints
        };
      })
    },
    // 重置
    refresh() {
      // this.resetForm('entity')
      this.refreshTable()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    openDia (row) {
      this.viewDia = true
      this.tableLoading1 = true
      selectDeviationWarning({deviationWarningId: row.deviationWarningId}).then(res => {
        this.tableLoading1 = false
        this.tableData1 = res.data
      }).catch(() => {
        this.tableLoading1 = false
      })
    },
  }
};
</script>

<style scoped>
.inspection-card{
  width: 100%;
  margin-top: 10px;
}
</style>
