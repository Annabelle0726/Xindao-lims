<!--成品抽样页面-->
<template>
  <div style="padding: 10px 0">
    <div class="card">
      <div class="title">
        <el-button size="small" type="primary" @click="addTemQuarter">新增</el-button>
        <el-button size="small" type="primary" @click="quarterSample">季度抽样</el-button>
        <el-button size="small" type="primary" @click="handleStockList">刷新</el-button>
      </div>
      <el-table
        ref="finishedproducttransferTable"
        v-loading="tableLoading"
        :data="stockList"
        :header-cell-style="lineSideWarehouseTableHeaderCellStyle"
        :row-class-name="lineSideWarehouseTableRowClassName"
        class="finishedproducttransfer-table"
        height="calc(100vh - 240px)"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="45"></el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          align="center"
          label="客户订单编号"
          prop="customerOrderNo"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">客户订单编号</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.customerOrderNo"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.customerOrderNo }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="成品零件号"
          prop="partNo"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">成品零件号</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.partNo"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.partNo }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="零件名称"
          prop="partName"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">零件名称</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.partName"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.partName }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          align="center"
          label="仓库"
          prop="warehouseName"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">仓库</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.warehouseName"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.warehouseName }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          align="center"
          label="库位号"
          prop="locationNo"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">库位号</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.locationNo"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.locationNo }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          align="center"
          label="库位名称"
          prop="locationName"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">库位名称</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.locationName"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.locationName }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="批次号"
          prop="partBatchNo"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">批次号</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.partBatchNo"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.partBatchNo }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="入库来源"
          prop="inSource"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">入库来源</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.inSource"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.inSource }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="外护颜色"
          prop="outerColor"
          width="140"
        >
          <template slot="header" slot-scope="scope">
            <div style="line-height: 14px;margin-bottom: 6px">外护颜色</div>
            <div class="th" @click.stop>
              <el-input
                v-model="queryParamOne.outerColor"
                clearable
                size="mini"
                type="text"
                @clear="handleStockList"
                @keyup.enter.native="handleStockList"
              ></el-input>
            </div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.outerColor }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="库存数量"
          prop="stockQuantity"
          width="140"
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="可用库存数量"
          prop="availableStockQuantity"
          width="140"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.availableStockQuantity }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagin-page" style="margin-top: 0;margin-bottom: 0;">
        <el-pagination
          background
          :current-page="queryReport.current"
          :page-size="queryReport.size"
          :page-sizes="[10, 15, 20, 50, 100]"
          :total="queryReport.total"
          layout="total, sizes, prev, pager, next, jumper"
          style="text-align:right;padding: 20px 16px;"
          @size-change="handleSizeChangeReport"
          @current-change="handleCurrentChangeReport"
        >
        </el-pagination>
      </div>
    </div>
    <add-quarter-item ref="addQuarterItem"></add-quarter-item>
  </div>
</template>

<script>

import AddQuarterItem from "./components/addQuarterItem.vue";
import {getIfsStock} from "@/api/business/finishedProductSampling";
import {mapGetters} from "vuex";

