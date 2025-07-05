<template>
  <!--  7.7质量监控计划-->
  <div class="app-container">
    <div class="table-card">
      <div style="display: flex;justify-content: space-between">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">计划名称</span>
            <el-input size="small" placeholder="请输入" clearable v-model="yearForm.monitorName"
                      @keyup.enter.native="getYearPlanList"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearPlanList">查询</el-button>
            <el-button size="mini" @click="clearYear">重置</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="leadInto">导入</el-button>
        </div>
      </div>
      <lims-table :tableData="yearTableData" :column="yearColumnData" :page="yearPage" :tableLoading="yearLoading"
                  :height="'calc(100vh - 34em)'" @pagination="pagination" :rowClick="rowClick" key="yearTableData"></lims-table>
    </div>
    <div style="margin-top: 20px">
      <div style="display: flex;justify-content: space-between">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">监控目的</span>
            <el-input v-model="yearDetailForm.monitorPurpose" placeholder="请输入" size="small"
                      @keyup.enter.native="getYearDetailPlanList"></el-input>
          </div>
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">监控项目</span>
            <el-input v-model="yearDetailForm.monitorProject" placeholder="请输入" size="small"
                      @keyup.enter.native="getYearDetailPlanList"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearDetailPlanList">查询</el-button>
            <el-button size="mini" @click="clearDetail">重置</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="exportSignatureTemplate">导出签名模板</el-button>
          <el-button size="small" type="primary" @click="showDialog('add')">新增</el-button>
        </div>
      </div>
      <lims-table :tableData="yearDetailTableData" :column="yearDetailColumnData" :page="yearDetailPage"
        :tableLoading="yearDetailLoading" :height="'calc(100vh - 34em)'" @pagination="pagination1" ref="yearDetailTableData"
        key="yearDetailTableData"></lims-table>
    </div>
    <!--新增修改弹框-->
    <detail-form-dialog v-if="formDia" ref="formDia" :qualityMonitorId="qualityMonitorId"
      @closeDia="closeDia"></detail-form-dialog>
    <!--实施流程弹框-->
    <carry-out-dialog v-if="carryOutDia" ref="carryOutDia" :qualityMonitorId="qualityMonitorId"
      @closeCarryOutDia="closeCarryOutDia"></carry-out-dialog>
    <!--评价流程弹框-->
    <evaluate-dialog v-if="evaluateDialog" ref="evaluateDialog" @closeEvaDia="closeEvaDia"></evaluate-dialog>
    <el-dialog :visible.sync="examineDialog" title="审核" width="30%" @close="closeExamineDia">
      <span>
        审核备注：
        <el-input v-model="examineInfo.examineRemark" type="textarea"></el-input>
      </span>
      <span style="margin-top: 10px;display: inline-block">
        批准人：
        <el-select v-model="examineInfo.ratifyUserId" clearable filterable size="small" style="width: 70%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="examineLoading" @click="handleReview(0)">不通过</el-button>
        <el-button :loading="examineLoading" type="primary" @click="handleReview(1)">通 过</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="ratifyDialog" title="批准" width="30%" @close="closeRatifyDia">
      <span>
        批准备注：
        <el-input v-model="ratifyInfo.ratifyRemark" type="textarea"></el-input>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="ratifyLoading" @click="handleRatify(0)">不批准</el-button>
        <el-button :loading="ratifyLoading" type="primary" @click="handleRatify(1)">批 准</el-button>
      </span>
    </el-dialog>
    <!--预览报告-->
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}"
        :fileUrl="javaApi + '/word/' + currentInfo.finishReportUrl" style="height: 70vh;overflow-y: auto;" />
      <div>
        批准状态：
        <el-tag v-if="this.ratifyStatus === 1" type="success">批准</el-tag>
        <el-tag v-if="this.ratifyStatus === 0" type="danger">不批准</el-tag>
      </div>
      <div>
        批准意见：
        <el-input v-model="ratifyRemark" :disabled="this.ratifyStatus === 1" type="textarea"></el-input>
      </div>
      <span v-if="this.ratifyStatus !== 1" slot="footer" class="dialog-footer">
        <el-button :loading="lookDialogLoading" @click="handleApproval(0)">不批准</el-button>
        <el-button :loading="lookDialogLoading" type="primary" @click="handleApproval(1)">批 准</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="downloadDialog" title="导出" width="600px">
      <span>
        <el-button plain type="primary" :disabled="download.detailsRatifyStatus !== 3"
          @click="controlDown">实施计划导出</el-button>
        <el-button plain type="primary" :disabled="!download.finishReportUrl"
                           @click="finishReportExport">完成报告导出</el-button>
        <el-button plain type="primary" :disabled="download.evaluateStatus !== 3"
          @click="processingDown">评价导出</el-button>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="downloadDialog = false">取 消</el-button>
      </span>
    </el-dialog>
    <!--导入计划-->
    <el-dialog :visible.sync="uploadDia" title="数据导入" width="500px" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <div style="display: flex; align-items: center;">
        <span style="width: 60px">年份：</span>
        <el-date-picker v-model="planYear" type="year" value-format="yyyy" clearable size="small" format="yyyy"
          placeholder="选择年">
        </el-date-picker>
      </div>
      <div style="display: flex;align-items: center;margin: 10px 0">
        <div style="width: 60px">审核人：</div>
        <el-select v-model="examineUserId" clearable filterable size="small" style="width: 50%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </div>
      <div style="margin: 0 auto;">
        <el-upload ref="upload" :action="action" :auto-upload="false" :file-list="fileList" :headers="uploadHeader"
          :limit="1" accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'
          :on-change="beforeUpload" :on-error="onError" :on-success="handleSuccessUp" drag
          :data="{ planYear: planYear, examineUserId: examineUserId }" name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitUpload">上 传</el-button>
      </span>
    </el-dialog>
    <!--生成报告弹框-->
    <el-dialog :visible.sync="uploadDia1" title="生成报告" width="500px">
      <div v-if="ratifyRemark">
        批准意见：
        <el-input v-model="ratifyRemark" :disabled="this.ratifyStatus === 1" type="textarea"></el-input>
      </div>
      <div style="margin: 0 auto;">
        <el-upload ref="upload1" :action="action1" :auto-upload="false"
          :data="{ qualityMonitorDetailsId: qualityMonitorDetailsId }" :file-list="fileList1" :headers="uploadHeader"
          :limit="1" :on-change="beforeUpload1" :on-error="onError1" :on-success="onSuccess1" accept='.doc,.docx' drag
          name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadDia1 = false">取 消</el-button>
        <el-button :loading="uploading1" type="primary" @click="submitUpload1()">上 传</el-button>
      </span>
    </el-dialog>
    <view-record v-if="ViewRecord" ref="ViewRecord"></view-record>
  </div>
