<!-- 人员能力 -->
<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">姓名</span>
          <el-input size="small" placeholder="请输入" clearable v-model="userName"
                    @keyup.enter.native="refreshTable"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button v-if="!isDepartment" size="small" type="primary" @click="addAppointPost('add')">新增</el-button>
      </div>
    </div>
    <lims-table :tableData="tableData" :column="yearColumnData"
                @pagination="pagination" :height="'calc(100vh - 20em)'"
                :page="search" :tableLoading="yearLoading">
      <div slot="jobResponsibilities" slot-scope="scope">
        <div v-html="changeLine(scope.row.responsibilities)"></div>
      </div>
      <div slot="placeWorkSlot" slot-scope="scope">
        <div v-html="changeLine(scope.row.placeWork)"></div>
      </div>
    </lims-table>
    <!--新增能力认定弹框-->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :title="title"
               :visible.sync="dialogVisible"
               width="80%" @close="resetForm">
      <el-form v-if="dialogVisible" ref="infoForm" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="人员：" label-width="110px" prop="userId">
            <el-select v-model="form.userId" disabled clearable
                       filterable size="small" style="width: 100%;">
              <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div style="display: flex;justify-content: space-evenly;font-weight: 600">
            <span>查核结果</span>
            <span>符合与否</span>
            <span>备注</span>
          </div>
        </el-col>
        <el-col :span="8">
          <el-form-item label="学历：" label-width="110px" prop="academicDegree">
            <el-input v-model="form.academicDegree" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="academicConformNot" style="text-align: center">
            <el-radio-group v-model="form.academicConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="academicRemarks">
            <el-input v-model="form.academicRemarks" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="相关年限：" label-width="110px" prop="relatedYears">
            <el-input v-model="form.relatedYears" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="relatedYearsConformNot" style="text-align: center">
            <el-radio-group v-model="form.relatedYearsConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="relatedYearsRemarks	">
            <el-input v-model="form.relatedYearsRemarks	" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="相关培训：" label-width="110px" prop="relatedTraining">
            <el-input v-model="form.relatedTraining" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="relatedTrainingConformNot" style="text-align: center">
            <el-radio-group v-model="form.relatedTrainingConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="relatedTrainingRemarks">
            <el-input v-model="form.relatedTrainingRemarks" :disabled="operationType === 'view' || operationType === 'confirm'"
                      size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="相关经验：" label-width="110px" prop="relevantExperience">
            <el-input v-model="form.relevantExperience" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="relevantExperienceConformNot" style="text-align: center">
            <el-radio-group v-model="form.relevantExperienceConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="relevantExperienceRemarks">
            <el-input v-model="form.relevantExperienceRemarks" :disabled="operationType === 'view' || operationType === 'confirm'"
                      size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="上岗证：" label-width="110px" prop="workLicense">
            <el-input v-model="form.workLicense" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"
                      style="width: 100%">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="workLicenseConformNot" style="text-align: center">
            <el-radio-group v-model="form.workLicenseConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="workLicenseRemarks">
            <el-input v-model="form.workLicenseRemarks" :disabled="operationType === 'view' || operationType === 'confirm'" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="岗位职责：" label-width="110px" prop="jobResponsibilitiesTem" style="height: 450px">
            <el-checkbox-group v-model="form.jobResponsibilitiesTem" :disabled="operationType === 'view' || operationType === 'confirm'"
                               @change="selectResponsibilities">
              <el-checkbox v-for="city in dict.type.responsibilities_list" :key="city.value" :label="city.label"
                           :value="city.value"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="radio-group" prop="jobResponsibilitiesConformNot"
                        style="text-align: center;height: 420px">
            <el-radio-group v-model="form.jobResponsibilitiesConformNot" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio :label="1">符合</el-radio>
              <el-radio :label="2">不符合</el-radio>
              <el-radio :label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="jobResponsibilitiesRemarks" style="height: 420px">
            <el-input v-model="form.jobResponsibilitiesRemarks" :disabled="operationType === 'view' || operationType === 'confirm'"
                      size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <span style="padding-left: 110px;font-size: 14px">备注：岗位职责达到5项及5项以上为符合</span>
        </el-col>
        <el-col :span="16">
          <el-form-item label="综合评价：" label-width="110px" prop="comprehensiveAssessment">
            <el-radio-group v-model="form.comprehensiveAssessment" :disabled="operationType === 'view' || operationType === 'confirm'" v-removeAriaHidden>
              <el-radio label="Qualified this position">可胜任该岗位</el-radio>
              <el-radio label="You can work while training">可边培训边上岗</el-radio>
              <el-radio label="Iconpetent for the position">不胜任该岗位</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="confirmOperatingPersonnelId" label="确认人：">
            <el-select v-model="form.confirmOperatingPersonnelId" clearable :disabled="operationType === 'view' || operationType === 'confirm'"
                       filterable size="small">
              <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="confirmDate" label="确认时间：">
            <el-date-picker v-model="form.confirmDate"
                            format="yyyy-MM-dd"
                            :disabled="operationType === 'view' || operationType === 'confirm'"
                            placeholder="选择日期"
                            size="small"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            type="date"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="operationType !== 'view'" @click="resetForm">取消</el-button>
        <el-button v-if="operationType !== 'view' && operationType !== 'confirm'" type="primary" @click="submitForm">保存</el-button>
        <el-button v-if="operationType !== 'view' && operationType !== 'confirm'" type="primary" @click="submitForm1">提交并通知</el-button>
        <el-button v-if="operationType === 'confirm'" type="primary" @click="verifyGet">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import limsTable from "@/components/Table/lims-table.vue";
