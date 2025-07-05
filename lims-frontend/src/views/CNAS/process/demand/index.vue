<template>
  <div class="app-container">
    <div style="display: flex;justify-content: space-between;">
      <el-form :model="entitySearch" ref="entitySearch" size="small" :inline="true">
        <el-form-item label="试样名称" prop="sampleName">
          <el-input size="small" placeholder="请输入" clearable v-model="entitySearch.sampleName"
            @keyup.enter.native="refreshTable"></el-input>
        </el-form-item>
        <el-form-item label="委托单位" prop="commissionUnit">
          <el-input v-model="entitySearch.commissionUnit" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="生产单位" prop="production">
          <el-input v-model="entitySearch.production" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="委托人" prop="commissionUser">
          <el-input v-model="entitySearch.commissionUser" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="text-align: right; margin-bottom: 10px">
      <el-button size="small" type="primary" @click="goAdd">新增</el-button>
    </div>
    <div class="table">
      <el-table v-loading="tableListLoading" :data="tableList"
                height="630" border :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
        <el-table-column align="center" label="序号" type="index"></el-table-column>
        <el-table-column label="试样名称" prop="sampleName" show-overflow-tooltip></el-table-column>
        <el-table-column label="委托编号" prop="entrustCode" show-overflow-tooltip></el-table-column>
        <el-table-column label="委托时间" prop="commissionDate" show-overflow-tooltip></el-table-column>
        <el-table-column label="型号" prop="modelNo" show-overflow-tooltip></el-table-column>
        <el-table-column label="委托单位" prop="commissionUnit" show-overflow-tooltip></el-table-column>
        <el-table-column label="生产单位" prop="production" show-overflow-tooltip></el-table-column>
        <el-table-column label="委托人" prop="commissionUser"></el-table-column>
        <el-table-column label="样品数量" prop="quantity"></el-table-column>
        <el-table-column label="样品状态" prop="sampleStatus"></el-table-column>
        <el-table-column label="是否留样" prop="isLeave">
          <template slot-scope="scope">
            <span v-if="scope.row.isLeave === 1">是</span>
            <span v-if="scope.row.isLeave === 0">否</span>
          </template>
        </el-table-column>
        <el-table-column label="样品处理方式" prop="processing" width="150">
          <template slot-scope="scope">
            <span v-if="scope.row.processing === 1">实验室处理</span>
            <span v-if="scope.row.processing === 0">委托单位取回</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="240">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="goUpdate(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleLook(scope.row)">查看</el-button>
            <el-button size="mini" style="color:#F56C6C" type="text" @click="deleteOrder(scope.row)">删除</el-button>
            <el-button size="mini" type="text" @click="openDownloadDia(scope.row)">下载</el-button>
            <el-upload ref='upload' :action="action" :data="{ inspectionOrderId: scope.row.inspectionOrderId }"
              :headers="uploadHeader" :on-error="onError" :on-success="handleSuccessUp" :show-file-list="false"
              accept='image/jpg,image/jpeg,image/png,application/pdf,.doc,.docx,.xlsx' name="file"
              style="background: transparent;display: inline;margin-left: 4px">
              <span style="color: #409EFF">上传</span>
            </el-upload>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="pages.size" :page-sizes="[10, 20, 30, 50, 100]" :total="total1"
        layout="->,total, sizes, prev, pager, next" style="margin-top: 10px" background @size-change="handleSizeChange1"
        @current-change="handleCurrentChange1">
      </el-pagination>
    </div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="orderRowsVisible"
      title="新增检验任务单" width="1000px">
      <div style="display: flex; align-items: center;margin-bottom: 10px">
        <span style="width: 80px">委托编号：</span>
        <el-input v-model="entity.entrustCode" clearable size="small"
          style="width: 300px;margin-right: 10px"></el-input>
        <el-button size="small" type="primary" @click="searchTableData">查询</el-button>
        <el-button size="small" @click="refreshTableData">重 置</el-button>
      </div>
      <el-table v-loading="tableLoading" :data="tableData"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                height="550" style="width: 100%">
        <el-table-column align="center" label="序号" type="index" width="60"></el-table-column>
        <el-table-column label="委托编号" prop="entrustCode"></el-table-column>
        <el-table-column label="样品名称" prop="sample"></el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button align="center" size="mini" type="text" @click="goAddOrder(scope.row)">新增委托单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="tableSearch.size" :page-sizes="[10, 20, 30, 50, 100]" :total="total"
        layout="->,total, sizes, prev, pager, next" style="margin-top: 10px" background @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
      </el-pagination>
    </el-dialog>
    <el-dialog :class="{ downPdf: title == '下载' }" :close-on-click-modal="false" :close-on-press-escape="false"
      :modal="title != '下载'" :title="title" :visible.sync="detailDialogVisible" top="20px" width="1200px">
      <div style="max-height: 75vh;overflow-y: auto;">
        <div id="dialogBody">
          <table border="1" cellpadding="10" class="tables heads" style="border: 1px dashed black;width: 100%">
            <tr>
              <td rowspan="2">
                <img alt="" src="@/assets/logo/ZTTlogo.png" style="width: 80%;">
              </td>
              <td>
                <p>记录名称：检验委托单</p>
              </td>
              <td>
                <p>保存期限：6年</p>
              </td>
            </tr>
            <tr>
              <td>
                <p>记录编号： ZTT/QR-16-04-a</p>
              </td>
              <td>
                <p>归档部门：综合室</p>
              </td>
            </tr>
          </table>
          <h4 style="display: flex;align-items: center;flex-direction: column;justify-content: center;">
            <span style="font-size: 28px;">检 验 委 托 单</span>
          </h4>
          <p v-if="operationType !== 'view'" style="margin-left: 560px;display: flex;align-items: center">
            <span style="width: 100px">委托编号：</span>
            <el-input v-model="currentInfo.entrustCode" clearable size="small"></el-input>
          </p>
          <p v-if="operationType === 'view'" style="margin-top: 16px;margin-left: 600px;">委托编号：{{
            currentInfo.entrustCode }}
          </p>
          <table border="1" cellpadding="10" class="tables" style="width: 100%">
            <tr>
              <td colspan="2">
                <p>试样名称</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.sampleName" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.sampleName }}</td>
              <td>
                <p>委托时间</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-date-picker v-model="currentInfo.commissionDate" clearable format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.commissionDate }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>型 号</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.modelNo" clearable
                  size="small"></el-input>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.modelNo }}</td>
              <td>
                <p>委托单位</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.commissionUnit" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.commissionUnit }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>生产单位</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.production" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.production }}</td>
              <td>
                <p>委托人</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.commissionUser" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.commissionUser }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>样品数量</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.quantity" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.quantity }}</td>
              <td>
                <p>样品状态</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.sampleStatus" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{
                insStateList.find(m => m.value == currentInfo.sampleStatus) ? insStateList.find(m => m.value == currentInfo.sampleStatus).label:'/'
                }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>是否留样</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-radio-group v-model="currentInfo.isLeave" v-removeAriaHidden>
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </td>
              <td v-if="operationType === 'view'">
                <span v-if="currentInfo.isLeave == 1">是</span>
                <span v-else>否</span>
              </td>
              <td>
                <p>样品处理方式</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-radio-group v-model="currentInfo.processing" v-removeAriaHidden>
                  <el-radio :label="0">委托单位取回</el-radio>
                  <el-radio :label="1">实验室处理</el-radio>
                </el-radio-group>
              </td>
              <td v-if="operationType === 'view'">
                <span v-if="currentInfo.processing == 0">委托单位取回</span>
                <span v-else>实验室处理</span>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <p>约定完成时间(报告日期)</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-date-picker v-model="currentInfo.appointed" clearable format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.appointed }}</td>
              <td>
                <p>报告发送方式</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-radio-group v-model="currentInfo.send" v-removeAriaHidden>
                  <el-radio :label="1">自取</el-radio>
                  <el-radio :label="0">其他</el-radio>
                </el-radio-group>
              </td>
              <td v-if="operationType === 'view'">
                <span v-if="currentInfo.send == 1">自取</span>
                <span v-else>其他</span>
              </td>
            </tr>
            <el-button size="small" type="primary" @click="addOrderDetailList"
              v-if="operationType !== 'view'">添加</el-button>
            <tr>
              <td>
                <p>序号</p>
              </td>
              <td>
                <p>样品编号</p>
              </td>
              <td>
                <p>试验项目</p>
              </td>
              <td>
                <p>检验依据</p>
              </td>
              <td>
                <p>备注</p>
              </td>
            </tr>
            <tr v-for="(item, index) in currentInfo.orderDetailList" :key="index">
              <td>{{ index + 1 }}</td>
              <td v-if="operationType !== 'view'"><el-input v-model="item.sampleNumber" clearable
                  size="small"></el-input>
              </td>
              <td v-if="operationType === 'view'">{{ item.sampleNumber }}</td>
              <td v-if="operationType !== 'view'"><el-input v-model="item.testItem" clearable size="small"></el-input>
              </td>
              <td v-if="operationType === 'view'">{{ item.testItem }}</td>
              <td v-if="operationType !== 'view'"><el-input v-model="item.testStandard" clearable
                  size="small"></el-input>
              </td>
              <td v-if="operationType === 'view'">{{ item.testStandard }}</td>
              <td v-if="operationType !== 'view'"><el-input v-model="item.remark" clearable size="small"></el-input>
              </td>
              <td v-if="operationType === 'view'">{{ item.remark }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>判定规则</p>
              </td>
              <td v-if="operationType !== 'view'" colspan="3">
                <el-radio-group v-model="currentInfo.criterionRule" v-removeAriaHidden>
                  <el-radio :label="0">不考虑不确定度</el-radio>
                  <el-radio :label="1">考虑不确定度</el-radio>
                </el-radio-group>
                <span v-if="currentInfo.criterionRule === 1"><el-input v-model="currentInfo.criterionRuleRemark"
                    clearable size="small" style="width: 60px"></el-input>%</span>
              </td>
              <td v-if="operationType === 'view'" colspan="3">
                <span v-if="currentInfo.criterionRule === 0">不考虑不确定度</span>
                <span v-if="currentInfo.criterionRule === 1">考虑不确定度</span>
                <span v-if="currentInfo.criterionRule === 1">{{ currentInfo.criterionRuleRemark + '%' }}</span>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <p>委托人签名</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.commissionUser" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">
                {{ currentInfo.commissionUser }}
              </td>
              <td>
                <p>委托日期</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-date-picker v-model="currentInfo.commissionDate" clearable format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.commissionDate }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>委托人联系电话</p>
              </td>
              <td v-if="operationType !== 'view'" colspan="3"><el-input v-model="currentInfo.commissionPhone" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'" colspan="3">{{ currentInfo.commissionPhone }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>综合室签名</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.generalOfficeUser" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.generalOfficeUser }}</td>
              <td>
                <p>接收日期</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-date-picker v-model="currentInfo.receiptData" clearable format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.receiptData }}</td>
            </tr>
            <tr>
              <td colspan="2">
                <p>领样员签名</p>
              </td>
              <td v-if="operationType !== 'view'"><el-input v-model="currentInfo.sampleTakerUser" clearable
                  size="small"></el-input></td>
              <td v-if="operationType === 'view'">{{ currentInfo.sampleTakerUser }}</td>
              <td>
                <p>领样日期</p>
              </td>
              <td v-if="operationType !== 'view'">
                <el-date-picker v-model="currentInfo.sampleData" clearable format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 100%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </td>
              <td v-if="operationType === 'view'">{{ currentInfo.sampleData }}</td>
            </tr>
            <tr>
              <td colspan="2" rowspan="3">
                <p>检测机构信息</p>
              </td>
              <td colspan="3">
                <p>江苏中天科技股份有限公司检测中心</p>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <p>地址：江苏省南通市经济技术开发区新开南路 19 号</p>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <p>电话：0513-89059043</p>
              </td>
            </tr>
          </table>
          <p style="margin-top: 10px;margin-left: 20px;">注：本检验委托单一式二份，一份综合室归档，一份委托单位留存。</p>
        </div>
      </div>
      <span v-if="operationType !== 'view'" slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">取 消</el-button>
        <el-button v-if="operationType === 'add'" :loading="buttonLoading" type="primary" @click="handleAdd">确
          定</el-button>
        <el-button v-if="operationType === 'edit'" :loading="buttonLoading" type="primary" @click="handleEdit">确
          定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="downloadDialog" title="下载" width="600px">
      <span>
        <el-button :loading="outLoading" plain type="primary" @click="orderReportDown">下载委托单</el-button>
        <el-button :loading="outLoading" plain type="primary" @click="orderFormDown">下载委托报告</el-button>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="downloadDialog = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addInspectionOrder, delInspectionOrder, exportInspectionOrder,
  getInsOrderOnInspection, getInspectionOrderByInsOderId, getInspectionOrderOne,
  pageInspectionOrder,
  updateInspectionOrder
} from "@/api/cnas/process/demand/demand";
import limsTable from "@/components/Table/lims-table.vue";
import { selectUserCondition } from "@/api/business/inspectionTask";

