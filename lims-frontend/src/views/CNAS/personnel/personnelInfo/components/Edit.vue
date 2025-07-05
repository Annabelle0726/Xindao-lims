<template>
  <div>
    <div class="page-header">
      <h4><span class="line"></span><span>培训与考核记录</span></h4>
      <div class="btns">
        <el-button size="small" type="primary" @click="submitForm(3)" v-if="this.currentRow.state !== 1">撤销</el-button>
        <el-button size="small" type="primary" @click="submitForm(0)" v-if="this.currentRow.state !== 1">提交</el-button>
        <el-button size="small" @click="$emit('goBack')">返回</el-button>
      </div>
    </div>
    <div class="form_title">
      <el-row>
        <el-col :span="7">
          <span class="form_label">培训内容：</span>
          <span> {{ trainingForm.trainingContent }} </span>
        </el-col>
        <el-col :span="5">
          <span class="form_label">状态：</span>
          <el-tag v-if="trainingForm.state === 1" type="success">已完成</el-tag>
          <el-tag v-if="trainingForm.state === 2" type="warning">待评价</el-tag>
          <el-tag v-if="trainingForm.state === 3" type="primary">未开始</el-tag>
        </el-col>
        <el-col :span="4">
          <span class="form_label">培训讲师：</span>
          <span> {{ trainingForm.trainingLecturerName }} </span>
        </el-col>
      </el-row>
      <el-row style="margin: 15px 0">
        <el-form>
          <el-row>
          <el-col :span="6">
            <el-form-item label="培训日期:">
              <el-date-picker v-model="trainingForm.openingTime" format="yyyy-MM-dd" :disabled="currentRow.state !== 3"
                              placeholder="选择日期" size="small" value-format="yyyy-MM-dd"
                              type="date" style="width: 60%"></el-date-picker>
            </el-form-item>
          </el-col>
            <el-col :span="7">
              <el-form-item label="培训地点">
                <el-input v-model="trainingForm.placeTraining" :disabled="currentRow.state !== 3" :rows="2" placeholder="请输入"
                          size="small" style="width: 60%" type="text"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="评价人">
                <el-select v-model="trainingForm.assessmentUserId" :disabled="currentRow.state !== 3" placeholder="请选择" size="small" style="width: 50%">
                  <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="考核方式">
                <el-input v-model="trainingForm.assessmentMethod" :disabled="currentRow.state !== 3" :rows="2" placeholder="请输入"
                          size="small" style="width: 79%" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="评价时间">
                <el-date-picker v-model="trainingForm.assessmentDate" :disabled="currentRow.state !== 2 || isDisabled"
                                type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" size="small"
                                placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          <el-col :span="12">
            <el-form-item label="本次培训综合评价">
              <el-input v-model="trainingForm.comprehensiveAssessment" :disabled="currentRow.state !== 2 || isDisabled" :rows="2" placeholder="请输入"
                        size="small" style="width: 68%" type="textarea"></el-input>
            </el-form-item>
          </el-col>
          </el-row>
        </el-form>
      </el-row>
    </div>
    <el-divider>人员详情</el-divider>
    <div>
      <div class="items_center">
        <span>姓名</span>
        <el-input v-model="userName" class="search" placeholder="请输入"
                  size="small"></el-input>
        <el-button size="small" type="primary" @click="getInfo">查询</el-button>
      </div>
      <div class="items_btn">
        <el-button :disabled="currentRow.state === 1" size="small" type="primary" @click="addPerson">新增人员</el-button>
        <el-button :disabled="currentRow.state === 1" size="small" @click="batchDelete">批量删除</el-button>
      </div>
      <el-table :data="trainingTableData" height="calc(100vh - 30em)" stripe style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="序号" type="index" width="60"></el-table-column>
        <el-table-column label="姓名" prop="userName"></el-table-column>
        <el-table-column label="工号" prop="account"></el-table-column>
        <el-table-column label="角色" prop="roleName"></el-table-column>
        <el-table-column label="电话号码" prop="phone"></el-table-column>
        <el-table-column label="考核结果" prop="examinationResults">
          <template v-slot="scope">
            <el-input v-model="scope.row.examinationResults" :disabled="currentRow.state === 1" clearable size="small" style="width: 100%" @blur="updatePersonResult(scope.row)"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog :visible.sync="selectUserDia" title="选择用户" width="70%">
      <div>
        <el-form :model="addUserTableInfo" ref="addUserTableInfo" size="small" :inline="true" label-position="left" label-width="100">
          <el-form-item label="用户名" prop="name">
            <el-input
              v-model="addUserTableInfo.name"
              clearable
              placeholder="请输入"
              size="small"
              @input="selectUserList"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="selectUserDia" class="body" style="height: 60vh;">
        <lims-table :tableData="tableData1" :column="column1"
                    :isSelection="true" :handleSelectionChange="selectMethod"
                    height="500" :tableLoading="tableLoading1"></lims-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="selectUserDia = false">取 消</el-button>
        <el-button type="primary" @click="selectUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import limsTable from "@/components/Table/lims-table.vue";
import {selectUserCondition} from "@/api/business/inspectionTask";
import {mapGetters} from "vuex";
import {
  newPersonnelAddedToTrainingRecords,
  outOfFocusPreservation, trainingAndAssessmentRecordsAdded, trainingAndAssessmentRecordsEvaluate,
  trainingAndAssessmentRecordsPage, deleteTrainingAndAssessmentRecords
} from "@/api/cnas/personal/personalTraining";

