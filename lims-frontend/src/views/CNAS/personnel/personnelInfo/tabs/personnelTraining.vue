<!-- 人员培训 -->
<template>
  <div class="flex_column">
    <div v-if="!editPlanShow && isDepartment">
      <div class="title">
        <span style="font-weight: bold">年度计划</span>
      </div>
      <div style="display: flex;justify-content: space-between">
        <div style="display: flex;">
          <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
            <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">编制人</span>
            <el-input size="small" placeholder="请输入" clearable v-model="page.compilerName"
                      @keyup.enter.native="getYearPlanList(departId)"></el-input>
          </div>
          <div style="line-height: 30px;">
            <el-button type="primary" size="mini" @click="getYearPlanList(departId)">查询</el-button>
          </div>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="uploadDia = true, getUserList()">导入</el-button>
        </div>
      </div>
      <lims-table :tableData="yearPlanTableData" :column="yearPlanColumn" :currentChange="currentChange"
        @pagination="pagination" height="40vh" :page="page" :tableLoading="yearLoading"></lims-table>
    </div>
    <div v-if="!editPlanShow" class="table">
      <div>
        <div class="title">
          <span style="font-weight: bold">年度计划明细</span>
        </div>
        <div style="display: flex;justify-content: space-between">
          <div style="display: flex;">
            <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
              <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">培训讲师</span>
              <el-input size="small" placeholder="请输入" clearable v-model="inDetailForm.trainingLecturerName"
                        @keyup.enter.native="searchTable"></el-input>
            </div>
            <div style="line-height: 30px;">
              <el-button size="mini" type="primary" @click="searchTable">查询</el-button>
            </div>
          </div>
          <div style="line-height: 30px;">
            <el-button v-if="isDepartment && currentChangeRow && isOperation" size="small"
                       @click="batchDelete">批量删除</el-button>
            <el-button v-if="isDepartment && currentChangeRow && isOperation" size="small" type="primary"
                       @click="addTrainingPlan('add')">新增</el-button>
          </div>
        </div>
        <lims-table :tableData="inDetailPlanTableData" :column="inDetailPlanColumn"
          :height="isDepartment ? '40vh' : '62vh'" :isSelection="true" :handleSelectionChange="handleSelectionChange"
          @pagination="pagination1" :page="inDetailPagination" :tableLoading="yearDetailLoading"></lims-table>
      </div>
    </div>
    <Add ref="addPlan" :currentChangeRow="currentChangeRow" @search="getInDetailPlan(currentRowId, departId)"></Add>
    <Edit v-if="editPlanShow" ref="editPlan" :currentRow="currentRow" @del="getInDetailPlan(currentRowId, departId)"
      @goBack="goBack"></Edit>
    <el-dialog :visible.sync="reviewDialog" title="审核" width="30%" @close="auditRemarks = ''">
      <span>
        审核备注：
        <el-input v-model="auditRemarks" type="textarea"></el-input>
      </span>
      <span style="margin-top: 10px;display: inline-block">
        批准人：
        <el-select v-model="approverId" clearable filterable size="small" style="width: 70%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="reviewLoading" @click="handleReview(2)">不通过</el-button>
        <el-button :loading="reviewLoading" type="primary" @click="handleReview(1)">通 过</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="approvalDialog" title="批准" width="30%" @close="approvalRemarks = ''">
      <span>
        批准备注：
        <el-input v-model="approvalRemarks" type="textarea"></el-input>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="approvalLoading" @click="handleApproval(2)">不批准</el-button>
        <el-button :loading="reviewLoading" type="primary" @click="handleApproval(1)">批 准</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="uploadDia" title="数据导入" width="500px">
      <div style="display: flex;align-items: center;">
        <div style="width: 70px"><span class="required-span">* </span>年份：</div>
        <el-date-picker v-model="planYear" type="year" value-format="yyyy" clearable size="small" format="yyyy"
          placeholder="选择年">
        </el-date-picker>
      </div>
      <div style="display: flex;align-items: center;margin: 10px 0">
        <div style="width: 70px"><span class="required-span">* </span>审核人：</div>
        <el-select v-model="reviewerId" clearable filterable size="small" style="width: 50%;">
          <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </div>
      <div style="margin: 0 auto;">
        <el-upload ref="upload"
          :action="javaApi + '/personTraining/personTrainingImport' + '?planYear=' + planYear + '&reviewerId=' + reviewerId"
          :auto-upload="false" :before-upload="beforeUpload" :file-list="fileList" :headers="uploadHeader" :limit="1"
          :on-error="onError" :on-success="onSuccess" accept=".xlsx" drag name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitUpload()">上 传</el-button>
      </span>
    </el-dialog>
    <view-record v-if="ViewRecord" ref="ViewRecord"></view-record>
  </div>