export default {
  name: "Demand",
  components: { limsTable },
  data() {
    return {
      title: '检验委托单',
      detailDialogVisible: false,
      addLoading: false,
      outLoading: false,
      currentInfo: {
        orderDetailList: []
      },
      personList: [],
      insStateList: [
        {
          label: '待审核',
          value: 0
        }, {
          label: '待检验',
          value: 1
        },
        {
          label: '已检验',
          value: 4
        },
        {
          label: '退回',
          value: 2
        },
        {
          label: '撤销',
          value: 3
        },
      ],
      operationType: '',
      orderRowsVisible: false,
      tableList: [],
      tableListLoading: false,
      pages: {
        current: 1,
        size: 20,
      },
      tableData: [],
      tableLoading: false,
      tableSearch: {
        current: 1,
        size: 20,
      },
      total: 0,
      total1: 0,
      entity: {
        entrustCode: ''
      },
      entitySearch: {
        sampleName: '',
        commissionUnit: '',
        production: '',
        commissionUser: '',
      },
      buttonLoading: false,
      downloadDialog: false,
      download: {},
    };
  },
  mounted() {
    this.getAuthorizedPerson()
    this.refreshTable()
  },
  computed: {
    action() {
      return this.javaApi + '/inspectionOrder/uploadInspectionOrderFile'
    }
  },
  methods: {
    addOrderDetailList() {
      if (this.currentInfo.orderDetailList == null) {
        this.currentInfo.orderDetailList = []
      }
      this.currentInfo.orderDetailList.push({
        sampleNumber: '',
        testItem: '',
        testStandard: '',
        standardMethodList: '',
        remark: '',
      })
    },
    // 查询列表
    refreshTable() {
      this.tableListLoading = true
      pageInspectionOrder({
        ...this.pages,
        ...this.entitySearch
      }).then(res => {
        this.tableListLoading = false
        this.tableList = res.data.records
        this.total1 = res.data.total
      }).catch(err => {
        this.tableListLoading = false
      })
    },
    // 重置
    refresh() {
      this.entitySearch = {
        sampleName: '',
        commissionUnit: '',
        production: '',
        commissionUser: '',
      }
      this.pages = {
        current: 1,
        size: 20,
      }
      this.refreshTable()
    },
    // 打开新增弹框
    goAdd() {
      this.operationType = 'add'
      this.title = '新增检验委托单';
      this.orderRowsVisible = true
      this.searchTableData()
    },
    // 查询可新增的检验单
    searchTableData() {
      this.tableLoading = true
      getInsOrderOnInspection({
        ...this.tableSearch,
        ...this.entity
      }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.total = res.data.total
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置检验单列表
    refreshTableData() {
      this.entity = {
        entrustCode: ''
      }
      this.tableSearch = {
        current: 1,
        size: 20,
      }
      this.searchTableData()
    },
    // 检验单列表分页
    handleSizeChange(val) {
      this.tableSearch.size = val
      this.searchTableData();
    },
    handleCurrentChange(val) {
      this.tableSearch.current = val
      this.searchTableData();
    },
    // 列表分页
    handleSizeChange1(val) {
      this.pages.size = val
      this.refreshTable();
    },
    handleCurrentChange1(val) {
      this.pages.current = val
      this.refreshTable();
    },
    // 提交新增
    handleAdd() {
      this.buttonLoading = true
      addInspectionOrder(this.currentInfo).then(res => {
        this.buttonLoading = false
        this.$message.success('新增成功')
        this.detailDialogVisible = false
        this.orderRowsVisible = false
        this.refreshTable()
      }).catch(err => {
        this.buttonLoading = false
      })
    },
    // 提交修改
    handleEdit() {
      this.buttonLoading = true
      updateInspectionOrder(this.currentInfo).then(res => {
        this.buttonLoading = false
        this.$message.success('新增成功')
        this.detailDialogVisible = false
        this.orderRowsVisible = false
        this.refreshTable()
      }).catch(err => {
        this.buttonLoading = false
      })
    },
    // 查询需要新增的委托单详情
    goAddOrder(row) {
      getInspectionOrderByInsOderId({ insOrderId: row.id }).then(res => {
        this.currentInfo = res.data
        this.title = '新增检验委托单';
        this.detailDialogVisible = true
      }).catch(err => {
        console.log('err----', err)
      })
    },
    // 打开编辑弹框
    goUpdate(row) {
      this.operationType = 'edit'
      getInspectionOrderOne({ inspectionOrderId: row.inspectionOrderId }).then(res => {
        this.currentInfo = res.data
        this.title = '编辑检验委托单';
        this.detailDialogVisible = true
      }).catch(err => {
        console.log('err----', err)
      })
    },
    // 查看
    handleLook(row) {
      this.operationType = 'view'
      getInspectionOrderOne({ inspectionOrderId: row.inspectionOrderId }).then(res => {
        this.currentInfo = res.data
        this.title = '查看检验委托单';
        this.detailDialogVisible = true
      }).catch(err => {
        console.log('err----', err)
      })
    },
    // 删除
    deleteOrder(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delInspectionOrder({ inspectionOrderId: row.inspectionOrderId }).then(res => {
          this.$message.success('删除成功！');
          this.refreshTable();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 打开下载弹框
    openDownloadDia(row) {
      this.downloadDialog = true
      this.download = row
    },
    // 委托单下载
    orderFormDown() {
      let url = this.download.fileUrl;
      const link = document.createElement('a');
      link.href = this.javaApi + url;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
    },
    // 委托报告下载
    orderReportDown() {
      this.outLoading = true;
      exportInspectionOrder({ inspectionOrderId: this.download.inspectionOrderId }).then(res => {
        this.outLoading = false;
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '检验委托单.docx')

      }).catch(err => {
        this.outLoading = false;
      })
    },
    // 上传
    handleSuccessUp(response,) {
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.refreshTable()
      }
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    // 人员列表
    getAuthorizedPerson() {
      selectUserCondition().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.personList = data
      })
    },

  },
}
</script>

<style scoped></style>