import {
  addOrUpdatePersonPersonnelCapacity,
  confirmPersonnelCapability,
  deletePersonPersonnelCapacity,
  exportPersonnelCapacity,
  personPersonnelCapacityPage,
  submitConfirmPersonnelCapability
} from "@/api/cnas/personal/personPersonnelCapacity";
import {selectUserCondition} from "@/api/cnas/resourceDemand/facilitiesEnvironment/facilitiesAndEnvironment";
import {mapGetters} from "vuex";

export default {
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
  components: {
    limsTable
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  dicts: ['responsibilities_list'],
  data() {
    return {
      userName: '',
      tableData: [],
      search: {
        size: 20,
        current: 1,
        total: 0
      },
      title: '新增能力认定',
      operationType: '',
      yearColumnData: [
        {
          label: '岗位',
          prop: 'postName',
          width: '100'
        }, {
          label: '姓名',
          prop: 'userName',
          width: '100'
        }, {
          label: '学历',
          prop: 'academicDegree',
          width: '100'
        }, {
          label: '专业',
          prop: 'major',
          width: '100'
        }, {
          label: '职称',
          prop: 'professionalTitle',
          width: '100'
        }, {
          dataType: 'slot',
          label: '岗位职责',
          prop: 'jobResponsibilities',
          width: '400',
          slot: 'jobResponsibilities'
        }, {
          dataType: 'tag',
          label: '综合评价',
          width: '140px',
          prop: 'comprehensiveAssessment',
          formatData: (params) => {
            if (params == 'Qualified this position') {
              return '可胜任该岗位';
            } else if (params == 'You can work while training') {
              return '可边培训边上岗';
            } else {
              return '不胜任该岗位';
            }
          },
          formatType: (params) => {
            if (params == 'Qualified this position') {
              return 'success';
            } else if (params == 'You can work while training') {
              return 'warning';
            } else {
              return 'danger';
            }
          }
        }, {
          label: '确认人',
          prop: 'confirmOperatingPersonnelName',
          width: '100'
        }, {
          label: '确认日期',
          prop: 'confirmDate',
          width: '160'
        }, {
          dataType: 'action',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.handleViewClick('edit', row);
              },
              disabled: (row) => {
                if (row.confirmDate) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.handleViewClick('view', row);
              }
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.downLoadPost(row);
              }
            },
            {
              name: '确认',
              type: 'text',
              clickFun: (row) => {
                this.handleViewClick('confirm', row);
              },
              disabled: (row) => {
                if (row.confirmDate || this.userId != row.confirmOperatingPersonnelId) {
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
                this.deletePost(row.id);
              },
              disabled: (row) => {
                if (row.confirmDate) {
                  return true
                } else {
                  return false
                }
              }
            }
          ]
        }],
      yearLoading: false,
      dialogVisible: false,
      verifyGetId: '',
      form: {
        jobResponsibilitiesTem: []
      },
      responsibleOptions: [],
      rules: {
        confirmOperatingPersonnelId: [{ required: true, message: '请选择确认人', trigger: 'change' }],
        confirmDate: [{ required: true, message: '请选择确认时间', trigger: 'change' }],
        userId: [{ required: true, message: '请选择人员', trigger: 'change' }],
        academicDegree: [{ required: true, message: '请输入学历查核结果', trigger: 'blur' }],
        academicConformNot: [{ required: true, message: '请选择学历符合与否', trigger: 'change' }],
        relatedYears: [{ required: true, message: '请输入相关年限查核结果', trigger: 'blur' }],
        relatedYearsConformNot: [{ required: true, message: '请选择相关年限符合与否', trigger: 'change' }],
        relatedTraining: [{ required: true, message: '请输入相培训限查核结果', trigger: 'blur' }],
        relatedTrainingConformNot: [{ required: true, message: '请选择相关培训符合与否', trigger: 'change' }],
        relevantExperience: [{ required: true, message: '请输入相关经验查核结果', trigger: 'blur' }],
        relevantExperienceConformNot: [{ required: true, message: '请选择相关经验符合与否', trigger: 'change' }],
        workLicense: [{ required: true, message: '请输入上岗证查核结果', trigger: 'blur' }],
        workLicenseConformNot: [{ required: true, message: '请选择上岗证符合与否', trigger: 'change' }],
        jobResponsibilitiesTem: [{ required: true, message: '请选择岗位职责查核结果', trigger: 'change' }],
        jobResponsibilitiesConformNot: [{ required: true, message: '请选择岗位职责符合与否', trigger: 'change' }],
        comprehensiveAssessment: [{ required: true, message: '请选择综合评价', trigger: 'change' }]
      },
      responsibilities: []
    };
  },
  mounted() {
    this.getList(this.departId);
  },
  methods: {
    // 下载
    downLoadPost(row) {
      exportPersonnelCapacity({id: row.id}).then(res => {
        this.outLoading = false
        const blob = new Blob([res],{ type: 'application/msword' });
        this.$download.saveAs(blob, '人员能力导出.docx')
      })
    },
    // 查询
    refreshTable() {
      this.getList(this.departId);
    },
    // 获取人员能力列表信息
    getList(userId) {
      this.search.userId = userId;
      const params = this.isDepartment ?
        {
          departmentId: this.search.userId,
          size: this.search.size,
          current: this.search.current,
          userName: this.userName,
        }: {
          userId: this.search.userId,
          size: this.search.size,
          current: this.search.current,
          userName: this.userName,
        }
      this.yearLoading = true
      personPersonnelCapacityPage(params).then(res => {
        this.yearLoading = false
        this.tableData = res.data.records;
        this.search.total = res.data.total;
      }).catch(err => {
        console.log(err);
        this.yearLoading = false
      })
    },
    pagination (page) {
      this.search.size = page.limit
      this.refreshTable()
    },
    selectResponsibilities(arr) {
      let arrTem = [];
      arr.map(val => {
        const index = this.dict.type.responsibilities_list.findIndex(item => item.label === val);
        if (index > -1) {
          arrTem.push(this.dict.type.responsibilities_list[index].value);
        }
      });
      this.form.jobResponsibilities = arrTem.join(',');
    },
    changeLine (val) {
      if(val) {
        return val.replace(/,/g, '<br>')
      } else {
        return
      }
    },
    // 新增
    addAppointPost(type) {
      this.operationType = type;
      this.title = '新增能力认定'
      this.dialogVisible = true;
      this.form = {
        jobResponsibilitiesTem: []
      }
      this.form.userId = this.departId
      this.getUserList();

    },
    // 编辑/查看
    handleViewClick(type, row) {
      this.operationType = type;
      this.verifyGetId = row.id
      this.title = this.operationType === 'edit' ? '修改能力认定' : '查看能力认定'
      this.dialogVisible = true;
      this.form = {...row}
      if(this.form.responsibilities) {
        this.$set(this.form, 'jobResponsibilitiesTem', this.form.responsibilities.split(","))
      } else {
        this.$set(this.form, 'jobResponsibilitiesTem', [])
      }
      this.getUserList();
    },
    // 保存
    submitForm() {
      this.$refs.infoForm.validate((valid) => {
        if (valid) {
          addOrUpdatePersonPersonnelCapacity(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功');
              this.getList(this.departId);
              this.dialogVisible = false;
            }
          });
        }
      });
    },
    // 提交并通知
    submitForm1() {
      this.$refs.infoForm.validate((valid) => {
        if (valid) {
          submitConfirmPersonnelCapability(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功');
              this.getList(this.departId);
              this.dialogVisible = false;
            }
          });
        }
      });
    },
    verifyGet () {
      confirmPersonnelCapability({id: this.verifyGetId}).then(res => {
        if (res.code == 200) {
          this.$message.success('确认成功');
          this.resetForm()
          this.getList(this.departId);
        }
      });
    },
    // 删除岗位职责
    deletePost(id) {
      this.$confirm('此操作将永久删除此数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePersonPersonnelCapacity({id: id}).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功');
            this.getList(this.departId);
          }
        });
      }).catch(() => {
        this.$message.error('删除失败');
      });
    },
    resetForm() {
      this.$refs.infoForm.resetFields();
      this.dialogVisible = false;
    },
    getUserList(){
      selectUserCondition({type: 2}).then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    departId: {
      handler(newId, oldId) {
        this.getList(newId);
      }
    },
  }
};
</script>
<style scoped>
>>> .el-dialog__body {
  max-height: 74vh;
  overflow-y: auto;
}

.radio-group >>> .el-form-item__error {
  padding-left: 38px;
}
>>>.el-checkbox__label {
  width: 212px;
  white-space: pre-wrap;
}
</style>