</template>

<script>
import Add from '../components/AddInDetail.vue';
import Edit from '../components/Edit.vue';
import ViewRecord from "../components/ViewRecord.vue";
import limsTable from "@/components/Table/lims-table.vue";
import { mapGetters } from "vuex";
import {
  approveAnnualPersonnelTraining, deleteAnnualPlanDetailTable, exportPersonTraining, exportPersonTrainingRecord,
  personTraining,
  personTrainingDelete,
  queryTheAnnualPlanDetailsTable, reviewAnnualPersonnelTraining
} from "@/api/cnas/personal/personalTraining";
import { selectUserCondition } from "@/api/system/user";

export default {
  name: 'PersonnelTraining',
  components: { limsTable, ViewRecord, Add, Edit },
  props: {
    departId: {
      type: Number,
      default: () => {
        return null;
      }
    },
    isDepartment: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  data() {
    return {
      planYear: '',
      reviewerId: '',
      responsibleOptions: [],
      search: {},
      superviseForm: {},
      inDetailForm: {
        trainingLecturerName: '',
        trainingDate: '',
      },
      yearLoading: false,
      yearDetailLoading: false,
      yearPlanTableData: [], // 年度计划表数据
      yearPlanColumn: [
        {
          label: '文件名称',
          width: '160px',
          prop: 'fileName'
        }, {
          label: '创建时间',
          width: '160px',
          prop: 'createTime'
        }, {
          label: '编制人',
          prop: 'compilerName'
        }, {
          label: '编制日期',
          width: '160px',
          prop: 'compilationDate'
        }, {
          label: '审核人',
          prop: 'reviewerName',
          minWidth: '100px',
        }, {
          dataType: 'tag',
          label: '审核状态',
          prop: 'reviewerStatus',
          width: '100px',
          formatData: (params) => {
            if (params == 1) {
              return '通过';
            } else if (params == 2) {
              return '不通过';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success';
            } else if (params == 2) {
              return 'danger';
            } else {
              return null
            }
          }
        }, {
          label: '审核备注',
          prop: 'auditRemarks',
          minWidth: '100px',
        }, {
          label: '审核日期',
          width: '160px',
          prop: 'auditDate'
        }, {
          label: '批准人',
          prop: 'approverName',
          minWidth: '100px',
        }, {
          label: '批准备注',
          prop: 'approvalRemarks',
          minWidth: '100px',
        }, {
          dataType: 'tag',
          label: '批准状态',
          minWidth: '100px',
          prop: 'approvalStatus',
          formatData: (params) => {
            if (params == 1) {
              return '批准';
            } else if (params == 2) {
              return '不批准';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success';
            } else if (params == 2) {
              return 'danger';
            } else {
              return null
            }
          }
        }, {
          label: '批准日期',
          width: '160px',
          prop: 'approvalDate'
        }, {
          dataType: 'action',
          width: '180px',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '导出',
              type: 'text',
              disabled: (row) => {
                if (row.approvalStatus === 1 && row.reviewerStatus === 1) {
                  return false;
                } else {
                  return true;
                }
              },
              clickFun: (row) => {
                this.downLoadPost(row);
              }
            },
            {
              name: '审核',
              type: 'text',
              disabled: (row) => {
                if (row.reviewerStatus === 1 || this.userId != row.reviewerId) {
                  return true;
                } else {
                  return false;
                }
              },
              clickFun: (row) => {
                this.handleCheck(row.id);
              }
            },
            {
              name: '批准',
              type: 'text',
              disabled: (row) => {
                if (row.approvalStatus === 1 || this.userId != row.approverId || row.reviewerStatus != 1) {
                  return true;
                } else {
                  return false;
                }
              },
              clickFun: (row) => {
                this.handleApprove(row.id);
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.deleteFun(row.id);
              },
              disabled: (row) => {
                if (row.reviewerStatus === 1) {
                  return true;
                } else {
                  return false;
                }
              },
            }
          ]
        }],
      inDetailPlanTableData: [], // 年度计划明细表表数据
      inDetailPlanColumn: [
        {
          label: '培训目标',
          prop: 'trainingObjectives',
          width: '100px',
        }, {
          label: '培训内容',
          prop: 'trainingContent',
          width: '100px',
        }, {
          label: '培训方式',
          prop: 'trainingMode',
          width: '100px',
        }, {
          dataType: 'tag',
          label: '课程状态',
          width: '100px',
          prop: 'state',
          formatData: (params) => {
            if (params == 1) {
              return '已完成';
            } else if (params == 2) {
              return '待评价';
            } else if (params == 3) {
              return '未开始';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success';
            } else if (params == 2) {
              return 'warning';
            } else if (params == 3) {
              return 'primary';
            } else if (params == 4) {
              return 'info';
            } else {
              return null
            }
          }
        }, {
          label: '参加对象',
          prop: 'participants',
          width: '100px',
        }, {
          label: '举办部门',
          prop: 'holdingDepartment',
          width: '100px',
        }, {
          label: '培训讲师',
          prop: 'trainingLecturerName',
          width: '100px',
        }, {
          label: '培训日期',
          prop: 'trainingDate',
          width: '100px',
        }, {
          label: '开始时间',
          prop: 'openingTime',
          width: '100px',
        }, {
          label: '课时',
          prop: 'classHour',
          width: '100px',
        }, {
          label: '备注',
          prop: 'remarks',
          width: '100px',
        }, {
          dataType: 'action',
          width: '200',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.addTrainingPlan('edit', row);
              },
            },
            {
              name: '结果明细',
              type: 'text',
              clickFun: (row) => {
                this.editInDetail(row);
              },
              showHide: () => {
                if (this.isDepartment) {
                  return true;
                } else {
                  return false;
                }
              }
            },
            {
              name: '附件',
              type: 'text',
              clickFun: (row) => {
                this.viewRecord(row);
              },
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.downLoadInDetail(row);
              }
            },

          ]
        }],
      page: {
        total: 0,
        size: 10,
        current: 1,
        compilerName: ""
      },
      inDetailPagination: {
        total: 0,
        size: 10,
        current: 1,
      },
      editPlanShow: false,
      currentRow: {},
      currentRowId: null, // 当前选中数据的id
      auditRemarks: '', // 审核备注
      reviewDialog: false, // 审核弹框
      reviewLoading: false, // 审核提交按钮
      approvalRemarks: '', // 审核备注
      approvalDialog: false, // 审核弹框
      approvalLoading: false, // 审核提交按钮
      multipleSelection: [], // 年度明细表选中的数据
      uploadDia: false,
      uploading: false,
      isOperation: false,
      ViewRecord: false,
      fileList: [],
      currentChangeRow: {},
      approverId: ''
    };
  },
  mounted() {
    if (this.isDepartment) {
      this.getYearPlanList(this.departId)
    } else {
      this.getInDetailPlan('', this.departId)
    }
  },
  methods: {
    searchTable() {
      this.getInDetailPlan(this.currentRowId)
    },
    // 查询-年度计划表
    getYearPlanList(userId) {
      this.yearLoading = true
      const params = this.isDepartment ?
        {
          departmentId: userId,
          size: this.page.size,
          current: this.page.current,
          compilerName: this.page.compilerName,
        } : {
          userId: userId,
          size: this.page.size,
          current: this.page.current,
          compilerName: this.page.compilerName,
        }
      personTraining(params).then(res => {
        this.yearLoading = false
        this.yearPlanTableData = res.data.records;
        this.pagination.total = res.data.total;
        if (this.yearPlanTableData.length > 0) {
          this.currentRowId = this.yearPlanTableData[0].id
          this.currentChange(this.yearPlanTableData[0])
        }
      }).catch(err => {
        this.yearLoading = false
      })
    },
    pagination(page) {
      this.page.size = page.limit
      this.getYearPlanList()
    },
    currentChange(row) {
      const now = new Date();
      const currentYear = now.getFullYear();
      if (row) {
        this.currentChangeRow = row
        this.currentRowId = row.id
        if (row.createTime.slice(0, 4) == currentYear) {
          this.isOperation = true;
        } else {
          this.isOperation = false;
        }
        this.getInDetailPlan(row.id)
      }
    },
    getInDetailPlan(id) {
      if (this.inDetailForm.trainingDate === null) {
        this.inDetailForm.trainingDate = ''
      }
      const userId = this.isDepartment ? '' : this.departId
      const params =
      {
        userId: userId,
        size: this.inDetailPagination.pageSize,
        current: this.inDetailPagination.current,
        id: id,
        trainingLecturerName: this.inDetailForm.trainingLecturerName,
        trainingDate: this.inDetailForm.trainingDate,
      }
      this.yearDetailLoading = true
      queryTheAnnualPlanDetailsTable(params).then(res => {
        this.yearDetailLoading = false
        this.inDetailPlanTableData = res.data.records;
        this.inDetailPagination.total = res.data.total;
      }).catch(err => {
        this.yearDetailLoading = false
      })
    },
    pagination1(page) {
      this.inDetailPagination.size = page.limit
      this.getInDetailPlan(this.currentRowId)
    },
    // 新增年度计划明细表
    addTrainingPlan(type, row) {
      if (!this.currentRowId) {
        this.$message.warning('请选择一条计划进行新增')
        return
      }
      this.$refs.addPlan.showDialog(this.currentRowId, type, row);
    },
    // 年度计划表-删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        personTrainingDelete({ id: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearPlanList(this.departId);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 年度计划表-审核
    handleCheck(id) {
      this.currentRowId = id
      this.reviewDialog = true
      this.getUserList()
    },
    // 提交审核
    handleReview(status) {
      const personTrainingUpdateDto = {
        id: this.currentRowId,
        auditRemarks: this.auditRemarks,
        reviewerStatus: status,
        approverId: this.approverId
      }
      this.reviewLoading = true
      reviewAnnualPersonnelTraining(personTrainingUpdateDto).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.reviewDialog = false
          this.getYearPlanList(this.departId);
        }
        this.reviewLoading = false
      }).catch(() => {
        this.reviewLoading = false
      })
    },
    // 年度计划表-批准
    handleApprove(id) {
      this.currentRowId = id
      this.approvalDialog = true
    },
    // 提交批准
    handleApproval(status) {
      const personTrainingUpdateDto = {
        id: this.currentRowId,
        approvalRemarks: this.approvalRemarks,
        approvalStatus: status
      }
      this.approvalLoading = true
      approveAnnualPersonnelTraining(personTrainingUpdateDto).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.getYearPlanList(this.departId);
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    // 年度计划表-下载
    downLoadPost(row) {
      exportPersonTraining({ id: row.id }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, row.fileName + '.docx')
      })
    },
    // 年度计划-导入
    submitUpload() {
      if (!this.planYear) {
        this.$message.error('请选择年份')
        return
      }
      if (!this.reviewerId) {
        this.$message.error('请选择审核人')
        return
      }
      if (this.$refs.upload.uploadFiles.length == 0) {
        this.$message.error('未选择文件')
        return
      }
      this.uploading = true
      this.$refs.upload.submit();
      this.uploading = false
    },
    onSuccess(response, file, fileList) {
      this.$refs.upload.clearFiles()
      this.uploadDia = false
      this.uploading = false
      if (response.code != 200) {
        this.$message.error(response.msg)
        return
      }
      this.$message.success('上传成功')
      this.standardList = []
      this.productList = []
      this.getYearPlanList(this.departId)
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
      this.uploading = false
    },
    beforeUpload(file, fileList) {
      if (file.type != 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        this.$message.error('上传文件格式不正确');
        this.$refs.upload.clearFiles()
        return false;
      }
    },
    // 年度计划明细表-编辑
    editInDetail(row) {
      this.editPlanShow = true;
      this.currentRow = row
    },
    //
    goBack() {
      this.editPlanShow = false;
      this.getInDetailPlan(this.currentRowId)
    },
    viewRecord(row) {
      this.ViewRecord = true
      this.$nextTick(() => {
        this.$refs.ViewRecord.openDia(row)
      })
    },
    // 年度计划明细表-下载
    downLoadInDetail(row) {
      exportPersonTrainingRecord({ id: row.id }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '人员培训与考核记录.docx')
      })
    },
    // 年度计划明细表-多选
    handleSelectionChange(list) {
      this.multipleSelection = list
    },
    // 年度明细表-删除
    batchDelete() {
      if (this.multipleSelection.length > 0) {
        let ids = []
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
        this.$confirm('是否确认删除所选择的数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteAnnualPlanDetailTable({ ids: ids.join(',') }).then(res => {
            if (res.code == 200) {
              this.$message.success('删除成功');
              this.getInDetailPlan(this.currentRowId);
            }
          });
        }).catch(() => {
          this.$message.warning('取消删除');
        });
      } else {
        this.$message.warning('请选择需要删除的数据')
      }
    },
    handleSizeChange(val) {
      this.inDetailPagination.size = val;
      this.getInDetailPlan(this.currentChangeRow.id)
    },
    handleCurrentChange(val) {
      this.inDetailPagination.current = val;
      this.getInDetailPlan(this.currentChangeRow.id)
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      });
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    departId: {
      handler(newId, oldId) {
        if (this.isDepartment) {
          this.getYearPlanList(newId);
        } else {
          this.getInDetailPlan('')
        }
      }
    },
    currentRowId: {
      handler(newId, oldId) {
        if (newId) {
          console.log('newId', newId);
          this.inDetailPagination.current = 1
          this.getInDetailPlan(this.currentChangeRow.id)
        }
      }
    }
  }
}
</script>
<style scoped>
.flex_column {
  display: flex;
  height: 80vh;
  flex-direction: column;
  overflow: auto;
  justify-content: space-between;
}

.search {
  width: 150px;
  margin: 0 16px;
}

.title {
  position: relative;
  font-size: 16px;
  color: #333;
  font-weight: 400;
  padding-left: 10px;
  margin-bottom: 10px;
}

.title::before {
  position: absolute;
  left: 0;
  top: 4px;
  content: '';
  width: 4px;
  height: 16px;
  background-color: #3A7BFA;
  border-radius: 2px;
}
</style>
