<template>
  <div class="app-container">
    <div>
      <el-form :model="entity" ref="entity" size="small" :inline="true">
        <el-form-item label="委托编号" prop="entrustCode">
          <el-input size="small" placeholder="请输入" clearable v-model="entity.entrustCode"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="下单类别" prop="entrustCode">
          <el-select size="small" v-model="entity.typeSource" clearable style="width: 100%;" @change="refreshTable()">
            <el-option v-for="(a, i) in typeSourceList" :key="i" :label="a.label" :value="a.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="page_total">
        <span>总计任务数量:</span>
        <span>{{ page.total }}</span>
      </div>
    </div>
    <div>
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
                  :rowClassName="rowClassName" :height="'calc(100vh - 270px)'" @pagination="pagination"
                  key="tableData0"></lims-table>
    </div>
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
import onlyoffice from "@/components/Onlyoffice/onlyoffice.vue";
import limsTable from "@/components/Table/lims-table.vue";
import { selectInsOrderPlanList, selectUserCondition } from "@/api/business/inspectionTask";
import { mapGetters } from "vuex";
import { upReportUrl } from "@/api/business/insReport";
import filePreview from "@/components/Preview/filePreview.vue";

export default {
  name: 'InspectionReview',
  components: {
    filePreview,
    onlyoffice,
    limsTable,
  },
  dicts: ["urgency_level", "inspection_task_state"],
  computed: {
    ...mapGetters(["nickName", "userId"]),
  },
  data() {
    return {
      lookDialogVisible: false,
      alone: false,
      tabList: [],
      active: 1,
      tabIndex: 0,
      entity: {
        sonLaboratory: null,
        insState: '3',
        userId: 0,
        typeSource: null,
        isCheck: 1
      },
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
            if (this.urgencyLevel.find((m) => m.value == params)) {
              return this.urgencyLevel.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (this.urgencyLevel.find((m) => m.value == params)) {
              return this.urgencyLevel.find((m) => m.value == params).type;
            } else {
              return null
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
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "复核",
              type: "text",
              clickFun: (row) => {
                this.handleReview(row);
              },
              disabled: (row) => {
                return row.checkName == null || (row.checkName && !row.checkName.includes(this.nickName))
              }
            },
            {
              name: "下载报告",
              type: "text",
              clickFun: (row) => {
                this.download(row);
              },
            },
            {
              name: "上传",
              type: "upload",
              accept: '.doc,.docx',
              url: '/insReport/inReport',
              uploadIdFun: (row) => {
                return row.insReportId
              }
            },
            {
              name: "还原",
              type: "text",
              clickFun: (row) => {
                this.handleRestore(row);
              },
            },
            {
              name: "查看报告",
              type: "text",
              clickFun: (row) => {
                this.handleIssued(row);
              }
            },
            // {
            //   name: "产业链",
            //   type: "text",
            //   clickFun: (row) => {
            //     this.openInfoDialog(row);
            //   },
            //   disabled: (row) => {
            //     return row.typeSource !== 1
            //   },
            // }
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
      upIndex: 0,
      planTotal: 0,
      insStateList: [],
      state: 0,//0:台账页，1：检验页面,2检验页面(复核)，默认为0
      activeFace: 0, //1：下单，2：查看，3：审核，默认为0
      examine: null,
      isReport: 0,
      currentId: null,
      orderId: 0,
      personList: [],
      currentTime: null,
      sonLaboratoryList: [],
      typeSourceList: [
        { label: '成品下单', value: 0 },
        { label: '原材料下单', value: 1 },
      ],
      isCopper: null,
      customsInspection: {},
      typeSource: null,// 0:成品下单，1：原材料下单
      showInfoDialog: false, // 产业链信息查看
      issuedVisible: false,
      fullscreen: false,
      option: null,
      orderTypeList: [
        { label: '委托试验', value: 'Customer-ordered test', type: 'success' },
        { label: '抽检', value: '抽检', type: 'danger' },
        { label: '进厂检验', value: '进厂检验', type: 'info' },
        { label: '季度检验', value: 'Quarterly inspection', type: '' },
      ],
      urgencyLevel: [],
      inspectionTaskState: [],
    }
  },
  created() {
    this.getDicts("urgency_level").then((response) => {
      this.urgencyLevel = this.dictToValue(response.data);
    });
    this.getDicts("inspection_task_state").then((response) => {
      this.inspectionTaskState = this.dictToValue(response.data);
    });
    this.getAuthorizedPerson()
    this.currentTime = getYearAndMonthAndDays()
  },
  mounted() {
    this.refreshTable()
  },
  activated() {
    this.refreshTable()
  },
  methods: {
    refreshTable() {
      this.tableLoading = true;
      let param = { ...this.entity, ...this.page };
      delete param.total;
      selectInsOrderPlanList({ ...param }).then((res) => {
        this.tableLoading = false;
        if (res.code === 200) {
          this.tableData = res.data.records;
          this.page.total = res.data.total;
        }
      }).catch((err) => {
        this.tableLoading = false;
      });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.refreshTable();
    },
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    rowClassName({ row, rowIndex }) {
      if (this.currentTime == row.appointed) {
        return "highlight-warning-row-border";
      } else if (this.currentTime > row.appointed) {
        return "highlight-danger-row-border";
      }
      return "";
    },
    selectAllByOne(row) {
      this.isCopper = row.isCopper
      this.customsInspection = row
      this.activeFace = 2;
      this.examine = 1;
      this.isReport = 0;
      this.currentId = parseInt(row.id)
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
    // 复核回调
    handleReview(row) {
      this.$router.push({
        path: "/inspectionTask/inspection",
        query: {
          sonLaboratory: row.sonLaboratory,
          state: 2,
          typeSource: row.typeSource,
          orderId: row.id,
        },
      })
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
  }
}
</script>
<style scoped>
.page_total {
  margin-bottom: 10px;
  display: flex;
}
.page_total span:last-child {
  color: #3a7bfa;
  font-size: 23px;
  font-weight: 400;
}
</style>
