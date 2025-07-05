<!-- 培训记录 -->
<template>
  <div>
    <div class="flex_table">
      <div v-if="isDepartment" style="width: 49%">
        <div class="title">
          <span style="font-weight: bold">年度计划</span>
        </div>
        <div style="display: flex;justify-content: space-between">
          <div style="display: flex;">
            <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
              <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">姓名</span>
              <el-input v-model="trainingPagination.userName" class="search"
                        @keyup.enter.native="getPersonnelTraining(departId)"
                        clearable placeholder="请输入" size="small"></el-input>
            </div>
            <div style="line-height: 30px;">
              <el-button size="small" type="primary" @click="getPersonnelTraining(departId)">查询</el-button>
            </div>
          </div>
        </div>
        <lims-table :tableData="trainingTableData" :column="trainingColumn"
                    ref="trainingTableData"
                    :currentChange="currentChange" :highlightCurrentRow="true"
                    @pagination="pagination" :height="'calc(100vh - 22em)'"
                    :page="trainingPagination" :tableLoading="trainingLoading"></lims-table>
      </div>
      <div :style="`width: ${isDepartment ? '49%' : '100%'};`">
        <div class="title">
          <span style="font-weight: bold">年度计划明细</span>
        </div>
        <div style="display: flex;justify-content: space-between;">
          <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
            <el-form-item label="年份">
              <el-date-picker
                v-model="searchForm.trainingDate"
                clearable
                format="yyyy"
                placeholder="选择年"
                @change="queryPersonnelDetailsPage(currentChangeRow.userId)"
                size="small"
                type="year"
                value-format="yyyy">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="small" type="primary" @click="queryPersonnelDetailsPage(currentChangeRow.userId)">查询</el-button>
            </el-form-item>
          </el-form>
          <div>
            <el-button size="small" type="primary" @click="openDownloadDia(currentChangeRow)">导出</el-button>
          </div>
        </div>
        <lims-table :tableData="trainingPersonTableData" :column="trainingPersonColumn"
                    :height="'calc(100vh - 22em)'" @pagination="pagination1"
                    :page="trainingPersonPagination" :tableLoading="trainingPersonLoading"></lims-table>
      </div>
    </div>
  </div>
</template>

<script>

import limsTable from "@/components/Table/lims-table.vue";
import {
  exportTrainingRecord,
  queryPersonnelDetails,
  trainingSelectTrainingRecord
} from "@/api/cnas/personal/personTrainingRecord";

export default {
  components: {limsTable},
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
  data() {
    return {
      outLoading: false,
      trainingForm: {},
      trainingColumn: [
        {
          label: '员工编号',
          prop: 'account',
          width: '100'
        }, {
          label: '姓名',
          prop: 'name'
        }, {
          label: '所在部门',
          prop: 'departLimsName',
          width: '150'
        }, {
          label: '职称',
          prop: 'professionalTitle'
        }, {
          label: '最高学历',
          prop: 'officialAcademicRedentials',
          width: '100'
        }, {
          label: '入单位时间',
          prop: 'unitTime',
          width: '150'
        },
        // {
        //   fixed: 'right',
        //   dataType: 'action',
        //   width: 80,
        //   label: '操作',
        //   operation: [
        //     {
        //       name: '导出',
        //       type: 'text',
        //       clickFun: (row) => {
        //         this.openDownloadDia(row)
        //       }
        //     }
        //   ]
        // }
      ],
      trainingTableData: [],
      trainingLoading: false,
      trainingPagination: {
        size: 20,
        total: 0,
        current: 1,
        userName: null
      },
      searchForm: {
        trainingDate: ''
      },
      trainingPersonColumn: [
        {
          label: '培训日期',
          prop: 'trainingDate'
        }, {
          label: '培训内容',
          prop: 'trainingContent'
        }, {
          label: '课时',
          prop: 'classHour'
        }, {
          label: '培训结果',
          prop: 'examinationResults',
        }, {
          label: '备注',
          prop: 'remarks'
        }
      ],
      trainingPersonTableData: [],
      trainingPersonLoading: false,
      trainingPersonPagination: {
        size: 20,
        current: 1,
        total: 0
      },
      currentChangeRow: '',
    };
  },
  mounted() {
    this.getPersonnelTraining(this.departId);
  },
  methods: {
    // exportExcel() {
    //   this.outLoading = true;
    //   const name = this.isDepartment ? 'departmentId' : 'userId';
    //   this.$axios.get(this.$api.personal.personTrackRecordExport + `&${name}=` + this.departId, { responseType: 'blob' }).then(res => {
    //     this.outLoading = false;
    //     this.$message.success('导出成功');
    //     const blob = new Blob([res], { type: 'application/octet-stream' });
    //     const url = URL.createObjectURL(blob);
    //     const link = document.createElement('a');
    //     link.href = url;
    //     link.download = '培训记录.xlsx';
    //     link.click();
    //   }).catch(err => {
    //     this.outLoading = false;
    //   })
    // },
    // 查询
    refreshTable() {
      this.getPersonnelTraining(this.departId);
    },
    // 培训记录导出
    openDownloadDia (row) {
      let date = this.searchForm.trainingDate
      if (!date) {
        date = this.$moment().format('YYYY')
      }
      exportTrainingRecord({userId: row.userId, trainingDate: date}).then(res => {
        this.outLoading = false
        const blob = new Blob([res],{ type: 'application/msword' });
        this.$download.saveAs(blob, '培训记录导出.docx');
      })
    },
    // 获取实验室-培训计划列表信息
    getPersonnelTraining(departId) {
      const params = {
        departmentId: departId,
        size: this.trainingPagination.size,
        current: this.trainingPagination.current,
        userName: this.trainingPagination.userName,
      }
      this.trainingLoading = true
      trainingSelectTrainingRecord(params).then(res => {
        this.trainingLoading = false
        this.trainingTableData = res.data.records;
        this.trainingPagination.total = res.data.total;
        if (this.trainingTableData.length > 0) {
          this.$refs.trainingTableData.setCurrentRow(this.trainingTableData[0])
        }
      }).catch(err => {
        this.trainingLoading = false
      })
    },
    // 获取个人-培训计划列表信息
    currentChange(row) {
      this.currentChangeRow = row
      if (row === null) {
        row = this.trainingTableData[0]
      }
      this.queryPersonnelDetailsPage(row.userId)
    },
    queryPersonnelDetailsPage(userId) {
      if (this.searchForm.trainingDate === null) {
        this.searchForm.trainingDate = ''
      }
      const params = {
        userId: userId,
        size: this.trainingPersonPagination.size,
        current: this.trainingPersonPagination.current,
        trainingDate: this.searchForm.trainingDate,
      }
      this.trainingPersonLoading = true
      queryPersonnelDetails(params).then(res => {
        this.trainingPersonLoading = false
        this.trainingPersonTableData = res.data.records;
        this.trainingPersonPagination.total = res.data.total;
      }).catch(err => {
        this.trainingPersonLoading = false
      })
    },
    // 分页
    pagination(page) {
      this.trainingPagination.size = page.limit;
      this.getPersonnelTraining(this.departId);
    },
    // 分页
    pagination1(page) {
      this.trainingPersonPagination.size = page.limit;
      this.queryPersonnelDetailsPage(this.currentChangeRow.userId);
    },
  },
  watch: {
    departId: {
      handler(newId, oldId) {
        if (this.isDepartment) {
          this.getPersonnelTraining(newId);
        } else {
          this.queryPersonnelDetailsPage(newId)
        }
      }
    }
  }
};
</script>
<style scoped>
.flex_table {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.search {
  width: 150px;
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
