<template>
  <div class="app-container">
    <div class="search">
      <el-form :model="componentData" ref="componentData" size="small" :inline="true">
        <el-row>
          <el-form-item label="批号" prop="updateBatchNo">
            <el-input v-model="componentData.updateBatchNo" clearable placeholder="请输入" size="small"
              @keyup.enter.native="refreshTable"></el-input>
          </el-form-item>
          <el-form-item label="零件号" prop="partNo">
            <el-input v-model="componentData.partNo" clearable placeholder="请输入" size="small"
              @keyup.enter.native="refreshTable">
            </el-input>
          </el-form-item>
          <el-form-item label="零件描述" prop="partDesc">
            <el-input v-model="componentData.partDesc" clearable placeholder="请输入" size="small"
              @keyup.enter.native="refreshTable">
            </el-input>
          </el-form-item>
          <el-form-item label="供应商" prop="supplierName" v-if="(tabIndex === 3 || tabIndex === 4)">
            <el-input v-model="componentData.supplierName" clearable placeholder="请输入" size="small"
              @keyup.enter.native="refreshTable">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button v-if="tabIndex === 3 || tabIndex === 4" :icon="!more ? 'el-icon-arrow-down' : 'el-icon-arrow-up'"
              style="color: #3A7BFA;" type="text" @click="more = !more">{{ !more ? '更多' : '收起' }}</el-button>
            <el-button size="mini" type="primary" @click="refreshTable()">查询</el-button>
            <el-button size="mini" @click="refresh()" >重置</el-button>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="检验状态" prop="inspectStatus" v-if="(tabIndex === 3 || tabIndex === 4) && more">
            <el-select v-model="componentData.inspectStatus" clearable size="small" style="width: 100%;"
              @change="refreshTable()">
              <el-option v-for="(a, i) in queryStatusList" :key="i" :label="a.label" :value="a.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="下发时间" prop="date" v-if="(tabIndex === 3 || tabIndex === 4) && more">
            <el-date-picker v-model="componentData.date" end-placeholder="结束日期" format="yyyy-MM-dd" placeholder="选择日期"
              range-separator="至" size="small" start-placeholder="开始日期" style="width: 100%;" type="daterange" @change="refreshTable()"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="委托编号" prop="entrustCode" v-if="(tabIndex === 3 || tabIndex === 4) && more">
            <el-input v-model="componentData.entrustCode" clearable placeholder="请输入" size="small"
              @keyup.enter.native="refreshTable">
            </el-input>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <div class="table">
      <div class="table-tab">
        <div>
          <ul class="tab">
            <li :class="{ active: tabIndex === 0 }" @click="handleTab(0)">待报检</li>
            <li :class="{ active: tabIndex === 1 }" @click="handleTab(1)">待下单</li>
            <li :class="{ active: tabIndex === 3 }" @click="handleTab(3)">已完成</li>
            <li :class="{ active: tabIndex === 4 }" v-if="checkPermi(['get:Ifs:ByAll'])" @click="handleTab(4)">全部</li>
          </ul>
        </div>
        <div>
          <el-button v-show="tabIndex === 4" :loading="outLoading" size="small" type="primary"
            @click="handleDown">导出</el-button>
          <el-button v-if="tabIndex === 0" :loading="btnLoading" size="small" type="primary"
            @click="openIFS">获取IFS订单</el-button>
          <el-button v-if="tabIndex === 0" size="small" type="primary" @click="declareS">报检</el-button>
          <el-button v-if="tabIndex === 0" size="small" type="primary" @click="addDeclare">新增报检信息</el-button>
        </div>
      </div>
      <!--待报检、待下单-->
      <div class="table">
        <lims-table :tableData="tableData" :column="column" v-if="tabIndex === 0 || tabIndex === 1" :isSelection="true"
          :handleSelectionChange="selectMethod" @pagination="pagination" :height="'calc(100vh - 290px)'" :page="page"
          :tableLoading="tableLoading"></lims-table>
      </div>
      <!--已完成、全部-->
      <div class="table">
        <lims-table :tableData="tableData1" :column="column1" v-if="tabIndex === 3 || tabIndex === 4"
          @pagination="pagination1" :height="'calc(100vh - 290px)'" :page="page1"
          :tableLoading="tableLoading1"></lims-table>
      </div>
    </div>
    <!-- 批量报检 -->
    <el-dialog :visible.sync="declareDialogSVisible" title="确认报检" width="30%">
      <p style="font-size:16px;color:#333333">是否确认报检选择的数据？</p>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="declareDialogSVisible = false">取 消</el-button>
          <el-button :loading="submitDeclareLoading" type="primary" @click="submitDeclareS">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 确认报检 -->
    <el-dialog :close-on-click-modal="false" :title="declareType === 'add' ? '新增报检信息' : '原材料报检'"
      :visible.sync="declareDialogVisible" width="800px" @close="resetFormData">
      <el-form ref="declareObj" :inline="true" :model="declareObj" :rules="declareObjRules" label-width="130px"
        label-position="right">
        <el-form-item class="declareObj-form-item" label="订单号:" prop="orderNo">
          <el-input v-model="declareObj.orderNo" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="零件号:" prop="partNo">
          <el-input v-model="declareObj.partNo" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="零件描述:" prop="partDesc">
          <el-input v-model="declareObj.partDesc" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="抵达的采购数量:" prop="qtyArrived">
          <el-input v-model="declareObj.qtyArrived" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item v-if="declareType !== 'add'" class="declareObj-form-item" label="供应商编号:" prop="supplierId">
          <el-input v-model="declareObj.supplierId" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="供应商名称:" prop="supplierName">
          <el-input v-model="declareObj.supplierName" :disabled="declareType !== 'add'" class="addObj-info" clearable
            placeholder="" size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="批号:" prop="updateBatchNo">
          <el-input v-model="declareObj.updateBatchNo" class="addObj-info" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item v-if="declareType !== 'add'" class="declareObj-form-item" label="库位号:" prop="locationNo">
          <el-input v-model="declareObj.locationNo" :disabled="declareType !== 'add'" class="addObj-info" clearable
            size="small"></el-input>
        </el-form-item>
        <el-form-item v-if="declareType !== 'add'" class="declareObj-form-item" label="接收时间:" prop="receiverDate">
          <el-input v-model="declareObj.receiverDate" class="addObj-info" clearable disabled size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="单位:" prop="buyUnitMeas">
          <el-input v-model="declareObj.buyUnitMeas" :disabled="declareType !== 'add'" clearable class="addObj-info"
            size="small"></el-input>
        </el-form-item>
        <el-form-item class="declareObj-form-item" label="物料类型：" prop="isExpire">
          <el-select v-model="declareObj.isExpire" prop="isExpire" :disabled="declareType !== 'add'" clearable
            size="small">
            <el-option :value="1" label="过期物料"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="resetFormData">取 消</el-button>
          <el-button :loading="submitDeclareLoading" type="primary" @click="submitDeclare">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 删除报检 -->
    <el-dialog :visible.sync="deleteVisible" title="确认删除" width="30%">
      <p style="font-size:16px;color:#333333">是否确认删除？</p>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="deleteVisible = false">取 消</el-button>
          <el-button :loading="deleteLoading" type="primary" @click="submitDelete">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!-- 撤销报检 -->
    <el-dialog :visible.sync="declareDialogVisible1" title="报检撤销" width="30%">
      <p style="font-size:16px;color:#333333">批号<span
          style="color:#34BD66">{{ insOrderRow.updateBatchNo }}</span>的信息是否<span style="color: #FF4902">撤销报检</span></p>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="declareDialogVisible1 = false">取 消</el-button>
          <el-button :loading="upLoad" type="primary" @click="submitDeclare1">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!--数据查看弹框-->
    <data-look-visible v-if="dataDialogVisible" ref="dataDialogVisible" :dataDialogVisible="dataDialogVisible"
      :dataLookInfo="dataLookInfo" @closeDataLook="closeDataLook"></data-look-visible>
  </div>