export default {
  name: 'Edit',
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable},
  props: {
    currentRow: {
      type: Object,
      default: () => {
        return {}
      }
    },
  },
  data() {
    // 这里存放数据
    return {
      isSelectedList: [], // 禁用的多选
      userName: undefined,
      trainingForm: {
        code: '111111',
        date: '2024-10-10',
      },
      trainingColumn: [
        {
          label: '姓名',
          prop: 'userName'
        },
        {
          label: '工号',
          prop: 'account'
        },
        {
          label: '角色',
          prop: 'roleName'
        },
        {
          label: '电话号码',
          prop: 'phone'
        },
        {
          label: '考核结果',
          prop: 'result'
        }
      ],
      trainingTableData: [],
      trainingLoading: false,
      isDisabled: false,
      selectUserDia: false, // 添加人员弹框
      tableData1: [],
      tableLoading1: false,
      column1: [
        {label: '姓名', prop: 'name'},
        {label: '账号', prop: 'account'},
        {label: '角色', prop: 'roleName'},
        {
          dataType: 'tag',
          label: '状态',
          prop: 'status',
          formatData: (params) => {
            if (params == 0) {
              return '启用'
            } else {
              return '停用'
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success'
            } else {
              return 'danger'
            }
          }
        },
        {label: '电话号码', prop: 'phone'},
      ],
      page1: {
        total:0,
        size:10,
        current:1
      },
      addUserTableInfo: {
        name: null,
        type: 2
      },
      multipleSelection: [],
      userList: [],
    };
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  mounted() {
    this.trainingForm = this.currentRow
    this.getInfo()
    this.getUserList()
    this.isDisabled = this.trainingForm.assessmentUserId !== this.userId
  },
  // 方法集合
  methods: {
    // 获取当前数据
    async getInfo() {
      this.trainingLoading = true
      await trainingAndAssessmentRecordsPage({
        trainingDetailedId: this.currentRow.id,
        userName: this.userName
      }).then(res => {
        if (res.code === 200) {
          this.trainingTableData = res.data
        }
        this.trainingLoading = false
      })
    },
    updatePersonResult(row) {
      outOfFocusPreservation(row).then(res => {
        this.$message.success("操作成功！")
      })
    },
    addPerson() {
      this.isSelectedList = this.trainingTableData.map(item => item.userId)
      this.selectUserDia = true;
      this.selectUserList()
    },
    selectUserList () {
      this.tableLoading1 = true
      selectUserCondition({...this.addUserTableInfo}).then(res => {
        this.tableLoading1 = false
        this.tableData1 = res.data
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 表格选择方法
    selectMethod(val) {
      this.multipleSelection = val
    },
    selectUser() {
      let selects = this.multipleSelection;
      if (selects.length == 0) {
        this.$message.error('未选择数据');
        return;
      }
      let list = []
      selects.forEach(a => {
        const obj = {
          courseId: this.currentRow.id,
          examinationResults: "",
          userId: a.id
        }
        list.push(obj)
      });
      newPersonnelAddedToTrainingRecords(list).then(res => {
        this.isSelectedList = []
        this.selectUserDia = false;
        this.getInfo()
      })
    },
    // 批量删除
    handleSelectionChange(list) {
      this.multipleSelection = list
    },
    batchDelete() {
      if (this.multipleSelection.length > 0) {
        let ids = this.multipleSelection.map(item => item.trainingRecordId).join(',')
        this.$confirm('是否确认删除所选择的数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteTrainingAndAssessmentRecords({ids: ids}).then(res => {
            if (res.code == 200) {
              this.$message.success('删除成功');
              this.getInfo()
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
      this.search.size = val;
      this.getInfo();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.getInfo();
    },
    /**
     * @desc 提交表单
     */
    async submitForm(status) {
      let state = this.currentRow.state
      if (this.trainingForm.assessmentUserId) {
        state = 2
      }
      if (this.trainingForm.comprehensiveAssessment) {
        state = 1
      }
      if (status === 3) {
        state = 3
      }
      let data = {
        assessmentMethod: this.trainingForm.assessmentMethod,
        openingTime: this.trainingForm.openingTime,
        placeTraining: this.trainingForm.placeTraining,
        comprehensiveAssessment: this.trainingForm.comprehensiveAssessment,
        trainingDetailedId: this.trainingForm.id,
        assessmentUserId: this.trainingForm.assessmentUserId,
        assessmentDate: this.trainingForm.assessmentDate,
        state: state
      }
      let code = {}
      if (state === 2) {
        code = await trainingAndAssessmentRecordsAdded(data)
      } else {
        code = await trainingAndAssessmentRecordsEvaluate(data)
      }
      this.isDisabled = this.trainingForm.assessmentUserId !== this.userId
      if(code.code === 200) {
        this.currentRow.state = state
        this.$message.success("操作成功")
      }
    },
    getUserList(){
      selectUserCondition({ type: 2 }).then((res) => {
        this.userList = res.data;
      })
    },
  }
};
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  margin-bottom: 10px;
}

h4 {
  display: flex;
  align-items: center;
}

h4 .line {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: #3A7BFA;
  margin-right: 4px;
}

.form_title {
  font-size: 14px;
  padding: 0 20px;
  margin-bottom: 10px;
}


.items_center {
  float: left;
  width: 50%;
  text-align: left;
}
.items_btn {
  text-align: right;
  width: 50%;
  float: right;
  margin-bottom: 1em;
}
</style>
