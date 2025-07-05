<template>
  <div class="app-container">
    <div style="height: 100%">
      <div class="search">
        <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
          <el-form-item label="委托编号" prop="entrustCode">
            <el-input v-model="queryParams.entrustCode" clearable placeholder="请输入" size="small"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="检验状态" prop="insState">
            <el-select v-model="queryParams.insState" size="small" style="width: 100%" @change="refreshTable()">
              <el-option v-for="(a, i) in dict.type.inspection_task_state" :key="i" :label="a.label"
                         :value="a.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" type="primary" @click="refreshTable()">查询</el-button>
            <el-button size="mini" @click="refresh()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="center" v-loading="tableLoading">
        <div class="center-options">
          <div style="display: flex; align-items: center">
            <span style="font-size: 14px">试验室种类:</span>
            <ul class="tab">
              <li v-for="(m, i) in tabList" :key="i" :class="{ active: i == tabIndex }" @click="handleTab(m, i)">
                {{ m.label.replace("试验室", "") }}
              </li>
            </ul>
            <div>
              <el-checkbox v-model="alone" class="view-self-checkbox"
                           @change="changeCheckBox"><span>我的任务</span></el-checkbox>
            </div>
          </div>
          <div class="center-title">
            <span>总计任务数量:</span>
            <span>{{ page.total }}</span>
          </div>
        </div>
        <lims-table :tableData="tableData" :column="column" :page="page"
          :rowClassName="rowClassName" :height="'calc(100vh - 300px)'" @pagination="pagination"
          key="tableData0">
          <div slot="action" slot-scope="scope">
            <el-button size="small" type="text" @click="handleDataLook(scope.row)">数据查看</el-button>
            <el-button type="text" size="small"
                       :disabled="(scope.row.userName == null || scope.row.insState == 3 || scope.row.insState == 5) && checkPermi(['update:product:onPlan'])"
                       @click="editInspection(scope.row)">修改检验值</el-button>
            <el-button type="text" size="small" :disabled="(
                  scope.row.userName == null ||
                  scope.row.insState == 3 ||
                  scope.row.insState == 5 ||
                  (scope.row.userName && !scope.row.userName.includes(nickName))
                )"
                       @click="handleInspection(scope.row)">检验</el-button>
            <el-button type="text" size="small" :disabled="(
                  scope.row.userName == null ||
                  scope.row.insState == 5 ||
                  scope.row.insState == 3 ||
                  (scope.row.userName && !scope.row.userName.includes(nickName))
                )"
                       @click="handleConnect(scope.row)">交接</el-button>
            <el-button type="text" size="small" @click="viewInspectInfo(scope.row)">原始记录</el-button>
            <el-popover placement="bottom" trigger="hover" style="margin-left: 6px">
              <template #reference>
                <el-button link type="text" size="small">更多</el-button>
              </template>
              <div>
                <el-button :disabled="(scope.row.insState != 3 || scope.row.userName == null ||
                  (scope.row.userName && !scope.row.userName.includes(nickName)))" style="margin-left: 10px" type="text" size="small" @click="download(scope.row)">下载报告</el-button>
                <el-upload ref='upload'
                           :action="javaApi + '/insReport/inReport'"
                           :before-upload="beforeUpload"
                           :data="{id: scope.row.insReportId}"
                           :headers="uploadHeader" :on-error="onError"
                           :on-success="handleSuccessUp"
                           :show-file-list="false"
                           style="display: inline;margin: 0 6px"
                           accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'>
                  <el-button :disabled="(scope.row.insState != 3 || scope.row.userName == null ||
                  (scope.row.userName && !scope.row.userName.includes(nickName)))" size="small" type="text">上传</el-button>
                </el-upload>
                <el-button :disabled="(scope.row.insState != 3 || scope.row.userName == null ||
                  (scope.row.userName && !scope.row.userName.includes(nickName)))" type="text" size="small" @click="handleRestore(scope.row)">还原</el-button>
                <el-button :disabled="(scope.row.insState != 3 || scope.row.userName == null ||
                  (scope.row.userName && !scope.row.userName.includes(nickName)))" type="text" size="small" @click="handleIssued(scope.row)">查看报告</el-button>
              </div>
            </el-popover>
          </div>
        </lims-table>
      </div>
    </div>
    <el-dialog :visible.sync="claimVisible" title="提示" width="400px">
      是否认领委托编号<span style="color: #33c130">{{
        sampleUserForm.entrustCode
      }}</span>的任务
      <span slot="footer" class="dialog-footer">
        <el-button @click="claimVisible = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="confirmClaim">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="connectVisible" title="检验交接" width="400px">
      <div class="search_thing">
        <div class="search_label" style="width: 90px">
          <span class="required-span">* </span>交接人员：
        </div>
        <div class="search_input">
          <el-select v-model="connect.connectPerson" filterable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="search_thing">
        <div class="search_label" style="width: 90px">
          <span class="required-span">* </span>试验室：
        </div>
        <div class="search_input">
          <el-select v-model="connect.sonLaboratory" filterable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in sonLaboratoryList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="connectVisible = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="confirmConnect">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dataDialogVisible" title="数据查看" width="80%">
      <div v-if="dataDialogVisible" style="height: 74vh; overflow-y: auto">
        <div>
          <el-form :model="entity" :inline="true">
            <el-form-item label="检验项" prop="outputWorkTime">
              <el-input v-model="entity.inspectionItem" clearable size="small"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" @click="getLookList">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <lims-table :tableData="lookTableData" :column="lookColumn" :page="lookPage" :tableLoading="lookTableLoading"
          :height="'60vh'" @pagination="lookPagination" key="tableData1" :key="upIndex"></lims-table>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="bindDialogVisible" title="检验项绑定" width="600px">
      <div slot="title" style="
          display: flex;
          align-items: center;
          justify-content: space-between;
        ">
        <span>检验项绑定</span>
        <el-button style="float: right; margin-left: 360px" size="small" @click="openBindAdd" type="primary">
          新增
        </el-button>
      </div>
      <el-table :data="bindTableData" style="width: 100%" height="70vh" v-loading="bindTableDataLoading"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column prop="inspectionItemClass" label="检验项分类" width="150">
        </el-table-column>
        <el-table-column prop="inspectionItem" label="检验项" width="150">
        </el-table-column>
        <el-table-column prop="inspectionItemSubclass" label="检验子项">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog :visible.sync="bindAddDialogVisible" title="选择绑定" width="600px">
      <el-table :data="bindAddTableData" style="width: 100%" height="60vh"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
        @selection-change="handleBindAddSelectionChange">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="inspectionItemClass" label="检验项分类" width="150">
        </el-table-column>
        <el-table-column prop="inspectionItem" label="检验项" width="150">
        </el-table-column>
        <el-table-column prop="inspectionItemSubclass" label="检验子项">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="bindAddDialogVisible = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="bindAdd">绑 定</el-button>
      </span>
    </el-dialog>
    <!--产业链信息查看-->
    <!-- <ShowInfo
      v-if="showInfoDialog"
      ref="showInfoDialog"
      :showInfoDialog="showInfoDialog"
    ></ShowInfo> -->
    <!--修改检验值弹框-->
    <edit-inspection-item ref="editInspectionItem"></edit-inspection-item>
    <!--查看工时弹框-->
    <viewManHourDia ref="viewManHourDia"></viewManHourDia>
    <!--不合格复测查看弹框-->
    <un-pass-retest-result v-if="retestVisible" :retestInfo="retestInfo" :retestVisible="retestVisible" @closeRetestLook="closeRetestLook"></un-pass-retest-result>
    <!--报告查看-->
    <el-dialog title="报告查看" :visible.sync="issuedVisible" width="80vw" :modal-append-to-body="false"
               :fullscreen="fullscreen">
      <div class="full-screen">
        <i class="el-icon-full-screen" style="cursor: pointer;font-size: 18px" @click="fullscreen = true;"
           v-if="!fullscreen"></i>
        <img src="@/assets/images/no-full.svg" alt="" v-else style="cursor: pointer;" @click="fullscreen = false;">
      </div>
      <div style="height: 80vh;" v-if="issuedVisible">
        <onlyoffice ref="onlyoffice" :options="option" style="width: 100%;height: 100%;" />
      </div>
    </el-dialog>
    <el-dialog title="查看附件" :visible.sync="lookDialogVisible" width="800px" top="5vh" fullscreen>
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.tempUrlPdf" :currentFile="{}"
                   style="max-height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import { getYearAndMonthAndDays } from "@/utils/date";

