<template>
  <div class="app-container">
    <div>
      <el-form :model="entity" ref="entity" size="small" :inline="true">
        <el-form-item label="报告编号" prop="code">
          <el-input v-model="entity.code" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="queryStatus">
          <el-select v-model="entity.queryStatus" clearable size="small" @change="refreshTable()">
            <el-option v-for="(a, i) in queryStatusList" :key="i" :label="a.label" :value="a.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="下单类别" prop="typeSource">
          <el-select v-model="entity.typeSource" clearable size="small" @change="refreshTable()">
            <el-option v-for="(a, i) in typeSourceList" :key="i" :label="a.label" :value="a.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="检验类别" prop="orderType">
          <el-select v-model="entity.orderType" clearable size="small" @change="refreshTable()">
            <el-option v-for="(a, i) in orderTypeList" :key="i" :label="a.label" :value="a.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="refreshTable()">查询</el-button>
          <el-button size="mini" @click="refresh()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="text-align: right;margin-bottom: 10px">
      <el-button :loading="outLoading" size="mini" type="primary" @click="handleDowns">批量下载</el-button>
    </div>
    <div>
      <lims-table :tableData="valueTableData" :column="column" :page="page" :tableLoading="tableLoading"
        :isSelection="true" :handleSelectionChange="handleChange" :height="'calc(100vh - 290px)'"
        @pagination="pagination" key="valueTableData">
        <div slot="action" slot-scope="scope">
          <el-button size="small" type="text" @click="viewIssued(scope.row)">查看报告</el-button>
          <el-upload ref='upload1' style="display: inline;margin: 0 6px" :action="fileAction1 + '?id=' + scope.row.id"
            :auto-upload="true" :before-upload="fileBeforeUpload1" :headers="uploadHeader" :on-error="onError1"
            :on-success="handleSuccessUp1" :show-file-list="false" accept='.doc,.docx'>
            <el-button size="small" type="text"
              :disabled="scope.row.state != 0 || nickName !== scope.row.writeUserName">上传</el-button>
          </el-upload>
          <el-button type="text" size="small" :disabled="scope.row.state != 0 || nickName !== scope.row.writeUserName"
            @click="handleRestore(scope.row)">还原</el-button>
          <el-button type="text" size="small" :disabled="scope.row.state != 0 || nickName !== scope.row.writeUserName"
            @click="sendBackTask(scope.row)">退回任务</el-button>
          <el-button type="text" size="small" :disabled="scope.row.state != 0 || nickName !== scope.row.writeUserName"
            @click="handle(scope.row)">提交</el-button>
          <el-button type="text" size="small"
            :disabled="scope.row.state == null || scope.row.state == 0 || scope.row.isExamine == 1 || nickName !== scope.row.examineUser"
            @click="handleIssued(scope.row)">审核</el-button>
          <el-button type="text" size="small"
            :disabled="scope.row.state == null || scope.row.state == 0 || scope.row.isExamine == 0 || scope.row.isExamine == null || scope.row.isRatify == 1 || nickName !== scope.row.ratifyUser"
            @click="handleApprove(scope.row)">批准</el-button>
          <el-popover placement="bottom" trigger="hover" style="margin-left: 6px">
            <template #reference>
              <el-button link type="text" size="small">更多</el-button>
            </template>
            <div>
              <el-button style="margin-left: 10px" type="text" size="small" @click="download(scope.row)">下载</el-button>
              <el-button type="text" size="small" @click="viewInspectInfo(scope.row)">原始记录</el-button>
              <el-button type="text" size="small" @click="handleFileLook(scope.row)">附件上传</el-button>
            </div>
          </el-popover>
        </div>
      </lims-table>
    </div>
    <el-dialog :fullscreen="fullscreen" :modal-append-to-body="false" :visible.sync="claimVisible" title="在线编制"
      width="22cm">
      <div class="full-screen">
        <i v-if="!fullscreen" class="el-icon-full-screen" style="cursor: pointer;font-size: 18px"
          @click="fullscreen = true;"></i>
        <img v-else alt="" src="@/assets/images/no-full.svg" style="cursor: pointer;" @click="fullscreen = false;">
      </div>
      <Word v-if="claimVisible" ref="Word" :class="{ fullscreen: fullscreen }" :value="value" style="height:70vh" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="claimVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmClaim">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :fullscreen="fullscreen" :modal-append-to-body="false" :visible.sync="issuedVisible" title="报告审核"
      width="80vw">
      <div class="full-screen">
        <i v-if="!fullscreen" class="el-icon-full-screen" style="cursor: pointer;font-size: 18px"
          @click="fullscreen = true;"></i>
        <img v-else alt="" src="@/assets/images/no-full.svg" style="cursor: pointer;" @click="fullscreen = false;">
      </div>
      <div v-if="issuedVisible" style="height: 70vh;">
        <onlyoffice ref="onlyoffice" :options="option" style="width: 100%;height: 100%;" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="loadingIssued" @click="issuedReasonVisible = true">不通过</el-button>
        <el-button type="primary" @click="subIssued">通 过</el-button>
      </span>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" :visible.sync="issuedReasonVisible" title="不通过原因" width="400px">
      <div class="search_thing">
        <div class="search_label">不通过原因：</div>
        <div class="search_input"><el-input v-model="reason" clearable placeholder="请输入" size="small"></el-input></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="loadingIssuedReason" @click="issuedReasonVisible = false">取消</el-button>
        <el-button :loading="loadingIssuedReason" type="primary" @click="handleIssuedReason">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog :fullscreen="fullscreen" :modal-append-to-body="false" :visible.sync="approveVisible" title="报告批准"
      width="80vw">
      <div class="full-screen">
        <i v-if="!fullscreen" class="el-icon-full-screen" style="cursor: pointer;font-size: 18px"
          @click="fullscreen = true;"></i>
        <img v-else alt="" src="@/assets/images/no-full.svg" style="cursor: pointer;" @click="fullscreen = false;">
      </div>
      <div v-if="approveVisible" style="height: 70vh;">
        <onlyoffice ref="onlyoffice" :options="option" style="width: 100%;height: 100%;" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="loadingApprove" @click="approveReasonVisible = true">不批准</el-button>
        <el-button :loading="loadingApprove" type="primary" @click="subApprove">批 准</el-button>
      </span>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" :visible.sync="approveReasonVisible" title="不批准原因" width="400px">
      <div class="search_thing">
        <div class="search_label">不批准原因：</div>
        <div class="search_input"><el-input v-model="reason" clearable placeholder="请输入" size="small"></el-input></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button :disabled="loadingApproveReason" @click="approveReasonVisible = false">取消</el-button>
        <el-button :loading="loadingApproveReason" type="primary" @click="handleApproveReason">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :visible.sync="addApproverDia" title="指定批准人员" width="400px"
      @close="closeAddApproverDia">
      <div class="body" style="display: flex;padding: 10px;align-items: center;">
        <div class="search_label" style="width: 150px;"><span class="required-span">*</span>批准人：</div>
        <div class="search_input" style="width: 100%;">
          <el-select v-model="approver" clearable filterable placeholder="请选择" size="small" style="width: 100%;">
            <el-option v-for="(item, i) in approverList" :key="i" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAddApproverDia">取 消</el-button>
        <el-button :loading="loadingIssued" type="primary" @click="submitAddApprover">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :visible.sync="addVerifyDia" title="指定审核人员" width="400px"
      @close="closeAddVerifyDia">
      <div class="body" style="display: flex;padding: 10px;align-items: center;">
        <div class="search_label" style="width: 150px;"><span class="required-span">*</span>审核人：</div>
        <div class="search_input" style="width: 100%;">
          <el-select v-model="verifyUser" clearable filterable placeholder="请选择" size="small" style="width: 100%;">
            <el-option v-for="(item, i) in approverList" :key="i" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAddVerifyDia">取 消</el-button>
        <el-button :loading="loadingVerify" type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
    <!--产业链信息查看-->
    <!--    <ShowInfo v-if="showInfoDialog" ref="showInfoDialog" :showInfoDialog="showInfoDialog"></ShowInfo>-->
    <!--报告查看-->
    <el-dialog :fullscreen="fullscreen" :modal-append-to-body="false" :visible.sync="viewIssuedVisible" title="报告查看"
      width="80vw">
      <div class="full-screen">
        <i v-if="!fullscreen" class="el-icon-full-screen" style="cursor: pointer;font-size: 18px"
          @click="fullscreen = true;"></i>
        <img v-else alt="" src="@/assets/images/no-full.svg" style="cursor: pointer;" @click="fullscreen = false;">
      </div>
      <div v-if="viewIssuedVisible" style="height: 80vh;">
        <onlyoffice ref="onlyoffice" :options="option" style="width: 100%;height: 100%;" />
      </div>
    </el-dialog>
    <!--附件查看-->
    <el-dialog :visible.sync="filesDialogVisible" title="附件查看" width="80%" @closed="closeFilesLook">
      <div style="margin-bottom: 10px">
        <el-upload ref='upload' :action="fileAction" :auto-upload="true" :before-upload="fileBeforeUpload"
          :data="{ orderId: filesLookInfo.insOrderId }" :headers="uploadHeader" :on-error="onError"
          :on-success="handleSuccessUp" :show-file-list="false"
          accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' style="width: 80px !important;">
          <el-button size="small" style="height: 38px" type="primary">附件上传</el-button>
        </el-upload>
      </div>
      <lims-table :tableData="tableDataFile" :column="columnFile" height="500px"
        key="tableDataFile" :tableLoading="tableLoadingFile"></lims-table>
    </el-dialog>
  </div>