</template>

<script>
import DetailFormDialog from './components/detailFormDialog.vue';
import CarryOutDialog from './components/carryOutDialog.vue';
import EvaluateDialog from './components/evaluateDialog.vue';
import filePreview from "@/components/Preview/filePreview.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {
  delQualityMonitorDetail,
  delQualitySupervise,
  examineQualityMonitor, exportQualityMonitorDetail, exportQualityMonitorEvaluate, exportQualityMonitorRatify,
  pageQualityMonitor, pageQualityMonitorDetail, ratifyFinishReport,
  ratifyQualityMonitor
} from "@/api/cnas/process/ensureResults/qualityMonitor";
import { selectUserCondition } from "@/api/performance/class";
import { mapGetters } from "vuex";
import ViewRecord from "./components/ViewRecord.vue";

export default {
  name: 'EnsureResultsValidity',
  // import 引入的组件需要注入到对象中才能使用
  components: {ViewRecord, limsTable, filePreview, EvaluateDialog, CarryOutDialog, DetailFormDialog },
  data() {
    // 这里存放数据
    return {
      ViewRecord: false,
      examineUserId: '',
      responsibleOptions: [],
      yearForm: {
        monitorName: '',
      },
      yearPage: {
        current: 1,
        size: 20,
        total: 0
      },
      yearColumnData: [
        {
          label: '计划名称',
          prop: 'monitorName',
          width: '150px'
        }, {
          label: '编制人',
          prop: 'writeName',
          width: '100'
        }, {
          label: '编制日期',
          prop: 'writeTime',
          width: '160'
        }, {
          dataType: 'tag',
          label: '审核状态',
          prop: 'examineStatus',
          width: '100',
          formatData: (params) => {
            if (params === 0) {
              return '不通过';
            } else if (params === 1) {
              return '通过';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return 'danger';
            } else if (params === 1) {
              return 'success';
            } else {
              return null;
            }
          }
        }, {
          label: '审核内容',
          prop: 'examineRemark',
          width: '100'
        }, {
          label: '审核人',
          prop: 'examineName',
          width: '100'
        }, {
          label: '审核日期',
          prop: 'examineTime',
          width: '160'
        }, {
          dataType: 'tag',
          label: '批准状态',
          prop: 'ratifyStatus',
          width: '100',
          formatData: (params) => {
            if (params === 0) {
              return '不批准';
            } else if (params === 1) {
              return '批准';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return 'danger';
            } else if (params === 1) {
              return 'success';
            } else {
              return null;
            }
          }
        }, {
          label: '批准内容',
          prop: 'ratifyRemark',
          width: '100'
        }, {
          label: '批准人',
          prop: 'ratifyName',
          width: '100'
        }, {
          label: '批准日期',
          prop: 'ratifyTime',
          width: '160'
        }, {
          dataType: 'action',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '审核',
              type: 'text',
              disabled: (row) => {
                if (row.examineStatus === 1 || this.userId != row.examineUserId) {
                  return true
                } else {
                  return false
                }
              },
              clickFun: (row) => {
                this.examinePlan(row)
              }
            },
            {
              name: '批准',
              type: 'text',
              disabled: (row) => {
                if (row.ratifyStatus === 1 || row.examineStatus !== 1 || this.userId != row.ratifyUserId) {
                  return true
                } else {
                  return false
                }
              },
              clickFun: (row) => {
                this.approvalPlan(row)
              }
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              },
              disabled: (row) => {
                if (row.ratifyStatus !== 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              },
              disabled: (row) => {
                if (row.examineStatus === 1) {
                  return true;
                } else {
                  return false;
                }
              },
            }
          ]
        }],
      yearTableData: [],  // 年表
      yearLoading: false,
      yearDetailForm: {
        monitorPurpose: '',
        monitorProject: '',
      },
      yearDetailColumnData: [
        {
          label: '监控目的',
          prop: 'monitorPurpose',
          width: '150px',
          showOverflowTooltip: true,
        }, {
          label: '计划开展时间',
          prop: 'plannedTime',
          width: '150px'
        }, {
          label: '监控项目',
          prop: 'monitorProject',
          width: '150px'
        }, {
          label: '参加单位（人员）',
          prop: 'participant',
          width: '190px'
        }, {
          label: '预算（元）',
          prop: 'budget',
          width: '150px'
        }, {
          label: '组织人员',
          prop: 'organization',
          width: '150px'
        }, {
          label: '监控方式',
          prop: 'monitorWay',
          width: '150px'
        },{
          dataType: 'tag',
          label: '实施状态',
          prop: 'detailsRatifyStatus',
          width: '100',
          formatData: (params) => {
            if (params === 0) {
              return '未开始';
            } else if (params === 1) {
              return '待评价';
            }  else if (params === 2) {
              return '不批准';
            } else if (params === 3) {
              return '已批准';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return '';
            } else if (params === 1) {
              return 'warning';
            } else if (params === 2) {
              return 'danger';
            }  else if (params === 3) {
              return 'success';
            } else {
              return null;
            }
          }
        },{
          dataType: 'tag',
          label: '报告状态',
          prop: 'reportStatus',
          width: '100',
          formatData: (params) => {
            if (params === 0) {
              return '未开始';
            } else if (params === 1) {
              return '待评价';
            }  else if (params === 2) {
              return '不批准';
            } else if (params === 3) {
              return '已批准';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return '';
            } else if (params === 1) {
              return 'warning';
            } else if (params === 2) {
              return 'danger';
            }  else if (params === 3) {
              return 'success';
            } else {
              return null;
            }
          }
        },{
          dataType: 'tag',
          label: '评价状态',
          prop: 'evaluateStatus',
          width: '100',
          formatData: (params) => {
            if (params === 0) {
              return '未开始';
            } else if (params === 1) {
              return '待评价';
            }  else if (params === 2) {
              return '待批准';
            } else if (params === 3) {
              return '已批准';
            } else {
              return null;
            }
          },
          formatType: (params) => {
            if (params === 0) {
              return '';
            } else if (params === 1) {
              return 'warning';
            } else if (params === 2) {
              return 'info';
            }  else if (params === 3) {
              return 'success';
            } else {
              return null;
            }
          }
        }, {
          dataType: 'action',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.showDialog('edit', row)
              }
            },
            {
              name: '实施',
              type: 'text',
              clickFun: (row) => {
                this.carryOut(row)
              }
            },
            {
              name: '完成报告',
              type: 'text',
              clickFun: (row) => {
                this.record(row)
              },
            },
            {
              name: '评价',
              type: 'text',
              clickFun: (row) => {
                this.evaluate(row)
              }
            },
            {
              name: '附件上传',
              type: 'text',
              clickFun: (row) => {
                this.viewTestRecord(row)
              }
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.downLoadPost(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delYearPlanDetail(row)
              }
            }
          ]
        }
      ],
      yearDetailTableData: [], // 年明细表
      yearDetailLoading: false,
      yearDetailPage: {
        current: 1,
        size: 20,
        total: 0
      },
      formDia: false,
      qualityMonitorId: '',
      carryOutDia: false,
      evaluateDialog: false,
      examineDialog: false,
      examineLoading: false,
      ratifyDialog: false,
      ratifyLoading: false,
      examineInfo: {},
      ratifyInfo: {},
      upLoading: false,
      uploadDia: false,
      uploadDia1: false,
      uploading: false,
      uploading1: false,
      fileList: [],
      fileList1: [],
      lookDialogVisible: false,
      lookDialogLoading: false,
      currentInfo: {},
      qualityMonitorDetailsId: '',
      ratifyStatus: '',
      ratifyRemark: '',
      downloadDialog: false,
      download: {},
      planYear: ''
    };
  },
  mounted() {
    this.getYearPlanList()
  },
  // 方法集合
  methods: {
    // 查询年度计划表
    getYearPlanList() {
      const entity = {
        monitorName: this.yearForm.monitorName,
      }
      const page = this.yearPage
      this.yearLoading = true
      pageQualityMonitor({ ...entity, ...page }).then(res => {
        this.yearLoading = false
        this.yearTableData = res.data.records
        this.yearPage.total = res.data.total
        if (this.yearTableData.length > 0) {
          this.rowClick(this.yearTableData[0])
        }
      }).catch(err => {
        console.log('err---', err);
        this.yearLoading = false
      })
    },
    clearYear() {
      this.yearForm.monitorName = ''
      this.getYearPlanList()
    },
    pagination({ page, limit }) {
      this.yearPage.current = page;
      this.yearPage.size = limit;
      this.getYearPlanList();
    },
    pagination1({ page, limit }) {
      this.yearDetailPage.current = page;
      this.yearDetailPage.size = limit;
      this.getYearPlanList();
    },
    leadInto() {
      this.uploadDia = true
      this.getUserList()
    },
    // 导入流程
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
    handleSuccessUp(response) {
      this.uploading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.timer = setTimeout(() => {
          this.uploadDia = false
          this.fileList = []
          this.planYear = ''
          this.examineUserId = ''
          this.getYearPlanList()
        }, 1000)
      } else {
        this.$message.error(response.msg)
      }
    },
    submitUpload() {
      if (!this.planYear) {
        this.$message.warning('请选择年份')
        return;
      }
      if (!this.examineUserId) {
        this.$message.warning('请选择批准人')
        return;
      }
      this.$refs.upload.submit();
    },
    // end
    // 审核
    examinePlan(row) {
      this.examineDialog = true
      this.examineInfo = row
      this.getUserList()
    },
    handleReview(examineStatus) {
      // 审核状态 , 0 不通过, 1通过
      this.examineInfo.examineStatus = examineStatus
      this.examineLoading = true
      examineQualityMonitor(this.examineInfo).then(res => {
        this.examineLoading = false
        this.$message.success('操作成功')
        this.closeExamineDia()
      }).catch(err => {
        console.log('err---', err);
        this.examineLoading = false
      })
    },
    closeExamineDia() {
      this.examineDialog = false
      this.examineInfo.examineRemark = ''
      this.getYearPlanList()
    },
    // 批准
    approvalPlan(row) {
      this.ratifyDialog = true
      this.ratifyInfo = row
    },
    handleRatify(ratifyStatus) {
      // 批准状态 , 0 不通过, 1通过
      this.ratifyInfo.ratifyStatus = ratifyStatus
      this.ratifyLoading = true
      ratifyQualityMonitor(this.ratifyInfo).then(res => {
        this.ratifyLoading = false
        this.$message.success('操作成功')
        this.closeRatifyDia()
      }).catch(err => {
        console.log('err---', err);
        this.ratifyLoading = false
      })
    },
    closeRatifyDia() {
      this.ratifyDialog = false
      this.ratifyInfo.ratifyRemark = ''
      this.getYearPlanList()
    },
    // 批准报告
    handleApproval(status) {
      const personTrainingUpdateDto = {
        qualityMonitorDetailsId: this.currentInfo.qualityMonitorDetailsId,
        ratifyRemark: this.ratifyRemark,
        ratifyStatus: status
      }
      this.lookDialogLoading = true
      ratifyFinishReport(personTrainingUpdateDto).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.lookDialogVisible = false
          this.getYearDetailPlanList();
        }
        this.lookDialogLoading = false
      }).catch(() => {
        this.lookDialogLoading = false
      })
    },
    // 导出
    handleDown(row) {
      exportQualityMonitorDetail({ qualityMonitorId: row.qualityMonitorId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, row.monitorName + '.docx')
      })
    },
    // 删除进度计划表
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.yearLoading = true
        delQualitySupervise({ qualityMonitorId: row.qualityMonitorId }).then(res => {
          this.yearLoading = false
          this.$message.success('删除成功')
          this.getYearPlanList()
        }).catch(err => {
          this.yearLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 年度计划表格，点击行数据后刷新详情
    rowClick(row) {
      this.qualityMonitorId = row.qualityMonitorId
      this.getYearDetailPlanList()
    },
    // 获取年度明细表
    getYearDetailPlanList() {
      const entity = {
        qualityMonitorId: this.qualityMonitorId,
        monitorPurpose: this.yearDetailForm.monitorPurpose,
        monitorProject: this.yearDetailForm.monitorProject,
      }
      const page = this.yearDetailPage
      this.yearDetailLoading = true
      pageQualityMonitorDetail({ ...entity, ...page }).then(res => {
        this.yearDetailLoading = false
        this.yearDetailTableData = res.data.records
        this.yearDetailPage.total = res.data.total
        this.$refs.yearDetailTableData.$refs.multipleTable.doLayout()
      }).catch(err => {
        console.log('err---', err);
        this.yearDetailLoading = false
      })
    },
    // 重置明细表
    clearDetail() {
      this.yearDetailForm = {
        monitorPurpose: '',
        monitorProject: ''
      }
      this.getYearDetailPlanList()
    },
    // 明细表实施
    carryOut(row) {
      this.carryOutDia = true
      this.$nextTick(() => {
        this.$refs.carryOutDia.openDia(row)
      })
    },
    closeCarryOutDia() {
      this.carryOutDia = false
      this.getYearDetailPlanList()
    },
    // 打开完成报告弹框
    record(row) {
      this.qualityMonitorDetailsId = row.qualityMonitorDetailsId
      this.ratifyRemark = row.ratifyRemark
      if (row.finishReportUrl) {
        this.currentInfo = row
        this.ratifyStatus = row.ratifyStatus
        this.lookDialogVisible = true
      } else {
        this.uploadDia1 = true
      }
    },
    onSuccess1(response, file, fileList) {
      this.$refs.upload1.clearFiles()
      this.uploading1 = false
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.uploadDia1 = false
      } else {
        this.$message.error(response.msg)
      }
      this.getYearDetailPlanList()
    },
    beforeUpload1(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload1.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError1(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload1.clearFiles()
      this.uploading1 = false
    },
    submitUpload1() {
      if (this.$refs.upload1.uploadFiles.length === 0) {
        this.$message.error('未选择文件')
        return
      }
      this.uploading1 = true
      this.$refs.upload1.submit();
      this.uploadDia1 = false;
    },
    // end
    // 打开评价弹框
    evaluate(row) {
      this.evaluateDialog = true
      this.$nextTick(() => {
        this.$refs.evaluateDialog.openDia(row)
      })
    },
    closeEvaDia() {
      this.evaluateDialog = false
      this.getYearDetailPlanList()
    },
    // 附件上传
    viewTestRecord (row) {
      this.ViewRecord = true
      this.$nextTick(() => {
        this.$refs.ViewRecord.openDia(row)
      })
    },
    // 打开导出弹框
    downLoadPost(row) {
      this.downloadDialog = true
      this.download = row
    },
    // 打开年度明细新增、修改弹框
    showDialog(type, row) {
      this.formDia = true
      this.$nextTick(() => {
        this.$refs.formDia.openDia(type, row)
      })
    },
    closeDia() {
      this.formDia = false
      this.getYearDetailPlanList()
    },
    // 控制单导出
    controlDown() {
      exportQualityMonitorRatify({ qualityMonitorDetailsId: this.download.qualityMonitorDetailsId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '质量监控实施计划.docx')
      })
    },
    // 完成报告导出
    finishReportExport() {
      let url = this.download.finishReportUrl;
      const link = document.createElement('a');
      link.href = this.javaApi +'/word/' + url;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
    },
    // 处理单导出
    processingDown() {
      exportQualityMonitorEvaluate({ qualityMonitorDetailsId: this.download.qualityMonitorDetailsId }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '质量监控评价.docx')
      })
    },
    delYearPlanDetail(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.yearDetailLoading = true
        delQualityMonitorDetail({ qualityMonitorDetailsId: row.qualityMonitorDetailsId }).then(res => {
          this.yearDetailLoading = false
          this.$message.success('删除成功')
          this.getYearDetailPlanList()
        }).catch(err => {
          this.yearDetailLoading = false
          console.log('err---', err);
        })
      })
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then((res) => {
        this.responsibleOptions = res.data;
      })
    },
    exportSignatureTemplate() {
      let url = '质量监控记录签名模板.doc';
      const link = document.createElement('a');
      link.href = this.javaApi +'/word/' + url;
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
    }
  },
  // 用于上传文件的信息
  computed: {
    ...mapGetters(["userId"]),
    action() {
      return this.javaApi + '/qualityMonitor/importQualityMonitor'
    },
    action1() {
      return this.javaApi + '/qualityMonitor/uploadFinishReport'
    }
  },
  beforeDestroy() {
    clearTimeout(this.timer);
  }
};
</script>

<style scoped></style>
