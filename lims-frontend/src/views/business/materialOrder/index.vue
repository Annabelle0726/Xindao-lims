<template>
  <div class="app-container">
    <div>
      <div class="search">
        <el-form :model="entity" ref="entity" size="small" :inline="true">
          <el-form-item label="批号" prop="updateBatchNo">
            <el-input v-model="entity.updateBatchNo" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item label="委托编号" prop="entrustCode" v-if="tabIndex !== 0">
            <el-input v-model="entity.entrustCode" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item label="零件号" prop="partNo">
            <el-input v-model="entity.partNo" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item label="零件描述" prop="partDesc">
            <el-input v-model="entity.partDesc" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button v-if="tabIndex === 2 || tabIndex === 3 || tabIndex === 4"
              :icon="!more ? 'el-icon-arrow-down' : 'el-icon-arrow-up'" style="color: #3A7BFA;" type="text"
              @click="more = !more">{{ !more ? '更多' : '收起' }}</el-button>
            <el-button size="mini" type="primary" @click="goSearch">查询</el-button>
            <el-button size="mini" @click="refresh()">重置</el-button>
          </el-form-item>
          <el-form-item label="供应商名称" prop="supplierName"
            v-if="(tabIndex === 2 || tabIndex === 3 || tabIndex === 4) && more">
            <el-input v-model="entity.supplierName" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item label="样品型号" prop="sampleModel"
            v-if="(tabIndex === 2 || tabIndex === 3 || tabIndex === 4) && more">
            <el-input v-model="entity.sampleModel" clearable placeholder="请输入" size="small"
              @keyup.enter.native="goSearch">
            </el-input>
          </el-form-item>
          <el-form-item label="检验状态" prop="inspectStatus"
            v-if="(tabIndex === 2 || tabIndex === 3 || tabIndex === 4) && more">
            <el-select v-model="entity.inspectStatus" clearable size="small" @change="goSearch">
              <el-option v-for="(a, i) in inspectStatusList" :key="i" :label="a.label" :value="a.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="下发时间" prop="date" v-if="(tabIndex === 2 || tabIndex === 3 || tabIndex === 4) && more">
            <el-date-picker v-model="entity.date" end-placeholder="结束日期" format="yyyy-MM-dd" placeholder="选择日期" @change="goSearch"
              range-separator="至" size="small" start-placeholder="开始日期" type="daterange" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>
      <div class="table">
        <div class="table-tab">
          <div>
            <ul class="tab">
              <li :class="{ active: tabIndex === 0 }" v-if="checkPermi(['get:raw:await'])" @click="handleTab(0)">待下单</li>
              <li :class="{ active: tabIndex === 1 }" v-if="checkPermi(['get:raw:testing'])" @click="handleTab(1)">检验中</li>
              <li :class="{ active: tabIndex === 2 }" v-if="checkPermi(['get:raw:check'])" @click="handleTab(2)">已检验</li>
              <li :class="{ active: tabIndex === 4 }" v-if="checkPermi(['get:raw:quarterCheck'])" @click="handleTab(4)">季度检验</li>
              <li :class="{ active: tabIndex === 3 }" v-if="checkPermi(['get:Ifs:ByAll'])" @click="handleTab(3)">全部</li>
            </ul>
          </div>
          <div>
            <el-button v-show="tabIndex === 3 || tabIndex === 2" :loading="outLoading" size="small" type="primary"
              @click="handleOut">导出</el-button>
            <el-button v-if="tabIndex === 0" size="small" type="primary" @click="copper">铜材料下单</el-button>
            <el-button v-if="tabIndex !== 0" size="small" type="primary" @click="openPrint">标签打印</el-button>
          </div>
        </div>
        <!--待下单-->
        <div class="table">
          <lims-table :tableData="tableData" :column="column" v-if="tabIndex === 0" @pagination="pagination"
                      ref="tableData" :rowClassName="changeRowClass" :height="'calc(100vh - 290px)'"
                      key="tableData" :page="page" :tableLoading="tableLoading"></lims-table>
        </div>
        <!--检验中-->
        <div class="table">
          <lims-table :tableData="tableData1" :column="column1" v-if="tabIndex === 1" :isSelection="true"
            :rowClassName="changeRowClass" :handleSelectionChange="selectMethod" @pagination="pagination1"
            :height="'calc(100vh - 290px)'" key="tableData1" :page="page1" :tableLoading="tableLoading1"></lims-table>
        </div>
        <!--已检验-->
        <div class="table">
          <lims-table :tableData="tableData2" :column="column2" v-if="tabIndex === 2" :isSelection="true"
            :rowClassName="changeRowClass" :handleSelectionChange="selectMethod" @pagination="pagination2"
            :height="'calc(100vh - 290px)'" key="tableData2" :page="page2" :tableLoading="tableLoading2"></lims-table>
        </div>
        <!--全部-->
        <div class="table">
          <lims-table :tableData="tableData3" :column="column3" v-if="tabIndex === 3" :isSelection="true"
            :rowClassName="changeRowClass" :handleSelectionChange="selectMethod" @pagination="pagination3"
            :height="'calc(100vh - 290px)'" key="tableData3" :page="page3" :tableLoading="tableLoading3"></lims-table>
        </div>
        <!--季度检验-->
        <div class="table">
          <lims-table :tableData="tableData4" :column="column4" v-if="tabIndex === 4" :isSelection="true"
            :rowClassName="changeRowClass" :handleSelectionChange="selectMethod" @pagination="pagination4"
            :height="'calc(100vh - 290px)'" key="tableData4" :page="page4" :tableLoading="tableLoading4"></lims-table>
        </div>
      </div>
    </div>
    <!-- 确认免检弹框 -->
    <el-dialog :visible.sync="exemptionVisible" title="确认免检" width="42%">
      <div style="display: flex">
        <span style="width: 90px; line-height: 32px">规格型号：</span>
        <el-input v-model="exemptionInfo.partDetail" clearable placeholder="请输入" size="small"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="exemptionVisible = false">取 消</el-button>
          <el-button :loading="exemptionLoading" type="primary" @click="submitExemption">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 撤销报检 -->
    <el-dialog :visible.sync="declareDialogVisible" title="报检撤销" width="30%">
      <p style="font-size:16px;color:#333333">批号<span style="color:#34BD66">{{ this.insOrderRow.updateBatchNo
          }}</span>的信息是否<span style="color: #FF4902">撤销报检</span>
      </p>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="declareDialogVisible = false">取 消</el-button>
          <el-button :loading="upLoad" type="primary" @click="submitDeclare">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 撤销下单 -->
    <el-dialog :visible.sync="quashDialogVisible" title="下单撤销" width="30%">
      <el-button size="small" type="primary" @click="cancelQuashOrder('enterOrderId')">撤销进厂检验下单</el-button>
      <el-button size="small" type="primary" @click="cancelQuashOrder('quarterOrderId')">撤销季度检验下单</el-button>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="quashDialogVisible = false">取 消</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 修改委托编号弹框 -->
    <el-dialog :visible.sync="entrustCodeVisible" title="提示" width="30%">
      <el-input v-model="entrustCodeInfo.entrustCode"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="entrustCodeVisible = false">取 消</el-button>
        <el-button :loading="submitCodeLoading" type="primary" @click="submitCode">确 定</el-button>
      </span>
    </el-dialog>
    <!--标签打印弹框-->
    <print-dialog v-if="printDialog" ref="printDialog" :printDialog="printDialog"
      @closePrintDialog="closePrintDialog"></print-dialog>
    <!--数据查看弹框-->
    <data-look-visible v-if="dataDialogVisible" ref="dataDialogVisible" :dataDialogVisible="dataDialogVisible"
      :dataLookInfo="dataLookInfo" @closeDataLook="closeDataLook"></data-look-visible>
    <!--附件查看弹框-->
    <files-look-visible v-if="filesDialogVisible" ref="filesDialogVisible" :filesDialogVisible="filesDialogVisible"
      :filesLookInfo="filesLookInfo" @closeFilesLook="closeFilesLook"></files-look-visible>
    <!--报告下载弹框-->
    <down-file-dialog v-if="downFileDialogVisible" ref="downFileDialogVisible"
      :downFileDialogVisible="downFileDialogVisible" :downLoadInfo="downLoadInfo"
      @closeDownFileDialog="closeDownFileDialog"></down-file-dialog>
    <!--产业链信息查看-->
    <ShowInfo v-if="showInfoDialog" ref="showInfoDialog" :showInfoDialog="showInfoDialog"></ShowInfo>
    <!--检验任务信息查看-->
    <el-dialog :visible.sync="InspectInfoDialog" title="数据查看" width="400px" @closed="closeInsInfoDialog">
      <div style="margin-bottom: 8px">
        <span style="font-size: 16px;">进厂检验原始数据</span>
        <el-link :disabled="!insInfo.enterOrderId" :underline="false" style="vertical-align: bottom;margin-left: 6px"
          type="primary" @click="viewInsInfo0">查看</el-link>
      </div>
      <div>
        <span style="font-size: 16px;">季度检验原始数据</span>
        <el-link :disabled="!insInfo.quarterOrderId" :underline="false" style="vertical-align: bottom;margin-left: 6px"
          type="primary" @click="viewInsInfo1">查看</el-link>
      </div>
    </el-dialog>
    <!--    <Inspection v-if="state>0" :key="InspectionKey" :inspectorList="inspectorList" :orderId="orderId"-->
    <!--                :sonLaboratory="'原材料'" :state="state"-->
    <!--                :typeSource="typeSource"-->
    <!--                @goback="goback" @refreshView="refreshView"/>-->
  </div>