</template>

<script>
import onlyoffice from "@/components/Onlyoffice/onlyoffice.vue";
// import ShowInfo from "../do/b1-material-ins-order/showInfo.vue";
import {
  downAll,
  examineReport,
  pageInsReport,
  ratifyReport,
  sendBackTask,
  upReportUrl,
  writeReport
} from "@/api/business/insReport";
import { mapGetters } from "vuex";
import { selectUserCondition } from "@/api/business/inspectionTask";
import limsTable from "@/components/Table/lims-table.vue";
import {delFile, downFile, getFileList} from "@/api/business/rawMaterialOrder";
export default {
  name: 'ReportPreparation',
  components: { limsTable, onlyoffice },
  data() {
    return {
      entity: {
        queryStatus: null,
        code: null,
        typeSource: null,
      },
      page: {
        current: 1,
        size: 20,
        total: 0
      },
      upIndex: 0,
      statusList: [],
      claimVisible: false,
      issuedVisible: false,
      issuedReasonVisible: false,
      approveVisible: false,
      approveReasonVisible: false,
      fullscreen: false,
      loadingApproveReason: false,
      loadingApprove: false,
      loadingIssuedReason: false,
      loadingIssued: false,
      value: ``,
      reason: '',
      currentInfo: null,
      option: null,
      mutiList: [],
      outLoading: false,
      inLoading: false,
      addApproverDia: false, // 指定审批人员弹框
      approver: '', // 审批人员
      approverId: '', // 审批人员
      approverList: [],
      addVerifyDia: false, // 指定审核人员弹框
      verifyUser: null, // 审核人员
      loadingVerify: false, // 审核人员
      typeSourceList: [
        { label: '成品下单', value: 0 },
        { label: '原材料下单', value: 1 },
      ],
      orderTypeList: [
        { label: '委托试验', value: 'Customer-ordered test' },
        { label: '抽检', value: '抽检' },
        { label: '进厂检验', value: '进厂检验' },
        { label: '季度检验', value: 'Quarterly inspection' },
      ],
      showInfoDialog: false, // 产业链信息查看
      isReport: 1,
      activeFace: 0, // 1：下单，2：查看，3：审核，默认为0
      customsInspection: {},
      currentId: null,
      examine: null,
      viewIssuedVisible: false,
      queryStatusList: [
        { label: '待提交', value: 0 },
        { label: '待审核', value: 1 },
        { label: '待批准', value: 2 },
      ],
      state: 0,
      orderId: 0,
      inspectorList: [],//检验人员列表
      InspectionKey: 1,
      typeSource: null,// 0:成品下单，1：原材料下单
      sonLaboratory: '', // 试验室
      filesDialogVisible: false,
      filesLookInfo: {},
      tableDataFile: [],
      tableLoadingFile: false,
      columnFile: [
        {
          dataType: 'tag',
          label: '类型',
          prop: 'type',
          formatData: (params) => {
            if (params == 1) {
              return '图片'
            } else if (params == 2) {
              return '文件'
            } else {
              return ''
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'warning'
            } else {
              return ''
            }
          }
        },
        { label: '附件名称', prop: 'fileName' },
        { label: '上传人', prop: 'name' },
        { label: '上传时间', prop: 'createTime' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '170px',
          operation: [
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row);
              }
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.delete(row);
              }
            },
          ]
        }
      ],
      isCopper: null,
      tableLoading: false,
      valueTableData: [],
      column: [
        {
          label: "报告编号",
          prop: "code",
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        {
          label: "下单类别",
          prop: "typeSource",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "成品下单";
            } else {
              return "原材料下单";
            }
          },
        },
        {
          label: "检验类型",
          prop: "orderType",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            return this.orderTypeList.find((m) => m.value == params).label;
          },
          formatType: (params) => {
            return this.orderTypeList.find((m) => m.value == params).type;
          },
        },
        { label: "创建时间", prop: "createTime" },
        { label: "提交人", prop: "writeUserName" },
        { label: "提交时间", prop: "writeTime" },
        {
          label: "提交状态",
          prop: "state",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "待提交";
            } else {
              return "已提交";
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "danger";
            } else {
              return "success";
            }
          },
        },
        { label: "审核人", prop: "examineUser" },
        { label: "审核时间", prop: "examineTime" },
        {
          label: "审核状态",
          prop: "isExamine",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "不通过";
            } else if (params == 1) {
              return "通过";
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "danger";
            } else if (params == 1) {
              return "success";
            } else {
              return null
            }
          },
        },
        { label: "审核备注", prop: "examineTell" },
        { label: "批准人", prop: "ratifyUser" },
        { label: "批准时间", prop: "ratifyTime" },
        {
          label: "批准状态",
          prop: "isRatify",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "不批准";
            } else if (params == 1) {
              return "批准";
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "danger";
            } else if (params == 1) {
              return "success";
            } else {
              return null
            }
          },
        },
        { label: "批准备注", prop: "ratifyTell" },
        {
          fixed: "right",
          dataType: "slot",
          slot: "action",
          width: '360px',
          label: "操作"
        }
      ],
      userName: '',
    }
  },
  computed: {
    ...mapGetters(["nickName", "userId"]),
    action() {
      return this.javaApi + '/insReport/upAll'
    },
    fileAction() {
      return this.javaApi + '/insOrderPlan/uploadFile'
    },
    fileAction1() {
      return this.javaApi + '/insReport/inReport'
    }
  },
  mounted() {
    this.refreshTable()
  },
  methods: {
    refreshTable() {
      this.tableLoading = true
      pageInsReport({
        ...this.page, ...this.entity
      }).then(res => {
        this.tableLoading = false
        this.page.total = res.data.body.total
        this.valueTableData = res.data.body.records
      })
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.refreshTable();
    },
    refresh() {
      this.resetForm('entity')
      this.refreshTable();
    },
    handleChange(arr) {
      this.mutiList = arr
    },
    // 查看检验数据
    viewInspectInfo(row) {
      //当前检验任务的检验人列表
      let inspectorList = []
      if (row.userName) {
        inspectorList = row.userName.split(',')
      }
      inspectorList.push(this.nickName)
      this.$router.push({
        path: "/inspectionTask/inspection",
        query: {
          sonLaboratory: row.sonLaboratory,
          state: 3,
          typeSource: row.typeSource,
          orderId: row.insOrderId,
          inspectorList: inspectorList,
        },
      })
    },
    // 打开查看附件弹框
    handleFileLook(row) {
      this.filesLookInfo = row
      this.filesDialogVisible = true
      this.getFileList()
    },
    // 查询附件查看列表回调
    getFileList() {
      this.tableLoadingFile = true
      getFileList({ insOrderId: this.filesLookInfo.insOrderId }).then(res => {
        this.tableLoadingFile = false
        if (res.code === 200) {
          this.tableDataFile = res.data.records
        }
      }).catch(err => {
        this.tableLoadingFile = false
      })
    },
    closeFilesLook() {
      this.filesDialogVisible = false
    },
    handleSuccessUp(response,) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.getFileList()
      }
    },
    handleSuccessUp1(response,) {
      if (response.code == 200) {
        this.$message.success('上传成功');
      }
    },
    // 下载附件的文件
    handleDown(row) {
      downFile({
        id: row.id,
      }).then(res => {
        this.$download.saveAs(res.data.fileUrl, row.fileName);
      }).catch(error => {

      })
    },
    // 删除附件文件
    delete(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoadingFile = true
        delFile({ id: row.id }).then(res => {
          this.tableLoadingFile = false
          this.$message.success('删除成功')
          this.getFileList()
        }).catch(err => {
          this.tableLoadingFile = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 查看产业链信息
    openInfoDialog(row) {
      this.showInfoDialog = true
      this.$nextTick(() => {
        this.$refs.showInfoDialog.getInfo(row.ifsInventoryId)
      })
    },
    handleDowns() {
      if (this.mutiList.length == 0) {
        this.$message.error('请选择报告')
        return
      }
      let str = this.mutiList.map(m => m.id).join(',')
      this.outLoading = true
      downAll({ ids: str }).then(res => {
        this.outLoading = false
        this.$download.zip(res.data, '报告.zip');
      }).catch(() => {
        this.outLoading = false
      })

    },
    beforeUpload(file) {
      const isZip = file.type === 'application/zip' || file.name.endsWith('.zip');
      if (!isZip) {
        this.$message.error('上传文件只能是 ZIP 格式!');
      }
      if (isZip) {
        this.inLoading = true;
      }
      return isZip;
    },
    fileBeforeUpload(file) {
      let flag = true
      console.log('file----', file)
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    fileBeforeUpload1(file) {
      let flag = true
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload1.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    handleSuccess(response,) {
      this.inLoading = false;
      if (response.code == 200) {
        this.$message.success('导入成功')
        this.refreshTable()
      } else {
        this.$message.error(response.message)
      }
    },
    onError(err, file, fileList, type) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    onError1(err, file, fileList, type) {
      this.$message.error('上传失败')
      this.$refs.upload1.clearFiles()
    },
    confirmClaim() {
      // console.log(this.$refs.Word.getValue())
    },
    selectAllByOne(row) {
      this.isCopper = row.isCopper
      this.customsInspection = row
      this.activeFace = 2;
      this.examine = 1;
      this.isReport = 1
      this.currentId = parseInt(row.insOrderId)
      switch (row.isCopper) {
        case 0:
          // 原材料
          this.$router.push({
            path: "/materialOrder/customsInspectionView", query: {
              customsInspection: row,
              active: this.activeFace,
              currentId: row.insOrderId,
              isReport: this.isReport
            }
          });
          break;
        case null:
          // 成品
          this.$router.push({
            path: "/productOrder/addView", query: {
              examine: this.examine,
              active: this.activeFace,
              currentId: row.insOrderId
            }
          });
          break;
        case 1:
          // 铜材
          this.$router.push({
            path: "/materialOrder/CopperView", query: {
              active: this.activeFace,
              currentId: row.insOrderId
            }
          });
          break;
      }
    },
    download(row) {
      let url = row.urlS ? row.urlS : row.url;
      const link = document.createElement('a');
      link.href = this.javaApi + url;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
    },
    // 还原操作
    handleRestore(row) {
      upReportUrl({
        id: row.id
      }).then(res => {
        if (res.code === 200) {
          this.$message.success('操作成功')
          this.refreshTable('page')
        }
      })
    },
    // 审核按钮
    handleIssued(row) {
      this.currentInfo = row;
      let fileName = (row.urlS === null || row.urlS === '') ? row.url : row.urlS
      let fileType = "docx"
      if (row.tempUrlPdf != null || row.tempUrlPdf === '') {
        fileName = row.tempUrlPdf
        fileType = "pdf"
      }
      fileName = fileName.replace('/word/', '')
      const userName = this.nickName
      this.option = {
        url: this.javaApi + "/word/" + fileName,
        isEdit: false,
        fileType: fileType,
        title: fileName,
        lang: 'zh-CN',
        isPrint: false,
        user_id: 1,
        user_name: userName,
        editUrl: this.javaApi + "/insReport/onlyOffice/save?fileName=" + fileName
      }
      this.issuedVisible = true;
    },
    // 查看报告
    viewIssued(row) {
      console.log('this.javaApi', this.javaApi)
      this.currentInfo = row;
      let fileName = (row.urlS === null || row.urlS === '') ? row.url : row.urlS
      let fileType = "docx"
      if (row.tempUrlPdf != null || row.tempUrlPdf === '') {
        fileName = row.tempUrlPdf
        fileType = "pdf"
      }
      fileName = fileName.replace('/word/', '')
      const userName = this.nickName
      this.option = {
        url: this.javaApi + "/word/" + fileName,
        isEdit: false,
        fileType: fileType,
        title: fileName,
        lang: 'zh-CN',
        isPrint: false,
        user_id: 1,
        user_name: userName,
        editUrl: this.javaApi + "/insReport/onlyOffice/save?fileName=" + fileName
      }
      this.viewIssuedVisible = true;
    },
    // 退回到任务
    sendBackTask(row) {
      this.$confirm('确认退回到检验任务?退回后需重新提交复核', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.upLoad = true
        sendBackTask({ id: row.id }).then(res => {
          this.upLoad = false
          this.$message.success('退回成功！')
          this.refreshTable('page')
        }).catch(err => {
          this.upLoad = false
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 审核通过
    submitAddApprover() {
      if (!this.approver) {
        this.$message.error('请选择审批人')
        return
      }
      this.loadingIssued = true;
      examineReport({
        id: this.currentInfo.id,
        userId: this.approver,
        isExamine: 1
      }).then(res => {
        this.loadingIssued = false;
        this.$message.success('提交成功')
        this.refreshTable('page')
        this.currentInfo = null;
        this.addApproverDia = false
        this.issuedVisible = false;
      }).catch(e => {
        this.$message.error('提交失败')
        this.loadingIssued = false;
      })
    },
    closeAddApproverDia() {
      this.addApproverDia = false
      this.approver = ''
    },
    // 点击通过，需要选择批准人员
    subIssued() {
      this.getAuthorizedPerson()
      this.addApproverDia = true
    },
    // 获取人员列表
    getAuthorizedPerson() {
      selectUserCondition({ type: 1 }).then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.approverList = data;
      })
    },
    // 批准按钮
    handleApprove(row) {
      this.currentInfo = row;
      let fileName = (row.urlS === null || row.urlS === '') ? row.url : row.urlS
      let fileType = "docx"
      if (row.tempUrlPdf != null || row.tempUrlPdf === '') {
        fileName = row.tempUrlPdf
        fileType = "pdf"
      }
      fileName = fileName.replace('/word/', '')
      const userName = this.nickName
      this.option = {
        url: this.javaApi + "/word/" + fileName,
        isEdit: false,
        fileType: fileType,
        title: fileName,
        lang: 'zh-CN',
        isPrint: false,
        user_id: 1,
        user_name: userName,
        editUrl: this.javaApi + "/insReport/onlyOffice/save?fileName=" + fileName
      }
      this.approveVisible = true;
    },
    // 批准通过
    subApprove() {
      this.loadingApprove = true;
      ratifyReport({
        id: this.currentInfo.id,
        isRatify: 1
      }).then(res => {
        this.loadingApprove = false;
        this.$message.success('已批准')
        this.refreshTable('page')
        this.currentInfo = null;
        this.approveVisible = false;
      }).catch(() => {
        this.loadingApprove = false;
      })
    },
    handle(row) {
      this.getAuthorizedPerson()
      this.currentInfo = row;
      this.addVerifyDia = true
    },
    // 提交审核人操作
    handleSubmit(row) {
      this.loadingVerify = true
      writeReport({
        id: this.currentInfo.id,
        userId: this.verifyUser,
      }).then(res => {
        this.loadingVerify = false
        this.addVerifyDia = false
        this.$message.success('提交成功')
        this.refreshTable('page')
      }).catch(e => {
        this.loadingVerify = false
        this.$message.error('提交失败')
      })
    },
    closeAddVerifyDia() {
      this.addVerifyDia = false
      this.verifyUser = ''
    },
    // 审核不通过原因提交
    handleIssuedReason() {
      if (!this.reason) {
        return this.$message.error('请输入原因')
      }
      this.loadingIssuedReason = true;
      examineReport({
        id: this.currentInfo.id,
        isExamine: 0,
        examineTell: this.reason
      }).then(res => {
        this.loadingIssuedReason = false;
        this.$message.success('操作成功')
        this.refreshTable('page')
        this.currentInfo = null;
        this.reason = '';
        this.issuedVisible = false;
        this.issuedReasonVisible = false;
      }).catch(e => {
        this.$message.error('操作失败')
        this.loadingIssuedReason = false;
      })
    },
    // 不批准原因提交
    handleApproveReason() {
      if (!this.reason) {
        return this.$message.error('请输入原因')
      }
      this.loadingApproveReason = true
      ratifyReport({
        id: this.currentInfo.id,
        isRatify: 0,
        examineTell: this.reason
      }).then(res => {
        this.loadingApproveReason = false
        this.$message.success('操作成功')
        this.refreshTable('page')
        this.currentInfo = null;
        this.reason = '';
        this.approveVisible = false;
        this.approveReasonVisible = false;
      }).catch(e => {
        this.$message.error('操作失败')
        this.loadingIssuedReason = false;
      })
    }
  }
}
</script>
<style scoped>
.title {
  height: 60px;
  line-height: 60px;
}

.search {
  background-color: #fff;
  height: 80px;
  display: flex;
  align-items: center;
}

.search_thing {
  display: flex;
  align-items: center;
  height: 50px;
}

.search_label {
  width: 120px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 120px);
}

.table {
  margin-top: 10px;
  background-color: #fff;
  width: calc(100% - 40px);
  height: calc(100% - 60px - 80px - 10px - 40px);
  padding: 20px;
}

.el-form-item {
  margin-bottom: 16px;
}

.full-screen {
  position: absolute;
  right: 52px;
  top: 22px;
}

.btns {
  position: absolute;
  right: 40px;
  top: 50%;
  transform: translate(0, -50%);
  display: flex;
  align-items: center;
}

.fullscreen {
  height: 82vh
}
</style>
