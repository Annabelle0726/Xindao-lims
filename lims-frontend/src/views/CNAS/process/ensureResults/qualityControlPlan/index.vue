<template>
  <!--  7.7质量监督计划-->
  <div class="app-container">
    <div class="table-card">
      <div style="display: flex;justify-content: space-between">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">计划名称</span>
            <el-input size="small" placeholder="请输入" clearable v-model="yearForm.superviseName"
                      @keyup.enter.native="getYearPlanList"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearPlanList">查询</el-button>
            <el-button size="mini" @click="clearYear">重置</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="record">导入</el-button>
        </div>
      </div>
      <lims-table :tableData="yearTableData" :column="yearColumnData" :page="yearPage" :tableLoading="yearLoading"
        height="40vh" @pagination="pagination" :rowClick="rowClick" key="yearTableData"></lims-table>
    </div>
    <div style="margin-top: 20px">
      <div style="display: flex;justify-content: space-between">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <el-radio-group v-model="yearDetailForm.causeType" @change="getYearDetailPlan" size="small">
              <el-radio-button :label="1">定期监督</el-radio-button>
              <el-radio-button :label="2">动态监督</el-radio-button>
            </el-radio-group>
          </div>
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">监控目的</span>
            <el-input v-model="yearDetailForm.supervisePurpose" placeholder="请输入" size="small"
                      @keyup.enter.native="getYearDetailPlan"></el-input>
          </div>
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">监控项目</span>
            <el-input v-model="yearDetailForm.superviseProject" placeholder="请输入" size="small"
                      @keyup.enter.native="getYearDetailPlan"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearDetailPlan">查询</el-button>
            <el-button size="mini" @click="clearDetail">重置</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="showDialog('add')">新增</el-button>
        </div>
      </div>
      <lims-table :tableData="yearDetailTableData" :column="yearDetailColumnData" :page="yearDetailPage"
        :tableLoading="yearDetailLoading" height="40vh" @pagination="pagination1" key="yearDetailColumnData"></lims-table>
    </div>
    <!--新增修改弹框-->
    <detail-form-dialog v-if="formDia" ref="formDia" :superviseId="superviseId"
      @closeDia="closeDia"></detail-form-dialog>
    <!--记录流程弹框-->
    <records-dialog v-if="recordsDia" ref="recordsDia" :superviseId="superviseId"
      @closeRecordsDia="closeRecordsDia"></records-dialog>
    <!--不符合处理流程弹框-->
    <processing-sheet v-if="processingDia" ref="processingDia" :superviseId="superviseId"
      @closeProcessingDia="closeProcessingDia"></processing-sheet>
    <!--纠正处理流程弹框-->
    <rectify-dialog-new v-if="rectifyDia" ref="rectifyDia" :superviseId="superviseId"
      @closeRectifyDia="closeRectifyDia"></rectify-dialog-new>
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
    <el-dialog :visible.sync="downloadDialog" title="导出" width="600px">
      <span>
        <el-button plain type="primary" :disabled="download.recordStatus !== 3" @click="controlDown">记录单导出</el-button>
        <el-button plain type="primary" :disabled="download.accordingStatus !== 3" @click="processingDown">处理单导出</el-button>
        <el-button plain type="primary" :disabled="download.correctStatus !== 3" @click="supervisoryDown">纠正单导出</el-button>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="downloadDialog = false">取 消</el-button>
      </span>
    </el-dialog>
    <!--导入计划-->
    <el-dialog :visible.sync="uploadDia" title="数据导入" width="500px" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <div style="display: flex; align-items: center;">
        <span style="width: 80px">年份：</span>
        <el-date-picker v-model="superviseYear" type="year" value-format="yyyy" clearable size="small" format="yyyy"
          placeholder="选择年">
        </el-date-picker>
        <span style="width: 110px">监督员：</span>
        <el-select v-model="recordUserIds" placeholder="请选择" size="small" @change="splitList" :multiple-limit="2"
          filterable multiple style="width: 100%">
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>
      <div style="display: flex;align-items: center;margin: 10px 0">
        <div style="width: 60px">批准人：</div>
        <el-select v-model="ratifyUserId" clearable filterable size="small" style="width: 50%;">
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </div>
      <div style="margin: 0 auto;">
        <el-upload ref="upload1" :action="action" :auto-upload="false" :file-list="fileList" :headers="uploadHeader"
          :limit="1" accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'
          :on-change="beforeUpload" :on-error="onError" :on-success="handleSuccessUp" drag
          :data="{ recordUserIds: recordUserIds1, superviseYear: superviseYear, ratifyUserId: ratifyUserId }"
          name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeUploadDia">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitUpload()">上 传</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import DetailFormDialog from './components/detailFormDialog.vue';