export default {
  name: 'FinishedProductSampling',
  data() {
    return {
      stockList: [],
      queryParamOne: {
        partNo: null,
        partName: null,
        warehouseName: null,
        locationName: null,
        locationNo: null,
        partBatchNo: null,
        stockQuantity: null,
        availableStockQuantity: null,
        outerColor: null,
        customerOrderNo: null,
        inSource: null,
      },
      queryReport: {
        current: 1,
        size: 20,
        total: 0
      },
      tableLoading: false,
      multipleSelection: [], // table所选中的对象
      showEditForm: false,
      showMoveForm: false,
      showMoveAllForm: false,
      erpfinishstock: {},
      isAvaliable: false,
      isPackage: false,
      quarterTemItems: [],
      operationType: ''
    }
  },
  components: {AddQuarterItem},
  mounted() {
    this.getIfsStock()
  },
  updated() {
    this.$nextTick(() => {
      this.$refs.finishedproducttransferTable.doLayout()
    })
  },
  computed: {
    ...mapGetters(["nickName"]),
  },
  methods: {
    // 季度抽样
    quarterSample () {
      this.$refs.addQuarterItem.openDia(this.quarterTemItems, 'add')
    },
    addTemQuarter () {
      if (this.multipleSelection.length > 0) {
        const multipleSelection = JSON.parse(JSON.stringify(this.multipleSelection))
        if (multipleSelection.length > 0) {
          multipleSelection.forEach(item => {
            if (item.partName.includes(' ')) {
              const list = item.partName.match(/^(\S+)\s(.*)/).slice(1)
              console.log('list', list)
              item.productType = list[0]
              item.spotCheckNumber = list[1]
            }
            item.dutyUser = this.nickName
            item.itemRemark = item.partBatchNo;
            this.quarterTemItems.push(item)
          })
        }
        this.$message.success('暂存成功')
        this.$refs.finishedproducttransferTable.clearSelection()
      } else {
        this.$message.warning('请选择数据')
      }
    },
    // 获取数据列表
    getIfsStock() {
      this.tableLoading = true
      this.stockList = []
      const newReqParam = this.getFinalParam()
      getIfsStock(newReqParam).then((response) => {
        this.tableLoading = false
        const resData = response.data
        this.queryReport.total = resData.total
        const resStockList = resData.data
        resStockList.forEach((item) => {
          this.stockList.push({
            partNo: item.PART_NO,
            partName: item.PART_DESC,
            warehouseName: item.WAREHOUSE_ID,
            locationName: item.LOCATION_DESC,
            locationNo: item.LOCATION_NO,
            partBatchNo: item.LOT_BATCH_NO,
            stockQuantity: item.QTY_ONHAND,
            availableStockQuantity: item.QTY_AVAILABLE,
            outerColor: item.ATTR4,
            customerOrderNo: item.ATTR6,
            inSource: item.ATTR23,
          })
        })
      }).catch(() => {
        this.tableLoading = false
      })
    },
    getFinalParam() {
      const newReqParam = {
        partNo: this.queryParamOne.partNo,
        partDescription: this.queryParamOne.partName,
        warehouse: this.queryParamOne.warehouseName
          ? this.queryParamOne.warehouseName + '%'
          : null,
        locDesc: this.queryParamOne.locationName,
        locNo: this.queryParamOne.locationNo,
        lotBatchNo: this.queryParamOne.partBatchNo,
        quantityFlag: this.queryParamOne.stockQuantity,
        outerColor: this.queryParamOne.outerColor,
        otcOrderNo: this.queryParamOne.customerOrderNo,
        availableStockQuantity: this.queryParamOne.availableStockQuantity,
        inSource: this.queryParamOne.inSource,
        page: this.queryReport.current,
        limit: this.queryReport.size
      }
      return newReqParam
    },
    lineSideWarehouseTableHeaderCellStyle({row, column, rowIndex, columnIndex}) {
      let headerStyle = 'background:#FAFAFA;color:#666;'
      if (columnIndex === 0) {
        headerStyle += 'border-radius: 6px 0px 0px 0px;'
      } else if (columnIndex === 13) {
        headerStyle += 'border-radius: 0px 6px 0px 0px;'
      }
      return headerStyle
    },
    lineSideWarehouseTableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 === 1) {
        return 'stripe-row'
      } else {
        return ''
      }
    },
    handleSizeChangeReport(val) {
      this.queryReport.size = val
      this.queryReport.current = 1
      this.getIfsStock()
    },
    handleCurrentChangeReport(val) {
      this.queryReport.current = val
      this.getIfsStock()
    },
    handleStockList() {
      this.queryReport.current = 1
      this.getIfsStock()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>

<style scoped>
>>>.el-table__header {
  height: 70px;
}
.card {
  height: calc(100% - 40px);
  background: #ffffff;
  padding: 10px;
}
.title {
  text-align: right;
  margin-bottom: 10px;
}
</style>
