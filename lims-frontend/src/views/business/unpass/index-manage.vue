<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="entity" ref="entity" size="small" :inline="true">
          <el-form-item label="订单编号" prop="no">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.no"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="规格型号" prop="model">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.model"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="样品名称" prop="sample">
            <el-input size="small" placeholder="请输入" clearable v-model="entity.sample"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div>
      <lims-table :tableData="tableData" :column="column"
                  :height="'calc(100vh - 250px)'" @pagination="pagination"
                  :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <el-dialog
      title="提交"
      :show-close="false"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      width="30%">
      <span>是否确认提交OA？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitOA" :loading="submitOALoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="删除"
      :show-close="false"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      :visible.sync="deleteVisible"
      width="30%">
      <span>是否确认<span style="color: #FF4902">删除</span>OA？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteVisible = false">取 消</el-button>
        <el-button type="primary" @click="cancelOA" :loading="cancelOALoading">确 定</el-button>
      </span>
    </el-dialog>
    <UnPassDialog ref="unPassDialog" v-if="unPassDialog"
                  :orderId="orderId"
                  @resetForm="resetForm1"
                  :unPassDialog="unPassDialog"></UnPassDialog>
    <OAProcess ref="OAProcess"
               :OAProcess="OAProcess"
               @closeOAProcess="closeOAProcess"
               v-if="OAProcess"></OAProcess>
  </div>
</template>

<script>
import UnPassDialog from "./components/unPassDialog.vue";
import OAProcess from "./components/OAProcess.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {deleteUnqualifiedHandler, page, pushOA} from "@/api/business/unqualifiedHandler";

export default {
  components: {
    limsTable,
    OAProcess,
    UnPassDialog,
  },
  data() {
    return {
      handlerId: null,
      entity: {
        sample: null,
        model: null,
      },
      tableData: [],
      tableLoading: false,
      column: [
        { label: '编号', prop: 'no', width: "160px", },
        // {
        //   label: "OA审核状态",
        //   prop: "oaState",
        //   width: "100px",
        //   dataType: "tag",
        //   formatData: (params) => {
        //     if (params == 1) {
        //       return "待审核";
        //     } else if(params == 2) {
        //       return "审核中";
        //     }  else if(params == 3) {
        //       return "通过";
        //     } else {
        //       return "驳回";
        //     }
        //   },
        //   formatType: (params) => {
        //     if (params == 1) {
        //       return "warning";
        //     } else if(params == 2) {
        //       return "info";
        //     }  else if(params == 3) {
        //       return "success";
        //     } else {
        //       return "danger";
        //     }
        //   },
        // },
        { label: '订单号', prop: 'orderNo' },
        {
          label: "零件号",
          prop: "partNo",
          width: "160px",
          dataType: "link",
          linkMethod: "openUnPassDialog",
        },
        { label: '零件描述', prop: 'partDesc' },
        { label: '物料名称', prop: 'materialName' },
        { label: '生产批次', prop: 'productionBatch' },
        { label: '到货数量', prop: 'cargoQuantity' },
        { label: '规格型号', prop: 'specsModels' },
        { label: '报检日期', prop: 'inspectTime' },
        { label: '状态', prop: 'statusDB' },
        { label: '反馈人', prop: 'feedbackUser' },
        { label: '要检验的采购数量', prop: 'qtyToInspect' },
        { label: '反馈日期', prop: 'feedbackTime' },
        {
          label: "分类",
          prop: "classification",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "一类不合格";
            } else if(params == 1) {
              return "二类不合格";
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "warning";
            } else if(params == 1) {
              return "info";
            } else {
              return "null";
            }
          },
        },
        { label: '供应商名称', prop: 'supplierName' },
        {
          label: "不合格归属",
          prop: "offGradeAscription",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "生产反馈不合格";
            } else if(params == 1) {
              return "检测不合格";
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "warning";
            } else if(params == 1) {
              return "info";
            } else {
              return "null";
            }
          },
        },
        { label: '不合格描述', prop: 'unqualifiedDesc' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '180px',
          operation: [
            {
              name: '提交OA',
              type: 'text',
              clickFun: (row) => {
                this.openOA(row);
              },
              disabled: (row, index) => {
                return row.requestId !== null  // 有requestId说明已经提交过OA，不可再次提交
              }
            },
            {
              name: '查看OA流程',
              type: 'text',
              clickFun: (row) => {
                this.OAView(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteOA(row);
              },
              disabled: (row, index) => {
                return row.requestId !== null  // 有requestId说明已经提交过OA，不可再次提交
              }
            },
          ]
        }
      ],
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      statusList: [],
      dialogVisible: false, // 确认提交OA弹框
      unPassDialog: false, // 不合格处理弹框
      orderId: '',
      OAProcess: false, // OA流程弹框
      submitOALoading: false, // OA流程弹框提交按钮loading
      deleteVisible: false, // OA流程删除弹框
      cancelOALoading: false, // OA流程删除弹框提交按钮loading
    };
  },
  mounted() {
    this.refreshTable()
  },
  methods: {
    refreshTable() {
      this.tableLoading = true
      page({ ...this.page, ...this.entity }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    // 打开不合格处理弹框
    openUnPassDialog (row) {
      this.unPassDialog = true
      this.$nextTick(() => {
        this.$refs.unPassDialog.getInsOrder('view', row)
      })
    },
    // 关闭不合格处理弹框
    resetForm1 () {
      this.$refs.unPassDialog.$refs['unPassForm'].resetFields();
      this.unPassDialog = false
    },
    // 打开删除OA确认弹框
    deleteOA (row) {
      this.handlerId = row.handlerId
      this.deleteVisible = true
    },
    // 提交删除申请
    cancelOA () {
      this.de = true
      deleteUnqualifiedHandler({id: this.handlerId,}).then(res => {
        this.cancelOALoading = false
        if (res.code === 200) {
          this.deleteVisible = false
          this.$message.success('删除成功')
          this.refreshTable('page')
        }
      }).catch(error => {
        this.cancelOALoading = false
        console.error(error);
      });
    },
    // 查看提交OA的数据
    openOA (row) {
      this.handlerId = row.handlerId
      this.dialogVisible = true
    },
    // 查看OA流程
    OAView (row) {
      this.OAProcess = true
      this.$nextTick(() => {
        this.$refs.OAProcess.getInfo(row.handlerId)
      })
    },
    // 关闭查看OA流程的弹框
    closeOAProcess () {
      this.OAProcess = false
    },
    //提交OA
    submitOA(row) {
      // 提交OA
      this.submitOALoading = true
      pushOA({handlerId: this.handlerId,}).then(res => {
        this.submitOALoading = false
        if (res.code === 200) {
          this.dialogVisible = false
          this.$message.success('提交成功')
          this.refreshTable('page')
        }
      }).catch(error => {
        this.submitOALoading = false
        console.error(error);
      });
    }
  }
};
</script>
