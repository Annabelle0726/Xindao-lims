<!-- 岗位职责 -->
<template>
  <div class="view">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 50px;font-size: 14px;font-weight: 700;color: #606266;">员工</span>
          <el-input size="small" placeholder="请输入" clearable v-model="userName"
                    @keyup.enter.native="refreshTable"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button size="small" type="primary" @click="refreshTable">查询</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" @click="addPost">新增</el-button>
        <!--        <el-button size="small" type="primary">导出excel</el-button>-->
      </div>
    </div>
    <div class="table">
      <el-table :data="tableData" v-loading="tableLoading" height="66.5vh" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column label="序号" type="index" width="60"></el-table-column>
        <el-table-column label="员工编号" min-width="120" prop="account"></el-table-column>
        <el-table-column label="岗位名称" min-width="180" prop="postName"></el-table-column>
        <el-table-column label="所属部门" min-width="180" prop="departLimsName"></el-table-column>
        <el-table-column label="工作目标" min-width="180" prop="jobObjective"></el-table-column>
        <el-table-column label="岗位职责" min-width="180" prop="jobResponsibilities"></el-table-column>
        <el-table-column label="任职人" min-width="180" prop="incumbentName"></el-table-column>
        <el-table-column label="任职人审核日期" min-width="180" prop="incumbentDate"></el-table-column>
        <el-table-column label="主管" min-width="180" prop="supervisorName"></el-table-column>
        <el-table-column label="主管审核日期" min-width="180" prop="supervisorDate"></el-table-column>
        <el-table-column fixed="right" label="操作" width="140" align="center">
          <template v-slot="scope">
            <el-button v-if="!isDepartment || scope.row.currentState === '关闭'" size="small" type="text"
              @click="handleViewClick(scope.row, 'view')">查看
            </el-button>
            <el-button v-if="isDepartment && scope.row.currentState !== '关闭'" size="small" type="text"
              @click="handleViewClick(scope.row, 'edit')">编辑
            </el-button>
            <el-button size="small" type="text" @click="downLoadPost(scope.row)">导出</el-button>
            <el-button v-if="isDepartment" size="small" style="color: #f56c6c" type="text"
              @click="deletePost(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" background style="margin-top: 10px"
        @size-change="handleSizeChange" @current-change="handleCurrentChange">
      </el-pagination>
    </div>
    <!-- 新增岗位职责 -->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible" title="新增岗位职责"
      width="50%" @close="resetForm">
      <el-steps :active="currentStep" align-center finish-status="success">
        <el-step v-for="(v, i) in steps" :key="i" :title="v" style="cursor:pointer"
          @click.native="choiceStep(i)"></el-step>
      </el-steps>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <div>
          <el-card style="margin-top: 1em; height: 40vh; overflow-y: scroll;">
            <!-- 新增设备事记录卡片 -->
            <el-row>
              <el-col :span="12">
                <el-form-item label="岗位名称：" prop="postName">
                  <el-input v-model="form.postName" :disabled="currentStep !== 0 || operationType === 'view'"
                    size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="工作目标：" prop="jobObjective">
                  <el-input v-model="form.jobObjective" :disabled="currentStep !== 0 || operationType === 'view'"
                    size="small" type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="岗位职责：" prop="jobResponsibilities">
                  <el-input v-model="form.jobResponsibilities" :disabled="currentStep !== 0 || operationType === 'view'"
                    size="small" type="textarea"></el-input>
                </el-form-item>
              </el-col>
              <el-col v-if="currentStep === 0 || operationType === 'view'" :span="12">
                <el-form-item :rules="[{ required: currentStep === 0, message: '请选择任职人', trigger: 'change' }]"
                  label="任职人：" prop="incumbentId">
                  <el-select v-model="form.incumbentId" :disabled="operationType === 'view'" clearable filterable
                    placeholder="请选择任职人" size="small" style="width: 100%;">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col v-if="currentStep === 1 || operationType === 'view'" :span="12">
                <el-form-item :rules="[{ required: currentStep === 1, message: '请选择主管', trigger: 'blur' }]" label="主管："
                  prop="supervisorId">
                  <el-select v-model="form.supervisorId" :disabled="currentStep !== 1 || operationType === 'view'"
                    clearable filterable placeholder="请选择主管" size="small" style="width: 100%;">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.submitPerson }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.submitDate }}
            </el-col>
          </el-row>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="currentStep !== 0 && currentStep !== 3" @click="submitForm('3reject')">驳回</el-button>
        <el-button v-if="currentStep === 0" @click="submitForm('2save')">保存</el-button>
        <el-button v-if="currentStep !== 3" type="primary" @click="submitForm('1submit')">{{ currentStep === 0 ? '提交' :
          '通过'
          }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { dateFormat } from '@/utils/date'
import {
  exportPersonJobResponsibilities, personJobResponsibilitiesDelete,
  personJobResponsibilitiesSave,
  personJobResponsibilitiesSelect
} from "@/api/cnas/personal/personJobResponsibilities";
import { selectUserCondition } from "@/api/cnas/resourceDemand/facilitiesEnvironment/facilitiesAndEnvironment";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      userName: '',
      tableData: [],
      tableLoading: false,
      responsibleOptions: [],
      search: {
        size: 20,
        current: 1,
        total: 0
      },
      dialogVisible: false,
      currentStep: 0, // 步骤条显示第几步
      currentStepClick: 0, // 点击步骤条变化
      operationType: '',
      steps: ['提交', '任职人确认', '主管确认'],
      form: {
        postName: '', // 岗位名称
        jobObjective: '', // 工作目标
        jobResponsibilities: '', // 岗位职责
        incumbentId: '', // 任职人
        supervisorId: '' // 主管
      },
      rules: {
        postName: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
        jobObjective: [{ required: true, message: '请输入工作目标', trigger: 'blur' }],
        jobResponsibilities: [{ required: true, message: '请输入岗位职责', trigger: 'blur' }],
        incumbentId: [{ required: true, message: '请选择任职人', trigger: 'change' }],
        supervisorId: [{ required: true, message: '请选择主管', trigger: 'change' }]
      }
      // departId: 0
    };
  },
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
  mounted() {
    this.getPostList(this.departId);
  },
  methods: {
    addPost() {
      this.dialogVisible = true;
      this.form = {
        postName: '', // 岗位名称
        jobObjective: '', // 工作目标
        jobResponsibilities: '', // 岗位职责
        incumbentId: '', // 任职人
        supervisorId: '', // 操作人
        submitPerson: '', // 主管
        submitDate: '', // 日期
        currentStep: 0 // 日期
      };
      this.currentStep = 0;
      this.getUserList();
    },
    // 查询列表信息
    getPostList(userId) {
      this.search.userId = userId;
      const params = this.isDepartment ? {
        userName: this.userName,
        departmentId: this.search.userId,
        size: this.search.size,
        current: this.search.current,
      } : {
        userName: this.userName,
        userId: this.search.userId,
        size: this.search.size,
        current: this.search.current,
      }
      this.tableLoading = true
      personJobResponsibilitiesSelect(params).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records;
        this.search.total = res.data.total;
      }).catch(err => {
        this.tableLoading = false
      })
    },
    //提交表单
    async submitForm(saveState) {
      this.$refs.form.validate((valid) => {
        if (valid === true || saveState !== '1submit') {
          // 给当前环节设置创建人与时间
          let user = this.nickName;
          const dateTime = dateFormat(new Date());
          // 获取当前环节操作人与日期
          switch (this.currentStep) {
            case 0:
              this.form.submittingOperator = user.name;
              this.form.submittingDate = dateTime;
              break;
            case 1:
              this.form.incumbentOperator = user.name;
              this.form.incumbentDate = dateTime;
              break;
            case 2:
              this.form.supervisorOperator = user.name;
              this.form.supervisorDate = dateTime;
              break;
            default:
              break;
          }
          // 获取当前环节负责人
          switch (saveState === '3reject' ? this.currentStep - 1 : this.currentStep) {
            case 1:
              this.form.currentResponsible = this.form.submittingOperator;
              break;
            case 2:
              this.form.currentResponsible = this.form.incumbentOperator;
              break;
            default:
              break;
          }
          let currentStepAction;
          // 设置该操作判断是否为提交，保存，驳回，通过
          switch (saveState) {
            // 提交，通过
            case '1submit':
              currentStepAction = this.currentStep + 1;
              break;
            // 保存
            case '2save':
              currentStepAction = this.currentStep;
              break;
            // 驳回
            case '3reject':
              currentStepAction = this.currentStep - 1;
              break;
            default:
              break;
          }
          // 获取当前状态
          this.form.currentState = currentStepAction === 3 ? '关闭' : this.steps[currentStepAction];
          personJobResponsibilitiesSave(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功');
              this.getPostList(this.departId);
              this.dialogVisible = false;
            }
          });
        } else {
          let step = this.steps[this.currentStep];
          this.$message.warning(step + '  流程中有必填项未填！');
        }
      });
    },
    // 编辑
    handleViewClick(row, type) {
      this.operationType = type;
      this.getUserList();
      row.incumbentId = Number(row.incumbentId);
      this.form = { ...row };
      switch (row.currentState) {
        case '提交':
          this.currentStep = 0;
          break;
        case '任职人确认':
          this.currentStep = 1;
          this.form.submitPerson = row.submittingOperator;
          this.form.submitDate = row.submittingDate;
          break;
        case '主管确认':
          this.currentStep = 2;
          this.form.submitPerson = row.incumbentOperator;
          this.form.submitDate = row.incumbentDate;
          break;
        case '关闭':
          this.currentStep = 3;
          this.form.submitPerson = row.supervisorOperator;
          this.form.submitDate = row.supervisorDate;
          break;
        default:
          break;
      }
      this.form.currentState = this.currentStep;
      this.currentStepClick = this.currentStep === 3 ? 0 : this.currentStep
      console.log('this.form---', this.form);
      this.dialogVisible = true;
    },
    // 下载岗位职责
    downLoadPost(row) {
      exportPersonJobResponsibilities({ id: row.id }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, row.incumbentName + '-岗位职责' + '.docx');
      })
    },
    // 删除岗位职责
    deletePost(row) {
      this.$confirm('此操作将永久删除此数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        personJobResponsibilitiesDelete({ id: row.id }).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功');
            this.getPostList(this.departId);
          }
        });
      }).catch(() => {
        this.$message.error('删除失败');
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    refreshTable() {
      this.getPostList(this.departId);
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition({type: 2}).then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
    choiceStep(index) {
      this.currentStepClick = index;
    },
    handleSizeChange(val) {
      this.search.size = val;
      this.getPostList(this.departId);
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.getPostList(this.departId);
    }
  },
  watch: {
    departId: {
      handler(newId, oldId) {
        this.getPostList(newId);
      }
    }
  },
  computed: {
    ...mapGetters(['nickName'])
  },
};
</script>
<style scoped></style>