</template>

<script>
import DataLookVisible from "@/views/business/materialOrderComponents/materialOrder/dataLookVisible.vue";
import {
  addIfsInventoryQuantity,
  advancedGodown,
  concessionRelease, delIfsInventory,
  getIfsByAll,
  getIfsByFinish,
  getIfsOrder,
  getWarehouseSubmit, inspectionReport, inspectionReportOne, rawAllExport,
  revokeInspectionReport
} from '@/api/business/materialInspection'
import limsTable from "@/components/Table/lims-table.vue";

export default {
  name: 'RawMaterialInspection',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, DataLookVisible },
  data() {
    // 这里存放数据
    return {
      tableData: [],
      tableLoading: false,
      column: [
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
        { label: '抵达的采购数量', prop: 'qtyArrived', width: '140px', },
        { label: '批号', prop: 'updateBatchNo' },
        { label: '零件号', prop: 'partNo' },
        { label: '零件描述', prop: 'partDesc' },
        { label: '供应商名称', prop: 'supplierName' },
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
              name: '报检',
              type: 'text',
              clickFun: (row) => {
                this.declare(row);
              },
              showHide: (row) => {
                return this.tabIndex === 0
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteMaterial(row);
              },
              showHide: (row) => {
                return this.tabIndex === 0
              },
            },
            {
              name: '撤销报检',
              type: 'text',
              clickFun: (row) => {
                this.cancelDeclare(row);
              },
              showHide: (row) => {
                return this.tabIndex === 1
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
        { label: '报检时间', prop: 'declareDate' },
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
        { label: '委托编号', prop: 'entrustCode', width: '160px' },
        { label: '样品名称', prop: 'sampleName' },
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
        { label: '下发时间', prop: 'sendTime' },
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
              showHide: (row) => {
                return this.tabIndex === 3 || this.tabIndex === 4;
              },
            },
            {
              name: '放行',
              type: 'text',
              clickFun: (row) => {
                this.goPass(row);
              },
              disabled: (row, index) => {
                return row.inspectStatus !== 2
              },
              showHide: (row) => {
                return this.tabIndex === 4
              },
            },
            {
              name: '提前入库',
              type: 'text',
              clickFun: (row) => {
                this.advancedGodown(row);
              },
              disabled: (row, index) => {
                return row.inspectStatus !== 0 && row.inspectStatus !== 3
              },
              showHide: (row) => {
                return this.tabIndex === 4
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
      more: false,
      declareObj: {
        id: null,
        orderNo: '', // 订单号
        partNo: '', // 零件号
        partDesc: '', // 零件描述
        qtyArrived: '', // 抵达的采购数量
        supplierId: '', // 供应商编号
        supplierName: '', // 供应商名称
        updateBatchNo: '', // 批号
        locationNo: '', // 库位号
        receiverDate: '', // 接收时间
        buyUnitMeas: '', // 单位
        isExpire: '', // 单位
      },
      componentData: { // 表格数据
        updateBatchNo: null,
        partNo: null,
        partDesc: null,
        supplierName: null,
        date: null,
        entrustCode: '',
        inspectStatus: '',
      },
      declareDialogVisible1: false,
      upLoad: false,
      upIndex: 0,
      multipleSelection: [],
      currentId: null,
      btnLoading: false, // 获取IFS订单按钮loading
      insOrderRow: {},
      declareDialogVisible: false, // 确认报检弹框
      submitDeclareLoading: false, // 提交报检弹框按钮loading
      declareDialogSVisible: false,
      declareObjRules: {
        orderNo: [
          { required: false, message: '请填写订单号', trigger: 'blur' }
        ],
        partNo: [
          { required: true, message: '请填写零件号', trigger: 'blur' }
        ],
        partDesc: [
          { required: true, message: '请填写零件描述', trigger: 'blur' }
        ],
        qtyArrived: [
          { required: false, message: '请填写抵达的采购数量', trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: '请填写供应商名称', trigger: 'blur' }
        ],
        updateBatchNo: [
          { required: true, message: '请填写批号', trigger: 'blur' }
        ],
        locationNo: [
          { required: false, message: '请填写库位号', trigger: 'blur' }
        ],
        buyUnitMeas: [
          { required: false, message: '请填写单位', trigger: 'blur' }
        ],
      },
      tabList: [
        {
          label: '待报检',
          value: 0
        },
        {
          label: '待下单',
          value: 1
        },
        {
          label: '已完成',
          value: 3
        },
        {
          label: '全部',
          value: 4
        }
      ],
      tabIndex: 0,
      deleteVisible: false, // 删除报检弹框
      deleteLoading: false, // 删除报检按钮
      dataDialogVisible: false, // 数据查看弹框
      dataLookInfo: {}, // 数据查看弹框数据
      declareType: '', // 操作报检的类型
      queryStatusList: [
        { label: '检验中', value: 0 },
        { label: '合格', value: 1 },
        { label: '不合格', value: 2 },
        { label: '未下单', value: 3 },
        { label: '让步放行', value: 4 },
      ],
      outLoading: false
    }
  },
  mounted() {
    this.refreshTable()
  },
  // 方法集合
  methods: {
    // 切换tab表格
    handleTab(m) {
      this.tabIndex = m;
      this.refreshTable()
    },
    // 查询回调
    refreshTable(e) {
      if (this.tabIndex === 0 || this.tabIndex === 1) {
        // 待报检、待下单查询
        this.getWarehouseSubmitApi()
      } else if (this.tabIndex === 3) {
        // 已完成部查询
        this.getIfsByFinishList()
      } else {
        // 全部查询
        this.getIfsByAllList()
      }
    },
    // 待报检、待下单查询
    getWarehouseSubmitApi() {
      this.tableLoading = true
      const params = { ...this.componentData, isInspect: this.tabIndex, state: this.tabIndex === 0 ? null : 0, ...this.page }
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
    // 已完成部查询
    getIfsByFinishList() {
      this.tableLoading1 = true
      if(null != this.componentData.date){
        this.componentData.beginDeclareDate = this.componentData.date[0]
        this.componentData.endDeclareDate = this.componentData.date[1]
      } else {
        this.componentData.beginDeclareDate = ''
        this.componentData.endDeclareDate = ''
      }
      getIfsByFinish({ ...this.componentData, ...this.page1 }).then(res => {
        this.tableLoading1 = false
        if (res.code === 200) {
          this.tableData1 = res.data.records
          this.page1.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 全部查询
    getIfsByAllList() {
      this.tableLoading1 = true
      if(null != this.componentData.date){
        this.componentData.beginDeclareDate = this.componentData.date[0]
        this.componentData.endDeclareDate = this.componentData.date[1]
      } else {
        this.componentData.beginDeclareDate = ''
        this.componentData.endDeclareDate = ''
      }
      getIfsByAll({ ...this.componentData, ...this.page1 }).then(res => {
        this.tableLoading1 = false
        if (res.code === 200) {
          this.tableData1 = res.data.records
          this.page1.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 重 置
    refresh() {
      this.resetForm('componentData')
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
    // 打开撤销报检弹框
    cancelDeclare(row) {
      this.declareDialogVisible1 = true
      this.insOrderRow = row
    },
    // 提交撤销报检申请
    submitDeclare1() {
      revokeInspectionReport({ id: this.insOrderRow.id }).then(res => {
        if (res.code === 200) {
          this.declareDialogVisible1 = false
          this.refreshTable('page')
          this.$message.success("撤销报检成功")
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 数据查看
    handleDataLook(row) {
      this.dataLookInfo = row
      this.dataDialogVisible = true;
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
            this.refreshTable()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消放行'
        });
      });
    },
    // 提前入库
    advancedGodown(row) {
      this.$confirm('当前原材料是否提前入库?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        advancedGodown({ ifsInventoryId: row.id }).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '操作成功!'
            });
            this.refreshTable()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '操作失败'
        });
      });
    },
    // 关闭数据查看弹框
    closeDataLook() {
      this.dataDialogVisible = false
    },
    // 手动报检
    addDeclare() {
      this.clear()
      this.declareDialogVisible = true
      this.declareType = 'add'
    },
    // 选择报检数据
    declareS() {
      if (this.multipleSelection.length > 0) {
        this.declareDialogSVisible = true
      } else {
        this.$message.error('请选择需要报检的数据')
      }
    },
    // 提交批量报检
    submitDeclareS() {
      let ids = []
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      this.declareDialogSVisible = true
      inspectionReport({ ids: ids }).then(res => {
        if (res.code === 200) {
          this.declareDialogSVisible = false
          this.$message.success('报检成功')
          this.refreshTable()
        }
        this.submitDeclareLoading = false
      }).catch(err => {
        this.submitDeclareLoading = false
        console.log(err)
      })
    },
    // 打开报检确认弹框
    declare(row) {
      this.declareObj = { ...row }
      this.declareDialogVisible = true
      this.declareType = 'edit'
    },
    // 提交报检
    submitDeclare() {
      if (this.declareType === 'add') {
        this.$refs['declareObj'].validate((valid) => {
          if (valid) {
            addIfsInventoryQuantity(this.declareObj).then(res => {
              if (res.code === 200) {
                this.declareDialogVisible = false
                this.$message.success('报检成功')
                this.refreshTable()
              }
            }).catch(err => {
              console.log(err)
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      } else {
        this.$refs['declareObj'].validate((valid) => {
          if (valid) {
            inspectionReportOne({
              id: this.declareObj.id,
              updateBatchNo: this.declareObj.updateBatchNo
            }).then(res => {
              if (res.code === 200) {
                this.declareDialogVisible = false
                this.$message.success('报检成功')
                this.refreshTable()
              }
            }).catch(err => {
              console.log(err)
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
    resetFormData() {
      this.$refs['declareObj'].resetFields();
      this.declareDialogVisible = false
    },
    // 打开删除弹框
    deleteMaterial(row) {
      this.deleteVisible = true
      this.deleteInfo = row
    },
    // 确认删除
    submitDelete() {
      this.deleteLoading = true
      delIfsInventory({ id: this.deleteInfo.id }).then(res => {
        this.deleteVisible = false
        this.$message.success('删除成功')
        this.refreshTable()
        this.deleteLoading = false
      }).catch(err => {
        console.log(err)
        this.deleteLoading = false
      })
    },
    // 表格选择方法
    selectMethod(val) {
      this.multipleSelection = val
    },
    // 获取IFS按钮回调
    openIFS() {
      this.btnLoading = true
      getIfsOrder().then(res => {
        this.resetForm('componentData')
        this.refreshTable()
        this.btnLoading = false
      }).catch(err => {
        this.btnLoading = false
        console.log(err)
      })
    },
    // 全部导出
    handleDown() {
      let entity = { ...this.componentData }
      delete entity.orderBy
      this.outLoading = true
      rawAllExport({
        entity: entity
      }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, "原材料检测信息导出.xlsx");
      })
    },
    clear() {
      this.declareObj = {
        id: null,
        orderNo: '', // 订单号
        partNo: '', // 零件号
        partDesc: '', // 零件描述
        qtyArrived: '', // 抵达的采购数量
        supplierId: '', // 供应商编号
        supplierName: '', // 供应商名称
        updateBatchNo: '', // 批号
        locationNo: '', // 库位号
        receiverDate: '', // 接收时间
        buyUnitMeas: '', // 单位
        isExpire: '', // 单位
      }
    }
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

.tab li:nth-child(4) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}
</style>
