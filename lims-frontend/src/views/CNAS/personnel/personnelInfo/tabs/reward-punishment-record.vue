<!-- 奖惩记录 -->
<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">姓名</span>
          <el-input size="small" placeholder="请输入" clearable v-model="search.userName"
                    @keyup.enter.native="getPersonnelTraining(departId)"></el-input>
        </div>
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">奖惩日期</span>
          <el-date-picker v-model="search.searchTimeList" :picker-options="pickerOptions" align="right" clearable
                          @change="getPersonnelTraining(departId)"
                          end-placeholder="结束日期" format="yyyy-MM-dd" range-separator="至" size="small" start-placeholder="开始日期"
                          style="width: 100%" type="daterange" unlink-panels value-format="yyyy-MM-dd 00:00:00">
          </el-date-picker>
        </div>
        <div style="line-height: 30px;">
          <el-button size="mini" type="primary" @click="getPersonnelTraining(departId)">查询</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button :loading="outLoading" size="small" type="primary" @click="handleDown">导出</el-button>
        <el-button size="small" type="primary" @click="addRow">新增</el-button>
      </div>
    </div>
    <div class="table">
      <el-table :data="tableData" v-loading="tableLoading"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                height="66.5vh" style="width: 100%">
        <el-table-column label="序号" type="index" width="120">
          <template v-slot="scope">
            <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="员工编号" min-width="180" prop="account">
        </el-table-column>
        <el-table-column label="姓名" min-width="180" prop="userName">
        </el-table-column>
        <el-table-column label="奖惩级别" min-width="180" prop="rewardPunishLevel">
        </el-table-column>
        <el-table-column label="奖惩时间" min-width="180" prop="rewardPunishTime">
        </el-table-column>
        <el-table-column label="奖惩名称" min-width="180" prop="rewardPunishName">
        </el-table-column>
        <el-table-column label="奖惩具体内容" min-width="120" prop="rewardPunishContent">
        </el-table-column>
        <el-table-column label="奖惩单位" min-width="180" prop="rewardPunishWorkUnit">
        </el-table-column>
        <el-table-column label="创建人" min-width="180" prop="createUserName">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100" align="center">
          <template v-slot="scope">
            <el-button size="small" type="text" @click="editForm(scope.row)">编辑</el-button>
            <el-button size="small" type="text" style="color: #f56c6c" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" background style="margin-top: 10px"
        @size-change="handleSizeChange" @current-change="handleCurrentChange">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="dialogVisible" title="奖惩记录" width="50%" @open="getUserList">
      <div style="height: 40vh">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-col :span="12">
            <el-form-item label="员工编号">
              <el-input v-model="form.account" disabled size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工姓名" prop="userId">
              <el-select v-model="form.userId" placeholder="请选择" size="small" style="width: 100%" value-key="id"
                @change="selectUserChange" :disabled="!isDepartment">
                <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖惩级别" prop="rewardPunishLevel">
              <el-input v-model="form.rewardPunishLevel" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖惩名称" prop="rewardPunishName">
              <el-input v-model="form.rewardPunishName" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖惩时间" prop="rewardPunishTime">
              <el-date-picker v-model="form.rewardPunishTime" format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                size="small" style="width: 100%" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖惩单位" prop="rewardPunishWorkUnit">
              <el-input v-model="form.rewardPunishWorkUnit" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="奖惩内容">
              <el-input v-model="form.rewardPunishContent" :rows="2" size="small" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addOrUpdateRewardPunishment, deleteRewardPunishment,
  rewardPunishmentExport,
  rewardPunishmentPage
} from "@/api/cnas/personal/personRewardPunishmentRecord";
import { selectUserCondition } from "@/api/system/user";
import { delCustomById } from "@/api/system/customer";

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
  data() {
    return {
      tableData: [],
      tableLoading: false,
      search: {
        size: 20,
        current: 1,
        total: 0,
        userName: '',
        searchTimeList: []
      },
      form: {},
      dialogVisible: false,
      outLoading: false,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      rules: {
        userId: [{
          required: true, message: '请选择员工', trigger: 'change'
        }],
        rewardPunishLevel: [{
          required: true, message: '请输入奖惩级别', trigger: 'blur'
        }],
        rewardPunishName: [{
          required: true, message: '请输入奖惩名称', trigger: 'blur'
        }],
        rewardPunishTime: [{
          required: true, message: '请输入奖惩时间', trigger: 'blur'
        }],
        rewardPunishWorkUnit: [{
          required: true, message: '请输入奖惩单位', trigger: 'blur'
        }]
      },
      responsibleOptions: []
    };
  },
  mounted() {
    this.getPersonnelTraining(this.departId);
  },
  methods: {
    handleSizeChange(val) {
      this.search.size = val
      this.getPersonnelTraining(this.departId);
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getPersonnelTraining(this.departId);
    },
    async getPersonnelTraining() {
      const params = {
        userId: this.isDepartment ? '' : this.departId,
        departmentId: this.isDepartment ? this.departId : '',
        current: this.search.curent,
        size: this.search.size,
        userName: this.search.userName,
        startTime: this.search.searchTimeList && this.search.searchTimeList[0],
        endTime: this.search.searchTimeList && this.search.searchTimeList[1],
      }
      this.tableLoading = true
      rewardPunishmentPage(params).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.search.total = res.data.total
      }).catch(err => {
        this.tableLoading = false
      })
    },
    addRow() {
      this.dialogVisible = true
      if (!this.isDepartment) {
        this.form.userId = this.departId
        this.selectUserChange(this.form.userId)
      }
    },
    handleDown() {
      this.outLoading = true
      rewardPunishmentExport({
        userId: this.isDepartment ? '' : this.departId,
        departmentId: this.isDepartment ? this.departId : '',
        userName: this.search.userName,
        startTime: this.search.searchTimeList && this.search.searchTimeList[0],
        endTime: this.search.searchTimeList && this.search.searchTimeList[1]
      }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], {
          type: 'application/force-download'
        })
        this.$download.saveAs(blob, '奖惩记录.xlsx')
      })
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition({type: 2}).then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      });
    },
    selectUserChange(val) {
      const index = this.responsibleOptions.findIndex(item => item.id === val)
      if (index > -1) {
        this.form.userName = this.responsibleOptions[index].name
        this.form.account = this.responsibleOptions[index].account
      }
    },
    // 打开表单弹框
    editForm(row) {
      this.dialogVisible = true
      this.form = { ...row };
    },
    // 提交表单数据
    saveOrUpdate() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          addOrUpdateRewardPunishment(this.form).then(res => {
            this.dialogVisible = false
            this.$message.success("操作成功")
            this.getPersonnelTraining(this.departId);
          })
        }
      })
    },
    deleteRow(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteRewardPunishment({ id: row.id }).then(res => {
          if (res.code === 500) {
            return
          }
          this.$message.success('删除成功')
          this.getPersonnelTraining(this.departId);
        }).catch(e => {
          this.$message.error('删除失败')
        })
      }).catch(() => { })

    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    departId: {
      handler(newId, oldId) {
        this.getPersonnelTraining(newId);
      }
    },
    dialogVisible(newVal) {
      if (newVal === false) {
        this.form = {}
        this.$refs['form'].resetFields()
      }
    }
  }
};
</script>
<style scoped>
.dateTime >>>.el-form-item__content {
  width: 260px;
}
</style>