import RecordsDialog from './components/recordsDialog.vue';
import ProcessingSheet from './components/processingSheet.vue';
import RectifyDialogNew from './components/rectifyDialogNew.vue';
import limsTable from "@/components/Table/lims-table.vue";
import {
  delQualitySupervise, delQualitySuperviseDetail,
  exportQualitySupervise, exportSuperviseDetailRecord,
  pageQualitySupervise, pageQualitySuperviseDetail,
  ratifyQualitySupervise, superviseDetailAccordingExport,
  exportSuperviseDetaillCorrect
} from "@/api/cnas/process/ensureResults/qualitySupervise";
import { selectUserCondition } from "@/api/business/inspectionTask";
import { mapGetters } from "vuex";

export default {
  name: 'QualityControlPlan',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, RectifyDialogNew, ProcessingSheet, RecordsDialog, DetailFormDialog },
  data() {
    // 这里存放数据
    return {
      yearForm: {
        superviseName: '',
      },
      yearPage: {
        current: 1,
        size: 20,
        total: 0
      },
      yearColumnData: [
        {
          label: '计划名称',
          prop: 'superviseName',
          width: '150px'
        }, {
          label: '编制人',
          prop: 'writeUserName',
        }, {
          label: '编制日期',
          prop: 'writeTime',
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
        }, {
          label: '批准人',
          prop: 'ratifyUserName',
        }, {
          label: '批准日期',
          prop: 'ratifyTime',
          width: '160'
        }, {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '批准',
              type: 'text',
              disabled: (row) => {
                if (row.ratifyStatus === 1 || this.userId != row.ratifyUserId) {
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
                if (row.ratifyStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            }
          ]
        }],
      yearTableData: [],  // 年表
      yearLoading: false,
      yearDetailForm: {
        supervisePurpose: '',
        superviseProject: '',
        causeType: 1
      },
      yearDetailColumnData: [
        {
          label: '监督日期',
          prop: 'superviseTime',
          width: '120px'
        }, {
          label: '监督目的',
          prop: 'supervisePurpose',
          width: '180px',
        }, {
          label: '监控项目',
          prop: 'superviseProject',
          width: '180px'
        }, {
          label: '被监督人员',
          prop: 'supervisee',
          width: '120px'
        }, {
          label: '监督原因',
          prop: 'superviseReason',
          width: '150px'
        },{
          dataType: 'tag',
          label: '记录状态',
          prop: 'recordStatus',
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
          label: '控制状态',
          prop: 'accordingStatus',
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
          label: '纠正状态',
          prop: 'correctStatus',
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
        }, {
          label: '备注',
          prop: 'remark',
        }, {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.showDialog('edit', row)
              }
            },
            {
              name: '记录',
              type: 'text',
              clickFun: (row) => {
                this.records(row)
              }
            },
            {
              name: '处理',
              type: 'text',
              clickFun: (row) => {
                this.processing(row)
              },
              disabled: (row) => {
                if (row.isAccording === 1 || row.isAccording === null) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '纠正',
              type: 'text',
              clickFun: (row) => {
                this.rectify(row)
              },
              disabled: (row) => {
                if (row.isAccording === 1 || row.isAccording === null || row.isCorrect === null || row.isCorrect === 0) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.openDownloadDia(row);
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
      currentScrollPosition: 0,
      superviseId: '',
      formDia: false,
      recordsDia: false,
      processingDia: false,
      rectifyDia: false,
      ratifyDialog: false,
      ratifyLoading: false,
      ratifyInfo: {},
      downloadDialog: false,
      download: {},
      uploadDia: false,
      uploading: false,
      fileList: [],
      userList: [],
      recordUserIds: [],
      recordUserIds1: '',
      superviseYear: '',
      ratifyUserId: '',
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
        superviseName: this.yearForm.superviseName,
      }
      const page = this.yearPage
      this.yearLoading = true
      pageQualitySupervise({ ...entity, ...page }).then(res => {
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
      this.yearForm.superviseName = ''
      this.getYearPlanList()
    },
    pagination({ page, limit }) {
      this.yearPage.current = page;
      this.yearPage.size = limit;
      this.getYearPlanList();
    },
    // 导入流程
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload1.clearFiles()
        return false;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload1.clearFiles()
    },
    handleSuccessUp(response) {
      this.uploading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.recordUserIds = []
        this.superviseYear = ''
        this.fileList = []
        this.timer = setTimeout(() => {
          this.closeUploadDia()
        }, 1000)
      }
    },
    splitList(val) {
      const string = this.HaveJson(val)
      this.recordUserIds1 = string.join(',');
      console.log(this.recordUserIds1)
    },
    // 提交导入
    submitUpload() {
      if (!this.superviseYear) {
        this.$message.warning('请选择年份')
        return;
      }
      if (this.recordUserIds.length === 0) {
        this.$message.warning('请选择监督员')
        return;
      }
      if (!this.ratifyUserId) {
        this.$message.warning('请选择批准人')
        return;
      }
      this.uploading = true
      this.$refs.upload1.submit();
    },
    // 关闭导入弹框
    closeUploadDia() {
      this.uploadDia = false;
      this.recordUserIds = []
      this.ratifyUserId = ''
      this.superviseYear = ''
      this.getYearPlanList()
    },
    // 打开报告弹框
    record(row) {
      this.uploadDia = true
      this.getUserList()
    },
    getUserList() {
      selectUserCondition({ type: 2 }).then((res) => {
        this.userList = res.data;
      })
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
      ratifyQualitySupervise(this.ratifyInfo).then(res => {
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
    // 导出
    handleDown(row) {
      exportQualitySupervise({ superviseId: row.superviseId }).then(res => {
        try {
          this.outLoading = false
          const blob = new Blob([res], { type: 'application/msword' });
          this.$download.saveAs(blob, row.superviseName + '.docx')
        } catch (error) {
          console.error('创建Blob对象时出错:', error);
        }
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
        delQualitySupervise({ superviseId: row.superviseId }).then(res => {
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
      this.superviseId = row.superviseId
      this.getYearDetailPlan()
    },
    getYearDetailPlan () {
      this.yearDetailPage.current = 1
      this.yearDetailPage.size = 20
      this.getYearDetailPlanList()
    },
    // 获取年度明细表
    getYearDetailPlanList() {
      const entity = {
        superviseId: this.superviseId,
        supervisePurpose: this.yearDetailForm.supervisePurpose,
        superviseProject: this.yearDetailForm.superviseProject,
        causeType: this.yearDetailForm.causeType,
      }
      const page = this.yearDetailPage
      this.yearDetailLoading = true
      pageQualitySuperviseDetail({ ...entity, ...page }).then(res => {
        this.yearDetailLoading = false
        this.yearDetailTableData = res.data.records
        this.yearDetailPage.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.yearDetailLoading = false
      })
    },
    pagination1(page) {
      this.yearDetailPage.size = page.limit;
      this.getYearDetailPlanList();
    },
    // 重置明细表
    clearDetail() {
      this.yearDetailForm.supervisePurpose = ''
      this.yearDetailForm.superviseProject = ''
      this.getYearDetailPlanList()
    },
    // 打开年度明细新增、修改弹框
    showDialog(type, row) {
      this.formDia = true
      this.$nextTick(() => {
        this.$refs.formDia.openDia(type, row, this.yearDetailForm.causeType)
      })
    },
    closeDia() {
      this.formDia = false
      this.getYearDetailPlanList()
    },
    // 记录流程
    records(row) {
      this.recordsDia = true
      this.$nextTick(() => {
        this.$refs.recordsDia.openDia(row)
      })
    },
    closeRecordsDia() {
      this.recordsDia = false
      this.getYearDetailPlanList()
    },
    // 不符合流程弹框
    processing(row) {
      this.processingDia = true
      this.$nextTick(() => {
        this.$refs.processingDia.openDia(row)
      })
    },
    closeProcessingDia() {
      this.processingDia = false
      this.getYearDetailPlanList()
    },
    // 纠正流程弹框
    rectify(row) {
      this.rectifyDia = true
      this.$nextTick(() => {
        this.$refs.rectifyDia.openDia(row)
      })
    },
    closeRectifyDia() {
      this.rectifyDia = false
      this.getYearDetailPlanList()
    },
    // 打开导出弹框
    openDownloadDia(row) {
      this.downloadDialog = true
      this.download = row
    },
    // 导出记录
    controlDown() {
      exportSuperviseDetailRecord({ superviseDetailsId: this.download.superviseDetailsId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '记录导出.docx')
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 处理单导出
    processingDown() {
      superviseDetailAccordingExport({ superviseDetailsId: this.download.superviseDetailsId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '不符合项导出.docx')
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 纠正单导出
    supervisoryDown() {
      exportSuperviseDetaillCorrect({ superviseDetailsCorrectId: this.download.superviseDetailsCorrectId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '监督纠正措施.docx')
      })
    },
    // 删除年度详情列表
    delYearPlanDetail(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.yearDetailLoading = true
        delQualitySuperviseDetail({ superviseDetailsId: row.superviseDetailsId }).then(res => {
          this.yearDetailLoading = false
          this.$message.success('删除成功')
          this.getYearDetailPlanList()
        }).catch(err => {
          this.yearDetailLoading = false
          console.log('err---', err);
        })
      })
    },
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/qualitySupervise/importQualitySupervise'
    },
    ...mapGetters(["userId"]),
  },
};
</script>

<style scoped>
.table-card {
  background-color: #ffffff;
}

.flex_column {
  display: flex;
  height: 80vh;
  flex-direction: column;
  overflow: auto;
  justify-content: space-between;
}

.pagination {
  display: flex;
  justify-content: space-between
}

.items_center {
  display: flex;
  align-items: center;
}

.date_box {
  margin: 0 5px;
}

.search {
  width: 150px;
  padding: 0 16px;
}
</style>