</template>

<script>
import PrintDialog from "@/views/business/materialOrderComponents/materialOrder/printDialog.vue";
import ShowInfo from "@/views/business/materialOrderComponents/materialOrder/showInfo.vue";
import DataLookVisible from "@/views/business/materialOrderComponents/materialOrder/dataLookVisible.vue";
import FilesLookVisible from "@/views/business/materialOrderComponents/materialOrder/filesLookVisible.vue";
import DownFileDialog from "@/views/business/materialOrderComponents/materialOrder/downFileDialog.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {
  concessionRelease,
  getIfsByAll,
  getIfsByOver,
  getIfsByQuarter,
  getIfsByStateOne,
  rawAllExport,
  rawOrderRelease,
  repealEnterRawOrder,
  repealQuarterRawOrder,
  revokeInspectionReport,
  updateEntrustCode
} from "@/api/business/rawMaterialOrder";
import { getWarehouseSubmit } from "@/api/business/materialInspection";
import {mapGetters} from "vuex";
// import Inspection from "../do/b1-inspect-order-plan/Inspection.vue";

export default {
  name: "MaterialOrder",
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, DownFileDialog, FilesLookVisible, DataLookVisible, ShowInfo, PrintDialog },
  data() {
    // 这里存放数据
    return {
      tableData: [],
      tableLoading: false,
      column: [
        { label: '批号', prop: 'updateBatchNo' },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        {
          dataType: 'tag',
          label: '物料类型',
          prop: 'isExpire',
          formatData: (params) => {
            if (params == 1) {
              return '过期物料'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'info'
            } else {
              return null
            }
          }
        },
        { label: '抵达的采购数量', prop: 'qtyArrived' },
        { label: '单位', prop: 'buyUnitMeas' },
        { label: '订单号', prop: 'orderNo' },
        { label: '接收时间', prop: 'receiverDate' },
        { label: '报检时间', prop: 'declareDate' },
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '下单',
              type: 'text',
              clickFun: (row) => {
                this.playOrder(row);
              }
            },
            {
              name: '免检',
              type: 'text',
              clickFun: (row) => {
                this.exemption(row);
              },
            },
            {
              name: '撤销报检',
              type: 'text',
              clickFun: (row) => {
                this.cancelDeclare(row);
              },
            },
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
        { label: '批号', prop: 'updateBatchNo' },
        {
          label: '委托编号',
          prop: 'entrustCode',
          width: "160px",
          dataType: "link",
          linkMethod: "changeEntrustCode",
        },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        {
          label: '样品名称',
          prop: 'sampleName',
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        { label: '样品型号', prop: 'sampleModel' },
        { label: '检验人', prop: 'userName' },
        { label: '下发时间', prop: 'sendTime' },
        {
          dataType: 'tag',
          label: '物料类型',
          prop: 'isExpire',
          formatData: (params) => {
            if (params == 1) {
              return '过期物料'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'info'
            } else {
              return null
            }
          }
        },
        { label: '抵达的采购数量', prop: 'qtyArrived' },
        { label: '单位', prop: 'buyUnitMeas' },
        { label: '订单号', prop: 'orderNo' },
        { label: '接收时间', prop: 'receiverDate' },
        { label: '报检时间', prop: 'declareDate' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              },
            },
            {
              name: '附件查看',
              type: 'text',
              clickFun: (row) => {
                this.handleFileLook(row);
              },
            },
            {
              name: '撤销下单',
              type: 'text',
              clickFun: (row) => {
                this.cancelOrder(row);
              },
            },
          ]
        }
      ],
      page1: {
        total: 0,
        size: 20,
        current: 1
      },
      tableData2: [],
      tableLoading2: false,
      column2: [
        {
          label: '委托编号',
          prop: 'entrustCode',
          width: "160px",
          dataType: "link",
          linkMethod: "changeEntrustCode",
        },
        {
          dataType: 'tag',
          label: '检验状态',
          prop: 'inspectStatus',
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
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'warning'
            } else if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'danger'
            } else if (params == 3) {
              return 'info'
            } else if (params == 4) {
              return ''
            } else {
              return null
            }
          }
        },
        { label: '订单号', prop: 'orderNo' },
        { label: '抵达的采购数量', prop: 'qtyArrived' },
        { label: '下发时间', prop: 'sendTime' },
        { label: '批号', prop: 'updateBatchNo' },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        { label: '供应商名称', prop: 'supplierName' },
        { label: '不合格描述', prop: 'unqualifiedDesc' },
        {
          dataType: 'tag',
          label: '免检',
          prop: 'isExemption',
          formatData: (params) => {
            if (params == 1) {
              return '免检'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else {
              return null
            }
          }
        },
        {
          label: '样品名称',
          prop: 'sampleName',
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        { label: '样品型号', prop: 'sampleModel' },
        { label: '检验人', prop: 'userName' },
        {
          dataType: 'tag',
          label: '物料类型',
          prop: 'isExpire',
          formatData: (params) => {
            if (params == 1) {
              return '过期物料'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'info'
            } else {
              return null
            }
          }
        },
        { label: '单位', prop: 'buyUnitMeas' },
        { label: '接收时间', prop: 'receiverDate' },
        { label: '报检时间', prop: 'declareDate' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '季度检验',
              type: 'text',
              clickFun: (row) => {
                this.playOrderSec(row);
              },
              disabled: (row) => {
                return row.isQuarter == 0
              },
              showHide: (row) => {
                return this.checkPermi([
                  "get:raw:check:operation",
                ]);
              },
            },
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              }
            },
            {
              name: '附件查看',
              type: 'text',
              clickFun: (row) => {
                this.handleFileLook(row);
              }
            },
            {
              name: '报告下载',
              type: 'text',
              clickFun: (row) => {
                this.download(row);
              }
            },
            {
              name: '原始记录',
              type: 'text',
              clickFun: (row) => {
                this.viewInspectInfo(row);
              },
              disabled: (row) => {
                return row.sampleName === null
              },
            },
            {
              name: '放行',
              type: 'text',
              clickFun: (row) => {
                this.goPass(row);
              },
              disabled: (row) => {
                return row.inspectStatus != 2
              },
              showHide: (row) => {
                return this.checkPermi([
                  "get:raw:check:operation",
                ]);
              },
            },
            {
              name: '季度撤销',
              type: 'text',
              clickFun: (row) => {
                this.repealQuarter(row);
              },
              disabled: (row) => {
                return row.quarterOrderId == null || row.quarterReportId != null
              },
              showHide: (row) => {
                return this.checkPermi([
                  "get:raw:check:operation",
                ]);
              },
            },
          ]
        }
      ],
      page2: {
        total: 0,
        size: 20,
        current: 1
      },
      tableData3: [],
      tableLoading3: false,
      column3: [
        { label: '委托编号', prop: 'entrustCode',width: "160px", },
        {
          dataType: 'tag',
          label: '检验状态',
          prop: 'inspectStatus',
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
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'warning'
            } else if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'danger'
            } else if (params == 3) {
              return 'info'
            } else if (params == 4) {
              return ''
            } else {
              return null
            }
          }
        },
        { label: '订单号', prop: 'orderNo' },
        { label: '抵达的采购数量', prop: 'qtyArrived' },
        { label: '下发时间', prop: 'sendTime' },
        { label: '批号', prop: 'updateBatchNo' },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        { label: '供应商名称', prop: 'supplierName' },
        { label: '不合格描述', prop: 'unqualifiedDesc' },
        {
          dataType: 'tag',
          label: '免检',
          prop: 'isExemption',
          formatData: (params) => {
            if (params == 1) {
              return '免检'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else {
              return null
            }
          }
        },
        {
          label: '样品名称',
          prop: 'sampleName',
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        { label: '样品型号', prop: 'sampleModel' },
        { label: '检验人', prop: 'userName' },
        {
          dataType: 'tag',
          label: '物料类型',
          prop: 'isExpire',
          formatData: (params) => {
            if (params == 1) {
              return '过期物料'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'info'
            } else {
              return null
            }
          }
        },
        { label: '单位', prop: 'buyUnitMeas' },
        { label: '接收时间', prop: 'receiverDate' },
        { label: '报检时间', prop: 'declareDate' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              }
            },
            {
              name: '附件查看',
              type: 'text',
              clickFun: (row) => {
                this.handleFileLook(row);
              },
            }
          ]
        }
      ],
      page3: {
        total: 0,
        size: 20,
        current: 1
      },
      tableData4: [],
      tableLoading4: false,
      column4: [
        { label: '委托编号', prop: 'entrustCode',width: "160px", },
        {
          dataType: 'tag',
          label: '检验状态',
          prop: 'inspectStatus',
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
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'warning'
            } else if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'danger'
            } else if (params == 3) {
              return 'info'
            } else if (params == 4) {
              return ''
            } else {
              return null
            }
          }
        },
        { label: '订单号', prop: 'orderNo' },
        { label: '抵达的采购数量', prop: 'qtyArrived' },
        { label: '下发时间', prop: 'sendTime' },
        { label: '批号', prop: 'updateBatchNo' },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        { label: '供应商名称', prop: 'supplierName' },
        { label: '不合格描述', prop: 'unqualifiedDesc' },
        {
          dataType: 'tag',
          label: '免检',
          prop: 'isExemption',
          formatData: (params) => {
            if (params == 1) {
              return '免检'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else {
              return null
            }
          }
        },
        {
          label: '样品名称',
          prop: 'sampleName',
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        { label: '样品型号', prop: 'sampleModel' },
        { label: '检验人', prop: 'userName' },
        {
          dataType: 'tag',
          label: '物料类型',
          prop: 'isExpire',
          formatData: (params) => {
            if (params == 1) {
              return '过期物料'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'info'
            } else {
              return null
            }
          }
        },
        { label: '单位', prop: 'buyUnitMeas' },
        { label: '接收时间', prop: 'receiverDate' },
        { label: '报检时间', prop: 'declareDate' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '季度检验',
              type: 'text',
              clickFun: (row) => {
                this.playOrderSec(row);
              },
              disabled: (row, index) => {
                return row.isQuarter == 0
              }
            },
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              }
            },
            {
              name: '附件查看',
              type: 'text',
              clickFun: (row) => {
                this.handleFileLook(row);
              },
            },
            {
              name: '报告下载',
              type: 'text',
              clickFun: (row) => {
                this.download(row);
              },
            }
          ]
        }
      ],
      page4: {
        total: 0,
        size: 20,
        current: 1
      },
      entity: {
        updateBatchNo: null,
        entrustCode: null,
        partDesc: null,
        supplierName: null,
        sampleModel: null,
        partNo: null,
        inspectStatus: null,
        date: null,
        beginDeclareDate: null,
        endDeclareDate: null,
      },
      tabList: [
        {
          label: '待下单',
          value: 0
        },
        {
          label: '检验中',
          value: 1
        },
        {
          label: '已检验',
          value: 2
        },
        {
          label: '季度检验',
          value: 4
        },
        {
          label: '全部',
          value: 3
        }
      ],
      more: false,
      tabIndex: '',
      multipleSelection: [],
      active: 0, //1：下单，2：查看
      orderType: 0, //0：原材料下单，1：季度检验下单
      currentId: null,
      btnLoading: false,
      quashDialogVisible: false, // 撤销下单提醒弹框
      declareDialogVisible: false, // 撤销报检提醒弹框
      insOrderRow: {},
      upLoad: false,
      filesDialogVisible: false, // 附件查看弹框
      printDialog: false, // 标签打印弹框
      showInfoDialog: false, // 产业链信息查看
      dataDialogVisible: false, // 数据查看弹框
      dataLookInfo: {}, // 数据查看弹框数据
      filesLookInfo: {}, // 附件查看弹框数据
      downFileDialogVisible: false, // 报告下载弹框
      downLoadInfo: {}, // 报告下载弹框
      entrustCodeVisible: false, // 修改委托编号弹框
      entrustCodeInfo: {},
      submitCodeLoading: false,
      exemptionVisible: false, // 免检确认弹框
      exemptionLoading: false,
      exemptionInfo: {},
      inspectStatusList: [
        { label: '检验中', value: 0 },
        { label: '合格', value: 1 },
        { label: '不合格', value: 2 },
        { label: '未下单', value: 3 },
        { label: '让步放行', value: 4 },
      ],
      state: 0,
      orderId: 0,
      inspectorList: [],//检验人员列表
      InspectionKey: 1,
      typeSource: null,// 0:成品下单，1：原材料下单, 2: 铜单丝下单
      InspectInfoDialog: false, // 数据查看弹框
      insInfo: {},
      outLoading: false
    }
  },
  computed: {
    ...mapGetters(['nickName'])
  },
  mounted() {
    if (this.checkPermi(['get:raw:await'])) {
      this.tabIndex = 0
    } else {
      this.tabIndex = 2
    }
    this.refreshTable()
  },
  activated () {
    this.refreshTable()
  },
  // 方法集合
  methods: {
    // 点击查询回调
    goSearch() {
      this.page.current = 1
      this.page1.current = 1
      this.page2.current = 1
      this.page3.current = 1
      this.page4.current = 1
      this.refreshTable()
    },
    // 查询回调
    refreshTable() {
      if (this.tabIndex === 0) {
        // 待下单查询
        this.getPurchaseOrderList()
      } else if (this.tabIndex === 1) {
        // 检验中查询
        this.getIfsByStateOneList()
      } else if (this.tabIndex === 2) {
        // 已检验查询
        this.getIfsByOverList()
      } else if (this.tabIndex === 4) {
        // 查询季度检验
        this.getIfsByQuarterList()
      } else {
        // 全部查询
        this.getIfsByAllList()
      }
    },
    // 待下单查询
    getPurchaseOrderList() {
      this.tableLoading = true
      const params = { ...this.entity, isInspect: 1, state: 0, ...this.page }
      getWarehouseSubmit(params).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 检验中查询
    getIfsByStateOneList() {
      this.tableLoading1 = true
      const params = { ...this.entity, orderState: 1, state: 1, ...this.page1 }
      getIfsByStateOne(params).then(res => {
        this.tableLoading1 = false
        if (res.code === 200) {
          this.tableData1 = res.data.records
          this.page1.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 已检验查询
    getIfsByOverList() {
      this.tableLoading2 = true
      if(null != this.entity.date){
        this.entity.beginDeclareDate = this.entity.date[0]
        this.entity.endDeclareDate = this.entity.date[1]
      } else {
        this.entity.beginDeclareDate = ''
        this.entity.endDeclareDate = ''
      }
      const params = { ...this.entity, orderState: 4, state: 2, ...this.page2 }
      getIfsByOver(params).then(res => {
        this.tableLoading2 = false
        if (res.code === 200) {
          this.tableData2 = res.data.records
          this.page2.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading2 = false
      })
    },
    // 查询季度检验
    getIfsByQuarterList() {
      this.tableLoading4 = true
      if(null != this.entity.date){
        this.entity.beginDeclareDate = this.entity.date[0]
        this.entity.endDeclareDate = this.entity.date[1]
      } else {
        this.entity.beginDeclareDate = ''
        this.entity.endDeclareDate = ''
      }
      const params = { ...this.entity, ...this.page4 }
      getIfsByQuarter(params).then(res => {
        this.tableLoading4 = false
        if (res.code === 200) {
          this.tableData4 = res.data.records
          this.page4.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading4 = false
      })
    },
    // 全部
    getIfsByAllList() {
      this.tableLoading3 = true
      if(null != this.entity.date){
        this.entity.beginDeclareDate = this.entity.date[0]
        this.entity.endDeclareDate = this.entity.date[1]
      } else {
        this.entity.beginDeclareDate = ''
        this.entity.endDeclareDate = ''
      }
      const params = { ...this.entity, isInspect: 1, ...this.page3 }
      getIfsByAll(params).then(res => {
        this.tableLoading3 = false
        if (res.code === 200) {
          this.tableData3 = res.data.records
          this.page3.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading3 = false
      })
    },
    // 重 置
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    pagination1(page) {
      this.page1.size = page.limit
      this.refreshTable()
    },
    pagination2(page) {
      this.page2.size = page.limit
      this.refreshTable()
    },
    pagination3(page) {
      this.page3.size = page.limit
      this.refreshTable()
    },
    pagination4(page) {
      this.page4.size = page.limit
      this.refreshTable()
    },
    // 查看检验数据
    viewInspectInfo(row) {
      //当前检验任务的检验人列表
      let inspectorList = []
      if (row.userName) {
        inspectorList = row.userName.split(',')
      }
      inspectorList.push(this.nickName)
      this.inspectorList = inspectorList
      this.insInfo = row
      this.InspectInfoDialog = true
    },
    closeInsInfoDialog() {
      this.InspectInfoDialog = false
    },
    // 直接放行
    goPass(row) {
      this.$confirm('是否放行当前数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        concessionRelease({ ifsInventoryId: row.id }).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '放行成功!'
            });
            this.refresh()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '放行失败'
        });
      });
    },
    // 季度撤销
    repealQuarter(row) {
      this.$confirm('是否撤销季度下单?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        repealQuarterRawOrder({ quarterOrderId: row.quarterOrderId }).then(res => {
          if (res.code === 200) {
            this.$message.success('撤销成功')
            this.refreshTable('page')
          }
        })
      }).catch(() => { })
    },
    viewInsInfo0() {
      let inspectorList = []
      inspectorList.push(this.nickName)
      this.InspectInfoDialog = false
      if (this.checkPermi(['business:inspectionView'])) {
        this.$router.push({
          name: "InspectionView",
          query: {
            sonLaboratory: '原材料',
            state: 3,
            typeSource: this.insInfo.typeSource,
            orderId: this.insInfo.enterOrderId,
            inspectorList: inspectorList,
          },
        })
      } else {
        this.$router.push({
          path: "/inspectionTask/inspection",
          query: {
            sonLaboratory: '原材料',
            state: 3,
            typeSource: this.insInfo.typeSource,
            orderId: this.insInfo.enterOrderId,
            inspectorList: inspectorList,
          },
        })
      }
    },
    viewInsInfo1() {
      let inspectorList = []
      inspectorList.push(this.nickName)
      this.InspectInfoDialog = false
      if (this.checkPermi(['business:inspectionView'])) {
        this.$router.push({
          name: "InspectionView",
          query: {
            sonLaboratory: '原材料',
            state: 3,
            typeSource: this.insInfo.typeSource,
            orderId: this.insInfo.quarterOrderId,
            inspectorList: inspectorList,
          },
        })
      } else {
        this.$router.push({
          path: "/inspectionTask/inspection",
          query: {
            sonLaboratory: '原材料',
            state: 3,
            typeSource: this.insInfo.typeSource,
            orderId: this.insInfo.quarterOrderId,
            inspectorList: inspectorList,
          },
        })
      }
    },
    goback() {
      this.state = 0
      this.refreshTable('page')
    },
    // 刷新页面
    refreshView() {
      this.InspectionKey++
    },
    // 铜材料下单
    copper() {
      this.$router.push({ path: "/materialOrder/copperOrder", query: { active: 1 } });
    },
    // 打开标签打印弹框
    openPrint() {
      if (this.multipleSelection.length > 0) {
        this.printDialog = true
        this.$nextTick(() => {
          let selection = this.multipleSelection
          this.$refs.printDialog.getLabelPrinting(selection)
        })
      } else {
        this.$message.error('请选择需要打印的数据')
      }
    },
    // 关闭标签打印弹框
    closePrintDialog() {
      this.printDialog = false
    },
    // 下单
    playOrder(row) {
      this.$router.push({ path: "/materialOrder/customsInspectionOrder", query: { orderType: 0, customsInspection: row, active: 1 } });
    },
    // 季度检验下单
    playOrderSec(row) {
      this.$router.push({ path: "/materialOrder/customsInspectionOrder", query: { orderType: 1, customsInspection: row, active: 1 } });
    },
    // 点击样品名称查看详情
    selectAllByOne(row) {
      if (row.isCopper == 1) {
        this.currentId = row.enterOrderId
        this.$router.push({ path: "/materialOrder/CopperView", query: { customsInspection: row, active: 2, currentId: this.currentId } });
      } else {
        this.currentId = row.insOrderId
        this.$router.push({ path: "/materialOrder/customsInspectionView", query: { customsInspection: row, active: 2, currentId: this.currentId } });
      }
    },
    // 打开免检弹框
    exemption(row) {
      this.exemptionVisible = true
      this.exemptionInfo = row
      this.$set(this.exemptionInfo, 'partDetail', row.partDesc)
    },
    // 提交免检信息
    submitExemption() {
      this.exemptionLoading = true
      rawOrderRelease({
        ifsInventoryId: this.exemptionInfo.id,
        partDetail: this.exemptionInfo.partDetail
      }).then(res => {
        if (res.code === 200) {
          this.exemptionVisible = false
          this.$message.success('操作成功')
          this.refresh()
        }
        this.exemptionLoading = false
      }).catch(err => {
        console.log(err)
        this.exemptionLoading = false
      })
    },
    // 打开撤销报检弹框
    cancelDeclare(row) {
      this.declareDialogVisible = true
      this.insOrderRow = row
    },
    // 提交撤销报检申请
    submitDeclare() {
      revokeInspectionReport({ id: this.insOrderRow.id }).then(res => {
        if (res.code === 200) {
          this.declareDialogVisible = false
          this.refreshTable()
          this.$message.success("撤销报检成功")
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 打开撤销下单的弹框
    cancelOrder(row) {
      if (row.enterOrderId && row.quarterOrderId) {
        this.quashDialogVisible = true
      } else if (row.enterOrderId && !row.quarterOrderId) {
        this.$confirm('是否撤销进厂下单?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          repealEnterRawOrder({ enterOrderId: row.enterOrderId }).then(res => {
            if (res.code === 200) {
              this.$message.success('撤销成功')
              this.refreshTable('page')
            }
          })
        }).catch(() => { })
      } else if (!row.enterOrderId && row.quarterOrderId) {
        this.$confirm('是否撤销季度下单?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          repealQuarterRawOrder({ quarterOrderId: row.quarterOrderId }).then(res => {
            if (res.code === 200) {
              this.$message.success('撤销成功')
              this.refreshTable('page')
            }
          })
        }).catch(() => { })
      }
      this.insOrderRow = row
    },
    cancelQuashOrder(type) {
      if (type === 'enterOrderId') {
        this.$confirm('是否撤销当前数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          repealEnterRawOrder({ enterOrderId: this.insOrderRow.enterOrderId }).then(res => {
            if (res.code === 200) {
              this.$message.success('撤销成功')
              this.refreshTable('page')
            }
          })
        }).catch(() => { })
      } else {
        this.$confirm('是否撤销当前数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          repealQuarterRawOrder({ quarterOrderId: this.insOrderRow.quarterOrderId }).then(res => {
            if (res.code === 200) {
              this.$message.success('撤销成功')
              this.refreshTable()
            }
          })
        }).catch(() => { })
      }
    },
    // 数据查看
    handleDataLook(row) {
      this.dataLookInfo = row
      this.dataDialogVisible = true;
    },
    // 关闭数据查看弹框
    closeDataLook() {
      this.dataDialogVisible = false
    },
    // 附件查看
    handleFileLook(row) {
      this.filesDialogVisible = true
      this.filesLookInfo = row
    },
    // 关闭附件查看弹框
    closeFilesLook() {
      this.filesDialogVisible = false
    },
    // 报告下载
    download(row) {
      this.downFileDialogVisible = true
      this.downLoadInfo = row
    },
    // 关闭报告下载弹框
    closeDownFileDialog() {
      this.downFileDialogVisible = false
    },
    // 查看产业链信息
    openInfoDialog(row) {
      this.showInfoDialog = true
      this.$nextTick(() => {
        this.$refs.showInfoDialog.getInfo(row.id)
      })
    },
    // 修改委托编号
    changeEntrustCode(row) {
      this.entrustCodeVisible = true
      this.entrustCodeInfo = { ...row }
    },
    // 导出
    handleOut() {
      let entity = this.tabIndex === 3 ? { ...this.entity, isInspect: 2 } : { ...this.entity, state: 2, orderState: 4, }
      delete entity.orderBy
      this.outLoading = true
      rawAllExport({ entity: entity }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '原材料检测信息导出.xlsx');
      })
    },
    // 提交修改委托编号信息
    submitCode() {
      this.submitCodeLoading = true
      try {
        updateEntrustCode({
          id: this.entrustCodeInfo.id,
          entrustCode: this.entrustCodeInfo.entrustCode,
        }).then(res => {
          if (res.code === 200) {
            this.entrustCodeVisible = false
            this.$message.success('修改成功')
          }
          this.refreshTable()
          this.submitCodeLoading = false
        })
      } catch (e) {
        this.submitCodeLoading = false
      }
    },
    // 切换下单tab表格
    handleTab(m) {
      this.tabIndex = m;
      this.refreshTable()
    },
    // 表格选择方法
    selectMethod(val) {
      this.multipleSelection = val
    },
    changeRowClass({ row, rowIndex }) {
      if (row.isFirst == 1) {
        return 'highlight-danger-row-border'
      }
      return ''
    },
  },
}
</script>

<style scoped>
.table-tab {
  display: flex;
  justify-content: space-between;
}

.tab {
  list-style-type: none;
  display: flex;
  margin-bottom: 12px;
  margin-top: 0;
  padding-left: 0;
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

.tab li:nth-child(5) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}
</style>