import EditInspectionItem from "./components/EditInspectionItem.vue";
import limsTable from "@/components/Table/lims-table.vue";
import viewManHourDia from "./components/viewManHourDia.vue"
import UnPassRetestResult from "@/components/rawMaterialInspection/unPassRetestResult.vue"
import {
  claimInsOrderPlan,
  upPlanUser2,
  upPlanUser,
  selectUserCondition,
  getBindingProductByProductId,
  getProductTreeByProductId,
  bindingProductTreeByProductId,
  delProductTreeByProductId,
  selectInsOrderPlanList,
  selectSampleAndProductByOrderId,
} from "@/api/business/inspectionTask.js";
import { mapGetters } from "vuex";
import {getRetestResult} from "@/api/business/rawMaterialOrder";
import {upReportUrl} from "@/api/business/insReport";
import onlyoffice from "@/components/Onlyoffice/onlyoffice.vue";
import filePreview from "@/components/Preview/filePreview.vue";
export default {
  name: 'InspectionTask',
  components: {
    filePreview, onlyoffice,
    EditInspectionItem,
    limsTable,
    viewManHourDia,
    UnPassRetestResult
  },
  dicts: ["urgency_level", "inspection_task_state"],
  computed: {
    ...mapGetters(["nickName", "userId"]),
  },
  data() {
    return {
      issuedVisible: false,
      fullscreen: false,
      lookDialogVisible: false,
      option: null,
      InspectionKey: 1,
      bindDialogVisible: false,
      bindAddDialogVisible: false,
      bindTableDataLoading: false,
      bindTableData: [],
      bindAddTableData: [],
      chooseBindAddList: [],
      examine: null,
      inspectorList: [], //检验人员列表
      alone: true,
      sampleUserForm: {
        entrustCode: null,
        insSampleId: null,
        userId: null,
      },
      claimVisible: false,
      tabList: [
        { label: "委托", value: 0 },
        { label: "原材料", value: 1 },
      ],
      active: 1,
      tabIndex: 0,
      dataDialogVisible: false,
      planTotal: 0,
      insStateList: [],
      state: 0, // 0:台账页，1：检验页面,2检验页面(复核)，默认为0,3数据查看
      typeSource: null, // 0:成品下单，1：原材料下单
      sonLaboratory: null, // 0:委托，1：原材料
      activeFace: 0, // 1：下单，2：查看，3：审核，默认为0
      currentId: null,
      entityCopy: {},
      orderId: 0,
      personList: [],
      connectVisible: false,
      connect: {
        connectPerson: "",
        sonLaboratory: "",
      },
      loading: false,
      currentTime: null,
      sonLaboratoryList: [],
      typeSourceList: [
        { label: "成品下单", value: 0 },
        { label: "原材料下单", value: 1 },
      ],
      customsInspection: {},
      showInfoDialog: false, // 产业链信息查看
      isReport: 0,
      retestInfo: [],
      isCopper: null,
      bindCurrentInfo: {},
      lookInfo: {},
      orderTypeList: [
        {
          value: "Customer-ordered test",
          label: "委托试验",
          type: "success",
          effect: "plain",
        },
        {
          value: "抽检",
          label: "抽检",
          type: "",
          effect: "plain",
        },
        {
          value: "进厂检验",
          label: "进厂检验",
          type: "info",
          effect: "plain",
        },
        {
          value: "Quarterly inspection",
          label: "季度检验",
          type: "warning",
          effect: "plain",
        },
      ],
      urgencyLevel: [],
      inspectionTaskState: [],
      tableData: [],
      column: [
        {
          label: "委托编号",
          prop: "entrustCode",
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        { label: "样品名称", prop: "sample", width: "160px" },
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
        { label: "样品型号", prop: "sampleModel", width: "120px" },
        {
          label: "紧急程度",
          prop: "type",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return '普通'
            } else if (params == 1) {
              return '优先'
            } else {
              return '紧急'
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success'
            } else if (params == 1) {
              return 'warning'
            } else {
              return 'danger'
            }
          }
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
        {
          label: "状态",
          prop: "insState",
          dataType: "tag",
          formatData: (params) => {
            if (this.inspectionTaskState.find((m) => m.value == params)) {
              return this.inspectionTaskState.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (this.inspectionTaskState.find((m) => m.value == params)) {
              return this.inspectionTaskState.find((m) => m.value == params).type;
            } else {
              return null
            }
          },
        },
        { label: "检验人", prop: "userName" },
        { label: "复核人", prop: "checkName" },
        { label: "约定时间", prop: "appointed" },
        { label: "下发时间", prop: "sendTime", width: "140px" },
        { label: "检验开始时间", prop: "insTime", width: "140px" },
        { label: "理由", prop: "verifyTell", width: "140px" },
        {
          fixed: "right",
          dataType: "slot",
          slot: "action",
          width: '340px',
          label: "操作"
        }
      ],
      page: {
        total: 0,
        size: 20,
        current: 0,
      },
      tableLoading: false,
      queryParams: {},
      insResultList: [
        {
          value: 1,
          label: "合格",
          type: "success",
        },
        {
          value: 0,
          label: "不合格",
          type: "danger",
        },
        {
          value: 3,
          label: "不判定",
          type: "",
        },
      ],
      // 数据查看相关字段---开始
      entity: {
        inspectionItem: "",
      },
      lookTableData: [],
      lookColumn: [
        {
          label: "样品编号",
          prop: "sampleCode",
          width: "140px",
          mergeCol: true, //合并列
        },
        { label: "样品名称", prop: "sample", width: "140px", mergeCol: true },
        { label: "检验项分类", prop: "inspectionItemClass" },
        { label: "检验项", prop: "inspectionItem", width: "140px" },
        { label: "检验子项", prop: "inspectionItemSubclass" },
        { label: "单位", prop: "unit" },
        { label: "样品型号", prop: "model", mergeCol: true },
        { label: "条件", prop: "radius" },
        { label: "电缆标识", prop: "cableTag" },
        { label: "试验要求", prop: "tell" },
        { label: "检验结果", prop: "lastValue" },
        {
          label: "结果判定",
          prop: "insResult",
          dataType: "tag",
          formatData: (params) => {
            let obj = this.insResultList.find((m) => m.value == params)
            if (obj) {
              return this.insResultList.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            let obj = this.insResultList.find((m) => m.value == params)
            if (obj) {
              return this.insResultList.find((m) => m.value == params).type;
            } else {
              return null
            }
          },
        },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "不合格复测查看",
              type: "text",
              clickFun: (row) => {
                this.getRetestResultInfo(row);
              },
              disabled: (row) => {
                return row.insResult!=0
              },
            },
            {
              name: "检验项绑定",
              type: "text",
              clickFun: (row) => {
                this.getBinding(row);
              },
              disabled: (row) => {
                return (
                  this.lookInfo.userName == null ||
                  this.lookInfo.insState == 3 ||
                  this.lookInfo.insState == 5
                );
              },
            },
          ],
        },
      ],
      lookPage: {
        total: 0,
        size: 20,
        current: 0,
      },
      lookTableLoading: false,
      // 数据查看相关字段---结束
      retestVisible: false,
      upIndex: 0
    };
  },
  mounted() {
    this.getAuthorizedPerson();
    this.queryParams.userId = this.userId;
    this.currentTime = getYearAndMonthAndDays();
    this.getDicts("urgency_level").then((response) => {
      this.urgencyLevel = this.dictToValue(response.data);
    });
    this.getDicts("inspection_task_state").then((response) => {
      this.inspectionTaskState = this.dictToValue(response.data);
    });
    this.refreshTable();
  },
  activated() {
    this.getAuthorizedPerson();
    this.currentTime = getYearAndMonthAndDays();
    this.getDicts("urgency_level").then((response) => {
      this.urgencyLevel = this.dictToValue(response.data);
    });
    this.getDicts("inspection_task_state").then((response) => {
      this.inspectionTaskState = this.dictToValue(response.data);
    });
    this.refreshTable();
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      selectInsOrderPlanList({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData = res.data.records;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    getLookList() {
      this.lookTableLoading = true;
      let param = { id: this.lookInfo.id, ...this.lookPage, inspectionItem: this.entity.inspectionItem };
      delete param.total;
      selectSampleAndProductByOrderId({ ...param })
        .then((res) => {
          this.lookTableLoading = false;
          if (res.code === 200) {
            this.lookTableData = res.data.records;
            this.lookPage.total = res.data.total;
            this.upIndex++
            this.dataDialogVisible = true;
          }
        })
        .catch((err) => {
          this.lookTableLoading = false;
        });
    },
    lookPagination({ page, limit }) {
      this.lookPage.current = page;
      this.lookPage.size = limit;
      this.getLookList();
    },
    // 修改检验值
    editInspection(row) {
      this.$refs.editInspectionItem.showDialog(row.id);
    },
    // 查看工时
    viewManHour(row) {
      this.$refs.viewManHourDia.showDialog(row.id, row.insState);
    },
    // 数据查看
    handleDataLook(row) {
      this.lookInfo = row;
      this.getLookList();
    },
    changeCheckBox(val) {
      this.queryParams.userId = val ? 0 : null;
      this.refreshTable();
    },
    refresh() {
      this.queryParams = {};
      this.queryParams.userId = this.userId;
      this.alone = true;
      this.refreshTable();
    },
    refreshTable(e) {
      this.page.current = 1;
      this.queryParams.typeSource = this.tabIndex;
      this.getList();
    },
    // 下载报告
    download(row) {
      let url = (row.urlS===null||row.urlS==='')?row.url:row.urlS
      const link = document.createElement('a');
      link.href = this.javaApi + url;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
    },
    // 还原操作
    handleRestore(row) {
      this.$confirm('是否还原当前报告?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        upReportUrl({ id: row.insReportId }).then(res => {
          if (res.code === 200) {
            this.$message.success('还原成功')
            this.refreshTable()
          }
        })
      }).catch(() => { })

    },
    // 查看报告
    handleIssued(row) {
      console.log('================')
      // todo: 查看报告组件
      this.currentInfo = row;
      let fileName = row.url
      let fileType = "docx"
      if (row.tempUrlPdf != null || row.tempUrlPdf === '') {
        fileName = row.tempUrlPdf
        fileType = "pdf"
      }
      fileName = fileName.replace('/word/','')
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
    // 查看产业链信息
    openInfoDialog(row) {
      this.showInfoDialog = true;
      this.$nextTick(() => {
        this.$refs.showInfoDialog.getInfo(row.ifsInventoryId);
      });
    },
    confirmClaim() {
      this.loading = true;
      claimInsOrderPlan(this.sampleUserForm)
        .then((res) => {
          if (res.code === 200 && res.data) {
            this.loading = false;
            this.$message.success("认领成功");
            this.claimVisible = false;
            this.refreshTable("page");
          }
        })
        .catch((error) => {
          console.error(error);
          this.loading = false;
        });
    },
    rowClassName({ row, rowIndex }) {
      if (this.currentTime == row.appointed) {
        return "highlight-warning-row-border";
      } else if (this.currentTime > row.appointed) {
        return "highlight-danger-row-border";
      }
      return "";
    },
    handleTab(m, i) {
      this.tabIndex = i;
      this.queryParams.sonLaboratory = "";
      this.refreshTable();
    },
    selectAllByOne(row) {
      this.isCopper = row.isCopper;
      this.customsInspection = row;
      this.activeFace = 2;
      this.examine = 1;
      this.isReport = 0;
      this.currentId = parseInt(row.id);
      switch (row.isCopper) {
        case 0:
          // 原材料
          this.$router.push({
            path: "/materialOrder/customsInspectionView", query: {
              customsInspection: row,
              active: this.activeFace,
              currentId: this.currentId,
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
              currentId: this.currentId
            }
          });
          break;
        case 1:
          // 铜材
          this.$router.push({
            path: "/materialOrder/CopperView", query: {
              active: this.activeFace,
              currentId: this.currentId
            }
          });
          break;
      }

    },
    goback() {
      this.state = 0;
      this.refreshTable("page");
    },
    // 进行检验任务
    handleInspection(row) {
      //当前检验任务的检验人列表
      let inspectorList = [];
      if (row.userName) {
        inspectorList = row.userName.split(",");
      }
      if (this.nickName) {
        inspectorList.push(this.nickName);
      }
      this.inspectorList = inspectorList;
      this.sonLaboratory = row.sonLaboratory;
      this.state = 1;
      this.typeSource = row.typeSource;
      this.orderId = row.id;
      this.$router.push({
        path: "/inspectionTask/inspection",
        query: {
          inspectorList: this.inspectorList,
          sonLaboratory: this.sonLaboratory,
          state: this.state,
          typeSource: this.typeSource,
          orderId: this.orderId,
        },
      });
    },
    // 查看检验数据
    viewInspectInfo(row) {
      //当前检验任务的检验人列表
      let inspectorList = [];
      if (row.userName) {
        inspectorList = row.userName.split(",");
      }
      if (this.nickName) {
        inspectorList.push(this.nickName);
      }
      this.inspectorList = inspectorList;
      this.sonLaboratory = row.sonLaboratory;
      this.state = 3;
      this.typeSource = row.typeSource;
      this.orderId = row.id;
      this.$router.push({
        path: "/inspectionTask/inspection",
        query: {
          inspectorList: this.inspectorList,
          sonLaboratory: this.sonLaboratory,
          state: this.state,
          typeSource: this.typeSource,
          orderId: this.orderId,
        },
      });
    },
    handleConnect(row) {
      this.orderId = row.id;
      this.connect = {};
      this.connectVisible = true;
      upPlanUser2({ orderId: this.orderId }).then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.sonLaboratoryList = [];
          res.data.forEach((m) => {
            this.sonLaboratoryList.push({
              value: m,
              label: m,
            });
          });
          this.connect.sonLaboratory = this.sonLaboratoryList[0].value;
        }
      });
    },
    confirmConnect() {
      if (
        this.connect.connectPerson == null ||
        this.connect.connectPerson == "" ||
        this.connect.connectPerson == undefined
      ) {
        this.$message.error("未选择交接人员");
        return;
      }
      if (
        this.connect.sonLaboratory == null ||
        this.connect.sonLaboratory == "" ||
        this.connect.sonLaboratory == undefined
      ) {
        this.$message.error("未选择试验室");
        return;
      }
      this.loading = true;
      upPlanUser({
        orderId: this.orderId,
        userId: this.connect.connectPerson,
        sonLaboratory: this.connect.sonLaboratory,
      })
        .then((res) => {
          if (res.code === 200) {
            this.loading = false;
            this.$message.success("操作成功");
            this.refreshTable("page");
          }
          this.connectVisible = false;
        })
        .catch((error) => {
          console.error(error);
          this.loading = false;
        });
    },
    getAuthorizedPerson() {
      selectUserCondition({ type: 1 }).then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.personList = data;
      });
    },
    // 查看不合格复测结果
    getRetestResultInfo (row) {
      getRetestResult({insProductId: row.insProductId}).then(res => {
        this.retestVisible = true
        this.retestInfo = res.data
      })
    },
    closeRetestLook () {
      this.retestVisible = false
    },
    // 绑定检验项绑定
    getBinding(row) {
      this.bindCurrentInfo = row;
      getBindingProductByProductId({ productId: row.insProductId })
        .then((res) => {
          this.bindTableData = res.data;
          this.bindDialogVisible = true;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    openBindAdd() {
      getProductTreeByProductId({
        productId: this.bindCurrentInfo.insProductId,
      })
        .then((res) => {
          this.bindAddTableData = res.data;
          this.bindAddDialogVisible = true;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    handleBindAddSelectionChange(list) {
      this.chooseBindAddList = list;
    },
    bindAdd() {
      if (this.chooseBindAddList.length == 0) {
        this.$message.error("请选择检验项");
        return;
      }
      this.chooseBindAddList.forEach((item) => {
        delete item.template;
      });
      this.loading = true;
      bindingProductTreeByProductId({
        insProductId: this.bindCurrentInfo.insProductId,
        insProductBindingList: this.chooseBindAddList,
      })
        .then((res) => {
          this.loading = false;
          this.$message.success("绑定成功");
          this.bindAddDialogVisible = false;
          this.getBinding(this.bindCurrentInfo);
        })
        .catch((error) => {
          console.error(error);
        });
    },
    handleDelete(row) {
      this.$confirm("确认删除此检验项, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
        this.bindTableDataLoading = true
        delProductTreeByProductId({ productId: row.id }).then((res) => {
          this.bindTableDataLoading = false
          this.getBinding(this.bindCurrentInfo);
        }).catch((error) => {
          this.bindTableDataLoading = false
          console.error(error);
        });
      });
    },
    beforeUpload (file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError(error, file, fileList, index) {
      this.$message.error('文件上传失败，请重试');
    },
    onExceed() {
      this.$message.warning("超出文件个数");
    },
    handleSuccessUp(response) {
      if (response.code == 200) {
        this.$refs.upload.clearFiles()
        this.$message.success("上传成功");
        this.refreshTable()
      } else {
        this.$message.error(response.message);
      }
    },
  },
};
</script>
<style scoped>
.search_thing {
  display: flex;
  align-items: center;
  height: 50px;
}

.search_label {
  width: 100px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 120px);
}

.tab {
  list-style-type: none;
  display: flex;
  padding-left: 6px;
}

.tab li {
  line-height: 24px;
  padding: 4px 10px;
  font-size: 14px;
  color: #333333;
  border: 1px solid #eeeeee;
  cursor: pointer;
}

.tab li:nth-child(1) {
  border-radius: 8px 0 0 8px;
}

.tab li:nth-last-child(1) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3a7bfa;
  color: #3a7bfa;
}
.center-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.center .center-options .center-title span:last-child {
  color: #3a7bfa;
  font-size: 23px;
  font-weight: 400;
}

.view-self-checkbox {
  margin-left: 50px;
}
</style>
